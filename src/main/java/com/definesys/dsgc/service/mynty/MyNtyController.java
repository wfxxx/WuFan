package com.definesys.dsgc.service.mynty;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;

import com.definesys.dsgc.service.mynty.bean.*;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "MyNotification",menuName = "我的通知")
@Api(description = "我的通知模块api", tags = "我的通知")
@RequestMapping(value = "/dsgc/mynty")
@RestController
public class MyNtyController {

    @Autowired
    private MyNtyService mns;


    @RequestMapping(value = "/queryMNRules", method = RequestMethod.POST)
    public Response queryMNRules(@RequestBody MyNtyQueryParamVO reqParam,HttpServletRequest request,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.mns.queryMNRules(uId,reqParam,pageSize,pageIndex));
    }


    @RequestMapping(value = "/cancelSubscribe", method = RequestMethod.POST)
    public Response cancelSubscribe(@RequestBody RuleStatSetVO reqParam,HttpServletRequest request) {
        String uId = request.getHeader("uid");
        this.mns.cancelSubscribe(uId,reqParam);
        return Response.ok();
    }

    @RequestMapping(value = "/setRuleStat", method = RequestMethod.POST)
    public Response setRuleStat(@RequestBody RuleStatSetVO reqParam,HttpServletRequest request) {
        String uId = request.getHeader("uid");
        String res = this.mns.setRuleStat(uId,reqParam);
        if ("S".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    @RequestMapping(value = "/delMNRule", method = RequestMethod.POST)
    public Response delMNRule(@RequestBody MyNtyRuleIdVO reqParam,HttpServletRequest request) {
        String uId = request.getHeader("uid");
        try {
            this.mns.delMNRule(uId,reqParam.getRuleId());
            return Response.ok();
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getMNRuleDetail", method = RequestMethod.POST)
    public Response getMNRuleDetail(@RequestBody MyNtyRuleIdVO reqParam,HttpServletRequest request){
       return Response.ok().setData(this.mns.getMNRuleDetail(reqParam.getRuleId()));
    }

    @RequestMapping(value = "/updateMNRuleDetail", method = RequestMethod.POST)
    public Response updateMNRuleDetail(@RequestBody MyNtyRuleDetailVO reqParam,HttpServletRequest request){
        String uId = request.getHeader("uid");
        try {
            return Response.ok().setData(this.mns.updateMNRuleDetail(uId,reqParam));
        }catch (Exception e){
            return Response.error(e.getMessage());
        }
    }



    /**
     * 获取规则列表
     *
     * @param ruleType
     * @param request
     * @return
     * @deprecated
     */
    @RequestMapping(value = "/getMNRules", method = RequestMethod.GET)
    public Response getMNRules(@RequestParam String ruleType,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }

        //验证ruleType
        if (ruleType == null || ruleType.trim().length() == 0) {
            return Response.error("无效的订阅规则类型！");
        }

        //根据用户id获取服务错误订阅规则列表
        return Response.ok().setData(mns.getMNRules(userId,ruleType));
    }


    /**
     * 更新我的通知订阅规则
     *
     * @param chgs
     * @param request
     */
    @RequestMapping(value = "/updMNRules", method = RequestMethod.POST)
    public Response updateMNRules(@RequestBody List<MyNtyRulesBean> chgs,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        mns.updateMNRules(userId,chgs);
        return Response.ok().setMessage("保存成功！");
    }

    /**
     * 获取用户服务异常通知规则
     *
     * @return
     */
    @RequestMapping(value = "/getServExcptSubRules", method = RequestMethod.GET)
    public Response getServExceptionSubRules(HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        //根据用户id获取服务错误订阅规则列表
        return Response.ok().setData(mns.getServExcptSubRules(userId));
    }


    /**
     * 更新服务异常通知订阅规则
     *
     * @param chgs
     * @param request
     * @return
     */
    @RequestMapping(value = "/updServExcptSubRules", method = RequestMethod.POST)
    public Response updateServExceptionSubRules(@RequestBody List<ServExcptSubRulesBean> chgs,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        mns.updateServExcptSubRules(userId,chgs);
        return Response.ok().setMessage("保存成功！");
    }

    /**
     * 获取规则订阅的服务
     *
     * @param sltReq
     * @param request
     * @return
     */
    @RequestMapping(value = "/getMNSubcributeServices", method = RequestMethod.POST)
    public Response getMNSubcributeServices(@RequestBody MyNtyServSltBean sltReq,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        return Response.ok().setData(mns.getMNSubscributedServList(sltReq));
    }

    /**
     * 保存用户指定的规则订阅服务
     *
     * @param sltReq
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveMNSubcributeServices", method = RequestMethod.POST)
    public Response saveMNSubcributeServices(@RequestBody MyNtyServSltBean sltReq,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        return Response.ok().setData(mns.saveMNSubcributeServList(sltReq));
    }


    @RequestMapping(value = "/findDSGCMnNotices", method = RequestMethod.POST)
    public Response findDSGCMnNotices(@RequestBody DSGCMnNotices dsgcMnNotices,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return Response.ok().data(this.mns.findDSGCMnNotices(dsgcMnNotices, pageSize, pageIndex));
    }

    @RequestMapping(value = "getServByUser", method = RequestMethod.POST)
    public Response getServByUser(@RequestBody DSGCUser dsgcUser) {
        List<Map<String,Object>> servByUser = mns.getServByUser(dsgcUser);
        return Response.ok().setData(servByUser);
    }

    @RequestMapping(value = "updateDSGCMnNoticesById", method = RequestMethod.POST)
    public Response updateDSGCMnNoticesById(@RequestBody DSGCMnNotices dsgcMnNotices) {
        mns.updateDSGCMnNoticesById(dsgcMnNotices);
        return Response.ok();
    }

    @RequestMapping(value = "/updateDSGCMnNotices", method = RequestMethod.POST)
    public Response updateDSGCMnNotices(@RequestBody DSGCMnNotices mnNotices) {
        this.mns.updateDSGCMnNotices(mnNotices);
        return Response.ok();
    }

    @RequestMapping(value = "/findDSGCMnNoticesByMnTitle", method = RequestMethod.POST)
    public Response findDSGCMnNoticesByMnTitle(@RequestBody DSGCMnNotices dsgcMnNotices) {
        List<DSGCMnNotices> dsgcMnNoticesList = this.mns.findDSGCMnNoticesByMnTitle(dsgcMnNotices);
        return Response.ok().data(dsgcMnNoticesList);
    }


    @RequestMapping(value = "/getMNSubUser", method = RequestMethod.POST)
    public Response getMNSubUser(@RequestBody MyNtyUserSltBean sltReq,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        return Response.ok().setData(mns.getMNSubUser(sltReq));
    }

    @RequestMapping(value = "/saveMNSubUser", method = RequestMethod.POST)
    public Response saveMNSubUser(@RequestBody MyNtyUserSltBean sltReq,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        return Response.ok().setData(mns.saveMNSubUser(sltReq));
    }

    @RequestMapping(value = "getUserList",method = RequestMethod.POST)
    public Response queryUserList(@RequestBody() CommonReqBean commonReqBean,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,HttpServletRequest request){
        String userRole= request.getHeader("userRole");

        PageQueryResult<UserResDTO> result = mns.queryUserList(commonReqBean,pageSize,pageIndex,userRole);

        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/getMNNoticeCount", method = RequestMethod.GET)
    public Response getMNNoticeCount(HttpServletRequest request){
        String userId = request.getHeader("uid");
        return Response.ok().setData(this.mns.getUserMNNoticesCount(userId));
    }


    @RequestMapping(value = "/pushNotice", method = RequestMethod.POST)
    public Response pushNotice(@RequestBody PushNoticeDTO notice) {
        boolean res = this.mns.pushNotice(notice);
        if (res) {
            PushNoticeResDTO r = new PushNoticeResDTO();
            r.setRtnCode("S");
            r.setRtnMsg("notice push successfully!");
            return Response.ok().setData(r);
        } else {
            PushNoticeResDTO r = new PushNoticeResDTO();
            r.setRtnCode("E");
            r.setRtnMsg("notice push failed! check the server log.");
            return Response.ok().setData(r);
        }
    }
}
