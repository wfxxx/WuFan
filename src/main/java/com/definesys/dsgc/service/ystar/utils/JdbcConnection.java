package com.definesys.dsgc.service.ystar.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
    public static Connection getDSGCDBConnectTest(String dbType, String url, String username, String password) throws ClassNotFoundException, SQLException {
        if ("oracle".equals(dbType)) {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(3);
            return DriverManager.getConnection(url, username, password);
        } else if ("mysql".equals(dbType)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.setLoginTimeout(3);
            return DriverManager.getConnection(url, username, password);
        } else if ("sqlserver".equals(dbType)) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.setLoginTimeout(3);
            return DriverManager.getConnection(url, username, password);
        } else if ("hive".equals(dbType)) {
            Class.forName("com.cloudera.hive.jdbc41.HS2Driver");
            DriverManager.setLoginTimeout(30);
            return DriverManager.getConnection(url, username, password);
        }
        return null;
    }
}
