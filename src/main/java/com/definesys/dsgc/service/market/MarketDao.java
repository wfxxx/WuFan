package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.market.bean.*;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.definesys.mpaas.query.MpaasQuery;

import java.util.List;
import java.util.Map;


@Repository
@Transactional
public class MarketDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void addMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .doInsert(marketcateVO);
    }

    public void deleteMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .eq("mcId", marketcateVO.getMcId())
                .doDelete(marketcateVO);
    }

    public void updateMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .eq("mcId", marketcateVO.getMcId())
                .doUpdate(marketcateVO);
    }

    public PageQueryResult<MarketCateVO> getAllMarketCate(MarketQueryBean q, int pageSize, int pageIndex) {
        MpaasQuery query = sw.buildQuery().sql("select * from (select y.mc_id,y.cate_code,y.cate_name,y.cate_order,y.CREATION_DATE,(select s.MEANING from FND_LOOKUP_VALUES s where y.PARENT_CATE = s.LOOKUP_CODE) PARENT_CATE from DSGC_MARKET_CATEGORY y) order by CREATION_DATE desc ");
        if(!"ALL".equals(q.getShareType().trim())){
            String shareTypNoSpace = q.getShareType().trim();
            query.and().eq("parentCate",shareTypNoSpace);
        }
        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String con : conArray) {
                if (con != null && con.trim().length() > 0) {
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("cateName", conNoSpace)
                            .likeNocase("cateOrder", conNoSpace)
                            .likeNocase("parentCate", conNoSpace);
                }
            }
        }
        return query.doPageQuery(pageIndex, pageSize, MarketCateVO.class);
    }

    public PageQueryResult<MarketApiBean> getAllMarketPub(MarketQueryBean q, int pageSize, int pageIndex) {
        MpaasQuery query = sw.buildQuery()
                .sql("select t.API_ID,\n" +
                        "       t.API_NAME,\n" +
                        "       t.APP_CODE,\n" +
                        "       t.API_DESC,\n" +
                        "       t.MARKET_CATEGORY,\n" +
                        "       t.MARKET_STAT,\n" +
                        "       t.type,\n" +
                        "       y.CATE_NAME,\n" +
                        "       t.creation_Date\n" +
                        "                               \n" +
                        "from (select API_ID, API_NAME, API_DESC, APP_CODE, MARKET_CATEGORY, MARKET_STAT,creation_Date, 'API' type\n" +
                        "      from DSGC_APIS\n" +
                        "      UNION ALL\n" +
                        "      select SERV_ID            API_ID,\n" +
                        "             SERV_NAME          API_NAME,\n" +
                        "             SERV_DESC          API_DESC,\n" +
                        "             SUBORDINATE_SYSTEM APP_CODE,\n" +
                        "             MARKET_CATEGORY,\n" +
                        "             MARKET_STAT,\n" +
                        "             creation_Date,\n" +
                        "             '集成服务'             type\n" +
                        "      from DSGC_SERVICES) t\n" +
                        "       left join DSGC_MARKET_CATEGORY y on t.MARKET_CATEGORY = y.CATE_CODE");
        if(!"ALL".equals(q.getShareType().trim())){
            String shareTypNoSpace = q.getShareType().trim();
            query.and().eq("type",shareTypNoSpace);
        }
        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String con : conArray) {
                if (con != null && con.trim().length() > 0) {
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("apiName", conNoSpace)
                            .likeNocase("apiDesc", conNoSpace)
                            .likeNocase("type",conNoSpace)
                            .likeNocase("appCode", conNoSpace)
                            .likeNocase("cateName", conNoSpace);

                }
            }
        }
        query.orderBy("creationDate","desc");

        return query.doPageQuery(pageIndex, pageSize, MarketApiBean.class);
    }

    public void updateMarketPub(MarketApiBean marketApiBean){
        if ( "API".equals(marketApiBean.getType())){
            sw.buildQuery()
                    .eq("apiId",marketApiBean.getApiId())
                    .doUpdate(marketApiBean);
        }else {
            sw.buildQuery()
                    .eq("SERV_ID",marketApiBean.getApiId())
                    .update("MARKET_CATEGORY",marketApiBean.getMarketCategory())
                    .update("MARKET_STAT",marketApiBean.getMarketStat())
                    .table("dsgc_services")
                    .doUpdate();
        }
    }

    public PageQueryResult<MarketEntiy> queryEsb(Map<String,Object> mapVlue, int pageSize, int pageIndex) {

        MpaasQuery query=sw.buildQuery().sql("select * from (\n" +
                "select t.serv_id servId,t.serv_no servNo,t.serv_name servName,t.serv_desc servDesc,e.sys_name fromSys,\n" +
                "                t.market_stat marketStat ,t.market_category  marketCategory,t.creation_date creationDate,(case when r.total_times >1 then r.total_times  else 0 end ) totalTimes \n" +
                "                from DSGC_SERVICES t \n" +
                "                left join rp_serv_total r on t.serv_no=r.serv_no \n" +
                "                 left join dsgc_system_entities e on t.subordinate_system=e.sys_code )");

        if(mapVlue.get("searchValue")!=null) {
            String searchValue = (String) mapVlue.get("searchValue");
            query.or().like("servNo", searchValue).like("servName", searchValue).conjuctionAnd().and();
        }
        if(mapVlue.get("apply")!=null){
            String[] applyServNo= (String[]) mapVlue.get("apply");
            if(applyServNo.length==0){
                return new PageQueryResult<MarketEntiy>();
            }
            query.in("servNo",applyServNo);
        }
        if(mapVlue.get("classConditions")!=null){
            String classConditions= (String) mapVlue.get("classConditions");
            query.and().eq("marketCategory",classConditions);
        }
        query.and().eq("marketStat","Y");
        return query.doPageQuery(pageIndex,pageSize,MarketEntiy.class);
    }

    public PageQueryResult<MarketEntiy> queryApi(Map<String,Object> mapVlue, int pageSize, int pageIndex) {

        MpaasQuery query=sw.buildQuery().sql("select * from (select t.api_id servId,t.api_code servNo,t.api_name servName,e.sys_name fromSys,t.api_desc servDesc,\n" +
                "t.market_category marketCategory,t.market_stat marketStat,t.creation_date creationDate ,(case when r.total_times >1 then r.total_times  else 0 end )\n" +
                "totalTimes from DSGC_APIS t \n" +
                "left join rp_serv_total r   on t.api_code=r.serv_no\n" +
                " left join dsgc_system_entities e   on t.app_code=e.sys_code)");

        if(mapVlue.get("searchValue")!=null){
            String searchValue= (String) mapVlue.get("searchValue");
            query.or().like("servNo",searchValue).like("servName",searchValue).conjuctionAnd().and();
        }

        if(mapVlue.get("classConditions")!=null){
            String classConditions= (String) mapVlue.get("classConditions");
            query.and().eq("marketCategory",classConditions);
        }
        if(mapVlue.get("apply")!=null){
            String[] applyServNo= (String[]) mapVlue.get("apply");
            if(applyServNo.length==0){
                return new PageQueryResult<MarketEntiy>();
            }
            query.in("servNo",applyServNo);
        }
        query .eq("marketStat","Y");
        return query.doPageQuery(pageIndex,pageSize,MarketEntiy.class);
    }

    public List<Map<String,Object>> queryAppliedEsb(String userId) {
        return  sw.buildQuery().sql("select sa.serv_no servNo from dsgc_consumer_users cu,dsgc_system_access sa\n" +
                " where cu.csm_code=sa.sys_code and cu.user_id=#userId  group by sa.serv_no").setVar("userId",userId).doQuery();

    }

    public List<Map<String,Object>>  queryAppliedApi(String userId) {
        return sw.buildQuery().sql("select aa.api_code servNo from DSGC_CONSUMER_USERS cu,dsgc_apis_access aa\n" +
                "where cu.csm_code=aa.csm_code and cu.user_id=#userId group by aa.api_code").setVar("userId",userId).doQuery();

    }

    public List<Map<String,Object>> getEsbClassType(String sourceType) {
        return sw.buildQuery().sql("select c.cate_code,c.cate_name,count( CASE WHEN s.market_stat='Y' THEN 1 ELSE NULL END ) TOTAL from dsgc_market_category c left join dsgc_services s on s.market_category=c.cate_code\n" +
                "where c.parent_cate=#sourceType \n" +
                "group by c.cate_code,c.cate_name")
                .setVar("sourceType",sourceType)
                .doQuery();
    }
    public List<Map<String,Object>> getApiClassType(String sourceType) {
        return sw.buildQuery().sql("select c.cate_code,c.cate_name,count( CASE WHEN s.market_stat='Y' THEN 1 ELSE NULL END ) TOTAL from dsgc_market_category c left join dsgc_apis s on s.market_category=c.cate_code\n" +
                "where c.parent_cate=#sourceType \n" +
                "group by c.cate_code,c.cate_name")
                .setVar("sourceType",sourceType)
                .doQuery();
    }

    public List<Map<String,Object>> queryApiTotal() {
        return sw.buildQuery().sql("select  count(1) total from dsgc_apis t where t.market_stat='Y' ")
                .doQuery();
    }

    public List<Map<String,Object>> queryEsbTotal() {
        return sw.buildQuery().sql("select  count(1) total from dsgc_services t where t.market_stat='Y'")
                .doQuery();
    }
    public DSGCApisBean queryApiByApiCode(String apiCode){
        return sw.buildQuery()
                .eq("api_code",apiCode)
                .doQueryFirst(DSGCApisBean.class);
    }
    public List<DSGCEnvInfoCfg> queryApiEnv(){
        return sw.buildQuery().eq("env_type","DAG").doQuery(DSGCEnvInfoCfg.class);
    }
    public List<DSGCEnvInfoCfg> queryEsbEnv(){
        return sw.buildQuery().eq("env_type","ESB").doQuery(DSGCEnvInfoCfg.class);
    }
    public List<DSGCServicesUri> queryServUri(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQuery(DSGCServicesUri.class);
    }
}
