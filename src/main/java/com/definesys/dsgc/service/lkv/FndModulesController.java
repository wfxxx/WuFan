package com.definesys.dsgc.service.lkv;

//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.definesys.dsgc.service.lkv.bean.FndModules;
import com.definesys.dsgc.service.lkv.FndModulesService;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/dsgc/fndModules")
@RestController
public class FndModulesController {

    @Autowired
    private SWordLogger logger;

    @Autowired
    FndModulesService fndModulesService;

    /**
     *  模块信息保存接口
     * @param u
     * @return
     */
//    @OperationAspect(value = "模块管理--新增或修改模块信息")
    @RequestMapping(value = "/modifyFndModules", method = RequestMethod.POST)
    public Response modifyUser(@RequestBody FndModules u) {
        String id = "";
        try{
            if (u.getModuleId() == null || u.getModuleId().trim().length() == 0) {
                id = this.fndModulesService.createFndModules(u);
            } else {
                id = this.fndModulesService.updateFndModules(u);
            }
        }catch (Exception ex) {
            // 捕获异常后回滚数据
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("%s", ex.getMessage());
            return Response.error("保存模块信息失败！").setCode("error").setMessage(ex.getMessage());
        }

        this.logger.debug("id " + id);
        return Response.ok().data(id).setMessage("保存模块信息成功！");
    }

    /**
     * 模块编码校重接口
     * @param modules
     * @return
     */
    @RequestMapping(value = "/checkModuleCodeIsExist", method = RequestMethod.POST)
    public Response checkModuleCodeIsExist(@RequestBody FndModules modules) {
        if(StringUtil.isBlank(modules.getModuleCode())){
            return Response.error("error").setMessage("校验参数模块编码不能为空！");
        }
        boolean bool = fndModulesService.checkModuleCodeIsExist(modules);
        if(bool){
            return Response.error("error").setMessage("模块编码已存在，请修改后重试！");
        }

        return Response.ok().setMessage("模块编码可以使用！");
    }


    /**
     *  根据模块id 批量删除模块信息
     * @param modIds
     * @return
     */
//    @OperationAspect(value = "模块管理--批量删除模块信息")
    @RequestMapping(value = "/deleteModuleById", method = RequestMethod.POST)
    public Response deleteBatchModuleById(@RequestBody String[] modIds) {
        try{
            fndModulesService.deleteBatchModuleById(modIds);
        }catch (Exception ex) {
            logger.error(" %s", ex.getMessage());
            return Response.error("error").setMessage("模块删除失败！");
        }

        return Response.ok().setMessage("模块删除成功！");
    }

    /**
     *
     * @param u
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    public Response query(@RequestBody FndModules u,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        this.logger.debug("pageSize " + pageSize);
        this.logger.debug("pageIndex " + pageIndex);
        this.logger.debug("data " + u);
        PageQueryResult<FndModules> datas = this.fndModulesService.query(u, pageSize, pageIndex);
        this.logger.debug("getResult " + datas.getResult());
        return Response.ok().data(datas);
    }

    @RequestMapping(value = "/findFndModulesById", method = RequestMethod.POST)
    public Response findUserById(@RequestBody FndModules u) {
        FndModules fndModules = this.fndModulesService.findFndModulesById(u);
        this.logger.debug("fndProperties " + fndModules.getModuleId());
        return Response.ok().data(fndModules);
    }

}
