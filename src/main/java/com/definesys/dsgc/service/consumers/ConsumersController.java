package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.consumers.bean.CommonReqBean;
import com.definesys.dsgc.service.consumers.bean.ConsumerBasicAuthUserDTO;
import com.definesys.dsgc.service.consumers.bean.ConsumerEntitieDTO;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "dsgc/entitys/consumers")
public class ConsumersController {

    @Autowired
    private ConsumersService consumersService;
    @RequestMapping(value = "queryconsumersList",method = RequestMethod.POST)
    public Response queryconsumersList(@RequestBody() CommonReqBean commonReqBean,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request, HttpServletResponse response){
        String userRole= request.getHeader("userRole");
        String userName = request.getHeader("userName");
        PageQueryResult<ConsumerEntitieDTO> result = consumersService.queryconsumersList(commonReqBean,pageSize,pageIndex,userName,userRole);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "addConsumer")
    public Response addConsumer(@RequestBody() DSGCConsumerEntities consumerEntities, HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        if("Tourist".equals(userRole) || "SystemLeader".equals(userRole)){
            return Response.error("无权限新增");
        }
        consumersService.addConsumer(consumerEntities);
        return Response.ok();
    }
    @RequestMapping(value = "delConsumer",method = RequestMethod.POST)
    public Response delConsumer(@RequestBody() CommonReqBean param, HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        if("Tourist".equals(userRole) || "SystemLeader".equals(userRole)){
            return Response.error("无权限新增");
        }
        consumersService.delConsumer(param.getCon0());
        return Response.ok();
    }
    @RequestMapping(value = "queryConsumerDataById")
    public Response queryConsumerDataById(@RequestParam String dceId){
        return Response.ok().setData(consumersService.queryConsumerDataById(dceId));
    }
    @RequestMapping(value = "saveConsumerData")
    public Response saveConsumerData(@RequestBody() ConsumerEntitieDTO consumerEntitieDTO,HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        String userId = request.getHeader("uid");
        if("Tourist".equals(userRole) || "SystemLeader".equals(userRole)){
            return Response.error("无权限新增");
        }
        consumersService.saveConsumerData(consumerEntitieDTO,userId);
        return Response.ok();
    }
    @RequestMapping(value = "queryConsumerBasicAuthData")
    public Response queryConsumerBasicAuthData(@RequestParam String dceId){
        return Response.ok().setData(consumersService.queryConsumerBasicAuthData(dceId));
    }
    @RequestMapping(value = "updateConsumerBasicAuthPwd",method = RequestMethod.POST)
    public Response updateConsumerBasicAuthPwd(@RequestBody() ConsumerBasicAuthUserDTO basicAuthUser){
        consumersService.updateConsumerBasicAuthPwd(basicAuthUser);
        return Response.ok();
    }
    @RequestMapping(value = "checkModifiable")
    public Response checkModifiable(HttpServletRequest request){
        String userRole= request.getHeader("userRole");
        if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
           return Response.ok().setData("Y");

        }else {
           return Response.ok().setData("N");
        }
    }
    @RequestMapping(value = "queryConsumersBaseInfoList")
    public Response queryConsumersBaseInfoList(){
        return Response.ok().setData(consumersService.queryConsumersBaseInfoList());
    }

    @RequestMapping(value = "queryConsumersListByUserId")
    public Response queryConsumersListByUserId(@RequestParam(value = "userId") String id){
        return Response.ok().setData(consumersService.queryConsumersListByUserId(id));
    }
}
