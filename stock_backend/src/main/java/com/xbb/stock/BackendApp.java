package com.xbb.stock;

import com.xbb.stock.pojo.vo.StockInfoConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.xbb.stock.mapper")
public class BackendApp {
    public static void main(String[] args) {

        SpringApplication.run(BackendApp.class,args);
    }
}
