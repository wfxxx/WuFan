package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.proxy.ConsumerProxy;
import com.definesys.dsgc.service.dagclient.proxy.PluginsProxy;
import com.definesys.dsgc.service.dagclient.proxy.bean.PluginSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class PluginDeployService {

    @Autowired
    DAGEnvDao dagEnvDao;

    @Autowired
    PluginDeployDao pluginDeployDao;


    public String validPluginConfig(String envCode,String vid) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return "非法的环境信息！";
        }
        List<PluginSettingVO> psetingList = this.pluginDeployDao.loadPluginConfig(vid);

        //从网关获取consumer的id
        Map<String,String> consumerKeyMap = this.getConsumerKey(env,psetingList);

        //检查插件配置使用的消费者对象是否存在
        return this.checkConsumerExistedInDAG(consumerKeyMap);

    }

    public String deployPlugins(String envCode,String vid,String targetType,String targetId) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return "非法的环境信息！";
        }

        List<PluginSettingVO> psetingList = this.pluginDeployDao.loadPluginConfig(vid);

        if (psetingList != null && psetingList.size() > 0) {
            //从网关获取consumer的id
            Map<String,String> consumerKeyMap = this.getConsumerKey(env,psetingList);

            String checkRes = this.checkConsumerExistedInDAG(consumerKeyMap);

            if (!"Y".equals(checkRes)) {
                return "插件关联的消费者在部署目标环境不存在，无法完成部署，请先部署消费者：" + checkRes;
            }

            Map<String,PluginSettingVO> pluginSettingMap = new HashMap<String,PluginSettingVO>();

            Iterator<PluginSettingVO> iters = psetingList.iterator();
            while (iters.hasNext()) {
                PluginSettingVO ps = iters.next();
                String key = ps.getPluginCode();
                if (ps.getCsmCode() != null && ps.getCsmCode().trim().length() != 0) {
                    key = ps.getPluginCode() + ":" + ps.getCsmCode();
                }
                if (!pluginSettingMap.containsKey(key)) {
                    pluginSettingMap.put(key,ps);
                }
            }

            PluginsProxy pp = new PluginsProxy(env.getAdminLocation(),targetType,targetId);
            pp.setPlugins(pluginSettingMap,true);
        }
        return "Y";
    }

    private Map<String,String> getConsumerKey(DAGEnvBean env,List<PluginSettingVO> plugins) {
        Map<String,String> consumerExistMap = new HashMap<String,String>();
        if (plugins != null) {
            Iterator<PluginSettingVO> piter = plugins.iterator();
            while (piter.hasNext()) {
                PluginSettingVO ps = piter.next();
                String csmCode = ps.getCsmCode();
                if (csmCode != null) {
                    if (!consumerExistMap.containsKey(csmCode)) {
                        ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),csmCode);
                        if (cp.isFound()) {
                            consumerExistMap.put(csmCode,cp.getId());
                        } else {
                            consumerExistMap.put(csmCode,null);
                        }
                    }
                }
            }
        }
        return consumerExistMap;
    }

    private String checkConsumerExistedInDAG(Map<String,String> consumerMaps) {
        String res = "Y";
        if (consumerMaps == null || consumerMaps.isEmpty()) {
            return res;
        } else {
            Iterator<String> keyIters = consumerMaps.keySet().iterator();
            while (keyIters.hasNext()) {
                String csmCode = keyIters.next();
                String value = consumerMaps.get(csmCode);
                if (value == null || value.trim().length() == 0) {
                    if ("Y".equals(res)) {
                        res = this.pluginDeployDao.getConsumerName(csmCode);
                    } else {
                        res += " " + this.pluginDeployDao.getConsumerName(csmCode);
                    }
                }
            }
        }
        return res;
    }
}
