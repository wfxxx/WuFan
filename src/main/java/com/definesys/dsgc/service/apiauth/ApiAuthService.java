package com.definesys.dsgc.service.apiauth;

import com.definesys.dsgc.service.apiauth.bean.APIInfoListBean;
import com.definesys.dsgc.service.apiauth.bean.CommonReqBean;
import com.definesys.dsgc.service.apiauth.bean.DSGCApisAccess;
import com.definesys.dsgc.service.apiauth.bean.DSGCApisBean;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.dagclient.ConsumerDeployService;
import com.definesys.dsgc.service.svcAuth.bean.SVCHisBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiAuthService {
    @Autowired
    private ApiAuthDao apiAuthDao;
    @Autowired
    private ConsumerDeployService consumerDeployService;

    public APIInfoListBean queryApiAuthDetailBaseInfo(String apiCode){
        DSGCApisBean dsgcApisBean = apiAuthDao.queryApiAuthDetailBaseInfo(apiCode);
        APIInfoListBean apiInfoListBean = new APIInfoListBean();
        apiInfoListBean.setApiCode(dsgcApisBean.getApiCode());
        apiInfoListBean.setApiName(dsgcApisBean.getApiName());
        apiInfoListBean.setAppCode(dsgcApisBean.getAppCode());
        return apiInfoListBean;
    }

    public PageQueryResult<DSGCConsumerEntities> queryApiAuthConsumerList(CommonReqBean commonReqBean, int pageIndex, int pageSize){
        return apiAuthDao.queryApiAuthConsumerList(commonReqBean,pageIndex,pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public String addApiAuthConsumer(CommonReqBean param, String userName){
        Boolean consumerIsExist = apiAuthDao.checkConsumerEntitieIsExist(param.getSysCode());
        Boolean authIsExist  = apiAuthDao.checkAccessSystemIsExist(param);
        if(!consumerIsExist){
            return "-1";
        }
        if (authIsExist){
            return "-2";
        }
        DSGCApisAccess dsgcApisAccess = new DSGCApisAccess();
        dsgcApisAccess.setApiCode(param.getApiCode());
        dsgcApisAccess.setCsmCode(param.getSysCode());

        //将授权信息部署至网关
        this.consumerDeployService.addDAGConsumerAcl(param.getSysCode(),param.getApiCode());

        apiAuthDao.addServAuthSytem(dsgcApisAccess);

        DSGCConsumerEntities dsgcConsumerEntities =apiAuthDao.queryConsumerByCode(dsgcApisAccess.getCsmCode());
        String cnt = "添加授权消费者："+dsgcConsumerEntities.getCsmName();
        SVCHisBean svcHisBean = new SVCHisBean();
        svcHisBean.setUpdCnt(cnt);
        svcHisBean.setHisType("edit");
        svcHisBean.setUpdType("auth");
        svcHisBean.setServNo(dsgcApisAccess.getApiCode());
        svcHisBean.setCreatedBy(userName);
        svcHisBean.setLastUpdatedBy(userName);
        this.addSvcHis(svcHisBean);
        return "1";
    }

    @Transactional(rollbackFor = Exception.class)
    public void addSvcHis(SVCHisBean svcHisBean){
        apiAuthDao.addSvcHis(svcHisBean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteApiAuthConsumer(CommonReqBean param,String userName){
        DSGCApisAccess dsgcApisAccess = apiAuthDao.queryAccessSystem(param.getCon0());
        if(dsgcApisAccess.getDaaId() != null){

            //将授权信息从网关移除
            this.consumerDeployService.removeDAGConsumerAcl(dsgcApisAccess.getCsmCode(),dsgcApisAccess.getApiCode());

            apiAuthDao.deleteAccessSystem(dsgcApisAccess.getDaaId());
            DSGCConsumerEntities dsgcConsumerEntities =apiAuthDao.queryConsumerByCode(dsgcApisAccess.getCsmCode());
            String cnt = "移除已授权系统："+dsgcConsumerEntities.getCsmName();
            SVCHisBean svcHisBean = new SVCHisBean();
            svcHisBean.setUpdCnt(cnt);
            svcHisBean.setUpdType("auth");
            svcHisBean.setHisType("edit");
            svcHisBean.setServNo(dsgcApisAccess.getApiCode());
            svcHisBean.setCreatedBy(userName);
            svcHisBean.setLastUpdatedBy(userName);
            this.addSvcHis(svcHisBean);
        }

    }

    public PageQueryResult<APIInfoListBean> queryApiAuthList(CommonReqBean param, int pageIndex, int pageSize, String userId, String userRole) {
        if ("Tourist".equals(userRole)) {
            return new PageQueryResult<>();
        } else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)) {
                List<DSGCSystemUser> dsgcSystemUsers = apiAuthDao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            PageQueryResult<DSGCApisBean> pageQueryResult = apiAuthDao.queryApiAuthList(param, pageIndex, pageSize, userId, userRole, sysCodeList);
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
        apiInfoListBean.setAuthSystemCount(apiAuthDao.queryAuthSystemCount(dsgcApisBean.getApiCode()));
        return apiInfoListBean;
    }
}
