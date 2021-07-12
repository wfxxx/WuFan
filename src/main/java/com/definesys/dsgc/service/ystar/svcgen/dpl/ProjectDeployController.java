package com.definesys.dsgc.service.ystar.svcgen.dpl;

import com.definesys.dsgc.service.ystar.svcgen.bean.SvcgenServiceInfo;
import com.definesys.dsgc.service.ystar.svcgen.proj.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLog;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ProjectVersionController
 * @Description: 项目代码版本控制（Git）
 * @Author：afan
 * @Date : 2020/1/7 14:38
 */
@Api(description = "项目代码管理", tags = {"项目代码管理"})
@RestController
@RequestMapping("dsgc/project")
public class ProjectDeployController {

    @Autowired
    private ProjectDeployService projectDeployService;

    @RequestMapping(value = "/queryDeployLog", method = RequestMethod.POST)
    public Response queryDeployLog(@RequestBody SvcGenDeployLog service) {
        return projectDeployService.queryDeployLog();
    }

    @ApiOperation("查询部署日志")
    @RequestMapping(value = "/pageQueryDeployLog", method = RequestMethod.POST)
    public Response pageQueryDeployLog(@RequestBody SvcGenDeployLog service,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        return projectDeployService.pageQueryDeployLog(service, pageSize, pageIndex);
    }

    /*** ystar **/
    @ApiOperation("打包项目")
    @RequestMapping(value = "/packageMuleProject", method = RequestMethod.GET)
    public Response packageMuleProject(@RequestParam String proId) {
        return projectDeployService.packageMuleProject(proId);
    }

    /*** ystar **/
    @ApiOperation("部署/回退项目")
    @PostMapping(value = "/deployOrFallBackProject")
    public Response deployOrFallBackProject(@RequestBody SvcGenDeployLog svcGenDeployLog) {
        return projectDeployService.deployOrFallBackProject(svcGenDeployLog);
    }


    /*** ystar **/
    @ApiOperation("初始化项目")
    @RequestMapping(value = "/initProject", method = RequestMethod.GET)
    public Response initProject(@RequestParam String proId) {
        return projectDeployService.initProject(proId);
    }

    /*** ystar **/
    @ApiOperation("查询项目列表")
    @PostMapping("/getSvcGenProjectList")
    public Response getSvcGenProjectList(@RequestBody DSGCSysProfileDir project) {
        return Response.ok().data(projectDeployService.getSvcGenProjectList(project));
    }

    @ApiOperation("根据条件查询一条数据")
    @PostMapping(value = "/queryProjectFirstInfo")
    public Response queryProjectFirstInfo(@RequestBody DSGCSysProfileDir project) {
        return Response.ok().data(projectDeployService.queryProjectFirstInfo(project));
    }


}

