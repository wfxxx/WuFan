package com.definesys.dsgc.service.ystar.svcgen.dpl;

import com.definesys.dsgc.service.esbenv.DSGCBusCfgDao;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesDao;
import com.definesys.dsgc.service.ystar.file.FileService;
import com.definesys.dsgc.service.ystar.file.bean.ScpConnectEntity;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.dsgc.service.ystar.svcgen.proj.DSGCProjDirManagerDao;
import com.definesys.dsgc.service.ystar.svcgen.proj.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.svcgen.conn.SvcGenConnDao;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLogVO;
import com.definesys.dsgc.service.ystar.utils.FileUtils;
import com.definesys.dsgc.service.ystar.utils.JGitUtils;
import com.definesys.dsgc.service.ystar.utils.MavenUtils;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLog;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.SvcGenDeployLogDao;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 项目编译部署服务类
 * @Author：ystar
 * @Date : 2021/5/21 15:18
 */
@Service
public class ProjectDeployService {

    @Autowired
    private FndPropertiesDao fndPropertiesDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;

    @Autowired
    private SvcGenDeployLogDao svcgenDeployLogDao;

    @Autowired
    private DSGCProjDirManagerDao projDirManagerDao;

    @Autowired
    private ProjectDeployDao projectDeployDao;
    @Autowired
    private SvcGenConnDao svcGenConnDao;

    @Autowired
    private FileService fileService;

    /**
     * @Description: 查询部署记录
     * @author: ystar
     * @param: [service, pageIndex, pageSize]
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response queryDeployLog() {
        List<SvcGenDeployLog> dplLogs = svcgenDeployLogDao.queryDeployLog(null, null);
        return Response.ok().data(dplLogs);
    }

    public Response pageQueryDeployLog(SvcGenDeployLog service, int pageSize, int pageIndex) {
        PageQueryResult<SvcGenDeployLogVO> pageQueryResult = svcgenDeployLogDao.pageQueryDeployLog(service, pageSize, pageIndex);
        return Response.ok().data(pageQueryResult);
    }

    /**
     * Maven打包
     *
     * @return
     */
    public Response packageMuleProject(String proId) {
        //maven安装目录
        String mavenHome = fndPropertiesService.getLocalHome("maven");
        // 本地仓库地址
        String repoHome = fndPropertiesService.getLocalHome("repo");
        //根据proId查询projectName
        String projectName = projDirManagerDao.getSysProfileDirByProId(proId).getProjName();
        //项目地址
        String proAddress = repoHome + "/" + projectName;
        // 打包
        boolean b = MavenUtils.mvnCleanPackage(proAddress, mavenHome);
        if (b) {
            //更新状态字段
            this.projDirManagerDao.updProjectPackStatus(proId, "1");
            return Response.ok().setMessage("编译打包成功!").data("/git-repo/" + projectName + "/target/" + projectName + ".war");
        }
        return Response.error("打包失败");
    }

    /**
     * 部署或回退版本
     *
     * @param deployLog
     * @return
     */
    public Response deployOrFallBackProject(SvcGenDeployLog deployLog) {
        String proId = deployLog.getProjId();
        String connIdList = deployLog.getConnIdList();
        if (StringUtil.isBlank(proId) || StringUtil.isBlank(connIdList)) {
            return Response.error("部署失败！").setMessage("项目ID不存在或部署目标服务器ID不准确！");
        }
        DSGCSysProfileDir profileDir = projDirManagerDao.getSysProfileDirByProId(proId);
        if (profileDir != null) {
            String projectName = profileDir.getProjName();
            String initStatus = profileDir.getInitStatus();
            if (!"Y".equals(initStatus)) {
                return Response.error("部署失败！").setMessage("请先初始化项目！");
            }
            try {
                String fileName = projectName + ".war";
                // mule安装目录
                String muleHome = fndPropertiesService.getLocalHome("mule");
                String dsgcHome = fndPropertiesService.getLocalHome("dsgc");
                // 本地仓库地址
                String repoHome = fndPropertiesService.getLocalHome("repo");
                //备份目录
                String bakFilePath = dsgcHome + "/bak";
                String version = deployLog.getvId();
                String dplMsg = deployLog.getDplMsg();
                String envCode = deployLog.getEnvCode();
                String userId = deployLog.getUserCode();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String timestamp = dateFormat.format(date);
                SvcGenDeployLog svcgenDeployLog = new SvcGenDeployLog();

                String warPath = repoHome + "/" + projectName + "/target/" + fileName;
                if (version == null) {//部署
                    version = projectName + timestamp + ".war";
                    //备份至本地
                    System.out.println("warPath->" + warPath + "\nbakFilePath->" + bakFilePath + "/" + version);
                    FileUtils.copyFile(new File(warPath), new File(bakFilePath + "/" + version));
                    // 保存部署日志,和备份版本号
                    svcgenDeployLog.setProjId(proId);
                    svcgenDeployLog.setEnvCode(envCode);
                    svcgenDeployLog.setDplMsg(dplMsg);
                    svcgenDeployLog.setDplStatus("N");
                    svcgenDeployLog.setUserCode(userId);
                    svcgenDeployLog.setConnIdList(connIdList);
                    svcgenDeployLog.setvId(version); // 备份的war包版本号
                    svcgenDeployLogDao.insertDeployLog(svcgenDeployLog);
                } else {//回退
                    svcgenDeployLog = svcgenDeployLogDao.updateDeployLogDate(proId, version, userId, dplMsg, timestamp);
                }
                //本地文件路径
                //String curFilePath = dsgcHome + "/bak";
                //上传文件至服务器
                //uploadProjectFile(envCode, fileName, muleHome, esbPort, bakFilePath + "/" + version);
                String[] connIdArr = connIdList.split(",");
                List<String> idList = new ArrayList<>();
                for (String connId : connIdArr) {
                    if (!idList.contains(connId)) {
                        //根据projId查询Conn信息
                        SvcgenConnBean connBean = this.svcGenConnDao.querySvcGenConnectById(connId);
                        if (connBean != null) {
                            System.out.println(connBean.toString());
                            //
                            String trgPath = muleHome + "/webapps";
                            fileService.uploadFile(getScpConnEntity(connBean, warPath, trgPath), fileName);
                            idList.add(connId);
                        }
                    }
                }
                //更新项目目录最新版本信息
                projDirManagerDao.updateProjectVersion(proId, version);
                return Response.ok().setMessage("部署成功！").data(svcgenDeployLog);
            } catch (Exception e) {
                e.printStackTrace();
                return Response.ok().setMessage("部署失败，该请检查环境信息配置！");
            }
        } else {
            return Response.ok().setMessage("部署失败，该项目目录找不到！");
        }
    }

    public ScpConnectEntity getScpConnEntity(SvcgenConnBean connBean, String filePath, String trgPath) {
        ScpConnectEntity scpConn = new ScpConnectEntity();
        scpConn.setTrgIp(connBean.getAttr2());
        scpConn.setUserName(connBean.getAttr4());
        scpConn.setPassWord(connBean.getAttr5());
        scpConn.setFilePath(filePath);
        scpConn.setTrgPath(trgPath);
        System.out.println(scpConn.toString());
        return scpConn;
    }


    public SvcGenDeployLog queryLastDeployLog(String proId) {
        DSGCSysProfileDir sysProfileDir = projDirManagerDao.getSysProfileDirByProId(proId);
        SvcGenDeployLog log = projectDeployDao.queryDeployLog(proId, sysProfileDir.getTextAttribute1());
        return log;
    }


    public Response initProject(String proId) {
        DSGCSysProfileDir profileDir = projDirManagerDao.getSysProfileDirByProId(proId);
        SvcgenConnBean svcgenConnBean = svcGenConnDao.querySvcGenConnectById(profileDir.getConnId());
        // 本地仓库地址
        String repoHome = fndPropertiesService.getLocalHome("repo");
        //git访问用户
        String gitUsername1 = svcgenConnBean.getAttr4();
        //git用户密码
        String gitPassword1 = svcgenConnBean.getAttr5();
        boolean initFlag = true;
        String projectName = profileDir.getProjName();
        String gitRepoUri = profileDir.getRepoUri();
        String branchName = profileDir.getBranchName();
        String proType = profileDir.getProjType();
        String projectPort = profileDir.getProjPort();
        String curProjectPath = repoHome + File.separator + projectName;
        //1-克隆远程空仓库至本地
        JGitUtils.clone(gitRepoUri, branchName, gitUsername1, gitPassword1, curProjectPath);
        //2-添加项目初始化配置文件
        Response res = this.projectDeployDao.initCfgFile(curProjectPath, projectName, proType, projectPort);
        if (!"ok".equals(res.getCode())) {
            return res;
        }
        //3-提交代码
        initFlag = JGitUtils.doCommitAndPush(curProjectPath, "first commit", gitUsername1, gitPassword1);
        if (!initFlag) {
            return Response.error("初始化代码提交失败！");
        }
        //4-更新初始化状态
        this.projectDeployDao.updProfileDirInitStatusByProId(proId, "Y");
        return Response.ok().data("初始化成功");
    }


    public List<DSGCSysProfileDir> getSvcGenProjectList(DSGCSysProfileDir project) {
        return this.projectDeployDao.getSvcGenProjectList(project);
    }

    public DSGCSysProfileDir queryProjectFirstInfo(DSGCSysProfileDir project) {
        return projectDeployDao.queryProjectFirstInfo(project);
    }

}

