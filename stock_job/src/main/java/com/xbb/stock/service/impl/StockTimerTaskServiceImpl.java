package com.xbb.stock.service.impl;


import com.google.common.collect.Lists;
import com.xbb.stock.constant.ParseType;
import com.xbb.stock.mapper.StockBlockRtInfoMapper;
import com.xbb.stock.mapper.StockBusinessMapper;
import com.xbb.stock.mapper.StockMarketIndexInfoMapper;
import com.xbb.stock.mapper.StockRtInfoMapper;
import com.xbb.stock.pojo.entity.StockBlockRtInfo;
import com.xbb.stock.pojo.entity.StockMarketIndexInfo;
import com.xbb.stock.pojo.entity.StockRtInfo;
import com.xbb.stock.pojo.vo.StockInfoConfig;
import com.xbb.stock.service.StockTimerTaskService;
import com.xbb.stock.utils.DateTimeUtil;
import com.xbb.stock.utils.IdWorker;
import com.xbb.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.xml.ws.spi.http.HttpExchange;
import javax.xml.ws.spi.http.HttpHandler;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("stockTimerTaskService")
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 必须保证对象无状态
     */
    private HttpEntity<Object> httpEntity;

    @Override
    public void getInnerMarketInfo() {
        //定义采集的url接口
        String url = stockInfoConfig.getMarketUrl() + String.join(",",stockInfoConfig.getInner());
//              //组装请求头
//            HttpHeaders headers = new HttpHeaders();
//            //防盗链
//            headers.add("Referer","https://finance.sina.com.cn/stock/");
//            //用户客户端标识
//            headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//            //维护http请求实体对象
//            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue !=200) {
            //当前请求失败
            log.info("当前时间点：{}，采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH-mm-ss"), statusCodeValue);
            return;
        }
        //获取js格式数据
        String jsData = responseEntity.getBody();
        //java正则解析原始数据
        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(jsData);
        List<StockMarketIndexInfo> entities=new ArrayList<>();
        while (matcher.find()) {
            String marketCode=matcher.group(1);
            String otherInfo=matcher.group(2);
            //以逗号切割字符串，形成数组
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //封装实体类
            StockMarketIndexInfo entity = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .curPoint(curPoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeAmount(tradeAmt)
                    .tradeVolume(tradeVol)
                    .curTime(curTime)
                    .build();
            entities.add(entity);
        }
        log.info("解析大盘数据完毕！！！");
        //插入数据库，批量入库
        int counts=stockMarketIndexInfoMapper.insertBatch(entities);
        if (counts>0) {
            //大盘数据完毕后，通知backend工程刷新缓存
            //发送的日期数据,接收方通过接收的日期与当前日期对比，能判断出是否超时
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
            log.info("当前时间：{}，插入大盘数据：{}成功！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),entities);
        }else {
            log.info("当前时间：{}，插入大盘数据：{}失败！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),entities);
        }
    }

    @Override
    public void getStockRtIndex() {
        //批量获取股票ID集合
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        //添加大盘业务前缀 sz sh
        List<String> allCodes = allStockCode.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        //分组
        Lists.partition(allCodes,15).forEach(codes->{
            String url = stockInfoConfig.getMarketUrl() + String.join(",",codes);
//            //组装请求头
//            HttpHeaders headers = new HttpHeaders();
//            //防盗链
//            headers.add("Referer","https://finance.sina.com.cn/stock/");
//            //用户客户端标识
//            headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//            //维护http请求实体对象
//            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            //发起请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            int statusCodeValue = responseEntity.getStatusCodeValue();
            if (statusCodeValue !=200) {
                //当前请求失败
                log.info("当前时间点：{}，采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH-mm-ss"), statusCodeValue);
                return;
            }
            //获取js格式数据
            String jsData = responseEntity.getBody();
            //调用工具类解析数据
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            log.info("采集个股数据：{}",list);
            //批量插入
            int counts=stockRtInfoMapper.insertBatch(list);
            if (counts>0) {
                log.info("当前时间：{}，插入个股数据：{}成功！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),list);
            }else {
                log.info("当前时间：{}，插入个股数据：{}失败！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),list);
            }

        });
    }

    @Override
    public void getStockBlockInfo() {
        //定义采集的url接口
        String url = stockInfoConfig.getBlockUrl();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue !=200) {
            //当前请求失败
            log.info("当前时间点：{}，采集数据失败，http状态码：{}", DateTime.now().toString("yyyy-MM-dd HH-mm-ss"), statusCodeValue);
            return;
        }
        //获取js格式数据
        String jsData = responseEntity.getBody();
        //调用工具类解析数据
        List<StockBlockRtInfo> list = parserStockInfoUtil.parse4StockBlock(jsData);
        log.info("采集板块数据：{}",list);
        //批量插入
        int counts=stockBlockRtInfoMapper.insertBatch(list);
        if (counts>0) {
            log.info("当前时间：{}，插入板块数据：{}成功！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),list);
        }else {
            log.info("当前时间：{}，插入板块数据：{}失败！",DateTime.now().toString("yyyy-MM-dd HH-mm-ss"),list);
        }
    }

    /**
     * bean生命周期的初始化调用方法
     */
    @PostConstruct
    public void initData(){
        //组装请求头
        HttpHeaders headers = new HttpHeaders();
        //防盗链
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        //用户客户端标识
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        //维护http请求实体对象
        httpEntity = new HttpEntity<>(headers);
    }
}
