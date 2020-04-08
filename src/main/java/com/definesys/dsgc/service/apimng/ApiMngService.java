package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
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

    public PageQueryResult<APIInfoListBean> queryApiMngList(CommonReqBean param, int pageIndex, int pageSize, String userId, String userRole) {
        if ("Tourist".equals(userRole)) {
            return new PageQueryResult<>();
        } else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)) {
                List<DSGCSystemUser> dsgcSystemUsers = apiMngDao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            PageQueryResult<DSGCApisBean> pageQueryResult = apiMngDao.queryApiMngList(param, pageIndex, pageSize, userId, userRole, sysCodeList);
            PageQueryResult<APIInfoListBean> result = new PageQueryResult<>();
            result.setCount(pageQueryResult.getCount());
            List<APIInfoListBean> list = new ArrayList<>();
            for (DSGCApisBean item : pageQueryResult.getResult()) {
                APIInfoListBean apiInfoListBean = apiMngBeanMapping(item);
                list.add(apiInfoListBean);
            }
            result.setResult(list);
            return result;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveApiBasicInfo(ApiBasicInfoDTO param) {
        apiMngDao.saveApiBasicInfo(param);
    }

    public ApiBasicInfoDTO queryBasicInfoByApiCode(String apiCode) {
        DSGCApisBean dsgcApisBean = apiMngDao.queryBasicInfoByApiCode(apiCode);
        ApiBasicInfoDTO apiBasicInfoDTO = new ApiBasicInfoDTO();
        if (dsgcApisBean != null) {
            apiBasicInfoDTO.setApiCode(dsgcApisBean.getApiCode());
            apiBasicInfoDTO.setApiDesc(dsgcApisBean.getApiDesc());
            apiBasicInfoDTO.setApiName(dsgcApisBean.getApiName());
            apiBasicInfoDTO.setSubSystem(dsgcApisBean.getAppCode());
        }
        return apiBasicInfoDTO;
    }

    public APIInfoListBean apiMngBeanMapping(DSGCApisBean dsgcApisBean) {
        APIInfoListBean apiInfoListBean = new APIInfoListBean();
        apiInfoListBean.setApiCode(dsgcApisBean.getApiCode());
        apiInfoListBean.setApiName(dsgcApisBean.getApiName());
        apiInfoListBean.setApiDesc(dsgcApisBean.getApiDesc());
        apiInfoListBean.setAppCode(dsgcApisBean.getAppCode());
        apiInfoListBean.setInfoFull(dsgcApisBean.getInfoFull());
        apiInfoListBean.setProvider(dsgcApisBean.getProvider());
        apiInfoListBean.setHttpMethod(dsgcApisBean.getHttpMethod());
        apiInfoListBean.setIbUri(dsgcApisBean.getIbUri());
        return apiInfoListBean;
    }

    public PageQueryResult<DagRouteInfoBean> getRouteInfoList(CommonReqBean q, int pageIndex, int pageSize,  String userId, String userRole) {
        if ("Tourist".equals(userRole)) {
            return new PageQueryResult<>();
        } else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)) {
                List<DSGCSystemUser> dsgcSystemUsers = apiMngDao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            return apiMngDao.getRouteInfoList(q, pageIndex, pageSize, userRole, sysCodeList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDsgcApi(DSGCApisBean dsgcApisBean, DSGCServicesUri dsgcServicesUri){
        apiMngDao.addDsgcApi(dsgcApisBean);
        apiMngDao.addDsgcUri(dsgcServicesUri);
    }

    public Boolean checkApiCodeIsExist(CommonReqBean param){
        Boolean isExist =  apiMngDao.checkApiCodeIsExist(param.getCon0());
        return isExist;
    }
}
