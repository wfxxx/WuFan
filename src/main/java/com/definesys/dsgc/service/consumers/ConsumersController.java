package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.consumers.bean.*;
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

    @RequestMapping(value = "/queryconsumersList", method = RequestMethod.POST)
    public Response queryconsumersList(@RequestBody() CommonReqBean commonReqBean,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request, HttpServletResponse response) {
        String userRole = request.getHeader("userRole");
        String userName = request.getHeader("userName");
        PageQueryResult<ConsumerEntitieDTO> result = consumersService.queryconsumersList(commonReqBean, pageSize, pageIndex, userName, userRole);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "addConsumer")
    public Response addConsumer(@RequestBody() DSGCConsumerEntities consumerEntities, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole) || "SystemLeader".equals(userRole)) {
            return Response.error("无权限新增");
        }
        consumersService.addConsumer(consumerEntities);
        return Response.ok();
    }

    @RequestMapping(value = "delConsumer", method = RequestMethod.POST)
    public Response delConsumer(@RequestBody() CommonReqBean param, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole) || "SystemLeader".equals(userRole)) {
            return Response.error("无权限新增");
        }
        consumersService.delConsumer(param.getCon0());
        return Response.ok();
    }

    @RequestMapping(value = "queryConsumerDataById")
    public Response queryConsumerDataById(@RequestParam String dceId) {
        return Response.ok().setData(consumersService.queryConsumerDataById(dceId));
    }

    @RequestMapping(value = "saveConsumerData")
    public Response saveConsumerData(@RequestBody() ConsumerEntitieDTO consumerEntitieDTO, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        String userId = request.getHeader("uid");
        if ("Tourist".equals(userRole) || "SystemLeader".equals(userRole)) {
            return Response.error("无权限新增");
        }
        consumersService.saveConsumerData(consumerEntitieDTO, userId);
        return Response.ok();
    }

    @RequestMapping(value = "queryConsumerBasicAuthData")
    public Response queryConsumerBasicAuthData(@RequestParam String dceId) {
        return Response.ok().setData(consumersService.queryConsumerBasicAuthData(dceId));
    }

    @RequestMapping(value = "updateConsumerBasicAuthPwd", method = RequestMethod.POST)
    public Response updateConsumerBasicAuthPwd(@RequestBody() UpdateBasicPwdVO updateBasicPwdVO, HttpServletRequest request) {
        try {
            String userId = request.getHeader("uid");
            consumersService.updateConsumerBasicAuthPwd(updateBasicPwdVO, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增或更新basic认证密码失败！");
        }
        return Response.ok();
    }

    @RequestMapping(value = "checkModifiable")
    public Response checkModifiable(HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)) {
            return Response.ok().setData("Y");

        } else {
            return Response.ok().setData("N");
        }
    }

    @RequestMapping(value = "queryConsumersBaseInfoList")
    public Response queryConsumersBaseInfoList() {
        return Response.ok().setData(consumersService.queryConsumersBaseInfoList());
    }

    @RequestMapping(value = "queryConsumersListByUserId")
    public Response queryConsumersListByUserId(@RequestParam(value = "userId") String id) {
        return Response.ok().setData(consumersService.queryConsumersListByUserId(id));
    }

    @RequestMapping(value = "queryConsumerDeployData")
    public Response queryConsumerDeployData(@RequestBody() CommonReqBean param) {
        List<ConsumerDeployDTO> result = null;
        try {
            result = consumersService.queryConsumerDeployData(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询部署信息失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "consumerDeploy")
    public Response consumerDeploy(@RequestBody() ConsumerDeployChangeVO param, HttpServletRequest request) {
        try {
            String userId = request.getHeader("uid");
            consumersService.consumerDeploy(param, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("部署或取消部署失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "checkCsmCodeIsExsit")
    public Response checkCsmCodeIsExsit(@RequestBody() CommonReqBean param) {
        try {
            return Response.ok().setData(consumersService.checkCsmCodeIsExsit(param));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("检测消费者代码是否存在失败！");
        }
    }

    @RequestMapping(value = "checkCsmNameIsExsit")
    public Response checkCsmNameIsExsit(@RequestBody() CommonReqBean param) {
        try {
            return Response.ok().setData(consumersService.checkCsmNameIsExsit(param));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("检测消费者名称是否存在失败！");
        }
    }

    @RequestMapping(value = "createToken")
    public Response createToken(@RequestBody() CreateTokenVO param) {
        try {
            consumersService.createToken(param);
            return Response.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增token失败！");
        }
    }

    @RequestMapping(value = "createTokenByCode")
    public Response createTokenByCode(@RequestBody() CreateTokenVO param) {
        try {
            consumersService.createTokenByCode(param);
            return Response.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增token失败！");
        }
    }

    @RequestMapping(value = "queryTokenList")
    public Response queryTokenList(@RequestBody() CreateTokenVO param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageQueryResult<ConsumerTokenVO> result = consumersService.queryTokenList(param, pageIndex, pageSize);
            return Response.ok().setData(result);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询token失败！");
        }
    }

    @RequestMapping(value = "queryConsumerDeployEnv")
    public Response queryConsumerDeployEnv(@RequestBody() CommonReqBean param) {
        try {
            return Response.ok().setData(consumersService.queryConsumerDeployEnv(param.getCon0()));

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询token失败！");
        }
    }

}
