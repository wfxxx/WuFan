package com.definesys.dsgc.service.apilog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilog.bean.*;
import com.definesys.dsgc.service.consumers.ConsumersDao;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.svclog.DSGCLogInstanceDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApiLogService {
    @Autowired
    private ApiLogDao apiLogDao;

    @Autowired
    private ConsumersDao consumersDao;

    @Autowired
    DSGCLogInstanceDao dsgcLogInstanceDao;

    @Transactional(rollbackFor = Exception.class)
    public void recordLog(JSONArray jsonArray){
        for (int i = 0; i <jsonArray.size() ; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            DagLogInstanceBean dagLogInstanceBean = new DagLogInstanceBean();
            DagLogBodyPayloadBean dagLogBodyReqPayloadBean = new DagLogBodyPayloadBean();
            DagLogBodyPayloadBean dagLogBodyResPayloadBean = new DagLogBodyPayloadBean();
            String consumerCode = jsonObject.getString("consumer");
            dagLogInstanceBean.setCsmCode(consumerCode);
            DSGCConsumerEntities dsgcConsumerEntities = consumersDao.queryConsumerEntByCsmCode(consumerCode);
            if (dsgcConsumerEntities != null) {
                dagLogInstanceBean.setExtraAttr2(dsgcConsumerEntities.getCsmName());
            }
            Date startTime = new Date(jsonObject.getLong("started_at"));
            dagLogInstanceBean.setStartTime(startTime);
            dagLogInstanceBean.setCreationDate(startTime);
            dagLogInstanceBean.setClientIp(jsonObject.getString("client_ip"));
            dagLogInstanceBean.setBsCode(jsonObject.getString("service"));
            String routeCode = jsonObject.getString("route");
            dagLogInstanceBean.setRouteCode(routeCode);

            JSONObject request = jsonObject.getJSONObject("request");

            dagLogBodyReqPayloadBean.setPayload(request.getString("body"));
            String method = request.getString("method");
            dagLogInstanceBean.setHttpMethod(method);
            if (StringUtil.isNotBlank(request.getString("size"))) {
                dagLogInstanceBean.setReqMsgSize(request.getString("size"));
            } else {
                dagLogInstanceBean.setReqMsgSize("0");
            }
            dagLogInstanceBean.setIbUri(request.getString("url"));
            String uri = request.getString("uri");
            if (uri.indexOf("?") != -1) {
                int index = uri.indexOf("?");
                uri = uri.substring(0, index);
            }
            DSGCApisBean dsgcApisBean = apiLogDao.queryApiMsgByPath(uri, method);

            if (dsgcApisBean != null) {
                dagLogInstanceBean.setApiCode(dsgcApisBean.getApiCode());
                dagLogInstanceBean.setExtraAttr1(dsgcApisBean.getApiName());
                dagLogInstanceBean.setExtraAttr3(dsgcApisBean.getAppCode());
                dagLogInstanceBean.setExtraAttr4(dsgcApisBean.getAppName());
            }
            JSONObject reqHeaders = request.getJSONObject("headers");
            dagLogInstanceBean.setReqContentType(reqHeaders.getString("content-type"));
            String reqHeadersStr = reqHeaders.toJSONString().replaceAll("/", "");
            if (reqHeadersStr.length() > 1000) {
                reqHeadersStr = reqHeadersStr.substring(0, 1000);
            }
            dagLogInstanceBean.setReqHeaders(reqHeadersStr);
            JSONObject response = jsonObject.getJSONObject("response");

            dagLogBodyResPayloadBean.setPayload(response.getString("body"));
            dagLogInstanceBean.setResCode(response.getString("status"));
            if (StringUtil.isNotBlank(response.getString("size"))) {
                dagLogInstanceBean.setResMsgSize(response.getString("size"));
            } else {
                dagLogInstanceBean.setResMsgSize("0");
            }

            JSONObject resHeaders = response.getJSONObject("headers");
            dagLogInstanceBean.setResContentType(resHeaders.getString("content-type"));
            String resHeadersStr = resHeaders.toJSONString().replaceAll("/", "");
            if (resHeadersStr.length() > 1000) {
                resHeadersStr = resHeadersStr.substring(0, 1000);
            }
            dagLogInstanceBean.setResHeaders(resHeadersStr);
            JSONObject latencies = jsonObject.getJSONObject("latencies");
            dagLogInstanceBean.setCostTime(latencies.getInteger("request"));
            dagLogInstanceBean.setBsCost(latencies.getInteger("proxy"));

            apiLogDao.addLogInstance(dagLogInstanceBean);
            dagLogBodyReqPayloadBean.setPayloadId(dagLogInstanceBean.getTrackId() + "IB");
            dagLogBodyResPayloadBean.setPayloadId(dagLogInstanceBean.getTrackId() + "OB");
            dagLogBodyReqPayloadBean.setTrackId(dagLogInstanceBean.getTrackId());
            dagLogBodyResPayloadBean.setTrackId(dagLogInstanceBean.getTrackId());
            apiLogDao.addLogPayload(dagLogBodyReqPayloadBean, dagLogBodyResPayloadBean);
        }
    }

    public List<ApiNameDTO> queryApiNameData(String apiName){
        List<DSGCApisBean> list = apiLogDao.queryApiNameData(apiName);
        List<ApiNameDTO> result = new ArrayList<>();
        Iterator<DSGCApisBean> iterator = list.iterator();
        while (iterator.hasNext()){
            DSGCApisBean dsgcApisBean = iterator.next();
            ApiNameDTO apiNameDTO = new ApiNameDTO();
            apiNameDTO.setApiCode(dsgcApisBean.getApiCode());
            apiNameDTO.setApiName(dsgcApisBean.getApiName());
            result.add(apiNameDTO);
        }
        return result;
    }
    //PageQueryResult<ApiLogInstListDTO>
    public Response doQueryApiLogInstanceList(QueryApiLogInstVO param,int pageIndex,int pageSize,String userId,String userRole,HttpServletRequest request)throws Exception{
        if(StringUtil.isBlank(param.getEnv())){
            throw new Exception("输入参数环境信息错误！");
        }

        FndProperties fndProperties =apiLogDao.findFndPropertiesByKey("DSGC_API_CURRENT_ENV");
        if(fndProperties == null){
            throw new Exception("请配置当前环境代码！");
        }
        List<DSGCEnvInfoCfg> envList = apiLogDao.queryApiEnv();
        PageQueryResult<ApiLogInstListDTO> queryResult = new PageQueryResult<>();
        List<ApiLogInstListDTO> apiLogInstListDTOList = new ArrayList<>();
        List<String> consumerCodeList = new ArrayList<>();
        List<String> appCodeList = new ArrayList<>();
        if ( fndProperties.getPropertyValue().equals(param.getEnv())){
            switch (userRole){
                case "Tourist":
                    List<DSGCConsumerUsers> consumerList =  apiLogDao.querytTouristConsumerList(userId);
                    Iterator<DSGCConsumerUsers> iterator = consumerList.iterator();
                    while (iterator.hasNext()){
                        DSGCConsumerUsers dsgcConsumerUsers = iterator.next();
                        consumerCodeList.add(dsgcConsumerUsers.getCsmCode());
                    }
                    break;
                case "SystemLeader":
                    List<DSGCSystemUser> systemUserList = apiLogDao.querySystemLeaderAppList(userId);
                    Iterator<DSGCSystemUser> iterator1 = systemUserList.iterator();
                    while (iterator1.hasNext()){
                        DSGCSystemUser dsgcSystemUser = iterator1.next();
                        appCodeList.add(dsgcSystemUser.getSysCode());
                    }
                    break;
                    default:
                        break;
            }
            PageQueryResult<DagLogInstanceBean> result =  apiLogDao.doQueryApiLogInstanceList(param,pageIndex,pageSize,userRole,consumerCodeList,appCodeList);
            apiLogInstListDTOList = resultMapping(result.getResult());
            queryResult.setResult(apiLogInstListDTOList);
            queryResult.setCount(result.getCount());
            return Response.ok().setData(queryResult);
        }
        else {
            ResultVO<Response> resultvo = new ResultVO<>();
            for (int i = 0; i < envList.size(); i++) {
                if(param.getEnv().equals(envList.get(i).getEnvCode())){
                    String logPath =envList.get(i).getDsgcAdmin();
                    logPath += "/dsgc/apilog/doQueryApiLogInstanceList?pageSize="+pageSize+"&pageIndex="+pageIndex;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("apiCode",param.getApiCode());
                    jsonObject.put("consumerCode",param.getConsumerCode());
                    jsonObject.put("startTime",param.getStartTime());
                    jsonObject.put("endTime",param.getEndTime());
                    jsonObject.put("env",envList.get(i).getEnvCode());
                    try {
                        resultvo = HttpReqUtil.sendPostRequest(logPath,jsonObject,request);

                    }catch(JSONException jex){
                        jex.printStackTrace();
                        throw new JSONException("参数解析异常，请检查请求参数是否正确！");
                    }catch (HttpClientErrorException hcex){
                        hcex.printStackTrace();
                        throw new HttpClientErrorException(HttpStatus.resolve(404));
                    }catch (IllegalArgumentException ex){
                        ex.printStackTrace();
                        throw new IllegalArgumentException("环境信息配置的uri不能为空！");
                    }
                    break;
                }
            }
            return resultvo.getData();
        }
    }

    public List<ApiLogInstListDTO> resultMapping(List<DagLogInstanceBean> result){
        List<ApiLogInstListDTO> apiLogInstListDTOList = new ArrayList<>();
        Iterator<DagLogInstanceBean> iterator = result.iterator();
        while (iterator.hasNext()){
            DagLogInstanceBean dagLogInstanceBean = iterator.next();
            ApiLogInstListDTO apiLogInstListDTO = new ApiLogInstListDTO();
            apiLogInstListDTO.setApiName(dagLogInstanceBean.getExtraAttr1());
            apiLogInstListDTO.setConsumerName(dagLogInstanceBean.getExtraAttr2());
            apiLogInstListDTO.setTrackId(dagLogInstanceBean.getTrackId());
            apiLogInstListDTO.setCostTime(dagLogInstanceBean.getCostTime());
            apiLogInstListDTO.setHttpMethod(dagLogInstanceBean.getHttpMethod());
            apiLogInstListDTO.setResCode(dagLogInstanceBean.getResCode());
            apiLogInstListDTO.setReqMsgSize(dagLogInstanceBean.getReqMsgSize());
            apiLogInstListDTO.setResMsgSize(dagLogInstanceBean.getResMsgSize());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = format.format(dagLogInstanceBean.getStartTime());
            apiLogInstListDTO.setStartTime(str);
            apiLogInstListDTOList.add(apiLogInstListDTO);
        }
        return apiLogInstListDTOList;
    }

    public Response queryApiLogPayloadData(CommonReqBean commonReqBean,HttpServletRequest request)throws Exception{
        if(StringUtil.isBlank(commonReqBean.getEnv())){
            throw new Exception("输入参数环境信息错误！");
        }
        FndProperties fndProperties =apiLogDao.findFndPropertiesByKey("DSGC_API_CURRENT_ENV");
        List<DSGCEnvInfoCfg> envList = apiLogDao.queryApiEnv();
        if(fndProperties != null && commonReqBean.getEnv().equals(fndProperties.getPropertyValue())){
            DagLogInstanceBean dagLogInstanceBean = apiLogDao.querApiLOgInstById(commonReqBean.getCon0());
            List<DagLogBodyPayloadBean> dagLogBodyPayloadBeans =apiLogDao.queryApiLogPayloadData(commonReqBean.getCon0());
            LogInstPayloadDTO logInstPayloadDTO = new LogInstPayloadDTO();
            logInstPayloadDTO.setUrl(dagLogInstanceBean.getIbUri().trim().replaceAll("\\s*",""));
            logInstPayloadDTO.setReqHeaders(dagLogInstanceBean.getReqHeaders().trim().replaceAll("\\s*",""));
            Iterator<DagLogBodyPayloadBean> iterator = dagLogBodyPayloadBeans.iterator();
            logInstPayloadDTO.setReqPayload("");
            logInstPayloadDTO.setResPayload("");
            while (iterator.hasNext()){
                DagLogBodyPayloadBean dagLogBodyPayloadBean = iterator.next();
                if (dagLogBodyPayloadBean.getPayload() != null && dagLogBodyPayloadBean.getPayloadId().indexOf("IB") != -1){
                    logInstPayloadDTO.setReqPayload(dagLogBodyPayloadBean.getPayload().trim().replaceAll("\\s*",""));
                }
                if(dagLogBodyPayloadBean.getPayload() != null && dagLogBodyPayloadBean.getPayloadId().indexOf("OB") != -1){
                    logInstPayloadDTO.setResPayload(dagLogBodyPayloadBean.getPayload().trim().replaceAll("\\s*",""));
                }
            }
           return Response.ok().setData(logInstPayloadDTO);
        }else {
            ResultVO<Response> resultvo = new ResultVO<>();
            for (int i = 0; i < envList.size(); i++) {
                if (commonReqBean.getEnv().equals(envList.get(i).getEnvCode())) {
                    String logPath =envList.get(i).getDsgcAdmin();
                    logPath += "/dsgc/apilog/queryApiNameData";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("con0",commonReqBean.getCon0());
                    jsonObject.put("env",envList.get(i).getEnvCode());
                    try {
                        resultvo = HttpReqUtil.sendPostRequest(logPath,jsonObject,request);

                    }catch(JSONException jex){
                        jex.printStackTrace();
                        throw new JSONException("参数解析异常，请检查请求参数是否正确！");
                    }catch (HttpClientErrorException hcex){
                        hcex.printStackTrace();
                        throw new HttpClientErrorException(HttpStatus.resolve(404));
                    }catch (IllegalArgumentException ex){
                        ex.printStackTrace();
                        throw new IllegalArgumentException("环境信息配置的uri不能为空！");
                    }
                    break;
                }
            }
            return resultvo.getData();
        }
    }
    public Map<String,Object> queryApiEnv(){
        Map<String,Object> map = new HashMap<>();
        List<DSGCEnvInfoCfg> list = apiLogDao.queryApiEnv();
        com.definesys.dsgc.service.svclog.bean.FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_API_CURRENT_ENV");
        List<DagEnvInfoCfgDTO> result = new ArrayList<>();
        Iterator<DSGCEnvInfoCfg> infoCfgBeanIterator = list.iterator();
        while (infoCfgBeanIterator.hasNext()){
            DSGCEnvInfoCfg dsgcEnvInfoCfg = infoCfgBeanIterator.next();
            DagEnvInfoCfgDTO dagEnvInfoCfgDTO = new DagEnvInfoCfgDTO();
            dagEnvInfoCfgDTO.setEnvCode(dsgcEnvInfoCfg.getEnvCode());
            dagEnvInfoCfgDTO.setEnvName(dsgcEnvInfoCfg.getEnvName());
            result.add(dagEnvInfoCfgDTO);
        }
        map.put("envList",result);
        map.put("currentEnv",fndProperties.getPropertyValue());
        return map;
    }
}
