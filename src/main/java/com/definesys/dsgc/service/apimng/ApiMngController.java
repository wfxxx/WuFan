package com.definesys.dsgc.service.apimng;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apimng.bean.*;
import com.definesys.dsgc.service.svcmng.bean.ServUriParamterDTO;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/dag/")
public class ApiMngController {

    @Autowired
    private ApiMngService apiMngService;

    /**
     * 删除服务
     *
     * @param map
     * @return
     */
    @GetMapping(value = "/delApiByCode")
    public Response delApiByCode(@RequestParam String apiCode) {
        try {
            apiMngService.delApiByCode(apiCode);
        } catch (MpaasBusinessException mp) {
            mp.printStackTrace();
            return Response.error(mp.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除服务失败");
        }
        return Response.ok().setMessage("删除成功！");
    }


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
        DSGCApisUri dsgcApisUri = new DSGCApisUri();
        dsgcApisUri.setServNo(uriParams.getString("servNo"));
        dsgcApisUri.setIbUri(uriParams.getString("ibUri"));
        dsgcApisUri.setUriType(uriParams.getString("uriType"));
        dsgcApisUri.setHttpMethod(uriParams.getString("httpMethod"));
        dsgcApisUri.setProvider(uriParams.getString("provider"));
        apiMngService.addDsgcApi(dsgcApisBean, dsgcApisUri);
        return Response.ok();
    }

    @RequestMapping(value = "/checkApiCodeIsExist", method = RequestMethod.POST)
    public Response checkApiCodeIsExist(@RequestBody CommonReqBean param) {
        try {
            Boolean isExist = apiMngService.checkApiCodeIsExist(param);
            return Response.ok().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("验证API编号失败！");
        }
    }

    @RequestMapping(value = "/queryApisUri", method = RequestMethod.POST)
    public Response queryApisUri(@RequestBody CommonReqBean param) {
        List<ApiUriDTO> result = apiMngService.queryApisUri(param.getCon0());
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/queryApisUriParameter", method = RequestMethod.POST)
    public Response queryApisUriParameter(@RequestBody CommonReqBean param) {
        List<ServUriParamterDTO> result = apiMngService.queryApisUriParameter(param.getCon0());
        return Response.ok().setData(result);
    }
}
