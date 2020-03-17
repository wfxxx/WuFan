package com.definesys.dsgc.service.apiplugin;

import com.definesys.dsgc.service.apilr.ApiLrDao;
import com.definesys.dsgc.service.apiplugin.bean.CommonReqBean;
import com.definesys.dsgc.service.apiplugin.bean.DAGPluginListVO;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ApiPlugInService {
    @Autowired
    private ApiPlugInDao apiPlugInDao;
    @Autowired
    private ApiLrDao apiLrDao;


    public PageQueryResult queryPluginList(CommonReqBean param, String userId, String userRole, int pageSize, int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   apiLrDao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult result=apiPlugInDao.queryPluginList(param,pageSize,pageIndex,userRole,sysCodeList);
        List<DAGPluginListVO> queryPluginList= result.getResult();
        for(DAGPluginListVO item:queryPluginList){
            if(item.getEnvCode()!=null&&item.getEnvCode().length()>0) {
                List<DSGCEnvInfoCfg> value = apiPlugInDao.queryDeplogDev(item.getEnvCode());
                if (value != null) {
                    for (DSGCEnvInfoCfg valueItem : value) {
                        item.setDevName(item.getDevName() + valueItem.getEnvName()+",");
                    }
                    String envName=item.getDevName();
                    item.setDevName(envName.trim().substring(0,envName.length()-1));
                }
            }
        }
        result.setResult(queryPluginList);
        return  result;
    }
}
