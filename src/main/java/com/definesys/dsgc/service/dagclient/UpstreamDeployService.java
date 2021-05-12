package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.bean.DAGUpstreamTargetBean;
import com.definesys.dsgc.service.dagclient.proxy.UpstreamProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UpstreamDeployService {
    @Autowired
    private UpstreamDeployDao upstreamDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;

    @Autowired
    DAGEnvDao dagEnvDao;

    public String deployDAGUpstream(String vid,String envCode,String deployDesc,String uid) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return "非法的环境信息！";
        }

        String upstreamName = this.upstreamDeployDao.getUpstreamName(vid);
        List<DAGUpstreamTargetBean> targets = this.upstreamDeployDao.getUpstreamTargets(vid);

        if(upstreamName == null){
            return "无效的配置信息，部署失败！";
        } else if(targets == null || targets.size() ==0){
            return "没有效的Target，部署失败！";
        } else{

            UpstreamProxy.UpstreamSetVO proxyReq = new UpstreamProxy.UpstreamSetVO();
            proxyReq.name = upstreamName;
            List<UpstreamProxy.TargetVO> tarLst = new ArrayList<UpstreamProxy.TargetVO>();

            Iterator<DAGUpstreamTargetBean> iter = targets.iterator();
            while(iter.hasNext()){
                DAGUpstreamTargetBean t = iter.next();
                UpstreamProxy.TargetVO tt = new UpstreamProxy.TargetVO();
                tt.weight = Integer.parseInt(t.getLrWeight());
                tt.target = t.getLrTarget();
                tarLst.add(tt);
            }

            proxyReq.targets = tarLst;

            UpstreamProxy up = new UpstreamProxy(env.getAdminLocation(),upstreamName);
            boolean dplRes = up.setUpstream(proxyReq);

            if (dplRes) {
                //部署成功，更新部署日志；
                this.dagDeployLogDao.logDAGDeploy(vid,envCode,deployDesc,uid);
                return "S";
            } else {
                return "部署失败，请联管理员处理！";
            }

        }

    }
}
