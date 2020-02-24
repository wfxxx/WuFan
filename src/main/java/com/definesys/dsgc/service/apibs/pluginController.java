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
            String vid=plBasicAuthBean.getVid();
            PlBasicAuthBean value=pluginService.queryPlBasicAuthBeanByVid(vid);
            if(value!=null&&value.getBaId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
            pluginService.addPlBasicAuthBean(plBasicAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlBasicAuthBeanByVid",method = RequestMethod.POST)
    public Response queryPlBasicAuthBeanByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlBasicAuthBean result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlBasicAuthBeanByVid(vid);
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
    @RequestMapping(value = "/delPlBasicAuthBeanByVId",method = RequestMethod.POST)
    public Response delPlBasicAuthBeanByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlBasicAuthBeanByVId(vid);
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
            String vid=plKeyAuthBean.getVid();
            PlKeyAuthBean value=pluginService.queryPlKeyAuthBeannByVid(vid);
            if(value!=null&&value.getKaId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
            pluginService.addPlKeyAuthBean(plKeyAuthBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlKeyAuthBeannByVid",method = RequestMethod.POST)
    public Response queryPlKeyAuthBeannByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlKeyAuthBean result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlKeyAuthBeannByVid(vid);
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
    @RequestMapping(value = "/delPlKeyAuthBeanByVId",method = RequestMethod.POST)
    public Response delPlKeyAuthBeanByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlKeyAuthBeanByVId(vid);
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
            String vid=plOauth2.getVid();
            PlOauth2 value=pluginService.queryPlOauth2nByVid(vid);
            if(value!=null&&value.getPoId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
            pluginService.addPlOauth2(plOauth2);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加插件错误!");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPlOauth2nByVid",method = RequestMethod.POST)
    public Response queryPlOauth2nByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlOauth2 result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlOauth2nByVid(vid);
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
    @RequestMapping(value = "/delPlOauth2ByVId",method = RequestMethod.POST)
    public Response delPlOauth2ByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlOauth2ByVId(vid);
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
            String vid=plAddAcl.getVid();
            PlAddAcl value=pluginService.queryPlAddAclByVid(vid);
            if(value!=null&&value.getAclId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlAddAclByVid",method = RequestMethod.POST)
    public Response queryPlAddAclByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlAddAcl result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlAddAclByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlAddAclByVId",method = RequestMethod.POST)
    public Response delPlAddAclByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlAddAclByVId(vid);
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
            String vid=plIpRestriction.getVid();
            PlIpRestriction value=pluginService.queryPlIpRestrictionByVid(vid);
            if(value!=null&&value.getIrId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlIpRestrictionByVid",method = RequestMethod.POST)
    public Response queryPlIpRestrictionByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlIpRestriction result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlIpRestrictionByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlIpRestrictionByVId",method = RequestMethod.POST)
    public Response delPlIpRestrictionByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlIpRestrictionByVId(vid);
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
            String vid=plRateLimiting.getVid();
            PlRateLimiting value=pluginService.queryPlRateLimitingByVid(vid);
            if(value!=null&&value.getPrlId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlRateLimitingByVid",method = RequestMethod.POST)
    public Response queryPlRateLimitingByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlRateLimiting result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlRateLimitingByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlRateLimitingByVId",method = RequestMethod.POST)
    public Response delPlRateLimitingByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlRateLimitingByVId(vid);
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
            String vid=plReqSizeLimiting.getVid();
            PlReqSizeLimiting value=pluginService.queryPlReqSizeLimitingByVid(vid);
            if(value!=null&&value.getRslId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlReqSizeLimitingByVid",method = RequestMethod.POST)
    public Response queryPlReqSizeLimitingByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlReqSizeLimiting result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlReqSizeLimitingByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlReqSizeLimitingByVId",method = RequestMethod.POST)
    public Response delPlReqSizeLimitingByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlReqSizeLimitingByVId(vid);
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
            String vid=plReqTrans.getVid();
            PlReqTrans value=pluginService.queryPlReqTransByVid(vid);
            if(value!=null&&value.getPrtId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlReqTransByVid",method = RequestMethod.POST)
    public Response queryPlReqTransByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlReqTrans result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlReqTransByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlReqTransByVId",method = RequestMethod.POST)
    public Response delPlReqTransByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlReqTransByVId(vid);
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
            String vid=plResTrans.getVid();
            PlResTrans value=pluginService.queryPlResTransByVid(vid);
            if(value!=null&&value.getPrtId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlResTransByVid",method = RequestMethod.POST)
    public Response queryPlResTransByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlResTrans result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlResTransByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlResTransByVId",method = RequestMethod.POST)
    public Response delPlResTransByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlResTransByVId(vid);
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
            String vid=plCorrelationId.getVid();
            PlCorrelationId value=pluginService.queryPlCorrelationIdByVid(vid);
            if(value!=null&&value.getClId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlCorrelationIdByVid",method = RequestMethod.POST)
    public Response queryPlCorrelationIdByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlCorrelationId result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlCorrelationIdByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlCorrelationIdByVId",method = RequestMethod.POST)
    public Response delPlCorrelationIdByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlCorrelationIdByVId(vid);
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
            String vid=pluginTcpLog.getVid();
            PluginTcpLog value=pluginService.queryPluginTcpLogByVid(vid);
            if(value!=null&&value.getTlId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPluginTcpLogByVid",method = RequestMethod.POST)
    public Response queryPluginTcpLogByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PluginTcpLog result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPluginTcpLogByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPluginTcpLog",method = RequestMethod.POST)
    public Response delPluginTcpLog(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPluginTcpLog(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }
    //     PlUdpLog;  12
    @RequestMapping(value = "/addPluginTcpLog",method = RequestMethod.POST)
    public Response addPluginTcpLog(@RequestBody PlUdpLog plUdpLog){
        try {
            String vid=plUdpLog.getVid();
            PlUdpLog value=pluginService.queryplUdpLogByVid(vid);
            if(value!=null&&value.getUlId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryplUdpLogByVid",method = RequestMethod.POST)
    public Response queryplUdpLogByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlUdpLog result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryplUdpLogByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delplUdpLogByVId",method = RequestMethod.POST)
    public Response delplUdpLogByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delplUdpLogByVId(vid);
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
            String vid=plHttpLog.getVid();
            PlHttpLog value=pluginService.queryPlHttpLogByVid(vid);
            if(value!=null&&value.getHlId()!=null){
                return Response.error("服务违反插件唯一性约束");
            }
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

    @RequestMapping(value = "/queryPlHttpLogByVid",method = RequestMethod.POST)
    public Response queryPlHttpLogByVid(@RequestBody CommonReqBean param, HttpServletRequest request){
        PlHttpLog result =null;
        try {
            String vid=param.getCon0();
            result=pluginService.queryPlHttpLogByVid(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询插件错误!");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/delPlHttpLogByVId",method = RequestMethod.POST)
    public Response delPlHttpLogByVId(@RequestBody CommonReqBean param, HttpServletRequest request){
        try {
            String vid=param.getCon0();
            pluginService.delPlHttpLogByVId(vid);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件错误!");
        }
        return Response.ok();
    }

}


