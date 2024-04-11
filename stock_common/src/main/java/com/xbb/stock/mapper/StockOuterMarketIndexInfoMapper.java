package com.xbb.stock.mapper;

import com.xbb.stock.pojo.domain.OuterMarketDomain;
import com.xbb.stock.pojo.entity.StockBlockRtInfo;
import com.xbb.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Entity com.xbb.stock.pojo.entity.StockOuterMarketIndexInfo
 */
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);
    /**
     * 根据外盘的id和时间查询外盘信息
     * @param outerCodes 外盘id集合
     * @param curDate 当前时间点（默认精确到分钟）
     * @return
     */
    List<OuterMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("outerCodes") List<String> outerCodes);

    /**
     *批量插入外盘信息
     */
    int insertBatch(@Param("list") List<StockOuterMarketIndexInfo> list);
}
