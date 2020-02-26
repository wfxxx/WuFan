package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;

import java.util.List;

public class ConsumerProxy extends DAGProxy {


    public ConsumerProxy(String adminUrl,String consumerId) {
        super(adminUrl,consumerId);
    }


    /**
     * 添加消费者
     *
     * @param consumerId
     * @param tags
     * @return
     * @throws Exception
     */
    public boolean add(String consumerId,String[] tags) {
        ConsumerAddVO addReq = new ConsumerAddVO();
        addReq.username = consumerId;
        addReq.custom_id = consumerId;
        addReq.tags = tags;
        this.entityProxy = HttpReqUtil.postObject(this.adminUrl + "/consumers",addReq,ConsumerEntity.class);
        return true;
    }


    /**
     * 添加消费者basic auth
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public boolean setBasicAuth(String pd) {
        ConsumerEntity ce = (ConsumerEntity)this.entityProxy;

        String reqJson = "{\"username\":\"" + ce.custom_id + "\",\"password\":\"" + pd + "\"}";

        ConsumerBasicAuthList basicAuth = HttpReqUtil.getObject(this.adminUrl + "/consumers/" + this.getId() + "/basic-auth",ConsumerBasicAuthList.class);

        if (basicAuth.data.size() == 0) {
            //没有证书，直接新建
            HttpReqUtil.postJsonText(this.adminUrl + "/consumers/" + this.getId() + "/basic-auth",reqJson,String.class);

        } else {
            //已经存在，直接更新
            HttpReqUtil.putJsonText(this.adminUrl + "/consumers/" + this.getId() + "/basic-auth/" + basicAuth.data.get(0).id,reqJson);
        }
        return true;
    }


    @Override
    protected void retrieve(String uid) {
        this.entityProxy = HttpReqUtil.getObject(this.adminUrl + "/consumers" + "/" + uid,ConsumerEntity.class);
    }

    @Override
    public void delete() {
        HttpReqUtil.delete(this.adminUrl + "/consumers" + "/" + this.getId());
    }


    protected static class ConsumerEntity {

        public ConsumerEntity() {

        }

        public String id;
        public long created_at;
        public String username;
        public String custom_id;
        public String[] tags;

    }

    protected static class ConsumerBasicAuthList {
        public String next;
        public List<ConsumerBasicAuth> data;

    }

    protected static class ConsumerBasicAuth {

        public ConsumerBasicAuth() {

        }

        public String id;
        public long created_at;
        public String username;
        public String password;

    }


    protected class ConsumerAddVO {

        public ConsumerAddVO() {

        }

        public String username;
        public String custom_id;
        public String[] tags;
    }


}
