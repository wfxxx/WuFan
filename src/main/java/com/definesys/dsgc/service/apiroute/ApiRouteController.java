package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @RequestMapping(value = "/queryRouteDetail",method = RequestMethod.POST)
    public Response queryRouteDetail(@RequestBody CommonReqBean param){
        DagRoutesBean dagRoutesBean = null;
        try {
            dagRoutesBean =  apiRouteService.queryRouteDetail(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询路由数据失败");
        }
        return Response.ok().setData(dagRoutesBean);
    }
    @RequestMapping(value = "/queryApiEnv",method = RequestMethod.GET)
    public Response queryApiEnv(){
        List<DagEnvInfoCfgDTO> result = null;
        try {
            result =  apiRouteService.queryApiEnv();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询环境信息失败");
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryApiCodeVersion",method = RequestMethod.POST)
    public Response queryApiCodeVersion(@RequestBody CommonReqBean param){
        List<DagCodeVersionDTO> result = null;
        try {
            result =  apiRouteService.queryApiCodeVersion(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询配置信息失败");
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryRouteConfigList",method = RequestMethod.POST)
    public Response queryRouteConfigList(@RequestBody CommonReqBean param){
        List<RouteConfigDTO> result = null;
        try {
            result =  apiRouteService.queryRouteConfigList(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询配置信息失败");
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/addRouteConfig",method = RequestMethod.POST)
    public Response addRouteConfig(@RequestBody AddRouteConfigVO param){
        try {
            apiRouteService.addRouteConfig(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增路由配置失败");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/updateRoutePathStrip",method = RequestMethod.POST)
    public Response updateRoutePathStrip(@RequestBody DagRoutesBean param){
        try {
            apiRouteService.updateRoutePathStrip(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新数据失败！");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/updateRouteDesc",method = RequestMethod.POST)
    public Response updateRouteDesc(@RequestBody DagRoutesBean param){
        try {
            apiRouteService.updateRouteDesc(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新数据失败！");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delRouteConfig",method = RequestMethod.POST)
    public Response delRouteConfig(@RequestBody CommonReqBean param) {
        try {
            apiRouteService.delRouteConfig(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除配置项失败！");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/queryCodeVersionById",method = RequestMethod.POST)
    public Response queryCodeVersionById(@RequestBody CommonReqBean param){
        DagCodeVersionDTO dto =null;
        try {
            dto =  apiRouteService.queryCodeVersionById(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询配置项失败！");
        }
        return Response.ok().setData(dto);
    }
}
