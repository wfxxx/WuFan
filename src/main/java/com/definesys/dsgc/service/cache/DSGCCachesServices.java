package com.definesys.dsgc.service.cache;

import com.definesys.dsgc.service.cache.bean.DSGCLogCacherefresh;
import com.definesys.dsgc.service.cache.bean.DSGCLogSysHeartbeat;
import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.cache.DSGCCachesDao;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
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

    public List<DSGCLogSysHeartbeat> getCaches(){
        return this.dsgcCachesDao.getCaches();
    }

    public List<DSGCLogCacherefresh> getChildCaches(String serverName){
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
            String re = "";
            Map<String, String> map = new HashMap<>();
            switch (result) {
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
            map.put("serverName",in);
            map.put("serverFreshStatus",re);
            res.add(map);
        }
        return res;
    }

    public Map<String, String> getUrls(String serverName){
        List<FndLookupValue> values = null;
        FndLookupType fndLookupType = fndLookupTypeDao.getFndLookupTypeByType("REFRESH_CACHE_URL");
        if(fndLookupType == null){
            throw new MpaasBusinessException("缓存刷新值列表REFRESH_CACHE_URL未配置");
        }
        values = fndLookupType.getValues();
        Map<String, String> map = new HashMap();
        for (int i = 0; i <values.size() ; i++) {
            FndLookupValue v = values.get(i);
            map.put(v.getLookupCode(),v.getMeaning());
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
}
