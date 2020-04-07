package com.definesys.dsgc.service.utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SoapClient {

    public synchronized static Map<String,String> accessService(String url,String reqPayload,String userName,String password){
        Map<String,String> map = new HashMap<>();
        System.out.println(reqPayload.toString());
        PostMethod postMethod = new PostMethod("http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        try {  
             String soapRequest = reqPayload.toString();
            StringRequestEntity requestEntity =
                    new StringRequestEntity(soapRequest, "text/xml; charset=UTF-8", "UTF-8");
                postMethod.setRequestEntity(requestEntity);
            HttpClient httpClient = new HttpClient();
         //   UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
         //   httpClient.getState().setCredentials(AuthScope.ANY, creds);
            HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(300000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(10000000);
            int status = httpClient.executeMethod(postMethod);
            if (Integer.compare(status, HttpStatus.SC_OK) != 0)
                throw new IllegalStateException("调用webservice错误 : " + postMethod.getStatusLine());

            String soapResponseData = postMethod.getResponseBodyAsString();
            Header[] resHeaders =  postMethod.getResponseHeaders();
            String resStrHeaders = "";
            for (int i = 0; i < resHeaders.length; i++) {
                System.out.println(resHeaders[i]);
                String temp = resHeaders[i].getName()+":"+resHeaders[i].getValue()+"\n";
                System.out.println(temp);
                resStrHeaders += temp;
            }
            map.put("resMsg",StringUtils.formatXml(soapResponseData).replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;"));
            map.put("resHeaders",resStrHeaders.toString().replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("resMsg","errorMessage : " + e.getMessage());
            return map;
        } catch (HttpException e) {
            e.printStackTrace();
            map.put("resMsg","errorMessage : " + e.getMessage());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            map.put("resMsg","errorMessage : " + e.getMessage());
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("resMsg","errorMessage : " + e.getMessage());
            return map;
        }finally {
            postMethod.releaseConnection();
        }
        return map;
    }


}
