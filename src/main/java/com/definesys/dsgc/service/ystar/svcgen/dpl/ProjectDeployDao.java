package com.definesys.dsgc.service.ystar.svcgen.dpl;

import com.definesys.dsgc.service.ystar.svcgen.proj.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.ystar.constant.ProjectConstant;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLog;
import com.definesys.dsgc.service.ystar.utils.FileUtils;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ProjectDeployDao")
public class ProjectDeployDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void updProfileDirInitStatusByProId(String proId, String status) {
        this.sw.buildQuery()
                .eq("proj_Id", proId)
                .update("init_status", status)
                .doUpdate(DSGCSysProfileDir.class);
    }

    //获取部署日志
    public List<SvcGenDeployLog> queryDeployLog(String proId) {
        return this.sw.buildQuery()
                .eq("proj_Id", proId)
                .doQuery(SvcGenDeployLog.class);
    }

    public SvcGenDeployLog queryDeployLog(String proId, String version) {
        return this.sw.buildQuery()
                .eq("proj_Id", proId)
                .eq("text_attribute1", version)
                .doQueryFirst(SvcGenDeployLog.class);
    }

    public Response initCfgFile(String curProjectPath, String projectName, String proType, String projectPort) {
        boolean initFlag = true;
        try {
            // pom.xml
            initFlag = FileUtils.createFile(curProjectPath + "/pom.xml", ProjectConstant.PRO_POM.replaceAll("#projectName#", projectName));
            if (!initFlag) {
                return Response.error("[pom.xml]文件初始化失败！");
            }
            //mule-project.xml
            initFlag = FileUtils.createFile(curProjectPath + "/mule-project.xml", ProjectConstant.PRO_MULE_PROJECT.replaceAll("#projectName#", projectName));
            if (!initFlag) {
                return Response.error("[mule-project.xml]文件初始化失败！");
            }
            if ("SOAP".equals(proType)) {
                //app/[projectName].xml
                initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/" + projectName + ".xml", ProjectConstant.PRO_SOAP_APP_XML.replaceAll("#projectName#", projectName));
                if (!initFlag) {
                    return Response.error("[" + projectName + ".xml]文件初始化失败！");
                }
                //app/[global-cfg].xml
                initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/global-cfg.xml", ProjectConstant.PRO_SOAP_CFG_XML.replaceAll("#projectName#", projectName));
                if (!initFlag) {
                    return Response.error("[" + projectName + ".xml]文件初始化失败！");
                }
            } else if ("DB".equals(proType)) {

            } else {
                //app/[projectName].xml
                initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/" + projectName + ".xml", ProjectConstant.PRO_REST_APP_XML.replaceAll("#projectName#", projectName));
                if (!initFlag) {
                    return Response.error("[" + projectName + ".xml]文件初始化失败！");
                }
                //app/[global-cfg].xml
                initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/global-cfg.xml", ProjectConstant.PRO_REST_CFG_XML.replaceAll("#projectName#", projectName));
                if (!initFlag) {
                    return Response.error("[" + projectName + ".xml]文件初始化失败！");
                }
            }
            //其余文件夹
            FileUtils.createDirectory(curProjectPath + "/src/main/wsdl/");
            FileUtils.createDirectory(curProjectPath + "/src/main/api/");
            FileUtils.createDirectory(curProjectPath + "/src/main/java/");
            FileUtils.createDirectory(curProjectPath + "/src/test/java/");
            FileUtils.createDirectory(curProjectPath + "/src/test/munit/");

            //PRO_DEV_PROPERTIES
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/dev.properties", ProjectConstant.PRO_DEV_PROPERTIES.replaceAll("#projectName#", projectName).replaceAll("#projectPort#", projectPort));
            if (!initFlag) {
                return Response.error("[dev.properties]文件初始化失败！");
            }
            //PRO_PRD_PROPERTIES
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/prd.properties", ProjectConstant.PRO_PRD_PROPERTIES.replaceAll("#projectName#", projectName).replaceAll("#projectPort#", projectPort));
            if (!initFlag) {
                return Response.error("[prd.properties]文件初始化失败！");
            }
            //mule-app.properties
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/mule-app.properties", "");
            if (!initFlag) {
                return Response.error("[mule-app.properties]文件初始化失败！");
            }
            //mule-deploy.properties
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/app/mule-deploy.properties", ProjectConstant.PRO_DEPLOY_PROPERTIES.replaceAll("#projectName#", projectName));
            if (!initFlag) {
                return Response.error("[mule-deploy.properties]文件初始化失败！");
            }
            //log4j2.xml
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/resources/log4j2.xml", ProjectConstant.PRO_RESOURCE_LOG4J2.replaceAll("#projectName#", projectName));
            if (!initFlag) {
                return Response.error("[log4j2.xml]文件初始化失败！");
            }
            //log4j2-test.xml
            initFlag = FileUtils.createFile(curProjectPath + "/src/test/resources/log4j2-test.xml", ProjectConstant.PRO_RESOURCE_LOG4J2_TEST);
            if (!initFlag) {
                return Response.error("[log4j2-test.xml]文件初始化失败！");
            }
            //catalog文件夹
            initFlag = FileUtils.createFile(curProjectPath + "/catalog/types/custom/", "");
            if (!initFlag) {
                return Response.error("[catalog]文件目录初始化失败！");
            }
            //web.xml
            FileUtils.createDirectory(curProjectPath + "/src/main/webapp/META-INF");
            initFlag = FileUtils.createFile(curProjectPath + "/src/main/webapp/WEB-INF/web.xml", ProjectConstant.PRO_WEB_XML.replaceAll("#projectName#", projectName));
            if (!initFlag) {
                return Response.error("[web.cml]文件初始化失败！");
            }
        } catch (Exception e) {
            return Response.error("文件初始化失败！");
        }
        return Response.ok().data("初始化完成！");
    }

    public List<DSGCSysProfileDir> getSvcGenProjectList(DSGCSysProfileDir project) {
        return this.sw.buildQuery()
                .eq("PROJ_ID", project.getProjId())
                .eq("SYS_CODE", project.getSysCode())
                .doQuery(DSGCSysProfileDir.class);
    }

    public DSGCSysProfileDir queryProjectFirstInfo(DSGCSysProfileDir project) {
        return this.sw.buildQuery().eq("projId", project.getProjId()).doQueryFirst(DSGCSysProfileDir.class);
    }
}
