package com.definesys.dsgc.service.dagclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerDeployService {

    @Autowired
    private ConsumerDeployDao consumerDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;

}
