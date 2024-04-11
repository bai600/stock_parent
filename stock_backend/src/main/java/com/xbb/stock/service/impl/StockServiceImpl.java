package com.xbb.stock.service.impl;

import cn.hutool.json.ObjectMapper;
import com.alibaba.excel.EasyExcel;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xbb.stock.mapper.*;
import com.xbb.stock.pojo.domain.*;
import com.xbb.stock.pojo.entity.SysPermission;
import com.xbb.stock.pojo.entity.SysRole;
import com.xbb.stock.pojo.entity.SysUser;
import com.xbb.stock.pojo.vo.StockInfoConfig;
import com.xbb.stock.service.StockService;
import com.xbb.stock.utils.DateTimeUtil;
import com.xbb.stock.vo.resp.PageResult;
import com.xbb.stock.vo.resp.R;
import com.xbb.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service("stockService")
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        /**
         * 获取国内大盘最新数据
         */
        //默认从本地缓存中加载数据，如果不存在，则数据库加载获取数据，并存入本地缓存
        //开盘周期内，本地缓存默认有效期1分钟
        R<List<InnerMarketDomain>> result= (R<List<InnerMarketDomain>>) caffeineCache.get("innerMarkeKey", key->{
            //1 获取最新交易时间点精确到分钟
            Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
            //mock mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
            curDate= DateTime.parse("2022-01-02 09:31:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            //2 获取大盘编码集合
            List<String> innerCodes = stockInfoConfig.getInner();
            //3 调用mapper查询数据
            List<InnerMarketDomain> data=stockMarketIndexInfoMapper.getMarketInfo(curDate,innerCodes);
            //4 封装并响应
            return R.ok(data);
        });
        return result;
    }

    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {
        //获取股票最新交易时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock数据,后续删除
        curDate=DateTime.parse("2021-12-21 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.调用mapper接口获取数据
        List<StockBlockDomain> data=stockBlockRtInfoMapper.sectorAllLimit(curDate);
        //2.组装数据
        if (CollectionUtils.isEmpty(data)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(data);
    }

    @Override
    public R<PageResult<StockUpdownDomain>> getStockInfoPage(Integer page,Integer pageSize) {
        //1 获取最新交易时间点精确到分钟
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock数据,后续删除
        curDate=DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2设置PageHelper分页参数
        PageHelper.startPage(page,pageSize);
        //3 调用mapper查询
        List<StockUpdownDomain> pageData = stockRtInfoMapper.getStockInfoByTime(curDate);
        //4 组装PageResult对象
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getStockInfoLimit4() {
        //获取最新交易时间点精确到分钟
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock数据,后续删除
        curDate=DateTime.parse("2021-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //调用mapper查询
        List<StockUpdownDomain> data=stockBlockRtInfoMapper.getStockInfoLimit4(curDate);
        return R.ok(data);
    }

    @Override
    public R<Map<String, List>> getStockUpdownCount() {
        //1.1 获取最新股票交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock mock数据,后续删除
        curDateTime= DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endTime = curDateTime.toDate();
        //1.2 获取最新交易时间对应的开盘时间
        Date startTime= DateTimeUtil.getOpenDate(curDateTime).toDate();
        //2.查询涨停数据,约定mapper中flag入参： 1:涨停; 0：跌停
        List<Map> upList=stockRtInfoMapper.getStockUpdownCount(startTime,endTime,1);
        //3.查询跌停数据
        List<Map> downList=stockRtInfoMapper.getStockUpdownCount(startTime,endTime,0);
        //4.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("upList",upList);
        info.put("downList",downList);
        //5.返回结果
        return R.ok(info);
    }

    @Override
    public void exportStockUpdownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockInfoPage(page, pageSize);
        List<StockUpdownDomain> rows = r.getData().getRows();
        //将数据导出到excel
        //设置响应excel文件格式类型
        response.setContentType("application/vnd.ms-excel");
        //设置响应数据的编码格式
        response.setCharacterEncoding("utf-8");
        //设置默认的文件名称
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            //设置默认文件名称：兼容一些特殊浏览器
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(),StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前导出数据异常，当前页码：{},每页大小：{},当前时间：{},异常信息：{}",page,pageSize,DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),e.getMessage());
        }
    }

    @Override
    public R<Map<String, List>> getCompareStockTradeAmt() {
        //获取T日最新股票交易时间点
        DateTime tEndDataTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock mock数据,后续删除
        tEndDataTime= DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndTime = tEndDataTime.toDate();
        //获取T日最新交易时间对应的开盘时间
        Date tStartTime= DateTimeUtil.getOpenDate(tEndDataTime).toDate();
        //获取T-1日时间范围
        DateTime preTEndDataTime = DateTimeUtil.getPreviousTradingDay(tEndDataTime);
        //mock mock数据,后续删除
        preTEndDataTime= DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date preTEndTime = preTEndDataTime.toDate();
        Date preTStartTime= DateTimeUtil.getOpenDate(preTEndDataTime).toDate();
        //调用mapper查询
        List<Map> tData=stockMarketIndexInfoMapper.getSumStockTradeAmt(tStartTime,tEndTime,stockInfoConfig.getInner());
        List<Map> preTData=stockMarketIndexInfoMapper.getSumStockTradeAmt(preTStartTime,preTEndTime,stockInfoConfig.getInner());
        //组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList",tData);
        info.put("yesAmtList",preTData);
        return R.ok(info);
    }

    @Override
    public R<Map> getStockIncreaseRangeInfo() {
        //获取股票最新交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock mock数据,后续删除
        curDateTime=DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = curDateTime.toDate();
        //调用mapper查询
        List<Map> infos = stockRtInfoMapper.getStockIncreaseRangeInfoByDate(curDate);
        //获取有序的标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        List<Map> allInfos = new ArrayList<>();
        for (String title : upDownRange) {
            Map tmp=null;
            for (Map info : infos) {
                if (info.containsValue(title)) {
                    tmp=info;
                    break;
                }
                if (tmp==null){
                    tmp = new HashMap();
                    tmp.put("count",0);
                    tmp.put("title",title);
                }
            }
            allInfos.add(tmp);
        }
        //组装数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("time",curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",allInfos);
        return R.ok(data);
    }

    @Override
    public R<List<Stock4MinuteDomain>> getstockScreenTimeSharing(String code) {
        //获取股票最新交易时间点
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock mock数据,后续删除
        endDateTime=DateTime.parse("2021-12-30 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date startDate= DateTimeUtil.getOpenDate(endDateTime).toDate();
        List<Stock4MinuteDomain> data = stockRtInfoMapper.getStock4MinuteInfo(startDate,endDate,code);
        return R.ok(data);
    }

    @Override
    public R<List<Stock4EvrDayDomain>> getStockScreenDKline(String code) {
        //获取查询的日期范围
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //mock mock数据,后续删除
        endDateTime=DateTime.parse("2022-06-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date startDate = endDateTime.minusMonths(4).toDate();
        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStock4EvrDayInfo(startDate,endDate,code);
        return R.ok(data);
    }

    @Override
    public R<List<OuterMarketDomain>> getOuterMarketInfo() {
        /**
         * 获取外盘最新数据
         */
        //1 获取最新交易时间点精确到分钟
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate= DateTime.parse("2022-05-18 15:58:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2 获取大盘编码集合
        List<String> outerCodes = stockInfoConfig.getOuter();
        //3 调用mapper查询数据
        List<OuterMarketDomain> data= stockOuterMarketIndexInfoMapper.getMarketInfo(curDate,outerCodes);
        //4 封装并响应
        return R.ok(data);

    }

    @Override
    public R<List<Map>> searchStockCodeandName(String searchStr) {
        if (searchStr == null) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        List<Map> data = stockBusinessMapper.getStockCodeandName(searchStr);
        return R.ok(data);
    }

    @Override
    public R<Map> getStockDescribe(String code) {
        Map data = stockBusinessMapper.getStockDescribe(code);
        return R.ok(data);
    }

    @Override
    public R<StockRtDomain> getStockDetail(String code) {
        //获取最新交易时间点精确到分钟
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate= DateTime.parse("2021-12-30 09:32:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        StockRtDomain data=stockRtInfoMapper.getStockDetail(curDate,code);
        return R.ok(data);
    }

    @Override
    public R<List<Map>> getStockRunningTab(String code) {
        //获取最新交易时间点精确到分钟
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate= DateTime.parse("2021-12-30 10:21:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Map> data = stockRtInfoMapper.getStockRunningTab(code, curDate);
        return R.ok(data);
    }

    @Override
    public R<List<Stock4WeekDomain>> getStockScreenWeekKline(String code) {
        //获取最新交易时间点精确到分钟
        DateTime curTimeDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date curDate=curTimeDate.toDate();
        //mock mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        curDate= DateTime.parse("2022-01-05 13:11:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //前推20周
        Date pre20Week = curTimeDate.minusDays(140).toDate();
        //mock mock测试数据
        pre20Week= DateTime.parse("2021-12-30 10:21:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<Stock4WeekDomain> data = stockRtInfoMapper.getStockScreenWeekKline(code, pre20Week, curDate);
        return R.ok(data);
    }


}
