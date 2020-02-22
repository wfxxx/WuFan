package com.definesys.dsgc.service.apibs;

import ch.qos.logback.core.status.OnPrintStreamStatusListenerBase;
import com.definesys.dsgc.service.apibs.bean.*;
import com.definesys.dsgc.service.apiroute.bean.DagEnvInfoCfgBean;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ApiBsService {
    @Autowired
    private ApiBsDao apiBsDao;

    @Autowired
    private SVCLogDao sldao;

    public PageQueryResult queryApiBsList(CommonReqBean param,String userId,String userRole,int pageSize,int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult queryApiBsList = apiBsDao.queryApiBsList(param,pageSize,pageIndex,userRole,sysCodeList);
        PageQueryResult result = new PageQueryResult();
        List<DagBsListDTO> listDTOS = new ArrayList<>();
        Iterator<DagBsbean> iterator = queryApiBsList.getResult().iterator();
        while (iterator.hasNext()){
            DagBsbean dagBsbean = iterator.next();
            DagBsListDTO dagBsListDTO = new DagBsListDTO();
            dagBsListDTO.setAppName(dagBsbean.getAppName());
            dagBsListDTO.setBsCode(dagBsbean.getBsCode());
            dagBsListDTO.setBsDesc(dagBsbean.getBsDesc());
            dagBsListDTO.setBsId(dagBsbean.getBsId());
            listDTOS.add(dagBsListDTO);
        }
        result.setCount(queryApiBsList.getCount());
        result.setResult(listDTOS);
        return result;
    }

    public List<String> queryApiBsByCustomInput(CommonReqBean param){
    List<DagBsbean> list = apiBsDao.queryApiBsByCustomInput(param);
    Iterator<DagBsbean> iterator = list.iterator();
    List<String> result = new ArrayList<>();
    while (iterator.hasNext()){
        DagBsbean dagBsbean = iterator.next();
        result.add(dagBsbean.getBsCode());
    }
    return result;
    }

    public void updateDagBs(DagBsbean dagBsbean){
          apiBsDao.updateDagBs(dagBsbean);
    }

    public DagBsbean queryApiBsByCode(String code){
        DagBsbean result=apiBsDao.queryApiBsByCode(code);
        Map<String,Object> appCode=apiBsDao.querySysNameByCode(result.getAppCode());
        result.setAppName((String) appCode.get("SYS_NAME"));
        return result;
    }

    public void addApiBs(DagBsbean dagBsbean){
        apiBsDao.addApiBs(dagBsbean);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApiBs(CommonReqBean param)throws Exception{
       DagBsbean dagBsbean = apiBsDao.queryApiBsById(param);
       if (dagBsbean != null){
           apiBsDao.delApiBs(param);
       }else {
           throw new Exception("服务不存在！");
       }
    }

    public DagBsDtiBean queryDagBsDtiByVid(String vid){

        return apiBsDao.queryDagBsDtiByVid(vid);
    }

    public DagBsDtiBean updateDagBsDti(DagBsDtiBean dagBsDtiBean) {
        return apiBsDao.updateDagBsDti(dagBsDtiBean);
    }

    public DagBsDtiBean addDagBsDti(DagBsDtiBean dagBsDtiBean) {
        return apiBsDao.addDagBsDti(dagBsDtiBean);
    }

    public List<DagCodeVersionBean> queryDagCodeVersionBySource(String sourceId){
        List<DagCodeVersionBean> result=apiBsDao.queryDagCodeVersionBySource(sourceId);
        for(DagCodeVersionBean item:result){
            List<String> envList=queryEnv(item.getEnvTargets());
            item.setEnvTargetsName(envList.toString());
        }
       return result;
    }

    public DagCodeVersionBean queryDagCodeVersionByid(String id){
        DagCodeVersionBean result=apiBsDao.queryDagCodeVersionByid(id);
        List<String> envList=queryEnv(result.getEnvTargets());
        result.setEnvTargetsName(envList.toString());
        return result;
    }


    public void delDagCodeVersionByid(String id){
         apiBsDao.delDagCodeVersionByid(id);
    }

    public DagCodeVersionBean updateDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        return apiBsDao.updateDagCodeVersion(dagCodeVersionBean);
    }

    public void addDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
         apiBsDao.addDagCodeVersion(dagCodeVersionBean);
    }

    public List<String> queryEnv(String envListStr) {
        List<String> envList= Arrays.asList(envListStr.split(","));
        List<DagEnvInfoCfgBean> envValue=apiBsDao.queryEnv(envList);
        List<String> result=new ArrayList<String>();
        for(DagEnvInfoCfgBean item : envValue){
            result.add(item.getEnvName());
        }
        return result;
    }


}
