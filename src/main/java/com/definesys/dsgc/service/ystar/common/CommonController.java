package com.definesys.dsgc.service.ystar.common;

import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CommonController
 * @Description: TODO
 * @Author：ystar
 * @Date : 2020/12/24 19:00
 */
@RestController("CommonController")
@Api(value = "公共controller", tags = {"公共controller"})
@RequestMapping("/dsgc/ystar/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    //上下游系统名称
    @RequestMapping(value = "/getSystem", method = RequestMethod.GET)
    public Response getSystem() {
        Map<String, Object> result = new HashMap<>();
        List<DSGCSystemEntities> res1 = this.commonService.getOnSystem();
        List<DSGCConsumerEntities> res2 = this.commonService.getDownSystem();
        result.put("OnSystem", res1);
        result.put("DownSystem", res2);
        if (result != null) {
            return Response.ok().data(result);
        } else {
            return Response.error("无数据");
        }
    }

    @RequestMapping(value = "/QueryList", method = RequestMethod.GET)
    public Response QueryList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve) {
        String uid = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if (userRole.equals("SuperAdministrators") || userRole.equals("Administrators")) {
            PageQueryResult list = this.commonService.QueryList(page, pageSize, sysCode, csmCode, servNo, levelJ, toSolve);
            if (list == null) {
                return Response.error("集合为空").data(null);
            } else {
                return Response.ok().data(list);
            }
        } else {
            PageQueryResult list = this.commonService.QueryLists(page, pageSize, sysCode, csmCode, servNo, levelJ, toSolve, uid);
            if (list == null) {
                return Response.error("集合为空").data(null);
            } else {
                return Response.ok().data(list);
            }

        }
    }

    @RequestMapping(value = "/QueryMap", method = RequestMethod.GET)
    public Response QueryMap() {
        List<Map<String, Object>> map = this.commonService.QueryMap();
        if (map.isEmpty()) {
            return Response.error("无数据").data(map);
        } else {
            return Response.ok().data(map);
        }
    }

    @RequestMapping(value = "/Test", method = RequestMethod.GET)
    public Response Test() {
        String res = this.commonService.Test();
        return Response.ok().data(res);
    }
}
