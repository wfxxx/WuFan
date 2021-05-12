package com.definesys.dsgc.service.interfacetest;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apiplugin.ApiPlugInDao;
import com.definesys.dsgc.service.apiplugin.bean.DagPluginUsingBean;
import com.definesys.dsgc.service.apiroute.ApiRouteDao;
import com.definesys.dsgc.service.apiroute.bean.DagDeployStatBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.consumers.ConsumersDao;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerAuth;
import com.definesys.dsgc.service.consumers.bean.DagConsumerToken;
import com.definesys.dsgc.service.interfacetest.bean.CommonReqBean;
import com.definesys.dsgc.service.interfacetest.bean.DoTestVO;
import com.definesys.dsgc.service.interfacetest.bean.InterfaceBaseInfoVO;
import com.definesys.dsgc.service.svcmng.SVCMngDao;
import com.definesys.dsgc.service.svcmng.SVCMngService;
import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadSampleBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri;
import com.definesys.dsgc.service.svcmng.bean.DSGCUriParamsBean;
import com.definesys.dsgc.service.svcmng.bean.ServUriParamterDTO;
import com.definesys.dsgc.service.utils.HttpURLClient;
import com.definesys.dsgc.service.utils.SoapClient;
import com.definesys.dsgc.service.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class InterfaceTestService {
    @Autowired
    private InterfaceTestDao interfaceTestDao;

    @Autowired
    private SVCMngDao svcMngDao;

    @Autowired
    private SVCMngService svcMngService;
    @Autowired
    private ConsumersDao consumersDao;

    @Autowired
    private ApiRouteDao apiRouteDao;

    @Autowired
    private ApiPlugInDao apiPlugInDao;

    public InterfaceBaseInfoVO queryInterfaceInfo(CommonReqBean param) throws Exception{
        if(StringUtil.isBlank(param.getCon0()) || StringUtil.isBlank(param.getQueryType())){
            throw new Exception("请求参数错误");
        }
        InterfaceBaseInfoVO infoVO = new InterfaceBaseInfoVO();
        DSGCServicesUri uris = svcMngDao.queryServUriFirst(param.getCon0());
        if (uris != null){
            if("0".equals(param.getQueryType())){
                infoVO.setHttpMethod(uris.getHttpMethod());
            }else if("1".equals(param.getQueryType())){
                infoVO.setSoapOper(uris.getSoapOper());
            }
            infoVO.setUri(uris.getIbUri());
        }
        List<ServUriParamterDTO>  servUriParamterDTOS =svcMngService.queryServUriParamter(param.getCon0());
        infoVO.setUriParam(servUriParamterDTOS);
        DSGCPayloadSampleBean dsgcPayloadSampleBean = svcMngDao.querySrvRestPaloadSample(param.getCon0());
        if(dsgcPayloadSampleBean != null){
            infoVO.setReqBody(dsgcPayloadSampleBean.getPlSample());
        }
        return infoVO;
    }
    public Map<String,String> doRestTest(DoTestVO param) throws Exception{
        List<Map<String,String>> paramterList = param.getParamters();
        String url = param.getUrl();
        int authType = 1;
        if(StringUtils.isNotBlank(param.getApiOrServ()) &&"API".equals( param.getApiOrServ())){
            String routeUri = param.getUri();
            DagRoutesBean dagRoutesBean = apiRouteDao.queryRouteByUri(routeUri);
            DagDeployStatBean dagDeployStatBean = apiRouteDao.queryDeployVid(dagRoutesBean.getRouteCode(),param.getEnvCode());
            List<DagPluginUsingBean> pluginUsingBeans = apiPlugInDao.queryPlugInsByVid(dagDeployStatBean.getVid());
            for (int i = 0; i < pluginUsingBeans.size(); i++) {
                if("basic-auth".equals(pluginUsingBeans.get(i).getPluginCode()) && "Y".equals(pluginUsingBeans.get(i).getIsEnable())){
                    authType = 1;
                    break;
                }
                if("jwt-auth".equals(pluginUsingBeans.get(i).getPluginCode()) && "Y".equals(pluginUsingBeans.get(i).getIsEnable())){
                    authType = 2;
                    break;
                }

            }

        }
        String auth = "";
        if(authType == 1){
            DSGCConsumerAuth dsgcConsumerAuth =consumersDao.queryConsumerDataByCsmCodeAndEnv(param.getConsumerCode(),param.getEnvCode());
//        if(dsgcConsumerAuth != null){
//            auth = dsgcConsumerAuth.getCsmCode() + ":"+dsgcConsumerAuth.getCaAttr1();
//
//        }else {
//            throw new Exception("消费者basic认证信息错误");
//        }
        }
        if(authType == 2){
            List<DagConsumerToken> dagConsumerTokenList = consumersDao.queryConsumerToken(param.getEnvCode(),param.getConsumerCode());
            for (int i = 0; i <dagConsumerTokenList.size() ; i++) {
                long currSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                if (Long.parseLong(dagConsumerTokenList.get(i).getExpTime())>currSecond){
                    String  token = dagConsumerTokenList.get(i).getToken();
                    Map<String,String> map = new HashMap<>();
                    map.put("headerName","Authorization");
                    map.put("headerValue","Bearer "+token);
                    param.getHeaders().add(map);
                    break;
                }
            }
        }

        if(param.getParamters().size()>0){
            StringBuffer urlParams = new StringBuffer("?");
            Iterator<Map<String,String>> iterator = paramterList.iterator();
            int i = 0;
            while (iterator.hasNext()){
                Map<String,String> item = iterator.next();
                String temp = "";
                if(i == 0){
                    temp = item.get("paramCode")+"="+item.get("paramSample");
                }else {
                    temp = "&"+ item.get("paramCode")+"="+item.get("paramSample");
                }

                urlParams.append(temp);
                i+=1;
            }
            url +=urlParams;
        }
        Map<String,String> result = new HashMap<>();
        try {
            if("POST".equals(param.getMethodType())){
                result = HttpURLClient.doPost(url,param.getRequestBody(),param.getHeaders(),auth);
            }
            if("GET".equals(param.getMethodType())){
                result = HttpURLClient.doGet(url,param.getHeaders(),auth);
            }
        }catch (Exception e){
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errorMsg",e.getMessage());
            result.put("resMsg",jsonObject.toJSONString());
        }
        result.put("reqUrl",url);
        result.put("reqBody",param.getRequestBody());

        if(param.getHeaders().size() > 0){
            String reqStrHeaders =JSONObject.toJSONString(param.getHeaders());
            result.put("reqHeaders",reqStrHeaders);
        }else {
            result.put("reqHeaders","");
        }

        System.out.println(result);
        return result;
    }
    public Map<String,String> doSoapTest(DoTestVO param) throws Exception{
        String url = param.getUrl();
        DSGCConsumerAuth dsgcConsumerAuth =consumersDao.queryConsumerDataByCsmCodeAndEnv(param.getConsumerCode(),param.getEnvCode());
        String userName = "";
        String password = "";
        if(dsgcConsumerAuth != null){
            userName = dsgcConsumerAuth.getCsmCode();
            password = dsgcConsumerAuth.getCaAttr1();

        }else {
            throw new RuntimeException("消费者basic认证信息错误");
        }

        Map<String,String> result = new HashMap<>();
        try {
            result = SoapClient.accessService(url,param.getRequestBody(),userName,password);
        }catch (Exception e){
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resMsg",e.getMessage());
            result.put("resMsg",jsonObject.toJSONString());
        }
        result.put("reqUrl",url);
        result.put("reqBody",param.getRequestBody().replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("\"","&quot;"));
        System.out.println(result);
        return result;
    }
}
