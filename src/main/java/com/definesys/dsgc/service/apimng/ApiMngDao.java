package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApiMngDao {
    @Autowired
    private MpaasQueryFactory sw;

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
