package com.definesys.dsgc.service.apimng;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apimng.bean.CommonReqBean;
import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import com.definesys.dsgc.service.apimng.bean.DSGCServicesUri;
import com.definesys.mpaas.common.http.Response;
import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/dsgc/dag/")
public class ApiMngController {

    @Autowired
    private ApiMngService apiMngService;

    /**
     * API资产管理查询
     *
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryApiMngList", method = RequestMethod.POST)
    public Response queryApiMngList(@RequestBody CommonReqBean param,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        return Response.ok().setData(apiMngService.queryApiMngList(param, pageIndex, pageSize, userId, userRole));
    }


    @RequestMapping(value = "/saveApiBasicInfo", method = RequestMethod.POST)
    public Response saveApiBasicInfo(@RequestBody ApiBasicInfoDTO param) {
        apiMngService.saveApiBasicInfo(param);
        return Response.ok();
    }

    @RequestMapping(value = "/queryBasicInfoByApiCode", method = RequestMethod.POST)
    public Response queryBasicInfoByApiCode(@RequestBody CommonReqBean param) {
        ApiBasicInfoDTO result = apiMngService.queryBasicInfoByApiCode(param.getCon0());
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/getRouteInfoList", method = RequestMethod.POST)
    public Response getRouteInfoList(@RequestBody CommonReqBean param,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        return Response.ok().setData(apiMngService.getRouteInfoList(param, pageIndex, pageSize, userId, userRole));
    }

    @RequestMapping(value = "/addDsgcApi", method = RequestMethod.POST)
    public Response addDsgcApi(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONObject apiParams = jsonObject.getJSONObject("apiParams");
        JSONObject uriParams = jsonObject.getJSONObject("uriParams");

        DSGCApisBean dsgcApisBean = new DSGCApisBean();
        dsgcApisBean.setApiCode(apiParams.getString("apiCode"));
        dsgcApisBean.setApiName(apiParams.getString("apiName"));
        dsgcApisBean.setApiDesc(apiParams.getString("apiDesc"));
        dsgcApisBean.setAppCode(apiParams.getString("appCode"));

        DSGCServicesUri dsgcServicesUri = new DSGCServicesUri();
        dsgcServicesUri.setServNo(uriParams.getString("servNo"));
        dsgcServicesUri.setIbUri(uriParams.getString("ibUri"));
        dsgcServicesUri.setUriType(uriParams.getString("uriType"));
        dsgcServicesUri.setHttpMethod(uriParams.getString("httpMethod"));
        dsgcServicesUri.setProvider(uriParams.getString("provider"));
        apiMngService.addDsgcApi(dsgcApisBean, dsgcServicesUri);
        return Response.ok();
    }
}
