package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//@AuthAspect(menuCode = "svcinfo", menuName = "服务资产信息")
@Api(description = "服务资产信息相关api", tags = "服务资产信息")
@RequestMapping(value = "/dsgc/svcinfo")
@RestController
public class SVCInfoController {
    @Autowired
    private SVCInfoService svcInfoService;

    @RequestMapping(value = "/querySvcInfoListByCon", method = RequestMethod.POST)
    public Response querySvcInfoListByCon(@RequestBody SVCInfoQueryBean q,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return Response.ok().setData(this.svcInfoService.querySvcInfoListByCon(q,pageSize,pageIndex));
    }
}
