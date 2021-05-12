package com.definesys.dsgc.service.esbenv;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvMachineCfg;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvServerCfg;
import com.definesys.dsgc.service.esbenv.bean.SvcgenDeployControl;
import com.definesys.dsgc.service.esbenv.bean.DSGCBusCfgVO;
import com.definesys.dsgc.service.esbenv.DSGCBusCfgService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description: 总线控制器
 * @author: biao.luo
 * @since: 2019/7/18 下午6:24
 * @history: 1.2019/7/18 created by biao.luo
 */
//@AuthAspect(menuCode = "serviceBus",menuName = "总线设置")
@RestController
@RequestMapping(value = "/dsgc/buscfg")
public class DSGCBusCfgController {
    @Autowired
    private SWordLogger logger;

    @Autowired
    private DSGCBusCfgService busCfgService;

    @RequestMapping(value="/server/queryEnvInfoCfgListPage",method = {RequestMethod.POST,RequestMethod.GET})
    public Response queryEnvInfoCfgListPage(){
        List<DSGCEnvInfoCfg> envInfoCfgs = busCfgService.queryEnvInfoCfgListPage();
        return Response.ok().data(envInfoCfgs);
    }

//    @OperationAspect(value = "新增或修改总线基本配置信息")
    @RequestMapping(value="/server/insertOrUpdateEnvInfoCfg",method = RequestMethod.POST)
    public Response insertOrUpdateEnvInfoCfg(@RequestBody DSGCEnvInfoCfg envInfoCfg){
        try{
            busCfgService.insertOrUpdateEnvInfoCfg(envInfoCfg);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存总线配置信息失败！").setMessage("保存总线配置信息失败！");
        }

        return Response.ok().setMessage("保存总线配置信息成功！");
    }

    @RequestMapping(value="/server/findBusCfgDetailsByEnvCode",method = RequestMethod.GET)
    public Response findBusCfgDetailsByEnvCode(@RequestParam(name = "deicId") String deicId){
        DSGCBusCfgVO busCfgVO = busCfgService.findBusCfgDetailsByEnvCode(deicId);
        return Response.ok().data(busCfgVO);
    }

//    @OperationAspect(value = "新增或修改总线--服务器配置信息")
    @RequestMapping(value="/server/insertOrUpdateEnvMachineCfg",method = RequestMethod.POST)
    public Response insertOrUpdateEnvMachineCfg(@RequestBody DSGCEnvMachineCfg envMachineCfg){
        try{
            busCfgService.insertOrUpdateEnvMachineCfg(envMachineCfg);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存环境机器信息失败！").setMessage("保存环境机器信息失败！");
        }

        return Response.ok().setMessage("保存环境机器信息成功！");
    }

//    @OperationAspect(value = "新增或修改总线--节点配置信息")
    @RequestMapping(value="/server/insertOrUpdateEnvServerCfg",method = RequestMethod.POST)
    public Response insertOrUpdateEnvServerCfg (@RequestBody DSGCEnvServerCfg envServerCfg){
        try{
            busCfgService.insertOrUpdateEnvServerCfg(envServerCfg);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存环境节点信息失败！").setMessage("保存环境节点信息失败！");
        }

        return Response.ok().setMessage("保存环境节点信息成功！");
    }

//    @OperationAspect(value = "新增或修改总线--角色部署环境配置信息")
    @RequestMapping(value="/server/insertOrUpdateSvcgenDeployControl",method = RequestMethod.POST)
    public Response insertOrUpdateSvcgenDeployControl(@RequestBody SvcgenDeployControl deployControl){
        try{
            busCfgService.insertOrUpdateSvcgenDeployControl(deployControl);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存环境部署控制信息失败！").setMessage("保存环境部署控制信息失败！");
        }

        return Response.ok().setMessage("保存环境部署控制信息成功！");
    }

//    @OperationAspect(value = "删除总线--服务器配置信息")
    @RequestMapping(value="/server/delEnvMachineCfgByDemcId",method = RequestMethod.POST)
    public Response delEnvMachineCfgByDemcId(@RequestBody String[] rowIds){
        try{
            busCfgService.delEnvMachineCfgByDemcId(rowIds);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("删除环境机器信息失败！").setMessage("删除环境机器信息失败！");
        }

        return Response.ok().setMessage("删除环境机器信息成功！");
    }

//    @OperationAspect(value = "删除总线--节点配置信息")
    @RequestMapping(value="/server/delEnvServerCfgBydDescId",method = RequestMethod.POST)
    public Response delEnvServerCfgBydDescId(@RequestBody String[] rowIds){
        try{
            busCfgService.delEnvServerCfgBydDescId(rowIds);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("删除环境节点信息失败！").setMessage("删除环环境节点信息失败！");
        }

        return Response.ok().setMessage("删除环境节点信息成功！");
    }

//    @OperationAspect(value = "删除总线--部署环境配置信息")
    @RequestMapping(value="/server/delSvcgenDeployControlBySdcId",method = RequestMethod.POST)
    public Response delSvcgenDeployControlBySdcId(@RequestBody String[] rowIds){
        try{
            busCfgService.delSvcgenDeployControlBySdcId(rowIds);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("删除环境部署控制信息失败！").setMessage("删除环境部署控制信息失败！");
        }

        return Response.ok().setMessage("删除环境部署控制信息成功！");
    }

//    @OperationAspect(value = "删除总线--环境信息")
    @RequestMapping(value="/server/delEnvInfoByDeid",method = RequestMethod.POST)
    public Response delEnvInfoByDeid(@RequestBody DSGCEnvInfoCfg dsgcEnvInfoCfg){
        try {
            busCfgService.delEnvInfoByDeid(dsgcEnvInfoCfg);
        }catch (Exception e){
            logger.error("删除环境信息及子表信息失败!");
            return Response.error("删除环境信息及子表信息失败!").setMessage("删除环境信息及子表信息失败!");
        }
        return Response.ok().setMessage("删除环境信息及子表信息成功!");
    }


}
