package com.definesys.dsgc.service.utils;

import java.util.regex.Pattern;

public class StringUtil
{
    public static boolean isNotBlank(String string)
    {
        if ((string != null) && (!"".equals(string.trim()))) {
            return true;
        }
        return false;
    }

    public static boolean isBlank(String string)
    {
        return !isNotBlank(string);
    }

    /**
     * 常见特殊字符过滤
     *
     * @param str
     * @return
     */
    public static String filtration(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}:;\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？']";
        str = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
        return str;
    }

}