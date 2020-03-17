package com.definesys.dsgc.service.apiplugin;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.apiplugin.bean.DAGPluginListVO;
import com.definesys.dsgc.service.dagclient.bean.DAGDeployReqVO;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/apiplugin")
public class ApiPlugInController {
    @Autowired
    private ApiPlugInService apiPlugInService;


    @RequestMapping(value = "/queryPluginList", method = RequestMethod.POST)
    public Response queryPluginList(@RequestBody CommonReqBean param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = apiPlugInService.queryPluginList(param,userId,userRole, pageSize, pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取插件列表失败");
        }
        return Response.ok().setData(result);
    }
}
