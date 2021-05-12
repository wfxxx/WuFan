package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

import java.util.List;

public class ServiceProxy extends DAGProxy{

    private String baseUrl;

    public ServiceProxy(String adminUrl,String refId){
        super(adminUrl,refId);

    }

    /**
     * create or update services
     * @return
     */
    public boolean setService(ServiceSetVO req){
        if(!this.isFound()){
            //网关中不存在，执行新增；
            this.entityProxy = HttpReqUtil.postObject(this.baseUrl,req,ServiceEntity.class);
        } else{
            //网关中存在，执行更新
            HttpReqUtil.putObject(this.baseUrl+"/"+ this.getId(),req,ServiceEntity.class);
        }
        return true;
    }

    @Override
    protected void retrieve(String refId) {
        this.baseUrl = adminUrl + "/services";
        this.entityProxy = HttpReqUtil.getObject(this.baseUrl+"/"+refId,ServiceEntity.class);

    }

    @Override
    public void delete() {
        HttpReqUtil.delete(this.baseUrl+"/"+this.getId());
    }



    public static class ServiceEntity{
        public String host;
        public long created_at;
        public long connect_timeout;
        public String id;
        public String protocol;
        public String name;
        public long  read_timeout;
        public int port;
        public String path;
        public long updated_at;
        public int retries;
        public long write_timeout;
        public String[] tags;
    }


    public static class ServiceSetVO{
        public String host;
        public long connect_timeout = 60000l;
        public String protocol;
        public String name;
        public long  read_timeout = 60000l;
        public int port;
        public String path;
        public int retries = 5;
        public long write_timeout = 60000l;
        public String[] tags;
    }

}
