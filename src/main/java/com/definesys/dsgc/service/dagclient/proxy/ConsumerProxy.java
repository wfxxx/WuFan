package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.dagclient.proxy.bean.IdVO;
import com.definesys.dsgc.service.dagclient.proxy.bean.JWTAuthBean;
import com.definesys.dsgc.service.dagclient.proxy.bean.JWTAuthListVO;
import com.definesys.dsgc.service.dagclient.proxy.bean.JWTAuthVO;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

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
     * 更新本地的Jwt auth设置到dag
     * @param jwtAuthMap
     * @param purgeFlag true 则清理dag中多余的jwt auth
     */
    public void setAllTokenAuth(Map<String,JWTAuthBean> jwtAuthMap,boolean purgeFlag) {
        JWTAuthListVO jaList = HttpReqUtil.getObject(this.adminUrl + "/consumers/" + this.getId() + "/jwt",JWTAuthListVO.class);
        if (jaList != null && jaList.getData() != null && jaList.getData().length > 0) {
            if (jwtAuthMap != null && !jwtAuthMap.isEmpty()) {
                Set<Map.Entry<String,JWTAuthBean>> eset = jwtAuthMap.entrySet();
                Iterator<Map.Entry<String,JWTAuthBean>> iter = eset.iterator();
                while (iter.hasNext()) {
                    Map.Entry<String,JWTAuthBean> jbEntry = iter.next();
                    if (jbEntry != null) {
                        JWTAuthBean jb = jbEntry.getValue();
                        this.addTokenAuth(jaList,jb);
                    }
                }

                if (purgeFlag) {
                    for (JWTAuthVO ja : jaList.getData()) {
                        if (ja != null && !jwtAuthMap.containsKey(ja.getKey())) {
                            this.deleteTokenAuthById(ja.getId());
                        }
                    }
                }

            } else {
                if (purgeFlag) {
                    for (JWTAuthVO ja : jaList.getData()) {
                        this.deleteTokenAuthById(ja.getId());
                    }
                }
            }

        } else {
            if (jwtAuthMap != null) {
                Set<Map.Entry<String,JWTAuthBean>> eset = jwtAuthMap.entrySet();
                Iterator<Map.Entry<String,JWTAuthBean>> iter = eset.iterator();
                while (iter.hasNext()) {
                    Map.Entry<String,JWTAuthBean> jbEntry = iter.next();
                    if (jbEntry != null) {
                        JWTAuthBean jb = jbEntry.getValue();
                        if (jb != null) {
                            JWTAuthVO ja = this.covertToJWTAuthVO(jb);
                            if (ja != null) {
                                this.delpoyTokenAuth(ja);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加一个jwt auth 至dag中
     * @param jb
     */
    public void addTokenAuth(JWTAuthBean jb) {
        if (jb != null && jb.getIssKey() != null) {
            JWTAuthListVO jaList = HttpReqUtil.getObject(this.adminUrl + "/consumers/" + this.getId() + "/jwt",JWTAuthListVO.class);
            this.addTokenAuth(jaList,jb);
        }
    }

    /**
     * 从dag中删除一个jwt auth
     * @param key
     */
    public void deleteTokenAuth(String key) {
        JWTAuthListVO jaList = HttpReqUtil.getObject(this.adminUrl + "/consumers/" + this.getId() + "/jwt",JWTAuthListVO.class);
        if (jaList != null && jaList.getData() != null && jaList.getData().length > 0) {
            for (JWTAuthVO ja : jaList.getData()) {
                if (ja != null && ja.getKey() != null && ja.getKey().equals(key)) {
                    this.deleteTokenAuthById(ja.getId());
                    break;
                }
            }
        }
    }

    private void addTokenAuth(JWTAuthListVO jaList,JWTAuthBean jb) {
        JWTAuthVO existJa = null;
        if (jaList != null && jaList.getData() != null && jaList.getData().length > 0) {
            for (JWTAuthVO ja : jaList.getData()) {
                if (ja != null && ja.getKey() != null && ja.getKey().equals(jb.getIssKey())) {
                    existJa = ja;
                    break;
                }
            }
        }

        if (existJa != null) {
            //dag环境中存在
            this.updateTokenAuth(existJa,this.covertToJWTAuthVO(jb));
        } else {
            this.delpoyTokenAuth(this.covertToJWTAuthVO(jb));
        }
    }

    private void updateTokenAuth(JWTAuthVO oldJa,JWTAuthVO newJa) {
        if (oldJa != null && newJa != null && oldJa.getId() != null) {
            if (oldJa.getAlgorithm() != null && !oldJa.getAlgorithm().equals(newJa.getAlgorithm())
                    || oldJa.getSecret() != null && !oldJa.getSecret().equals(newJa.getSecret())
                    || oldJa.getSecret() == null && newJa.getSecret() != null
                    || oldJa.getRsa_public_key() != null && !oldJa.getRsa_public_key().equals(newJa.getRsa_public_key())
                    || oldJa.getRsa_public_key() == null && newJa.getRsa_public_key() != null) {
                this.deleteTokenAuthById(oldJa.getId());
                this.delpoyTokenAuth(newJa);
            }
        }
    }


    private void delpoyTokenAuth(JWTAuthVO ja) {
        JWTAuthVO p = new JWTAuthVO();
        if (ja.getId() == null) {
            p.setRsa_public_key(ja.getRsa_public_key());
            p.setSecret(ja.getSecret());
            p.setAlgorithm(ja.getAlgorithm());
            p.setKey(ja.getKey());
            try {
                HttpReqUtil.postObject(this.adminUrl + "/consumers/" + this.getId() + "/jwt",p,String.class);
            } catch (HttpClientErrorException.Conflict e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTokenAuthById(String jwtAuthId) {
        try {
            if (jwtAuthId != null && jwtAuthId.length() > 0) {
                HttpReqUtil.delete(this.adminUrl + "/consumers/" + this.getId() + "/jwt/" + jwtAuthId);
            }
        } catch (HttpClientErrorException.Conflict e) {
            e.printStackTrace();
        }
    }


    private JWTAuthVO covertToJWTAuthVO(JWTAuthBean jb) {
        if (jb != null && jb.getAlgorithm() != null && jb.getAlgorithm().trim().length() > 0
                && jb.getIssKey() != null && jb.getIssKey().trim().length() > 0
                && ("HS256".equals(jb.getAlgorithm().trim()) && jb.getSecretKey() != null && jb.getSecretKey().trim().length() > 0
                || "RS256".equals(jb.getAlgorithm().trim()) && jb.getRsaPublicKey() != null && jb.getRsaPublicKey().trim().length() > 0)) {
            JWTAuthVO ja = new JWTAuthVO();
            ja.setKey(jb.getIssKey().trim());
            ja.setAlgorithm(jb.getAlgorithm().trim());
            IdVO consumer = new IdVO();
            consumer.setId(this.getId());
            ja.setConsumer(consumer);
            if ("HS256".equals(ja.getAlgorithm())) {
                ja.setSecret(jb.getSecretKey().trim());
            } else if ("RS256".equals(ja.getAlgorithm())) {
                ja.setRsa_public_key(jb.getRsaPublicKey().trim());
            }

            return ja;
        }
        return null;
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


    public boolean removeGroup(String group) {
        if (group != null) {
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
