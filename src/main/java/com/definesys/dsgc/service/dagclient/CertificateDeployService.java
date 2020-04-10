package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGEnvBean;
import com.definesys.dsgc.service.dagclient.bean.DeployResBean;
import com.definesys.dsgc.service.dagclient.proxy.CertificateProxy;
import com.definesys.dsgc.service.dagclient.proxy.bean.CertificateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateDeployService {

    @Autowired
    private CertificateDeployDao certificateDeployDao;

    @Autowired
    private DAGEnvDao dagEnvDao;

    public DeployResBean deployCertificate(String certId,String envCode,String cert,String key,String snisList){
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return DeployResBean.error("非法的环境信息！");
        }

        if(cert == null ||  cert.trim().length() == 0){
            return DeployResBean.error("证书不能为空!");
        }

        if(key == null ||  key.trim().length() == 0){
            return DeployResBean.error("密钥信息不能为空!");
        }

        CertificateVO cv = new CertificateVO();
        cv.setId(certId);
        cv.setCert(cert);
        cv.setKey(key);
        cv.setSnis(DAGDeployUtils.covertToListBySplit(snisList));

        CertificateProxy cp = new CertificateProxy(env.getAdminLocation(),certId);
        return DeployResBean.success(cp.setCertificate(cv));
    }

    public DeployResBean deleteCertificate(String certId,String envCode){
        DAGEnvBean env = this.dagEnvDao.getDAGEnvInfoByEnvCode(envCode);
        if (env == null || env.getAdminLocation() == null || env.getAdminLocation().trim().length() == 0) {
            return DeployResBean.error("非法的环境信息！");
        }
        CertificateProxy cp = new CertificateProxy(env.getAdminLocation(),certId);
        cp.delete();
        return DeployResBean.success();
    }
}
