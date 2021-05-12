package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.market.bean.*;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.definesys.mpaas.common.http.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/dsgc/mkt")
public class MarketController {
    @Autowired
    private MarketService marketService;

    /**
     * 新增市场分类
     */
    @RequestMapping(value = "/addMarketCate", method = RequestMethod.POST)
    public Response addMarketCate(@RequestBody MarketCateVO marketCateVO) {
        this.marketService.addMarketCate(marketCateVO);
        return Response.ok();
    }

    /**
     * 获取市场分类
     *
     * @return
     */
    @RequestMapping(value = "/getAllMarketCate", method = RequestMethod.POST)
    public Response getAllMarketCate(@RequestBody MarketQueryBean q ,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return Response.ok().data(this.marketService.getAllMarketCate(q,pageSize,pageIndex));
    }

    /**
     * 删除市场分类
     *
     * @param marketCateVO
     * @return
     */
    @RequestMapping(value = "/deleteMarketCate", method = RequestMethod.POST)
    public Response deleteMarketCate(@RequestBody MarketCateVO marketCateVO) {
        this.marketService.deleteMarketCate(marketCateVO);
        return Response.ok();
    }

    /**
     * 修改市场分类
     *
     * @param marketCateVO
     * @return
     */
    @RequestMapping(value = "/updateMarketCate", method = RequestMethod.POST)
    public Response updateMarketCate(@RequestBody MarketCateVO marketCateVO) {
        this.marketService.updateMarketCate(marketCateVO);
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
    public Response getAllMarketPub(@RequestBody MarketQueryBean q ,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<MarketApiBean> allMarketPub = marketService.getAllMarketPub(q,pageSize, pageIndex);
        return Response.ok().data(allMarketPub);
    }

    /***
     * 发布服务
     * @param marketApiBean
     * @return
     */
    @RequestMapping(value = "/updateMarketPub", method = RequestMethod.POST)
    public Response updateMarketPub(@RequestBody MarketApiBean marketApiBean) {
        this.marketService.updateMarketPub(marketApiBean);
        return Response.ok();
    }

    /**
     * 条件查询市场的api服务
     *
     * @author 薛云龙
     * @time 2020.2.10
     */
    @PostMapping("getMarketApi")
    private Response getMarketApi(@RequestBody Map<String,Object> mapValue,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "12") int pageSize,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        Map<String,Object> mapValue1=new HashMap<String,Object>();
        //搜索框查询的条件
        if(mapValue.get("searchValue")!=null){
            String searchValue= (String) mapValue.get("searchValue");
            mapValue1.put("searchValue",searchValue);
        }
        //查询权限的条件--查询全体还是分类查询
        if(mapValue.get("powerType")!=null){
            String powerType= (String) mapValue.get("powerType");
            String classType=null;
            String classConditions=null;
            switch (powerType){
                case "all"://查询全部
                    break;
                case "apply"://查询已申请
                    if(mapValue.get("userId")!=null){
                        String userId= (String) mapValue.get("userId");
                        mapValue1.put("apply",userId);
                    }
                    break;
                default://默认查询类别
                    classConditions= powerType;
                    mapValue1.put("classConditions",powerType);
            }
        }
        return Response.ok().data(marketService.getMarketApi(mapValue1,pageSize,pageIndex));
    }

    /**
     * 条件查询市场的esb服务
     *
     * @author 薛云龙
     * @time 2020.2.10
     */
    @PostMapping("getMarketEsb")
    public Response getMarketEsb(@RequestBody Map<String,Object> mapValue,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "12") int pageSize,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        Map<String,Object> mapValue1=new HashMap<String,Object>();
        //搜索框查询的条件
        if(mapValue.get("searchValue")!=null){
            String searchValue= (String) mapValue.get("searchValue");
            mapValue1.put("searchValue",searchValue);
        }
        //查询权限的条件--查询全体还是分类查询
        if(mapValue.get("powerType")!=null){
            String powerType= (String) mapValue.get("powerType");
            String classType=null;
            String classConditions=null;
            switch (powerType){
                case "all"://查询全部
                    break;
                case "apply"://查询已申请
                    if(mapValue.get("userId")!=null){
                        String userId= (String) mapValue.get("userId");
                        mapValue1.put("apply",userId);
                    }
                    break;
                default://默认查询类别
                    classConditions= powerType;
                    mapValue1.put("classConditions",powerType);
            }
        }
        return Response.ok().data(marketService.getMarketEsb(mapValue1,pageSize,pageIndex));
    }




    /**
     * 查询市场界面分类的总数
     *
     * @author 薛云龙
     * @time 2020.2.10
     */
    @PostMapping("getPowerValue")
    public Response getPowerType(@RequestBody Map<String,Object> mapValue){
        String userId=null;
        String sourceType=null;
        if(mapValue.get("userId")!=null){
            userId= (String) mapValue.get("userId");
        }
        if(mapValue.get("sourceType")!=null){
            sourceType= (String) mapValue.get("sourceType");
        }
        if(mapValue.get("sourceType")!=null){
            sourceType= (String) mapValue.get("sourceType");
        }
        if(userId==null||sourceType==null){
            return Response.error("参数不完整");
        }
        return Response.ok().data(this.marketService.getPowerType(sourceType,userId));
    }


    /**
     * 查询市场分类集合
     *
     * @author 薛云龙
     * @time 2020.2.10
     */
    @GetMapping("/getClassType")
    public Response getClassType( @RequestParam(value = "sourceType") String sourceType){
        List<Map<String,Object>> result= this.marketService.getClassType(sourceType);
        if(result==null){
            return Response.error("参数错误");
        }
        return Response.ok().data(result);
    }

    /**
     * 集成服务详情查看页面查询api
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryServBaseInfo",method = RequestMethod.POST)
    public Response queryServBaseInfo(@RequestBody MarketQueryBean param){
        SrevDetailInfoDTO result = marketService.queryServBaseInfo(param.getCon0());
    return Response.ok().setData(result);
    }

    /**
     * API详情查看页面查询api
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryApiBaseInfo",method = RequestMethod.POST)
    public Response queryApiBaseInfo(@RequestBody MarketQueryBean param){
        ApiDetailInfoDTO result = marketService.queryApiBaseInfo(param.getCon0());
        return Response.ok().setData(result);
    }

}
