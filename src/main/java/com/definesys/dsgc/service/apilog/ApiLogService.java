package com.definesys.dsgc.service.apilog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilog.bean.DSGCApisBean;
import com.definesys.dsgc.service.apilog.bean.DagLogBodyPayloadBean;
import com.definesys.dsgc.service.apilog.bean.DagLogInstanceBean;
import com.definesys.dsgc.service.consumers.ConsumersDao;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ApiLogService {
    @Autowired
    private ApiLogDao apiLogDao;

    @Autowired
    private ConsumersDao consumersDao;

    @Transactional(rollbackFor = Exception.class)
    public void recordLog(JSONArray jsonArray){
    JSONObject jsonObject = jsonArray.getJSONObject(0);
        DagLogInstanceBean dagLogInstanceBean = new DagLogInstanceBean();
        DagLogBodyPayloadBean dagLogBodyReqPayloadBean = new DagLogBodyPayloadBean();
        DagLogBodyPayloadBean dagLogBodyResPayloadBean = new DagLogBodyPayloadBean();
        String consumerCode = jsonObject.getString("consumer");
        dagLogInstanceBean.setCsmCode(consumerCode);
        DSGCConsumerEntities dsgcConsumerEntities =consumersDao.queryConsumerEntByCsmCode(consumerCode);
        if(dsgcConsumerEntities != null){
            dagLogInstanceBean.setExtraAttr2(dsgcConsumerEntities.getCsmName());
        }

        dagLogInstanceBean.setStartTime(new Date( jsonObject.getLong("started_at")));
        dagLogInstanceBean.setClientIp(jsonObject.getString("client_ip"));
        dagLogInstanceBean.setBsCode(jsonObject.getString("service"));
        String routeCode = jsonObject.getString("route");
        dagLogInstanceBean.setRouteCode(routeCode);
        JSONObject request = jsonObject.getJSONObject("request");

        dagLogBodyReqPayloadBean.setPayload(request.getString("body"));
        String method = request.getString("method");
        dagLogInstanceBean.setHttpMethod(method);
        dagLogInstanceBean.setReqMsgSize(request.getString("size"));
        dagLogInstanceBean.setIbUri(request.getString("url"));
        String uri = request.getString("uri");
        if(uri.indexOf("?") != -1){
           int index = uri.indexOf("?");
           uri = uri.substring(0,index);
        }
        DSGCApisBean dsgcApisBean =apiLogDao.queryApiMsgByPath(uri,method);

        if(dsgcApisBean != null){
            dagLogInstanceBean.setApiCode(dsgcApisBean.getApiCode());
            dagLogInstanceBean.setExtraAttr1(dsgcApisBean.getApiName());
            dagLogInstanceBean.setExtraAttr3(dsgcApisBean.getAppCode());
            dagLogInstanceBean.setExtraAttr4(dsgcApisBean.getAppName());
        }
        JSONObject reqHeaders = request.getJSONObject("headers");
        dagLogInstanceBean.setReqContentType(reqHeaders.getString("content-type"));
        String reqHeadersStr =reqHeaders.toJSONString().replaceAll("/","");
        if (reqHeadersStr.length()>1000){
            reqHeadersStr = reqHeadersStr.substring(0,1000);
        }
        dagLogInstanceBean.setReqHeaders(reqHeadersStr);
        JSONObject response = jsonObject.getJSONObject("response");

        dagLogBodyResPayloadBean.setPayload(response.getString("body"));
        dagLogInstanceBean.setResCode(response.getString("status"));
        dagLogInstanceBean.setReqMsgSize(response.getString("size"));
        JSONObject resHeaders = response.getJSONObject("headers");
        dagLogInstanceBean.setResContentType(resHeaders.getString("content-type"));
        String resHeadersStr =resHeaders.toJSONString().replaceAll("/","");
        if (resHeadersStr.length()>1000){
            resHeadersStr = resHeadersStr.substring(0,1000);
        }
        dagLogInstanceBean.setResHeaders(resHeadersStr);
        JSONObject latencies = jsonObject.getJSONObject("latencies");
        dagLogInstanceBean.setCostTime(latencies.getInteger("request"));
        dagLogInstanceBean.setBsCost(latencies.getInteger("proxy"));

        apiLogDao.addLogInstance(dagLogInstanceBean);
        dagLogBodyReqPayloadBean.setPayloadId(dagLogInstanceBean.getTrackId()+"IB");
        dagLogBodyResPayloadBean.setPayloadId(dagLogInstanceBean.getTrackId()+"OB");
        dagLogBodyReqPayloadBean.setTrackId(dagLogInstanceBean.getTrackId());
        dagLogBodyResPayloadBean.setTrackId(dagLogInstanceBean.getTrackId());
        apiLogDao.addLogPayload(dagLogBodyReqPayloadBean,dagLogBodyResPayloadBean);
    }
}
