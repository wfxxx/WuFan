package com.definesys.dsgc.service.utils.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     * @return  ResultVO
     */
    public static ResultVO sendPostRequest(String url, JSONObject params, HttpServletRequest request){
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
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);
        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, new ParameterizedTypeReference<ResultVO>(){});


        return response.getBody();
    }

    public static String sendPostRequestCallStr(String url, JSONObject params, HttpServletRequest request){
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
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        ResponseEntity<ResultVO> response = client.exchange(url, method, requestEntity, ResultVO.class);
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);


        return response.getBody();
    }

}
