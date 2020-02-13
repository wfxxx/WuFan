package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketCateVO;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.dsgc.service.market.bean.MarketQueryBean;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MarketService {
    @Autowired
    private MarketDao marketDao;

    public void addMarketCate(MarketCateVO marketcateVO) {
        marketDao.addMarketCate(marketcateVO);
    }

    public void deleteMarketCate(MarketCateVO marketcateVO) {
        marketDao.deleteMarketCate(marketcateVO);
    }

    public void updateMarketCate(MarketCateVO marketcateVO) {
        marketDao.updateMarketCate(marketcateVO);
    }

    public PageQueryResult<MarketCateVO> getAllMarketCate(MarketQueryBean q, int pageSize, int pageIndex) {
        return marketDao.getAllMarketCate(q,pageSize,pageIndex);
    }

    public PageQueryResult<MarketApiBean> getAllMarketPub(MarketQueryBean q, int pageSize, int pageIndex) {
        return marketDao.getAllMarketPub(q,pageSize, pageIndex);
    }

    public void updateMarketPub(MarketApiBean marketApiBean){
        marketDao.updateMarketPub(marketApiBean);
    }

    //条件获取Esb服务
    public PageQueryResult<MarketEntiy> getMarketEsb(Map<String,Object> mapValue, int pageSize, int pageIndex){
        if(mapValue.get("apply")!=null){
            String userId= (String) mapValue.get("apply");
            String[] servNoList=queryAppliedEsb(userId);
            mapValue.put("apply",servNoList);
        }
        return  this.marketDao.queryEsb(mapValue,pageSize,pageIndex);
    }
    //条件获取Api服务
    public PageQueryResult<MarketEntiy> getMarketApi(Map<String,Object> mapVlue, int pageSize, int pageIndex){
        if(mapVlue.get("apply")!=null){
            String userId= (String) mapVlue.get("apply");
            String[] servNoList=queryAppliedApi(userId);
            mapVlue.put("apply",servNoList);
        }
        return  this.marketDao.queryApi(mapVlue,pageSize,pageIndex);
    }


    public List<Map<String,Object>> getClassType(String sourceType){
        String parentType="API_CATE";
        switch(sourceType){
            case "apiSource":
                parentType="API_CATE";
                return this.marketDao.getApiClassType(parentType);
            case "servSource":
                parentType="SVC_CATE";
                return this.marketDao.getEsbClassType(parentType);
            default:
                return null;
        }
    }
    /**
     * 查询市场服务总数与用户申请总数，
     *  return
     * @author 薛云龙
     * @time 2020.2.10
     */
    public Map<String,Object> getPowerType(String sourceType,String userId){
        Map<String,Object> result=new HashMap<String,Object>();
        Object allTotal=0;
        Object applyTotal=0;
        switch(sourceType){
            case "apiSource":
                List<Map<String,Object>> apiTotal=this.marketDao.queryApiTotal();
                if(apiTotal.size()>0){
                    allTotal= (apiTotal.get(0).get("TOTAL"));
                }
                Map<String,Object> mapApi=new HashMap<String,Object>();
                mapApi.put("apply",userId);
                PageQueryResult<MarketEntiy> apiList= getMarketApi(mapApi,12,1);
                applyTotal=apiList.getCount()==null?0:apiList.getCount();
                break;
            case "servSource":
                List<Map<String,Object>> esbTotal=this.marketDao.queryEsbTotal();
                if(esbTotal.size()>0){
                    allTotal= (esbTotal.get(0).get("TOTAL"));
                }
                Map<String,Object> mapEsb=new HashMap<String,Object>();
                mapEsb.put("apply",userId);
                PageQueryResult<MarketEntiy> esbList= getMarketApi(mapEsb,12,1);
                applyTotal=esbList.getCount()==null?0:esbList.getCount();
                break;
            default:
        }
        result.put("allTotal",allTotal);
        result.put("applyTotal",applyTotal);
        return result;
    }

    /**
     * 查询市场用户申请的esb服务，返回servNo集合
     *  return
     * @author 薛云龙
     * @time 2020.2.11
     */
    private String[] queryAppliedEsb(String userId){
        List<Map<String,Object>>  servNoList=this.marketDao.queryAppliedEsb(userId);
        List<String> servNoResult=new ArrayList();
        for( Map<String,Object> value:servNoList){
            servNoResult.add((String) value.get("SERVNO"));
        }
        return  servNoResult.toArray(new String[servNoResult.size()]);
    }
    /**
     * 查询市场用户申请的api服务，返回servNo集合
     *  return
     * @author 薛云龙
     * @time 2020.2.10
     */
    private  String[] queryAppliedApi(String userId){
        List<Map<String,Object>>  servNoList=this.marketDao.queryAppliedApi(userId);
        List<String> servNoResult=new ArrayList();
        for( Map<String,Object> value:servNoList){
            servNoResult.add((String) value.get("SERVNO"));
        }
        return  servNoResult.toArray(new String[servNoResult.size()]);
    }
}
