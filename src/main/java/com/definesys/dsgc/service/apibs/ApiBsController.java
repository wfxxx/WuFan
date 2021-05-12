package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "dsgc/apibs")
public class ApiBsController {
    @Autowired
    private ApiBsService apiBsService;

    @RequestMapping(value = "/queryApiBsList",method = RequestMethod.POST)
    public Response queryApiBsList(@RequestBody CommonReqBean param,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
           result = apiBsService.queryApiBsList(param,userId,userRole,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取服务列表失败");
        }

        return Response.ok().setData(result);
    }


    @RequestMapping(value = "/checkSame",method = RequestMethod.POST)
    public Response queryApiBsList(@RequestBody DagBsbean bean){
        DagBsbean result;
        try {
            result = apiBsService.checkSame(bean);
            if(result==null){
                return Response.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("异常");
        }
        return Response.error("存在相同数据");
    }

    @RequestMapping(value = "/queryApiBsByCustomInput",method = RequestMethod.POST)
    public Response queryApiBsByCustomInput(@RequestBody CommonReqBean param, HttpServletRequest request){
        List<String> result = new ArrayList<>();
        try {
            result = apiBsService.queryApiBsByCustomInput(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("内部错误!");
        }

        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/queryApiBsByCode",method = RequestMethod.GET)
    public Response queryApiBsByCode(@RequestParam(value = "code") String code){
        DagBsbean result =null;
        try {
            result = apiBsService.queryApiBsByCode(code);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询失败");
        }

        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/addApiBs",method = RequestMethod.POST)
    public Response addApiBs(@RequestBody DagBsbean dagBsbean){

        try {
            apiBsService.addApiBs(dagBsbean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增服务发生错误");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/updateDagBs",method = RequestMethod.POST)
    public Response updateDagBs(@RequestBody DagBsbean dagBsbean){

        try {
            apiBsService.updateDagBs(dagBsbean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新服务发生错误");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/delApiBs",method = RequestMethod.POST)
    public Response delApiBs(@RequestBody CommonReqBean param){
        try {
            apiBsService.delApiBs(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除服务发生错误");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryDagBsDtiByVid",method = RequestMethod.GET)
    public Response queryDagBsDtiByVid(@RequestParam(value = "vid") String id){
        DagBsDtiBean result=null;
        try {
            result=apiBsService.queryDagBsDtiByVid(id);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查找失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updateDagBsDti",method = RequestMethod.POST)
    public Response updateDagBsDti(@RequestBody DagBsDtiBean param){
        DagBsDtiBean result=null;
        try {
             result=apiBsService.updateDagBsDti(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/addDagBsDti",method = RequestMethod.POST)
    public Response addDagBsDti(@RequestBody DagBsDtiBean param){
        System.out.println(param);
        DagBsDtiBean result=null;
        try {
            result=apiBsService.addDagBsDti(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增失败");
        }
        return Response.ok().setData(result);
    }


    @RequestMapping(value = "/queryDagCodeVersionBySource",method = RequestMethod.GET)
    public Response queryDagCodeVersionBySource(@RequestParam(value = "sourceId") String sourceId){
        List<DagCodeVersionBean> result=null;
        try {
            result=apiBsService.queryDagCodeVersionBySource(sourceId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/queryDagCodeVersionByid",method = RequestMethod.GET)
    public Response queryDagCodeVersionByid(@RequestParam(value = "id") String id){
        DagCodeVersionBean result=null;
        try {
            result=apiBsService.queryDagCodeVersionByid(id);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询失败");
        }
        return Response.ok().setData(result);
    }


    @RequestMapping(value = "/delDagCodeVersionByid",method = RequestMethod.POST)
    public Response delDagCodeVersionByid(@RequestBody CommonReqBean commonReqBean){

        List<Map<String,Object>> value=apiBsService.queryDeployByVid(commonReqBean.getCon0());
        if(value.size()>0){
            return Response.error("该配置项使用中，无法删除");
        }
        try {
            apiBsService.delDagCodeVersionByid(commonReqBean.getCon0());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/updateDagCodeVersion",method = RequestMethod.POST)
    public Response updateDagCodeVersion(@RequestBody DagCodeVersionBean dagCodeVersionBean){
        DagCodeVersionBean result=null;
        try {
            result=apiBsService.updateDagCodeVersion(dagCodeVersionBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/addDagCodeVersion",method = RequestMethod.POST)
    public Response addDagCodeVersion(@RequestBody DagCodeVersionBean dagCodeVersionBean){
        DagCodeVersionBean result=null;
        String vid="";
        try {
             vid=apiBsService.addDagCodeVersion(dagCodeVersionBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增失败");
        }
        return Response.ok().setData(vid);
    }

    @RequestMapping(value = "/copyDagCodeVersion",method = RequestMethod.POST)
    public Response copyDagCodeVersion(@RequestBody DagCodeVersionBean dagCodeVersionBean){
        DagCodeVersionBean result=null;
        try {
            apiBsService.copyDagCodeVersion(dagCodeVersionBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("复制失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/queryPluginUsing",method = RequestMethod.POST)
    public Response queryPluginUsing(@RequestBody CommonReqBean commonReqBean){
        List<DagPlugUsingBean> result=null;
        try {
            result=apiBsService.queryPluginUsing(commonReqBean.getCon0());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/addPluginUsing",method = RequestMethod.POST)
    public Response addPluginUsing(@RequestBody DagPlugUsingBean dagPlugUsingBean){
        DagPlugUsingBean result=null;
        try {
            result =apiBsService.addPluginUsing(dagPlugUsingBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增失败");
        }
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/updatePluginUsing",method = RequestMethod.POST)
    public Response updatePluginUsing(@RequestBody DagPlugUsingBean dagPlugUsingBean){
        try {
            apiBsService.updatePluginUsing(dagPlugUsingBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/delPluginUsing",method = RequestMethod.POST)
    public Response delPluginUsing(@RequestBody DagPlugUsingBean dagPlugUsingBean){
        DagCodeVersionBean result=null;
        try {
            apiBsService.delPluginUsing(dagPlugUsingBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除插件失败");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/queryPluginStore",method = RequestMethod.POST)
    public Response queryPluginStore(){
        List<Map<String,Object>> result=null;
        try {
            result=apiBsService.queryPluginStore();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询失败");
        }
        return Response.ok().setData(result);
    }

}
