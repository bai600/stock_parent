package com.xbb.stock;

import com.google.common.collect.Lists;
import com.xbb.stock.mapper.StockBusinessMapper;
import com.xbb.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestStockTimerService {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 获取大盘数据
     */
    @Test
    public void test01(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 获取A股数据
     */
    @Test
    public void test02(){
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        System.out.println(allStockCode);
        //添加大盘业务前缀 sz sh
        List<String> allCodes = allStockCode.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        System.out.println(allCodes);
        //分组
        Lists.partition(allCodes,15).forEach(codes->{
            System.out.println("Size:"+codes.size()+"+"+codes);

        });
    }

    @Test
    public void testInnerGetMarketInfo(){
        stockTimerTaskService.getStockRtIndex();
    }

    @Test
    public void testgetStockBlockInfo(){
        stockTimerTaskService.getStockBlockInfo();
    }

    @Test
    public void testgetOuterMarketInfo(){
        stockTimerTaskService.getOuterMarketInfo();
    }
}
