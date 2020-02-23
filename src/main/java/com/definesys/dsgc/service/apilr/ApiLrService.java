package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.sun.deploy.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiLrService {
    @Autowired
    private ApiLrDao apiLrDao;

    public PageQueryResult queryApiLrList(CommonReqBean param, String userId, String userRole, int pageSize, int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   apiLrDao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult queryApiBsList = apiLrDao.queryApiLrList(param,pageSize,pageIndex,userRole,sysCodeList);
        PageQueryResult result = new PageQueryResult();
        List<DagLrListDTO> listDTOS = new ArrayList<>();
        Iterator<DagLrbean> iterator = queryApiBsList.getResult().iterator();
        while (iterator.hasNext()){
            DagLrbean dagLrbean = iterator.next();
            DagLrListDTO dagLrListDTO = new DagLrListDTO();
            dagLrListDTO.setAppName(dagLrbean.getAppName());
            dagLrListDTO.setLrName(dagLrbean.getLrName());
            dagLrListDTO.setLrDesc(dagLrbean.getLrDesc());
            dagLrListDTO.setDlId(dagLrbean.getDlId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dagLrListDTO.setCreationDate(sdf.format(dagLrbean.getCreationDate()));
            listDTOS.add(dagLrListDTO);
        }
        result.setCount(queryApiBsList.getCount());
        result.setResult(listDTOS);
        return result;
    }

    public void addApiLr(DagLrbean dagLrbean){
        apiLrDao.addApiLr(dagLrbean);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApiLr(CommonReqBean param)throws Exception{
        DagLrbean dagLrbean = apiLrDao.queryApiLrById(param);
        if (dagLrbean != null){
            apiLrDao.delApiLr(param);
        }else {
            throw new Exception("服务不存在！");
        }

    }

    public DagLrbean queryLrDetail(CommonReqBean param){
        return  apiLrDao.queryLrDetail(param);
    }

    public List<LrConfigDTO> queryLrConfigList(CommonReqBean param){
        List<DagCodeVersionBean> list = apiLrDao.queryLrConfigListBySourCode(param);
        List<LrConfigDTO> result = new ArrayList<>();
        Iterator<DagCodeVersionBean> iterator = list.iterator();
        while (iterator.hasNext()){
            DagCodeVersionBean dagCodeVersionBean = iterator.next();
            LrConfigDTO dto = new LrConfigDTO();
            dto.setVid(dagCodeVersionBean.getVid());
            dto.setEnvTargets(dagCodeVersionBean.getEnvTargets());
            if(StringUtil.isNotBlank(dagCodeVersionBean.getEnvTargets())){
                String[] envArr = dagCodeVersionBean.getEnvTargets().trim().split(",");
                List<DagEnvInfoCfgBean> dagEnvInfoCfgBeans = apiLrDao.queryApiEnvName(envArr);
                List<String> envName = new ArrayList<>();
                for(int i = 0; i < dagEnvInfoCfgBeans.size(); i++){
                    envName.add(dagEnvInfoCfgBeans.get(i).getEnvName());
                }
                String envNameStr = StringUtils.join(envName, ",");
                dto.setEnvTargets(envNameStr);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dto.setCreationDate(sdf.format(dagCodeVersionBean.getCreationDate()));
            dto.setvName(dagCodeVersionBean.getvName());
            dto.setEnvcode(dagCodeVersionBean.getEnvTargets());
            result.add(dto);
        }
        return result;
    }

    public void addLrConfig(AddLrConfigVO param){
        DagCodeVersionBean dagCodeVersionBean = new DagCodeVersionBean();
        if (param != null){
            dagCodeVersionBean.setSourCode(param.getDlId());
            dagCodeVersionBean.setSourType(param.getCourType());
            dagCodeVersionBean.setvName(param.getConfigName());
            List<String> envList = param.getEnabledEnv();
            String str = StringUtils.join(envList, ",");
            dagCodeVersionBean.setEnvTargets(str);
        }
        apiLrDao.addLrConfig(dagCodeVersionBean);
    }

    public void deLrConfig(CommonReqBean param){
        apiLrDao.deLrConfig(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateLrDesc(DagLrbean param){
        CommonReqBean commonReqBean = new CommonReqBean();
        commonReqBean.setCon0(param.getLrName());
        DagLrbean dagLrbean =  apiLrDao.queryLrDetail(commonReqBean);
        if (dagLrbean != null){
            apiLrDao.updateLrDesc(param);
        }
    }

}
