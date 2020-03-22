package com.definesys.dsgc.service.utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpURLClient {
    public static Map<String,String> doGet(String httpurl,String auth) throws Exception {
        Map<String,String> map = new HashMap<>();
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
        //    connection.setRequestProperty("Authorization","Basic " + res);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
          //  if (connection.getResponseCode() == 200) {
                Map<String,List<String>> resHeadersMap = connection.getHeaderFields();
                String resHeaders =JSONObject.toJSONString(resHeadersMap);
                map.put("resHeaders",resHeaders);
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                map.put("resMsg",result);
          //  }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return map;
    }

    public static Map<String,String> doPost(String httpUrl, String param,List<Map<String,String>> headers,String auth) throws Exception {
        Map<String,String> map = new HashMap<>();
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            //"http://localhost:8888/dsgc/envinfo/queryEnvListDetail"
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);

            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
          //  String auth = "123"+":"+"94248c5e3d6754c58c69846cc7935c21f31c65c2";
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
         //   connection.setRequestProperty("Authorization","Basic " + res);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
            Iterator<Map<String,String>> iterator = headers.iterator();
            while (iterator.hasNext()){
                Map<String,String> item = iterator.next();
                connection.setRequestProperty(item.get("headerName"),item.get("headerValue"));
            }
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("con0","DAG");
            os.write(jsonObject.toJSONString().getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            //if (connection.getResponseCode() == 200) {
                Map<String,List<String>> resHeadersMap = connection.getHeaderFields();
                String resHeaders =JSONObject.toJSONString(resHeadersMap);
                map.put("resHeaders",resHeaders);
                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                map.put("resMsg",result);
           // }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            throw new Exception(e.getCause());
        }
        finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return map;
    }
}
