package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.*;
import com.definesys.dsgc.service.svcmng.SVCMngDao;
import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadParamsBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadSampleBean;
import com.definesys.dsgc.service.svcmng.bean.SVCCommonReqBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
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

    @Autowired
    private SVCMngDao svcMngDao;

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
        completionThread(param.getApiCode());
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
        completionThread(dsgcApisBean.getApiCode());

    }

    public Boolean checkApiCodeIsExist(CommonReqBean param){
        Boolean isExist =  apiMngDao.checkApiCodeIsExist(param.getCon0());
        return isExist;
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateApiDataCompletion(String apiCode){
        int completion = 0;
        DSGCApisBean dsgcApisBean = apiMngDao.queryBasicInfoByApiCode(apiCode);
        if(StringUtil.isNotBlank(dsgcApisBean.getApiCode())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcApisBean.getApiName())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcApisBean.getAppCode())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcApisBean.getApiDesc())){
            completion +=10;
        }
        List<com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri> uris = svcMngDao.queryServUri(apiCode);
        if(uris != null && uris.size() > 0){
            List<String> uriTypeList = new ArrayList<>();
            Iterator<com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri> iterator = uris.iterator();
            while (iterator.hasNext()){
                com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri dsgcServicesUri = iterator.next();
                uriTypeList.add(dsgcServicesUri.getUriType());
            }
            if (uriTypeList.contains("REST")){
                completion +=10;
            }
        }
        SVCCommonReqBean param = new SVCCommonReqBean();
        param.setCon0(apiCode);
        param.setQueryType("REQ");
        List<DSGCPayloadSampleBean> reqSampleBeans = svcMngDao.querySrvPaloadSample(param);
        List<DSGCPayloadParamsBean> reqSampleParamBeans = svcMngDao.queryServPayloadParam(param);
        param.setQueryType("RES");
        List<DSGCPayloadSampleBean> resSampleBeans = svcMngDao.querySrvPaloadSample(param);
        List<DSGCPayloadParamsBean> resSampleParamBeans = svcMngDao.queryServPayloadParam(param);
        if(reqSampleBeans.size()>0){
            completion += 10;
        }
        if(reqSampleParamBeans.size()>0){
            completion +=5;

        }
        if (resSampleBeans.size() >0){
            completion += 10;
        }
        if(resSampleParamBeans.size()>0){
            completion +=5;

        }

        DSGCApisBean apisBean = new DSGCApisBean();
        apisBean.setInfoFull(String.valueOf(completion));
        apisBean.setApiCode(apiCode);
        apiMngDao.updateApiDataCompletion(apisBean);
    }
    public void completionThread(String servNo){
        Runnable myRunnable = new Runnable(){
            public void run(){
                updateApiDataCompletion(servNo);
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
