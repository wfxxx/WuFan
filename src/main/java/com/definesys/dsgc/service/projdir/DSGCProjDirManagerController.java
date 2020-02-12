package com.definesys.dsgc.service.projdir;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.definesys.dsgc.service.projdir.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.projdir.bean.SysProfileDirVO;
import com.definesys.dsgc.service.projdir.DSGCProjDirManagerService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/6/21 上午10:14
 * @history: 1.2019/6/21 created by biao.luo
 */
//@AuthAspect(menuCode = "projectManage",menuName = "项目目录管理")
@RestController
@RequestMapping("/dsgc/proj/dir")
public class DSGCProjDirManagerController {

    @Autowired
    private SWordLogger logger;

    @Autowired
    private DSGCProjDirManagerService projDirManagerService;

    @RequestMapping(value = "/manage/querySysProfileDirListPage", method = {RequestMethod.POST,RequestMethod.GET})
    public Response querySysProfileDirListPage(@RequestBody SysProfileDirVO sysProfileDirVO,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                               @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<SysProfileDirVO> queryResult = projDirManagerService.querySysProfileDirListPage(pageIndex,pageSize,sysProfileDirVO);
        List<SysProfileDirVO> sysProfileDirVOS = new ArrayList<>();
        Map<String, Object> resMap = new HashMap<>();
        if (queryResult.getCount() < 1) {
            resMap.put("count", 0);
            resMap.put("dirList", null);
        } else {
            sysProfileDirVOS = queryResult.getResult();
            resMap.put("count", queryResult.getCount());
            resMap.put("dirList", sysProfileDirVOS);
        }
        return Response.ok().data(resMap);
    }

//    @OperationAspect(value = "项目目录管理--新增或修改项目目录")
    @RequestMapping(value="/manage/saveOrUpdateSysProfileDir",method = RequestMethod.POST)
    public Response saveOrUpdateSysProfileDir(@RequestBody SysProfileDirVO sysProfileDirVO){

        try{
            projDirManagerService.saveOrUpdateSysProfileDir(sysProfileDirVO);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("保存项目信息失败!").setMessage(ex.getMessage());
        }

        return Response.ok().setMessage("保存项目信息成功!");
    }

//    @OperationAspect(value = "项目目录管理--删除项目目录")
    @RequestMapping(value="/manage/delSysProfileDirByProId",method = RequestMethod.POST)
    public Response delSysProfileDirByProId(@RequestBody String[] proIds){

        try{
            projDirManagerService.delSysProfileDirByProId(proIds);
        }catch (Exception ex) {
            logger.error("%s", ex.getMessage());
            return Response.error("删除目录配置信息失败！").setMessage("删除目录配置信息失败！");
        }

        return Response.ok().setMessage("删除目录配置信息成功！");
    }

    @RequestMapping(value="/manage/getSysProfileDirByProId",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Response getSysProfileDirByProId(@RequestBody String pId){
        JSONObject object = JSONObject.parseObject(pId);
        String proId = object.getString("projId");
        DSGCSysProfileDir sysProfileDir = projDirManagerService.getSysProfileDirByProId(proId);
        if(null == sysProfileDir){
            return Response.ok().data(null).setMessage("查询无数据！");
        }
        return Response.ok().data(sysProfileDir);
    }

    /**
     * 根据角色与用户id查询系统信息
     * @param request
     * @return 系统信息list
     */
    @RequestMapping(value="/manage/querySystemListPage",method = RequestMethod.GET)
    public Response querySystemListPage(HttpServletRequest request){
        String userRole = request.getHeader("userRole");
        String uid = request.getHeader("uid");
        List<DSGCSystemEntities> systemEntitiesList = projDirManagerService.querySystemListPage(userRole,uid);
        if(null == systemEntitiesList || systemEntitiesList.size() < 0){
            return Response.ok().data(null).setMessage("查询无数据！");
        }
        return Response.ok().data(systemEntitiesList);
    }

    /**
     * 校验项目目录是否存在
     * @param proDirName 项目目录
     * @return
     */
    @RequestMapping(value="/manage/checkProDir",method = RequestMethod.POST)
    public Response checkProDir(@RequestBody String proDirName){
        JSONObject jsonObject = JSONObject.parseObject(proDirName);
        String proDir = jsonObject.getString("pointProDire");
        DSGCSysProfileDir sysProfileDir = projDirManagerService.checkProDir(proDir);
        if (sysProfileDir != null) {
            return Response.error("该项目目录已存在！").setMessage("该项目目录已存在！");
        }
        return Response.ok().setMessage("该项目目录不存在，可以新增使用");
    }

    /**
     * 根据系统获取目录
     * @param serNo 服务编号
     * @return
     */
    @RequestMapping(value="/manage/getProDirBySystem",method = RequestMethod.POST)
    public Response getProDirBySystem(@RequestBody String serNo){
        JSONObject jsonObject = JSONObject.parseObject(serNo);
        String servNo = jsonObject.getString("servNo");
        List<DSGCSysProfileDir> list = projDirManagerService.getProDirBySystem(servNo);
        if (list.size()>0) {
            return Response.ok().data(list);
        }
        return Response.error("服务所属系统下无项目目录，请检查！").setMessage("服务所属系统下无项目目录，请检查！");
    }

    @RequestMapping(value = "/manage/querySystenBysubSystem",method = RequestMethod.POST)
    public Response querySystenBysubSystem(@RequestBody String subSystem) {
        JSONObject jsonObject = JSONObject.parseObject(subSystem);
        String subSystemCode = jsonObject.getString("subSystem");
        return Response.ok().data(projDirManagerService.querySystenBysubSystem(subSystemCode));
    }

}
