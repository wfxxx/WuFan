package com.definesys.dsgc.service.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {

    public static String charReader(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = request.getReader();
            String str, wholeStr = "";
            while ((str = br.readLine()) != null) {
                wholeStr += str;
            }
            return wholeStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 打印异常的详细堆栈信息
     *
     * @param e
     * @return
     */
    public static String getExceptionTrackDetailInfo(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            StringBuffer sbf = sw.getBuffer();
            pw.close();
            sw.close();
            return sbf.toString();
        } catch (Exception es) {
            es.printStackTrace();
            return null;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception ess) {
                    ess.printStackTrace();
                }
            }
        }
    }

    public static String splitSpecifyLengthStr(String sour,int length) {

        if (sour != null && sour.length() > 0 && length > 0) {
            try {
                byte[] b = sour.getBytes("UTF-8");
                if (b.length > length) {
                    byte[] subArray = new byte[length];
                    for (int i = 0; i < length; i++) {
                        subArray[i] = b[i];
                    }
                    return new String(subArray,"UTF-8");
                } else {
                    return sour;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }

}
