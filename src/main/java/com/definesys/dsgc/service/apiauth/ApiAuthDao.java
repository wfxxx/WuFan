package com.definesys.dsgc.service.apiauth;

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
                .sql("select * from (select da.API_CODE,da.API_NAME,(select dse.SYS_NAME from DSGC_SYSTEM_ENTITIES dse where da.APP_CODE = dse.SYS_CODE) APP_CODE,da.APP_CODE sys_code,da.INFO_FULL,daa.CSM_CODE from DSGC_APIS da,DSGC_APIS_ACCESS daa where da.APP_CODE = daa.API_CODE(+))\n");
        if(q.getSelectSystemList().size() != 0){
            if(q.getSelectSystemList().size()<=1){
                query.and().eq("csmCode",q.getSelectSystemList().get(0));
            }else {
                query.and().in("csmCode",q.getSelectSystemList());
            }
        }
        if ("SystemLeader".equals(userRole)) {
            if (sysCodeList.size() != 0) {
                if (sysCodeList.size() <= 1) {
                    query.eq("sysCode", sysCodeList.get(0));
                } else {
                    query.in("sysCode", sysCodeList);
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
        Map<String, Object> map = sw.buildQuery().sql("select count(*) count from (select daa.* from DSGC_APIS da,DSGC_APIS_ACCESS daa where da.API_CODE = daa.API_CODE and da.API_CODE = #apiCode)")
                .setVar("apiCode", apiCode).doQueryFirst();
        return map.get("COUNT").toString();

    }
}
