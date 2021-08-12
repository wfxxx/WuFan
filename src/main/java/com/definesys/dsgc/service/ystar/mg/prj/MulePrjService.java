package com.definesys.dsgc.service.ystar.mg.prj;

import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.dsgc.service.ystar.mg.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.mg.prj.bean.MulePrjInfoBean;
import com.definesys.dsgc.service.ystar.mg.util.FileUtil;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: ystar
 * @since: 2021/7/9 上午10:34
 * @history: 1.2021/7/9 created by ystar
 */
@Service("MulePrjService")
public class MulePrjService {
    @Autowired
    private MulePrjDao prjDao;
    @Autowired
    private FndPropertiesService propertiesService;

    public Response pageQueryMulePrjInfo(CommonReqBean commonReqBean, int pageIndex, int pageSize) {
        PageQueryResult<MulePrjInfoBean> result = null;
        String prjName = commonReqBean.getCon0();
        String prjDesc = commonReqBean.getCon0();
        try {
            result = prjDao.pageQueryMulePrjInfo(pageIndex, pageSize, prjName, prjDesc);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response listQueryMulePrjInfo(MulePrjInfoBean prjInfoBean) {
        List<MulePrjInfoBean> result = null;
        try {
            result = prjDao.listQueryMulePrjInfo(prjInfoBean);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response sigQueryMulePrjById(MulePrjInfoBean prjInfoBean) {
        MulePrjInfoBean result = null;
        try {
            String prjId = prjInfoBean.getPrjId();
            if (StringUtil.isBlank(prjId)) {
                return Response.error("查询失败！项目ID不存在！");
            }
            result = prjDao.sigQueryMulePrjById(prjId);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response checkMulePrjNameIsExist(String prjName) {
        MulePrjInfoBean prjInfo = prjDao.sigQueryMulePrjInfoByName(prjName);
        if (prjInfo == null) {
            return Response.ok().data(false);
        } else {
            return Response.ok().data(true);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Response saveMulePrjInfo(MulePrjInfoBean prjInfoBean) {
        String prjId = prjInfoBean.getPrjId();
        try {
            if (StringUtil.isBlank(prjId)) {
                prjDao.addMulePrjInfo(prjInfoBean);
            } else {
                prjDao.updMulePrjInfo(prjInfoBean);
            }
        } catch (Exception e) {
            return Response.error("保存失败！").data(e.getMessage());
        }
        return Response.ok().setMessage("保存成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delMulePrjInfoById(String prjId) {
        try {
            if (StringUtil.isNotBlank(prjId)) {
                prjDao.delMulePrjInfoById(prjId);
                return Response.ok().setMessage("删除成功！");
            } else {
                return Response.ok().setMessage("删除失败！项目ID不存在！");
            }
        } catch (Exception e) {
            return Response.error("发生异常").setMessage(e.getMessage());
        }

    }

    @Transactional
    public Response initMuleProject(String prjId) {
        MulePrjInfoBean prjInfoBean = this.prjDao.sigQueryMulePrjById(prjId);
        String prjName = prjInfoBean.getPrjName();
        String prjPort = prjInfoBean.getPrjPort();
        String repoHome = propertiesService.getLocalHome("repo");
        boolean initFlag = true;
        if (StringUtil.isBlank(repoHome)) {
            return Response.error("本地配置路径为空！").data(false);
        }
        String prjPath = repoHome + File.separator + prjName + File.separator;
        String cfgPath = prjPath;
        //添加项目初始化配置文件
        //mule-host.properties
        StringBuilder deploySb = new StringBuilder("##############################################\n######### cmn-cfg #########\n##############################################\n");
        //todo pom.xml
        String pom = FileUtil.getMuleTemplateFileContent("xml", "pom");
        pom = pom.replaceAll("#prjName#", prjName);
        initFlag = FileUtil.createFile(cfgPath + "pom.xml", pom);
        if (!initFlag) {
            return Response.error("[pom.xml]文件初始化失败！").data(false);
        }
        //todo mule-project.xml
        String prj = FileUtil.getMuleTemplateFileContent("xml", "mule-project");
        prj = prj.replaceAll("#prjName#", prjName);
        initFlag = FileUtil.createFile(cfgPath + "mule-project.xml", prj);
        if (!initFlag) {
            return Response.error("[mule-project.xml]文件初始化失败！");
        }
        // /src/main/app/
        cfgPath = prjPath + "src" + File.separator + "main" + File.separator + "app" + File.separator;
        //todo cmn-cfg.xml
        String cmn = FileUtil.getMuleTemplateFileContent("xml", "cmn-cfg");
        cmn = cmn.replaceAll("#prjName#", prjName);
        deploySb.append("cmn.http.listener.proj.port=").append(prjPort).append("\n\n");
        initFlag = FileUtil.createFile(cfgPath + "cmn-cfg.xml", cmn);
        if (!initFlag) {
            return Response.error("[cmn-cfg.xml]文件初始化失败！");
        }
        //todo cxf-cfg.xml
        String cxf = FileUtil.getMuleTemplateFileContent("xml", "cxf-cfg");
        cxf = cxf.replaceAll("#prjName#", prjName);
        initFlag = FileUtil.createFile(cfgPath + "cxf-cfg.xml", cxf);
        if (!initFlag) {
            return Response.error("[cxf-cfg.xml]文件初始化失败！");
        }
        //todo db-cfg.xml
        String db = FileUtil.getMuleTemplateFileContent("xml", "db-cfg");
        initFlag = FileUtil.createFile(cfgPath + "db-cfg.xml", db);
        if (!initFlag) {
            return Response.error("[db-cfg.xml]文件初始化失败！");
        }
        //todo http-req-cfg.xml
        String http = FileUtil.getMuleTemplateFileContent("xml", "http-req-cfg");
        initFlag = FileUtil.createFile(cfgPath + "http-req-cfg.xml", http);
        if (!initFlag) {
            return Response.error("[http-req-cfg.xml]文件初始化失败！");
        }
        //todo ws-cs-cfg.xml
        String ws = FileUtil.getMuleTemplateFileContent("xml", "ws-cs-cfg");
        initFlag = FileUtil.createFile(cfgPath + "ws-cs-cfg.xml", ws);
        if (!initFlag) {
            return Response.error("[ws-cs-cfg.xml]文件初始化失败！");
        }
        //todo db-flow.xml
        String dbFlow = FileUtil.getMuleTemplateFileContent("xml", "db-flow");
        initFlag = FileUtil.createFile(cfgPath + "db-flow.xml", dbFlow);
        if (!initFlag) {
            return Response.error("[db-flow.xml]文件初始化失败！");
        }
        //todo rest-flow.xml
        String restFlow = FileUtil.getMuleTemplateFileContent("xml", "rest-flow");
        initFlag = FileUtil.createFile(cfgPath + "rest-flow.xml", restFlow);
        if (!initFlag) {
            return Response.error("[rest-flow.xml]文件初始化失败！");
        }
        //todo soap-flow.xml
        String soapFlow = FileUtil.getMuleTemplateFileContent("xml", "soap-flow");
        initFlag = FileUtil.createFile(cfgPath + "soap-flow.xml", soapFlow);
        if (!initFlag) {
            return Response.error("[soap-flow.xml]文件初始化失败！");
        }
        // mule-app.properties
        initFlag = FileUtil.createFile(cfgPath + "mule-app.properties", "");
        if (!initFlag) {
            return Response.error("[mule-app.properties]文件初始化失败！");
        }
        //mule-host.properties
        initFlag = FileUtil.createFile(cfgPath + "mule-host.properties", deploySb.toString());
        if (!initFlag) {
            return Response.error("[mule-host.properties]文件初始化失败！");
        }
        //mule-deploy.properties
        String deploy = FileUtil.getMuleTemplateFileContent("properties", "mule-deploy");
        initFlag = FileUtil.createFile(cfgPath + "mule-deploy.properties", deploy);
        if (!initFlag) {
            return Response.error("[mule-deploy.properties]文件初始化失败！");
        }

        // /src/main
        //todo  web.xml
        String web = FileUtil.getMuleTemplateFileContent("xml", "web");
        web = web.replaceAll("#prjName#", prjName);
        cfgPath = prjPath + "src" + File.separator + "main" + File.separator;
        initFlag = FileUtil.createFile(cfgPath + "webapp" + File.separator + "WEB-INF" + File.separator + "web.xml", web);
        if (!initFlag) {
            return Response.error("[web.xml]文件初始化失败！");
        }
        //其余文件夹
        FileUtil.createDirectory(cfgPath + "webapp" + File.separator + "META-INF");
        FileUtil.createDirectory(cfgPath + File.separator + "wsdl");
        FileUtil.createDirectory(cfgPath + File.separator + "api");
        FileUtil.createDirectory(cfgPath + File.separator + "java");
        cfgPath = prjPath + File.separator + "src" + File.separator + "test" + File.separator;
        FileUtil.createDirectory(cfgPath + File.separator + "java");
        FileUtil.createDirectory(cfgPath + File.separator + "munit");

        //更新项目状态为已初始化
        this.prjDao.updMulePrjStatus(prjId, "1");
        return Response.ok().setMessage("项目代码初始化成功！");
    }
}
