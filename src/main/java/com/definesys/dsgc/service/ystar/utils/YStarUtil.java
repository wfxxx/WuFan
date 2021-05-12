package com.definesys.dsgc.service.ystar.utils;

import com.definesys.dsgc.service.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 辅助工具类
 */
public class YStarUtil {

    public static final String KEY_AES = "AES/ECB/PKCS5Padding";

    /*** 获取指定长度的字符串数组 ** @param type:0/1个位数前面补零 @param length * @return*/
    public static List<String> getNumArrayList(String type, int index, int length) {
        List<String> list = new ArrayList<>();
        if ("0".equals(type)) {
            for (int i = index; i < length + index; i++) {
                list.add("" + i);
            }
        } else {
            for (int i = index; i < length + index; i++) {
                if (i < 10) {
                    list.add("0" + i);
                } else {
                    list.add("" + i);
                }
            }
        }
        return list;
    }

    /*** 获取指定长度的时刻字符串数组 ** @param @param length * @return*/
    public static List<String> getTimeNumArrayList(int index, int length) {
        List<String> list = new ArrayList<>();
        for (int i = index; i < length + index; i++) {
            if (i < 10) {
                list.add("0" + i + ":00");
            } else {
                list.add(i + ":00");
            }
        }
        return list;
    }

    /**
     * 字符串转Date
     *
     * @param dateFormat
     * @param dateStr
     * @return
     */
    public static Date getDateByStr(String dateFormat, String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            if (StringUtils.isNotEmpty(dateStr)) {
                date = formatter.parse(dateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


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
