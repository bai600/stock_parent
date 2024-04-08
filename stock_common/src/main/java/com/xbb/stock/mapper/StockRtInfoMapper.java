package com.xbb.stock.mapper;

import com.xbb.stock.pojo.domain.Stock4EvrDayDomain;
import com.xbb.stock.pojo.domain.Stock4MinuteDomain;
import com.xbb.stock.pojo.domain.StockUpdownDomain;
import com.xbb.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    /**
     * 查询指定时间点下股票的数据，并按照涨幅降序排序
     */
    List<StockUpdownDomain> getStockInfoByTime(@Param("curDate") Date curDate);

    /**
     * 查询指定时间范围内每分钟涨停或者跌停的数量
     * flag:1:涨停; 0：跌停
     */
    List<Map> getStockUpdownCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("flag") int flag);

    /**
     * 统计指定时间点下，各个涨跌区间内股票的个数
     */
    List<Map> getStockIncreaseRangeInfoByDate(@Param("curDate") Date curDate);

    /**
     * 根据时间范围查询指定股票的分时交易流水
     */
    List<Stock4MinuteDomain> getStock4MinuteInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("code")String code);
    /**
     * 查询指定日期范围内指定股票每天的交易数据
     */
    List<Stock4EvrDayDomain> getStock4EvrDayInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("code")String code);

    int insertBatch(@Param("list") List<StockRtInfo> list);
}
