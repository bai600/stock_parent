package com.xbb.stock.service;

import com.xbb.stock.pojo.domain.*;
import com.xbb.stock.vo.resp.PageResult;
import com.xbb.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface StockService {

    R<List<InnerMarketDomain>> getInnerMarketInfo();

    R<List<StockBlockDomain>> sectorAllLimit();

    R<PageResult<StockUpdownDomain>> getStockInfoPage(Integer page,Integer pageSize);

    R<List<StockUpdownDomain>> getStockInfoLimit4();

    R<Map<String, List>> getStockUpdownCount();

    void exportStockUpdownInfo(Integer page, Integer pageSize, HttpServletResponse response);

    R<Map<String, List>> getCompareStockTradeAmt();

    R<Map> getStockIncreaseRangeInfo();

    R<List<Stock4MinuteDomain>> getstockScreenTimeSharing(String code);

    R<List<Stock4EvrDayDomain>> getStockScreenDKline(String code);

    R<List<OuterMarketDomain>> getOuterMarketInfo();

    R<List<Map>> searchStockCodeandName(String searchStr);

    R<Map> getStockDescribe(String code);

    R<StockRtDomain> getStockDetail(String code);

    R<List<Map>> getStockRunningTab(String code);

    R<List<Stock4WeekDomain>> getStockScreenWeekKline(String code);

}
