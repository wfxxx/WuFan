package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

public class RouteProxy extends DAGProxy{

    private String baseUrl;

    public RouteProxy(String adminUrl,String refId){
        super(adminUrl,refId);
    }

    public boolean setRoute(RouteSetVO req) {
        if(!this.isFound()){
            //网关中不存在，执行新增；
            this.entityProxy = HttpReqUtil.postObject(this.baseUrl,req,RouteEntity.class);
        } else{
            //网关中存在，执行更新
            HttpReqUtil.putObject(this.baseUrl+"/"+ this.getId(),req,RouteEntity.class);
        }
        return true;
    }


    @Override
    protected void retrieve(String refId) {
        this.baseUrl = this.adminUrl + "/routes";
        this.entityProxy = HttpReqUtil.getObject(this.baseUrl+"/"+refId,RouteEntity.class);
    }

    @Override
    public void delete() {
        HttpReqUtil.delete(this.baseUrl+"/"+this.getId());
    }

    public static class RouteEntity{
        public String id;
        public ServiceRef service;
        public String[] paths;
        public String[] protocols;
        public String[] methods;
        public String name;
        public boolean strip_path;
        public boolean preserve_host;
        public int regex_priority;
        public long updated_at;
        public String[] hosts;
        public int https_redirect_status_code;
        public String[] tags;
        public long created_at;
    }

    public static class RouteSetVO{
        public ServiceRef service;
        public String[] paths;
        public String[] protocols = new String[]{"http","https"};
        public String[] methods;
        public String name;
        public boolean strip_path = true;
        public boolean preserve_host = false;
        public int regex_priority = 0;
        public String[] hosts;
        public int https_redirect_status_code = 426;
        public String[] tags;
    }

    public static class ServiceRef{
        public String id;
    }


}
