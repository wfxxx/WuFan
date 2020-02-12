package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketCateVO;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.definesys.mpaas.common.http.Response;

@RestController
@RequestMapping(value = "/dsgc/mkt")
public class MarketController {
    @Autowired
    private MarketService marketService;

    /**
     * 新增市场分类
     */
    @RequestMapping(value = "/addMarketCate", method = RequestMethod.POST)
    public Response addMarketCate(@RequestBody MarketCateVO MarketCateVO) {
        this.marketService.addMarketCate(MarketCateVO);
        return Response.ok();
    }

    /**
     * 获取市场分类
     *
     * @return
     */
    @RequestMapping(value = "/getAllMarketCate", method = RequestMethod.POST)
    public Response getAllMarketCate(@RequestBody SVCInfoQueryBean q ,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return Response.ok().data(this.marketService.getAllMarketCate(q,pageSize,pageIndex));
    }

    /**
     * 删除市场分类
     *
     * @param MarketCateVO
     * @return
     */
    @RequestMapping(value = "/deleteMarketCate", method = RequestMethod.POST)
    public Response deleteMarketCate(@RequestBody MarketCateVO MarketCateVO) {
        this.marketService.deleteMarketCate(MarketCateVO);
        return Response.ok();
    }

    /**
     * 修改市场分类
     *
     * @param MarketCateVO
     * @return
     */
    @RequestMapping(value = "/updateMarketCate", method = RequestMethod.POST)
    public Response updateMarketCate(@RequestBody MarketCateVO MarketCateVO) {
        this.marketService.updateMarketCate(MarketCateVO);
        return Response.ok();
    }

    /**
     * 获取发布服务
     *
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping(value = "/getAllMarketPub", method = RequestMethod.POST)
    public Response getAllMarketPub(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<MarketApiBean> allMarketPub = marketService.getAllMarketPub(pageSize, pageIndex);
        return Response.ok().data(allMarketPub);
    }
}
