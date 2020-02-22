package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.apilr.bean.DagLrbean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
}
