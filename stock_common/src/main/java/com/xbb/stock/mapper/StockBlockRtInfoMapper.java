package com.xbb.stock.mapper;

import com.xbb.stock.pojo.entity.StockBlockRtInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.xbb.stock.pojo.entity.StockBlockRtInfo
 */

public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

}
