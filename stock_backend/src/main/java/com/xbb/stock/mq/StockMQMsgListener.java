package com.xbb.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.xbb.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.xml.crypto.Data;

/**
 * 监听股票变化的mq消息
 */
@Component
@Slf4j
public class StockMQMsgListener {
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockService stockService;

    @RabbitListener(queues = "innerMarketQueue")
    public void refreshInnerMarketInfo(Data startTime){
        //获取当前时间和发送消息时间毫秒差值，超过一分钟，则警告
        long diffTime= DateTime.now().getMillis()-new DateTime(startTime).getMillis();
        if (diffTime>60000l) {
            log.error("大盘发送消息时间：{},延迟：{}ms",new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        //刷新缓存
        //剔除旧数据
        caffeineCache.invalidate("innerMarkeKey");
        //调用服务方法，刷新数据
        stockService.getInnerMarketInfo();
    }
}
