package com.definesys.dsgc.service.apicert;

import com.definesys.dsgc.service.apicert.bean.CommonReqBean;
import com.definesys.dsgc.service.apicert.bean.DagCertbean;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ApiCertService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-24 18:17
 * @Version 1.0
 **/
@Service
public class ApiCertService {
    @Autowired
    ApiCertDao apiCertDao;
    @Autowired
    private SVCLogDao sldao;


    public PageQueryResult queryApiCertList(CommonReqBean param, String userId, String userRole, int pageSize, int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult ApiCertList = apiCertDao.queryApiCertList(param,pageSize,pageIndex,userRole,sysCodeList);
        return ApiCertList;
    }


    public void addApiCert(DagCertbean dagCertbean){
         apiCertDao.addApiCert(dagCertbean);
    }

    public void updateApiCert(DagCertbean dagCertbean){
        apiCertDao.updateApiCert(dagCertbean);
    }

    public void delApiCert(DagCertbean dagCertbean){
        apiCertDao.delApiCert(dagCertbean);
    }

    public DagCertbean checkSameName(String certName){
        return apiCertDao.checkSameName(certName);
    }


}
