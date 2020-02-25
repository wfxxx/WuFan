package com.definesys.dsgc.service.dagclient;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ConsumerDeployDao {
    @Autowired
    private MpaasQueryFactory sw;


    public String getConsumerBasicAuth(String consumerCode,String envCode) {
        Map<String,Object> res = sw.buildQuery().sql("select ca_attr1 BA_PD dsgc_consumer_auth where csm_code = #consumerCode and env_code = #envCode")
                .setVar("consumerCode",consumerCode)
                .setVar("envCode",envCode)
                .doQueryFirst();
        if (res != null) {
            Object pdObj = res.get("BA_PD");
            if (pdObj != null) {
                return pdObj.toString();
            }
        }
        return null;
    }

}
