package com.definesys.dsgc.service.svcgen.utils;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.mpaas.common.http.Response;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

public class ServiceGenerateProxy {

    private Object generateServcie;
    private Class generateServcieClass;

    private Class sapUtilsClass;


    public static ServiceGenerateProxy newInstance() {
        try {
            ServiceGenerateProxy proxy = new ServiceGenerateProxy();
            proxy.generateServcieClass = Class.forName("com.definesys.dsgc.common.svcgen.ServiceGenerate");
            proxy.generateServcie = proxy.generateServcieClass.newInstance();
            proxy.sapUtilsClass = Class.forName("com.definesys.dsgc.sap.SapUtils");

            return proxy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * 获取项目目录下接口目录
     *
     * @param projDir
     * @return
     * @throws Exception
     */
    public List<ServDirJsonBean> getServDirByProjDir(String projDir) throws Exception {
        Method listTeamRepositoryDir = this.generateServcieClass.getMethod("listTeamRepositoryDir",String.class,Set.class);
        Object invokeRes = listTeamRepositoryDir.invoke(this.generateServcie,projDir,null);
        List<ServDirJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>)invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<ServDirJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object obj = iters.next();
                    String isDir = this.getObjAttrStringValue(obj,"isDir");
                    if ("Y".equals(isDir)) {
                        ServDirJsonBean serDir = new ServDirJsonBean();
                        serDir.setServDirName(this.getObjAttrStringValue(obj,"objName"));
                        res.add(serDir);
                    }
                }
            }
        }
        return res;
    }

    /**
     * 列出目录下文件和目录
     *
     * @param dirRelativePath
     * @return
     * @throws Exception
     */
    public List<VCObjectJsonBean> listTeamRepositoryDir(String dirRelativePath,Set<String> filter,boolean filterType) throws Exception {
        Method listTeamRepositoryDir = this.generateServcieClass.getMethod("listTeamRepositoryDir",String.class,Set.class,boolean.class);
        Object invokeRes = listTeamRepositoryDir.invoke(this.generateServcie,dirRelativePath,filter,filterType);
        List<VCObjectJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>)invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<VCObjectJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object obj = iters.next();
                    VCObjectJsonBean vcj = new VCObjectJsonBean();
                    vcj.setIsDir(this.getObjAttrStringValue(obj,"isDir"));
                    vcj.setObjName(this.getObjAttrStringValue(obj,"objName"));
                    vcj.setRelativePath(this.getObjAttrStringValue(obj,"relativePath"));
                    res.add(vcj);
                }
            }
        }
        return res;
    }

    /**
     * 获取连接信息
     *
     * @param connName
     * @return
     * @throws Exception
     */
    public SapConnInfoJsonBean getSapConnInfoByName(String connName) throws Exception {
        Method getSapConnInfoByName = this.sapUtilsClass.getMethod("getSapConnInfoByName",String.class);
        Object invokeRes = getSapConnInfoByName.invoke(null,connName);
        SapConnInfoJsonBean b = null;
        if (invokeRes != null) {
            b = this.covertConfigToJsonBean(invokeRes);
        }
        return b;
    }

    private SapConnInfoJsonBean covertConfigToJsonBean(Object cfg) throws Exception {
        SapConnInfoJsonBean b = new SapConnInfoJsonBean();
        if (cfg != null) {
            b.setConnId(this.getObjAttrStringValue(cfg,"connId"));
            b.setSapCient(this.getObjAttrStringValue(cfg,"client"));
            b.setLang(this.getObjAttrStringValue(cfg,"lang"));
            b.setConnUN(this.getObjAttrStringValue(cfg,"user"));
            b.setConnPD(this.getObjAttrStringValue(cfg,"password"));
            b.setSapIp(this.getObjAttrStringValue(cfg,"host"));
            b.setSn(this.getObjAttrStringValue(cfg,"sysnr"));
            b.setConnName(this.getObjAttrStringValue(cfg,"connName"));
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
            List<Object> lst = (List<Object>)invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<SapConnSltJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object cfg = iters.next();
                    if (cfg != null) {
                        SapConnSltJsonBean b = new SapConnSltJsonBean();
                        b.setConnId(this.getObjAttrStringValue(cfg,"connId"));
                        b.setConnName(this.getObjAttrStringValue(cfg,"connName"));
                        res.add(b);
                    }
                }
            }
        }
        return res;
    }

    public String vaildSapConnInfo(String loginUser,SapConnInfoJsonBean connInfo) throws Exception {
        String rtn = null;
        Class cfgCl = Class.forName("com.definesys.dsgc.sap.bean.SapConfiguration");
        Method vaildSapConnInfo = this.sapUtilsClass.getMethod("vaildSapConnInfo",String.class,cfgCl);
        if (connInfo != null) {
            Object param = cfgCl.newInstance();
            this.setObjAttrValue(param,"client",connInfo.getSapCient());
            this.setObjAttrValue(param,"lang",connInfo.getLang());
            this.setObjAttrValue(param,"user",connInfo.getConnUN());
            this.setObjAttrValue(param,"password",connInfo.getConnPD());
            this.setObjAttrValue(param,"host",connInfo.getSapIp());
            this.setObjAttrValue(param,"sysnr",connInfo.getSn());
            this.setObjAttrValue(param,"connName",connInfo.getConnName());

            if (connInfo.getConnId() == null || connInfo.getConnId().trim().length() == 0) {
                this.setObjAttrValue(param,"connId",UUID.randomUUID().toString());
            } else {
                this.setObjAttrValue(param,"connId",connInfo.getConnId());
            }
            Object invokeRes = vaildSapConnInfo.invoke(null,loginUser,param);
            if (invokeRes != null) {
                String rtnCode = this.getObjAttrStringValue(invokeRes,"rtnCode");
                if ("S".equals(rtnCode)) {
                    rtn = "S";
                } else {
                    rtn = this.getObjAttrStringValue(invokeRes,"rtnMsg");
                }
            }
        }
        return rtn;
    }

    public int syncRfcFromSap(String uid,String connName) throws Exception {
        Method syncRfcFromSap = this.sapUtilsClass.getMethod("syncRfcFromSap",String.class,String.class);
        int count = 0;
        Object invokeRes = syncRfcFromSap.invoke(null,uid,connName);
        if (invokeRes != null) {
            Integer integer = (Integer)invokeRes;
            count = integer.intValue();
        }
        return count;
    }

    /**
     * 获取服务基础信息
     *
     * @param servNo
     * @return
     */
    public BasicInfoJsonBean getSvcGenBasicInfo(String servNo) throws Exception {
        Method getCurrentSvcGenBasicInfo = this.generateServcieClass.getMethod("getCurrentSvcGenBasicInfo",String.class);
        Object invokeRes = getCurrentSvcGenBasicInfo.invoke(this.generateServcie,servNo);
        BasicInfoJsonBean res = null;
        if (invokeRes != null) {
            res = new BasicInfoJsonBean();
            res.setServNo(this.getObjAttrStringValue(invokeRes,"servNo"));
            res.setServName(this.getObjAttrStringValue(invokeRes,"servName"));
            res.setServSystemCode(this.getObjAttrStringValue(invokeRes,"servSystemCode"));
            res.setServSystemName(this.getObjAttrStringValue(invokeRes,"servSystemName"));
            res.setTmplCode(this.getObjAttrStringValue(invokeRes,"tmplCode"));
            res.setTmplCodeMeaning(this.getObjAttrStringValue(invokeRes,"tmplCodeMeaning"));
            res.setVcStat(this.getObjAttrStringValue(invokeRes,"vcStat"));
            res.setVcStatMeaning(this.getObjAttrStringValue(invokeRes,"vcStatMeaning"));
            res.setLastUpdateBy(this.getObjAttrStringValue(invokeRes,"lastUpdateBy"));
            Timestamp lud = this.getObjAttrTimestampValue(invokeRes,"lastUpdatedDate");
            if (lud != null) {
                res.setLastUpdatedDate(new Date(lud.getTime()));
            }
        }
        return res;
    }

    /**
     * 获取服务当前使用的代码文件（基于快速配置生成的代码文件）
     *
     * @param servNo
     * @return
     */
    public List<CodeFileJsonBean> getCurrentUsingSvcGenFileList(String servNo) throws Exception {
        Method getCurrentUsingSvcGenFiles = this.generateServcieClass.getMethod("getCurrentUsingSvcGenFiles",String.class);
        Object invokeRes = getCurrentUsingSvcGenFiles.invoke(this.generateServcie,servNo);
        return this.covertToCodeFileJsonBean(invokeRes);
    }


    /**
     * 获取部署配置项信息
     *
     * @param servNo
     * @return
     */
    public List<DeployProfileJsonBean> getServDeployProfileList(String servNo) throws Exception {
        Method getServDeployProfileInfo = this.generateServcieClass.getMethod("getServDeployProfileInfo",String.class);
        Object invokeRes = getServDeployProfileInfo.invoke(this.generateServcie,servNo);
        List<DeployProfileJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>)invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<DeployProfileJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object obj = iters.next();
                    DeployProfileJsonBean djb = new DeployProfileJsonBean();
                    djb.setDeveId(this.getObjAttrStringValue(obj,"deveId"));
                    djb.setDlpCust(null); //查询列表时，不返回内容
                    djb.setDpName(this.getObjAttrStringValue(obj,"dpName"));
                    djb.setDpId(this.getObjAttrStringValue(obj,"dpId"));
                    djb.setEnvCode(this.getObjAttrStringValue(obj,"envCode"));
                    djb.setEnvName(this.getObjAttrStringValue(obj,"envName"));
                    djb.setIsEnable(this.getObjAttrStringValue(obj,"isEnable"));
                    djb.setLastDplDate(this.getObjAttrTimestampValue(obj,"lastDplDate"));
                    djb.setLastDplUser(this.getObjAttrStringValue(obj,"lastDplUser"));
                    djb.setLastUpdatedBy(this.getObjAttrStringValue(obj,"lastUpdatedBy"));
                    djb.setLastUpdateDate(this.getObjAttrTimestampValue(obj,"lastUpdateDate"));
                    djb.setTextAttribute1(this.getObjAttrStringValue(obj,"textAttribute1"));
                    djb.setTextAttribute2(this.getObjAttrStringValue(obj,"textAttribute2"));
                    djb.setTextAttribute3(this.getObjAttrStringValue(obj,"textAttribute3"));
                    res.add(djb);
                }
            }
        }

        return res;
    }

    /**
     * disable 服务部署配置项
     *
     * @param uid
     * @param dpId
     * @return
     */
    public int disableDeployProfileByDPId(String uid,String dpId) throws Exception {
        int res = 0;
        Method disableDeployProfileByDpId = this.generateServcieClass.getMethod("disableDeployProfileByDpId",String.class,String.class);
        Object invokeRes = disableDeployProfileByDpId.invoke(this.generateServcie,uid,dpId);
        if (invokeRes != null) {
            Integer intRes = (Integer)invokeRes;
            res = intRes.intValue();
        }
        return res;
    }

    /**
     * 部署服务
     *
     * @param loginUser
     * @param servNo
     * @param deployProfiles
     * @param deployDesc
     * @return
     * @throws Exception
     */
    public Response deployServcie(String loginUser,String servNo,String deployProfiles,String deployDesc) throws Exception {
        Method deploy = this.generateServcieClass.getMethod("deployService",String.class,String.class,String.class,String.class);
        Object res = deploy.invoke(this.generateServcie,loginUser,servNo,deployProfiles,deployDesc);
        return this.covertRtnMsgToResponse(res);
    }

    /**
     * 获取资源对象产生的uri
     *
     * @param sgObjCode
     * @return
     * @throws Exception
     */
    public List<SVCUriBean> resolveServIBUriList(String sgObjCode) throws Exception {
        Method resolveServIBUriList = this.generateServcieClass.getMethod("resolveServIBUriList",String.class);
        Object res = resolveServIBUriList.invoke(this.generateServcie,sgObjCode);
        List<SVCUriBean> suriList = new ArrayList<SVCUriBean>();
        if (res != null) {
            List<Object> resList = (List<Object>)res;
            Iterator iter = resList.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                SVCUriBean suri = new SVCUriBean();
                suri.setIbUri(this.getObjAttrStringValue(obj,"ibUri"));
                suri.setSoapOper(this.getObjAttrStringValue(obj,"soapOper"));
                suri.setTransportType(this.getObjAttrStringValue(obj,"transportType"));
                suri.setUriType(this.getObjAttrStringValue(obj,"uriType"));
                suriList.add(suri);
            }
        }
        return suriList;
    }


    /**
     * 生成服务
     *
     * @param loginUser
     * @param cfg
     * @param paramMap
     * @return
     * @throws Exception
     */
    public Response generateServiceCode(String loginUser,TmplConfigBean cfg,Map<String,Object> paramMap) throws Exception {
        Method genService = this.generateServcieClass.getMethod("generateServiceCode",String.class,String.class,Map.class);

        Class paramTcbClass = Class.forName("com.definesys.dsgc.common.svcgen.bean.TmplCntBean");
        Object paramTcb = paramTcbClass.newInstance();
        if ("3".equals(cfg.getTmplFlag())) {
            //rfc配置方式
            this.updateRfcSvcGenConfig(cfg,paramTcb);
        } else if ("2".equals(cfg.getTmplFlag())) {
            //ide配置方式
            this.updateIDESvcGenConfig(cfg,paramTcb,2,true);
        } else if ("1".equals(cfg.getTmplFlag())) {
            //Rest配置方式
            this.updateRestSvcGenConfig(cfg,paramTcb,true);
        } else if ("0".equals(cfg.getTmplFlag())) {
            //SOAP配置方式
            this.updateSoapSvcGenConfig(cfg,paramTcb,true);
        } else if ("51".equals(cfg.getTmplFlag())) {
            //静态sa
            this.updateSaCmptGenConfig(cfg,paramTcb,true);
        } else if ("71".equals(cfg.getTmplFlag())) {
            //杂项
            this.updateIDESvcGenConfig(cfg,paramTcb,71,true);
        } else if ("101".equals(cfg.getTmplFlag())) {
            //杂项
            this.updateIDESvcGenConfig(cfg,paramTcb,101,true);
        }
        //设置httpheaders
        List<OBHeaderBean> ohList = cfg.getObHeaders();
        if (ohList != null && !ohList.isEmpty()) {
            this.setObjAttrValue(paramTcb,"obHeaders",this.covertListToMap(ohList),Map.class);
        }
        paramMap.put("tcb",paramTcb);
        Object res = genService.invoke(this.generateServcie,loginUser,cfg.getServNo(),paramMap);
        return this.covertRtnMsgToResponse(res);
    }


    private void updateRfcSvcGenConfig(TmplConfigBean cfg,Object param) throws Exception {
        this.setObjAttrValue(param,"sgObjCode",cfg.getServNo());
        this.setObjAttrValue(param,"projDir",cfg.getProjDir());
        this.setObjAttrValue(param,"tmplCodeFlag",3,int.class);
        this.setObjAttrValue(param,"servDir",cfg.getServDir());
        this.setObjAttrValue(param,"psUri",cfg.getSoapPSUri());
        this.setObjAttrValue(param,"psUri1",cfg.getRestPSUri());
        this.setObjAttrValue(param,"isProfile","N");
        this.setObjAttrValue(param,"provider",cfg.getToSystem());
        this.setObjAttrValue(param,"textAttr1",cfg.getSapConn());
        this.setObjAttrValue(param,"textAttr2",cfg.getRfcName());
        this.setObjAttrValue(param,"servNameCode",cfg.getServNameCode());
        this.setObjAttrValue(param,"needUpdStore",true,boolean.class);
    }

    private void updateRestSvcGenConfig(TmplConfigBean cfg,Object param,boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param,"sgObjCode",cfg.getServNo());
        this.setObjAttrValue(param,"tmplCodeFlag",1,int.class);
        this.setObjAttrValue(param,"projDir",cfg.getProjDir());
        this.setObjAttrValue(param,"servDir",cfg.getServDir());
        this.setObjAttrValue(param,"bsName",cfg.getServNameCode() + "BS");
        this.setObjAttrValue(param,"bsURI",cfg.getBsUri());
        this.setObjAttrValue(param,"psName",cfg.getServNameCode() + "RestPS");
        this.setObjAttrValue(param,"psUri",cfg.getRestPSUri());
        this.setObjAttrValue(param,"plName",cfg.getServNameCode() + "Pipeline");
        this.setObjAttrValue(param,"servNameCode",cfg.getServNameCode());
        this.setObjAttrValue(param,"isProfile","N");
        this.setObjAttrValue(param,"provider",cfg.getToSystem());
        this.setObjAttrValue(param,"saCode",cfg.getSaCode());
        this.setObjAttrValue(param,"needUpdStore",needUpdStore,boolean.class);
    }

    private void updateSaCmptGenConfig(TmplConfigBean cfg,Object param,boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param,"sgObjCode",cfg.getServNo());
        this.setObjAttrValue(param,"tmplCodeFlag",51,int.class);
        this.setObjAttrValue(param,"projDir",cfg.getProjDir());
        this.setObjAttrValue(param,"servNameCode",cfg.getServNameCode());
        this.setObjAttrValue(param,"isProfile","N");
        this.setObjAttrValue(param,"sysCode",cfg.getToSystem());
        this.setObjAttrValue(param,"sgObjDesc",cfg.getServDesc());
        this.setObjAttrValue(param,"bsUserName",cfg.getSaUN());
        this.setObjAttrValue(param,"bsPasswd",cfg.getSaPD());
        this.setObjAttrValue(param,"needUpdStore",needUpdStore,boolean.class);
    }


    private void updateSoapSvcGenConfig(TmplConfigBean cfg,Object param,boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param,"sgObjCode",cfg.getServNo());
        this.setObjAttrValue(param,"tmplCodeFlag",0,int.class);
        this.setObjAttrValue(param,"projDir",cfg.getProjDir());
        this.setObjAttrValue(param,"servDir",cfg.getServDir());
        this.setObjAttrValue(param,"bsName",cfg.getServNameCode() + "BS");
        this.setObjAttrValue(param,"bsURI",cfg.getBsUri());
        this.setObjAttrValue(param,"psName",cfg.getServNameCode() + "SoapPS");
        this.setObjAttrValue(param,"psUri",cfg.getSoapPSUri());
        this.setObjAttrValue(param,"plName",cfg.getServNameCode() + "Pipeline");
        this.setObjAttrValue(param,"servNameCode",cfg.getServNameCode());
        this.setObjAttrValue(param,"isProfile","N");
        this.setObjAttrValue(param,"provider",cfg.getToSystem());
        this.setObjAttrValue(param,"saCode",cfg.getSaCode());
        this.setObjAttrValue(param,"psName1",cfg.getServNameCode() + "RestPS");
        this.setObjAttrValue(param,"psUri1",cfg.getRestPSUri());
        this.setObjAttrValue(param,"wsdlName",cfg.getServNameCode());
        this.setObjAttrValue(param,"textAttr1",cfg.getWsdlUri());
        this.setObjAttrValue(param,"textAttr2",cfg.getWsdlUN());
        this.setObjAttrValue(param,"textAttr3",cfg.getWsdlPD());
        this.setObjAttrValue(param,"sltPort",cfg.getSltPort());
        this.setObjAttrValue(param,"needUpdStore",needUpdStore,boolean.class);
    }


    private void updateIDESvcGenConfig(TmplConfigBean cfg,Object param,int flag,boolean needUpdStore) throws Exception {
        this.setObjAttrValue(param,"sgObjCode",cfg.getServNo());
        this.setObjAttrValue(param,"tmplCodeFlag",flag,int.class);
        this.setObjAttrValue(param,"isProfile","N");
        this.setObjAttrValue(param,"sysCode",cfg.getToSystem());
        this.setObjAttrValue(param,"servNameCode",cfg.getServNameCode());
        this.setObjAttrValue(param,"sgObjDesc",cfg.getServDesc());
        this.setObjAttrValue(param,"needUpdStore",needUpdStore,boolean.class);
    }

    /**
     * 生成服务
     *
     * @param loginUser
     * @param cfg
     * @param paramMap
     * @return
     * @throws Exception
     */
    public List<CodeFileJsonBean> generateIDETmplCodeFiles(String loginUser,TmplConfigBean cfg,Map<String,Object> paramMap) throws Exception {
        Method generateIDETmplCodeFiles = this.generateServcieClass.getMethod("generateIDETmplCodeFiles",String.class,String.class,Map.class);

        Class paramTcbClass = Class.forName("com.definesys.dsgc.common.svcgen.bean.TmplCntBean");
        Object paramTcb = paramTcbClass.newInstance();
        if ("2".equals(cfg.getTmplFlag())) {
            //ide配置方式
            this.updateIDESvcGenConfig(cfg,paramTcb,2,false);
        } else if ("71".equals(cfg.getTmplFlag())) {
            //ide配置方式
            this.updateIDESvcGenConfig(cfg,paramTcb,71,false);
        } else if ("101".equals(cfg.getTmplFlag())) {
            //ide配置方式
            this.updateIDESvcGenConfig(cfg,paramTcb,101,false);
        }
        paramMap.put("tcb",paramTcb);
        Object invokeRes = generateIDETmplCodeFiles.invoke(this.generateServcie,loginUser,cfg.getServNo(),paramMap);
        return covertToCodeFileJsonBean(invokeRes);
    }


    private List<CodeFileJsonBean> covertToCodeFileJsonBean(Object invokeRes) throws Exception {
        List<CodeFileJsonBean> res = null;
        if (invokeRes != null) {
            List<Object> lst = (List<Object>)invokeRes;
            if (lst.size() > 0) {
                res = new ArrayList<CodeFileJsonBean>();
                Iterator iters = lst.iterator();
                while (iters.hasNext()) {
                    Object obj = iters.next();
                    CodeFileJsonBean cfjb = new CodeFileJsonBean();
                    cfjb.setFilePath(this.getObjAttrStringValue(obj,"fileNamePath"));
                    cfjb.setFileSuffix(this.getObjAttrStringValue(obj,"fileSuffix"));
                    Timestamp lud = this.getObjAttrTimestampValue(obj,"lastUpdatedDate");
                    //设置最后更新时间
                    if (lud != null) {
                        cfjb.setLastUpdatedDate(new Date(lud.getTime()));
                    }
                    cfjb.setLastUpdateBy(this.getObjAttrStringValue(obj,"lastUpdateBy"));
                    res.add(cfjb);
                }
            }
        }
        return res;
    }


    /**
     * 生成部署客户化设置文件内容
     *
     * @param servNo
     * @return
     * @throws Exception
     */
    public CustomSettingJsonBean generateIDECustomSettingCnt(String servNo) throws Exception {
        Method generateServiceCustomization = this.generateServcieClass.getMethod("generateServiceCustomization",String.class,String.class,Map.class);
        Map paramMap = new HashMap<String,Object>();
        Object invokeRes = generateServiceCustomization.invoke(this.generateServcie,null,servNo,paramMap);
        CustomSettingJsonBean cust = new CustomSettingJsonBean();
        cust.setServNo(servNo);
        if (invokeRes != null) {
            String rtnCode = this.getObjAttrStringValue(invokeRes,"rtnCode");
            if ("S".equals(rtnCode)) {
                String cnt = this.getObjAttrStringValue(invokeRes,"rtnValue1");
                if (cnt != null && cnt.trim().length() > 0) {
                    cust.setCustomization(Base64.getEncoder().encodeToString(cnt.getBytes(Charset.forName("UTF-8"))));
                }
            }
        }
        return cust;
    }


    public String newRfcDeployProfile(String loginUser,String servNo,String dplName,String envCode,String jndiName) throws Exception {
        Method newRfcDeployProfile = this.generateServcieClass.getMethod("newRfcDeployProfile",String.class,String.class,String.class,String.class,String.class);
        Object res = newRfcDeployProfile.invoke(this.generateServcie,loginUser,servNo,dplName,envCode,jndiName);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    /**
     * 创建ide部署配置项
     *
     * @param loginUser
     * @param servNo
     * @param dplName
     * @param envCode
     * @param dplCust
     * @return
     * @throws Exception
     */
    public String newIdeDeployProfile(String loginUser,String servNo,String dplName,String envCode,String dplCust) throws Exception {
        Method newOsbIdeDeployProfile = this.generateServcieClass.getMethod("newOsbIdeDeployProfile",String.class,String.class,String.class,String.class,String.class);
        Object res = newOsbIdeDeployProfile.invoke(this.generateServcie,loginUser,servNo,dplName,envCode,dplCust);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    /**
     * 新增Rest部署配置项代理方法
     *
     * @param loginUser
     * @param servNo
     * @param dplName
     * @param envCode
     * @param bsUri
     * @param saCode
     * @param ohMap
     * @return
     * @throws Exception
     */
    public String newOsbRestDeployProfile(String loginUser,String servNo,String dplName,String envCode,String bsUri,String saCode,Map<String,String> ohMap) throws Exception {
        Method newOsbRestDeployProfile = this.generateServcieClass.getMethod("newOsbRestDeployProfile",String.class,String.class,String.class,String.class,String.class,String.class,Map.class);
        Object res = newOsbRestDeployProfile.invoke(this.generateServcie,loginUser,servNo,dplName,envCode,bsUri,saCode,ohMap);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    /**
     * 新增SOAP部署配置项代理方法
     *
     * @param loginUser
     * @param servNo
     * @param dplName
     * @param envCode
     * @param bsUri
     * @param saCode
     * @param ohMap
     * @return
     * @throws Exception
     */
    public String newOsbSoapDeployProfile(String loginUser,String servNo,String dplName,String envCode,String bsUri,String saCode,Map<String,String> ohMap) throws Exception {
        Method newOsbSoapDeployProfile = this.generateServcieClass.getMethod("newOsbSoapDeployProfile",String.class,String.class,String.class,String.class,String.class,String.class,Map.class);
        Object res = newOsbSoapDeployProfile.invoke(this.generateServcie,loginUser,servNo,dplName,envCode,bsUri,saCode,ohMap);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    public String newOsbSaCmptDeployProfile(String loginUser,String servNo,String dplName,String envCode,String saUN,String saPD) throws Exception {
        Method newOsbSaCmptDeployProfile = this.generateServcieClass.getMethod("newOsbSaCmptDeployProfile",String.class,String.class,String.class,String.class,String.class,String.class);
        Object res = newOsbSaCmptDeployProfile.invoke(this.generateServcie,loginUser,servNo,dplName,envCode,saUN,saPD);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }


    public Response deployServMetaDataByServNo(String loginUser,String servNo) throws Exception {
        Method deployServMetaDataByServNo = this.generateServcieClass.getMethod("deployServMetaDataByServNo",String.class,String.class);
        Object rtnObj = deployServMetaDataByServNo.invoke(this.generateServcie,loginUser,servNo);

        if (rtnObj != null) {
            String rtnCode = this.getObjAttrStringValue(rtnObj,"rtnCode");
            if ("S".equals(rtnCode)) {
                return Response.ok().setMessage("信息同步成功！");
            } else {
                return Response.error("服务资产信息同步失败！请联系管理员处理");
            }
        }

        return Response.error("服务资产信息同步失败！");
    }

    public Response parseSpyWSDL(String wsdlUri,String uriUN,String uriPD) throws Exception {
        Method parseSpyWSDL = this.generateServcieClass.getMethod("parseSpyWSDL",String.class,String.class,String.class);
        Object res = parseSpyWSDL.invoke(this.generateServcie,wsdlUri,uriUN,uriPD);
        if (res != null) {
            Method getRntMethod = this.getAttributeGetMethod(res.getClass(),"rmb");
            Object rtnObj = getRntMethod.invoke(res);
            if (rtnObj != null) {
                String rtnCode = this.getObjAttrStringValue(rtnObj,"rtnCode");
                if ("S".equals(rtnCode)) {
                    Method getPortMethod = this.getAttributeGetMethod(res.getClass(),"ports");
                    Object portObj = getPortMethod.invoke(res);
                    return Response.ok().setData(portObj);
                } else {
                    return Response.error(this.getObjAttrStringValue(rtnObj,"rtnMsg"));
                }
            }
        }
        return Response.error("wsdl无法解析！");
    }


//    /**
//     * 更新服务快速配置的配置项
//     * @param loginUser
//     * @param cfg
//     * @throws Exception
//     */
//    public void updateSvcGenConfig(String loginUser,TmplConfigBean cfg) throws Exception{
//        Class paramCl = Class.forName("com.definesys.dsgc.common.svcgen.bean.TmplCntBean");
//        Object param = paramCl.newInstance();
//        if("3".equals(cfg.getTmplFlag())){
//            //rfc配置方式
//            this.updateRfcSvcGenConfig(cfg,param);
//        }
//        Method upd = this.generateServcieClass.getMethod("updateSvcGenConfig",String.class,Connection.class,paramCl,boolean.class);
//        upd.invoke(this.generateServcie,loginUser,null,param,false);
//    }


    public Map<String,String> covertListToMap(List<OBHeaderBean> ohs) {
        Map<String,String> res = null;
        if (ohs != null && ohs.size() > 0) {
            res = new HashMap<String,String>();
            Iterator<OBHeaderBean> iters = ohs.iterator();
            while (iters.hasNext()) {
                OBHeaderBean ohb = iters.next();
                if (ohb.getHeaderName() != null && ohb.getHeaderName().trim().length() > 0) {
                    res.put(ohb.getHeaderName(),ohb.getHeaderValue());
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
                Boolean resBoolean = (Boolean)resObj;
                Method getRtnMsg = rtnMsgClass.getMethod("getRtnMsg");
                String msg = (String)getRtnMsg.invoke(rtnMsg);
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


    private String getObjAttrStringValue(Object obj,String attrName) throws Exception {
        Method getMethod = this.getAttributeGetMethod(obj.getClass(),attrName);
        Object res = getMethod.invoke(obj);
        if (res != null) {
            return res.toString();
        } else {
            return null;
        }
    }

    private Timestamp getObjAttrTimestampValue(Object obj,String attrName) throws Exception {
        Method getMethod = this.getAttributeGetMethod(obj.getClass(),attrName);
        Object res = getMethod.invoke(obj);
        if (res != null) {
            return (Timestamp)res;
        } else {
            return null;
        }
    }

    private void setObjAttrValue(Object obj,String attrName,Object attrValue,Class type) throws Exception {
        if (attrValue != null) {
            Method setMethod = this.getAttributeSetMethod(obj.getClass(),attrName,type);
            setMethod.invoke(obj,attrValue);
        }
    }

    private void setObjAttrValue(Object obj,String attrName,Object attrValue) throws Exception {
        if (attrValue != null) {
            Method setMethod = this.getAttributeSetMethod(obj.getClass(),attrName,attrValue.getClass());
            setMethod.invoke(obj,attrValue);
        }
    }

    private Method getAttributeSetMethod(Class cl,String attrName,Class<?> paramType) throws Exception {
        String methodName = null;
        if (attrName != null && attrName.length() > 1) {
            methodName = "set" + attrName.substring(0,1).toUpperCase() + attrName.substring(1);
        } else if (attrName != null && attrName.length() == 1) {
            methodName = "set" + attrName.toUpperCase();
        }

        if (methodName != null) {
            return cl.getMethod(methodName,paramType);
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
