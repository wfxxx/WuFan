package com.definesys.dsgc.service.cache;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.cache.bean.DSGCLogCacherefresh;
import com.definesys.dsgc.service.cache.bean.DSGCLogSysHeartbeat;
import com.definesys.dsgc.service.cache.DSGCCachesServices;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "caches",menuName = "缓存管理")
@RequestMapping(value = "/dsgc/caches")
@RestController
public class DSGCCachesController {

    @Autowired
    DSGCCachesServices dsgcCachesServices;

    @RequestMapping(value = "/getCachesServer", method = RequestMethod.GET)
    public Response getCaches(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                              @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex
    ) {
        List<DSGCLogSysHeartbeat> datas = this.dsgcCachesServices.getCaches();
        return Response.ok().data(datas);
    }

    @RequestMapping(value = "/getChildCacheServer", method = RequestMethod.GET)
    public Response getChildCaches(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                   String serverName) {
        List<DSGCLogCacherefresh> datas = this.dsgcCachesServices.getChildCaches(serverName);
        return Response.ok().data(datas);
    }

    //    @OperationAspect(value = "刷新服务器缓存")
    @RequestMapping(value = "/refreshCache", method = RequestMethod.GET)
    public Response refreshCache(HttpServletRequest request) {

        List<Map> res = dsgcCachesServices.refresh("ALL", "ALL");
        return Response.ok().data(res);
    }

    /**
     * Mule服务器刷新缓存
     * YStar 2020-11-28
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/refreshMuleCache", method = RequestMethod.GET)
    public Response refreshMuleCache(HttpServletRequest request) {
        String serverName = request.getParameter("serverName");
        String refreshItem = request.getParameter("refreshItem");
        System.out.println("缓存刷新-serverName->" + serverName + "-refreshItem->" + refreshItem);
        List<Map> res = dsgcCachesServices.refreshMuleCache(serverName, refreshItem);
        System.out.println("-result->" + res);
        return Response.ok().data(res);
    }
}
