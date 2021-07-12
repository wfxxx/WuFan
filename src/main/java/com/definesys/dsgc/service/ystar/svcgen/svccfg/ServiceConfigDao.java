package com.definesys.dsgc.service.ystar.svcgen.svccfg;

import com.definesys.dsgc.service.ystar.utils.JGitUtils;
import com.definesys.mpaas.common.http.Response;
import org.springframework.stereotype.Repository;

@Repository("ServiceConfigDao")
public class ServiceConfigDao {


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
