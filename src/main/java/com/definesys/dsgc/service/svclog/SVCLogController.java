package com.definesys.dsgc.service.svclog;

import com.definesys.dsgc.service.svclog.bean.*;
import com.definesys.dsgc.service.svclog.bean.DSGCValidResutl;
import com.definesys.dsgc.service.svcmng.bean.DSGCServInterfaceNode;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(description = "服务日志相关API", tags = "服务日志")
@RequestMapping(value = "/dsgc/svclog")
@RestController
public class SVCLogController {
    @Autowired
    private SVCLogService sls;

    @RequestMapping(value = "/querySvcLogRecordListByCon", method = RequestMethod.POST)
    public Response querySvcLogRecordListByCon(@RequestBody SVCLogQueryBean q,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                               @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        return Response.ok().setData(this.sls.querySvcLogRecordListByCon(q,pageSize,pageIndex,userRole,userId));
    }
    @RequestMapping(value = "/updateServSaveTypeById", method = RequestMethod.POST)
    public Response updateServSaveTypeById(@RequestBody SVCLogListBean q, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
            this.sls.updateServSaveTypeById(q,userRole,userId);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok();
    }
    @RequestMapping(value = "/queryServCommonByCon", method = RequestMethod.POST)
    public Response queryServCommonByCon(@RequestBody SVCLogQueryBean q,
                                         HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        List<SVCCommonServResDTO> result = new ArrayList<>();
        try {
            result =  this.sls.queryServCommonByCon(q,userId,userRole);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/querySvcLogResultByCon", method = RequestMethod.POST)
    public Response querySvcLogResultByCon(@RequestBody SVCLogQueryBean q,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                         HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        PageQueryResult<SVCLogResultResDTO> result = new PageQueryResult<>();
        try {
            result =  this.sls.querySvcLogResultByCon(q,pageSize,pageIndex,userRole,userId);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/deleteResultObj", method = RequestMethod.POST)
    public Response deleteResultObj(@RequestBody DSGCValidResutl validResut,
                                    HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        int result =  this.sls.deleteResultObj(validResut,userRole,userId);
        if (result == -1){
           return Response.error("无权限操作");
        }else {
            return Response.ok();
        }

    }
    @RequestMapping(value = "/addResultObj", method = RequestMethod.POST)
    public Response addResultObj(@RequestBody DSGCValidResutl validResut,
                                    HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        int result =  this.sls.addResultObj(validResut,userRole,userId);
        if (result == -1){
            return Response.error("无权限操作");
        }else {
            return Response.ok();
        }

    }
    @RequestMapping(value = "/querySvcLogBizkeyByCon", method = RequestMethod.POST)
    public Response querySvcLogBizkeyByCon(@RequestBody SVCLogQueryBean q,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                           HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        PageQueryResult<SVCBizkeyResDTO> result = new PageQueryResult<>();
        try {
            result =  this.sls.querySvcLogBizkeyByCon(q,pageSize,pageIndex,userId,userRole);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/addBizkeyObj", method = RequestMethod.POST)
    public Response addBizkeyObj(@RequestBody SVCAddBizkeyVO svcAddBizkeyVO,
                                 HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        int result =  this.sls.addBizkeyObj(svcAddBizkeyVO,userRole,userId);
        if (result == -1){
            return Response.error("无权限操作");
        }else {
            return Response.ok();
        }

    }
    @RequestMapping(value = "/deleteBizkeyObj", method = RequestMethod.POST)
    public Response deleteBizkeyObj(@RequestBody SVCLogQueryBean param,
                                 HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        int result =  this.sls.deleteBizkeyObj(param,userRole,userId);
        if (result == -1){
            return Response.error("无权限操作");
        }else {
            return Response.ok();
        }

    }
    @RequestMapping(value = "/queryServbizkey", method = RequestMethod.POST)
    public Response queryServbizkey(@RequestBody SVCLogQueryBean param,
                                    HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        List<SVCKeywordResDTO> result =  this.sls.queryServBizkey(param,userRole,userId);
        if(result==null){
            return Response.error("您权限下无对应数据");
        }else{
            return Response.ok().setData(result);
        }
        }
    @RequestMapping(value = "/updateBizResolve", method = RequestMethod.POST)
    public Response updateBizResolve(@RequestBody UpdateBizResolveVO param,
                                    HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
            this.sls.updateBizResolve(param,userRole,userId);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok();
    }
    @RequestMapping(value="/queryEsbEnv",method = RequestMethod.GET)
    public Response queryEsbEnv(){
    // List<EsbEnvInfoCfgDTO>
        Map<String,Object> result = null;
        try {
            result  = sls.queryEsbEnv();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询环境信息失败");
        }
        return Response.ok().data(result);
    }
    @RequestMapping(value = "/getKeyword",method = RequestMethod.GET)
    public Response getKeyword(@RequestParam String servNo){
        List<DSGCServInterfaceNode> list = this.sls.getKeyword(servNo);
        return Response.ok().data(list);

    }
    @RequestMapping(value = "/addLogMark", method = RequestMethod.POST)
    public Response addLogMark(@RequestBody Map<String,Object> param,
                                     HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
            this.sls.addLogMark(param,request);
        } catch (Exception e) {
            return Response.error("添加标记失败，请联系管理员");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/onCloseTag", method = RequestMethod.POST)
    public Response onCloseTag(@RequestBody Map<String,Object> param,
                               HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        try {
            this.sls.onCloseTag(param,request);
        } catch (Exception e) {
            return Response.error("删除标记失败，请联系管理员");
        }
        return Response.ok();
    }
}
