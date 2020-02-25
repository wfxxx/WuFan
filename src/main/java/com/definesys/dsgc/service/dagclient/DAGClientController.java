package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGDeployReqVO;
import com.definesys.dsgc.service.mynty.bean.MyNtyRulesBean;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(value = "/dsgc/dag")
@RestController
public class DAGClientController {


    @Autowired
    private DAGClientService dagClientService;

    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public Response updateMNRules(@RequestBody DAGDeployReqVO req,HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        String res = this.dagClientService.deploy(req,userId);
        if ("S".equals(res)) {
            return Response.ok().setMessage("部署成功！");
        } else {
            return Response.error(res);
        }
    }

}
