package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.CommonReqBean;
import com.definesys.dsgc.service.apibs.bean.DagBsbean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "dsgc/apibs")
public class ApiBsController {
    @Autowired
    private ApiBsService apiBsService;

    @RequestMapping(value = "/queryApiBsList",method = RequestMethod.POST)
    public Response queryApiBsList(@RequestBody CommonReqBean param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
           result = apiBsService.queryApiBsList(param,userId,userRole,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取服务列表失败");
        }

        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryApiBsByCustomInput",method = RequestMethod.POST)
    public Response queryApiBsByCustomInput(@RequestBody CommonReqBean param, HttpServletRequest request){
        List<String> result = new ArrayList<>();
        try {
            result = apiBsService.queryApiBsByCustomInput(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("内部错误!");
        }

        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/addApiBs",method = RequestMethod.POST)
    public Response addApiBs(@RequestBody DagBsbean dagBsbean){
        try {
            apiBsService.addApiBs(dagBsbean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增服务发生错误");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delApiBs",method = RequestMethod.POST)
    public Response delApiBs(@RequestBody CommonReqBean param){
        try {
            apiBsService.delApiBs(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除服务发生错误");
        }
        return Response.ok();
    }

}
