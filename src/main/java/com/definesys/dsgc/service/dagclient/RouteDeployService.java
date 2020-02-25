package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.bean.DAGRouteHostsBean;
import com.definesys.dsgc.service.dagclient.bean.DAGRouteInfoBean;
import com.definesys.dsgc.service.dagclient.proxy.RouteProxy;
import com.definesys.dsgc.service.dagclient.proxy.ServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteDeployService {
    @Autowired
    private RouteDeployDao routeDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;

    @Autowired
    DAGEnvDao dagEnvDao;

    public String deployDAGRoute(String vid,String envCode,String deployDesc,String uid) {

        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return "非法的环境信息！";
        }

        DAGRouteInfoBean route = this.routeDeployDao.getRouteBasicInfo(vid);
        if (route == null) {
            return "路由规则信息不存在，部署失败！";
        } else if (route.getBsCode() == null || route.getBsCode().trim().length() == 0) {
            return "路由后端服务不能为空！";
        } else if (route.getRouteMethod() == null || route.getRouteMethod().trim().length() == 0) {
            return "路由匹配的HTTP方法不能为空！";
        } else if (route.getRoutePath() == null || route.getRoutePath().trim().length() == 0) {
            return "路由匹配路径不能为空！";
        } else {
            ServiceProxy sp = new ServiceProxy(env.getAdminLocation(),route.getBsCode());
            if (!sp.isFound()) {
                return "后端服务未部署，请先部署后端服务！";
            } else {
                String[] hosts = this.routeDeployDao.getRouteHostNames(vid);
                RouteProxy.RouteSetVO proxyReq = new RouteProxy.RouteSetVO();
                proxyReq.hosts = hosts;
                proxyReq.methods = new String[]{route.getRouteMethod()};
                proxyReq.name = route.getRouteCode();
                proxyReq.paths = new String[]{route.getRoutePath()};
                if ("Y".equals(route.getStripPath())) {
                    proxyReq.strip_path = false;
                }
                RouteProxy.ServiceRef bsRef = new RouteProxy.ServiceRef();
                bsRef.id = sp.getId();
                proxyReq.service = bsRef;

                RouteProxy rp = new RouteProxy(env.getAdminLocation(),route.getRouteCode());
                boolean dplRes = rp.setRoute(proxyReq);
                if (dplRes) {
                    //部署成功，更新部署日志；
                    this.dagDeployLogDao.logDAGDeploy(vid,envCode,deployDesc,uid);
                    return "S";
                } else {
                    return "部署失败，请联管理员处理！";
                }

            }
        }

    }
}
