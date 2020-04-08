package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.CommonReqBean;
import com.definesys.dsgc.service.apibs.bean.pluginBean.*;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName pluginController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-24 10:06
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "dsgc/plugin")
public class pluginController {

    @Autowired
        PluginService pluginService;

    @RequestMapping(value = "/addPlBasicAuthBean",method = RequestMethod.POST)
    public Response addPlBasicAuthBean(@RequestBody PlBasicAuthBean plBasicAuthBean){
        try {
//            String dupId=plBasicAuthBean.getVid();
//            PlBasicAuthBean value=pluginService.queryPlBasicAuthBeanById(dupId);
//            if(value!=null&&value.getBaId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlBasicAuthBean(plBasicAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlBasicAuthBeanById",method = RequestMethod.POST)
    public Response queryPlBasicAuthBeanById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlBasicAuthBean result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlBasicAuthBeanById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updatePlBasicAuthBean",method = RequestMethod.POST)
    public Response queryPlugin(@RequestBody PlBasicAuthBean plBasicAuthBean){
        try {
            pluginService.updatePlBasicAuthBean(plBasicAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delPlBasicAuthBeanById",method = RequestMethod.POST)
    public Response delPlBasicAuthBeanById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlBasicAuthBeanById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //PlKeyAuthBeann
    @RequestMapping(value = "/addPlKeyAuthBean",method = RequestMethod.POST)
    public Response addPlKeyAuthBean(@RequestBody PlKeyAuthBean plKeyAuthBean){
        try {
//            String dupId=plKeyAuthBean.getVid();
//            PlKeyAuthBean value=pluginService.queryPlKeyAuthBeannById(dupId);
//            if(value!=null&&value.getKaId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlKeyAuthBean(plKeyAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlKeyAuthBeannById",method = RequestMethod.POST)
    public Response queryPlKeyAuthBeannById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlKeyAuthBean result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlKeyAuthBeannById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updatePlKeyAuthBeanBean",method = RequestMethod.POST)
    public Response updatePlKeyAuthBeanBean(@RequestBody PlKeyAuthBean plKeyAuthBean){
        try {
            pluginService.updatePlKeyAuthBeanBean(plKeyAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delPlKeyAuthBeanById",method = RequestMethod.POST)
    public Response delPlKeyAuthBeanById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlKeyAuthBeanById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //    PlOauth2;  3
    @RequestMapping(value = "/addPlOauth2",method = RequestMethod.POST)
    public Response addPlOauth2(@RequestBody PlOauth2 plOauth2){
        try {
//            String dupId=plOauth2.getVid();
//            PlOauth2 value=pluginService.queryPlOauth2nById(dupId);
//            if(value!=null&&value.getPoId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlOauth2(plOauth2);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlOauth2nById",method = RequestMethod.POST)
    public Response queryPlOauth2nById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlOauth2 result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlOauth2nById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updatePlOauth2",method = RequestMethod.POST)
    public Response updatePlOauth2(@RequestBody PlOauth2 plOauth2){
        try {
            pluginService.updatePlOauth2(plOauth2);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }
    @RequestMapping(value = "/delPlOauth2ById",method = RequestMethod.POST)
    public Response delPlOauth2ById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlOauth2ById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }
//    PlAddAcl;  4

    @RequestMapping(value = "/addPlAddAcl",method = RequestMethod.POST)
    public Response addPlAddAcl(@RequestBody PlAddAcl plAddAcl){
        try {
//            String dupId=plAddAcl.getVid();
//            PlAddAcl value=pluginService.queryPlAddAclById(dupId);
//            if(value!=null&&value.getAclId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlAddAcl(plAddAcl);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlAddAcl",method = RequestMethod.POST)
    public Response updatePlAddAcl(@RequestBody PlAddAcl plAddAcl){
        try {
            pluginService.updatePlAddAcl(plAddAcl);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlAddAclById",method = RequestMethod.POST)
    public Response queryPlAddAclById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlAddAcl result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlAddAclById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlAddAclById",method = RequestMethod.POST)
    public Response delPlAddAclById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlAddAclById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //    PlIpRestriction;  5

    @RequestMapping(value = "/addPlIpRestriction",method = RequestMethod.POST)
    public Response addPlIpRestriction(@RequestBody PlIpRestriction plIpRestriction){
        try {
//            String dupId=plIpRestriction.getVid();
//            PlIpRestriction value=pluginService.queryPlIpRestrictionById(dupId);
//            if(value!=null&&value.getIrId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlIpRestriction(plIpRestriction);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlIpRestriction",method = RequestMethod.POST)
    public Response updatePlIpRestriction(@RequestBody PlIpRestriction plIpRestriction){
        try {
            pluginService.updatePlIpRestriction(plIpRestriction);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlIpRestrictionById",method = RequestMethod.POST)
    public Response queryPlIpRestrictionById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlIpRestriction result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlIpRestrictionById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlIpRestrictionById",method = RequestMethod.POST)
    public Response delPlIpRestrictionById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlIpRestrictionById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //   PlRateLimiting; 6
    @RequestMapping(value = "/addPlRateLimiting",method = RequestMethod.POST)
    public Response addPlRateLimiting(@RequestBody PlRateLimiting plRateLimiting){
        try {
//            String dupId=plRateLimiting.getVid();
//            PlRateLimiting value=pluginService.queryPlRateLimitingById(dupId);
//            if(value!=null&&value.getPrlId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlRateLimiting(plRateLimiting);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlRateLimiting",method = RequestMethod.POST)
    public Response updatePlRateLimiting(@RequestBody PlRateLimiting plRateLimiting){
        try {
            pluginService.updatePlRateLimiting(plRateLimiting);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlRateLimitingById",method = RequestMethod.POST)
    public Response queryPlRateLimitingById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlRateLimiting result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlRateLimitingById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlRateLimitingById",method = RequestMethod.POST)
    public Response delPlRateLimitingById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlRateLimitingById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //   PlReqSizeLimiting; 7
    @RequestMapping(value = "/addPlReqSizeLimiting",method = RequestMethod.POST)
    public Response addPlReqSizeLimiting(@RequestBody PlReqSizeLimiting plReqSizeLimiting){
        try {
//            String dupId=plReqSizeLimiting.getVid();
//            PlReqSizeLimiting value=pluginService.queryPlReqSizeLimitingById(dupId);
//            if(value!=null&&value.getRslId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlReqSizeLimiting(plReqSizeLimiting);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlReqSizeLimitingl",method = RequestMethod.POST)
    public Response updatePlReqSizeLimitingl(@RequestBody PlReqSizeLimiting plReqSizeLimiting){
        try {
            pluginService.updatePlReqSizeLimitingl(plReqSizeLimiting);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlReqSizeLimitingById",method = RequestMethod.POST)
    public Response queryPlReqSizeLimitingById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlReqSizeLimiting result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlReqSizeLimitingById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlReqSizeLimitingById",method = RequestMethod.POST)
    public Response delPlReqSizeLimitingById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlReqSizeLimitingById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //    PlReqTrans;  8
    @RequestMapping(value = "/addPlReqTrans",method = RequestMethod.POST)
    public Response addPlReqTrans(@RequestBody PlReqTrans plReqTrans){
        try {
//            String dupId=plReqTrans.getVid();
//            PlReqTrans value=pluginService.queryPlReqTransById(dupId);
//            if(value!=null&&value.getPrtId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlReqTrans(plReqTrans);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlReqTrans",method = RequestMethod.POST)
    public Response updatePlReqTrans(@RequestBody PlReqTrans plReqTrans){
        try {
            pluginService.updatePlReqTrans(plReqTrans);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlReqTransById",method = RequestMethod.POST)
    public Response queryPlReqTransById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlReqTrans result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlReqTransById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlReqTransById",method = RequestMethod.POST)
    public Response delPlReqTransById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlReqTransById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //     PlResTrans;  9

    @RequestMapping(value = "/addPlResTrans",method = RequestMethod.POST)
    public Response addPlResTrans(@RequestBody PlResTrans plResTrans){
        try {
//            String dupId=plResTrans.getVid();
//            PlResTrans value=pluginService.queryPlResTransById(dupId);
//            if(value!=null&&value.getPrtId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlResTrans(plResTrans);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlResTrans",method = RequestMethod.POST)
    public Response updatePlResTrans(@RequestBody PlResTrans plResTrans){
        try {
            pluginService.updatePlResTrans(plResTrans);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlResTransById",method = RequestMethod.POST)
    public Response queryPlResTransById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlResTrans result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlResTransById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlResTransById",method = RequestMethod.POST)
    public Response delPlResTransById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlResTransById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }
    //     PlCorrelationId;  10

    @RequestMapping(value = "/addPlCorrelationId",method = RequestMethod.POST)
    public Response addPlCorrelationId(@RequestBody PlCorrelationId plCorrelationId){
        try {
//            String dupId=plCorrelationId.getVid();
//            PlCorrelationId value=pluginService.queryPlCorrelationIdById(dupId);
//            if(value!=null&&value.getClId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlCorrelationId(plCorrelationId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlCorrelationId",method = RequestMethod.POST)
    public Response updatePlCorrelationId(@RequestBody PlCorrelationId plCorrelationId){
        try {
            pluginService.updatePlCorrelationId(plCorrelationId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlCorrelationIdById",method = RequestMethod.POST)
    public Response queryPlCorrelationIdById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlCorrelationId result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlCorrelationIdById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlCorrelationIdById",method = RequestMethod.POST)
    public Response delPlCorrelationIdById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlCorrelationIdById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }
    //     PluginTcpLog;   11
    @RequestMapping(value = "/addPluginTcpLog",method = RequestMethod.POST)
    public Response addPluginTcpLog(@RequestBody PluginTcpLog pluginTcpLog){
        try {
//            String dupId=pluginTcpLog.getVid();
//            PluginTcpLog value=pluginService.queryPluginTcpLogById(dupId);
//            if(value!=null&&value.getTlId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPluginTcpLog(pluginTcpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePluginTcpLog",method = RequestMethod.POST)
    public Response updatePluginTcpLog(@RequestBody PluginTcpLog pluginTcpLog){
        try {
            pluginService.updatePluginTcpLog(pluginTcpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPluginTcpLogById",method = RequestMethod.POST)
    public Response queryPluginTcpLogById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PluginTcpLog result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPluginTcpLogById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPluginTcpLog",method = RequestMethod.POST)
    public Response delPluginTcpLog(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPluginTcpLog(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }
    //     PlUdpLog;  12
    @RequestMapping(value = "/addplUdpLog",method = RequestMethod.POST)
    public Response addplUdpLog(@RequestBody PlUdpLog plUdpLog){
        try {
//            String dupId=plUdpLog.getVid();
//            PlUdpLog value=pluginService.queryplUdpLogById(dupId);
//            if(value!=null&&value.getUlId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addplUdpLog(plUdpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updateplUdpLog",method = RequestMethod.POST)
    public Response updateplUdpLog(@RequestBody PlUdpLog plUdpLog){
        try {
            pluginService.updateplUdpLog(plUdpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryplUdpLogById",method = RequestMethod.POST)
    public Response queryplUdpLogById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlUdpLog result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryplUdpLogById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delplUdpLogById",method = RequestMethod.POST)
    public Response delplUdpLogById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delplUdpLogById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //     PlHttpLog;  13

    @RequestMapping(value = "/addPlHttpLog",method = RequestMethod.POST)
    public Response addPlHttpLog(@RequestBody PlHttpLog plHttpLog){
        try {
//            String dupId=plHttpLog.getVid();
//            PlHttpLog value=pluginService.queryPlHttpLogById(dupId);
//            if(value!=null&&value.getHlId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
           pluginService.addPlHttpLog(plHttpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlHttpLog",method = RequestMethod.POST)
    public Response updatePlHttpLog(@RequestBody PlHttpLog plHttpLog){
        try {
            pluginService.updatePlHttpLog(plHttpLog);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlHttpLogById",method = RequestMethod.POST)
    public Response queryPlHttpLogById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlHttpLog result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlHttpLogById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlHttpLogById",method = RequestMethod.POST)
    public Response delPlHttpLogById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlHttpLogById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

    //     PlJwt;  14

    @RequestMapping(value = "/addPlJwt",method = RequestMethod.POST)
    public Response addPlJwt(@RequestBody PlJwt plJwt){
        try {
//            String dupId=plHttpLog.getVid();
//            PlHttpLog value=pluginService.queryPlHttpLogById(dupId);
//            if(value!=null&&value.getHlId()!=null){
//                return Response.error("服务违反插件唯一性约束");
//            }
            pluginService.addPlJwt(plJwt);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updatePlJwt",method = RequestMethod.POST)
    public Response updatePlJwt(@RequestBody PlJwt plJwt){
        try {
            pluginService.updatePlJwt(plJwt);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlJwtById",method = RequestMethod.POST)
    public Response queryPlJwtById(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlJwt result =null;
        try {
            String dupId=param.getCon0();
            result=pluginService.queryPlJwtById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlJwtById",method = RequestMethod.POST)
    public Response delPlJwtById(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String dupId=param.getCon0();
            pluginService.delPlJwtById(dupId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

}


