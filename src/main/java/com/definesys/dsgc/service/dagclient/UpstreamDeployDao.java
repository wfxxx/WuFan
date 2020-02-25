package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGCodeVersionBean;
import com.definesys.dsgc.service.dagclient.bean.DAGUpstreamTargetBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UpstreamDeployDao {
    @Autowired
    private MpaasQueryFactory sw;


    public String getUpstreamName(String vid) {
        DAGCodeVersionBean cv = sw.buildQuery().eq("vid",vid).doQueryFirst(DAGCodeVersionBean.class);
        if (cv != null) {
            return cv.sourCode;
        } else {
            return null;
        }
    }

    public List<DAGUpstreamTargetBean> getUpstreamTargets(String vid) {
        return sw.buildQuery().eq("vid",vid).doQuery(DAGUpstreamTargetBean.class);
    }
}
