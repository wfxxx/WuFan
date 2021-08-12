package com.definesys.dsgc.service.ystar.mg.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

    public static String getValueByKey(String str, String key) {
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject.getString(key);
    }
}
