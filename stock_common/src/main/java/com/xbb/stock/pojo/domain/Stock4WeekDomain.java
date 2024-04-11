package com.xbb.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 个股日K数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4WeekDomain {
    /**
     * 日期，eg:202201280809
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date mxTime;
    /**
     * 股票编码
     */
    private String stockCode;
    /**
     * 一周内最低价
     */
    private BigDecimal minPrice;
    /**
     * 一周内最高价
     */
    private BigDecimal maxPrice;
    /**
     * 周一开盘价
     */
    private BigDecimal openPrice;
    /**
     * 周五收盘价
     */
    private BigDecimal closePrice;
    /**
     * 一周内平均价
     */
    private BigDecimal avgPrice;
}