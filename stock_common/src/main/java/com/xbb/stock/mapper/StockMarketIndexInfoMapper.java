package com.xbb.stock.mapper;

import com.xbb.stock.pojo.domain.InnerMarketDomain;
import com.xbb.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Entity com.xbb.stock.pojo.entity.StockMarketIndexInfo
 */
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);
    /**
     * 根据大盘的id和时间查询大盘信息
     * @param marketCodes 大盘id集合
     * @param curDate 当前时间点（默认精确到分钟）
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("marketCodes") List<String> marketCodes);

    /**
     * 根据时间范围和指定的大盘id统计每分钟的交易量
     */
    List<Map> getSumStockTradeAmt(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("marketCodes")List<String> inner);
    /**
     * 批量插入股票大盘数据
     */
    int insertBatch(@Param("entities") List<StockMarketIndexInfo> entities);
}
