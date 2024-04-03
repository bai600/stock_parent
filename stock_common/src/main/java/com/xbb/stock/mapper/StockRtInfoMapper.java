package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.StockRtInfo;

/**
 * @Entity com.xbb.stock.pojo.entity.StockRtInfo
 */
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

}
