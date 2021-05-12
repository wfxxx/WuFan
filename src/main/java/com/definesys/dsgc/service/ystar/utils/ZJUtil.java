package com.definesys.dsgc.service.ystar.utils;

import com.definesys.dsgc.service.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: ystar
 * @since: 2019/06/26 上午10:14
 * @history: 1.2019/06/26 created by ystar
 */
public class ZJUtil {

    /**
     * 对字符串进行去空格操作
     *
     * @param string 字符串
     * @return String
     */
    public static String getStringAfterTrim(String string) {
        if ((string != null) && (!"".equals(string.trim()))) {
            return string.trim();
        } else {
            return null;
        }
    }

    /**
     * 校验IPV4表达式
     * 10.10.7.1-10&20-50
     * 10.10.7.*
     *
     * @param ipStr IP地址
     * @return 返回S表示成功，非S，则返回对应信息
     */
    public static String checkIPV4String(String ipStr) {
        //不为空
        if (StringUtil.isNotBlank(ipStr)) {
            String[] ips = ipStr.split("\\.");
            //IPV4长度为4个网段
            if (ips.length == 4) {
                for (int i = 0; i < ips.length; i++) {
                    String ip = ips[i];
                    boolean isPassed = true;
                    //判断是否为多值情况，如10.10.7.4|5
                    if (ip.contains("|")) {
                        //进行截取
                        String[] mutInterval = ip.split("\\|");
                        //排除只输入一个字符|的可能，10.10.7.|
                        if (mutInterval.length == 0) {
                            isPassed = false;
                        } else {
                            //遍历每个值
                            for (String s : mutInterval) {
                                //判断是否为多区间 10.10.7.1-10|20-50
                                if (s.contains("-")) {
                                    //对每个区间进行截取前后区间值，区间长度必须为2
                                    if (!checkInterVal(s, "-")) {
                                        isPassed = false;
                                        break;
                                    }
                                } else if (!"*".equals(s) && (checkStringIsNumber(s, 0, 255) != 0)) {
                                    //不为区间值，直接校验是否为数字或*
                                    isPassed = false;
                                    break;
                                }
                            }
                        }
                        //不为多值情况，判断是否为区间值
                    } else if (ip.contains("-")) {
                        //对每个区间进行截取前后区间值，区间长度必须为2
                        if (!checkInterVal(ip, "-")) {
                            isPassed = false;
                        }
                    } else if (!"*".equals(ip) && (checkStringIsNumber(ip, 0, 255) != 0)) {
                        //不为区间值，直接校验是否为数字或*
                        isPassed = false;
                    }
                    if (!isPassed) {
                        return "第" + (i + 1) + "网段不为数字或区间值不规范！";
                    }
                }
            } else {
                return "网段数为" + ips.length;
            }
        } else {
            return "IP地址为空";
        }
        return "S";
    }

    /**
     * 校验区间值：1-是否为数字；2-区间值个数为2（即不为空）；3-区间左值小于等于区间右值
     *
     * @param string
     * @param key
     * @return
     */
    private static boolean checkInterVal(String string, String key) {
        String[] interval = string.split(key);
        boolean checkedFlag;
        if (interval.length == 2) {
            String interValBegin = interval[0];
            String interValEnd = interval[1];
            if (StringUtil.isBlank(interValBegin) || StringUtil.isBlank(interValEnd)) {
                return false;
            } else if ((checkStringIsNumber(interValBegin, 1, 254) != 0)
                    || (checkStringIsNumber(interValEnd, 1, 254) != 0)) {
                return false;
            } else {
                //区间右值大于等于区间左值
                checkedFlag = Integer.parseInt(interValBegin) <= Integer.parseInt(interValEnd);
            }
        } else {
            return false;
        }
        return checkedFlag;
    }


    /**
     * 将字符串按指定关键字分割，拆分成list集合
     * （暂未用）
     *
     * @param string
     * @param keyArr
     * @return
     */
    public static List<String> splitStringByKeys(String string, String[] keyArr) {
        //先用第一个字符串分割获得集合
        String[] strArr = string.split(keyArr[0]);
        List<String> strList = new ArrayList<>(Arrays.asList(strArr));
        //再进行遍历
        for (int i = 1; i < keyArr.length; i++) {
            List<String> tempList = new ArrayList<>();
            for (int j = 0; j < strList.size(); j++) {
                String str = strList.get(j);
                String[] tempStrArr = str.split(keyArr[i]);
                tempList.addAll(Arrays.asList(tempStrArr));
            }
            strList.clear();
            strList.addAll(tempList);
        }

        for (int i = 0; i < strList.size(); i++) {
            System.out.println(i + " ->" + strList.get(i));
        }
        return strList;
    }

    /**
     * 判断String变量是否为某数字
     *
     * @param str
     * @return
     */
    public static boolean checkStringIsNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断String变量是否为某区间的数字
     *
     * @param str
     * @param beginNum 区间左值
     * @param endNum   区间右值
     * @return 0正常，-1不为数字，-2不在区间内
     */
    public static int checkStringIsNumber(String str, int beginNum, int endNum) {
        if (!checkStringIsNumber(str) || beginNum > endNum) {
            return -1;
        } else {
            int num = Integer.parseInt(str);
            if (!(num >= beginNum && num <= endNum)) {
                return -2;
            }
        }
        return 0;
    }

    /**
     * 将字符串数组转换成where条件里面的in参数
     * 20191225 报表条件查询使用
     *
     * @param strArr
     * @return 拼接单引号的字符串
     */
    public static String getSqlStringByArr(String[] strArr) {
        if (strArr != null) {
            String sql = " in (";
            for (String str : strArr) {
                sql += "'" + str + "',";
            }
            sql = sql.substring(0, sql.length() - 1) + ")";//去除最后一位逗号
            return sql;
        }
        return null;
    }

    /*** 将list数据转为 in 条件 @param strList @return */
    public static String getSqlStringByList(List<String> strList) {
        if (strList != null && strList.size() > 0) {
            StringBuilder sql = new StringBuilder(" in (");
            for (String str : strList) {
                sql.append("'").append(str).append("',");
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ")");//去除最后一位逗号
            return sql.toString();
        }
        return null;
    }

    /**
     * 字符串数组转换成
     * @param strArr
     * @return
     */
    public static List<String> getListByStringArr(String[] strArr) {
        if (strArr != null) {
            return new ArrayList<>(Arrays.asList(strArr));
        }
        return null;
    }

    /**
     * 获取格式化的日期
     *
     * @param time
     * @return
     */
    public static Date getFormatDate(String time) {
        String formatStr = "yyyy-MM-dd HH:mm:ss";
        String time1 = time.substring(time.indexOf(":") + 1);
        String time2 = time1.substring(time1.indexOf(":") + 1);
        if (time2.contains(".")) {
            formatStr += ".SSS";
        } else if (time2.contains(":")) {
            formatStr += ":SSS";
        }
        int len = time2.substring(time2.indexOf(".") + 1).length();
        if (len == 1) {
            time += "00";
        } else if (len == 2) {
            time += "0";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        Date rtnTime = null;
        try {
            rtnTime = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rtnTime;
    }

    public static void main(String[] args) throws ParseException {
        String startTime = "2020-03-19 11:50:38:120";
        String resTime = "2020-03-19 11:50:39:6";
        Date st = getFormatDate(startTime);
        Date rt = getFormatDate(resTime);
        System.out.println("st->" + st + " rt->" + rt);
        Long stTime = st.getTime();
        Long rtTime = rt.getTime();

        Long interval = (rtTime - stTime);
        System.out.println("st->" + stTime + " rt->" + rtTime + " interval->" + interval);
        // String rtn = ;
        //System.out.println(checkIPV4String("10.1"));

        //System.out.println("&".split("&").length);
        //splitStringByKeys("1-10&20-50&2&30-60", new String[]{"&", "-"});

        // System.out.println(getSqlStringByArr("PPS,OA".split(",")));
//        String str = "123.|133|21|.21";
////        str =str.replaceAll(".\\|",".");
////        System.out.println("str1->"+str);
////        str = str.replaceAll("\\|.",".");
////        System.out.println("acd|.323.|24".split("\\|")[0]);
    }
}

