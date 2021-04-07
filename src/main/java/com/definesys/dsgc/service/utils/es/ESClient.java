package com.definesys.dsgc.service.utils.es;


import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

import java.util.List;

public class ESClient {

    public static List<String> searchESPayload(String findKey) {
        if (findKey == null || findKey.trim().length() == 0) {
            return null;
        } else {
            PLFindRequestVO req = new PLFindRequestVO();
            req.setFindKey(findKey);
            String url = System.getenv("ES_API_URI");
            if( url == null){
                return null;
            }
            PLFindResponseVO res = HttpReqUtil.postObject(url,req,PLFindResponseVO.class);
            if (res != null) {
                return res.getData();
            } else {
                return null;
            }
        }
    }
}
