package com.definesys.dsgc.service.ystar.mg.svc;

import com.definesys.dsgc.service.ystar.mg.bean.UserInfoBean;
import com.definesys.dsgc.service.ystar.mg.otr.OtrDao;
import com.definesys.dsgc.service.ystar.mg.svc.bean.SvcInfoView;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SvcInfoService {
    @Autowired
    private SvcInfoDao svcInfoDao;
    @Autowired
    private OtrDao otrDao;

    public Response listQuerySvcInfo(SvcInfoView svcInfoView, String userId) {

        List<Map<String, String>> sysInfoList = new ArrayList<>();
        List<Map<String, String>> svcInfoList = new ArrayList<>();
        try {
            //根据uid查询用户角色
            UserInfoBean user = this.otrDao.sigQueryUserById(userId);
            if (user == null) {
                return Response.error("无效的Header参数[uid]");
            }
            String userRole = user.getUserRole();
            if (StringUtil.isNotBlank(userRole)) {
                List<SvcInfoView> listSysInfo;
                List<SvcInfoView> listSvcInfo = null;
                if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)) {
                    listSysInfo = svcInfoDao.listAllQuerySysCode();
                    listSvcInfo = svcInfoDao.listQuerySvcInfo(svcInfoView, null, null);
                } else {
                    //根据userId查询所有系统列表
                    listSysInfo = svcInfoDao.listQuerySysCode(userId);
                }
                List<String> sysCodeList = new ArrayList<>();
                for (SvcInfoView sys : listSysInfo) {
                    String sysCode = sys.getSysCode();
                    String sysName = sys.getSysName();
                    sysCodeList.add(sysCode);
                    Map<String, String> sysMap = new HashMap<>();
                    sysMap.put("sysCode", sysCode);
                    sysMap.put("sysName", sysName);
                    sysInfoList.add(sysMap);
                }
                if (!("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole))) {
                    if (sysCodeList.size() <= 0) {
                        return Response.ok().data(svcInfoView);
                    }
                    listSvcInfo = svcInfoDao.listQuerySvcInfo(svcInfoView, null, sysCodeList);
                }

                for (SvcInfoView svc : listSvcInfo) {
                    String svcCode = svc.getSvcNo();
                    String svcName = svc.getSvcName();
                    String sysCode = svc.getSysCode();
                    String sysName = svc.getSysName();
                    Map<String, String> svcMap = new HashMap<>();
                    svcMap.put("svcCode", svcCode);
                    svcMap.put("svcName", svcName);
                    svcMap.put("sysCode", sysCode);
                    svcMap.put("sysName", sysName);
                    svcInfoList.add(svcMap);
                }
                svcInfoView.setSysInfoList(sysInfoList);
                svcInfoView.setSvcInfoList(svcInfoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询失败！").data(e.getMessage());
        }
        return Response.ok().data(svcInfoView);
    }


}
