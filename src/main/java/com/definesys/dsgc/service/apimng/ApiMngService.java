package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.APIInfoListBean;
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
        apiInfoListBean.setAppCode(dsgcApisBean.getAppCode());
        apiInfoListBean.setInfoFull(dsgcApisBean.getInfoFull());
        return apiInfoListBean;
    }

    public PageQueryResult<APIInfoListBean> queryApiAuthList(CommonReqBean param, int pageIndex, int pageSize, String userId, String userRole) {
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
            PageQueryResult<DSGCApisBean> pageQueryResult = apiMngDao.queryApiAuthList(param, pageIndex, pageSize, userId, userRole, sysCodeList);
            PageQueryResult<APIInfoListBean> result = new PageQueryResult<>();
            result.setCount(pageQueryResult.getCount());
            List<APIInfoListBean> list = new ArrayList<>();
            for (DSGCApisBean item : pageQueryResult.getResult()) {
                APIInfoListBean apiInfoListBean = apiAuthBeanMapping(item);
                list.add(apiInfoListBean);
            }
            result.setResult(list);
            return result;
        }
    }


    public APIInfoListBean apiAuthBeanMapping(DSGCApisBean dsgcApisBean) {
        APIInfoListBean apiInfoListBean = new APIInfoListBean();
        apiInfoListBean.setApiCode(dsgcApisBean.getApiCode());
        apiInfoListBean.setApiName(dsgcApisBean.getApiName());
        apiInfoListBean.setAppCode(dsgcApisBean.getAppCode());
        apiInfoListBean.setAuthSystemCount(apiMngDao.queryAuthSystemCount(dsgcApisBean.getApiCode()));
        return apiInfoListBean;
    }


}
