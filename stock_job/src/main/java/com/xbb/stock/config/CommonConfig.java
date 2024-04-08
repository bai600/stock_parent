package com.xbb.stock.config;

import com.xbb.stock.pojo.vo.StockInfoConfig;
import com.xbb.stock.utils.IdWorker;
import com.xbb.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StockInfoConfig.class)//开启常用参数配置bean
public class CommonConfig {
    /**
     * 配置基于雪花算法生成全局唯一id
     *   参与元算的参数： 时间戳 + 机房id + 机器id + 序列号
     *   保证id唯一
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        //指定当前为1号机房的2号机器生成
        return new IdWorker(1L,2L);
    }
    /**
     * 配置解析工具bean
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(){
        return new ParserStockInfoUtil(idWorker());
    }
}  