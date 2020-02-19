package com.definesys.dsgc.service.apiauth;

import com.definesys.dsgc.service.apiauth.bean.CommonReqBean;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "dsgc/apiauth")
public class ApiAuthController {
    @Autowired
    private ApiAuthService apiAuthService;

    @RequestMapping(value = "/queryApiAuthDetailBaseInfo",method = RequestMethod.GET)
    public Response queryApiAuthDetailBaseInfo(@RequestParam String apiCode){
        return Response.ok().setData(apiAuthService.queryApiAuthDetailBaseInfo(apiCode));
    }

    @RequestMapping(value = "/queryApiAuthConsumerList",method = RequestMethod.POST)
    public Response queryApiAuthConsumerList(@RequestBody CommonReqBean svcBaseInfoBean,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){

        return Response.ok().setData(apiAuthService.queryApiAuthConsumerList(svcBaseInfoBean,pageIndex,pageSize));
    }

    @RequestMapping(value = "/addApiAuthConsumer",method = RequestMethod.POST)
    public Response addApiAuthConsumer(@RequestBody CommonReqBean param, HttpServletRequest request){
        String userName = request.getHeader("userName");
        String str = apiAuthService.addApiAuthConsumer(param,userName);
        if("-1".equals(str)){
            return Response.error("系统不存在，请联系管理员");
        }
        if("-2".equals(str)){
            return Response.error("该系统已授权，请勿重新添加");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/deleteApiAuthConsumer",method = RequestMethod.POST)
    public Response deleteApiAuthConsumer(@RequestBody CommonReqBean param,HttpServletRequest request){
        String userName = request.getHeader("userName");
        apiAuthService.deleteApiAuthConsumer(param,userName);
        return Response.ok();
    }

    /**
     * API授权管理查询
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryApiAuthList",method = RequestMethod.POST)
    public Response queryApiAuthList(@RequestBody CommonReqBean param,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole= request.getHeader("userRole");
        return Response.ok().setData(apiAuthService.queryApiAuthList(param,pageIndex,pageSize,userId,userRole));
    }
}
