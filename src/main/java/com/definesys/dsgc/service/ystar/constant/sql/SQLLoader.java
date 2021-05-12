package com.definesys.dsgc.service.ystar.constant.sql;

import com.definesys.dsgc.common.logger.DSGCLogger;
import com.definesys.dsgc.common.utils.DomXmlUtils;
import com.definesys.dsgc.service.ystar.constant.SqlConstant;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLLoader {

    private static Map<String, String> sqlStore = new HashMap<String, String>();

    static {
        ClassPathResource classPathResource = new ClassPathResource("sql.xml");
        String url = classPathResource.getClassLoader().getResource("sql.xml").getPath();
        System.out.println("url->" + url);
        InputStream inputStream = null;
        BufferedReader br = null;
        try {
            File f = new File(url);
            inputStream = new FileInputStream(f);
            br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = br.readLine()) != null && s.length() != 0) {
                //System.out.println("s->" + s);
                sb.append(s);
            }
            System.out.println("sb-->" + sb.toString());
            String sqlStr = sb.toString();
            if (sqlStr.trim().length() > 0) {
                Document sqlDoc = DomXmlUtils.parseToXML(sqlStr.trim());
                List<Node> nodeList = DomXmlUtils.selectNodeListByXpath(sqlDoc, new HashMap<String, String>(), "/store/sql");
                if (nodeList != null) {
                    for (Node n : nodeList) {
                        Node sqlID = n.selectSingleNode("@id");
                        if (sqlID != null && sqlID.getStringValue() != null && sqlID.getStringValue().trim().length() > 0) {
                            Node sqlNode = null;
                            if (SqlConstant.DB_TYPE_MYSQL.equals(SqlConstant.dbType)) {
                                sqlNode = n.selectSingleNode("mysql");
                            } else if (SqlConstant.DB_TYPE_ORACLE.equals(SqlConstant.dbType)) {
                                sqlNode = n.selectSingleNode("oracle");
                            }

                            if (sqlNode == null) {
                                sqlNode = n;
                            }

                            if (sqlNode.getStringValue() != null && sqlNode.getStringValue().trim().length() > 0) {
                                if (sqlStore.containsKey(sqlID.getStringValue().trim())) {
                                    DSGCLogger.warning(DSGCLogger.MODULE_DSGC_PROCESS, null, "重复的sql标别ID在sql_store中找到：" + sqlID.getStringValue());
                                }
                                sqlStore.put(sqlID.getStringValue().trim(), sqlNode.getStringValue().trim());
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String sql(String key) {
        return sqlStore.get(key);
    }

}
