package com.definesys.dsgc.service.dagclient.proxy;

import com.definesys.dsgc.service.dagclient.proxy.bean.CertificateVO;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import org.springframework.web.client.HttpClientErrorException;

public class CertificateProxy extends DAGProxy {

    private String baseUrl;

    public CertificateProxy(String adminUrl,String refId) {
        super(adminUrl,refId);
    }

    @Override
    protected void retrieve(String refId) {
        this.baseUrl = this.adminUrl + "/certificates";
        this.entityProxy = HttpReqUtil.getObject(this.baseUrl + "/" + refId,CertificateVO.class);
    }

    @Override
    public void delete() {
        try {
            HttpReqUtil.delete(this.baseUrl + "/" + this.getId());
        } catch (
                HttpClientErrorException.NotFound e) {
            e.printStackTrace();
        }
    }


    public String setCertificate(CertificateVO cert){
       if(isFound()){
           HttpReqUtil.putObject(this.baseUrl+"/"+this.getId(),cert);
       } else{
           this.entityProxy = HttpReqUtil.postObject(this.baseUrl,cert,CertificateVO.class);
       }
       return this.getId();
    }

}
