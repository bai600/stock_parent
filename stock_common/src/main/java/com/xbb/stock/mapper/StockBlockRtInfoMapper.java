package com.xbb.stock.mapper;

import com.xbb.stock.pojo.domain.StockBlockDomain;
import com.xbb.stock.pojo.domain.StockUpdownDomain;
import com.xbb.stock.pojo.entity.StockBlockRtInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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

    List<StockBlockDomain> sectorAllLimit(Date curDate);

    List<StockUpdownDomain> getStockInfoLimit4(Date curDate);

    int insertBatch(@Param("list") List<StockBlockRtInfo> list);

}
