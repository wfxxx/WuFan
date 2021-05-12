package com.definesys.dsgc.service.role;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.definesys.dsgc.service.role.bean.DSGCRoleControl;
import com.definesys.dsgc.service.role.bean.DSGCRoleControlVO;
import com.definesys.dsgc.service.role.DSGCRoleControlService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@AuthAspect(menuCode = "role-control",menuName = "角色控制")
@RequestMapping(value = "/dsgc/roleControl")
@RestController
@Api(description = "角色控制接口", tags = "系统模块")
public class DSGCRoleControlController {
@Autowired
    DSGCRoleControlService roleControlService;
    @Autowired
    private SWordLogger logger;

//    @OperationAspect(value = "角色控制--新增角色")
    @ApiOperation(value = "新增角色控制接口", notes = "新增角色控制接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleControl", value = "角色控制对象", dataType = "DSGRoleControl")
    })
    @RequestMapping(value = "/addRoleControl", method = RequestMethod.POST)
    public Response addRoleControl(@RequestBody DSGCRoleControl roleControl) {
       this.roleControlService.addRoleControl(roleControl);
        return Response.ok().data("");
    }

    @ApiOperation(value = "查询角色控制信息", notes = "根据条件，查询符合条件的角色控制对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "r", value = "角色控制对象", dataType = "DSGCRoleControl"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int")
    })
    @RequestMapping(value = "/checkRoleControl", method = RequestMethod.POST)
    public Response checkRoleControl( @RequestBody DSGCRoleControl r){
    Integer num = this.roleControlService.checkRoleControl(r);
     return new Response().data(num);
    }

    @RequestMapping(value = "/queryRoleControl", method = RequestMethod.POST)
    public Response queryRoleControl(HttpServletRequest request,
                                     @RequestBody DSGCRoleControl r,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        this.logger.debug("pageSize " + pageSize);
        this.logger.debug("pageIndex " + pageIndex);
        this.logger.debug("role " + r);
            PageQueryResult<DSGCRoleControlVO> roleControl = this.roleControlService.queryRoleControl(r, pageSize, pageIndex);
            this.logger.debug("getResult " + roleControl.getResult());
            return Response.ok().data(roleControl);

    }
    @RequestMapping(value = "/queryRoleJurisdiction", method = RequestMethod.POST)
    public Response queryRoleJurisdiction(HttpServletRequest request,
                                          @RequestBody DSGCRoleControl r) {
        this.logger.debug("role " + r);
        List<DSGCRoleControl> roleControl = this.roleControlService.queryRoleJurisdiction(r);
        return Response.ok().data(roleControl);

    }

//    @OperationAspect(value = "角色控制--修改角色")
    @RequestMapping(value = "/updateRoleControl", method = RequestMethod.POST)
    public Response updateRoleControl(@RequestBody DSGCRoleControl roleControl) {
      String roleId =  this.roleControlService.updateRoleControl(roleControl);
        return Response.ok().data(roleId);
    }

//    @OperationAspect(value = "角色控制--删除角色")
    @RequestMapping(value = "/removeRoleControl", method = RequestMethod.POST)
    public Response removeRoleControl(@RequestBody DSGCRoleControl roleControl) {
        System.out.println(roleControl.getRoleId());
         String roleId =  this.roleControlService.removeRoleControl(roleControl);
         return Response.ok().data(roleId);
    }
}
