package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.APIMngInfoListBean;
import com.definesys.dsgc.service.apimng.bean.CommonReqBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;

@Repository
public class ApiMngDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<APIMngInfoListBean> queryApiMngList(CommonReqBean q, int pageIndex, int pageSize, String userId, String userRole, List<String> sysCodeList) {
        MpaasQuery query = sw.buildQuery()
                .sql("select * from DSGC_APIS");
        if ("Y".equals(q.getIsComplete())) {
            query.and()
                    .ne("infoFull", "100")
                    .isNotNull("infoFull");
        }
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
                            .likeNocase("apiCode", conNoSpace)
                            .likeNocase("apiName", conNoSpace)
                            .likeNocase("appCode", conNoSpace);
                }
            }
        }
        PageQueryResult<APIMngInfoListBean> result = query.doPageQuery(pageIndex, pageSize, APIMngInfoListBean.class);
        return result;
    }

    public List<DSGCSystemUser> findUserSystemByUserId(String userId) {
        return sw.buildQuery().eq("user_id", userId).doQuery(DSGCSystemUser.class);
    }

    public void saveApiBasicInfo(ApiBasicInfoDTO param){
        sw.buildQuery()
                .eq("api_code",param.getApiCode())
                .update("api_name",param.getApiName())
                .update("api_desc",param.getApiDesc())
                .update("app_code",param.getSubSystem())
                .doUpdate(DSGCApisBean.class);
    }
    public DSGCApisBean queryBasicInfoByApiCode(String apiCode){
       return sw.buildQuery().eq("api_code",apiCode).doQueryFirst(DSGCApisBean.class);
    }
}
