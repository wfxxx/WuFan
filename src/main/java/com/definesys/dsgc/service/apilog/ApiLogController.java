package com.definesys.dsgc.service.apilog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/dsgc/apilog")
public class ApiLogController {

    @Autowired
    private ApiLogService apiLogService;
    @RequestMapping(value = "/recordLog",method = RequestMethod.POST)
    public Response recordLog(HttpServletRequest request){
        String body = CommonUtils.charReader(request);
        if ("".equals(body)) {
            throw new MpaasBusinessException("日志数据为空");
        }
        System.out.println(body);
        System.out.println("===================================================================================================================");
        JSONArray jsonArray = JSONArray.parseArray(body);
        System.out.println(jsonArray);
        try {
            apiLogService.recordLog(jsonArray);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("记录日志失败！");
        }
        return Response.ok().setData("ok");

//        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//        Matcher m = p.matcher(body);
//       String dest = m.replaceAll("");
//        body = body.replaceAll("/","");
//        body = body.replaceAll("\\s","");
//        body = body.replaceAll("\n","");
//        body = body.replaceAll("\t","");
//        body = body.replaceAll("\"","\"");

    }
    public static void main(String args[]){
String content = "{\"latencies\":{\"request\":320,\"kong\":1,\"proxy\":318},\"service\":{\"host\":\"120.55.58.216\",\"created_at\":1582802191,\"connect_timeout\":60000,\"id\":\"09ba2dc3-dbb3-4ff3-9fcb-390ccb717941\",\"protocol\":\"http\",\"name\":\"testRouteDetail\",\"read_timeout\":60000,\"port\":7888,\"path\":\"\\/dsgc\\/apiRoute\\/queryRouteDetail\",\"updated_at\":1582802191,\"retries\":5,\"write_timeout\":60000,\"tags\":[]},\"request\":{\"querystring\":{},\"size\":\"313\",\"uri\":\"\\/testPost\",\"url\":\"http:\\/\\/esb.definesys.com:8800\\/testPost\",\"headers\":{\"host\":\"esb.definesys.com:8800\",\"content-type\":\"application\\/json\",\"postman-token\":\"7ab677eb-4613-4d87-90ac-bd4311b9ea9e\",\"accept\":\"*\\/*\",\"cache-control\":\"no-cache\",\"content-length\":\"18\",\"accept-encoding\":\"gzip, deflate, br\",\"user-agent\":\"PostmanRuntime\\/7.22.0\",\"connection\":\"keep-alive\"},\"body\":\"{\\n\\t\\\"con0\\\":\\\"test\\\"\\n}\",\"method\":\"POST\"},\"client_ip\":\"124.228.65.24\",\"tries\":[{\"balancer_latency\":0,\"port\":7888,\"balancer_start\":1582860724873,\"ip\":\"120.55.58.216\"}],\"upstream_uri\":\"\\/dsgc\\/apiRoute\\/queryRouteDetail\",\"response\":{\"size\":\"918\",\"headers\":{\"content-type\":\"application\\/json;charset=UTF-8\",\"date\":\"Fri, 28 Feb 2020 03:32:04 GMT\",\"connection\":\"close\",\"content-length\":\"593\",\"via\":\"kong\\/2.0.1\",\"server\":\"Oracle-HTTP-Server\",\"x-kong-proxy-latency\":\"2\",\"x-kong-upstream-latency\":\"318\",\"x-oracle-dms-ecid\":\"005btdvF2zGBd5GMyyBh6G00032Z00026R\",\"x-oracle-dms-rid\":\"0:1\"},\"status\":200,\"body\":\"{\\\"code\\\":\\\"ok\\\",\\\"data\\\":{\\\"rowId\\\":\\\"MAGRGRKJOO5VWAFOYY2J5V5RPFRXOOF3337JJVVJWHEM3DBNCZL5U36UEBNQW5J62OB6X3IXZKF4E\\\",\\\"drId\\\":\\\"670775975dfd4d22b9427e26d34c811f\\\",\\\"routeCode\\\":\\\"test\\\",\\\"bsCode\\\":\\\"ttttt\\\",\\\"routePath\\\":\\\"\\/test\\\",\\\"routeMethod\\\":\\\"POST\\\",\\\"stripPath\\\":\\\"Y\\\",\\\"routeDesc\\\":\\\"testfdfgdfgdf得分的地方干部\\\",\\\"appCode\\\":\\\"TEST123\\\",\\\"appName\\\":\\\"测试用例123sdfsdf\\\",\\\"createdBy\\\":\\\"2d24228f957248abbfd3836fa5266f82\\\",\\\"creationDate\\\":\\\"2020-02-21 14:35:01\\\",\\\"lastUpdatedBy\\\":\\\"2d24228f957248abbfd3836fa5266f82\\\",\\\"lastUpdateDate\\\":\\\"2020-02-21 14:35:01\\\",\\\"objectVersionNumber\\\":1},\\\"requestid\\\":\\\"a7ffe718d2784d369f8692f41cb7e730\\\"}\"},\"route\":{\"id\":\"c5410f8a-de23-46f2-9f1f-372935fb0ed8\",\"path_handling\":\"v0\",\"paths\":[\"\\/testPost\"],\"protocols\":[\"http\"],\"methods\":[\"POST\"],\"service\":{\"id\":\"09ba2dc3-dbb3-4ff3-9fcb-390ccb717941\"},\"name\":\"postRouteTest\",\"strip_path\":true,\"preserve_host\":false,\"regex_priority\":0,\"updated_at\":1582802459,\"hosts\":[],\"https_redirect_status_code\":426,\"created_at\":1582802294},\"started_at\":1582860724871}\n"
        ;
         JSONObject jsonObject = JSONObject.parseObject(content);
         System.out.println(jsonObject);
    }
}
