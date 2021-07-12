package com.definesys.dsgc.service.ystar.utils;

import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.svcgen.util.ShellUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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


    /***
     * 方法参数说明
     * @param target 调用方法的当前对象
     * @param methodName 方法名称
     * @param parameterTypes 调用方法的参数类型
     * @param params 参数  可以传递多个参数
     *
     * */
    public static Object callMethod(final Object target, final String methodName, int second, final Class<?>[] parameterTypes, final Object[] params) throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
                Object returnValue = method.invoke(target, params);
                return returnValue != null ? returnValue.toString() : null;
            }
        });

        executorService.execute(future);
        String result = null;
        try {
            /**获取方法返回值 并设定方法执行的时间为10秒*/
            result = future.get(second, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            executorService.shutdownNow();
            System.out.println("--0---");
            throw e;
        } finally {
            System.out.println("--1---");
            executorService.shutdownNow();
            System.out.println("--2---");
        }
        return result;
    }


    public static void main(String[] args) {
        try {
            callMethod(new ShellUtil(), "checkValid", 5, new Class<?>[]{String.class, String.class, String.class},
                    new Object[]{"dongdong.yuan@definesys.com", "YStarYdd089", "http://git.definesys.com/ystar/dsgcapp.git"});
        } catch (RuntimeException | ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        System.out.println("11111");

    }


}
