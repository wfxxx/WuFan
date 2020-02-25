package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGCodeVersionBean;
import com.definesys.dsgc.service.dagclient.bean.DAGDeployReqVO;
import com.definesys.dsgc.service.utils.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DAGClientService {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private DAGClientDao dagClientDao;

    @Autowired
    private ServiceDeployService serviceDeployService;

    @Autowired
    private UpstreamDeployService upstreamDeployService;

    @Autowired
    private RouteDeployService routeDeployService;

    public String deploy(DAGDeployReqVO req,String uid) {
        String noAppAuth = "无当前应用的部署权限，部署失败！";
        UserHelper uh = this.userHelper.user(uid);
        if (uh.isAdmin() || uh.isSuperAdministrator() || uh.isSystemMaintainer()) {

            DAGCodeVersionBean cv = dagClientDao.getCVDtl(req.getVid());

            if (cv != null) {

                if ("bs".equals(cv.getSourType())) {
                    //service对象
                    String appCode = this.dagClientDao.getBSAppCode(cv.getSourCode());
                    if (uh.isSystemMaintainer() && !uh.isSpecifySystemMaintainer(appCode)){
                        //判断是否是当前部署对象的应用负责人
                        return noAppAuth;
                    }

                    return this.serviceDeployService.deployDAGService(req.getVid(),req.getEnvCode(),req.getDeployDesc(),uid);

                } else if ("route".equals(cv.getSourType())) {
                    //路由对象
                    String appCode = this.dagClientDao.getRouteAppCode(cv.getSourCode());
                    if (uh.isSystemMaintainer() && !uh.isSpecifySystemMaintainer(appCode)){
                        //判断是否是当前部署对象的应用负责人
                        return noAppAuth;
                    }

                    return this.routeDeployService.deployDAGRoute(req.getVid(),req.getEnvCode(),req.getDeployDesc(),uid);

                } else if ("lr".equals(cv.getSourType())) {
                    //upstream对象
                    String appCode = this.dagClientDao.getUpStreamAppCode(cv.getSourCode());
                    if (uh.isSystemMaintainer() && !uh.isSpecifySystemMaintainer(appCode)){
                        //判断是否是当前部署对象的应用负责人
                        return noAppAuth;
                    }
                    return this.upstreamDeployService.deployDAGUpstream(req.getVid(),req.getEnvCode(),req.getDeployDesc(),uid);
                } else{
                    return "未知的部署对象，无法完成部署！";
                }


            } else {
                return "数据不存在，部署失败！";
            }


        } else {
            return "当前用户无部署权限，部署失败！";
        }

    }
}
