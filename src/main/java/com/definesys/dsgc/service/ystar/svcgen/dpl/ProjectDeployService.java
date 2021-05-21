package com.definesys.dsgc.service.ystar.svcgen.dpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.esbenv.DSGCBusCfgDao;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvMachineCfg;
import com.definesys.dsgc.service.lkv.FndPropertiesDao;
import com.definesys.dsgc.service.lkv.bean.FndProperties;
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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
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
    @Value("${ystar.git.deploy_proj_name}")
    private String deployProjName;
    @Value("${ystar.git.maven_home}")
    private String mavenHome;
    @Value("${ystar.git.branch_name}")
    private String branchName;
    @Value("${ystar.git.username}")
    private String gitUsername;
    @Value("${ystar.git.password}")
    private String gitPassword;
    @Value("${ystar.esb.mule_home}")
    private String muleHome;
    @Value("${ystar.esb.dsgc_home}")
    private String dsgcHome;
    @Value("${ystar.git.repo_home}")
    private String repoHome;
    @Value("${ystar.esb.esb_port}")
    private String esbPort;

    @Autowired
    private FndPropertiesDao fndPropertiesDao;

    @Autowired
    private DSGCBusCfgDao dsgcBusCfgDao;

    @Autowired
    private SvcGenDeployLogDao svcgenDeployLogDao;

    @Autowired
    private DSGCProjDirManagerDao projDirManagerDao;

    @Autowired
    private ProjectDeployDao projectDeployDao;
    @Autowired
    private SvcGenConnDao svcGenConnDao;

    /**
     * @Description: 从远程仓库拉取代码
     * @author: afan
     * @param: []
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response cloneProject(Map<String, String> map) {
        //git远程仓库地址
        String gitRepoUri = map.get("gitUrl");
        String projectName = map.get("projectName");
        //git远程仓库分支,分支名从tag中获取
        branchName = StringUtil.isNotBlank(branchName) ? branchName : fndPropertiesDao.findFndPropertiesByKey("GIT_BRANCH_NAME").getPropertyValue();
        //String branchName = map.get("TAG");
        //git访问用户
        gitUsername = StringUtil.isNotBlank(gitUsername) ? gitUsername : fndPropertiesDao.findFndPropertiesByKey("GIT_USERNAME").getPropertyValue();
        //git用户密码
        gitPassword = StringUtil.isNotBlank(gitPassword) ? gitPassword : fndPropertiesDao.findFndPropertiesByKey("GIT_PASSWORD").getPropertyValue();
        // 本地仓库地址
        repoHome = StringUtil.isNotBlank(repoHome) ? repoHome : fndPropertiesDao.findFndPropertiesByKey("LOCAL_REPO_PATH").getPropertyValue();
        //获取当前环境
        String curEnv = fndPropertiesDao.findFndPropertiesByKey("DSGC_CURRENT_ENV").getPropertyValue();

        // clone代码
        boolean isCloned = JGitUtils.clone(gitRepoUri, branchName, gitUsername, gitPassword,
                repoHome + "/" + projectName);
        if (isCloned) {
            return Response.ok().setMessage("同步远程仓库代码成功").setData(curEnv);
        }
        return Response.error("同步代码失败");
    }

    /**
     * @Description: 代码提交和远程推送
     * @author: afan
     * @param: []
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response doCommitAndPush(String message) {
        //git访问用户
        gitUsername = getFndCfgPath("GIT_USERNAME");//StringUtil.isNotBlank(gitUsername) ? gitUsername : fndPropertiesDao.findFndPropertiesByKey("GIT_USERNAME").getPropertyValue();
        //git用户密码
        gitPassword = getFndCfgPath("GIT_PASSWORD");//StringUtil.isNotBlank(gitPassword) ? gitPassword : fndPropertiesDao.findFndPropertiesByKey("GIT_PASSWORD").getPropertyValue();
        // 本地仓库地址
        repoHome = getFndCfgPath("dsgc") + "/git-repo";//StringUtil.isNotBlank(repoHome) ? repoHome : fndPropertiesDao.findFndPropertiesByKey("REPO_HOME").getPropertyValue();
        boolean b = JGitUtils.doCommitAndPush(repoHome, message, gitUsername, gitPassword);
        if (b) {
            return Response.ok().setMessage("提交代码成功");
        }
        return Response.error("提交代码失败");
    }

    /**
     * @Description: 打包项目
     * @author: afan
     * @param: []
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response packageProject() {
        //maven安装目录
        mavenHome = getFndCfgPath("maven");
        // 本地仓库地址
        repoHome = getFndCfgPath("dsgc") + "/git-repo";
        // 打包
        boolean b = MavenUtils.mvnCleanPackage(repoHome, mavenHome);
        if (b) {
            return Response.ok().setMessage("编译打包成功!");
        }
        return Response.error("打包失败");
    }

    /**
     * @param message
     * @param userName
     * @Description: 部署项目
     * @author: afan
     * @param: []
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response deployProject(String message, String userName, String curEnv) {
        if (curEnv == null || "".equals(curEnv)) {
            return Response.error("请选择环境");
        }
        // 获取部署项目名
        deployProjName = StringUtil.isNotBlank(deployProjName) ? deployProjName : fndPropertiesDao.findFndPropertiesByKey("DEPLOY_PROJ_NAME")
                .getPropertyValue();
        // 本地仓库地址
        dsgcHome = getFndCfgPath("dsgc");
        repoHome = dsgcHome + "/git-repo";//StringUtil.isNotBlank(repoHome) ? repoHome : fndPropertiesDao.findFndPropertiesByKey("REPO_HOME").getPropertyValue();
        //curEnv = fndPropertiesDao.findFndPropertiesByKey("DSGC_CURRENT_ENV").getPropertyValue();
        String warPath = repoHome + "/target/" + deployProjName + ".war";
        File projectFile = new File(repoHome + "/target/");
        File file = new File(repoHome + "/target/");
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.getName().endsWith(".war")) {
                projectFile = file1;
            }

        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(date);
        List<String> results = new ArrayList<>();
        // 根据当前环境获取集群的节点信息
        List<DSGCEnvMachineCfg> envMachineCfgByEnvCode = dsgcBusCfgDao.findEnvMachineCfgByEnvCode(curEnv);
        if (envMachineCfgByEnvCode.size() > 0) {
            for (DSGCEnvMachineCfg dsgcEnvMachineCfg : envMachineCfgByEnvCode) {
                String machineName = dsgcEnvMachineCfg.getMachineName();
                String machineIp = dsgcEnvMachineCfg.getMachineIp();
                String machinePort = dsgcEnvMachineCfg.getMachinePort();
                System.out.println(machineIp);
                String url = "http://" + machineIp + ":" + machinePort + "/muletools/deployProject?timestamp=" + timestamp;
                String rtnBody = sendFile(url, projectFile, null);
                JSONObject jsonObject = JSON.parseObject(rtnBody);
                String result;
                String code = jsonObject.getString("code");
                if ("S".equals(code)) {
                    result = "部署成功";
                } else {
                    result = "部署失败，请手动操作";
                }
                results.add(machineName + ":" + result);
            }

            // 保存部署日志,和备份版本号
            SvcGenDeployLog svcgenDeployLog = new SvcGenDeployLog();
            svcgenDeployLog.setDplMsg(message);
            svcgenDeployLog.setDplStatus("N");
            svcgenDeployLog.setUserCode(userName);
            svcgenDeployLog.setvId(deployProjName + ".war" + timestamp); // 备份的war包版本号
            svcgenDeployLogDao.insertDeployLog(svcgenDeployLog);


            String s = JSONObject.toJSONString(results);
            return Response.ok().setMessage(s);
        } else {
            return Response.error("找不到节点服务器");
        }
    }

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
     * @Description: 回退版本
     * @author: afan
     * @param: [vId]
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response revokeProj(String vId) {
        String curEnv = fndPropertiesDao.findFndPropertiesByKey("DSGC_CURRENT_ENV").getPropertyValue();
        // 根据当前环境获取集群的节点信息
        List<String> results = new ArrayList<>();
        List<DSGCEnvMachineCfg> envMachineCfgByEnvCode = dsgcBusCfgDao.findEnvMachineCfgByEnvCode(curEnv);
        for (DSGCEnvMachineCfg dsgcEnvMachineCfg : envMachineCfgByEnvCode) {
            String machineName = dsgcEnvMachineCfg.getMachineName();
            String machineIp = dsgcEnvMachineCfg.getMachineIp();
            String machinePort = dsgcEnvMachineCfg.getMachinePort();
            String url = "http://" + machineIp + ":" + machinePort + "/muletools/revokeProject?vId=" + vId;
            String rtnBody = sendFile(url, null, null);
            JSONObject jsonObject = JSON.parseObject(rtnBody);
            String result = "";
            String code = jsonObject.getString("code");
            if ("S".equals(code)) {
                result = "回退成功";
            } else {
                result = "回退失败，请手动操作";
            }
            results.add(machineName + ":" + result);
        }

        svcgenDeployLogDao.updateDeployLog(vId);
        String s = JSONObject.toJSONString(results);
        return Response.ok().setMessage(s);
    }

    /**
     * @Description: 部署项目到指定服务器
     * @author: afan
     * @param: [servers]
     * @return: com.definesys.mpaas.common.http.Response
     */
    public Response syncProject(List<Map<String, String>> servers) {

        // 获取当本war版本部署路径
        String muleDeployDir = fndPropertiesDao.findFndPropertiesByKey("MULE_DEPLOY_DIR")
                .getPropertyValue();
        // 获取部署项目名
        String deployProjName = fndPropertiesDao.findFndPropertiesByKey("DEPLOY_PROJ_NAME")
                .getPropertyValue();
        String warFilePath = muleDeployDir + deployProjName + ".war";
        File file = new File(warFilePath);
        List<Map<String, String>> rtmMsg = new ArrayList<>();
        Map<String, String> map = null;
        for (Map<String, String> server : servers) {
            map = new HashMap<String, String>();
            String sName = server.get("serverName");
            String url = server.get("url");
            String rtnBody = this.sendFile(url, file, null);
            JSONObject jsonObject = JSON.parseObject(rtnBody);
            String code = jsonObject.getString("code");
            if ("OK".equals(code) && code != null) {
                map.put("msg", "部署成功");
            } else {
                map.put("msg", "部署失败");
            }
            rtmMsg.add(map);
        }
        return Response.ok().data(rtmMsg);
    }

    /**
     * @Description: 接收要部署的项目文件
     * @author: afan
     * @param: [request]
     * @return: Response
     */
    public Response receiveProj(HttpServletRequest request) {
        // 获取当本war版本部署路径
        String muleDeployDir = fndPropertiesDao.findFndPropertiesByKey("MULE_DEPLOY_DIR")
                .getPropertyValue();
        // 获取部署项目名
        String deployProjName = fndPropertiesDao.findFndPropertiesByKey("DEPLOY_PROJ_NAME")
                .getPropertyValue();
        String warFilePath = muleDeployDir + deployProjName + ".war";

        // 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            // 将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 获取multiRequest 中所有的文件名
            Iterator<String> iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                // 一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if (file != null) {

                    File file1 = new File(warFilePath);
                    if (file1.exists()) {
                        file1.delete();
                    }
                    // 文件数据存储起来
                    try {
                        file.transferTo(file1);
                        return Response.ok();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Response.error("操作失败");
    }


    /*
     * @Description: http发送文件
     * @author: afan
     * @param: [xx, file]
     * @return: void
     */
    public String sendFile(String url, File file, Map<String, String> header) {
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        try {
            if (file != null) {
                // FilePart：用来上传文件的类,file即要上传的文件
                FilePart fp = new FilePart("file", file);
                Part[] parts = {fp};
                // 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
                MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
                postMethod.setRequestEntity(mre);
            }
            if (header != null) {
                for (String headerName : header.keySet()) {
                    postMethod.setRequestHeader(headerName, header.get(headerName));
                }
            }
            // 由于要上传的文件可能比较大 , 因此在此设置最大的连接超时时间
            client.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                // 获取返回数据
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                String body = stringBuffer.toString();
                return body;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 释放连接
            postMethod.releaseConnection();
        }

        return null;
    }

    /**
     * Maven打包
     *
     * @return
     */
    public Response packageMuleProject(String proId) {
        //maven安装目录
        mavenHome = getFndCfgPath("maven");//StringUtil.isNotBlank(mavenHome) ? mavenHome : fndPropertiesDao.findFndPropertiesByKey("MAVEN_HOME").getPropertyValue();
        // 本地仓库地址
        dsgcHome = getFndCfgPath("dsgc");// StringUtil.isNotBlank(dsgcHome) ? dsgcHome : fndPropertiesDao.findFndPropertiesByKey("DSGC_HOME").getPropertyValue();
        repoHome = dsgcHome + "/git-repo";
        //根据proId查询projectName
        String projectName = projDirManagerDao.getSysProfileDirByProId(proId).getProjName();
        //项目地址
        String proAddress = repoHome + "/" + projectName;
        // 打包
        boolean b = MavenUtils.mvnCleanPackage(proAddress, mavenHome);
        if (b) {
            //String warPath = proAddress + "/target/" + projectName + ".war";
            return Response.ok().setMessage("编译打包成功!").data("{repoHome}/" + projectName + "/target/" + projectName + ".war");
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
                muleHome = getFndCfgPath("mule");//StringUtil.isNotBlank(muleHome) ? muleHome : fndPropertiesDao.findFndPropertiesByKey("MULE_HOME").getPropertyValue();
                esbPort = getFndCfgPath("MULE_ESB_PORT");//StringUtil.isNotBlank(esbPort) ? esbPort : fndPropertiesDao.findFndPropertiesByKey("MULE_ESB_PORT").getPropertyValue();
                dsgcHome = getFndCfgPath("dsgc");//StringUtil.isNotBlank(dsgcHome) ? dsgcHome : fndPropertiesDao.findFndPropertiesByKey("DSGC_HOME").getPropertyValue();
                // 本地仓库地址
                repoHome = dsgcHome + "/git-repo";//StringUtil.isNotBlank(repoHome) ? repoHome : fndPropertiesDao.findFndPropertiesByKey("REPO_HOME").getPropertyValue();
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
                if (version == null) {//部署
                    String warPath = repoHome + "/" + projectName + "/target/" + fileName;
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
                    svcgenDeployLog.setvId(version); // 备份的war包版本号
                    svcgenDeployLogDao.insertDeployLog(svcgenDeployLog);
                } else {//回退
                    svcgenDeployLog = svcgenDeployLogDao.updateDeployLogDate(proId, version, userId, dplMsg, timestamp);
                }
                //本地文件路径
                //String curFilePath = dsgcHome + "/bak";
                //上传文件至服务器
                uploadProjectFile(envCode, fileName, muleHome, esbPort, bakFilePath + "/" + version);
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


    /**
     * 上传文件至服务器
     *
     * @param envCode
     * @param fileName
     * @param muleHome
     * @param curFilePath
     * @return
     */
    @Transactional
    public Response uploadProjectFile(String envCode, String fileName, String muleHome, String esbPort, String curFilePath) {
        File file = new File(curFilePath);
        if (!file.exists()) {
            return Response.error("部署失败").setMessage("文件不存在!");
        }
        List<String> results = new ArrayList<>();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("fileName", fileName);
        headerMap.put("filePath", muleHome + "/webapps");
        headerMap.put("Content-Type", "multipart/form-data");
        // 根据当前环境获取集群的节点信息
        List<DSGCEnvMachineCfg> envMachineCfgList = dsgcBusCfgDao.findEnvMachineCfgByEnvCode(envCode);
        String res;
        if (envMachineCfgList.size() > 0) {
            for (DSGCEnvMachineCfg machine : envMachineCfgList) {
                String machineName = machine.getMachineName();
                String machineIp = machine.getMachineIp();
                //String machinePort = machine.getMachinePort();
                System.out.println(machineIp);
                String url = "http://" + machineIp + ":" + esbPort + "/Test/FileDemo01";
                String rtnBody = sendFile(url, file, headerMap);//  调用远程接口 上传文件至服务器
                System.out.println("url->" + url + "\n rtnBody->" + rtnBody);
                String result;
                if (StringUtil.isBlank(rtnBody)) {
                    result = "部署失败，请手动操作";
                } else {
                    JSONObject jsonObject = JSON.parseObject(rtnBody);
                    String code = jsonObject.getString("code");
                    if ("S".equals(code)) {
                        result = "部署成功";
                    } else {
                        result = "部署失败，请手动操作";
                    }
                }
                results.add(machineName + ":" + result);
            }
            res = JSONObject.toJSONString(results);
        } else {
            return Response.error("找不到节点服务器");
        }
        return Response.ok().setMessage("部署成功！").data(res);
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
        dsgcHome = getFndCfgPath("dsgc");
        repoHome = dsgcHome + "/git-repo";
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


    public String getFndCfgPath(String type) {
        String key = type;
        switch (type) {
            case "dsgc":
                if (StringUtil.isNotBlank(dsgcHome)) {
                    return dsgcHome;
                } else {
                    key = "DSGC_HOME";
                }
                break;
            case "mule":
                if (StringUtil.isNotBlank(muleHome)) {
                    return muleHome;
                } else {
                    key = "MULE_HOME";
                }
                break;
            case "maven":
                if (StringUtil.isNotBlank(mavenHome)) {
                    return mavenHome;
                } else {
                    key = "MAVEN_HOME";
                }
                break;
            default:
                break;
        }
        FndProperties properties = fndPropertiesDao.findFndPropertiesByKey(key);
        return properties != null ? properties.getPropertyValue() : null;
    }


}

