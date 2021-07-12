package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.bean.DAGServiceInfoBean;
import com.definesys.dsgc.service.dagclient.proxy.PluginsProxy;
import com.definesys.dsgc.service.dagclient.proxy.ServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceDeployService {
    @Autowired
    private ServiceDeployDao serviceDeployDao;

    @Autowired
    private DAGDeployLogDao dagDeployLogDao;

    @Autowired
    private DAGEnvDao dagEnvDao;

    @Autowired
    private PluginDeployService pluginDeployService;

    public String deployDAGService(String vid,String envCode,String deployDesc,String uid) {

        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env  == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return "非法的环境信息！";
        }

        DAGServiceInfoBean bsInfo = this.serviceDeployDao.getDagServiceInfoByVid(vid);
        if(bsInfo==null){
            return "未找到信息技术配置信息";
        }

        if (bsInfo.getHostName() == null || bsInfo.getHostName().trim().length() == 0) {
            return "后端地址不正确！";
        }

        //检查插件配置信息是否正确
        String pluginCheckRes = this.pluginDeployService.validPluginConfig(envCode,vid);
        if(!"Y".equals(pluginCheckRes)){
            return pluginCheckRes;
        }

        ServiceProxy sp = new ServiceProxy(env.getAdminLocation(),bsInfo.getBsCode());

        ServiceProxy.ServiceSetVO req = new ServiceProxy.ServiceSetVO();
        req.host = bsInfo.getHostName();
        req.name = bsInfo.getBsCode();
        if (bsInfo.getConnectTimeout()!=null && bsInfo.getConnectTimeout() != 0l) {
            req.connect_timeout = bsInfo.getConnectTimeout();
        }

        req.path = bsInfo.getPaths();
        req.port = Integer.parseInt(bsInfo.getPort());

        req.protocol = bsInfo.getProtocal();
        if (bsInfo.getReadTimeout() != null && bsInfo.getReadTimeout() != 0l) {
            req.read_timeout = bsInfo.getReadTimeout();
        }
        if(bsInfo.getRtyCount() != null) {
            req.retries = bsInfo.getRtyCount().intValue();
        }

        if (bsInfo.getSendTimeout() !=null && bsInfo.getSendTimeout().longValue() != 0l) {
            req.write_timeout = bsInfo.getSendTimeout().longValue();
        }


        boolean res = sp.setService(req);

        if (res) {
            //部署路由使用的插件
            this.pluginDeployService.deployPlugins(envCode,vid,PluginsProxy.PLUGIN_TARGET_SERVICE,sp.getId());

            //部署成功，更新部署日志；
            this.dagDeployLogDao.logDAGDeploy(vid,envCode,deployDesc,uid);
        } else {
            return "部署失败，请联管理员处理！";
        }

        return "S";

    }


}

