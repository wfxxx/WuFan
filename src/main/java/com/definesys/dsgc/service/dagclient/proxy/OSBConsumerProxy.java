package com.definesys.dsgc.service.dagclient.proxy;

import java.lang.reflect.Method;

public class OSBConsumerProxy {

    private Object userProxy;
    private Class userProxyClass;

    public static OSBConsumerProxy newInstance() {
        try {
            OSBConsumerProxy proxy = new OSBConsumerProxy();
            proxy.userProxyClass = Class.forName("com.definesys.dsgc.wls.UserProxy");
            proxy.userProxy = proxy.userProxyClass.newInstance();
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void setUser(String envCode,String loginUser,String un,String pd) {
        try {
            Method setUserMethod = this.userProxyClass.getMethod("setUser",String.class,String.class,String.class,String.class);
            setUserMethod.invoke(this.userProxy,envCode,loginUser,un,pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String envCode,String loginUser,String un){
        try {
            Method removeUserMethod = this.userProxyClass.getMethod("removeUser",String.class,String.class,String.class);
            removeUserMethod.invoke(this.userProxy,envCode,loginUser,un);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
