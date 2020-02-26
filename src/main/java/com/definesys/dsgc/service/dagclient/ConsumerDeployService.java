package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.proxy.ConsumerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean deployConsumer(String consumerCode,String envCode) {
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
                String basicAuthPd = this.consumerDeployDao.getConsumerBasicAuth(consumerCode,envCode);
                if(basicAuthPd != null && basicAuthPd.trim().length() > 0) {
                    res = cp.setBasicAuth(basicAuthPd);
                }
            }

        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境添加用户
        }

        return res;

    }

    /**
     * 从指定环境中取消部署consumer
     *
     * @param consumerCode
     * @param envCode
     * @return
     */
    public boolean undeployConsumer(String consumerCode,String envCode) {
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);

        if (env == null || env.getAdminLocation() == null) {
            return false;
        }

        if ("DAG".equals(env.getEnvType())) {
            //网关环境添加用户
            ConsumerProxy cp = new ConsumerProxy(env.getAdminLocation(),consumerCode);
            if (cp.isFound()) {
                cp.delete();
            }

        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境添加用户
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
    public boolean updateBasicAuth(String consumerCode,String newPd,String envCode) {

        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);

        if (env == null || env.getAdminLocation() == null) {
            return false;
        }

        if (newPd == null || newPd.trim().length() == 0) {
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
                res = cp.setBasicAuth(newPd);
            }
        } else if ("ESB".equals(env.getEnvType()) && "OSB".equals(env.getTechType())) {
            //OSB总线环境添加用户
        }

        return res;
    }


}
