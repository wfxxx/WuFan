package com.definesys.dsgc.service.apiHome;


import com.definesys.dsgc.service.apiHome.bean.ApiHomeHisto;
import com.definesys.dsgc.service.apideploylog.bean.CommonReqBean;
import com.definesys.dsgc.service.bpm.BpmService;
import com.definesys.dsgc.service.bpm.bean.BpmCommonReqBean;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceDTO;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiHomeController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-28 10:26
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "dsgc/apihome")
public class ApiHomeController {

    @Autowired
    ApiHomeService apiHomeService;
    @Autowired
    private BpmService bpmService;

//获取Api首页卡片视图数据
    @GetMapping("/queryCardsData")
    public Response queryCardsData(HttpServletRequest request){
        Map<String,Object> result=null;
        try{
            result=apiHomeService.queryCardsData();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取Api访问排序柱状图数据
    @PostMapping("/querySortVist")
    public Response querySortVist(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.querySortVist(startTime,endTime,limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取Api并发排序柱状图数据
    @PostMapping("/querySortConcurrent")
    public Response querySortConcurrent(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.querySortConcurrent(startTime,endTime,limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);

    }

    //获取Api等待排序柱状图数据
    @PostMapping("/querySortWait")
    public Response querySortWait(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.querySortWait(startTime,endTime,limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取本月新增Api详细数据
    @GetMapping("/queryApiIcrease")
    public Response queryApiIcrease(HttpServletRequest request){
        List<MarketEntiy> result;
        try{
            result=apiHomeService.queryApiIcrease();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取我的待办统计
    @GetMapping("/queryTaskTotal")
    public Response queryMyTask(HttpServletRequest request){
        String userId = request.getHeader("uid");
        Map<String,Object> result;
        try{
            result=apiHomeService.queryMyTask(userId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取我的待办列表
    @PostMapping("/queryTaskList")
    public Response queryMyTaskList(HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        String userId = request.getHeader("uid");
        BpmCommonReqBean param=new BpmCommonReqBean();
        param.setQueryType("ALL");
        param.setCon0("");
        PageQueryResult<BpmInstanceDTO> result;
        try{
            result=bpmService.getTaskList(param,userId,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }


    //按应用分类获取api信息,获取全部
    @PostMapping("/queryApiInfoByAppALL")
    public Response queryApiInfoByAppALL( HttpServletRequest request){
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.queryApiInfoByAppALL();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //按类型分类获取api信息,获取个人
    @PostMapping("/queryApiInfoByType")
    public Response queryApiInfoByType(HttpServletRequest request){
        String userId = request.getHeader("uid");
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.queryApiInfoByType(userId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //按类型分类获取api信息
    @PostMapping("/queryApiInfoByTypeALL")
    public Response queryApiInfoByTypeALL(HttpServletRequest request){
        List<ApiHomeHisto> result;
        try{
            result=apiHomeService.queryApiInfoByTypeALL();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }




    //获取流量分析数据
    @PostMapping("/queryTrafficAnalysis")
    public Response queryTrafficAnalysis (HttpServletRequest request){
        Map<String,Object> result=null;
        try{
            result=apiHomeService.queryTrafficAnalysis();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取当前环境地址
    @PostMapping("/queryAdminLocaltion")
    public Response queryAdminLocaltion (@RequestBody  CommonReqBean reqBean,HttpServletRequest request){
        ApiHomeHisto result=null;
        try{
            result=apiHomeService.queryAdminLocaltion(reqBean.getCon0());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }


}
