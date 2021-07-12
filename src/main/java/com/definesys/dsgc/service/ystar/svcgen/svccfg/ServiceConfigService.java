package com.definesys.dsgc.service.ystar.svcgen.svccfg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesDao;
import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.dsgc.service.ystar.svcgen.conn.SvcGenConnDao;
import com.definesys.dsgc.service.ystar.svcgen.proj.DSGCProjDirManagerDao;
import com.definesys.dsgc.service.ystar.svcgen.proj.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.svcgen.RFCStepsService;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.svcgen.dbcfg.DBSvcCfgDao;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.SvcgenServiceInfoDao;
import com.definesys.dsgc.service.ystar.svcgen.bean.*;
import com.definesys.dsgc.service.ystar.svcgen.restcfg.RestServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.sapcfg.SapServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.soapcfg.SoapServiceConfigDTO;
import com.definesys.dsgc.service.ystar.utils.MuleXmlConfUtils;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: RestServiceConfigService
 * @Description: Rest接口快速开发
 * @Author：afan
 * @Date : 2020/1/7 9:36
 */
@Service
public class ServiceConfigService {
    @Autowired
    private DSGCServiceDao dsgcServiceDao;
    @Autowired
    private DSGCServiceService dsgcServiceService;
    @Autowired
    private FndPropertiesService fndPropertiesService;
    @Autowired
    private RFCStepsService rfcStepsService;
    @Autowired
    private SvcgenServiceInfoDao svcgenServiceInfoDao;
    @Autowired
    private DSGCProjDirManagerDao proDirManagerDao;
    @Autowired
    private ServiceConfigDao serviceConfigDao;
    @Autowired
    private DBSvcCfgDao dbSvcCfgDao;
    @Autowired
    private SvcGenConnDao svcGenConnDao;

    /**
     * @Description: Rest接口快速开发
     * @author: ystar
     * @param: [restServiceConfigDTO]
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response saveRestService(RestServiceConfigDTO restServiceConfigDTO, String userName) {
        // 获取配置项目目录
        String proId = restServiceConfigDTO.getProjId();
        // 新增快速配置接口信息
        SvcgenServiceInfo svcgenServiceInfo = new SvcgenServiceInfo();
        svcgenServiceInfo.setSvcCode(restServiceConfigDTO.getSvcCode());
        svcgenServiceInfo.setSvcName(restServiceConfigDTO.getSvcName());
        svcgenServiceInfo.setProjId(proId);
        svcgenServiceInfo.setProjName(restServiceConfigDTO.getProjName());
        svcgenServiceInfo.setCreatedBy(userName);
        svcgenServiceInfo.setSysCode(restServiceConfigDTO.getToSystem());
        svcgenServiceInfo.setSvcType("REST");
        // 存服务详细信息
        String svcDetail = JSONObject.toJSONString(restServiceConfigDTO);
        svcgenServiceInfo.setSvcInfo(svcDetail);
        svcgenServiceInfoDao.insertSvcServInfo(svcgenServiceInfo);

        return Response.ok().setMessage("接口配置保存成功");
    }

    public Response pageQuerySvcGenInfo(String reqParam, int pageIndex, int pageSize) {
        return Response.ok().data(svcgenServiceInfoDao.pageQuerySvcGenInfo(reqParam, pageIndex, pageSize));
    }

    public Response generateService(String servNo) {
        // 获取快速配置服务信息
        SvcgenServiceInfo svcGen = svcgenServiceInfoDao.querySvcGenInfoBySvcCode(servNo);
        if (svcGen == null) {
            return Response.error("找不到该服务配置");
        }
        // 拿到接口完整的配置信息
        String configInfo = svcGen.getTextAttribute1();
        // 本地仓库地址
        String repoHome = fndPropertiesService.getLocalHome("repo");
        boolean isSuccess = false;
        if ("REST".equals(svcGen.getSvcType())) {
            RestServiceConfigDTO restConfig = JSONObject.toJavaObject(JSON.parseObject(configInfo), RestServiceConfigDTO.class);
            String xmlConfigFile = repoHome + "/src/main/app/" + restConfig.getProjName() + ".xml";
            System.out.println("xmlConfigFile->" + xmlConfigFile);
            System.out.println("-1--->" + restConfig.toString());
            isSuccess = MuleXmlConfUtils.appendProjectCode(repoHome, restConfig.getProjName(), svcGen.getSvcType(), restConfig);
        } else if ("SOAP".equals(svcGen.getSvcType())) {
            SoapServiceConfigDTO soapConfig = JSONObject.toJavaObject(JSON.parseObject(configInfo), SoapServiceConfigDTO.class);
            isSuccess = MuleXmlConfUtils.appendProjectCode(repoHome, soapConfig.getProjName(), svcGen.getSvcType(), soapConfig);
        } else if ("DB".equals(svcGen.getSvcType())) {
            SoapServiceConfigDTO soapConfig = JSONObject.toJavaObject(JSON.parseObject(configInfo), SoapServiceConfigDTO.class);
            isSuccess = MuleXmlConfUtils.appendProjectCode(repoHome, soapConfig.getProjName(), svcGen.getSvcType(), soapConfig);
        } else if ("SAP".equals(svcGen.getSvcType())) {
            SapServiceConfigDTO sapServiceConfigDTO = JSONObject.toJavaObject(JSON.parseObject(configInfo), SapServiceConfigDTO.class);
            try {
                SapConnInfoJsonBean sapConnInfoByName = rfcStepsService.getSapConnInfoByName(sapServiceConfigDTO.getSapConn());
                String env = "uat";
                String propsPath = repoHome + "/src/main/app/" + env + ".xml";
                Map<String, String> sapConfigMap = MuleXmlConfUtils.sapConfig(propsPath, sapConnInfoByName);
                isSuccess = MuleXmlConfUtils.appendSapConf(repoHome, sapConfigMap, sapServiceConfigDTO, sapConnInfoByName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!isSuccess) {
            return Response.error("配置接口失败");
        }
        // 更新部署状态
        svcgenServiceInfoDao.updateRestStatus(servNo);
        return Response.ok();
    }

    @Transactional
    public Response saveSoapService(TmplConfigBean tmplConfigBean, String userId) {
        // 获取配置项目目录
        String proId = tmplConfigBean.getProjId();
        DSGCSysProfileDir sysProfileDir = proDirManagerDao.getSysProfileDirByProId(proId);
        // 新增快速配置接口信息
        SvcgenServiceInfo svcgenServiceInfo = new SvcgenServiceInfo();
        svcgenServiceInfo.setSvcCode(tmplConfigBean.getServNo());
        svcgenServiceInfo.setSvcName(tmplConfigBean.getSvcName());
        svcgenServiceInfo.setProjId(proId);
        svcgenServiceInfo.setProjName(sysProfileDir.getProjName());
        svcgenServiceInfo.setCreatedBy(userId);
        svcgenServiceInfo.setSysCode(tmplConfigBean.getAppCode());
        svcgenServiceInfo.setSvcType("SOAP");
        // 存服务详细信息
        String svcDetail = JSONObject.toJSONString(tmplConfigBean);
        svcgenServiceInfo.setSvcInfo(svcDetail);
        svcgenServiceInfoDao.insertSvcServInfo(svcgenServiceInfo);
        return Response.ok().setMessage("接口配置保存成功");
    }

    public Response saveSapService(SapServiceConfigDTO sapServiceConfigDTO, String userName) {// 判断敏捷开发的服务中是否已经有了
        List<SvcgenServiceInfo> serviceInfos = svcgenServiceInfoDao.queryServByservNo(sapServiceConfigDTO.getAppCode());
        if (serviceInfos.size() > 0) {
            return Response.error("敏捷服务编号已存在!");
        }
        // 新增快速配置接口信息
        SvcgenServiceInfo svcgenServiceInfo = new SvcgenServiceInfo();
        svcgenServiceInfo.setSvcCode(sapServiceConfigDTO.getAppCode());
        svcgenServiceInfo.setSvcName(sapServiceConfigDTO.getServName());
        svcgenServiceInfo.setProjId(sapServiceConfigDTO.getResConfig());
        svcgenServiceInfo.setCreatedBy(userName);
        svcgenServiceInfo.setSysCode(sapServiceConfigDTO.getToSystem());
        svcgenServiceInfo.setSvcType("REST");
        // 存服务详细信息
        String servdetail = JSONObject.toJSONString(sapServiceConfigDTO);
        svcgenServiceInfo.setTextAttribute1(servdetail);
        svcgenServiceInfoDao.insertSvcServInfo(svcgenServiceInfo);
        // 新增服务资产
        DSGCServiceVO dsgcServiceVO = new DSGCServiceVO();
        dsgcServiceVO.setServNo(sapServiceConfigDTO.getAppCode());
        dsgcServiceVO.setServName(sapServiceConfigDTO.getServName());
        dsgcServiceVO.setSubordinateSystem(sapServiceConfigDTO.getToSystem());
        //设置服务地址
        DSGCServicesUri dsgcServicesUri = new DSGCServicesUri();
        dsgcServicesUri.setServNo(sapServiceConfigDTO.getAppCode());
        dsgcServicesUri.setIbUri(sapServiceConfigDTO.getAppUri());
        dsgcServicesUri.setHttpMethod("POST");
        dsgcServicesUri.setUriType("REST");
        ArrayList<DSGCServicesUri> dsgcServicesUris = new ArrayList<>();
        dsgcServicesUris.add(dsgcServicesUri);
        dsgcServiceVO.setServicesUris(dsgcServicesUris);
        dsgcServiceService.insertDsgcService(dsgcServiceVO);

        return Response.ok().setMessage("保存成功");
    }

    //发布接口
    public Response publishSvcCode(String svcNo, String type) {
        //查询敏捷服务配置
        SvcgenServiceInfo svcgenServiceInfo = svcgenServiceInfoDao.querySvcGenInfoBySvcCode(svcNo);
        //根据配置内容，向指定文件添加接口配置flow
        if ("ADD".equals(type)) {
            if (svcgenServiceInfo != null) {
                if ("1".equals(svcgenServiceInfo.getPblStatus())) {
                    return Response.error("发布失败！").setMessage("服务已发布，无需重新操作！");
                } else if (this.appendSvcContent(svcgenServiceInfo)) {
                    svcgenServiceInfoDao.updatePublishStatus(svcgenServiceInfo.getSvcCode(), "1");
                    return Response.ok().setData(svcgenServiceInfo).setMessage("发布成功！");
                }
            } else {
                return Response.error("发布失败！").setMessage("发布失败，请检查接口配置！");
            }
        } else if ("DEL".equals(type)) {
            if (svcgenServiceInfo != null) {
                if ("0".equals(svcgenServiceInfo.getPblStatus())) {
                    return Response.error("撤销失败！").setMessage("服务已撤销，无需重新操作！");
                } else if (this.removeSvcContent(svcgenServiceInfo)) {
                    svcgenServiceInfoDao.updatePublishStatus(svcgenServiceInfo.getSvcCode(), "0");
                    return Response.ok().setData(svcgenServiceInfo).setMessage("撤销成功！");
                }
            } else {
                return Response.error("撤销失败！").setMessage("撤销失败，请检查接口配置！");
            }
        }
        return Response.ok().setMessage("更新成功！");
    }

    //追加代码
    private boolean appendSvcContent(SvcgenServiceInfo svcgenServiceInfo) {
        System.out.println(svcgenServiceInfo.toString());
        //获取项目名称
        String proId = svcgenServiceInfo.getProjId();
        String svcType = svcgenServiceInfo.getSvcType();
        //根据项目名称，获取项目目录
        DSGCSysProfileDir proInfo = proDirManagerDao.getSysProfileDirByProId(proId);
        String proName = proInfo.getProjName();
        System.out.println(proInfo.toString());
        //获取git连接信息
        SvcgenConnBean svcgenConnBean = svcGenConnDao.querySvcGenConnectById(proInfo.getConnId());
        String gitUsername = svcgenConnBean.getAttr4();
        String gitPwd = svcgenConnBean.getAttr5();
        //获取本地仓库地址
        String repoHome = fndPropertiesService.getLocalHome("repo");
        //配置内容
        String configInfo = svcgenServiceInfo.getSvcInfo();
        if ("REST".equals(svcType)) {
            RestServiceConfigDTO restConfig = JSONObject.toJavaObject(JSON.parseObject(configInfo), RestServiceConfigDTO.class);
            MuleXmlConfUtils.appendRestProjectCode(repoHome, proName, restConfig);
            serviceConfigDao.commitAndPushCode(gitUsername, gitPwd, repoHome + "/" + proName, "提交REST接口[" + svcgenServiceInfo.getSvcCode() + "]源代码");
        } else if ("DB".equals(svcType)) {
            TmplConfigBean tcb = JSONObject.toJavaObject(JSON.parseObject(configInfo), TmplConfigBean.class);
            SvcgenConnBean connBean = dbSvcCfgDao.queryDBConnList("DB", tcb.getDbConn());
            MuleXmlConfUtils.appendDBProjectCode(repoHome, proName, connBean, tcb);
            serviceConfigDao.commitAndPushCode(gitUsername, gitPwd, repoHome + "/" + proName, "提交REST接口[" + svcgenServiceInfo.getSvcCode() + "]源代码");
        } else if ("SOAP".equals(svcType)) {
            SoapServiceConfigDTO soapCfg = JSONObject.toJavaObject(JSON.parseObject(configInfo), SoapServiceConfigDTO.class);
            MuleXmlConfUtils.appendProjectCode(repoHome, proName, svcType, soapCfg);
            serviceConfigDao.commitAndPushCode(gitUsername, gitPwd, repoHome + "/" + proName, "提交SOAP接口[" + svcgenServiceInfo.getSvcCode() + "]源代码");
        }
        return true;
    }

    //删除代码
    private boolean removeSvcContent(SvcgenServiceInfo svcgenServiceInfo) {
        System.out.println("撤销代码->" + svcgenServiceInfo.toString());
        //获取项目名称
        String proName = svcgenServiceInfo.getProjName();
        String svcType = svcgenServiceInfo.getSvcType();
        //根据项目名称，获取项目目录
        DSGCSysProfileDir proInfo = proDirManagerDao.getProjectInfoByProName(proName);
        System.out.println(proInfo.toString());
        //获取git连接信息
        SvcgenConnBean svcgenConnBean = svcGenConnDao.querySvcGenConnectById(proInfo.getConnId());
        String gitUsername = svcgenConnBean.getAttr4();
        String gitPwd = svcgenConnBean.getAttr5();
        //获取本地仓库地址
        String repoHome = fndPropertiesService.getLocalHome("repo");
        //删除代码并提交
        MuleXmlConfUtils.removeProjectCode(repoHome, proName, svcType, svcgenServiceInfo.getSvcCode());
        serviceConfigDao.commitAndPushCode(gitUsername, gitPwd, repoHome + "/" + proName, "删除" + svcType + "接口[" + svcgenServiceInfo.getSvcCode() + "]源代码");
        return true;
    }

    public SvcgenServiceInfo querySvcGenFirstInfo(SvcgenServiceInfo svcgen) {
        return svcgenServiceInfoDao.querySvcGenFirstInfo(svcgen);
    }

    public SvcgenServiceInfo querySvcGenInfoByCode(String svcCode) {
        return svcgenServiceInfoDao.querySvcGenInfoBySvcCode(svcCode);
    }

    public Response saveDBService(TmplConfigBean tcb, String userName) {
        // 获取配置项目目录
        String projectName = tcb.getProjName();
        //查询数据库连接信息

        // 检查服务编号是否存在
        DSGCService service = dsgcServiceDao.findServiceByServNo(tcb.getServNo());
        if (service == null) {
            // 新增服务资产
            DSGCServiceVO dsgcServiceVO = new DSGCServiceVO();
            dsgcServiceVO.setServNo(tcb.getServNo());
            dsgcServiceVO.setServName(tcb.getServNameCode());
            dsgcServiceVO.setServDesc(tcb.getServDesc());
            dsgcServiceVO.setBizAddr(tcb.getRestPSUri());
            dsgcServiceVO.setSubordinateSystem(tcb.getAppCode());
            //设置服务地址
            DSGCServicesUri dsgcServicesUri = new DSGCServicesUri();
            dsgcServicesUri.setServNo(tcb.getServNo());
            dsgcServicesUri.setIbUri(tcb.getRestPSUri());
            dsgcServicesUri.setHttpMethod("POST");
            dsgcServicesUri.setUriType("REST");
            ArrayList<DSGCServicesUri> servicesUris = new ArrayList<>();
            servicesUris.add(dsgcServicesUri);
            dsgcServiceVO.setServicesUris(servicesUris);
            dsgcServiceService.insertDsgcService(dsgcServiceVO);
        } else {
            return Response.error("服务编号已存在！");
        }
        // 新增快速配置接口信息
        SvcgenServiceInfo svcgenServiceInfo = new SvcgenServiceInfo();
        svcgenServiceInfo.setSvcCode(tcb.getServNo());
        svcgenServiceInfo.setSvcName(tcb.getServNameCode());
        svcgenServiceInfo.setProjId(projectName);
        svcgenServiceInfo.setCreatedBy(userName);
        svcgenServiceInfo.setSysCode(tcb.getToSystem());
        svcgenServiceInfo.setSvcType("DB");
        // 存服务详细信息
        String svcDetail = JSONObject.toJSONString(tcb);
        svcgenServiceInfo.setTextAttribute1(svcDetail);
        svcgenServiceInfoDao.insertSvcServInfo(svcgenServiceInfo);
        return Response.ok().setMessage("接口配置保存成功");
    }

}

