package com.definesys.dsgc.service.envinfo;

import com.definesys.dsgc.service.envinfo.bean.CommonReqBean;
import com.definesys.dsgc.service.envinfo.bean.DagEnvNodeDTO;
import com.definesys.dsgc.service.envinfo.bean.DsgcEnvInfoCfgBean;
import com.definesys.dsgc.service.envinfo.bean.DagEnvInfoDTO;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/envinfo")
public class EnvInfoController {
    @Autowired
    private EnvInfoService envInfoService;

    @RequestMapping(value = "/addApiInfoCfg",method = RequestMethod.POST)
    public Response addApiInfoCfg(@RequestBody DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean){
        try {
            envInfoService.addApiInfoCfg(dsgcEnvInfoCfgBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加环境信息失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/addApiInfoNode",method = RequestMethod.POST)
    public Response addApiInfoNode(@RequestBody String params){
        try {
            envInfoService.addApiInfoNode(params);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加节点信息失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryApiEnvCfg",method = RequestMethod.POST)
    public Response queryApiEnvCfg (@RequestBody CommonReqBean q){
        DagEnvInfoDTO dagEnvInfoDTO = null;
        try {
            dagEnvInfoDTO = envInfoService.queryApiEnvCfg(q);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询环境信息失败");
        }
        return Response.ok().data(dagEnvInfoDTO);
    }

    @RequestMapping(value = "/queryApiEnvNode",method = RequestMethod.POST)
    public Response queryApiEnvNode (@RequestBody CommonReqBean q){
        List<DagEnvNodeDTO> result = null;
        try {
            result = envInfoService.queryApiEnvNode(q);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询节点信息失败");
        }
        return Response.ok().data(result);
    }

    @RequestMapping(value = "/updateApiInfoCfg",method = RequestMethod.POST)
    public Response updateApiInfoCfg(@RequestBody DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean){
        try {
            envInfoService.updateApiInfoCfg(dsgcEnvInfoCfgBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新环境信息发生错误");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryApiEnvCfgList",method = RequestMethod.POST)
    public Response queryApiEnvCfgList (){
        List<DagEnvInfoDTO> result = null;
        try {
            result = envInfoService.queryApiEnvCfgList();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询环境信息失败");
        }
        return Response.ok().data(result);
    }
}
