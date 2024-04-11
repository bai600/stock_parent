package com.xbb.stock.controller;

import com.xbb.stock.pojo.domain.*;
import com.xbb.stock.service.StockService;
import com.xbb.stock.vo.resp.PageResult;
import com.xbb.stock.vo.resp.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags = "股票相关接口处理器")
@RestController
@RequestMapping("/api/quot")
public class StockController {

    @Autowired
    private StockService stockService;
    /**
     *需求说明: 获取国内大盘最新数据
     */
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
        return stockService.getInnerMarketInfo();
    }

    /**
     *需求说明: 获取外盘最新数据
     */
    @GetMapping("/external/index")
    public R<List<OuterMarketDomain>> getOuterMarketInfo(){
        return stockService.getOuterMarketInfo();
    }

    /**
     *需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     */
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAllLimit(){
        return stockService.sectorAllLimit();
    }

    /**
     * 需求说明: 分页查询股票最新数据，并按照涨幅排序查询
     */
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockInfoPage(@RequestParam(name = "page",required = false,defaultValue = "1")Integer page,
                                                             @RequestParam(name = "pageSize",required = false,defaultValue = "20")Integer pageSize){
        return stockService.getStockInfoPage(page,pageSize);
    }

    /**
     * 需求说明: 统计沪深两市个股最新交易数据，并按涨幅降序排序查询前4条数据
     */
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockInfoLimit4(){
        return stockService.getStockInfoLimit4();
    }

    /**
     * 需求说明: 统计最新交易日下股票每分钟涨跌停的数量
     */
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpdownCount(){
        return stockService.getStockUpdownCount();
    }

    /**
     * 需求说明: 将指定页的股票涨幅数据导出到excel表下
     */
    @GetMapping("/stock/export")
    public void stockExport(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,
                            @RequestParam(name = "pageSize",required = false,defaultValue = "20") Integer pageSize,
                            HttpServletResponse response){
        stockService.exportStockUpdownInfo(page,pageSize,response);
    }
    /**
     * 需求说明: 统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     */
    @GetMapping("/stock/tradeAmt")
    public R<Map<String,List>> getCompareStockTradeAmt(){
        return stockService.getCompareStockTradeAmt();
    }

    /**
     * 需求说明:查询当前时间下股票(A股)的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     */
    @GetMapping("/stock/updown")
    public R<Map> getStockIncreaseRangeInfo(){
        return stockService.getStockIncreaseRangeInfo();
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     */
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(@RequestParam(value = "code",required = true) String code){
        return stockService.getstockScreenTimeSharing(code);
    }

    /**
     * 单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     */
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDKline(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockScreenDKline(code);
    }

    /**
     *  统计20周内每周内的股票数据信息，信息包含：
     * 	股票ID、 一周内最高价、 一周内最低价 、周1开盘价、周5的收盘价、
     * 	整周均价、以及一周内最大交易日期（一般是周五所对应日期）;
     */
    @GetMapping("/stock/screen/weekkline")
    public R<List<Stock4WeekDomain>> getStockScreenWeekKline(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockScreenWeekKline(code);
    }

    /**
     * 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     */
    @GetMapping("/stock/search")
    public R<List<Map>> searchStockCodeandName(String searchStr){
        return stockService.searchStockCodeandName(searchStr);
    }

    /**
     * 个股主营业务查询接口
     */
    @GetMapping("/stock/describe")
    public R<Map> getStockDescribe(String code){
        return stockService.getStockDescribe(code);
    }

    /**
     * 获取个股最新分时行情数据，主要包含：
     * 	开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息
     * @param code 股票编码
     * @return
     */
    @RequestMapping("/stock/screen/second/detail")
    public R<StockRtDomain> getStockDetail(String code){
        return stockService.getStockDetail(code);
    }

    /**
     * 个股交易流水行情数据查询--查询最新交易流水，按照交易时间降序取前10
     * @param code
     * @return
     */
    @RequestMapping("/stock/screen/second")
    public R<List<Map>> getStockRunningTab(String code){
        return stockService.getStockRunningTab(code);
    }
}
