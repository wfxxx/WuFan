package com.definesys.dsgc.service.svcAuth;


import com.definesys.dsgc.service.svcAuth.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "dsgc/svcauth")
public class SVCAuthController {
    @Autowired
    private SVCAuthService svcAuthService;
    @RequestMapping(value = "/querySvcHis",method = RequestMethod.POST)
    public Response querySvcHis(@RequestBody SVCCommonReqBean param,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
       return Response.ok().setData(svcAuthService.querySvcHis(param,pageIndex,pageSize)) ;
    }
    @RequestMapping(value = "/querySvcAuth",method = RequestMethod.POST)
    public Response querySvcAuth(@RequestBody SVCCommonReqBean param,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole= request.getHeader("userRole");
        PageQueryResult<SVCAuthInfoListBean> result = new PageQueryResult<SVCAuthInfoListBean>();
        result = svcAuthService.querySvcAuth(param,pageIndex,pageSize,userId,userRole);
        return Response.ok().setData(result) ;
    }
    @RequestMapping(value = "/querySvcSystem",method = RequestMethod.GET)
    public Response querySystem(){
        return Response.ok().setData(svcAuthService.querySystem()) ;
    }

    @RequestMapping(value = "/querySvcAuthDetailBaseInfo",method = RequestMethod.GET)
    public Response querySvcAuthDetailBaseInfo(@RequestParam String servNo){
      return Response.ok().setData(svcAuthService.querySvcAuthDetailBaseInfo(servNo));
    }
    @RequestMapping(value = "/updateSvcAuthServShareType",method = RequestMethod.POST)
    public Response updateSvcAuthServShareType(@RequestBody SVCBaseInfoBean svcBaseInfoBean,HttpServletRequest request){
        String userName = request.getHeader("userName");
        svcAuthService.updateSvcAuthServShareType(svcBaseInfoBean,userName);
        return Response.ok();
    }
    @RequestMapping(value = "/queryServAuthSystemList",method = RequestMethod.POST)
    public Response queryServAuthSystemList(@RequestBody SVCBaseInfoBean svcBaseInfoBean,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){

        return Response.ok().setData(svcAuthService.queryServAuthSystemList(svcBaseInfoBean,pageIndex,pageSize));
    }
    @RequestMapping(value = "/queryServAuthConsumerList",method = RequestMethod.POST)
    public Response queryServAuthConsumerList(@RequestBody SVCBaseInfoBean svcBaseInfoBean,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){

        return Response.ok().setData(svcAuthService.queryServAuthConsumerList(svcBaseInfoBean,pageIndex,pageSize));
    }
    @RequestMapping(value = "/deleteServAuthSystem",method = RequestMethod.POST)
    public Response deleteServAuthSystem(@RequestBody SVCCommonReqBean param,HttpServletRequest request){
        String userName = request.getHeader("userName");
        svcAuthService.deleteServAuthSystem(param,userName);
        return Response.ok();
    }
    @RequestMapping(value = "/deleteServAuthConsumer",method = RequestMethod.POST)
    public Response deleteServAuthConsumer(@RequestBody SVCCommonReqBean param,HttpServletRequest request){
        String userName = request.getHeader("userName");
        svcAuthService.deleteServAuthConsumer(param,userName);
        return Response.ok();
    }
    @RequestMapping(value = "/addServAuthSytem",method = RequestMethod.POST)
    public Response addServAuthSytem(@RequestBody SVCAddAuthSystemReqBean param, HttpServletRequest request){
        String userName = request.getHeader("userName");
       String str = svcAuthService.addServAuthSytem(param,userName);
       if("-1".equals(str)){
           return Response.error("系统不存在，请联系管理员");
       }
        if("-2".equals(str)){
            return Response.error("该系统已授权，请勿重新添加");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/addServAuthConsumer",method = RequestMethod.POST)
    public Response addServAuthConsumer(@RequestBody SVCAddAuthSystemReqBean param, HttpServletRequest request){
        String userName = request.getHeader("userName");
        String str = svcAuthService.addServAuthConsumer(param,userName);
        if("-1".equals(str)){
            return Response.error("系统不存在，请联系管理员");
        }
        if("-2".equals(str)){
            return Response.error("该系统已授权，请勿重新添加");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/queryLookupValue")
    public Response queryLookupValue(@RequestParam String lookupType){
       return Response.ok().setData(svcAuthService.queryLookupValue(lookupType));
    }


    /**
     * @Description 申请权限流程
     * @Author Xueyunlong
     * @Date 2020-2-18 13:26
     * @Version 1.0
     **/
    @RequestMapping(value = "/applyServAuthPro",method = RequestMethod.POST)
    public Response applyServAuthPro(@RequestBody ApplyAuthProBean applyAuthProBean){

        return Response.ok().setMessage( svcAuthService.applyServAuthPro(applyAuthProBean));
    }


}
