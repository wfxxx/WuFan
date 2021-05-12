package com.definesys.dsgc.service.cache;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.cache.bean.DSGCLogCacherefresh;
import com.definesys.dsgc.service.cache.bean.DSGCLogSysHeartbeat;
import com.definesys.dsgc.service.envinfo.EnvInfoDao;
import com.definesys.dsgc.service.envinfo.bean.DSGCEnvMachineCfg;
import com.definesys.dsgc.service.envinfo.bean.DSGCEnvServerCfg;
import com.definesys.dsgc.service.envinfo.bean.EnvOptionDTO;
import com.definesys.dsgc.service.envinfo.bean.SvcgenDeployControl;
import com.definesys.dsgc.service.lkv.FndPropertiesDao;
import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.cache.DSGCCachesDao;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dsgcCachesServices")
public class DSGCCachesServices {

    @Autowired
    DSGCCachesDao dsgcCachesDao;

    @Autowired
    FndLookupTypeDao fndLookupTypeDao;

    @Autowired
    EnvInfoDao envInfoDao;
    @Autowired
    FndPropertiesDao fndPropertiesDao;


    public List<DSGCLogSysHeartbeat> getCaches() {
        return this.dsgcCachesDao.getCaches();
    }

    public List<DSGCLogCacherefresh> getChildCaches(String serverName) {
        return this.dsgcCachesDao.getChildCaches(serverName);
    }

    public List<Map> refresh(String serverName, String propertyDescription) {
        List<Map> res = new ArrayList<>();
        Map<String, String> urls = getUrls(serverName);
        for (String in : urls.keySet()) {
            String url = urls.get(in);//得到每个key多对用value的值
            if ("ALL".equals(serverName)) {
                url = url + "?code=ALL";
            } else {
                url = url + "?code=" + propertyDescription;
            }
            String result = exeRestService(url);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.getString("rtnMsg");
            String re = "";
            Map<String, String> map = new HashMap<>();
            switch (code) {
                case "1":
                    re = "刷新成功";
                    break;
                case "0":
                    re = "找不到配置，配置失败";
                    break;
                case "-1":
                    re = "参数为空";
                    break;
                case "-2":
                    re = "服务器异常";
                    break;
                case "-3":
                    re = "服务器链接不上";

            }
            map.put("serverName", in);
            map.put("serverFreshStatus", re);
            res.add(map);
        }
        return res;
    }

    public Map<String, String> getUrls(String serverName) {
        List<FndLookupValue> values = null;
        FndLookupType fndLookupType = fndLookupTypeDao.getFndLookupTypeByType("REFRESH_CACHE_URL");
        if (fndLookupType == null) {
            throw new MpaasBusinessException("缓存刷新值列表REFRESH_CACHE_URL未配置");
        }
        values = fndLookupType.getValues();
        Map<String, String> map = new HashMap();
        for (int i = 0; i < values.size(); i++) {
            FndLookupValue v = values.get(i);
            map.put(v.getLookupCode(), v.getMeaning());
        }
//        map.put("serverName", "http://172.18.189.59:8011/refreshCache/RefreshCache?code=ALL");
        if ("ALL".equals(serverName)) {
            return map;
        } else {
            String url = map.get(serverName);
            Map<String, String> res = new HashMap();
            res.put(serverName, url);
            return res;
        }
    }

    public String exeRestService(String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print("");
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-3";
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public List<Map> refreshMuleCache(String serverName, String refreshItem) {
        List<Map> res = new ArrayList<>();
        //获取URL地址
        Map<String, String> urls = getRefreshCacheUrls(serverName);
        //判断传入的刷新编码是不是值列表所配置内容
        if (!checkRefreshCfg(refreshItem)) {
            Map<String, String> map = new HashMap<>();
            map.put("serverName", serverName);
            map.put("refreshItem", refreshItem);
            map.put("serverFreshStatus", "刷新失败，值列表[REFRESH_CACHE_TYPE]找不到相关配置!");
            res.add(map);
            return res;
        }
        //遍历url
        for (String sevName : urls.keySet()) {
            String url = urls.get(sevName) + "?param1=" + refreshItem + "&param2=ESB";//得到每个key多对用value的值
            String result = exeRestService(url);
            System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.getString("rtnMsg");
            String re = "";
            Map<String, String> map = new HashMap<>();
            switch (code) {
                case "1":
                    re = "刷新成功";
                    break;
                case "0":
                    re = "找不到配置，配置失败";
                    break;
                case "-1":
                    re = "参数为空";
                    break;
                case "-2":
                    re = "服务器异常";
                    break;
                case "-3":
                    re = "服务器链接不上";

            }
            map.put("serverName", sevName);
            map.put("refreshItem", refreshItem);
            map.put("serverFreshStatus", re);
            res.add(map);
        }
        return res;
    }

    /**
     * 获取缓存刷新Url
     *
     * @param serverName
     * @return Map
     */
    public Map<String, String> getRefreshCacheUrls(String serverName) {
        Map<String, String> map = new HashMap();
        String envCode = fndPropertiesDao.findFndPropertiesByKey("DSGC_CURRENT_ENV").getPropertyValue();
        if (StringUtil.isNotBlank(serverName) && !"ALL".equals(serverName)) {
            DSGCEnvServerCfg serverCfg = envInfoDao.getCfgByServerName(envCode, serverName);
            if ("Y".equals(serverCfg.getCacheDpl())) {
                String port = serverCfg.getServerPort();
                DSGCEnvMachineCfg machineCfg = envInfoDao.getCfgByMachineName(envCode, serverCfg.getMachineName());
                String ip = machineCfg.getMachineIp();
                String url = "http://" + ip + ":" + port + "/muleService/refreshCache";
                map.put(serverName, url);
            }
        } else {
            List<DSGCEnvServerCfg> envServerCfgs = envInfoDao.getEnvServCfgByEnvCode(envCode);
            //判断是否部署缓存
            for (DSGCEnvServerCfg serverCfg : envServerCfgs) {
                if ("Y".equals(serverCfg.getCacheDpl())) {
                    serverName = serverCfg.getServerName();
                    String port = serverCfg.getServerPort();
                    DSGCEnvMachineCfg machineCfg = envInfoDao.getCfgByMachineName(envCode, serverCfg.getMachineName());
                    String ip = machineCfg.getMachineIp();
                    String url = "http://" + ip + ":" + port + "/muleService/refreshCache";
                    map.put(serverName, url);
                }
            }
        }
        return map;
    }

    /**
     * 校验该缓存刷新项是否存在
     *
     * @param refreshType
     * @return
     */
    public boolean checkRefreshCfg(String refreshType) {
        if ("ALL".equals(refreshType)) {
            return true;
        } else if (StringUtil.isBlank(refreshType)) {
            return false;
        }
        return fndLookupTypeDao.checkLookupValue("REFRESH_CACHE_TYPE", refreshType);
    }
}
