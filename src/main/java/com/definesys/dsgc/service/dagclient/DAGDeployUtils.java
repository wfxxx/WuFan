package com.definesys.dsgc.service.dagclient;

import java.util.ArrayList;
import java.util.List;

public class DAGDeployUtils {
    public static List<String> covertToListBySplit(String str){
        List<String> res = new ArrayList<String>();
        if(str != null ){
            String[]  arr = str.split(",");
            for(String a:arr){
                if(a != null && a.trim().length() >0){
                    res.add(a.trim());
                }
            }
        }

        if(res.size() > 0){
            return res;
        } else{
            return null;
        }
    }
}
