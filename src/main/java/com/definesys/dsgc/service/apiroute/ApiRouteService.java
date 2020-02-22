package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiRouteService {
    @Autowired
    private ApiRouteDao apiRouteDao;
    @Autowired
    private SVCLogDao sldao;

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
       List<DagEnvInfoCfgBean> list = apiRouteDao.queryApiEnv();
        List<DagEnvInfoCfgDTO> result = new ArrayList<>();
        Iterator<DagEnvInfoCfgBean> infoCfgBeanIterator = list.iterator();
        while (infoCfgBeanIterator.hasNext()){
            DagEnvInfoCfgBean dagEnvInfoCfgBean = infoCfgBeanIterator.next();
            DagEnvInfoCfgDTO dagEnvInfoCfgDTO = new DagEnvInfoCfgDTO();
            dagEnvInfoCfgDTO.setEnvCode(dagEnvInfoCfgBean.getEnvCode());
            dagEnvInfoCfgDTO.setEnvName(dagEnvInfoCfgBean.getEnvName());
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
    List<DagEnvInfoCfgBean> envList = apiRouteDao.queryApiEnv();
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
            for (String temp:envArr){
                for (int i = 0; i < envList.size(); i++) {
                    if (temp.equals(envList.get(i).getEnvCode()) && i ==0){
                        envNameStr.append(envList.get(i).getEnvName());
                        break;
                    }else {
                        envNameStr.append("，"+envList.get(i).getEnvName());
                        break;
                    }
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
    }
}
    @Transactional(rollbackFor = Exception.class)
    public void updateRouteConfig(AddRouteConfigVO param){
        if (param != null){
            DagCodeVersionBean dagCodeVersionBean = new DagCodeVersionBean();
            dagCodeVersionBean.setSourCode(param.getRouteCode());
            dagCodeVersionBean.setSourType(param.getCourType());
            dagCodeVersionBean.setvName(param.getConfigName());
            if(param.getEnabledEnv() != null){
                String join = String.join(",", param.getEnabledEnv());
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
    public void delRouteConfig(CommonReqBean param){
        DagCodeVersionBean dagRoutesBean =  apiRouteDao.queryRouteConfigByid(param.getCon0());
        if (dagRoutesBean != null){
            apiRouteDao.delRouteConfig(param.getCon0());
        }
    }
}
