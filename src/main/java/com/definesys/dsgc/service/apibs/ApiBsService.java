package com.definesys.dsgc.service.apibs;

import ch.qos.logback.core.status.OnPrintStreamStatusListenerBase;
import com.definesys.dsgc.service.apibs.bean.*;
import com.definesys.dsgc.service.apiplugin.bean.DAGPluginListVO;
import com.definesys.dsgc.service.apiroute.bean.DagEnvInfoCfgBean;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApiBsService {
    @Autowired
    private ApiBsDao apiBsDao;
    @Autowired
    private PluginService pluginService;

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
            dagBsListDTO.setAppCode(dagBsbean.getAppCode());
            if(dagBsbean.getEnvCode()!=null&&dagBsbean.getEnvCode().length()>0) {
                List<DSGCEnvInfoCfg> value = apiBsDao.queryDeplogDev(dagBsbean.getEnvCode());
                if (value != null) {
                    for (DSGCEnvInfoCfg valueItem : value) {
                        dagBsbean.setEnvName(dagBsbean.getEnvName() + valueItem.getEnvName()+",");
                    }
                    String envName=dagBsbean.getEnvName();
                    dagBsbean.setEnvName(envName.trim().substring(0,envName.length()-1));
                }
            }
            dagBsListDTO.setEnvCode(dagBsbean.getEnvCode());
            dagBsListDTO.setEnvName(dagBsbean.getEnvName());
            listDTOS.add(dagBsListDTO);
        }




        result.setCount(queryApiBsList.getCount());
        result.setResult(listDTOS);
        return result;
    }


    public DagBsbean checkSame(DagBsbean bean){
        return apiBsDao.checkSame(bean);
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


    public List<Map<String,Object>> queryDeployByVid(String vid){
        return  apiBsDao.queryDeployByVid(vid);
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

    @Transactional(rollbackFor = Exception.class)
    public void delDagCodeVersionByid(String id) throws Exception {
        //删除技术信息
        apiBsDao.delDagBsDtiByVid(id);
        //删除插件
            //删除插件具体参数
        List<DagPlugUsingBean> value= queryPluginUsing(id);
        for(DagPlugUsingBean item:value){
            pluginService.deletePluginContext(item.getVid(),item.getPluginCode());
        }
            //删除插件使用表
         delPluginUsingByVid(id);
        //删除配置
         apiBsDao.delDagCodeVersionByid(id);
    }

    public DagCodeVersionBean updateDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        return apiBsDao.updateDagCodeVersion(dagCodeVersionBean);
    }

    public String addDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
         return apiBsDao.addDagCodeVersion(dagCodeVersionBean);

    }

    @Transactional(rollbackFor = Exception.class)
    public void copyDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){

        Date date=new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        String vid=dagCodeVersionBean.getVid();
        String name=dagCodeVersionBean.getvName();
        dagCodeVersionBean.setVid(null);
        int index=name.indexOf("-副本");
        if(index>-1){
            String newName=name.substring(0,index)+"-副本"+sdf.format(date);
            dagCodeVersionBean.setvName(newName);
        }else{
            String newName=name+"-副本"+sdf.format(date);
            dagCodeVersionBean.setvName(newName);
        }
        //添加配置
        apiBsDao.addDagCodeVersion(dagCodeVersionBean);
       //添加信息技术
        DagBsDtiBean newDagBsDtiBean= apiBsDao.queryDagBsDtiByVid(vid);
        if(newDagBsDtiBean!=null&&newDagBsDtiBean.getDbdId()!=null){
            newDagBsDtiBean.setDbdId(null);
            newDagBsDtiBean.setVid(dagCodeVersionBean.getVid());
            apiBsDao.addDagBsDti(newDagBsDtiBean);
        }
        //添加插件
        List<DagPlugUsingBean> newDagPlugUsingBean=apiBsDao.queryPluginUsing(vid);
        for(DagPlugUsingBean  dagPlugUsingBean:newDagPlugUsingBean){
            dagPlugUsingBean.setDpuId(null);
            dagPlugUsingBean.setVid(dagCodeVersionBean.getVid());
            addPluginUsing(dagPlugUsingBean);
        }
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


    public DagPlugUsingBean addPluginUsing(DagPlugUsingBean dagPlugUsingBean){
        return apiBsDao.addPluginUsing(dagPlugUsingBean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delPluginUsing(DagPlugUsingBean dagPlugUsingBean) throws Exception {
        //删除插件内容，
        pluginService.deletePluginContext(dagPlugUsingBean.getDpuId(),dagPlugUsingBean.getPluginCode());
        //删除插件
        apiBsDao.delPluginUsing(dagPlugUsingBean.getDpuId());
    }

    public void delPluginUsingByVid(String vid){
        apiBsDao.delPluginUsingByVid(vid);
    }

    public DagPlugUsingBean queryPluginUsingByid(String id){
        return apiBsDao.queryPluginUsingByid(id);
    }

    public void updatePluginUsing(DagPlugUsingBean dagPlugUsingBean){
        apiBsDao.updatePluginUsing(dagPlugUsingBean);
    }

    public List<DagPlugUsingBean> queryPluginUsing(String vid){
        List<DagPlugUsingBean> reuslt=apiBsDao.queryPluginUsing(vid);
        for(DagPlugUsingBean item: reuslt){
            DagPlugStoreBean dagPlugStoreBean=apiBsDao.queryPluginStoreByCode(item.getPluginCode());
            item.setPluginName(dagPlugStoreBean.getPluginName());
        }
        return reuslt;
    }

    public List<Map<String,Object>> queryPluginStore(){
        List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> typeList=apiBsDao.queryPluginStoreTypeTotal();
        for(Map<String,Object> item: typeList){
            Map<String,Object> itemResult=new HashMap<String,Object>();
            String typeName=(String) item.get("PLUGIN_TYPE");
            List<DagPlugStoreBean> dagPlugStoreBeanTypeList=apiBsDao.queryPluginStoreByType(typeName);
            itemResult.put("typeName",typeName);
            itemResult.put("value",dagPlugStoreBeanTypeList);
            result.add(itemResult);
        }
        Collections.reverse(result);
        return result;
    }


    public void updatePluginUsingConsumer(String consumer,String dupId){
        apiBsDao.updatePluginUsingConsumer(consumer,dupId);
    }


}
