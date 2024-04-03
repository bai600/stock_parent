package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.StockBusiness;

/**
 * @Entity com.xbb.stock.pojo.entity.StockBusiness
 */
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

}
