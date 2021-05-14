package com.definesys.dsgc.service.ystar.svcgen.util;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.mpaas.common.http.Response;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

public class ServiceGenerateProxy {

    private Class sapUtilsClass;


    public static ServiceGenerateProxy newInstance() {
        try {
            ServiceGenerateProxy proxy = new ServiceGenerateProxy();
            proxy.sapUtilsClass = Class.forName("com.definesys.dsgc.sap.SapUtils");
            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取连接信息
     *
     * @param connName
     * @return
     * @throws Exception
     */
    public SapConnInfoJsonBean getSapConnInfoByName(String connName) throws Exception {
        Method getSapConnInfoByName = this.sapUtilsClass.getMethod("getSapConnInfoByName", String.class);
        Object invokeRes = getSapConnInfoByName.invoke(null, connName);
        SapConnInfoJsonBean b = null;
        if (invokeRes != null) {
            b = this.covertConfigToJsonBean(invokeRes);
        }
        return b;
    }

    private SapConnInfoJsonBean covertConfigToJsonBean(Object cfg) throws Exception {
        SapConnInfoJsonBean b = new SapConnInfoJsonBean();
        if (cfg != null) {
            b.setConnId(this.getObjAttrStringValue(cfg, "connId"));
            b.setSapClient(this.getObjAttrStringValue(cfg, "client"));
            b.setLang(this.getObjAttrStringValue(cfg, "lang"));
            b.setConnUN(this.getObjAttrStringValue(cfg, "user"));
            b.setConnPD(this.getObjAttrStringValue(cfg, "password"));
            b.setSapIp(this.getObjAttrStringValue(cfg, "host"));
            b.setSn(this.getObjAttrStringValue(cfg, "sysnr"));
            b.setConnName(this.getObjAttrStringValue(cfg, "connName"));
        }
        return b;
    }

    /**
     * 获取所有sap连接信息
     *
     * @return
     * @throws Exception
     */
    public List<SapConnSltJsonBean> getSapConnList() throws Exception {
        Method getSapConnList = this.sapUtilsClass.getMethod("getSapConnList");
        Object invokeRes = getSapConnList.invoke(null);
        List<SapConnSltJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>) invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<SapConnSltJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object cfg = iters.next();
                    if (cfg != null) {
                        SapConnSltJsonBean b = new SapConnSltJsonBean();
                        b.setConnId(this.getObjAttrStringValue(cfg, "connId"));
                        b.setConnName(this.getObjAttrStringValue(cfg, "connName"));
                        res.add(b);
                    }
                }
            }
        }
        return res;
    }

    public String validSapConnInfo(SapConnInfoJsonBean connInfo) throws Exception {
        String rtn = null;
        Class cfgCl = Class.forName("com.definesys.dsgc.sap.bean.SapConfiguration");
        Method validSapConnInfo = this.sapUtilsClass.getMethod("validSapConnInfo", String.class, cfgCl);
        if (connInfo != null) {
            Object param = cfgCl.newInstance();
            this.setObjAttrValue(param, "client", connInfo.getSapClient());
            this.setObjAttrValue(param, "lang", connInfo.getLang());
            this.setObjAttrValue(param, "user", connInfo.getConnUN());
            this.setObjAttrValue(param, "password", connInfo.getConnPD());
            this.setObjAttrValue(param, "host", connInfo.getSapIp());
            this.setObjAttrValue(param, "sysnr", connInfo.getSn());
            this.setObjAttrValue(param, "connName", connInfo.getConnName());
            if (connInfo.getConnId() == null || connInfo.getConnId().trim().length() == 0) {
                this.setObjAttrValue(param, "connId", UUID.randomUUID().toString());
            } else {
                this.setObjAttrValue(param, "connId", connInfo.getConnId());
            }
            Object invokeRes = validSapConnInfo.invoke(null, null, param);
            if (invokeRes != null) {
                String rtnCode = this.getObjAttrStringValue(invokeRes, "rtnCode");
                if ("S".equals(rtnCode)) {
                    rtn = "S";
                } else {
                    rtn = this.getObjAttrStringValue(invokeRes, "rtnMsg");
                }
            }
        }
        return rtn;
    }

    public int syncRfcFromSap(String uid, String connName) throws Exception {
        Method syncRfcFromSap = this.sapUtilsClass.getMethod("syncRfcFromSap", String.class, String.class);
        int count = 0;
        Object invokeRes = syncRfcFromSap.invoke(null, uid, connName);
        if (invokeRes != null) {
            Integer integer = (Integer) invokeRes;
            count = integer.intValue();
        }
        return count;
    }


    private void updateRfcSvcGenConfig(TmplConfigBean cfg, Object param) throws Exception {
        this.setObjAttrValue(param, "sgObjCode", cfg.getServNo());
        this.setObjAttrValue(param, "projDir", cfg.getProjDir());
        this.setObjAttrValue(param, "tmplCodeFlag", 3, int.class);
        this.setObjAttrValue(param, "servDir", cfg.getServDir());
        this.setObjAttrValue(param, "psUri", cfg.getSoapPSUri());
        this.setObjAttrValue(param, "psUri1", cfg.getRestPSUri());
        this.setObjAttrValue(param, "isProfile", "N");
        this.setObjAttrValue(param, "provider", cfg.getToSystem());
        this.setObjAttrValue(param, "textAttr1", cfg.getSapConn());
        this.setObjAttrValue(param, "textAttr2", cfg.getRfcName());
        this.setObjAttrValue(param, "servNameCode", cfg.getServNameCode());
        this.setObjAttrValue(param, "sysCode", cfg.getAppCode());
        this.setObjAttrValue(param, "needUpdStore", true, boolean.class);
    }

    private void updateRestSvcGenConfig(TmplConfigBean cfg, Object param, boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param, "sgObjCode", cfg.getServNo());
        this.setObjAttrValue(param, "tmplCodeFlag", 1, int.class);
        this.setObjAttrValue(param, "projDir", cfg.getProjDir());
        this.setObjAttrValue(param, "servDir", cfg.getServDir());
        this.setObjAttrValue(param, "bsName", cfg.getServNameCode() + "BS");
        this.setObjAttrValue(param, "bsURI", cfg.getBsUri());
        this.setObjAttrValue(param, "psName", cfg.getServNameCode() + "RestPS");
        this.setObjAttrValue(param, "psUri", cfg.getRestPSUri());
        this.setObjAttrValue(param, "plName", cfg.getServNameCode() + "Pipeline");
        this.setObjAttrValue(param, "servNameCode", cfg.getServNameCode());
        this.setObjAttrValue(param, "sysCode", cfg.getAppCode());
        this.setObjAttrValue(param, "isProfile", "N");
        this.setObjAttrValue(param, "provider", cfg.getToSystem());
        this.setObjAttrValue(param, "saCode", cfg.getSaCode());
        this.setObjAttrValue(param, "needUpdStore", needUpdStore, boolean.class);
    }

    private void updateSaCmptGenConfig(TmplConfigBean cfg, Object param, boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param, "sgObjCode", cfg.getServNo());
        this.setObjAttrValue(param, "tmplCodeFlag", 51, int.class);
        this.setObjAttrValue(param, "projDir", cfg.getProjDir());
        this.setObjAttrValue(param, "servNameCode", cfg.getServNameCode());
        this.setObjAttrValue(param, "isProfile", "N");
        this.setObjAttrValue(param, "sysCode", cfg.getToSystem());
        this.setObjAttrValue(param, "sgObjDesc", cfg.getServDesc());
        this.setObjAttrValue(param, "bsUserName", cfg.getSaUN());
        this.setObjAttrValue(param, "bsPasswd", cfg.getSaPD());
        this.setObjAttrValue(param, "needUpdStore", needUpdStore, boolean.class);
    }


    private void updateSoapSvcGenConfig(TmplConfigBean cfg, Object param, boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param, "sgObjCode", cfg.getServNo());
        this.setObjAttrValue(param, "tmplCodeFlag", 0, int.class);
        this.setObjAttrValue(param, "projDir", cfg.getProjDir());
        this.setObjAttrValue(param, "servDir", cfg.getServDir());
        this.setObjAttrValue(param, "bsName", cfg.getServNameCode() + "BS");
        this.setObjAttrValue(param, "bsURI", cfg.getBsUri());
        this.setObjAttrValue(param, "psName", cfg.getServNameCode() + "SoapPS");
        this.setObjAttrValue(param, "psUri", cfg.getSoapPSUri());
        this.setObjAttrValue(param, "plName", cfg.getServNameCode() + "Pipeline");
        this.setObjAttrValue(param, "servNameCode", cfg.getServNameCode());
        this.setObjAttrValue(param, "sysCode", cfg.getAppCode());
        this.setObjAttrValue(param, "isProfile", "N");
        this.setObjAttrValue(param, "provider", cfg.getToSystem());
        this.setObjAttrValue(param, "saCode", cfg.getSaCode());
        this.setObjAttrValue(param, "psName1", cfg.getServNameCode() + "RestPS");
        this.setObjAttrValue(param, "psUri1", cfg.getRestPSUri());
        this.setObjAttrValue(param, "wsdlName", cfg.getServNameCode());
        this.setObjAttrValue(param, "textAttr1", cfg.getWsdlUri());
        this.setObjAttrValue(param, "textAttr2", cfg.getWsdlUN());
        this.setObjAttrValue(param, "textAttr3", cfg.getWsdlPD());
        this.setObjAttrValue(param, "sltPort", cfg.getSltPort());
        this.setObjAttrValue(param, "needUpdStore", needUpdStore, boolean.class);
    }


    private void updateIDESvcGenConfig(TmplConfigBean cfg, Object param, int flag, boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param, "sgObjCode", cfg.getServNo());
        this.setObjAttrValue(param, "tmplCodeFlag", flag, int.class);
        this.setObjAttrValue(param, "isProfile", "N");
        this.setObjAttrValue(param, "sysCode", cfg.getToSystem());
        this.setObjAttrValue(param, "servNameCode", cfg.getServNameCode());
        this.setObjAttrValue(param, "sysCode", cfg.getAppCode());
        this.setObjAttrValue(param, "sgObjDesc", cfg.getServDesc());
        this.setObjAttrValue(param, "needUpdStore", needUpdStore, boolean.class);
    }


    private List<CodeFileJsonBean> covertToCodeFileJsonBean(Object invokeRes) throws Exception {
        List<CodeFileJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>) invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<CodeFileJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object obj = iters.next();
                    CodeFileJsonBean cfjb = new CodeFileJsonBean();
                    cfjb.setFilePath(this.getObjAttrStringValue(obj, "fileNamePath"));
                    cfjb.setFileSuffix(this.getObjAttrStringValue(obj, "fileSuffix"));
                    Timestamp lud = this.getObjAttrTimestampValue(obj, "lastUpdatedDate");
                    //设置最后更新时间
                    if (lud != null) {
                        cfjb.setLastUpdatedDate(new Date(lud.getTime()));
                    }
                    cfjb.setLastUpdateBy(this.getObjAttrStringValue(obj, "lastUpdateBy"));
                    res.add(cfjb);
                }
            }
        }
        return res;
    }


    public Map<String, String> covertListToMap(List<OBHeaderBean> ohs) {
        Map<String, String> res = null;
        if (ohs != null && ohs.size() > 0) {
            res = new HashMap<String, String>();
            Iterator<OBHeaderBean> iters = ohs.iterator();
            while (iters.hasNext()) {
                OBHeaderBean ohb = iters.next();
                if (ohb.getHeaderName() != null && ohb.getHeaderName().trim().length() > 0) {
                    res.put(ohb.getHeaderName(), ohb.getHeaderValue());
                }
            }
        }
        return res;
    }


    private Response covertRtnMsgToResponse(Object rtnMsg) throws Exception {
        if (rtnMsg != null) {
            Class rtnMsgClass = Class.forName("com.definesys.dsgc.common.bean.RtnMsgBean");
            Method isSuccessMethod = rtnMsgClass.getMethod("getRtnCodeIsSuccess");
            Object resObj = isSuccessMethod.invoke(rtnMsg);
            if (resObj != null && resObj instanceof Boolean) {
                Boolean resBoolean = (Boolean) resObj;
                Method getRtnMsg = rtnMsgClass.getMethod("getRtnMsg");
                String msg = (String) getRtnMsg.invoke(rtnMsg);
                if (resBoolean.booleanValue()) {
                    return Response.ok().setMessage(msg);
                } else {
                    return Response.error(msg);
                }
            } else {
                return Response.error("未知的处理结果");
            }
        } else {
            return Response.error("空的返回值！");
        }
    }


    private String getObjAttrStringValue(Object obj, String attrName) throws Exception {
        Method getMethod = this.getAttributeGetMethod(obj.getClass(), attrName);
        Object res = getMethod.invoke(obj);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    private Timestamp getObjAttrTimestampValue(Object obj, String attrName) throws Exception {
        Method getMethod = this.getAttributeGetMethod(obj.getClass(), attrName);
        Object res = getMethod.invoke(obj);
        if (res != null) {
            return (Timestamp) res;
        } else {
            return null;
        }
    }

    private void setObjAttrValue(Object obj, String attrName, Object attrValue, Class type) throws Exception {
        if (attrValue != null) {
            Method setMethod = this.getAttributeSetMethod(obj.getClass(), attrName, type);
            setMethod.invoke(obj, attrValue);
        }
    }

    private void setObjAttrValue(Object obj, String attrName, Object attrValue) throws Exception {
        if (attrValue != null) {
            Method setMethod = this.getAttributeSetMethod(obj.getClass(), attrName, attrValue.getClass());
            setMethod.invoke(obj, attrValue);
        }
    }

    private Method getAttributeSetMethod(Class cl, String attrName, Class<?> paramType) throws Exception {
        String methodName = null;
        if (attrName != null && attrName.length() > 1) {
            methodName = "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
        } else if (attrName != null && attrName.length() == 1) {
            methodName = "set" + attrName.toUpperCase();
        }

        if (methodName != null) {
            return cl.getMethod(methodName, paramType);
        } else {
            return null;
        }
    }

    private Method getAttributeGetMethod(Class cl, String attrName) throws Exception {
        String methodName = null;
        if (attrName != null && attrName.length() > 1) {
            methodName = "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
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
