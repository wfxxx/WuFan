package com.definesys.dsgc.service.ystar.token;

import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.ystar.token.bean.DSGCUserTokenBean;
import com.definesys.dsgc.service.ystar.token.bean.WorkWcTokenBean;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/dsgc/ystar/user")
@RestController
public class UserTokenController {

    @Autowired
    private UserTokenService userTokenService;


    @RequestMapping(value = "/getUserToken1", method = RequestMethod.POST)
    public Response getUserToken1(HttpServletRequest request) {
        String uid = request.getHeader("uid");
        System.out.println("uid->" + uid);
        return Response.ok().setData(uid);
    }

    @RequestMapping(value = "/getUserToken", method = RequestMethod.POST)
    public Response getUserToken(@RequestBody DSGCUser user) {
        return Response.ok().setData(userTokenService.getUserToken(user));
    }

    @RequestMapping(value = "/createTokenByUserId", method = RequestMethod.POST)
    public Response createTokenByUserId(@RequestBody DSGCUser user) {
        return Response.ok().setData(userTokenService.createTokenByUserId(user));
    }

    @RequestMapping(value = "/deleteInvalidToken", method = RequestMethod.POST)
    public Response deleteInvalidToken(@RequestBody DSGCUser user) {
        return Response.ok().setData(userTokenService.deleteInvalidToken(user));
    }

    @RequestMapping(value = "/checkTokenValid", method = RequestMethod.POST)
    public Response checkTokenValid(@RequestBody DSGCUserTokenBean tokenBean) {
        return Response.ok().setData(userTokenService.checkTokenValid(tokenBean));
    }

    @RequestMapping(value = "/queryWorkWxToken", method = RequestMethod.POST)
    public Response queryWorkWxToken(@RequestBody WorkWcTokenBean tokenBean) {
        return Response.ok().setData(userTokenService.queryWorkWxToken(tokenBean));
    }

    @RequestMapping(value = "/createWorkWxToken", method = RequestMethod.POST)
    public Response createWorkWxToken(@RequestBody WorkWcTokenBean tokenBean) {
        return Response.ok().setData(userTokenService.createWorkWxToken(tokenBean));
    }
}
