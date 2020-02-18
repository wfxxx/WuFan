package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiMngService {
    @Autowired
    private ApiMngDao apiMngDao;

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
