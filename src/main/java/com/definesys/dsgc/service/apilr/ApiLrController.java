package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/apiLr")
public class ApiLrController {
    @Autowired
    private ApiLrService apiLrService;

    @RequestMapping(value = "/queryApiLrList",method = RequestMethod.POST)
    public Response queryApiLrList(@RequestBody CommonReqBean param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = apiLrService.queryApiLrList(param,userId,userRole,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取服务列表失败");
        }

        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/addApiLr",method = RequestMethod.POST)
    public Response addApiLr(@RequestBody DagLrbean dagLrbean){
        try {
            apiLrService.addApiLr(dagLrbean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增服务发生错误");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delApiLr",method = RequestMethod.POST)
    public Response delApiLr(@RequestBody CommonReqBean param){
        try {
            apiLrService.delApiLr(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除服务发生错误");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryLrDetail",method = RequestMethod.POST)
    public Response queryLrDetail(@RequestBody CommonReqBean param){
        DagLrbean dagLrbean = null;
        try {
            dagLrbean =  apiLrService.queryLrDetail(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询数据失败");
        }
        return Response.ok().setData(dagLrbean);
    }

    @RequestMapping(value = "/queryLrConfigList",method = RequestMethod.POST)
    public Response queryLrConfigList(@RequestBody CommonReqBean param){
        List<LrConfigDTO> result = null;
        try {
            result =  apiLrService.queryLrConfigList(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询配置信息失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updateLrDesc",method = RequestMethod.POST)
    public Response updateLrDesc(@RequestBody DagLrbean param){
        try {
            apiLrService.updateLrDesc(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新数据失败！");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/addLrConfig",method = RequestMethod.POST)
    public Response addLrConfig(@RequestBody AddLrConfigVO param){
        try {
            apiLrService.addLrConfig(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加配置信息失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/deLrConfig",method = RequestMethod.POST)
    public Response deLrConfig(@RequestBody CommonReqBean param){
        try {
            apiLrService.deLrConfig(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除服务发生错误");
        }
        return Response.ok();
    }

}
