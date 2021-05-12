package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGCodeVersionBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DAGClientDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DAGCodeVersionBean getCVDtl(String vid){

        return sw.buildQuery().eq("vid",vid).doQueryFirst(DAGCodeVersionBean.class);

    }

    public String  getRouteAppCode(String routeCode){
        Map<String,Object> res = sw.buildQuery().sql("select app_code from dag_routes where route_code = #routeCode").setVar("routeCode",routeCode).doQueryFirst();

        if(res != null){
            Object appCode = res.get("APP_CODE");
            if(appCode != null){
                return appCode.toString();
            } else {
                return null;
            }
        }

        return null;
    }

    public String getBSAppCode(String bsCode){
        Map<String,Object> res = sw.buildQuery().sql("select app_code from dag_bs where bs_code = #bsCode").setVar("bsCode",bsCode).doQueryFirst();

        if(res != null){
            Object appCode = res.get("APP_CODE");
            if(appCode != null){
                return appCode.toString();
            } else {
                return null;
            }
        }

        return null;
    }

    public String getUpStreamAppCode(String upstreamCode){
        Map<String,Object> res = sw.buildQuery().sql("select app_code from dag_lr where lr_name = #upstreamCode").setVar("upstreamCode",upstreamCode).doQueryFirst();

        if(res != null){
            Object appCode = res.get("APP_CODE");
            if(appCode != null){
                return appCode.toString();
            } else {
                return null;
            }
        }

        return null;
    }





}
