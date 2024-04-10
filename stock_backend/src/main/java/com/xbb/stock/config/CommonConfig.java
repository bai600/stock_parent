package com.xbb.stock.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xbb.stock.pojo.vo.StockInfoConfig;
import com.xbb.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.image.DataBuffer;
import java.text.SimpleDateFormat;

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

    /**
     * 统一定义Long序列化转String设置（所有的Long序列号成String）
     */
//    @Bean
//    public MappingJackson2CborHttpMessageConverter mappingJackson2CborHttpMessageConverter(){
//        //启动http信息转换对象
//        MappingJackson2CborHttpMessageConverter converter = new MappingJackson2CborHttpMessageConverter();
//        ObjectMapper objectMapper=new ObjectMapper();
//        //反序列化忽略位置属性
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//        SimpleModule simpleModule=new SimpleModule();
//        //Long类型转String设置
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
//        //注册转换器
//        objectMapper.registerModule(simpleModule);
//        //设置序列号实现
//        converter.setObjectMapper(objectMapper);
//        return converter;
//
//    }


}