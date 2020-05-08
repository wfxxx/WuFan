package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.proxy.ConsumerProxy;
import com.definesys.dsgc.service.dagclient.proxy.OSBConsumerProxy;
import com.definesys.dsgc.service.dagclient.proxy.bean.JWTAuthBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ConsumerDeployService {

    @Autowired
    private ConsumerDeployDao consumerDeployDao;

    @Autowired
    private DAGEnvDao dagEnvDao;


    /**
     * 部署至指定环境
     *
     * @param consumerCode
     * @param envCode
     * @return
     */
    public boolean deployConsumer(String consumerCode,String envCode,String loginUser) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);

        if (env == null || env.getAdminLocation() == null) {
            return false;
        }

        boolean res = true;

        if ("DAG".equals(env.getEnvType())) {
            //网关环境添加用户
            ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
            if (!cp.isFound()) {
                res = cp.add(consumerCode,null);
            }

            if (res) {
                //设置认证
                String basicAuthPd = this.consumerDeployDao.getConsumerBasicAuth(consumerCode,envCode);
                if(basicAuthPd != null && basicAuthPd.trim().length() > 0) {
                    res = cp.setBasicAuth(basicAuthPd);
                }

                //设置jwt token
                List<JWTAuthBean> jaList = this.consumerDeployDao.getJwtAuthList(consumerCode,envCode);
                Map<String,JWTAuthBean> jaMap = new HashMap<String,JWTAuthBean>();
                if(jaList != null && jaList.size() >0){
                    Iterator<JWTAuthBean> iter = jaList.iterator();
                    while(iter.hasNext()){
                        JWTAuthBean jb = iter.next();
                        if(jb != null && jb.getIssKey() != null && jb.getIssKey().trim().length() >0){
                            jb.setIssKey(jb.getIssKey().trim());
                            jaMap.put(jb.getIssKey(),jb);
                        }
                    }
                }
                cp.setAllTokenAuth(jaMap,true);

                //设置groups
                List<String> groups = this.consumerDeployDao.getConsumerGroups(consumerCode);
                res = cp.setGroups(groups);

            }

        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境添加用户
            String basicAuthPd = this.consumerDeployDao.getConsumerBasicAuth(consumerCode,envCode);
            OSBConsumerProxy.newInstance().setUser(envCode,loginUser,consumerCode,basicAuthPd);
        }

        return res;

    }

    /**
     * 添加group至所有已部署环境
     * @param consumerCode
     * @param apiCode
     * @return
     */
    public boolean addDAGConsumerAcl(String consumerCode,String apiCode){

        List<DAGEnvBean> envList = this.dagEnvDao.getConsumerDeployedDAGEnvInfo(consumerCode);

        String group = this.consumerDeployDao.getGroupByApiCode(apiCode);

        if(envList != null && group != null && group.trim().length() >0){
            Iterator<DAGEnvBean> envIter = envList.iterator();
            while(envIter.hasNext()){
                DAGEnvBean env = envIter.next();
                if(env != null && "DAG".equals(env.getEnvType())){
                    ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
                    if (cp.isFound()) {
                        cp.addGroup(group);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 取消group至所有已部署环境
     * @param consumerCode
     * @param apiCode
     * @return
     */
    public boolean removeDAGConsumerAcl(String consumerCode,String apiCode){

        List<DAGEnvBean> envList = this.dagEnvDao.getConsumerDeployedDAGEnvInfo(consumerCode);

        String group = this.consumerDeployDao.getGroupByApiCode(apiCode);

        if(envList != null && group != null && group.trim().length() >0){
            Iterator<DAGEnvBean> envIter = envList.iterator();
            while(envIter.hasNext()){
                DAGEnvBean env = envIter.next();
                if(env != null && "DAG".equals(env.getEnvType())){
                    ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
                    if (cp.isFound()) {
                        cp.removeGroup(group);
                    }
                }
            }
        }
        return true;
    }


    /**
     * 从所有已部署环境移除group
     * @param group
     * @return
     */
    public boolean removeGroupToALLEnv(String group){
        return true;
    }

    /**
     * 从指定环境中取消部署consumer
     *
     * @param consumerCode
     * @param envCode
     * @return
     */
    public boolean undeployConsumer(String consumerCode,String envCode,String loginUser) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);

        if (env == null || env.getAdminLocation() == null) {
            return false;
        }

        if ("DAG".equals(env.getEnvType())) {
            //网关环境取消部署消费者
            ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
            if (cp.isFound()) {
                cp.delete();
            }

        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境取消部署消费者
            OSBConsumerProxy.newInstance().removeUser(envCode,loginUser,consumerCode);

        }

        return true;
    }

    /**
     * 更新指定环境的basic密码
     *
     * @param consumerCode
     * @param newPd
     * @param envCode
     * @return
     */
    public boolean updateBasicAuth(String consumerCode,String newPd,String envCode,String loginUser) {

        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);

        if (env == null || env.getAdminLocation() == null) {
            return false;
        }

        if (newPd == null || newPd.trim().length() == 0) {
            return false;
        }

        boolean res = true;

        if ("DAG".equals(env.getEnvType())) {
            //网关环境查找用户
            ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
//            if (!cp.isFound()) {
//                res = cp.add(consumerCode,null);
//            }
//            if (res) {
//                res = cp.setBasicAuth(newPd);
//            }
            if (cp.isFound()) {
                res = cp.setBasicAuth(newPd);
            }
        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境添加用户
            OSBConsumerProxy.newInstance().setUser(envCode,loginUser,consumerCode,newPd);
        }

        return res;
    }

    /**
     * 删除jwtauth认证
     * @param dcjId
     * @return
     */
    public boolean deleteJWTAuth(String dcjId) {
        JWTAuthBean jb = this.consumerDeployDao.getJwtAuth(dcjId);
        if (jb != null) {
            DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(jb.getEnvCode());

            if (env == null || env.getAdminLocation() == null) {
                return false;
            }
            if ("DAG".equals(env.getEnvType())) {
                //网关环境查找用户
                ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),jb.getCsmCode());
                if (cp.isFound()) {
                    cp.deleteTokenAuth(jb.getIssKey());
                }

            } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
                //OSB总线环境添加用户
            }

        }
        return true;
    }

    /**
     * 添加JWT Token
     * @param jb
     * @return
     */
    public boolean addJWTAuth(JWTAuthBean jb) {
        if (jb != null) {
            DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(jb.getEnvCode());

            if (env == null || env.getAdminLocation() == null) {
                return false;
            }
            if ("DAG".equals(env.getEnvType())) {
                //网关环境查找用户
                ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),jb.getCsmCode());
                if (cp.isFound()) {
                    cp.addTokenAuth(jb);
                }

            } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
                //OSB总线环境添加用户
            }

        }
        return true;
    }


}
