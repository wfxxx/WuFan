package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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


    /**
     * 为consumer添加groups
     *
     * @param groups
     * @return
     */
    public boolean setGroups(List<String> groups) {
        if (groups != null && groups.size() > 0) {
            Set<String> dagGroupsSet = this.getDagGroupSet();
            Iterator<String> iters = groups.iterator();
            while (iters.hasNext()) {
                String newGroup = iters.next();
                if (newGroup != null && newGroup.trim().length() > 0 && !dagGroupsSet.contains(newGroup)) {
                    this.addGroup(newGroup);
                    dagGroupsSet.add(newGroup);
                }
            }
        }
        return true;
    }

    private Set<String> getDagGroupSet() {
        ConsumerGroupsEntity dagGroups = this.getConsumerGroups();
        Set<String> dagGroupSet = new HashSet<String>();
        if (!(dagGroups == null
                || dagGroups.data == null
                || dagGroups.data.size() == 0)) {
            Iterator<GroupEntity> iters = dagGroups.data.iterator();
            while (iters.hasNext()) {
                GroupEntity ge = iters.next();
                dagGroupSet.add(ge.group);
            }
        }
        return dagGroupSet;
    }

    /**
     * 移除消费者groups
     *
     * @param groups
     * @return
     */
    public boolean removeGroups(Set<String> groups) {
        ConsumerGroupsEntity dagGroups = this.getConsumerGroups();
        if (!(dagGroups == null
                || dagGroups.data == null
                || dagGroups.data.size() == 0)) {
            Iterator<GroupEntity> iters = dagGroups.data.iterator();
            while (iters.hasNext()) {
                GroupEntity ge = iters.next();
                if (groups.contains(ge.group)) {
                    this.delGroup(ge.id);
                }
            }
        }
        return true;
    }

    public ConsumerGroupsEntity getConsumerGroups() {
        return HttpReqUtil.getObject(this.adminUrl + "/consumers/" + this.getId() + "/acls",ConsumerGroupsEntity.class);
    }


    public boolean removeGroup(String group){
        if(group != null){
            Set<String> groups = new HashSet<String>();
            groups.add(group);
            return this.removeGroups(groups);
        }
        return true;
    }

    private void delGroup(String id) {
        try {
            HttpReqUtil.delete(this.adminUrl + "/consumers/" + this.getId() + "/acls/" + id);
        } catch (HttpClientErrorException.NotFound e) {
            e.printStackTrace();
        }
    }

    public void addGroup(String group) {
        try {
            String req = "{\"group\":\"" + group + "\"}";
            HttpReqUtil.postJsonText(this.adminUrl + "/consumers/" + this.getId() + "/acls",req,String.class);
        } catch (HttpClientErrorException.Conflict e) {
            e.printStackTrace();
        }
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


    public static class ConsumerGroupsEntity {
        public String next;
        public List<GroupEntity> data;
    }

    public static class GroupEntity {
        public long created_at;
        public GroupConsumerRef consumer;
        public String id;
        public String group;
        public String[] args;
    }

    public static class GroupConsumerRef {
        public String id;
    }


}
