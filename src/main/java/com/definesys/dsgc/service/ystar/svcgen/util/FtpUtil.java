package com.definesys.dsgc.service.ystar.svcgen.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.nio.charset.Charset;

public class FtpUtil {


    public static Boolean checkFtpConnect(String ip, int port, String name, String pwd) {
        FTPClient ftp = new FTPClient();
        //验证登录
        try {
            ftp.setConnectTimeout(10000);
            ftp.connect(ip, port);
            return ftp.login(name, pwd);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
