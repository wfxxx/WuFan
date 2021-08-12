package com.definesys.dsgc.service.ystar.mg.util;

import com.definesys.dsgc.service.utils.StringUtils;

public class QueryUtil {

    /**
     * 获取SQL IN 条件
     *
     * @param code
     * @return
     */
    public static String getSqlWhereClause(String code) {
        String res = "";
        if (StringUtils.isNotEmpty(code) && code.contains(",")) {
            String[] strArr = code.split(",");
            for (String s : strArr) {
                res += "'" + s + "',";
            }
            if (res.contains(",")) {
                res = res.substring(0, res.length() - 1);
            }
        } else {
            res = "'" + code + "'";
        }
        return res;
    }
}
