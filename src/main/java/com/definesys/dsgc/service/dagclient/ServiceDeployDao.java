package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.apibs.bean.DagCodeVersionBean;
import com.definesys.dsgc.service.dagclient.bean.DAGServiceInfoBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceDeployDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;


    public DAGServiceInfoBean getDagServiceInfoByVid(String vid){
       return sw.buildViewQuery("V_BS_DTL").setVar("vidVar",vid).doQueryFirst(DAGServiceInfoBean.class);
    }


}
