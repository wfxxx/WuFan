package com.definesys.dsgc.service.svclog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.svclog.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.svclog.DSGCLogInstanceService;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.dsgc.service.utils.MsgCompressUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequestMapping(value = "/dsgc/logInstance")
@RestController
public class DSGCLogInstanceController {

    @Autowired
    DSGCLogInstanceService logService;

    @Autowired
    private SWordLogger logger;

    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Response query(@RequestBody TempQueryLogCondition tempQueryLogCondition,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                          HttpServletRequest request) {
        LogInstanceQueryDTO logInstance = tempQueryLogCondition.getLogInstance();
        //  List<Object> keyword = tempQueryLogCondition.getKeywordForm();
        String userRole = request.getHeader("userRole");
        String uid = request.getHeader("uid");
        //  PageQueryResult<DSGCLogInstance> datas = this.logService.query(keyword,userRole,uid,logInstance, pageSize, pageIndex);
        return null;
        //   return Response.ok().data(datas);
    }

    /**
     * 查询日志实例--根据url，切换到不同的环境，查询不同环境的的日志实例
     *
     * @param reqMap
     * @param request
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/queryLogInstanceBySwitchUrl", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO queryLogInstanceBySwitchUrl(@RequestBody com.alibaba.fastjson.JSONObject reqMap, HttpServletRequest request) {
        ResultVO<PageQueryResult<DSGCLogInstance>> resultVO = new ResultVO<PageQueryResult<DSGCLogInstance>>();
        LogInstanceQueryDTO logInstance = JSON.parseObject(reqMap.getString("logInstance"), LogInstanceQueryDTO.class);
        List<Object> keyword = JSON.parseArray(reqMap.getString("keyword"));
        Integer pageSize = (Integer) JSON.parse(reqMap.getString("pageSize"));
        Integer pageIndex = (Integer) JSON.parse(reqMap.getString("pageIndex"));
        String payloadData = (String) JSON.parse(reqMap.getString("payloadData"));
//        String userName = (String)JSON.parse(reqMap.getString("userName"));

        String userRole = request.getHeader("userRole");
        String uid = request.getHeader("uid");
        //   PageQueryResult<DSGCLogInstance> datas = this.logService.query(keyword,userRole,uid,logInstance, pageSize, pageIndex);
        //   resultVO.setData(datas);
        return resultVO;
    }

    /**
     * 日志查询控制器，用于切换环境，查询不同环境下的日志实例
     *
     * @return
     */
    @RequestMapping(value = "/HttpRestLogSwith", method = {RequestMethod.POST, RequestMethod.GET})
    public Response HttpRestLogSwith(@RequestBody TempQueryLogCondition tempQueryLogCondition,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                     HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
            return logService.queryEsbServLogInst(tempQueryLogCondition, pageSize, pageIndex, userRole, userId, request);
        } catch (JSONException | HttpClientErrorException | IllegalArgumentException jex) {
            jex.printStackTrace();
            return Response.error(jex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询数据失败！");
        }
    }


    @RequestMapping(value = "/httpRestFindLogByIdSwitch", method = RequestMethod.POST)
    public Response httpRestFindLogByIdSwitch(@RequestBody TempQueryLogCondition param, HttpServletRequest request, HttpServletResponse response) {

        try {
            return logService.httpRestFindLogByIdSwitch(param, request);
        } catch (JSONException jex) {
            jex.printStackTrace();
            return Response.error(jex.getMessage());
        } catch (HttpClientErrorException hcex) {
            hcex.printStackTrace();
            return Response.error(hcex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return Response.error(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询数据失败！");
        }

//        String reqJson
//        JSONObject req = JSONObject.parseObject(reqJson);
//        String httpReqUrl = req.getString("httpReqChildUrl");
//        String trackId = req.getString("trackId");

//        com.alibaba.fastjson.JSONObject trackPram = new com.alibaba.fastjson.JSONObject();
//
//        JSONObject res = new JSONObject();
//        JSONObject json = null;
//        PrintWriter out = null;
//        String resultVO = null;
//        try{
//            trackPram.put("trackId",trackId);
        //      resultVO = HttpReqUtil.sendPostRequestCallStr(httpReqUrl,trackPram,request);
//
//            json = JSONObject.parseObject(resultVO);
//            res.put("data",json);
//            res.put("code","ok");
//            response.setContentType("text/xml;charset=UTF-8");
//            out = response.getWriter();
//        }catch(JSONException jex){
//            jex.printStackTrace();
//            logger.error("%s", jex.getMessage());
//            res.put("code","error");
//            res.put("message","参数解析异常，请检查请求参数是否正确！"+jex.getMessage());
//
//        }catch (HttpClientErrorException hcex){
//            hcex.printStackTrace();
//            logger.error("%s", hcex.getMessage());
//            res.put("code","error");
//            res.put("message","404，请求的url不存在，请检查要访问的远程外部接口URL配置是否正确！"+hcex.getMessage());
//
//        }catch (IllegalArgumentException ex){
//            ex.printStackTrace();
//            logger.error("%s", ex.getMessage());
//            res.put("code","error");
//            res.put("message","请求的uri不能为空！"+ex.getMessage());
//
//        }catch (HttpServerErrorException hssee){
//            hssee.printStackTrace();
//            logger.error("%s", hssee.getMessage());
//            res.put("code","error");
//            res.put("message","返还数据时，转换出错！出错信息为："+hssee.getMessage());
//
//        }catch (IOException ioex){
//            ioex.printStackTrace();
//            logger.error("%s", ioex.getMessage());
//            res.put("code","error");
//            res.put("message","返还数据时，转换出错！出错信息为："+ioex.getMessage());
//
//        }finally {
//            out.print(res);
//            out.flush();
//            out.close();
//        }

    }

    @RequestMapping(value = "/findLogByTraceId", method = RequestMethod.POST)
    public Response findLogByTraceId(@RequestBody com.alibaba.fastjson.JSONObject reqJson, HttpServletRequest request) {
        DSGCLogInstance u = new DSGCLogInstance();
        u.setTrackId(reqJson.getString("trackId"));
        JSONObject result = logService.findLogByTraceId(u);
//        DSGCLogInstance log = this.logService.findLogById(u);
//        List<DSGCLogAudit> audits = this.logService.getAuditLog(u.getTrackId());
//        List<DSGCLogOutBound> logOutBounds = this.logService.getStackLog(u.getTrackId());
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("logInstance", log);
//        if(audits !=null && audits.size() >0){
//            jsonObject.put("auditLogs", audits);
//        }
//        if(logOutBounds !=null && logOutBounds.size() >0){
//            jsonObject.put("trackLogs", logOutBounds);
//        }
//
//        String jsonStr = jsonObject.toString();

//        ResultVO resultVo = new ResultVO();
//        resultVo.setData(jsonObject);
        return Response.ok().setData(result);
    }


    @RequestMapping(value = "/findLogById", method = RequestMethod.POST)
    public void findLogById(@RequestBody DSGCLogInstance u, HttpServletResponse response) {
        DSGCLogInstance log = this.logService.findLogById(u);
        List<DSGCLogAudit> audits = this.logService.getAuditLog(u.getTrackId());
        List<DSGCLogOutBound> logOutBounds = this.logService.getStackLog(u.getTrackId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logInstance", log);
        if (audits != null && audits.size() > 0) {
            jsonObject.put("auditLogs", audits);
        }
        if (logOutBounds != null && logOutBounds.size() > 0) {
            jsonObject.put("trackLogs", logOutBounds);
        }

//        System.out.println(jsonObject);
//        return Response.ok().data(jsonObject);

        JSONObject res = new JSONObject();
        res.put("data", jsonObject);
        res.put("code", "ok");
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(res);

        } catch (IOException e) {
            e.printStackTrace();
            res.put("code", "error");
            res.put("message", e.getMessage());
        } finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "/getLogPartition", method = RequestMethod.GET)
    public Response getLogPartition() {
        List<String> list = this.logService.getLogPartition();
        return Response.ok().data(list);
    }

    //    @RequestMapping(value = "/doRetry", method = RequestMethod.POST)
//    public Response doRetry(HttpServletRequest request) {
//        String body = CommonUtils.charReader(request);
//        if ("".equals(body)) {
//            throw new MpaasBusinessException("请求数据为空");
//        }
//        JSONArray js = JSONArray.parseArray(body);
////        System.out.println(js);
//        this.logService.doRetry(js);
//        return Response.ok().data("");
//    }
    @RequestMapping(value = "/doRetry", method = RequestMethod.POST)
    public Response doRetry(@RequestBody LogRetryReqDTO param, HttpServletRequest request) {
        String uid = request.getHeader("uid");
        this.logService.doRetry(uid, param, request);
        return Response.ok();
    }

    @RequestMapping(value = "/getHeaderPayload", method = RequestMethod.GET)
    public Response getHeaderPayload(@RequestParam("trackId") String trackId,
                                     @RequestParam("ibLob") String ibLob,
                                     @RequestParam("envCode") String envCode,
                                     String startTime,
                                     String endTime) {
        try {
            String switchUrl = this.logService.getSwitchUrl(envCode);
            if (switchUrl != null) {
                String res = HttpReqUtil.getObject(switchUrl + "/dsgc/logInstance/getHeaderPayload?trackId=" + trackId + "&ibLob=" + ibLob + "&envCode=" + envCode + "&startTime=" + startTime + "&endTime=" + endTime, String.class);
                return Response.ok().data(res);
            }

            String str = logService.getHeaderPayload(trackId, ibLob);
            if (str != null) {
                if (str.trim().length() == 0) {
                    Response.ok().data("无报文");
                } else {
                    str = str.replaceAll(" ", "");
                    if (!str.contains("<")) {
                        str = MsgCompressUtil.deCompress(str);
                    }
                    Response.ok().data(str);
                }
            } else {
                Response.ok().data("无报文");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().data("无报文");
    }

    @RequestMapping(value = "/getBodyPayload", method = RequestMethod.GET)
    public Response getBodyPayload(@RequestParam("ibLob") String ibLob,
                                   @RequestParam("servNo") String servNo,
                                   @RequestParam("trackId") String trackId,
                                   @RequestParam("envCode") String envCode,
                                   @RequestParam("startTime") String startTime,
                                   @RequestParam("endTime") String endTime,
                                   @RequestParam(value = "jobId", required = false, defaultValue = "undefined") String jobId,
                                   @RequestParam(value = "bodyType", required = false, defaultValue = "undefined") String bodyType) {
        try {
            String switchUrl = this.logService.getSwitchUrl(envCode);
            if (switchUrl != null) {
                String res = HttpReqUtil.getObject(switchUrl + "/dsgc/logInstance/getBodyPayload?trackId=" + trackId + "&servNo=" + servNo + "&ibLob=" + ibLob + "&startTime=" + startTime + "&endTime=" + endTime + "&jobId=" + jobId + "&envCode=" + envCode + "&bodyType=" + bodyType, String.class);
                return Response.ok().data(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"undefined".equals(jobId)) {
            try {
                String str = "";
                if ("req".equals(bodyType)) {
                    str = logService.getReqBodyRetry(jobId);
                } else if ("res".equals(bodyType)) {
                    str = logService.getResBodyRetry(jobId);
                }
                if (str != null) {
                    if (str.trim().length() == 0 || str.trim().equals("{NullPayload}")) {
                        return Response.ok().data("无报文");
                    } else {
                        str.replaceAll(" ", "");
                        return Response.ok().data(str);
                    }

                } else {
                    return Response.ok().data("无报文");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DSGCLogInstance detailsInterfaceData = logService.findLogById(trackId);
        String type = detailsInterfaceData.getPlStoreType();
        if ("DB".equals(type)) {
            try {
                String str = logService.getBodyPayload(ibLob);
                if (str != null) {
                    if (str.trim().length() == 0 || str.trim().equals("{NullPayload}")) {
                        return Response.ok().data("无报文");
                    } else {
                        str = str.replaceAll(" ", "");
                        if (!str.contains("<")) {
                            str = MsgCompressUtil.deCompress(str);
                        }
                        if ("{NullPayload}".equals(str.trim())) {
                            return Response.ok().data("无报文");
                        }
                        return Response.ok().data(str);
                    }

                } else {
                    return Response.ok().data("无报文");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("BOTH".equals(type)) {
            try {
                String str = logService.getBodyPayload(ibLob);
                if (str != null) {
                    if (!str.contains("<")) {
                        str = MsgCompressUtil.deCompress(str);
                    }
                    return Response.ok().data(str);
                } else {
                    String path = logService.dealPath(startTime, servNo, ibLob);
                    String fileContent = logService.readFileByLines(path);
                    if (fileContent != null && !"error".equals(fileContent)) {
                        if (!fileContent.contains("<")) {
                            str = MsgCompressUtil.deCompress(fileContent);
                        }
                        if ("{NullPayload}".equals(str.trim())) {
                            return Response.ok().data("无报文");
                        }
                        return Response.ok().data(str);
                    } else {
                        return Response.ok().data("无报文");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("FILE".equals(type)) {
            try {
                String path = logService.dealPath(startTime, servNo, ibLob);
                String fileContent = logService.readFileByLines(path);
                if (fileContent != null && !"error".equals(fileContent)) {
                    if (!fileContent.contains("<")) {
                        fileContent = MsgCompressUtil.deCompress(fileContent);
                    }
                    return Response.ok().data(fileContent);
                } else {
                    return Response.ok().data("无报文");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return Response.ok().data("无报文");
        }
        return Response.ok().data("无报文");
    }


    @RequestMapping(value = "/getErrorPayload", method = RequestMethod.GET)
    @ResponseBody
    public Response getErrorPayload(String errLob, HttpServletResponse response) {
        String res = "";
        try {
            response.setContentType("text/xml;charset=UTF-8");
            res = logService.getErrMsg(errLob);

        } catch (Exception e) {
            e.printStackTrace();
            res = "无异常";
        }
        return Response.ok().data(res);
    }

    @RequestMapping(value = "/getAllSystemCodeAndName", method = RequestMethod.GET)
    public Response getAllSystemCodeAndName() {
        List<DSGCSystemEntities> systemEntitiesList = logService.getAllSystemCodeAndName();

        return Response.ok().data(systemEntitiesList.size() > 0 ? systemEntitiesList : null);
    }

    @RequestMapping(value = "/getRetryDetial", method = {RequestMethod.POST, RequestMethod.GET})
    public Response getRetryDetial(@RequestBody TempQueryLogCondition tempQueryLogCondition,
                                   HttpServletRequest request) {
        try {
            return logService.getRetryDetial(tempQueryLogCondition, request);
        } catch (JSONException jex) {
            jex.printStackTrace();
            return Response.error(jex.getMessage());
        } catch (HttpClientErrorException hcex) {
            hcex.printStackTrace();
            return Response.error(hcex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return Response.error(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询数据失败！");
        }

    }
}
