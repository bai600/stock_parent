package com.xbb.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xbb.stock.mapper")
public class BackendApp {
    public static void main(String[] args) {

        SpringApplication.run(BackendApp.class,args);
    }
}