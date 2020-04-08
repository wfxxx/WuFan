package com.definesys.dsgc.service.apiauth;

import com.definesys.dsgc.service.apiauth.bean.APIAuthConsumersBean;
import com.definesys.dsgc.service.apiauth.bean.CommonReqBean;
import com.definesys.dsgc.service.apiauth.bean.DSGCApisAccess;
import com.definesys.dsgc.service.apiauth.bean.DSGCApisBean;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.svcAuth.bean.SVCHisBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class ApiAuthDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DSGCApisBean queryApiAuthDetailBaseInfo(String apiCode) {
        return sw.buildQuery().sql("select da.API_CODE,da.API_NAME,dse.SYS_NAME APP_CODE from DSGC_APIS da,dsgc_system_entities dse where da.APP_CODE = dse.sys_code and da.API_CODE = #apiCode")
                .setVar("apiCode", apiCode).doQueryFirst(DSGCApisBean.class);
    }

    public PageQueryResult<DSGCConsumerEntities> queryApiAuthConsumerList(CommonReqBean commonReqBean, int pageIndex, int pageSize){
        return sw.buildQuery().sql("select temp1.csm_code        csmCode,\n" +
                "       temp1.sysName        csmName,\n" +
                "       temp1.lastUpdateDate lastUpdateDate,\n" +
                "       du.user_name         lastUpdatedBy,\n" +
                "       temp1.daaId           attribue1\n" +
                "from (select dce.csm_code        csm_code,\n" +
                "             dce.csm_name        sysName,\n" +
                "             temp.lastUpdateDate lastUpdateDate,\n" +
                "             temp.lastUpdatedBy  lastUpdatedBy,\n" +
                "             temp.daaId           daaId\n" +
                "      from (select daa.CSM_CODE          csm_code,\n" +
                "                   daa.last_update_date lastUpdateDate,\n" +
                "                   daa.last_updated_by   lastUpdatedBy,\n" +
                "                   daa.DAA_ID             daaId\n" +
                "            from DSGC_APIS da,\n" +
                "                 DSGC_APIS_ACCESS daa\n" +
                "            where da.API_CODE = daa.API_CODE\n" +
                "              and da.API_CODE = #apiCode) temp,\n" +
                "           dsgc_consumer_entities dce\n" +
                "      where temp.csm_code = dce.csm_code) temp1,\n" +
                "     dsgc_user du\n" +
                "where temp1.lastUpdatedBy = du.user_id\n" +
                "order by lastUpdateDate desc")
                .setVar("apiCode",commonReqBean.getApiCode())
                .doPageQuery(pageIndex,pageSize, DSGCConsumerEntities.class);
    }

    public List<APIAuthConsumersBean> queryApiAuthConsumerListByVid(String vid) {
        if (vid != null) {
            Map<String,Object> vidDtl = sw.buildQuery().sql("select v.sour_code ,v.sour_type from dag_code_version v where v.vid = #vid").setVar("vid",vid).doQueryFirst();
            String stype = null;
            String sCode = null;
            if (vidDtl != null) {
                stype = vidDtl.get("SOUR_TYPE") == null ? null : vidDtl.get("SOUR_TYPE").toString();
                sCode = vidDtl.get("SOUR_CODE") == null ? null : vidDtl.get("SOUR_CODE").toString();
            }

            if (stype != null && sCode != null) {
                if ("route".equals(stype)) {
                    //找出所有该路由对应的api的已授权消费者
                    return sw.buildQuery().sql("select e.csm_code,e.csm_name from dsgc_apis_access a, dsgc_consumer_entities e \n" +
                            " where a.csm_code = e.csm_code \n" +
                            "   and a.API_CODE in (select p.API_CODE from DSGC_APIS p,DSGC_SERVICES_URI u,DAG_ROUTES r where p.API_CODE = u.SERV_NO and u.PROVIDER = r.ROUTE_CODE and r.ROUTE_CODE = #routeCode)").setVar("routeCode",sCode).doQuery(APIAuthConsumersBean.class);
                } else if ("bs".equals(stype)) {
                    //找出所有该后端服务产生的路由对应的api的已授权消费者
                    return sw.buildQuery().sql("select e.csm_code,e.csm_name from dsgc_apis_access a, dsgc_consumer_entities e\n" +
                            " where a.csm_code = e.csm_code\n" +
                            "   and a.API_CODE in (select p.API_CODE from DSGC_APIS p,DSGC_SERVICES_URI u,DAG_ROUTES r, dag_bs b where p.API_CODE = u.SERV_NO and u.PROVIDER = r.ROUTE_CODE and r.BS_CODE = b.BS_CODE and b.BS_CODE = #bsCode)").setVar("bsCode",sCode).doQuery(APIAuthConsumersBean.class);

                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    public List<APIAuthConsumersBean> queryApiAuthConsumerList(String apiCode){
        return sw.buildQuery().sql("select e.csm_code,e.csm_name from dsgc_apis_access a, dsgc_consumer_entities e where a.csm_code = e.csm_code and a.api_code = #apiCode").setVar("apiCode",apiCode).doQuery(APIAuthConsumersBean.class);
    }

    public Boolean checkConsumerEntitieIsExist(String csmCode){
        List<DSGCConsumerEntities> list = sw.buildQuery().eq("csmCode",csmCode).doQuery(DSGCConsumerEntities.class);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    public Boolean checkAccessSystemIsExist(CommonReqBean param){
        List<DSGCApisAccess> list =  sw.buildQuery().and()
                .eq("apiCode",param.getApiCode())
                .eq("csmCode",param.getSysCode())
                .doQuery(DSGCApisAccess.class);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    public void addServAuthSytem(DSGCApisAccess dsgcApisAccess){
        sw.buildQuery().doInsert(dsgcApisAccess);
    }

    public DSGCConsumerEntities queryConsumerByCode(String sysCode){
        return sw.buildQuery().eq("csmCode",sysCode).doQueryFirst(DSGCConsumerEntities.class);

    }

    public void addSvcHis(SVCHisBean svcHisBean){
        sw.buildQuery().doInsert(svcHisBean);
    }

    public DSGCApisAccess queryAccessSystem(String daaId){
        return  sw.buildQuery().eq("daaId",daaId).doQueryFirst(DSGCApisAccess.class);
    }

    public void deleteAccessSystem(String daaId){
        sw.buildQuery().eq("daaId",daaId).doDelete(DSGCApisAccess.class);
    }

    public List<DSGCSystemUser> findUserSystemByUserId(String userId) {
        return sw.buildQuery().eq("user_id", userId).doQuery(DSGCSystemUser.class);
    }

    public PageQueryResult<DSGCApisBean> queryApiAuthList(CommonReqBean q, int pageIndex, int pageSize, String userId, String userRole, List<String> sysCodeList) {
        MpaasQuery query = sw.buildQuery()
                .sql("select distinct api_code,api_name,APP_CODE,sys_code,creation_date from (select da.API_CODE,da.API_NAME,(select dse.SYS_NAME from DSGC_SYSTEM_ENTITIES dse where da.APP_CODE = dse.SYS_CODE) " +
                        "APP_CODE,da.APP_CODE sys_code,da.INFO_FULL,daa.CSM_CODE,da.creation_date from DSGC_APIS da left join DSGC_APIS_ACCESS daa on da.API_CODE = daa.API_CODE " +
                        "order by creation_date desc) s ");
        if(q.getSelectSystemList().size() != 0){
            if(q.getSelectSystemList().size()<=1){
                query.and().eq("csm_code",q.getSelectSystemList().get(0));
            }else {
                query.and().in("csm_code",q.getSelectSystemList());
            }
            query.orderBy("creation_date","desc");
        }
        if ("SystemLeader".equals(userRole)) {
            if (sysCodeList.size() != 0) {
                if (sysCodeList.size() <= 1) {
                    query.eq("sys_code", sysCodeList.get(0));
                } else {
                    query.in("sys_code", sysCodeList);
                }
            } else {
                return new PageQueryResult<>();
            }
        }
        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String con : conArray) {
                if (con != null && con.trim().length() > 0) {
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("apiCode", conNoSpace)
                            .likeNocase("apiName", conNoSpace)
                            .likeNocase("appCode", conNoSpace);
                }
            }
        }
        PageQueryResult<DSGCApisBean> result = query.doPageQuery(pageIndex, pageSize, DSGCApisBean.class);
        return result;
    }

    public String queryAuthSystemCount(String apiCode) {
        Map<String, Object> map = sw.buildQuery().sql("select count(*) COUNT from (select daa.* from DSGC_APIS da,DSGC_APIS_ACCESS daa where da.API_CODE = daa.API_CODE and da.API_CODE = #apiCode) t")
                .setVar("apiCode", apiCode).doQueryFirst();
        return map.get("COUNT").toString();
    }

    public List<DSGCApisAccess> checkAPIAuthIsExist(String apiCode,List<String> customerList){
        return sw.buildQuery().sql("select * from （SELECT a.api_code,a.csm_code  ,e.csm_name    FROM DSGC_APIS_ACCESS a\n" +
                "left join  dsgc_consumer_entities  e on e.csm_code=a.csm_code） ")
                .in("csm_code",customerList)
                .eq("api_code",apiCode)
                .doQuery(DSGCApisAccess.class);
    }
}
