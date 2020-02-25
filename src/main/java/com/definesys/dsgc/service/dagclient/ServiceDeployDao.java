package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGServiceInfoBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceDeployDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;


    public DAGServiceInfoBean getDagServiceInfoByVid(String vid){
        return sw.buildViewQuery("V_BS_DTL").setVar("vid",vid).doQueryFirst(DAGServiceInfoBean.class);
    }


}
