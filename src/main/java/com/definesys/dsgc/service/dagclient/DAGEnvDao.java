package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DAGEnvDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DAGEnvBean getDAGEnvInfoByEnvCode(String envCode){
        return sw.buildQuery().eq("envCode",envCode).doQueryFirst(DAGEnvBean.class);
    }




}
