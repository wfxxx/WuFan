package com.definesys.dsgc.service.svcmng.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName WsdlResolvProxy
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-14 17:38
 * @Version 1.0
 **/

public class WsdlResolvProxy {

    private Object resolverWsdlServcie;
    private Class resolverWsdlClass;

    public static WsdlResolvProxy newInstance() {
        try {
            WsdlResolvProxy proxy = new WsdlResolvProxy();
            proxy.resolverWsdlClass = Class.forName("com.definesys.dsgc.common.svcgen.utils.ResolverWsdl");
            proxy.resolverWsdlServcie = proxy.resolverWsdlClass.newInstance();
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取wsdl的报文请求实例
     *
     * @param wsdlUrl methodName
     * @return
     * @throws Exception
     */
    public String getWsdlBodyReq(String wsdlUrl,String methodName)  {
        String invokeRes = null;
        try {
            Method getBodyByUrl = this.resolverWsdlClass.getMethod("getWsdlBodyReq",String.class, String.class);
            invokeRes = (String) getBodyByUrl.invoke(this.resolverWsdlServcie,wsdlUrl,methodName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invokeRes;
    }


    /**
     * 获取wsdl的响应报文实例
     *
     * @param wsdlUrl methodName
     * @return
     * @throws Exception
     */
    public String getWsdlBodyRes(String wsdlUrl,String methodName)  {
        String invokeRes = null;
        try {
            Method getBodyByUrl = this.resolverWsdlClass.getMethod("getWsdlBodyByRes",String.class, String.class);
            invokeRes = (String) getBodyByUrl.invoke(this.resolverWsdlServcie,wsdlUrl,methodName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return invokeRes;
    }



}
