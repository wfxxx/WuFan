package com.definesys.dsgc.service.fullScreen.util;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MyFactory {
    public static Random random;

    public static final String[] COLOR_ARR = {"#ca91ff", "lightblue", "#91ff96", "#f56c6c", "#3a8ee6", "#3a8ee6",
            "#66b1ff", "#409eff", "#67c23a", "#e6a23c", "#909399", "#606266", "#40ffa0"};

    /**
     * 获取指定位数的随机数
     *
     * @param length
     * @return
     */
    public static String randomHexStr(int length) {
        try {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < length; i++) {
                //随机生成0-15的数值并转换成16进制
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            return "#" + result.toString().toUpperCase();
        } catch (Exception e) {
            System.out.println("获取16进制字符串异常，返回默认...");
            return "#00CCCC";
        }
    }

    /**
     * 获取指定个数的随机颜色编码
     *
     * @param num
     * @return
     */
    public static String[] getRandomColorCode(int num) {
        String[] colorArr = new String[num];
        for (int i = 0; i < num; i++) {
            colorArr[i] = randomHexStr(6);
        }
        return colorArr;
    }


    /**
     * 获取指定长度的最近一段时间 日期表
     *
     * @param length **包括当天**
     * @return
     */
    public static String[] getIntervalDay(int length) {
        String[] dayArr = new String[length];
        Calendar calendar = Calendar.getInstance();
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);//当天日期
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - (length - 1));//包括当天，所以少减1天
        int lastWeekDay = calendar.get(Calendar.DAY_OF_MONTH);//数天前日期
        int lastMonthLength = 0;//上月的天数
        int curMonthLength = 0;
        if (length - curDay > 0) {
            lastMonthLength = length - curDay;
            curMonthLength = curDay;
            for (int i = 0; i < lastMonthLength; i++) {
                dayArr[i] = lastWeekDay + "";
                lastWeekDay++;
                //System.out.println(i + "-" + dayArr[i]);
            }
        } else {
            curMonthLength = curDay - lastWeekDay + 1;
        }

        for (int j = curDay; j >= curDay - curMonthLength + 1; j--) {
            dayArr[j - curDay + curMonthLength - 1] = j + "";
            //System.out.println(j + "-" + j);
        }
//        System.out.println("curDay->" + curDay + " lastWeekDay->" + lastWeekDay +
//                " lastMonthLength->" + lastMonthLength+" curMonthLength->" + curMonthLength);
        return dayArr;
    }

    public static HttpSession getSessionObject() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.
                        getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();//创建session对象

        return session;
    }


    public static void main(String[] args) {
        //getRandomColorCode(10);
        //getIntervalDay(12);
        getSessionObject();
    }
}
