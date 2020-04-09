package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.dagclient.proxy.bean.*;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PluginsProxy {

    public static String PLUGIN_TARGET_ROUTE = "routes";
    public static String PLUGIN_TARGET_SERVICE = "services";

    protected String adminUrl;

    protected String pluginTarget;

    protected String targetId;

    protected PluginListVO pluginList;

    public PluginsProxy(String adminUrl,String pluginTarget,String targetId) {
        if (adminUrl != null && adminUrl.endsWith("/")) {
            this.adminUrl = adminUrl.substring(0,adminUrl.length() - 1);
        } else {
            this.adminUrl = adminUrl;
        }

        this.pluginTarget = pluginTarget;

        this.targetId = targetId;

        try {
            this.retrieve();
        } catch (HttpClientErrorException.NotFound e) {
            //网关不存在该对象
        }
    }


    private void retrieve() {
        this.pluginList = HttpReqUtil.getObject(this.adminUrl + "/" + pluginTarget + "/" + targetId + "/plugins",PluginListVO.class);
    }


    /**
     * 从dag中获取插件对象的id
     * @param pluginName
     * @param consumerId
     * @return
     */
    private String getPluginId(String pluginName,String consumerId) {
        PluginVO p = this.getPluginObj(pluginName,consumerId);
        return p != null ? p.id : null;
    }

    /**
     * 从dag中获取插件对象
     * @param pluginName
     * @param consumerId
     * @return
     */
    private PluginVO getPluginObj(String pluginName,String consumerId) {
        if (this.pluginList != null && this.pluginList.getData() != null) {
            Iterator<PluginVO> iter = this.pluginList.getData().iterator();
            while (iter.hasNext()) {
                PluginVO p = iter.next();
                if (p != null && p.name != null && p.name.equals(pluginName)) {
                    if (consumerId != null) {
                        if (p.consumer != null && consumerId.equals(p.consumer.getId())) {
                            return p;
                        }
                    } else {
                        if(p.consumer == null) {
                            return p;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 设置plugin列表
     *
     * @param plugins     插件列表
     * @param purgePlugin 是否清理网关中不在plugins的其它插件
     */
    public void setPlugins(Map<String,PluginSettingVO> plugins,boolean purgePlugin) {

        if (plugins != null) {
            Set<String> pluginKeySet = plugins.keySet();
            Iterator<String> keyIter = pluginKeySet.iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();

                String pluginName = key;
                String consumerId = null;
                int splitIdx = key.indexOf(":");
                if (splitIdx != -1) {
                    pluginName = key.substring(0,splitIdx);
                    consumerId = key.substring(splitIdx+1);
                }

                String id = this.getPluginId(pluginName,consumerId);
                if (id != null) {
                    this.updatePlugin(pluginName,consumerId,id,plugins.get(key));
                } else {
                    this.applyPlugin(pluginName,consumerId,plugins.get(key));
                }
            }

        }
        if (purgePlugin) {
            if (this.pluginList != null && this.pluginList.getData() != null) {
                Iterator<PluginVO> iter = this.pluginList.getData().iterator();
                while (iter.hasNext()) {
                    PluginVO p = iter.next();
                    if (p != null) {
                        String searcheKey = p.name;
                        if (p.consumer != null && p.consumer.getId() != null && p.consumer.getId().trim().length() > 0) {
                            searcheKey = p.name + ":" + p.consumer.getId();
                        }
                        if (plugins == null || !plugins.containsKey(searcheKey)) {
                            this.delete(p.id);
                        }
                    }
                }
            }
        }

    }

    /**
     * 更新插件是否启用状态
     * @param ps
     */
    public void updatePluginEnableStat(PluginSettingVO ps){
        if(ps !=null && ps.getPluginCode() != null){
            PluginVO p =  this.getPluginObj(ps.getPluginCode(),ps.getCsmCode());
            this.updatePluginEnableStat(p,ps.isEnabled());
        }
    }

    private void updatePluginEnableStat(PluginVO p,boolean isEnable){
        if(p != null && p.enabled != isEnable){
            if(p.id != null && p.id.length() >0){
                String jsonText = "{\"enabled\":"+isEnable+"}";
                HttpReqUtil.putJsonText(this.adminUrl + "/plugins/" + p.id,jsonText);
            }
        }
    }

    private void applyPlugin(String pluginName,String consumerId,PluginSettingVO cfg) {
        if (cfg != null) {
            PluginVO p = this.generatePluginVO(pluginName,null,consumerId,cfg);
            HttpReqUtil.postObject(this.adminUrl + "/plugins",p,String.class);
        }
    }

    private void updatePlugin(String pluginName,String consumerId,String pluginId,PluginSettingVO cfg) {
        if (pluginId != null && pluginId.trim().length() > 0 && cfg != null) {
            PluginVO p = this.generatePluginVO(pluginName,pluginId,consumerId,cfg);
            HttpReqUtil.putObject(this.adminUrl + "/plugins/" + pluginId,p,String.class);
        }
    }





    private PluginVO generatePluginVO(String pluginName,String pluginId,String consumerId,PluginSettingVO cfg) {
        PluginVO p = new PluginVO();
        p.config = cfg.getConfig();
        p.enabled = cfg.isEnabled();
        if (pluginId != null && pluginId.trim().length() > 0) {
            p.id = pluginId;
        }

        p.name = pluginName;

        IdVO targetId = new IdVO();
        targetId.setId(this.targetId);

        if (PLUGIN_TARGET_ROUTE.equals(this.pluginTarget)) {
            p.route = targetId;
        } else if (PLUGIN_TARGET_SERVICE.equals(this.pluginTarget)) {
            p.service = targetId;
        }

        if (consumerId != null && consumerId.trim().length() > 0) {
            p.consumer = new IdVO();
            p.consumer.setId(consumerId);
        }
        return p;
    }


    private void delete(String pluginId) {
        if (pluginId != null && pluginId.trim().length() > 0) {
            try {
                HttpReqUtil.delete(this.adminUrl + "/plugins/" + pluginId);
            } catch (HttpClientErrorException.NotFound e) {
                e.printStackTrace();
            }
        }
    }


}
