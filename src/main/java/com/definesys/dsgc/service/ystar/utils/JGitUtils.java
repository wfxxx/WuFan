package com.definesys.dsgc.service.ystar.utils;

import com.definesys.dsgc.service.utils.FileCopyUtil;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: JGitUtils
 * @Description: git操作工具类
 * @Author：afan
 * @Date : 2020/1/7 16:15
 */
public class JGitUtils {
    private List<String> filelist = new ArrayList<>();

    /**
     * @param gitRepoUri
     * @param branchName
     * @param gitUsername
     * @param gitPassword
     * @param localRepoPath
     * @Description: clone远程仓库
     * @author: ystar
     * @return: java.lang.Boolean
     */
    public static boolean clone(String gitRepoUri, String branchName, String gitUsername, String gitPassword, String localRepoPath) {
        System.out.println("uri->" + gitRepoUri + " branchName->" + branchName + " gitUsername->"
                + gitUsername + " gitPassword->" + gitPassword + " localRepoPath->" + localRepoPath);

        boolean status = false;
        try {
            // 如果本地目录存在先删除
            File file = new File(localRepoPath);
            if (file.exists()) {
                FileCopyUtil.deleteDirectory(localRepoPath);
            }
            CloneCommand clone = Git.cloneRepository()
                    .setURI(gitRepoUri)
                    .setDirectory(file)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUsername,
                            gitPassword))
                    .setBranch(branchName);
            Git git = clone.call();
            git.close();
            status = true;
        } catch (GitAPIException e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

    /**
     * @Description: 代码提交和远程推送
     * @author: afan
     * @param: []
     * @return: void
     */
    public static boolean doCommitAndPush(String repoPath, String message, String user, String password) {
        System.out.println("repoPath->" + repoPath + " message->" + message + " user->" + user + " pwd->" + password);
        String projectURL = repoPath;
        try {
            Git git = Git.open(new File(projectURL));
            AddCommand add = git.add();
            add.addFilepattern(".").call();//git add .
            CommitCommand commit = git.commit();
            commit.setMessage(message).call();//提交
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(user,
                    password)).call();//推送;
            git.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /****************** 新增 ********/


}

