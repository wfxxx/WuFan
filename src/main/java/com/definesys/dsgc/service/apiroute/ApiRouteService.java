package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            String[] envArr = dagCodeVersionBean.getEnvTargets().trim().split(",");
            dto.setEnvList(Arrays.asList(envArr));
            dto.setVid(dagCodeVersionBean.getVid());
            dto.setSourType(dagCodeVersionBean.getSourType());
            dto.setSourCode(dagCodeVersionBean.getSourCode());
            result.add(dto);
        }
        return result;
    }
public List<RouteConfigDTO> queryRouteConfigList(CommonReqBean param){
    List<DagCodeVersionBean> list = apiRouteDao.queryRouteConfigListBySourCode(param);
    List<RouteConfigDTO> result = new ArrayList<>();
    Iterator<DagCodeVersionBean> iterator = list.iterator();
    while (iterator.hasNext()){
        DagCodeVersionBean dagCodeVersionBean = iterator.next();
        RouteConfigDTO dto = new RouteConfigDTO();
        dto.setVid(dagCodeVersionBean.getVid());
        dto.setEnvTargets(dagCodeVersionBean.getEnvTargets());
        dto.setCreationDate(dagCodeVersionBean.getCreationDate());
        dto.setvName(dagCodeVersionBean.getvName());
        result.add(dto);
    }
    return result;
}
public void addRouteConfig(AddRouteConfigVO param){
    if (param != null){
        DagCodeVersionBean dagCodeVersionBean = new DagCodeVersionBean();
        dagCodeVersionBean.setSourCode(param.getDrId());
        dagCodeVersionBean.setSourType(param.getCourType());
        dagCodeVersionBean.setvName(param.getConfigName());
    }
}
}
