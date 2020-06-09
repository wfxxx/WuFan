package com.definesys.dsgc.service.utils.httpclient;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.svclog.bean.TempQueryLogCondition;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/7/3 上午10:10
 * @history: 1.2019/7/3 created by biao.luo
 */
public class HttpReqUtil {


    public static void main(String[] args){
        try {
            RestTemplate client = new RestTemplate();
            String url = "http://192.168.1.34:7778/dsgc/logInstance/HttpRestLogSwith?pageSize=10&pageIndex=1";
            TempQueryLogCondition params = new TempQueryLogCondition();

            String json="{\n" +
                    "  \"logInstance\": {\n" +
                    "    \"trackId\": null,\n" +
                    "    \"servNo\": null,\n" +
                    "    \"servName\": null,\n" +
                    "    \"logPartition\": null,\n" +
                    "    \"token\": null,\n" +
                    "    \"startTime\": \"2020-06-09 15:19:00\",\n" +
                    "    \"endTime\": \"2020-06-09 16:20:00\",\n" +
                    "    \"defaultDateRange\": [\n" +
                    "      \"2020-06-09T07:19:00.000Z\",\n" +
                    "      \"2020-06-09T08:20:00.000Z\"\n" +
                    "    ],\n" +
                    "    \"instStatus\": null,\n" +
                    "    \"bizStatus\": null,\n" +
                    "    \"reqFrom\": null,\n" +
                    "    \"bizKey1\": null,\n" +
                    "    \"history\": null,\n" +
                    "    \"bizStatusDtl\": null\n" +
                    "  },\n" +
                    "  \"env\": \"ESB_DEV\"\n" +
                    "}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.set("userRole","SuperAdministrators");
            headers.set("uid","053d7986b7394f2db64fcc9713cdbd99");
            headers.set("userName","Legolas");
            headers.set("token",null);
            HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.parseObject(json),headers);
            //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);
            ResponseEntity<ResultVO> response = client.exchange(url,HttpMethod.resolve("POST"),requestEntity,new ParameterizedTypeReference<ResultVO>() {
            });


            System.out.println(response.getBody());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 向目的URL发送post请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return ResultVO
     */
    public static ResultVO sendPostRequest(String url,JSONObject params,HttpServletRequest request) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("userRole",request.getHeader("userRole"));
        headers.set("uid",request.getHeader("uid"));
        headers.set("userName",request.getHeader("userName"));
        headers.set("token",request.getHeader("token"));
        //将请求头部和参数合成一个请求
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params,headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);
        ResponseEntity<ResultVO> response = client.exchange(url,method,requestEntity,new ParameterizedTypeReference<ResultVO>() {
        });


        return response.getBody();
    }

    public static String sendPostRequestCallStr(String url,JSONObject params,HttpServletRequest request) {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("userRole",request.getHeader("userRole"));
        headers.set("uid",request.getHeader("uid"));
        headers.set("userName",request.getHeader("userName"));
        headers.set("token",request.getHeader("token"));
        //将请求头部和参数合成一个请求
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params,headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);
        ResponseEntity<String> response = client.exchange(url,method,requestEntity,String.class);


        return response.getBody();
    }

    public static <T> T postJsonText(String url,@Nullable String jsonText,Class<T> responseType,Object... uriVariables) {
        RestTemplate client = new RestTemplate();
        //client.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept",MediaType.APPLICATION_JSON.toString());

     //  System.out.println("===>post to dag:"+jsonText);
        HttpEntity<String> request = new HttpEntity<String>(jsonText,headers);

        String res = client.postForObject(url,request,String.class,uriVariables);

        if (responseType == String.class) {
            return (T)res;
        } else {
            return JSONObject.parseObject(res,responseType);
        }

    }

    public static <T> T postObject(String url,@Nullable Object request,Class<T> responseType,Object... uriVariables) {
        String jsonText = null;
        if (request != null) {
            jsonText = JSONObject.toJSONString(request);
        }
        return postJsonText(url,jsonText,responseType,uriVariables);
    }

    public static <T> T patchJsonText(String url,@Nullable String jsonText,Class<T> responseType,Object... uriVariables) {
        RestTemplate client = new RestTemplate();
        //client.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept",MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> request = new HttpEntity<String>(jsonText,headers);

        String res = client.patchForObject(url,request,String.class,uriVariables);

        if (responseType == String.class) {
            return (T)res;
        } else {
            return JSONObject.parseObject(res,responseType);
        }

    }

    public static <T> T patchObject(String url,@Nullable Object request,Class<T> responseType,Object... uriVariables) {
        String jsonText = null;
        if (request != null) {
            jsonText = JSONObject.toJSONString(request);
        }
        return patchJsonText(url,jsonText,responseType,uriVariables);
    }

    public static void putJsonText(String url,@Nullable String jsonText,Object... uriVariables) {
        RestTemplate client = new RestTemplate();
        //client.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        HttpEntity<String> request = new HttpEntity<String>(jsonText,headers);

         client.put(url,request,uriVariables);



    }

    public static void putObject(String url,@Nullable Object request,Object... uriVariables) {
        String jsonText = null;
        if (request != null) {
            jsonText = JSONObject.toJSONString(request);
        }
        //System.out.println("====>put to DAG:"+jsonText);
        putJsonText(url,jsonText,uriVariables);
    }

    public static <T> T getObject(String url,Class<T> responseType,Object... uriVariables) {
        RestTemplate client = new RestTemplate();
        String res = client.getForObject(url,String.class,uriVariables);
        if (responseType == String.class) {
            return (T)res;
        } else {
            return JSONObject.parseObject(res,responseType);
        }
    }

    public static void delete(String url,Object... uriVariables){
        RestTemplate client = new RestTemplate();
        client.delete(url,uriVariables);
    }







}
