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
        List<Object> keyword = tempQueryLogCondition.getKeywordForm();
        String userRole = request.getHeader("userRole");
        String uid = request.getHeader("uid");
        PageQueryResult<DSGCLogInstance> datas = this.logService.query(keyword,userRole,uid,logInstance, pageSize, pageIndex);
        return Response.ok().data(datas);
    }

    /**
     * 查询日志实例--根据url，切换到不同的环境，查询不同环境的的日志实例
     * @param reqMap
     * @param request
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/queryLogInstanceBySwitchUrl", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultVO queryLogInstanceBySwitchUrl(@RequestBody com.alibaba.fastjson.JSONObject reqMap,HttpServletRequest request)  {
        ResultVO<PageQueryResult<DSGCLogInstance>> resultVO = new ResultVO<PageQueryResult<DSGCLogInstance>>();
        LogInstanceQueryDTO logInstance = JSON.parseObject(reqMap.getString("logInstance"),LogInstanceQueryDTO.class);
        List<Object> keyword = JSON.parseArray(reqMap.getString("keyword"));
        Integer pageSize = (Integer)JSON.parse(reqMap.getString("pageSize"));
        Integer pageIndex = (Integer)JSON.parse(reqMap.getString("pageIndex"));
        String payloadData = (String)JSON.parse(reqMap.getString("payloadData"));
//        String userName = (String)JSON.parse(reqMap.getString("userName"));

        String userRole = request.getHeader("userRole");
        String uid = request.getHeader("uid");
        PageQueryResult<DSGCLogInstance> datas = this.logService.query(keyword,userRole,uid,logInstance, pageSize, pageIndex);
        resultVO.setData(datas);
        return resultVO;
    }

    /**
     * 日志查询控制器，用于切换环境，查询不同环境下的日志实例
     * @return
     */
    @RequestMapping(value="/HttpRestLogSwith",method = {RequestMethod.POST, RequestMethod.GET})
    public Response HttpRestLogSwith(@RequestBody TempQueryLogCondition tempQueryLogCondition,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                     HttpServletRequest request){
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
             return logService.queryEsbServLogInst(tempQueryLogCondition,pageSize,pageIndex,userRole,userId,request);
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

//        DSGCLogInstance logInstance = tempQueryLogCondition.getLogInstance();
//        List<Object> keyword = tempQueryLogCondition.getKeywordForm();
//        String reqUrl = tempQueryLogCondition.getHttpReqUrl();
//        com.alibaba.fastjson.JSONObject reqMap = new com.alibaba.fastjson.JSONObject();
//        ResultVO<PageQueryResult<DSGCLogInstance>> resultvo = new ResultVO<>();
//        try{
//            reqMap.put("keyword", JSON.toJSONString(keyword));
//            reqMap.put("logInstance",JSON.toJSONString(logInstance));
//            reqMap.put("pageSize",JSON.toJSONString(pageSize));
//            reqMap.put("pageIndex",JSON.toJSONString(pageIndex));
//            resultvo = HttpReqUtil.sendPostRequest(reqUrl,reqMap,request);
//
//        }catch(JSONException jex){
//            jex.printStackTrace();
//            logger.error("%s", jex.getMessage());
//            return Response.error("参数解析异常！").setCode("error").setMessage("参数解析异常，请检查请求参数是否正确！");
//        }catch (HttpClientErrorException hcex){
//            hcex.printStackTrace();
//            logger.error("%s", hcex.getMessage());
//            return Response.error("404，请求的url不存在！").setCode("error").setMessage("404，请求的url不存在，请检查要访问的远程外部接口URL配置是否正确！");
//        }catch (IllegalArgumentException ex){
//            ex.printStackTrace();
//            logger.error("%s", ex.getMessage());
//            return Response.error("请求的uri不能为空！").setCode("error").setMessage("请求的uri不能为空！");
//        }
//        return Response.ok().data(resultvo.getData());
    }


    @RequestMapping(value="/httpRestFindLogByIdSwitch",method = RequestMethod.POST)
    public Response httpRestFindLogByIdSwitch(@RequestBody TempQueryLogCondition param,HttpServletRequest request,HttpServletResponse response){

        try {
            return logService.httpRestFindLogByIdSwitch(param,request);
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

    @RequestMapping(value="/findLogByTraceId",method = RequestMethod.POST)
    public Response findLogByTraceId (@RequestBody com.alibaba.fastjson.JSONObject reqJson,HttpServletRequest request)  {
        DSGCLogInstance u = new DSGCLogInstance();
        u.setTrackId(reqJson.getString("trackId"));
        JSONObject result =  logService.findLogByTraceId(u);
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
    public void findLogById(@RequestBody DSGCLogInstance u,HttpServletResponse response) {
        DSGCLogInstance log = this.logService.findLogById(u);
        List<DSGCLogAudit> audits = this.logService.getAuditLog(u.getTrackId());
        List<DSGCLogOutBound> logOutBounds = this.logService.getStackLog(u.getTrackId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logInstance", log);
        if(audits !=null && audits.size() >0){
            jsonObject.put("auditLogs", audits);
        }
        if(logOutBounds !=null && logOutBounds.size() >0){
            jsonObject.put("trackLogs", logOutBounds);
        }

//        System.out.println(jsonObject);
//        return Response.ok().data(jsonObject);

        JSONObject res = new JSONObject();
        res.put("data",jsonObject);
        res.put("code","ok");
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(res);

        } catch (IOException e) {
            e.printStackTrace();
            res.put("code","error");
            res.put("message",e.getMessage());
        }finally {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "/getLogPartition", method = RequestMethod.GET)
    public Response getLogPartition() {
        List<String> list = this.logService.getLogPartition();
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/doRetry", method = RequestMethod.POST)
    public Response doRetry(@RequestBody LogRetryReqDTO param, HttpServletRequest request) {
        String uid = request.getHeader("uid");
        this.logService.doRetry(uid,param,request);
        return Response.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/getHeaderPayload", method = RequestMethod.GET)
    public void getHeaderPayload(@RequestParam("trackId") String trackId,
                                 @RequestParam("ibLob") String ibLob,
                                 @RequestParam("envCode") String envCode,
                                 String startTime,
                                 String endTime,
                                 HttpServletResponse response) {
        try {
            response.setContentType("text/xml;charset=UTF-8");
            PrintWriter out = response.getWriter();
            if (trackId == null || "".equals(trackId)) {
                out.print("trackId 为空");
                out.flush();
                out.close();
                return;
            }
            if (ibLob == null || "".equals(ibLob)) {
                out.print("ibLob 为空");
                out.flush();
                out.close();
                return;
            }

            String switchUrl = this.logService.getSwitchUrl(envCode);
            if(switchUrl != null ){
                out.println(HttpReqUtil.getObject(switchUrl+"/dsgc/logInstance/getHeaderPayload?trackId="+trackId+"&ibLob="+ibLob+"&envCode="+envCode+"&startTime="+startTime+"&endTime="+endTime,String.class));
                out.flush();
                out.close();
                return;
            }

            String str = logService.getHeaderPayload(trackId, ibLob);
            if (str != null) {
                if (str.trim().length() == 0) {
                    logService.showData(response, "报文为空");
                }else{
                    str.replaceAll(" ", "");
                    if (str.contains("<")) {
                        out.println(str);
                    } else {
                        String s = MsgCompressUtil.deCompress(str);
                        out.println(s);
                    }
                    out.flush();
                    out.close();
                }
            } else {
                logService.noPayload(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getBodyPayload", method = RequestMethod.GET)
    public void getBodyPayload(@RequestParam("ibLob") String ibLob,
                               @RequestParam("servNo") String servNo,
                               @RequestParam("trackId") String trackId,
                               @RequestParam("envCode") String envCode,
                               @RequestParam("startTime") String startTime,
                               @RequestParam("endTime") String endTime,
                               HttpServletResponse response) {
        System.out.println(trackId + trackId.getClass());
//        if ("N/A".equals(servNo) || "n/a".equals(servNo) || "".equals(servNo)) {
//            logService.noPayload(response);
//            return;
//        }
        try {
            String switchUrl = this.logService.getSwitchUrl(envCode);
            if (switchUrl != null) {
                String res = HttpReqUtil.getObject(switchUrl + "/dsgc/logInstance/getBodyPayload?trackId=" + trackId + "&servNo="+ servNo + "&ibLob=" + ibLob + "&envCode=" + envCode + "&startTime=" + startTime + "&endTime=" + endTime,String.class);
                this.logService.showData(response,res);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        DSGCLogInstance detailsInterfaceData = logService.findLogById(trackId);
        String type = detailsInterfaceData.getPlStoreType();
//        detailsInterfaceData.
        System.out.println(type);
        if ("DB".equals(type)) {
            try {

                String str = logService.getBodyPayload(ibLob);

                if (str != null) {
                    if (str.trim().length() == 0) {
                        logService.showData(response, "报文为空");
                    } else {
                        str.replaceAll(" ", "");
                        System.out.println("|" + str + "|");
                        if (str.contains("<")) {
                            System.out.println("---xx---");
                            logService.showData(response, str);
                        } else {
//                     String s = MsgZLibUtil.decompress(str);
                            String s = MsgCompressUtil.deCompress(str);
                            logService.showData(response, s);
                        }
                    }

                } else {
                    logService.noPayload(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("BOTH".equals(type)) {
            try {
                String str = logService.getBodyPayload(ibLob);
                System.out.println(str);
                if (str != null) {
//                    if (str.indexOf("<?xml version=") != -1) {
                    if (str.contains("<")) {
                        logService.showData(response, str);
                    } else {
//                      String s = MsgZLibUtil.decompress(str);
                        String s = MsgCompressUtil.deCompress(str);
                        System.out.println(s);
                        logService.showData(response, s);
                    }
                } else {
                    String path = logService.dealPath(startTime, servNo, ibLob);
                    String fileContent = logService.readFileByLines(path);
                    if (fileContent != null && !"error".equals(fileContent)) {
//                        if (fileContent.indexOf("<?xml version=") != -1) {
                        if (fileContent.contains("<")) {
                            logService.showData(response, fileContent);
                        } else {
                            String s = MsgCompressUtil.deCompress(fileContent);
//                        String s = MsgZLibUtil.decompress(fileContent);
                            logService.showData(response, s);
                        }
                    } else {
                        logService.noPayload(response);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("FILE".equals(type)) {
            try {
                String path = logService.dealPath(startTime, servNo, ibLob);
                String fileContent = logService.readFileByLines(path);
                System.out.println(fileContent);
                if (fileContent != null && !"error".equals(fileContent)) {
//                    if (fileContent.indexOf("<?xml version=") != -1) {
                    if (fileContent.contains("<")) {
                        logService.showData(response, fileContent);
                    } else {
                        String s = MsgCompressUtil.deCompress(fileContent);
//                      String s = MsgZLibUtil.decompress(fileContent);
                        logService.showData(response, s);
                    }
                } else {
                    logService.noPayload(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logService.noPayload(response);
        }
    }

    @RequestMapping(value = "/getErrMsg", method = RequestMethod.GET)
    @ResponseBody
    public void getErrMsg(@RequestParam("errLob") String errLob,@RequestParam("envCode")String envCode, HttpServletResponse response) {
        try {
            String switchUrl = this.logService.getSwitchUrl(envCode);
            if (switchUrl != null) {
                String res = HttpReqUtil.getObject(switchUrl + "/dsgc/logInstance/getErrMsg?errLob=" + errLob+"&envCode=" + envCode,String.class);
                this.logService.showData(response,res);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            response.setContentType("text/xml;charset=UTF-8");
            String str = logService.getErrMsg(errLob);
            if (!str.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><payload>" + str + "</payload>";
            }
            logService.showData(response, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value="/getAllSystemCodeAndName",method = RequestMethod.GET)
    public Response getAllSystemCodeAndName(){
        List<DSGCSystemEntities> systemEntitiesList = logService.getAllSystemCodeAndName();

        return Response.ok().data(systemEntitiesList.size() > 0 ? systemEntitiesList : null);
    }
}
