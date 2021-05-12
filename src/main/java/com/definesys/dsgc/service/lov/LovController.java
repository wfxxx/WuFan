package com.definesys.dsgc.service.lov;

import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(description = "下拉框数据获取api", tags = "下拉框")
@RequestMapping(value = "/dsgc/lov")
@RestController
public class LovController {

    @Autowired
    private LovService lovService;


    @RequestMapping(value = "/getLovListByLKT", method = RequestMethod.GET)
    public Response getLovListByLookupType(@RequestParam(value = "lookupType", required = true, defaultValue = "") String lookupType) {
        return Response.ok().setData(this.lovService.getLovListByLookupType(lookupType));
    }

    @RequestMapping(value = "/getAllSystemLov", method = RequestMethod.GET)
    public Response getAllSystemLov() {
        return Response.ok().setData(this.lovService.getAllSystemLov());
    }

    @RequestMapping(value = "/getAuthRangeSystemLov", method = RequestMethod.GET)
    public Response getAuthRangeSystemLov(HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return Response.ok().setData(this.lovService.getAuthRangeSystemLov(userId));
    }
}
