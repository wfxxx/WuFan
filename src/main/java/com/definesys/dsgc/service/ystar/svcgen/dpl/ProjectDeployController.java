package com.definesys.dsgc.service.ystar.svcgen.dpl;

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
    private ProjectDeployService projectVersionService;

    @ApiOperation("clone远程仓库代码")
    @PostMapping("/cloneProject")
    public Response cloneProject(@RequestBody Map<String, String> map) {
        return projectVersionService.cloneProject(map);
    }

    @ApiOperation("打包项目")
    @PostMapping("/packageProject")
    public Response packageProject() {
        return projectVersionService.packageProject();
    }


    @ApiOperation("项目部署和备份")
    @PostMapping("/deployProject")
    public Response deployProject(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String message = map.get("message");// 部署说明
        String curEnv = map.get("curEnv");//部署的当前环境
        System.out.println(curEnv);
        String userName = request.getHeader("userName"); //部署人
        return projectVersionService.deployProject(message, userName, curEnv);
    }

    @ApiOperation("代码提交")
    @PostMapping("/doCommitAndPush")
    public Response doCommitAndPush(@RequestBody Map<String, String> map) {
        return projectVersionService.doCommitAndPush(map.get("message"));
    }

    
    @RequestMapping(value = "/queryDeployLog", method = RequestMethod.POST)
    public Response queryDeployLog(@RequestBody SvcGenDeployLog service) {
        return projectVersionService.queryDeployLog();
    }

    @ApiOperation("查询部署日志")
    @RequestMapping(value = "/pageQueryDeployLog", method = RequestMethod.POST)
    public Response pageQueryDeployLog(@RequestBody SvcGenDeployLog service,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        return projectVersionService.pageQueryDeployLog(service, pageSize, pageIndex);
    }

    @ApiOperation("回退版本")
    @PostMapping("/revoke")
    public Response revokeProj(@RequestBody Map<String, String> map) {
        // 获取要回退的版本号
        String vId = map.get("vId");
        return projectVersionService.revokeProj(vId);
    }

    @ApiOperation("部署项目到指定服务器")
    @PostMapping("/syncProject")
    public Response syncProject(@RequestBody Map map) {
        List<Map<String, String>> servers = (List<Map<String, String>>) map.get("servers");

        return projectVersionService.syncProject(servers);

    }

    @ApiOperation("接收项目文件")
    @PostMapping(consumes = "multipart/form-data", value = "/receiveProj")
    public Response receiveProj(HttpServletRequest request) {
        projectVersionService.receiveProj(request);
        return Response.ok();
    }

    /*** ystar **/
    @ApiOperation("打包项目")
    @RequestMapping(value = "/packageMuleProject", method = RequestMethod.GET)
    public Response packageMuleProject(@RequestParam String proId) {
        return projectVersionService.packageMuleProject(proId);
    }

    /*** ystar **/
    @ApiOperation("部署/回退项目")
    @PostMapping(value = "/deployOrFallBackProject")
    public Response deployOrFallBackProject(@RequestBody SvcGenDeployLog svcGenDeployLog) {
        return projectVersionService.deployOrFallBackProject(svcGenDeployLog);
    }


    /*** ystar **/
    @ApiOperation("初始化项目")
    @RequestMapping(value = "/initProject", method = RequestMethod.GET)
    public Response initProject(@RequestParam String proId) {
        return projectVersionService.initProject(proId);
    }

    /*** ystar **/
    @ApiOperation("查询项目列表")
    @PostMapping("/getSvcGenProjectList")
    public Response getSvcGenProjectList(@RequestBody DSGCSysProfileDir project) {
        return Response.ok().data(projectVersionService.getSvcGenProjectList(project));
    }


}

