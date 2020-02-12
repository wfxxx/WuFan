package com.definesys.dsgc.service.utils;

import java.sql.*;
import java.util.Map;
import java.util.Set;

public class SqlInjectParamsUtil {

    public static final String SERV_NO = "SERV_NO";
    public static final String JR_CODE = "JR_CODE";

    public SqlInjectParamsUtil() {
        super();
    }

    /**
     * 替换sql中的占位符
     * @param sql
     * @param paramsMap 以占位符名为key，值为value
     * @return 替换以后的sql
     */
    public static String execute(String sql, Map<String, Object> paramsMap) {
        Set<String> keySet = paramsMap.keySet();
        for (String key : keySet) {
            String keyRegex = "#START#" + key + "#END#";
            sql = sql.replaceAll(keyRegex, paramsMap.get(key).toString());
        }
        return sql;
    }

    public static Connection getDBConnect(String user,String password,String url,String driver) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static String getReplacedValueBySql(Connection conn,String sql){
        System.out.println("getReplacedValueBySql  " + sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps =  conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                String res = rs.getString(  1);
                System.out.println("res  " + res);
                return res;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs !=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps !=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(conn !=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}