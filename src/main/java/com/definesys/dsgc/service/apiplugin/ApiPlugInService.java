package com.definesys.dsgc.service.apiplugin;

import com.definesys.dsgc.service.apilr.ApiLrDao;
import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.apilr.bean.DagLrListDTO;
import com.definesys.dsgc.service.apilr.bean.DagLrbean;
import com.definesys.dsgc.service.apiplugin.bean.DAGPluginListVO;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
            Map<String,Object> value=apiPlugInDao.queryDeplogDev(item.getVid());
            if(value !=null){
                Object devName=value.get("DEV");
                item.setDevName(devName.toString());
            }else{
                item.setDevName("未部署");
            }
        }
        result.setResult(queryPluginList);
        return  result;
    }
}
