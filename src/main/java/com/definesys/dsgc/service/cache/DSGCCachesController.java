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
    @RequestMapping(value = "/refreshCache", method = RequestMethod.POST)
    public Response refreshCache(HttpServletRequest request){

        String body = CommonUtils.charReader(request);
        if ("".equals(body)) {
            throw new MpaasBusinessException("请求数据为空");
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
        String propertyDescription = jsonObject.getString("propertyDescription");
        String serverName = jsonObject.getString("serverName");
        if(propertyDescription == null || propertyDescription.equals("")){
            throw new MpaasBusinessException("输入参数propertyDescription为空，请检查输入值");
        }
        if(serverName == null || serverName.equals("")){
            throw new MpaasBusinessException("serverName，请检查输入值");
        }
        List<Map> res = dsgcCachesServices.refresh(serverName,propertyDescription);
        return Response.ok().data(res);
//        return Response.error("后台提示错误");
    }
}
