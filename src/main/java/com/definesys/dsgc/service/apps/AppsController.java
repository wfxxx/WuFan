package com.definesys.dsgc.service.apps;

import com.definesys.dsgc.service.apps.bean.CommonReqBean;
import com.definesys.dsgc.service.apps.bean.SystemEntitireDTO;
import com.definesys.dsgc.service.apps.bean.UserResDTO;
import com.definesys.dsgc.service.apps.bean.UserRoleResDTO;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "dsgc/entitys/apps")
public class AppsController {

    @Autowired
    private AppsService appsService;
    @RequestMapping(value = "queryUserList",method = RequestMethod.POST)
    public Response queryUserList(@RequestBody() CommonReqBean commonReqBean,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,HttpServletRequest request){
        String userRole= request.getHeader("userRole");

          PageQueryResult<UserResDTO> result = appsService.queryUserList(commonReqBean,pageSize,pageIndex,userRole);

        return Response.ok().setData(result);
    }
    @RequestMapping(value = "queryUserRoleList")
    public Response queryUserRoleList(){
        List<UserRoleResDTO> result = appsService.queryUserRoleList();
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "queryAppsList",method = RequestMethod.POST)
    public Response queryAppsList(@RequestBody() CommonReqBean commonReqBean,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        String userName = request.getHeader("userName");
        PageQueryResult<DSGCSystemEntities> result = appsService.queryAllAppsList(commonReqBean,pageSize,pageIndex,userName,userRole);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "delApp",method = RequestMethod.POST)
    public Response delApp(@RequestBody() CommonReqBean param){
        appsService.delApp(param.getCon0());
        return Response.ok();
    }
    @RequestMapping(value = "queryAppDataById")
    public Response queryAppDataById(@RequestParam String id){
        return Response.ok().setData(appsService.queryAppDataById(id));
    }
    @RequestMapping(value = "queryUserListByUserIdArr")
    public Response queryUserListByUserIdArr(@RequestBody() Map<String,List<String>> userIdMap){
        System.out.println(userIdMap);
        List<String> userIdList = userIdMap.get("userIdMap");
        return Response.ok().setData(appsService.queryUserListByUserIdArr(userIdList));
    }
    @RequestMapping(value = "saveAppData")
    public Response saveAppData(@RequestBody() SystemEntitireDTO systemEntitireDTO,HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        String userId = request.getHeader("uid");
        if("Tourist".equals(userRole) || "SystemLeader".equals(userRole)){
            return Response.error("无权限新增");
        }
        appsService.saveAppData(systemEntitireDTO,userId);
        return Response.ok();
    }
}
