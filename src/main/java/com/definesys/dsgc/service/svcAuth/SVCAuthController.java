package com.definesys.dsgc.service.svcAuth;


import com.definesys.dsgc.service.apiauth.bean.CommonReqBean;
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
    public Response querySystem(HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        return Response.ok().setData(svcAuthService.querySystem(userId,userRole)) ;
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
     * @Description 发起申请权限流程时，添加业务数据
     * @Author Xueyunlong
     * @Date 2020-2-18 13:26
     * @Version 1.0
     **/
    @RequestMapping(value = "/applyServAuthPro",method = RequestMethod.POST)
    public Response applyServAuthPro(@RequestParam(value = "instanceId") String id,@RequestBody ApplyAuthProBean applyAuthProBean){
        return Response.ok().setMessage( svcAuthService.applyServAuthPro(id,applyAuthProBean));
    }



    /**
     * @Description 获取申请权限流程业务信息
     * @Author Xueyunlong
     * @Date 2020-2-18 13:26
     * @Version 1.0
     **/
    @RequestMapping(value = "/getProcessBusinessInfo",method = RequestMethod.GET)
    public Response getProcessBusinessInfo(@RequestParam(value = "instanceId") String instanceId){
        return Response.ok().setData( svcAuthService.getProcessBusinessInfo(instanceId));
    }

    /**
     * @Description 删除申请权限流程业务信息
     * @Author Xueyunlong
     * @Date 2020-2-18 13:26
     * @Version 1.0
     **/
    @RequestMapping(value = "/delProcessBusinessInfo",method = RequestMethod.GET)
    public Response delProcessBusinessInfo(@RequestParam(value = "instanceId") String instanceId){
        try{
            svcAuthService.delProcessBusinessInfo(instanceId);
        }catch (Exception e){
            return Response.error("权限赋予失败");
        }
        return Response.ok();
    }

    /**
     * @Description 流程结束，赋予权限
     * @Author Xueyunlong
     * @Date 2020-2-18 13:26
     * @Version 1.0
     **/
    @RequestMapping(value = "/authSevPro",method = RequestMethod.GET)
    public Response authSevPro(@RequestParam(value = "instId") String instanceId, HttpServletRequest request){
        String userName = request.getHeader("userName");
        try{
            svcAuthService.authSevPro(instanceId,userName);
        }catch (Exception e){
            return Response.error("权限赋予失败");
        }
        return Response.ok();
    }

    /**
     * Ser检查是否存在授权消费者
     * @param param
     * @return
     */
    @RequestMapping(value = "/checkSerAuthIsExist",method = RequestMethod.POST)
    public Response checkSerAuthIsExist(@RequestBody CommonReqBean param, HttpServletRequest request){
        return Response.ok().setData(svcAuthService.checkSerAuthIsExist(param.getApiCode(),param.getSelectSystemList()));
    }
    @RequestMapping(value="/saveIpRuleConfig",method = RequestMethod.POST)
    public Response saveIpRuleConfig(@RequestBody IPRuleConfigVO ipRuleConfigVO){
        try {
            svcAuthService.saveIpRuleConfig(ipRuleConfigVO);
            return Response.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("保存ip规则失败，请稍后再试！");
        }
    }
    @RequestMapping(value = "/queryIpRuleConfig",method = RequestMethod.GET)
    public Response queryIpRuleConfig(@RequestParam String limitType,@RequestParam String limitTarget){
        try{
            IPRuleConfigVO ipRuleConfigVO = svcAuthService.queryIpRuleConfig(limitType,limitTarget);
            return Response.ok().setData(ipRuleConfigVO);
        }catch (Exception e){
            return Response.error("查询ip黑白名单失败！");
        }
    }
}
