package com.xbb.stock.job;

import com.xbb.stock.pojo.vo.TaskThreadPoolInfo;
import com.xbb.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义股票相关数据的定时任务
 */
@Component
public class StockJob {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @XxlJob("job_test")
    public void jobTest(){
        System.out.println("jobTest run.....");
    }

    @XxlJob("getStockInfos")
    public void getStockInfos(){
        stockTimerTaskService.getStockRtIndex();
    }

    @XxlJob("getInnerMarketInfos")
    public void getInnerMarketInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    @XxlJob("getStockBlockInfos")
    public void getStockBlockInfos(){
        stockTimerTaskService.getStockBlockInfo();
    }

}