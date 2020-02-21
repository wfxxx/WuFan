package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.CommonReqBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
}
