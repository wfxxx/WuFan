package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.dsgc.service.svcgen.bean.db.RspDTO;
import com.definesys.dsgc.service.svcgen.bean.db.TblDTO;

import java.util.ArrayList;
import java.util.List;

public class TmplConfigBean {
    private String dpName;
    private String envCode;
    private String isEnable;
    private String deveId;
    private String servNo;
    private String projDir;
    private String servDir;
    private String servNameCode;
    private String appCode;
    private String toSystem;
    private String bsUri;
    private String soapPSUri;
    private String restPSUri;
    private String wsdlUri;
    private String sltPort;
    private String wsdlUN;
    private String wsdlPD;
    private String rfcName;
    private String sapConn;
    private String tmplFlag;
    private String saCode;
    private String saForWsdl;
    private String saUN;
    private String saPD;
    private String servDesc;
    private List<OBHeaderBean> obHeaders;
    private String resolveDenpendencies;
    private List<String> toResolveFileList = new ArrayList<>();

    //db 快速配置
    private String dbConn;
    private String dbOper;
    private TblDTO[] tbls;
    private RspDTO[] rsps;
    private String sqlcode;

//    //创建服务代码并同时创建服务资产信息
//    private String servAssetNo;
//    private String servName;
//    private String servSystem;
//    private String servShareType;

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getServNo() {
        return servNo;
    }

    public String getDeveId() {
        return deveId;
    }

    public void setDeveId(String deveId) {
        this.deveId = deveId;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getProjDir() {
        return projDir;
    }

    public void setProjDir(String projDir) {
        this.projDir = projDir;
    }

    public String getServDir() {
        return servDir;
    }

    public void setServDir(String servDir) {
        this.servDir = servDir;
    }

    public String getServNameCode() {
        return servNameCode;
    }

    public void setServNameCode(String servNameCode) {
        this.servNameCode = servNameCode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getToSystem() {
        return toSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    public String getBsUri() {
        return bsUri;
    }

    public void setBsUri(String bsUri) {
        this.bsUri = bsUri;
    }

    public String getSoapPSUri() {
        return soapPSUri;
    }

    public void setSoapPSUri(String soapPSUri) {
        this.soapPSUri = soapPSUri;
    }

    public String getRestPSUri() {
        return restPSUri;
    }

    public void setRestPSUri(String restPSUri) {
        this.restPSUri = restPSUri;
    }

    public String getWsdlUri() {
        return wsdlUri;
    }

    public void setWsdlUri(String wsdlUri) {
        this.wsdlUri = wsdlUri;
    }

    public String getSltPort() {
        return sltPort;
    }

    public void setSltPort(String sltPort) {
        this.sltPort = sltPort;
    }

    public String getWsdlUN() {
        return wsdlUN;
    }

    public void setWsdlUN(String wsdlUN) {
        this.wsdlUN = wsdlUN;
    }

    public String getWsdlPD() {
        return wsdlPD;
    }

    public void setWsdlPD(String wsdlPD) {
        this.wsdlPD = wsdlPD;
    }

    public List<OBHeaderBean> getObHeaders() {
        return obHeaders;
    }

    public void setObHeaders(List<OBHeaderBean> obHeaders) {
        this.obHeaders = obHeaders;
    }

    public String getRfcName() {
        return rfcName;
    }

    public void setRfcName(String rfcName) {
        this.rfcName = rfcName;
    }

    public String getSapConn() {
        return sapConn;
    }

    public void setSapConn(String sapConn) {
        this.sapConn = sapConn;
    }

    public String getTmplFlag() {
        return tmplFlag;
    }

    public void setTmplFlag(String tmplFlag) {
        this.tmplFlag = tmplFlag;
    }


    public String getResolveDenpendencies() {
        return resolveDenpendencies;
    }

    public void setResolveDenpendencies(String resolveDenpendencies) {
        this.resolveDenpendencies = resolveDenpendencies;
    }

    public List<String> getToResolveFileList() {
        return toResolveFileList;
    }

    public void setToResolveFileList(List<String> toResolveFileList) {
        this.toResolveFileList = toResolveFileList;
    }

    public String getSaCode() {
        return saCode;
    }

    public void setSaCode(String saCode) {
        this.saCode = saCode;
    }

    public String getSaUN() {
        return saUN;
    }

    public void setSaUN(String saUN) {
        this.saUN = saUN;
    }

    public String getSaPD() {
        return saPD;
    }

    public void setSaPD(String saPD) {
        this.saPD = saPD;
    }

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public String getSaForWsdl() {
        return saForWsdl;
    }

    public void setSaForWsdl(String saForWsdl) {
        this.saForWsdl = saForWsdl;
    }

    public String getDbConn() {
        return dbConn;
    }

    public void setDbConn(String dbConn) {
        this.dbConn = dbConn;
    }

    public String getDbOper() {
        return dbOper;
    }

    public void setDbOper(String dbOper) {
        this.dbOper = dbOper;
    }

    public TblDTO[] getTbls() {
        return tbls;
    }

    public void setTbls(TblDTO[] tbls) {
        this.tbls = tbls;
    }

    public RspDTO[] getRsps() {
        return rsps;
    }

    public void setRsps(RspDTO[] rsps) {
        this.rsps = rsps;
    }

    public String getSqlcode() {
        return sqlcode;
    }

    public void setSqlcode(String sqlcode) {
        this.sqlcode = sqlcode;
    }

    //    public String getServAssetNo() {
//        return servAssetNo;
//    }
//
//    public void setServAssetNo(String servAssetNo) {
//        this.servAssetNo = servAssetNo;
//    }
//
//    public String getServName() {
//        return servName;
//    }
//
//    public void setServName(String servName) {
//        this.servName = servName;
//    }
//
//    public String getServSystem() {
//        return servSystem;
//    }
//
//    public void setServSystem(String servSystem) {
//        this.servSystem = servSystem;
//    }
//
//    public String getServShareType() {
//        return servShareType;
//    }
//
//    public void setServShareType(String servShareType) {
//        this.servShareType = servShareType;
//    }
}
