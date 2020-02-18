package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.APIMngInfoListBean;
import com.definesys.dsgc.service.apimng.bean.CommonReqBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiMngService {
    @Autowired
    private ApiMngDao apiMngDao;

    public PageQueryResult<APIMngInfoListBean> queryApiMngList(CommonReqBean param, int pageIndex, int pageSize, String userId, String userRole){
        if ("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)){
                List<DSGCSystemUser> dsgcSystemUsers =   apiMngDao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            PageQueryResult<APIMngInfoListBean> result = apiMngDao.queryApiMngList(param, pageIndex, pageSize, userId, userRole,sysCodeList);
            return result;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApiBasicInfo(ApiBasicInfoDTO param){
        apiMngDao.saveApiBasicInfo(param);
    }

    public ApiBasicInfoDTO queryBasicInfoByApiCode(String apiCode){
       DSGCApisBean dsgcApisBean = apiMngDao.queryBasicInfoByApiCode(apiCode);
       ApiBasicInfoDTO apiBasicInfoDTO = new ApiBasicInfoDTO();
       if (dsgcApisBean != null){
           apiBasicInfoDTO.setApiCode(dsgcApisBean.getApiCode());
           apiBasicInfoDTO.setApiDesc(dsgcApisBean.getApiDesc());
           apiBasicInfoDTO.setApiName(dsgcApisBean.getApiName());
           apiBasicInfoDTO.setSubSystem(dsgcApisBean.getAppCode());
       }
       return apiBasicInfoDTO;
    }
}
