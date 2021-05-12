package com.definesys.dsgc.service.system;


//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.definesys.dsgc.service.system.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhenglong
 * @Description: 当前系统所对应的controller
 * @Date 2019/3/12 14:24
 */
//@AuthAspect(menuCode = "systemManage",menuName = "系统管理")
@RestController
@RequestMapping("/dsgc/system")
public class DSGCSystemController {
    @Autowired
    private DSGCSystemService dsgcSystemService;
    @Autowired
    private SWordLogger logger;

   @ApiOperation(value = "查询所有", notes = "查询所有的系统信息")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "system", value = "系统信息对象", dataType="DSGCSystem")
   })
   @RequestMapping(value = "/findAll", method = RequestMethod.GET)
   public Response findSystem() {
       List<DSGCSystem> list = this.dsgcSystemService.findAll();
       this.logger.debug("list" +  list);

       return Response.ok().data(list);
   }
    @ApiOperation(value = "根据系统的code查询接口对应的信息", notes = "根据系统的code查询接口对应的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "systemAndInterface", value = "系统与接口对应数据对象", dataType="DSGCSytemAndInterface"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType="int"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType="int")
    })
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Response query(@RequestBody DSGCSystem systemAndInterface,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        this.logger.debug("pageSize " + pageSize);
        this.logger.debug("pageIndex " + pageIndex);
        this.logger.debug("systemAndInterface " + systemAndInterface);
        PageQueryResult<DSGCServRouting> system = this.dsgcSystemService.query(systemAndInterface, pageSize, pageIndex);
        return Response.ok().data(system);
    }

    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public Response query(@RequestBody DSGCSystemEntities systemEntities,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {

        PageQueryResult<DSGCSystemEntities> system = this.dsgcSystemService.query(systemEntities, pageIndex, pageSize);
        return Response.ok().data(system);
    }

//    @OperationAspect(value = "系统管理--删除系统基本信息")
    @RequestMapping(value = "/deleteDSGCSystemEntities", method = RequestMethod.POST)
    public Response deleteDSGCSystemEntities(@RequestBody DSGCSystemEntities systemEntities) {
        this.dsgcSystemService.deleteDSGCSystemEntities(systemEntities);
        return Response.ok();
    }
    @RequestMapping(value = "/findDSGCSystemEntitiesById", method = RequestMethod.POST)
    public Response findDSGCSystemEntitiesById(@RequestBody DSGCSystemEntities systemEntities) {
        DSGCSystemEntities dsgcSystemEntities = this.dsgcSystemService.findDSGCSystemEntities(systemEntities);
        return Response.ok().data(dsgcSystemEntities);
    }



    @RequestMapping(value = "/queryAllItems", method = RequestMethod.POST)
    public Response query(@RequestBody DSGCSystemItems t,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {

        PageQueryResult<DSGCSystemItems> system = this.dsgcSystemService.query(t, pageIndex, pageSize);
        return Response.ok().data(system);
    }


//    @OperationAspect(value = "系统管理--删除系统管理子项")
    @RequestMapping(value = "/deleteDSGCSystemItems", method = RequestMethod.POST)
    public Response deleteDSGCSystemItems(@RequestBody DSGCSystemItems t) {
        this.dsgcSystemService.deleteDSGCSystemItems(t);
        return Response.ok();
    }
    @RequestMapping(value = "/findDSGCSystemItems", method = RequestMethod.POST)
    public Response findDSGCSystemItems(@RequestBody DSGCSystemItems t) {
        DSGCSystemItems dsgcSystemItems = this.dsgcSystemService.findDSGCSystemItems(t);
        return Response.ok().data(dsgcSystemItems);
    }

//    @OperationAspect(value = "系统管理--新增或修改系统管理子项")
    @RequestMapping(value = "/modifyDSGCSystemItems", method = RequestMethod.POST)
    public Response modifyDSGCSystemEntities(@RequestBody DSGCSystemItems t) {
//        System.out.println(t);
        if(t.getId() != null && !"".equals( t.getId() )){
            this.dsgcSystemService.updateDSGCSystemItems(t);
        }else {
            this.dsgcSystemService.addDSGCSystemItems(t);
        }
        return Response.ok();
    }

    @RequestMapping(value = "/checkSysCode", method = RequestMethod.POST)
    public Response checkSysCode(@RequestBody DSGCSystemEntities systemEntities) {
        DSGCSystemEntities entities = this.dsgcSystemService.checkSysCode(systemEntities);
        return Response.ok().data(entities);
    }

//    @OperationAspect(value = "系统管理--新增系统服务配置项")
    @RequestMapping(value = "/addDSGCSystemAccess", method = RequestMethod.POST)
    public Response addDSGCSystemAccess(@RequestBody String body) {
       this.dsgcSystemService.addDSGCSystemAccess(body);
        return Response.ok();
    }

//    @OperationAspect(value = "系统管理--删除系统服务配置项")
    @RequestMapping(value = "/deleteDSGCSystemAccess", method = RequestMethod.POST)
    public Response deleteDSGCSystemAccess(@RequestBody DSGCSystemAccess t) {
        this.dsgcSystemService.deleteDSGCSystemAccess(t);
        return Response.ok();
    }

    @RequestMapping(value = "/findDSGCSystemAccess", method = RequestMethod.POST)
    public Response findDSGCSystemAccess(@RequestBody DSGCSystemAccess t) {
        List<DSGCSystemAccess> list = this.dsgcSystemService.findDSGCSystemAccess(t);
        return Response.ok().data(list);
    }

    @RequestMapping(value="/findSystemUserByUserId",method = RequestMethod.POST)
    public Response findSystemUserByUserId(@RequestBody String req,HttpServletRequest request){
//        JSONObject json = JSON.parseObject(req);
//        String userId = json.getString("userId");
        String userId = request.getHeader("uid");
        List<DSGCSystemUser> list = dsgcSystemService.findSystemUserByUserId(userId);
        return Response.ok().data(list);
    }

    /********************2020-11-28***start*******************************/

    /**
     * 查询所有的系统集合，传入attribute1标识是否为全部系统还是仅仅为已注册服务资产的所属系统
     *
     * @param systemEntities attribute1为ALL：全部系统/SVC：已注册服务资产的所属系统
     * @return
     */
    @RequestMapping(value = "/queryAllSystemList", method = RequestMethod.POST)
    public Response queryAllSystemList(@RequestBody DSGCSystemEntities systemEntities) {
        return Response.ok().data(this.dsgcSystemService.queryAllSystemList(systemEntities));
    }




    @RequestMapping(value = "/querySystemList", method = RequestMethod.POST)
    public Response querySystemList(@RequestBody QuerySystemParamBean paramBean) {
        return Response.ok().data(this.dsgcSystemService.querySystemList(paramBean));
    }
}
