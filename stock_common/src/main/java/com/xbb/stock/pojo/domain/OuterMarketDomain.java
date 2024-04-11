package com.xbb.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 定义封装多外盘数据的领域对象
 */
@Data
public class OuterMarketDomain {
    /**
     * 外盘名称
     */
    private String name;
    /**
     * 当前点
     */
    private BigDecimal curPoint;
    /**
     * 涨跌值
     */
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    private BigDecimal rose;
    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}