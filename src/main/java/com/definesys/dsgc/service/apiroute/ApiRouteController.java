package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.CommonReqBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/dsgc/apiRoute")
public class ApiRouteController {
    @Autowired
    private ApiRouteService apiRouteService;

    @RequestMapping(value = "/queryApiRouteList",method = RequestMethod.POST)
    public Response queryApiRouteList(@RequestBody CommonReqBean param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult pageQueryResult = apiRouteService.queryApiRouteList(param,userId,userRole,pageSize,pageIndex);
        return Response.ok().setData(pageQueryResult);
    }
    @RequestMapping(value = "/addApiRoute",method = RequestMethod.POST)
    public Response addApiRoute(@RequestBody DagRoutesBean dagRoutesBean){
        try {
            apiRouteService.addApiRoute(dagRoutesBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增路由发生错误");
        }
       return Response.ok();
    }
    @RequestMapping(value = "/delApiRoute",method = RequestMethod.POST)
    public Response delApiRoute(@RequestBody CommonReqBean param){
        try {
            apiRouteService.delApiRoute(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除路由发生错误");
        }
        return Response.ok();
    }
}
