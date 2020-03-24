package com.definesys.dsgc.service.svcAuth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apiauth.ApiAuthDao;

import com.definesys.dsgc.service.apiauth.ApiAuthService;
import com.definesys.dsgc.service.apiauth.bean.CommonReqBean;
import com.definesys.dsgc.service.apiauth.bean.DSGCApisAccess;
import com.definesys.dsgc.service.consumers.ConsumersDao;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.svcAuth.bean.*;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.DSGCSystemDao;
import com.definesys.dsgc.service.system.DSGCSystemService;
import com.definesys.dsgc.service.system.bean.DSGCSystemAccess;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.users.DSGCUserDao;
import com.definesys.dsgc.service.users.UsersDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class SVCAuthService {
    @Autowired
    private SVCAuthDao svcAuthDao;

    @Autowired
    private SVCLogDao sldao;

    @Autowired
    private ConsumersDao consumersDao;

    @Autowired
    private DSGCUserDao dsgcUserDao;
    @Autowired
    private ApiAuthService apiAuthService;
    @Autowired
    private DSGCSystemDao systemDao;


    public PageQueryResult<SVCHisBean> querySvcHis(SVCCommonReqBean param, int pageIndex, int pageSize){
        PageQueryResult<SVCHisBean> result = svcAuthDao.querySvcHis(param,pageIndex,pageSize);
        return result;
    }
    public PageQueryResult<SVCAuthInfoListBean> querySvcAuth(SVCCommonReqBean param, int pageIndex, int pageSize,String userId,String userRole){
        if ("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {

          List<FndLookupValue> fndLookupValueList =  queryFndModuleByLookupType("SVC_SHARE_TYPE");
            List<String> sysCodeList = new ArrayList<>();
          if ("SystemLeader".equals(userRole)){
              List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
              Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
              while (iter.hasNext()) {
                  DSGCSystemUser s = (DSGCSystemUser) iter.next();
                  sysCodeList.add(s.getSysCode());
              }
          }
              PageQueryResult<DSGCService> pageQueryResult = svcAuthDao.querySvcAuth(param,pageIndex,pageSize,userRole,sysCodeList);
            PageQueryResult<SVCAuthInfoListBean> result = new PageQueryResult<>();
            result.setCount(pageQueryResult.getCount());
            List<SVCAuthInfoListBean> list = new ArrayList<>();
            for (DSGCService item:pageQueryResult.getResult()) {
                SVCAuthInfoListBean temp =svcAuthBeanMapping(item);
                if("public".equals(item.getShareType())){
                    temp.setAuthSystemCount("全部");
                }else if("private".equals(item.getShareType())) {
                   String countMap = svcAuthDao.queryAuthSystemCount(item.getServNo());
                   temp.setAuthSystemCount(countMap);
                }
                for (int  i =0;i<fndLookupValueList.size();i++){
                    if (temp.getShareTypeDesc() != null && temp.getShareTypeDesc().equals(fndLookupValueList.get(i).getLookupCode())){
                        temp.setShareTypeDesc(fndLookupValueList.get(i).getMeaning());
                        break;
                    }
                }
                list.add(temp);
            }
            result.setResult(list);
                return result;
        }
    }
    public SVCAuthInfoListBean svcAuthBeanMapping(DSGCService dsgcService){
        SVCAuthInfoListBean svcAuthInfoListBean = new SVCAuthInfoListBean();
        svcAuthInfoListBean.setServName(dsgcService.getServName());
        svcAuthInfoListBean.setServNo(dsgcService.getServNo());
        svcAuthInfoListBean.setSubSystem(dsgcService.getAttribue1());
        svcAuthInfoListBean.setShareTypeDesc(dsgcService.getShareType());
        return svcAuthInfoListBean;

    }

    public List<LookupValueDTO> queryLookupValue(String lookupType){
        List<FndLookupValue> fndLookupValues = svcAuthDao.queryFndModuleByLookupType(lookupType);
        List<LookupValueDTO> result = new ArrayList<>();
        Iterator<FndLookupValue> iterator = fndLookupValues.iterator();
        while (iterator.hasNext()){
            FndLookupValue temp = iterator.next();
            LookupValueDTO lookupValueDTO = new LookupValueDTO();
            lookupValueDTO.setMeanning(temp.getMeaning());
            lookupValueDTO.setLookupCode(temp.getLookupCode());
            result.add(lookupValueDTO);
        }
        return result;
    }

    public List<FndLookupValue> queryFndModuleByLookupType(String lookupType){
      return svcAuthDao.queryFndModuleByLookupType(lookupType);
    }

    public List<SVCSystemResDTO> querySystem(String userId,String userRole){
        List<SVCSystemResDTO> resDTOS = new ArrayList<>();
        List<DSGCSystemEntities> list = new ArrayList<>();
        if(userRole.equals("SuperAdministrators") || userRole.equals("Administrators")){
                 list = svcAuthDao.querySystem();
        }else if(userRole.equals("SystemLeader")){
            list =  svcAuthDao.querySystemLeaderSystem(userId);
        }else {
            return resDTOS;
        }

        for (DSGCSystemEntities item:list) {
            SVCSystemResDTO dto = new SVCSystemResDTO();
            dto.setSysCode(item.getSysCode());
            dto.setSysName(item.getSysName());
            resDTOS.add(dto);
        }
        return resDTOS;
    }

    public SVCAuthInfoListBean querySvcAuthDetailBaseInfo(String servNo){
       DSGCService dsgcService = svcAuthDao.querySvcAuthDetailBaseInfo(servNo);
       SVCAuthInfoListBean svcAuthInfoListBean = new SVCAuthInfoListBean();
       svcAuthInfoListBean.setServNo(dsgcService.getServNo());
       svcAuthInfoListBean.setServName(dsgcService.getServName());
       svcAuthInfoListBean.setSubSystem(dsgcService.getAttribue1());
        List<FndLookupValue> fndLookupValueList = svcAuthDao.queryFndModuleByLookupType("SVC_SHARE_TYPE");
        for (int  i =0;i<fndLookupValueList.size();i++){
            if (dsgcService.getShareType().equals(fndLookupValueList.get(i).getLookupCode())){
                svcAuthInfoListBean.setShareTypeDesc(fndLookupValueList.get(i).getMeaning());
                break;
            }
        }
        return svcAuthInfoListBean;
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateSvcAuthServShareType(SVCBaseInfoBean svcBaseInfoBean,String userName){
        svcAuthDao.updateSvcAuthServShareType(svcBaseInfoBean);
        SVCHisBean svcHisBean = new SVCHisBean();
        svcHisBean.setUpdType("auth");
        String cnt = "修改服务共享类型为";
        List<FndLookupValue> fndLookupValueList = svcAuthDao.queryFndModuleByLookupType("SVC_SHARE_TYPE");
        for (int  i =0;i<fndLookupValueList.size();i++){
            if (svcBaseInfoBean.getShareType().equals(fndLookupValueList.get(i).getLookupCode())){
                cnt += fndLookupValueList.get(i).getMeaning();
                break;
            }
        }
        svcHisBean.setUpdCnt(cnt);
        svcHisBean.setHisType("edit");
        svcHisBean.setServNo(svcBaseInfoBean.getServNo());
        svcHisBean.setCreatedBy(userName);
        svcHisBean.setLastUpdatedBy(userName);
        this.addSvcHis(svcHisBean);
    }
    public PageQueryResult<SVCAuthSystemResDTO> queryServAuthSystemList(SVCBaseInfoBean svcBaseInfoBean,int pageIndex,int pageSize){
       PageQueryResult<DSGCSystemEntities> queryResult = svcAuthDao.queryServAuthSystemList(svcBaseInfoBean,pageIndex,pageSize);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PageQueryResult<SVCAuthSystemResDTO> result = new PageQueryResult<>();
        List<SVCAuthSystemResDTO> resDTOS = new ArrayList<>();
        Long count = queryResult.getCount();
        List<DSGCSystemEntities> list = new ArrayList<>();
        for (DSGCSystemEntities item:queryResult.getResult()) {
            SVCAuthSystemResDTO svcAuthSystemResDTO = new SVCAuthSystemResDTO();
            svcAuthSystemResDTO.setSysName(item.getSysName());
            svcAuthSystemResDTO.setSysCode(item.getSysCode());
            svcAuthSystemResDTO.setLastUpdateDate(simpleDateFormat.format(item.getLastUpdateDate()));
            svcAuthSystemResDTO.setLastUpdatedBy(item.getLastUpdatedBy());
            svcAuthSystemResDTO.setSaId(item.getAttribue1());
            resDTOS.add(svcAuthSystemResDTO);
        }
        result.setResult(resDTOS);
        result.setCount(count);
        return result;
    }
    public PageQueryResult<SVCAuthConsumerResDTO> queryServAuthConsumerList(SVCBaseInfoBean svcBaseInfoBean,int pageIndex,int pageSize){
        PageQueryResult<DSGCConsumerEntities> queryResult = svcAuthDao.queryServAuthConsumerList(svcBaseInfoBean,pageIndex,pageSize);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PageQueryResult<SVCAuthConsumerResDTO> result = new PageQueryResult<>();
        List<SVCAuthConsumerResDTO> resDTOS = new ArrayList<>();
        Long count = queryResult.getCount();
        List<DSGCSystemEntities> list = new ArrayList<>();
        for (DSGCConsumerEntities item:queryResult.getResult()) {
            SVCAuthConsumerResDTO consumerResDTO = new SVCAuthConsumerResDTO();
            consumerResDTO.setCsmName(item.getCsmName());
            consumerResDTO.setCsmCode(item.getCsmCode());
            consumerResDTO.setLastUpdateDate(simpleDateFormat.format(item.getLastUpdateDate()));
            consumerResDTO.setLastUpdatedBy(item.getLastUpdatedBy());
            consumerResDTO.setSaId(item.getAttribue1());
            resDTOS.add(consumerResDTO);
        }
        result.setResult(resDTOS);
        result.setCount(count);
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteServAuthSystem(SVCCommonReqBean param,String userName){
       DSGCSystemAccess dsgcSystemAccess = svcAuthDao.queryAccessSystem(param.getCon0());
       if(dsgcSystemAccess.getSaId() != null){
           svcAuthDao.deleteAccessSystem(dsgcSystemAccess.getSaId());
           DSGCSystemEntities dsgcSystemEntities =svcAuthDao.querySystemByCode(dsgcSystemAccess.getSysCode());
           String cnt = "移除已授权系统："+dsgcSystemEntities.getSysName();
           SVCHisBean svcHisBean = new SVCHisBean();
           svcHisBean.setUpdCnt(cnt);
           svcHisBean.setUpdType("auth");
           svcHisBean.setHisType("edit");
           svcHisBean.setServNo(dsgcSystemAccess.getServNo());
           svcHisBean.setCreatedBy(userName);
           svcHisBean.setLastUpdatedBy(userName);
           this.addSvcHis(svcHisBean);
       }

    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteServAuthConsumer(SVCCommonReqBean param,String userName){
        DSGCSystemAccess dsgcSystemAccess = svcAuthDao.queryAccessSystem(param.getCon0());
        if(dsgcSystemAccess.getSaId() != null){
            svcAuthDao.deleteAccessSystem(dsgcSystemAccess.getSaId());
            DSGCConsumerEntities dsgcConsumerEntities =svcAuthDao.queryConsumerByCode(dsgcSystemAccess.getSysCode());
            String cnt = "移除已授权系统："+dsgcConsumerEntities.getCsmName();
            SVCHisBean svcHisBean = new SVCHisBean();
            svcHisBean.setUpdCnt(cnt);
            svcHisBean.setUpdType("auth");
            svcHisBean.setHisType("edit");
            svcHisBean.setServNo(dsgcSystemAccess.getServNo());
            svcHisBean.setCreatedBy(userName);
            svcHisBean.setLastUpdatedBy(userName);
            this.addSvcHis(svcHisBean);
        }

    }
    @Transactional(rollbackFor = Exception.class)
    public String addServAuthSytem(SVCAddAuthSystemReqBean param,String userName){
       Boolean systemIsExist = svcAuthDao.checkSystemEntitieIsExist(param.getSysCode());
       Boolean authIsExist  = svcAuthDao.checkAccessSystemIsExist(param);
       if(!systemIsExist){
           return "-1";
       }
       if (authIsExist){
           return "-2";
       }
       DSGCSystemAccess dsgcSystemAccess = new DSGCSystemAccess();
       dsgcSystemAccess.setServNo(param.getServNo());
       dsgcSystemAccess.setSysCode(param.getSysCode());
        svcAuthDao.addServAuthSytem(dsgcSystemAccess);

        DSGCSystemEntities dsgcSystemEntities =svcAuthDao.querySystemByCode(dsgcSystemAccess.getSysCode());
        String cnt = "添加授权系统："+dsgcSystemEntities.getSysName();
        SVCHisBean svcHisBean = new SVCHisBean();
        svcHisBean.setUpdCnt(cnt);
        svcHisBean.setHisType("edit");
        svcHisBean.setUpdType("auth");
        svcHisBean.setServNo(dsgcSystemAccess.getServNo());
        svcHisBean.setCreatedBy(userName);
        svcHisBean.setLastUpdatedBy(userName);
        this.addSvcHis(svcHisBean);
        return "1";
    }
    @Transactional(rollbackFor = Exception.class)
    public String addServAuthConsumer(SVCAddAuthSystemReqBean param,String userName){
        Boolean consumerIsExist = svcAuthDao.checkConsumerEntitieIsExist(param.getSysCode());
        Boolean authIsExist  = svcAuthDao.checkAccessSystemIsExist(param);
        if(!consumerIsExist){
            return "-1";
        }
        if (authIsExist){
            return "-2";
        }
        DSGCSystemAccess dsgcSystemAccess = new DSGCSystemAccess();
        dsgcSystemAccess.setServNo(param.getServNo());
        dsgcSystemAccess.setSysCode(param.getSysCode());
        svcAuthDao.addServAuthSytem(dsgcSystemAccess);

        DSGCConsumerEntities dsgcConsumerEntities =svcAuthDao.queryConsumerByCode(dsgcSystemAccess.getSysCode());
        String cnt = "添加授权消费者："+dsgcConsumerEntities.getCsmName();
        SVCHisBean svcHisBean = new SVCHisBean();
        svcHisBean.setUpdCnt(cnt);
        svcHisBean.setHisType("edit");
        svcHisBean.setUpdType("auth");
        svcHisBean.setServNo(dsgcSystemAccess.getServNo());
        svcHisBean.setCreatedBy(userName);
        svcHisBean.setLastUpdatedBy(userName);
        this.addSvcHis(svcHisBean);
        return "1";
    }
    @Transactional(rollbackFor = Exception.class)
    public void addSvcHis(SVCHisBean svcHisBean){
        svcAuthDao.addSvcHis(svcHisBean);
    }


    //发起申请服务流程
    @Transactional(rollbackFor = Exception.class)
    public String applyServAuthPro(String instanceId,ApplyAuthProBean applyAuthProBean){
        applyAuthProBean.setInstanceId(instanceId);
        String[] consumer=applyAuthProBean.getConsumer();
        String consumerStr="";
        for(int i=0;i<consumer.length-1;i++){
            consumerStr=consumerStr+consumer[i]+",";
        }
        consumerStr=consumerStr+consumer[consumer.length-1];
        applyAuthProBean.setConsumerStr(consumerStr);
        svcAuthDao.addApplyServAuthBus(applyAuthProBean);
        return "成功";
    }

    //获取申请服务流程业务信息
    @Transactional(rollbackFor = Exception.class)
    public ApplyAuthProBeanDTO getProcessBusinessInfo(String instanceId){
        ApplyAuthProBeanDTO applyAuthProBeanDTO=new ApplyAuthProBeanDTO();
        ApplyAuthProBean applyAuthProBean=svcAuthDao.getProcessBusinessInfo(instanceId);
        //补充用户信息
        DSGCUser dsgcUser= dsgcUserDao.findUserById(applyAuthProBean.getApplicant());
        applyAuthProBeanDTO.setApplicantEmail(dsgcUser.getUserMail());
        applyAuthProBeanDTO.setApplicantPhone(dsgcUser.getUserMail());
        applyAuthProBeanDTO.setApplicantName(dsgcUser.getUserName());
        applyAuthProBeanDTO.setApplicantDesc(dsgcUser.getUserDescription());
        //补充基础信息
        applyAuthProBeanDTO.setApplyDesc(applyAuthProBean.getApplyDesc());
        applyAuthProBeanDTO.setApplySerName(applyAuthProBean.getApplySerName());
        applyAuthProBeanDTO.setApplySerType(applyAuthProBean.getApplySerType());
        //补充消费者信息
        List<String> consCodes=  Arrays.asList(applyAuthProBean.getConsumerStr().split(","));
        List<DSGCConsumerEntities> consCodesList=consumersDao.queryConsumersListByIds(consCodes);
        applyAuthProBeanDTO.setConsumerList(consCodesList);
        applyAuthProBeanDTO.setInstanceId(applyAuthProBean.getInstanceId());
        applyAuthProBeanDTO.setProcessType(applyAuthProBean.getProcessType());
        //补充服务信息
        if(applyAuthProBeanDTO.getApplySerType().equals("apiSource")){
            applyAuthProBeanDTO.setAppliSer(svcAuthDao.queryApi(applyAuthProBeanDTO.getApplySerName()));
        }
        if(applyAuthProBeanDTO.getApplySerType().equals("servSource")){
            applyAuthProBeanDTO.setAppliSer(svcAuthDao.queryServ(applyAuthProBeanDTO.getApplySerName()));
        }
        return applyAuthProBeanDTO;
    }

        public void delProcessBusinessInfo(String instanceId){
            svcAuthDao.delProcessBusinessInfo(instanceId);
        }


    //流程结束，赋予权限
    public void authSevPro(String instanceId,String userName) {
        //根据instanceId获取业务信息
        ApplyAuthProBean applyAuthProBean = svcAuthDao.getProcessBusinessInfo(instanceId);
        //根据服务类型注册
        String servType = applyAuthProBean.getApplySerType();
        String consumerStr = applyAuthProBean.getConsumerStr();
        String[] consumerlist = consumerStr.split(",");
        //api注册
        if (servType.equals("apiSource")) {
            for (String item : consumerlist) {
                CommonReqBean commonReqBean = new CommonReqBean();
                commonReqBean.setApiCode(applyAuthProBean.getApplySerName());
                commonReqBean.setSysCode(item);
                apiAuthService.addApiAuthConsumer(commonReqBean, applyAuthProBean.getApplicant());
            }
        }
        if (servType.equals("servSource")) {
            for (String item : consumerlist) {
                SVCAddAuthSystemReqBean bean=new SVCAddAuthSystemReqBean();
                bean.setServNo(applyAuthProBean.getApplySerName());
                bean.setSysCode(item);
                addServAuthConsumer(bean,userName);
            }
        }
    }

    //检查该消费者是否持有服务权限
    public List<DSGCSystemAccess> checkSerAuthIsExist(String servNo, List<String> customerList){
        return svcAuthDao.checkSerAuthIsExist(servNo,customerList);
    }

}
