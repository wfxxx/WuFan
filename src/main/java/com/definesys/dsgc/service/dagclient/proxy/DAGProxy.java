package com.definesys.dsgc.service.dagclient.proxy;

import org.springframework.web.client.HttpClientErrorException;

public abstract class DAGProxy {

    protected String adminUrl;

    protected Object entityProxy;

    public DAGProxy(String adminUrl,String refId) {
        if(adminUrl != null && adminUrl.endsWith("/")){
            this.adminUrl = adminUrl.substring(0,adminUrl.length()-1);
        } else {
            this.adminUrl = adminUrl;
        }
        try {
            this.retrieve(refId);
        } catch (HttpClientErrorException.NotFound e) {
            //网关不存在该对象
        }
    }

    protected abstract void retrieve(String refId);


    public abstract void delete();

    public String getId() {
        if (this.entityProxy != null) {
            try {
                Object idObj = this.entityProxy.getClass().getDeclaredField("id").get(this.entityProxy);
                if (idObj != null) {
                    return idObj.toString();
                } else {
                    return null;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    public boolean isFound() {
        if (this.getId() == null) {
            return false;
        } else {
            return true;
        }
    }


}
