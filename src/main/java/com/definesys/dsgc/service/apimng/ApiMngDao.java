package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Repository
public class  ApiMngDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<DSGCApisBean> queryApiMngList(CommonReqBean q, int pageIndex, int pageSize, String userId, String userRole, List<String> sysCodeList) {
        MpaasQuery query = sw.buildQuery()
                .sql("select * from (select da.API_CODE,da.API_NAME,da.API_DESC,(select dse.SYS_NAME from DSGC_SYSTEM_ENTITIES dse where dse.SYS_CODE = da.APP_CODE) APP_CODE,da.APP_CODE sys_code,da.INFO_FULL,dsu.PROVIDER,dsu.HTTP_METHOD,dsu.IB_URI,da.CREATION_DATE from DSGC_APIS da left join DSGC_SERVICES_URI dsu on da.API_CODE = dsu.SERV_NO order by CREATION_DATE desc) t ");
        if ("Y".equals(q.getIsComplete())) {
            query.and()
                    .ne("infoFull", "100")
                    .isNotNull("infoFull");
        }
        if ("SystemLeader".equals(userRole)) {
            if (sysCodeList.size() != 0) {
                if (sysCodeList.size() <= 1) {
                    query.and().eq("sys_code", sysCodeList.get(0));
                } else {
                    query.and().in("sys_code", sysCodeList);
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
                            .likeNocase("appCode", conNoSpace)
                            .likeNocase("provider",conNoSpace)
                            .likeNocase("httpMethod",conNoSpace)
                            .likeNocase("ibUri",conNoSpace);
                }
            }
        }
        PageQueryResult<DSGCApisBean> result = query.doPageQuery(pageIndex, pageSize, DSGCApisBean.class);
        return result;
    }

    public List<DSGCSystemUser> findUserSystemByUserId(String userId) {
        return sw.buildQuery().eq("user_id", userId).doQuery(DSGCSystemUser.class);
    }

    public void saveApiBasicInfo(ApiBasicInfoDTO param) {
        sw.buildQuery()
                .eq("api_code", param.getApiCode())
                .update("api_name", param.getApiName())
                .update("api_desc", param.getApiDesc())
                .update("app_code", param.getSubSystem())
                .doUpdate(DSGCApisBean.class);
    }

    public DSGCApisBean queryBasicInfoByApiCode(String apiCode) {
        return sw.buildQuery().eq("api_code", apiCode).doQueryFirst(DSGCApisBean.class);
    }

    public PageQueryResult<DagRouteInfoBean> getRouteInfoList(CommonReqBean q, int pageIndex, int pageSize, String userRole, List<String> sysCodeList) {
        MpaasQuery query = sw.buildQuery().sql("select * from (\n" +
                "       select dr.DR_ID,dr.ROUTE_CODE,dr.ROUTE_PATH,dr.ROUTE_METHOD,dr.BS_CODE,\n" +
                "       (select SYS_NAME  from DSGC_SYSTEM_ENTITIES dse where dse.SYS_CODE = dr.APP_CODE) APP_CODE,dr.APP_CODE SYS_CODE from DAG_ROUTES dr\n" +
                "       order by dr.creation_date desc\n" +
                ") s ");
        if ("SystemLeader".equals(userRole)) {
            if (sysCodeList.size() != 0) {
                if (sysCodeList.size() <= 1) {
                    query.eq("appCode", sysCodeList.get(0));
                } else {
                    query.in("appCode", sysCodeList);
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
                            .likeNocase("routeCode", conNoSpace)
                            .likeNocase("routePath", conNoSpace)
                            .likeNocase("routeMethod", conNoSpace)
                            .likeNocase("bsCode",conNoSpace)
                            .likeNocase("appCode",conNoSpace);
                }
            }
        }
        PageQueryResult<DagRouteInfoBean> result = query.doPageQuery(pageIndex, pageSize, DagRouteInfoBean.class);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDsgcApi(DSGCApisBean dsgcApisBean){
        sw.buildQuery().doInsert(dsgcApisBean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDsgcUri(DSGCServicesUri dsgcServicesUri){
        sw.buildQuery().doInsert(dsgcServicesUri);
    }
}
