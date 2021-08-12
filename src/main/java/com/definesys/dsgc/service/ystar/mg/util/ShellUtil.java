package com.definesys.dsgc.service.ystar.mg.util;

//import ch.ethz.ssh2.Connection;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ShellUtil {

    public static boolean checkIpPortValid(String ip, int port) {
        System.out.println("checkIpPortValid->Ip:" + ip + ",Port:" + port);
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

//    public static boolean checkGitRepoValid(String gitLoginUrl, String userName, String password) {
//        try {
//            ApiUtil.exeRestService(gitLoginUrl);
//            return true;
//        } catch (RuntimeException e) {
//            System.out.println("error->" + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static boolean checkShellConnValid(String ip, String userName, String password) {
        System.out.println("checkIpPortValid->Ip:" + ip + ",userName:" + userName + ",password:" + password);
        try {
//            Connection connection = new Connection(ip);
//            connection.connect();
//            if (!connection.authenticateWithPassword(userName, password)) {
//                return false;
//            }
//            connection.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String encodeStr = "%E2%9C%93";
//        String res = URLDecoder.decode(encodeStr, "utf-8");


    }
}
