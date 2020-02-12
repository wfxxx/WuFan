package com.definesys.dsgc.service.utils;

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
}