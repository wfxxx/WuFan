package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGCodeVersionBean;
import com.definesys.dsgc.service.dagclient.bean.DAGDeployLogBean;
import com.definesys.dsgc.service.dagclient.bean.DAGDeployStatBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

@Repository
public class DAGDeployLogDao {
    @Autowired
    private MpaasQueryFactory sw;


    public void logDAGDeploy(String vid,String envCode,String deployDesc,String uid) {
        DAGDeployLogBean logBean = new DAGDeployLogBean();
        logBean.setEnvCode(envCode);
        logBean.setLogCnt(deployDesc);
        logBean.setVid(vid);

        sw.buildQuery().doInsert(logBean);

        DAGCodeVersionBean vc = sw.buildQuery().eq("vid",vid).doQueryFirst(DAGCodeVersionBean.class);

        Map<String,Object> isExistRes = sw.buildQuery().sql("select s.dds_id from dag_deploy_stat s,dag_code_version v where s.vid = v.vid and s.env_code = #envCode and v.sour_code = #sourCode and v.sour_type = #sourType")
                .setVar("envCode",envCode)
                .setVar("sourCode",vc.getSourCode())
                .setVar("sourType",vc.getSourType())
                .doQueryFirst();

        Date curDate = new Date(System.currentTimeMillis());

        if (isExistRes != null && isExistRes.size() > 0) {
            String ddsId = isExistRes.get("DDS_ID").toString();
            sw.buildQuery().update("vid",vid).update("deploy_time",curDate).eq("ddsId",ddsId).doUpdate(DAGDeployStatBean.class);
        } else {
            DAGDeployStatBean statBean = new DAGDeployStatBean();
            statBean.setDeployTime(curDate);
            statBean.setEnvCode(envCode);
            statBean.setVid(vid);
            sw.buildQuery().doInsert(statBean);
        }


    }


}
