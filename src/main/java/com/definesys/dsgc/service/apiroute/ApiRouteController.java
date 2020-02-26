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
    @RequestMapping(value = "/updateRouteConfig",method = RequestMethod.POST)
    public Response updateRouteConfig(@RequestBody DagCodeVersionDTO param){
        try {
            apiRouteService.updateRouteConfig(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新路由配置失败");
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
        int result;
        try {
            result =  apiRouteService.delRouteConfig(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除配置项失败！");
        }
        if(result == -1){
            return Response.error("该配置已部署环境，不可删除！");
        }else {
            return Response.ok().setMessage("删除成功");
        }

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
    @RequestMapping(value = "/queryRoutePlug",method = RequestMethod.POST)
    public Response queryRoutePlug(@RequestBody CommonReqBean param,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        PageQueryResult<QueryRoutePlugDTO> pageQueryResult = null;
        try {
            pageQueryResult  = apiRouteService.queryRoutePlug(param,pageIndex,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件失败");
        }
        return Response.ok().setData(pageQueryResult);
    }
    @RequestMapping(value = "/queryRouteAnotherRule",method = RequestMethod.POST)
    public Response queryRouteAnotherRule(@RequestBody CommonReqBean param){
        List<QueryRouteHostnameDTO> result =null;
        try {
            result =  apiRouteService.queryRouteAnotherRule(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询路由其他规则失败！");
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/deployRoute",method = RequestMethod.POST)
    public Response deployRoute(@RequestBody DeployRouteVO param,HttpServletRequest request){
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        try { apiRouteService.deployRoute(param,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok();
    }
    @RequestMapping(value = "/copyRouteConfig",method = RequestMethod.POST)
    public Response copyRouteConfig(@RequestBody CommonReqBean param) {
        try {
            apiRouteService.copyRouteConfig(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("拷贝配置项失败！");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/addRouteDomain",method = RequestMethod.POST)
    public Response addRouteDomain(@RequestBody AddRouteDomainVO param) {
        try {
            apiRouteService.addRouteDomain(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增域名规则失败！");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delRouteDomain",method = RequestMethod.POST)
    public Response delRouteDomain(@RequestBody CommonReqBean param) {
        try {
            apiRouteService.delRouteDomain(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除域名规则失败！");
        }
        return Response.ok();
    }
}
