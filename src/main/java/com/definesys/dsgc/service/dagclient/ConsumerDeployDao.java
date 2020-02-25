package com.definesys.dsgc.service.dagclient;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ConsumerDeployDao {
    @Autowired
    private MpaasQueryFactory sw;

    public String getEnvType(String envCode){
        return "dag";
    }
}
