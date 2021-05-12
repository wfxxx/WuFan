package com.definesys.dsgc.service.dagclient.proxy;

import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Method;

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
            Object idObj = null;
            try {
                idObj = this.entityProxy.getClass().getDeclaredField("id").get(this.entityProxy);
            } catch (IllegalAccessException e) {
                try {
                    idObj = this.getObjAttrStringValue(this.entityProxy,"id");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            if (idObj != null) {
                return idObj.toString();
            } else {
                return null;
            }


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


    private String getObjAttrStringValue(Object obj,String attrName) throws Exception {
        Method getMethod = this.getAttributeGetMethod(obj.getClass(),attrName);
        Object res = getMethod.invoke(obj);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    private Method getAttributeGetMethod(Class cl,String attrName) throws Exception {
        String methodName = null;
        if (attrName != null && attrName.length() > 1) {
            methodName = "get" + attrName.substring(0,1).toUpperCase() + attrName.substring(1);
        } else if (attrName != null && attrName.length() == 1) {
            methodName = "get" + attrName.toUpperCase();
        }

        if (methodName != null) {
            return cl.getMethod(methodName);
        } else {
            return null;
        }
    }


}
