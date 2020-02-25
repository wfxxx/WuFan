package com.definesys.dsgc.service.dagclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteDeployService {
    @Autowired
    private RouteDeployDao routeDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;

    public String deployDAGRoute(String vid,String envCode,String deployDesc,String uid) {
        return null;
    }
}
