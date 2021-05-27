package com.definesys.dsgc.service.ystar.svcgen.svccfg;

import com.definesys.dsgc.service.lkv.FndPropertiesDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.utils.JGitUtils;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository("ServiceConfigDao")
public class ServiceConfigDao {

    @Value("${ystar.git.repo_home}")
    private String repoHome;
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

    @Autowired
    private FndPropertiesDao fndPropertiesDao;

    /**
     * 提交代码
     * ystar
     *
     * @param message
     * @param localRepoPath
     * @param username
     * @param password
     * @return
     */
    public Response commitAndPushCode(String username, String password, String localRepoPath, String message) {
        boolean b = JGitUtils.doCommitAndPush(localRepoPath, message, username, password);
        if (b) {
            return Response.ok().setMessage("提交代码成功");
        }
        return Response.error("提交代码失败");
    }
}
