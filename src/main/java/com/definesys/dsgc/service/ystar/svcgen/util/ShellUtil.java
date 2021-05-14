package com.definesys.dsgc.service.ystar.svcgen.util;

import com.definesys.dsgc.common.logger.DSGCLogger;
import com.definesys.dsgc.service.ystar.utils.JGitUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;

public class ShellUtil {

    public static boolean checkIpPortValid(String ip, int port) {
        DSGCLogger.info(DSGCLogger.DSGC_MULE_LOGGING, null, "checkIpPortValid->Ip:" + ip + ",Port:" + port);
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 500);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean checkValid(String ip, int port, String userName, String password, String remoteUrl) {
        //获取登陆凭证
        boolean res = Repository.isValidRefName(remoteUrl);
        try {
            Git git = Git.open(new File("."));
            AddCommand add = git.add();
            add.addFilepattern(".").call();//git add .
            CommitCommand commit = git.commit();
            commit.setMessage("测试").call();//提交

            PushCommand pushCommand = git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName,
                    password));
            pushCommand.setRemote(remoteUrl);
            pushCommand.call();//推送;
            git.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }


//        System.out.println("res->" + res);
        return res;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String encodeStr = "%E2%9C%93";
//        String res = URLDecoder.decode(encodeStr, "utf-8");

        checkValid("git.definesys.com", 80, "dongdong.yuan@definesys.com111", "YStarYdd089", "http://git.definesys.com/ystar/dsgcapp.git");

        //System.out.println(res);
    }
}
