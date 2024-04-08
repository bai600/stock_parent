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
    @RequestMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockScreenDKline(@RequestParam(value = "code",required = true) String code){
        return stockService.getStockScreenDKline(code);
    }
}
