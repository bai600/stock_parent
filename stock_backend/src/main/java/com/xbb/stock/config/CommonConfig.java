package com.xbb.stock.config;

import com.xbb.stock.pojo.vo.StockInfoConfig;
import com.xbb.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author
 * @Date
 * @Description 定义公共配置类
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class}) //开启相关配置对象加载
public class CommonConfig {
    /**
     * 密码加密器
     * BCryptPasswordEncoder方法采用SHA-256对密码进行加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IdWorker idWorker(){
        /**
         * 雪花算法确保唯一性
         * 配置id生成器bean,参数机器ID和机房ID
         * ID基于运维人员对机房和机器的编号规划自行约定
         */
        return new IdWorker(1l,2l);
    }
}