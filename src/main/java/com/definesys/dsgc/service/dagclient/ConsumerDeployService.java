package com.definesys.dsgc.service.dagclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerDeployService {

    @Autowired
    private ConsumerDeployDao consumerDeployDao;



    public boolean deployConsumer(String consumerCode,String envCode){
        String envType = this.consumerDeployDao.getEnvType(envCode);

        if("dag".equals(envType)){

        }



        return true;

    }

    public boolean updateBasicAuth(String consumerCode,String newPd,String envCode){
        return true;
    }



}
