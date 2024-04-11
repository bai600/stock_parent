package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<String> getAllStockCode();

    List<Map> getStockCodeandName(@Param("searchStr") String searchStr);

    Map getStockDescribe(@Param("code") String code);
}
