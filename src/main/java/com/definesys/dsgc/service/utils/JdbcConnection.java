package com.definesys.dsgc.service.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
    public static Connection getDSGCDBConnectTest(String dbType,String url,String username,String password ) throws ClassNotFoundException,SQLException{
        if ("oracle".equals(dbType)) {
            Connection conn = null;
                Class.forName("oracle.jdbc.driver.OracleDriver");
                DriverManager.setLoginTimeout(3);
                conn = DriverManager.getConnection(url,username,password);
//            catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("getDBConnect异常，无法连接数据库");
//                return conn;
//            }
            return conn;
        } else if("mysql".equals(dbType)){
            Connection conn = null;
         //   try {
                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.setLoginTimeout(3);
                conn = DriverManager.getConnection(url,username,password);
         //   }
//            catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("getDBConnect异常，无法连接数据库");
//                return conn;
//            }
            return conn;

        }
        return null;
    }
}
