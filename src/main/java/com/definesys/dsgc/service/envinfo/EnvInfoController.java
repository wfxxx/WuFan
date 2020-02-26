package com.definesys.dsgc.service.envinfo;

import com.definesys.dsgc.service.envinfo.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/envinfo")
public class EnvInfoController {
    @Autowired
    private SWordLogger logger;
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

    @RequestMapping(value="/addOrUpdateEnvInfoCfg",method = RequestMethod.POST)
    public Response addOrUpdateEnvInfoCfg(@RequestBody DsgcEnvInfoCfgBean envInfoCfg){
        try{
            envInfoService.addOrUpdateEnvInfoCfg(envInfoCfg);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.error("保存总线配置信息失败！").setMessage("保存总线配置信息失败！");
        }

        return Response.ok().setMessage("保存总线配置信息成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value="/updateEnvCfg",method = RequestMethod.POST)
    public Response updateEnvCfg(@RequestBody CommonReqBean params){
        try{
            envInfoService.addOrUpdateEnvMachineCfg(params);
            envInfoService.addOrUpdateEnvServerCfg(params);
            envInfoService.addOrUpdateSvcgenDeployControl(params);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存配置信息失败！").setMessage("保存配置信息失败！");
        }

        return Response.ok().setMessage("保存配置信息成功！");
    }


    @RequestMapping(value="/queryEsbCfgDetails",method = RequestMethod.GET)
    public Response queryEsbCfgDetails(@RequestParam(name = "deicId") String deicId){
        DSGCBusCfgVO busCfgVO = envInfoService.queryEsbCfgDetails(deicId);
        return Response.ok().data(busCfgVO);
    }
}
