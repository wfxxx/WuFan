package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.common.cache.helper.FndPropertiesHelper;
import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.lkv.FndPropertiesDao;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DAGEnvDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DAGEnvBean getDAGEnvInfoByEnvCode(String envCode){
        return sw.buildQuery().eq("envCode",envCode).doQueryFirst(DAGEnvBean.class);
    }

    public DAGEnvBean getESBCurrentEnvInfoByEnvCode(){
        return sw.buildQuery().sql("select * from DSGC_ENV_INFO_CFG where ENV_CODE = (select PROPERTY_VALUE from FND_PROPERTIES where PROPERTY_KEY = 'DSGC_CURRENT_ENV')").doQueryFirst(DAGEnvBean.class);
    }

    public List<DAGEnvBean> getConsumerDeployedDAGEnvInfo(String consumerCode){
        return sw.buildQuery().sql("select e.* from dsgc_consumer_entities c,dsgc_env_info_cfg e\n" +
                "where instr(','||c.deploy_env,','||e.env_code) <> 0 and c.csm_code = #csmCode").setVar("csmCode",consumerCode)
                .doQuery(DAGEnvBean.class);
    }




}
