package com.definesys.dsgc.service.dagclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpstreamDeployService {
    @Autowired
    private UpstreamDeployDao upstreamDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;


    public String deployDAGUpstream(String vid,String envCode,String deployDesc,String uid) {
     return null;
    }
}
