package com.xbb.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 个股股票domain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockRtDomain {
    /**
     * 股票代码
     */
    private String code;
    /**
     * 前收盘价| 昨日收盘价
     */
    private BigDecimal preClosePrice;
    /**
     * 开盘价
     */
    private BigDecimal openPrice;
    /**
     * 当前价格
     */
    private BigDecimal tradePrice;
    /**
     * 今日最低价
     */
    private BigDecimal lowPrice;
    /**
     * 今日最高价
     */
    private BigDecimal highPrice;
    /**
     * 成交量
     */
    private Long tradeAmt;
    /**
     * 成交金额
     */
    private BigDecimal tradeVol;
    /**
     * 当前时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curDate;
}