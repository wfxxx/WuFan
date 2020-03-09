package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.dsgc.service.dagclient.DAGClientService;
import com.definesys.dsgc.service.dagclient.bean.DAGDeployReqVO;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApiRouteService {
    @Autowired
    private ApiRouteDao apiRouteDao;
    @Autowired
    private SVCLogDao sldao;

    @Autowired
    private DAGClientService dagClientService;

    public PageQueryResult queryApiRouteList(CommonReqBean param, String userId, String userRole, int pageSize, int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult pageQueryResult = apiRouteDao.queryApiRouteList(param,pageSize,pageIndex,userRole,sysCodeList);
        return pageQueryResult;
    }

    public void addApiRoute(DagRoutesBean dagRoutesBean){
        apiRouteDao.addApiRoute(dagRoutesBean);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApiRoute(CommonReqBean param)throws Exception{
        DagRoutesBean dagBsbean = apiRouteDao.queryApiRouteById(param);
        if (dagBsbean != null){
            apiRouteDao.delApiRoute(param);
        }else {
            throw new Exception("路由不存在！");
        }

    }
    public DagRoutesBean queryRouteDetail(CommonReqBean param){
      return  apiRouteDao.queryRouteDetail(param);
    }
    public List<DagEnvInfoCfgDTO> queryApiEnv(){
       List<DSGCEnvInfoCfg> list = apiRouteDao.queryApiEnv();
        List<DagEnvInfoCfgDTO> result = new ArrayList<>();
        Iterator<DSGCEnvInfoCfg> infoCfgBeanIterator = list.iterator();
        while (infoCfgBeanIterator.hasNext()){
            DSGCEnvInfoCfg dsgcEnvInfoCfg = infoCfgBeanIterator.next();
            DagEnvInfoCfgDTO dagEnvInfoCfgDTO = new DagEnvInfoCfgDTO();
            dagEnvInfoCfgDTO.setEnvCode(dsgcEnvInfoCfg.getEnvCode());
            dagEnvInfoCfgDTO.setEnvName(dsgcEnvInfoCfg.getEnvName());
            result.add(dagEnvInfoCfgDTO);
        }
        return result;
    }
    public List<DagCodeVersionDTO> queryApiCodeVersion(CommonReqBean param){
        List<DagCodeVersionBean> list = apiRouteDao.queryApiCodeVersion(param);
        List<DagCodeVersionDTO> result = new ArrayList<>();
        Iterator<DagCodeVersionBean> iterator = list.iterator();
        while (iterator.hasNext()){
            DagCodeVersionBean dagCodeVersionBean = iterator.next();
            DagCodeVersionDTO dto = new DagCodeVersionDTO();
//            if(dagCodeVersionBean.getEnvTargets().indexOf(",") != -1){
//
//
//            }
            String[] envArr = dagCodeVersionBean.getEnvTargets().trim().split(",");
            dto.setEnvList(Arrays.asList(envArr));
            dto.setVid(dagCodeVersionBean.getVid());
            dto.setvName(dagCodeVersionBean.getvName());
            dto.setSourType(dagCodeVersionBean.getSourType());
            dto.setSourCode(dagCodeVersionBean.getSourCode());
            result.add(dto);
        }
        return result;
    }

    public DagCodeVersionDTO queryCodeVersionById(CommonReqBean param){
        DagCodeVersionBean dagCodeVersionBean = apiRouteDao.queryCodeVersionById(param);
        DagCodeVersionDTO dto = new DagCodeVersionDTO();
        String[] envArr = dagCodeVersionBean.getEnvTargets().trim().split(",");
        dto.setEnvList(Arrays.asList(envArr));
        dto.setVid(dagCodeVersionBean.getVid());
        dto.setvName(dagCodeVersionBean.getvName());
        dto.setSourType(dagCodeVersionBean.getSourType());
        dto.setSourCode(dagCodeVersionBean.getSourCode());
        return dto;

    }
public List<RouteConfigDTO> queryRouteConfigList(CommonReqBean param){
    List<DagCodeVersionBean> list = apiRouteDao.queryRouteConfigListBySourCode(param);
    List<DSGCEnvInfoCfg> envList = apiRouteDao.queryApiEnv();
    List<RouteConfigDTO> result = new ArrayList<>();
    Iterator<DagCodeVersionBean> iterator = list.iterator();
    while (iterator.hasNext()){
        DagCodeVersionBean dagCodeVersionBean = iterator.next();
        RouteConfigDTO dto = new RouteConfigDTO();
        dto.setVid(dagCodeVersionBean.getVid());
      //  dto.setEnvTargets(dagCodeVersionBean.getEnvTargets());
        if(StringUtil.isNotBlank(dagCodeVersionBean.getEnvTargets())){
            String[] envArr = dagCodeVersionBean.getEnvTargets().trim().split(",");
            StringBuilder envNameStr = new StringBuilder();
            for (int j= 0;j<envArr.length;j++){
                for (int i = 0; i < envList.size(); i++) {
                    if (envArr[j].equals(envList.get(i).getEnvCode())){
                        if(j >0){
                            envNameStr.append(",");
                        }
                        envNameStr.append(envList.get(i).getEnvName());
                        break;
                    }
//                      else if(envArr[j].equals(envList.get(i).getEnvCode())){
//                        envNameStr.append("，"+envList.get(i).getEnvName());
//                        break;
//                    }
                }
            }
            dto.setEnvTargets(envNameStr.toString());
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(dagCodeVersionBean.getCreationDate());
        dto.setCreationDate(str);
        dto.setvName(dagCodeVersionBean.getvName());
        result.add(dto);
    }
    return result;
}
@Transactional(rollbackFor = Exception.class)
public void addRouteConfig(AddRouteConfigVO param){
    if (param != null){
        DagCodeVersionBean dagCodeVersionBean = new DagCodeVersionBean();
        dagCodeVersionBean.setSourCode(param.getRouteCode());
        dagCodeVersionBean.setSourType(param.getCourType());
        dagCodeVersionBean.setvName(param.getConfigName());
        if(param.getEnabledEnv() != null){
            String join = String.join(",", param.getEnabledEnv());
            dagCodeVersionBean.setEnvTargets(join);
        }
        apiRouteDao.addRouteConfig(dagCodeVersionBean);
        DagPluginUsingBean dagPluginUsingBean = new DagPluginUsingBean();
        dagPluginUsingBean.setVid(dagCodeVersionBean.getVid());
        dagPluginUsingBean.setIsEnable("Y");
        dagPluginUsingBean.setPluginCode("1");
        apiRouteDao.addRoutePlugin(dagPluginUsingBean);
        dagPluginUsingBean.setPluginCode("2");
        apiRouteDao.addRoutePlugin(dagPluginUsingBean);

    }
}
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteConfig(DagCodeVersionDTO param){
        if (param != null){
            DagCodeVersionBean dagCodeVersionBean = new DagCodeVersionBean();
            dagCodeVersionBean.setVid(param.getVid());
            dagCodeVersionBean.setvName(param.getvName());
            if(param.getEnvList() != null){
                String join = String.join(",", param.getEnvList());
                dagCodeVersionBean.setEnvTargets(join);
            }
            apiRouteDao.updateRouteConfig(dagCodeVersionBean);
        }
    }
    @Transactional(rollbackFor = Exception.class)
public void updateRoutePathStrip(DagRoutesBean param){
    CommonReqBean commonReqBean = new CommonReqBean();
    commonReqBean.setCon0(param.getRouteCode());
    DagRoutesBean dagRoutesBean =  apiRouteDao.queryRouteDetail(commonReqBean);
    if (dagRoutesBean != null){
        apiRouteDao.updateRoutePathStrip(param);
    }
}
@Transactional(rollbackFor = Exception.class)
    public void updateRouteDesc(DagRoutesBean param){
        CommonReqBean commonReqBean = new CommonReqBean();
        commonReqBean.setCon0(param.getRouteCode());
        DagRoutesBean dagRoutesBean =  apiRouteDao.queryRouteDetail(commonReqBean);
        if (dagRoutesBean != null){
            apiRouteDao.updateRouteDesc(param);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public int delRouteConfig(CommonReqBean param){
       Boolean isDeploy = apiRouteDao.queryRouteConfigIsDeployed(param.getCon0());
       if(isDeploy){
           return -1;
       }
        PageQueryResult<DagPluginUsingBean> pluginrResult = apiRouteDao.queryRoutePlug(param.getCon0(),1,1);
        if(pluginrResult.getCount() > 0){
            apiRouteDao.delRoutePlugin(param.getCon0());
        }
        List<DagRoutesHostnameBean> hostnameBeans = apiRouteDao.queryRouteAnotherRule(param);
        if(hostnameBeans !=null && hostnameBeans.size() > 0){
            apiRouteDao.delRouteAnotherRuleById(param.getCon0());
        }
        DagCodeVersionBean dagRoutesBean =  apiRouteDao.queryRouteConfigByid(param.getCon0());
        if (dagRoutesBean != null){
            apiRouteDao.delRouteConfig(param.getCon0());
        }
    return 1;
    }
    public PageQueryResult<QueryRoutePlugDTO> queryRoutePlug(CommonReqBean param,int pageIndex,int pageSize){
        PageQueryResult<DagPluginUsingBean> dagPluginUsingBeanList =  apiRouteDao.queryRoutePlug(param.getCon0(),pageIndex,pageSize);
        PageQueryResult<QueryRoutePlugDTO> result = new PageQueryResult<>();
        List<QueryRoutePlugDTO> queryRoutePlugDTOS = new ArrayList<>();
        Iterator<DagPluginUsingBean> iterator = dagPluginUsingBeanList.getResult().iterator();
        while (iterator.hasNext()){
            DagPluginUsingBean dagPluginUsingBean = iterator.next();
            QueryRoutePlugDTO queryRoutePlugDTO = new QueryRoutePlugDTO();
            queryRoutePlugDTO.setDpuId(dagPluginUsingBean.getDpuId());
            if("Y".equals(dagPluginUsingBean.getIsEnable())){
                queryRoutePlugDTO.setEnable(true);
            }else {
                queryRoutePlugDTO.setEnable(false);
            }

            queryRoutePlugDTO.setPluginCode(dagPluginUsingBean.getPluginCode());
            queryRoutePlugDTO.setPluginName(dagPluginUsingBean.getPluginName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = format.format(dagPluginUsingBean.getCreationDate());
            queryRoutePlugDTO.setCreationDate(str);
            queryRoutePlugDTOS.add(queryRoutePlugDTO);
        }
        result.setResult(queryRoutePlugDTOS);
        result.setCount(dagPluginUsingBeanList.getCount());
        return result;
    }
    public List<QueryRouteHostnameDTO> queryRouteAnotherRule(CommonReqBean param){
        List<QueryRouteHostnameDTO> result = new ArrayList<>();
        List<DagRoutesHostnameBean> hostnameBeans = apiRouteDao.queryRouteAnotherRule(param);
        Iterator<DagRoutesHostnameBean> iterator = hostnameBeans.iterator();
        while (iterator.hasNext()){
            DagRoutesHostnameBean dagRoutesHostnameBean = iterator.next();
            QueryRouteHostnameDTO queryRouteHostnameDTO = new QueryRouteHostnameDTO();
            queryRouteHostnameDTO.setDrhId(dagRoutesHostnameBean.getDrhId());
            queryRouteHostnameDTO.setHostName(dagRoutesHostnameBean.getHostName());
            result.add(queryRouteHostnameDTO);
        }
        return result;
    }
    @Transactional(rollbackFor = Exception.class)
    public void deployRoute(DeployRouteVO param,String userId) throws Exception{
        DAGDeployReqVO dagDeployReqVO = new DAGDeployReqVO();
        dagDeployReqVO.setVid(param.getVid());
        dagDeployReqVO.setEnvCode(param.getDeployEnv());
        dagDeployReqVO.setDeployDesc(param.getDeployDesc());
        String res  = dagClientService.deploy(dagDeployReqVO,userId);
        if (!"S".equals(res)) {
            throw new Exception(res);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void copyRouteConfig(CommonReqBean param){
        DagCodeVersionBean dagRoutesBean =  apiRouteDao.queryRouteConfigByid(param.getCon0());
        if(dagRoutesBean != null){
            dagRoutesBean.setVid(null);
            dagRoutesBean.setvName("副本-"+dagRoutesBean.getvName());
            apiRouteDao.addRouteConfig(dagRoutesBean);
            List<DagPluginUsingBean> dagPluginUsingBeanList = apiRouteDao.queryRoutePlugByVid(param.getCon0());
            for (DagPluginUsingBean item:dagPluginUsingBeanList) {
                item.setDpuId(null);
                item.setVid(dagRoutesBean.getVid());
                apiRouteDao.addRoutePlugin(item);
            }
            List<DagRoutesHostnameBean> hostnameBeans = apiRouteDao.queryRouteAnotherRule(param);
            for (DagRoutesHostnameBean item:hostnameBeans) {
                item.setDrhId(null);
                item.setVid(dagRoutesBean.getVid());
                apiRouteDao.addRouteAnotherRule(item);
            }

        }

    }
    @Transactional(rollbackFor = Exception.class)
    public void addRouteDomain(AddRouteDomainVO param){
        if(param != null){
            DagRoutesHostnameBean dagRoutesHostnameBean = new DagRoutesHostnameBean();
            dagRoutesHostnameBean.setVid(param.getVid());
            dagRoutesHostnameBean.setHostName(param.getLocation()+":"+param.getPort());
            apiRouteDao.addRouteAnotherRule(dagRoutesHostnameBean);
        }

    }
    @Transactional(rollbackFor = Exception.class)
    public void delRouteDomain(CommonReqBean param){
        if(param != null){
            DagRoutesHostnameBean dagRoutesHostnameBean =  apiRouteDao.queryRouteDomainById(param.getCon0());
            if(dagRoutesHostnameBean != null){
                apiRouteDao.delRouteDomain(param.getCon0());
            }
        }
    }
    public Boolean checkRouteCodeIsExsit(CommonReqBean param){
           Boolean isExist =  apiRouteDao.checkRouteCodeIsExsit(param.getCon0());
          return isExist;
    }
}
