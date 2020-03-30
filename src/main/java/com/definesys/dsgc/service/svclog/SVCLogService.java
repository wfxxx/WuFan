package com.definesys.dsgc.service.svclog;

import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.dsgc.service.svclog.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCServInterfaceNode;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.svclog.bean.DSGCValidResutl;
import com.definesys.dsgc.service.users.DSGCUserDao;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SVCLogService {
    @Autowired
    private SVCLogDao sldao;

    @Autowired
    private DSGCUserDao dsgcUserDao;
    @Autowired
    DSGCLogInstanceDao dsgcLogInstanceDao;

public PageQueryResult<SVCLogListBean> querySvcLogRecordListByCon(SVCLogQueryBean q, int pageSize, int pageIndex,String userRole,String userId) {
    PageQueryResult<SVCLogListBean> result = new PageQueryResult<SVCLogListBean>();
    String sysCode = "";
    List<String> sysCodeList = new ArrayList<>();
    if("SystemLeader".equals(userRole)){
        List<DSGCSystemUser> dsgcSystemUsers = sldao.findUserSystemByUserId(userId);
        for (DSGCSystemUser item:dsgcSystemUsers) {
            sysCodeList.add(item.getSysCode());
        }
//        sysCode = dsgcSystemUser.getSysCode();
    }
    PageQueryResult<DSGCService> list =sldao.querySvcLogRecordListByCon(q,pageSize,pageIndex,userRole,userId,sysCodeList);
    List<DSGCService> dsgcServiceList = list.getResult();
    Long count = list.getCount();
    List<SVCLogListBean> svcLogListBeanList = new ArrayList<>();
 if("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
     for (DSGCService item:dsgcServiceList) {
         SVCLogListBean svcLogListBean = new SVCLogListBean();
         svcLogListBean.setBodyStoreType(item.getBodyStoreType());
         svcLogListBean.setModifiable("Y");
         svcLogListBean.setServId(item.getServId());
         svcLogListBean.setServName(item.getServName());
         svcLogListBean.setServNo(item.getServNo());
         svcLogListBean.setSubSystem(item.getSubordinateSystem());
         svcLogListBean.setSubSystemName(item.getAttribue1());
         svcLogListBeanList.add(svcLogListBean);
     }
 }else if("SystemLeader".equals(userRole)){
     for (DSGCService item:dsgcServiceList) {
         SVCLogListBean svcLogListBean = new SVCLogListBean();
         svcLogListBean.setBodyStoreType(item.getBodyStoreType());
         if(sysCodeList.contains(item.getSubordinateSystem())){
             svcLogListBean.setModifiable("Y");
         }else {
             svcLogListBean.setModifiable("N");
         }
         svcLogListBean.setServId(item.getServId());
         svcLogListBean.setServName(item.getServName());
         svcLogListBean.setServNo(item.getServNo());
         svcLogListBean.setSubSystem(item.getSubordinateSystem());
         svcLogListBean.setSubSystemName(item.getAttribue1());
         svcLogListBeanList.add(svcLogListBean);
     }
 }else {
     for (DSGCService item:dsgcServiceList) {
         SVCLogListBean svcLogListBean = new SVCLogListBean();
         svcLogListBean.setBodyStoreType(item.getBodyStoreType());
         svcLogListBean.setServId(item.getServId());
         svcLogListBean.setModifiable("N");
         svcLogListBean.setServName(item.getServName());
         svcLogListBean.setServNo(item.getServNo());
         svcLogListBean.setSubSystem(item.getSubordinateSystem());
         svcLogListBean.setSubSystemName(item.getAttribue1());
         svcLogListBeanList.add(svcLogListBean);
     }
 }
    result.setResult(svcLogListBeanList);
    result.setCount(count);
    return result;
        }
        @Transactional(rollbackFor = Exception.class)
        public void updateServSaveTypeById(SVCLogListBean q,String userRole,String userId) throws Exception{
        Boolean isEdit = false;
        if("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
            isEdit = true;
        }else if("SystemLeader".equals(userRole)){
            DSGCService dsgcService = sldao.findServById(q.getServId());
            List<DSGCSystemUser> dsgcSystemUsers = sldao.findUserSystemByUserId(userId);
            for (DSGCSystemUser item:dsgcSystemUsers) {
                String sysCode = item.getSysCode();
                if(dsgcService.getSubordinateSystem().equals(sysCode)){
                    isEdit = true;

                }else {
                    isEdit = false;
                }
            }

        }else {
            isEdit = false;
        }
        if (isEdit){
            sldao.updateServLogSaveType(q);
        }else {
            throw new Exception("无权限修改！");
        }

        }


        public List<SVCCommonServResDTO> queryServCommonByCon(SVCLogQueryBean q,String userId,String userRole){
            if ("Tourist".equals(userRole)){
                return new ArrayList<>();
            }else {
                List<String> sysCodeList = new ArrayList<>();
                if ("SystemLeader".equals(userRole)){
                    List<DSGCSystemUser> dsgcSystemUsers =sldao.findUserSystemByUserId(userId);
                    Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                    while (iter.hasNext()) {
                        DSGCSystemUser s = (DSGCSystemUser) iter.next();
                        sysCodeList.add(s.getSysCode());
                    }
                }
                List<DSGCService> result =sldao.queryServCommonByCon(q,userRole,sysCodeList);
                List<SVCCommonServResDTO> servResDTOs = new ArrayList<>();
                for (DSGCService temp:result) {
                    SVCCommonServResDTO svcCommonServResDTO = new SVCCommonServResDTO();
                    svcCommonServResDTO.setServId(temp.getServId());
                    svcCommonServResDTO.setServName(temp.getServName());
                    svcCommonServResDTO.setServNo(temp.getServNo());
                    svcCommonServResDTO.setSysCode(temp.getSubordinateSystem());
                    svcCommonServResDTO.setSysName(temp.getAttribue1());
                    servResDTOs.add(svcCommonServResDTO);
                }
                return servResDTOs;
            }

        }
    public PageQueryResult<SVCLogResultResDTO> querySvcLogResultByCon(SVCLogQueryBean q,int pageSize,int pageIndex,String userRole,String userId){
        if ("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)){
                List<DSGCSystemUser> dsgcSystemUsers =sldao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            PageQueryResult<DSGCValidResutl> result =sldao.querySvcLogResultByCon(q,pageSize,pageIndex,userRole,sysCodeList);
            List<SVCLogResultResDTO> servResDTOs = new ArrayList<>();
            for (DSGCValidResutl item:result.getResult()) {
                SVCLogResultResDTO svcLogResultResDTO = new SVCLogResultResDTO();
                svcLogResultResDTO.setServName(item.getAttribue1());
                svcLogResultResDTO.setServNo(item.getServNo());
                svcLogResultResDTO.setSubSystem(item.getAttribue2());
                svcLogResultResDTO.setReqSystem(item.getAttribue3());
                svcLogResultResDTO.setReqSystemCode(item.getSystemCode());
                svcLogResultResDTO.setValidColumn(item.getValidColumn());
                svcLogResultResDTO.setValidType(item.getValidType());
                svcLogResultResDTO.setValidValue(item.getValidValue());
                svcLogResultResDTO.setCreatedBy(item.getLastUpdatedBy());
                svcLogResultResDTO.setCreationDate(item.getLastUpdateDate());
                servResDTOs.add(svcLogResultResDTO);
            }
            PageQueryResult<SVCLogResultResDTO> pageQueryResult = new PageQueryResult<>();
            pageQueryResult.setResult(servResDTOs);
            pageQueryResult.setCount(result.getCount());
            return pageQueryResult;
        }

    }
    public List<String> queryUserSubSystem(String userId){
        List<String> sysCodeList = new ArrayList<>();
        List<DSGCSystemUser> dsgcSystemUsers =sldao.findUserSystemByUserId(userId);
        Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
        while (iter.hasNext()) {
            DSGCSystemUser s = (DSGCSystemUser) iter.next();
            sysCodeList.add(s.getSysCode());
        }
        return sysCodeList;
    }
    @Transactional(rollbackFor = Exception.class)
    public int deleteResultObj(DSGCValidResutl validResut,String userRole,String userId){
        if("Tourist".equals(userRole)){
            return -1;
        }
        Boolean temp = true;
        if("SystemLeader".equals(userRole)){
            List<String> userSubsystemList = this.queryUserSubSystem(userId);
            DSGCService dsgcService =this.sldao.queryServiceByServNo(validResut.getServNo());
            for (int i = 0; i < userSubsystemList.size(); i++) {
                if (!dsgcService.getSubordinateSystem().equals(userSubsystemList.get(i))){
                    temp = false;
                }else {
                    temp = true;
                }
            }
        }
        if(!temp){
            return -1;
        }else {
            this.sldao.deleteResultObj(validResut);
            return 1;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public int addResultObj(DSGCValidResutl validResut,String userRole,String userId){
        if("Tourist".equals(userRole)){
            return -1;
        }
        Boolean temp = true;
        if("SystemLeader".equals(userRole)){
            List<String> userSubsystemList = this.queryUserSubSystem(userId);
            DSGCService dsgcService =this.sldao.queryServiceByServNo(validResut.getServNo());
            for (int i = 0; i < userSubsystemList.size(); i++) {
                if (dsgcService.getSubordinateSystem().equals(userSubsystemList.get(i))){
                    temp = true;
                    break;
                }else {
                    temp = false;
                }
            }
        }
        if (!temp){
            return -1;
        }else {
            this.sldao.addResultObj(validResut);
            return 1;
        }
    }

    public PageQueryResult<SVCBizkeyResDTO> querySvcLogBizkeyByCon(SVCLogQueryBean q,int pageSize,int pageIndex,String userId,String userRole){
        if ("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {
            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)){
                sysCodeList = this.queryUserSubSystem(userId);
            }
            PageQueryResult<DSGCService> result =sldao.querySvcLogBizkeyByCon(q,pageSize,pageIndex,userRole,sysCodeList);
            List<SVCBizkeyResDTO> bizkeyDTOs = new ArrayList<>();
            for (DSGCService item:result.getResult()) {
                SVCBizkeyResDTO svcBizkeyResDTO = new SVCBizkeyResDTO();
                svcBizkeyResDTO.setServNo(item.getServNo());
                svcBizkeyResDTO.setServName(item.getServName());
                svcBizkeyResDTO.setSysCode(item.getSubordinateSystem());
                svcBizkeyResDTO.setSysName(item.getAttribue1());
                svcBizkeyResDTO.setBizResolve(item.getBizResolve());
                bizkeyDTOs.add(svcBizkeyResDTO);
            }
            PageQueryResult<SVCBizkeyResDTO> pageQueryResult = new PageQueryResult<>();
            pageQueryResult.setResult(bizkeyDTOs);
            pageQueryResult.setCount(result.getCount());
            return pageQueryResult;
        }

    }
    @Transactional(rollbackFor = Exception.class)
    public int addBizkeyObj(SVCAddBizkeyVO svcAddBizkeyVO,String userRole,String userId){
        if("Tourist".equals(userRole)){
            return -1;
        }
        Boolean temp = true;
        if("SystemLeader".equals(userRole)){
            List<String> userSubsystemList = this.queryUserSubSystem(userId);
            DSGCService dsgcService =this.sldao.queryServiceByServNo(svcAddBizkeyVO.getServNo());
            for (int i = 0; i < userSubsystemList.size(); i++) {
                if (!dsgcService.getSubordinateSystem().equals(userSubsystemList.get(i))){
                    temp = false;
                }else {
                    temp = true;
                }
            }
        }
        if (!temp){
            return -1;
        }else {
            this.sldao.delBizkeyByServNo(svcAddBizkeyVO.getServNo());
            List<DSGCServInterfaceNode> list = svcAddBizkeyVO.getKeywordList();
            for (DSGCServInterfaceNode item: svcAddBizkeyVO.getKeywordList()) {
                item.setServNo(svcAddBizkeyVO.getServNo());
            }
            this.sldao.addbizkeyObj(svcAddBizkeyVO.getKeywordList());
            return 1;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public int deleteBizkeyObj(SVCLogQueryBean param,String userRole,String userId){
        if("Tourist".equals(userRole)){
            return -1;
        }
        Boolean temp = true;
        if("SystemLeader".equals(userRole)){
            List<String> userSubsystemList = this.queryUserSubSystem(userId);
            DSGCService dsgcService =this.sldao.queryServiceByServNo(param.getCon0());
            for (int i = 0; i < userSubsystemList.size(); i++) {
                if (!dsgcService.getSubordinateSystem().equals(userSubsystemList.get(i))){
                    temp = false;
                }else {
                    temp = true;
                }
            }
        }
        if(!temp){
            return -1;
        }else {
            this.sldao.deleteBizkeyObj(param.getCon0());
            return 1;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public List<SVCKeywordResDTO> queryServBizkey(SVCLogQueryBean param,String userRole,String userId){
        List<SVCKeywordResDTO> result = new ArrayList<>();
        if("Tourist".equals(userRole)){
            return result;
        }
        Boolean temp = true;
        if("SystemLeader".equals(userRole)){
            List<String> userSubsystemList = this.queryUserSubSystem(userId);
            DSGCService dsgcService =this.sldao.queryServiceByServNo(param.getCon0());
            for (int i = 0; i < userSubsystemList.size(); i++) {
                if (dsgcService.getSubordinateSystem().equals(userSubsystemList.get(i))){
                    temp = true;
                    break;
                }else {
                    temp = false;
                }
            }
        }
        if(!temp){
            return result;
        }else {
          List<DSGCServInterfaceNode> list =  this.sldao.queryServBizkey(param.getCon0());
            for (DSGCServInterfaceNode item:list) {
                SVCKeywordResDTO svcKeywordResDTO = new SVCKeywordResDTO();
                svcKeywordResDTO.setServNo(item.getServNo());
                svcKeywordResDTO.setNodeName(item.getNodeName());
                svcKeywordResDTO.setNodeDesc(item.getNodeDesc());
                svcKeywordResDTO.setNodeType(item.getNodeType());
                svcKeywordResDTO.setBusKeyword(item.getBusKeyword());
                result.add(svcKeywordResDTO);
            }
            return result;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateBizResolve(UpdateBizResolveVO param,String userRole,String userId) throws Exception{
        Boolean isEdit = false;
        if("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
            isEdit = true;
        }else if("SystemLeader".equals(userRole)){
            DSGCService dsgcService = sldao.findServByServNo(param.getServNo());
            List<DSGCSystemUser> dsgcSystemUsers = sldao.findUserSystemByUserId(userId);
            for (DSGCSystemUser item:dsgcSystemUsers) {
                String sysCode = item.getSysCode();
                if(dsgcService.getSubordinateSystem().equals(sysCode)){
                    isEdit = true;

                }else {
                    isEdit = false;
                }
            }

        }else {
            isEdit = false;
        }
        if (isEdit){
            sldao.updateBizResolve(param);
        }else {
            throw new Exception("无权限修改！");
        }

    }

    public  Map<String,Object> queryEsbEnv(){
        //List<EsbEnvInfoCfgDTO>
        Map<String,Object> map = new HashMap<>();
        List<EsbEnvInfoCfgDTO> result = new ArrayList<>();
      List<DSGCEnvInfoCfg>  list = sldao.queryEsbEnv();
        FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
      Iterator<DSGCEnvInfoCfg> iterator = list.iterator();
      while (iterator.hasNext()){
          DSGCEnvInfoCfg dsgcEnvInfoCfg = iterator.next();
          EsbEnvInfoCfgDTO dto = new EsbEnvInfoCfgDTO();
          dto.setEnvCode(dsgcEnvInfoCfg.getEnvCode());
          dto.setEnvName(dsgcEnvInfoCfg.getEnvName());
          result.add(dto);
      }
      map.put("envList",result);
      map.put("currentEnv",fndProperties.getPropertyValue());
      return map;
    }

    public List<DSGCServInterfaceNode> getKeyword(String servNo) {
        return this.sldao.getKeyword(servNo);
    }
}
//    insert into DSGC_SVCGEN_OBJ
//        select deve_id,
//        'SVC',
//        serv_no,
//        t.serv_code_name,
//        t.serv_no,
//        'Y',
//        t.object_version_number,
//        t.created_by,
//        t.creation_date,
//        t.last_updated_by,
//        t.last_update_date,
//        null,
//        null,
//        null,
//        null,
//        null
//        from dsgc_svcgen_tmpl t
//        where t.is_profile = 'N'