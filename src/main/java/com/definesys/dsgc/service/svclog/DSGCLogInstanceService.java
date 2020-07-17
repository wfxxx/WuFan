package com.definesys.dsgc.service.svclog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilog.bean.ApiLogInstListDTO;
import com.definesys.dsgc.service.svclog.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;
import com.definesys.mpaas.common.exception.MpaasRuntimeException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("logService")
public class DSGCLogInstanceService {

    @Autowired
    DSGCLogInstanceDao dsgcLogInstanceDao;
    @Autowired
    SVCLogDao svcLogDao;

    @Autowired
    private UserHelper userHelper;



    public PageQueryResult<DSGCLogInstance> query(List<Object> keyword,String userRole, String uid, LogInstanceQueryDTO instance, int pageSize, int pageIndex) {
        try {
            PageQueryResult<DSGCLogInstance> dsgcLogInstance = new PageQueryResult<DSGCLogInstance>();
            // 根据用户名查询用户所管理的系统
            // 记录管理系统名称
            List<String> codeList = new ArrayList<>();
            // 记录接口名称
            List<String> servNoList = new ArrayList<>();
            if ("SuperAdministrators".equals(userRole)||"Administrators".equals(userRole)) { // 超管和管理员可以查看到所有的服务日志
            }
            //else if ("SystemLeader".equals(userRole)) { // 系统负责人只能查看自己负责的系统
            else{ //系统负责人和游客只能查看自己所属应用的服务
                // 根据uid可以查看到用户与系统的关系
                List<DSGCSystemUser> codes = dsgcLogInstanceDao.querySystemlistUserByUserId(uid);
                if(codes.size() < 1){
                   return dsgcLogInstance;
                }else{
                    for(DSGCSystemUser user : codes){
                        codeList.add(user.getSysCode());
                    }
                    // 根据系统查询接口名称
                    List<TopologyVO> servNoBySystem = dsgcLogInstanceDao.getServNoBySystem(codeList);
                    for (TopologyVO topologyVO: servNoBySystem) {
                        servNoList.add(topologyVO.getServNo());
                    }
                }
            }
//            else if ("Tourist".equals(userRole)) {
//                return dsgcLogInstance;
//            }
            dsgcLogInstance = this.dsgcLogInstanceDao.query(keyword,userRole ,uid, instance, pageSize, pageIndex,servNoList);
            return dsgcLogInstance;
        } catch (Exception e) {
            throw new MpaasRuntimeException(e);
        }
    }

//    public PageQueryResult<DSGCLogInstance> query1(String userRole, DSGCLogInstance instance, int pageSize, int pageIndex,String uId) {
//        //当前用户为系统管理员时，获取系统编码，作为查询筛选条件
//        DSGCSystemUser systemUser = projDirManagerDao.querySystemUserByUserId(uId);
//        try {
//
//            return this.dsgcLogInstanceDao.query(userRole,instance, pageSize, pageIndex, systemUser);
//        } catch (Exception e) {
//            throw new MpaasRuntimeException(e);
//        }
//    }

    public JSONObject findLogByTraceId(DSGCLogInstance u){
        DSGCLogInstance log = findLogById(u);
        List<DSGCLogAudit> audits = getAuditLog(u.getTrackId());
        List<DSGCLogOutBound> logOutBounds =getStackLog(u.getTrackId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logInstance", log);
        if(audits !=null && audits.size() >0){
            jsonObject.put("auditLogs", audits);
        }
        if(logOutBounds !=null && logOutBounds.size() >0){
            jsonObject.put("trackLogs", logOutBounds);
        }
        return jsonObject;
    }

    public DSGCLogInstance findLogById(DSGCLogInstance instance) {
        return this.dsgcLogInstanceDao.findLogById(instance);
    }

    public DSGCLogInstance findLogById(String trackId) {
        return this.dsgcLogInstanceDao.findLogById(trackId);
    }

    public List<String> getLogPartition() {
        return this.dsgcLogInstanceDao.getLogPartition();
    }

    public List<DSGCLogAudit> getAuditLog(String trackId) {
        return this.dsgcLogInstanceDao.getAuditLog(trackId);
    }

    public void doRetry(String uid,LogRetryReqDTO param,HttpServletRequest request) {
        UserHelper uh = this.userHelper.user(uid);
        if(uh.isSuperAdministrator() || uh.isAdmin() || uh.isSystemMaintainer()) {
            FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
            if(fndProperties == null){
                throw new RuntimeException("请配置当前环境代码！");
            }
            if ( fndProperties.getPropertyValue().equals(param.getEnvCode())) {
                this.dsgcLogInstanceDao.doRetry(uid,param);
            } else {
                List<DSGCEnvInfoCfg> envList = svcLogDao.queryEsbEnv();
                for(DSGCEnvInfoCfg env: envList){
                    if(env.getEnvCode().equals(param.getEnvCode())){
                        String retryPath =env.getDsgcAdmin();
                        retryPath += "/dsgc/logInstance/doRetry";
                        try {
                            HttpReqUtil.sendPostRequest(retryPath,JSONObject.parseObject(JSONObject.toJSONString(param)),request);
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
            }
        }
    }

    public List<DSGCLogOutBound> getStackLog(String trackId) {
        return this.dsgcLogInstanceDao.getStackLog(trackId);
    }

    public String getHeaderPayload(String trackId, String ibLob) {
        return this.dsgcLogInstanceDao.getHeaderPayload(trackId, ibLob);
    }

    public String getBodyPayload(String ibLob) {
        return this.dsgcLogInstanceDao.getBodyPayload(ibLob);
    }

    public void noPayload(HttpServletResponse response) {
        String s = "该报文不存在，请联系管理员";
        showData(response, s);
    }

    public void showData(HttpServletResponse response,String str) {
        if (str.startsWith("<")) {
            response.setContentType("text/xml;charset=UTF-8");
        } else {
            response.setContentType("text/plain;charset=UTF-8");
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(str);
        out.flush();
        out.close();
    }

    public String dealPath(String startTime, String servNo, String ibLob) {
        String t =startTime.trim();// StringUtils.substringBefore(startTime, " ");
        String folderName = t.replace("-", "");
        String folderName1 = folderName.substring(0, 6);

        String path = "/u01/osbLog/" + folderName1 + "/" + folderName + "/" + servNo + "/" + ibLob;
        System.out.println(path);
        return path;
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String tempString = "";
        String fileContent = "";
        if (file.exists()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    fileContent += tempString;
                    line++;
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e1) {
                    }
                }
            }
        } else {
            return "error";
        }
        return fileContent;
    }

    public String getErrMsg(String errLob) {
        return this.dsgcLogInstanceDao.getErrMsg(errLob);
    }

    public List<DSGCSystemEntities> getAllSystemCodeAndName() {
        return dsgcLogInstanceDao.getAllSystemCodeAndName();
    }

    public Response queryEsbServLogInst( TempQueryLogCondition tempQueryLogCondition,int pageSize,int pageIndex,String userRole,String userId, HttpServletRequest request)throws Exception{
        LogInstanceQueryDTO logInstance = tempQueryLogCondition.getLogInstance();
        List<Object> keyword = tempQueryLogCondition.getKeywordForm();
        if(StringUtil.isBlank(tempQueryLogCondition.getEnv())){
            throw new Exception("请求参数错误！");
        }
        FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
        if(fndProperties == null){
            throw new RuntimeException("请配置当前环境代码！");
        }
        List<DSGCEnvInfoCfg> envList = svcLogDao.queryEsbEnv();
        if ( fndProperties.getPropertyValue().equals(tempQueryLogCondition.getEnv())){
            PageQueryResult<DSGCLogInstance> result =  query(keyword,userRole,userId,logInstance,pageSize,pageIndex);
            return Response.ok().setData(result);
        }else {
            ResultVO<Response> resultvo = new ResultVO<>();
            for (int i = 0; i < envList.size(); i++) {
                if(tempQueryLogCondition.getEnv().equals(envList.get(i).getEnvCode())){
                    String logPath =envList.get(i).getDsgcAdmin();
                    logPath += "/dsgc/logInstance/HttpRestLogSwith?pageSize="+pageSize+"&pageIndex="+pageIndex;
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("keyword", JSON.toJSONString(keyword));
//                    jsonObject.put("logInstance",JSON.toJSONString(logInstance));
//                    jsonObject.put("pageSize",JSON.toJSONString(pageSize));
//                    jsonObject.put("pageIndex",JSON.toJSONString(pageIndex));
                    try {
                        String json =JSONObject.toJSONString(tempQueryLogCondition);
                        System.out.println("==================>"+json);
                        resultvo = HttpReqUtil.sendPostRequest(logPath,JSONObject.parseObject(JSONObject.toJSONString(tempQueryLogCondition)),request);

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
            return Response.ok().setData(resultvo.getData());
        }
    }


    public Response httpRestFindLogByIdSwitch( TempQueryLogCondition tempQueryLogCondition, HttpServletRequest request)throws Exception{
        String env = tempQueryLogCondition.getEnv();
        String trackId = tempQueryLogCondition.getTrackId();
        if(StringUtil.isBlank(tempQueryLogCondition.getEnv())){
            throw new Exception("请求参数错误！");
        }
        FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
        if(fndProperties == null){
            throw new Exception("请配置当前环境代码！");
        }
        List<DSGCEnvInfoCfg> envList = svcLogDao.queryEsbEnv();
        if ( fndProperties.getPropertyValue().equals(tempQueryLogCondition.getEnv())){
            DSGCLogInstance u = new DSGCLogInstance();
            u.setTrackId(trackId);
            JSONObject result =  findLogByTraceId(u);
            return Response.ok().setData(result);
        }else {
            ResultVO<Response> resultvo = new ResultVO<>();
            for (int i = 0; i < envList.size(); i++) {
                if(tempQueryLogCondition.getEnv().equals(envList.get(i).getEnvCode())){
                    String logPath =envList.get(i).getDsgcAdmin();
                    logPath += "/dsgc/logInstance/httpRestFindLogByIdSwitch";
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("trackId", trackId);
                    jsonObject.put("env",env);
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
            return Response.ok().setData(resultvo.getData());
        }
    }


    public String getSwitchUrl(String envCode) throws Exception{
        FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
        if(fndProperties == null){
            throw new Exception("请配置当前环境代码！");
        }

        if(fndProperties.getPropertyValue().equals(envCode)){
            return null;
        } else {
            List<DSGCEnvInfoCfg> envList = svcLogDao.queryEsbEnv();
            for(DSGCEnvInfoCfg env :envList){
                if(env.getEnvCode().equals(envCode)){
                    return env.getDsgcAdmin();
                }
            }
        }
        return null;
    }

    public Response getRetryDetial(TempQueryLogCondition tempQueryLogCondition,HttpServletRequest request)throws Exception{
        if(StringUtil.isBlank(tempQueryLogCondition.getEnv())){
            throw new Exception("请求参数错误！");
        }
        FndProperties fndProperties =dsgcLogInstanceDao.findFndPropertiesByKey("DSGC_CURRENT_ENV");
        if(fndProperties == null){
            throw new RuntimeException("请配置当前环境代码！");
        }
        List<DSGCEnvInfoCfg> envList = svcLogDao.queryEsbEnv();
        if ( fndProperties.getPropertyValue().equals(tempQueryLogCondition.getEnv())){
            List<RetryJobDTO> retryDetial = dsgcLogInstanceDao.getRetryDetial(tempQueryLogCondition.getTrackId());
            for (RetryJobDTO r : retryDetial) {
                Map<String, Object> userName = dsgcLogInstanceDao.getUserName(request.getHeader("uid"));
                if(userName!=null){
                    String name = userName.get("USER_NAME").toString();
                    r.setCreatedBy(name);
                }
            }
            return Response.ok().setData(retryDetial);
        }else {
            ResultVO<Response> resultvo = new ResultVO<>();
            for (int i = 0; i < envList.size(); i++) {
                if(tempQueryLogCondition.getEnv().equals(envList.get(i).getEnvCode())){
                    String logPath =envList.get(i).getDsgcAdmin();
                    logPath += "/dsgc/logInstance/getRetryDetial";
                    try {
                        String json =JSONObject.toJSONString(tempQueryLogCondition);
                        System.out.println("==================>"+json);
                        resultvo = HttpReqUtil.sendPostRequest(logPath,JSONObject.parseObject(JSONObject.toJSONString(tempQueryLogCondition)),request);

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
            return Response.ok().setData(resultvo.getData());
        }
    }

    public String getReqBodyRetry(String jobId) {
        return this.dsgcLogInstanceDao.getReqBodyRetry(jobId);
    }

    public String getResBodyRetry(String jobId) {
        return this.dsgcLogInstanceDao.getResBodyRetry(jobId);
    }

    public String getErrMsgRetry(String jobId){
        return this.dsgcLogInstanceDao.getErrMsgRetry((jobId));
    }
}
