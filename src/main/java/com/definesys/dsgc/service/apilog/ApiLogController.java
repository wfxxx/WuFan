package com.definesys.dsgc.service.apilog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilog.bean.*;
import com.definesys.dsgc.service.svclog.bean.DSGCLogInstance;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        JSONArray jsonArray = JSONArray.parseArray(body);
        try {
            apiLogService.recordLog(jsonArray);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("记录日志失败！");
        }
        return Response.ok().setData("ok");
    }
    @RequestMapping(value = "/queryApiNameData")
    public Response queryApiNameData(@RequestParam String apiName){
        List<ApiNameDTO> result = null;
        try {
           result = this.apiLogService.queryApiNameData(apiName);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询api名称失败");
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/doQueryApiLogInstanceList",method = RequestMethod.POST)
    public Response doQueryApiLogInstanceList(@RequestBody QueryApiLogInstVO param, @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        ResultVO<PageQueryResult<ApiLogInstListDTO>> resultVO = new ResultVO<PageQueryResult<ApiLogInstListDTO>>();
       // PageQueryResult<ApiLogInstListDTO> result = null;
        try {
          return  apiLogService.doQueryApiLogInstanceList(param,pageIndex,pageSize,userId,userRole, request);
           // resultVO.setData(result);
        }catch(JSONException jex){
            jex.printStackTrace();
            return Response.error(jex.getMessage());
        }catch (HttpClientErrorException hcex){
            hcex.printStackTrace();
            return Response.error(hcex.getMessage());
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
            return Response.error(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询数据失败！");
        }
    }
    @RequestMapping(value = "/queryApiLogPayloadData",method = RequestMethod.POST)
    public Response queryApiLogPayloadData(@RequestBody CommonReqBean commonReqBean,HttpServletRequest request){
        LogInstPayloadDTO result = null;
        try {
          return apiLogService.queryApiLogPayloadData(commonReqBean,request);
    }catch(JSONException jex){
        jex.printStackTrace();
        return Response.error(jex.getMessage());
    }catch (HttpClientErrorException hcex){
        hcex.printStackTrace();
        return Response.error(hcex.getMessage());
    }catch (IllegalArgumentException ex){
        ex.printStackTrace();
        return Response.error(ex.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return Response.error("查询数据失败！");
    }
    }
}
