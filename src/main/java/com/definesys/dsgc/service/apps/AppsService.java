package com.definesys.dsgc.service.apps;

import com.definesys.dsgc.service.apps.bean.CommonReqBean;
import com.definesys.dsgc.service.apps.bean.SystemEntitireDTO;
import com.definesys.dsgc.service.apps.bean.UserResDTO;
import com.definesys.dsgc.service.apps.bean.UserRoleResDTO;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppsService {
    @Autowired
    private AppsDao appsDao;

    @Autowired
    private SVCAuthDao svcAuthDao;

    public PageQueryResult<UserResDTO> queryUserList(CommonReqBean commonReqBean,int pageSize,int pageIndex,String userRole){
        PageQueryResult<UserResDTO> resDTOPageQueryResult = new PageQueryResult<>();
        if("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
        List<FndLookupValue> lookupValueList = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
            PageQueryResult<DSGCUser> userList = appsDao.queryUserList(commonReqBean,pageSize,pageIndex);
            Long total = userList.getCount();
           List<UserResDTO> resDTOS = userDataMapping(userList.getResult(),lookupValueList);
           resDTOPageQueryResult.setCount(total);
           resDTOPageQueryResult.setResult(resDTOS);
        }else {
            return null;
        }
        return resDTOPageQueryResult;
    }

    public List<UserResDTO> userDataMapping(List<DSGCUser> dsgcUsers,List<FndLookupValue> lookupValueList){
        List<UserResDTO> result = new ArrayList<>();
    for (int i=0;i<dsgcUsers.size();i++){
        UserResDTO userResDTO = new UserResDTO();
        userResDTO.setUserId(dsgcUsers.get(i).getUserId());
        userResDTO.setUserName(dsgcUsers.get(i).getUserName());
        userResDTO.setUserRole(dsgcUsers.get(i).getUserRole());
        userResDTO.setUserDescription(dsgcUsers.get(i).getUserDescription());
        userResDTO.setUserMail(dsgcUsers.get(i).getUserMail());
        userResDTO.setUserPhone(dsgcUsers.get(i).getUserPhone());
        for (int j = 0;j<lookupValueList.size();j++){
            if (userResDTO.getUserRole().equals(lookupValueList.get(j).getLookupCode())){
                userResDTO.setUserRoleName(lookupValueList.get(j).getMeaning());
                break;
            }
        }
        result.add(userResDTO);
    }
    return result;
    }
    public List<UserRoleResDTO> queryUserRoleList(){
       List<FndLookupValue> lookupValues = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
       List<UserRoleResDTO> resDTOS = new ArrayList<>();
        for (FndLookupValue item:lookupValues) {
           UserRoleResDTO userRoleResDTO = new UserRoleResDTO();
           userRoleResDTO.setUserRoleCode(item.getLookupCode());
           userRoleResDTO.setUserRoleName(item.getMeaning());
           resDTOS.add(userRoleResDTO);
        }
        return resDTOS;
    }

    public PageQueryResult<DSGCSystemEntities> queryAllAppsList(CommonReqBean commonReqBean,int pageSize,int pageIndex,String userName,String userRole){
        PageQueryResult<DSGCSystemEntities> entitiesPageQueryResult = new PageQueryResult<>();
        if("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {
           commonReqBean.setCon0(StringUtil.filtration(commonReqBean.getCon0()));
             entitiesPageQueryResult = appsDao.queryAppsList(commonReqBean,pageSize,pageIndex,userName,userRole);
        }
        return entitiesPageQueryResult;
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApp(String id){
       DSGCSystemEntities dsgcSystemEntities = appsDao.querySystemEnt(id);
        List<DSGCService> services =queryServBySysCode(dsgcSystemEntities.getSysCode());
       if(services !=null && services.size()>0){
           appsDao.updateSystemEnt(id);
       }else {
           appsDao.delSytemUser(dsgcSystemEntities.getSysCode());
           appsDao.delSytemEnt(id);
       }

    }
    public List<DSGCService> queryServBySysCode(String sysCode){
       return appsDao.queryServBySysCode(sysCode);
    }
    public SystemEntitireDTO queryAppDataById(String id){
        DSGCSystemEntities dsgcSystemEntities = appsDao.querySystemEnt(id);
        SystemEntitireDTO result = new SystemEntitireDTO();
        List<UserResDTO> userResDTOList = new ArrayList<>();
        List<DSGCUser> userList = appsDao.querySystemUserBySysCode(dsgcSystemEntities.getSysCode());
        List<FndLookupValue> lookupValues = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
        for (DSGCUser item:userList) {
            UserResDTO userResDTO = new UserResDTO();
            userResDTO.setUserId(item.getUserId());
            userResDTO.setUserName(item.getUserName());
            userResDTO.setUserRole(item.getUserRole());
            userResDTO.setUserDescription(item.getUserDescription());
            for (int i=0;i<lookupValues.size();i++){
                if (item.getUserRole().equals(lookupValues.get(i).getLookupCode())){
                    userResDTO.setUserRoleName(lookupValues.get(i).getMeaning());
                    break;
                }
            }
            userResDTOList.add(userResDTO);
        }
        result.setId(dsgcSystemEntities.getId());
        result.setSysCode(dsgcSystemEntities.getSysCode());
        result.setSysName(dsgcSystemEntities.getSysName());
        result.setSysDesc(dsgcSystemEntities.getSysDesc());
        result.setUserResDTOS(userResDTOList);
        return result;
    }

    public List<UserResDTO> queryUserListByUserIdArr(List<String> userIdList){
      List<DSGCUser> list =  appsDao.queryUserListByUserIdArr(userIdList);
        List<FndLookupValue> lookupValues = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
      List<UserResDTO> result = new ArrayList<>();
        for (DSGCUser item:list) {
           UserResDTO userResDTO = new UserResDTO();
           userResDTO.setUserId(item.getUserId());
           userResDTO.setUserName(item.getUserName());
           userResDTO.setUserRole(item.getUserRole());
           userResDTO.setUserDescription(item.getUserDescription());
            for (int i=0;i<lookupValues.size();i++){
                if (item.getUserRole().equals(lookupValues.get(i).getLookupCode())){
                    userResDTO.setUserRoleName(lookupValues.get(i).getMeaning());
                    break;
                }
            }
            result.add(userResDTO);
        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveAppData(SystemEntitireDTO systemEntitireDTO,String userId){
        DSGCSystemEntities dsgcSystemEntities = new DSGCSystemEntities();
        dsgcSystemEntities.setSysName(systemEntitireDTO.getSysName());
        dsgcSystemEntities.setSysCode(systemEntitireDTO.getSysCode());
        dsgcSystemEntities.setSysDesc(systemEntitireDTO.getSysDesc());
        dsgcSystemEntities.setPwd("");
        dsgcSystemEntities.setCreatedBy(userId);
        dsgcSystemEntities.setLastUpdatedBy(userId);
        if(StringUtil.isNotBlank(systemEntitireDTO.getId())){
            dsgcSystemEntities.setId(systemEntitireDTO.getId());
            appsDao.updateAppData(dsgcSystemEntities);
        }else {
            appsDao.addAppData(dsgcSystemEntities);
        }
        List<UserResDTO> list =systemEntitireDTO.getUserResDTOS();
        List<DSGCSystemUser> userList = new ArrayList<>();
        for (UserResDTO item:list) {
            DSGCSystemUser dsgcSystemUser = new DSGCSystemUser();
            dsgcSystemUser.setSysCode(systemEntitireDTO.getSysCode());
            dsgcSystemUser.setUserId(item.getUserId());
            dsgcSystemUser.setCreatedBy(userId);
            dsgcSystemUser.setLastUpdatedBy(userId);
            userList.add(dsgcSystemUser);
        }
        if(userList != null && userList.size()>0){
            appsDao.delSytemUser(systemEntitireDTO.getSysCode());
            appsDao.addSystemUser(userList);
        }

    }
}
