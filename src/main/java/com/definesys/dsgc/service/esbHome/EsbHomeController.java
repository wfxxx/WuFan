package com.definesys.dsgc.service.esbHome;


import com.definesys.dsgc.service.apideploylog.bean.CommonReqBean;
import com.definesys.dsgc.service.bpm.BpmService;
import com.definesys.dsgc.service.bpm.bean.BpmCommonReqBean;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceDTO;
import com.definesys.dsgc.service.esbHome.bean.EsbHomeHisto;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EsbHomeController
 * @Description TODO
 * @Author XIEZHONGYUAN
 * @Date 2020-3-2
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "dsgc/esbhome")
public class EsbHomeController {

    @Autowired
    EsbHomeService esbHomeService;
    @Autowired
    private BpmService bpmService;

//获取ESB首页卡片视图数据
    @GetMapping("/queryCardsData")
    public Response queryCardsData(HttpServletRequest request){
        Map<String,Object> result=null;
        try{
            result= esbHomeService.queryCardsData();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取ESB服务运行统计柱状图数据
    @PostMapping("/querySortVist")
    public Response querySortVist(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.querySortVist(limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取ESB失败排序柱状图数据
    @PostMapping("/queryFailTotal")
    public Response queryFailTotal(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.queryFailTotal(startTime,endTime,limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);

    }

    //获取ESB等待排序柱状图数据
    @PostMapping("/querySortWait")
    public Response querySortWait(@RequestBody Map<String,Object> params,HttpServletRequest request){
        String startTime= params.get("startTime")!=null? (String) params .get("startTime") :null;
        String endTime=params.get("endTime")!=null?(String)params.get("endTime"):null;
        String limitTime= params.get("limitTime")!=null?(String)params.get("limitTime"):null;
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.querySortWait(startTime,endTime,limitTime);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取本月新增ESB详细数据
    @GetMapping("/queryEsbIcrease")
    public Response queryEsbIcrease(HttpServletRequest request){
        List<MarketEntiy> result;
        try{
            result= esbHomeService.queryEsbIcrease();
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
            result= esbHomeService.queryMyTask(userId);
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
            result=bpmService.getServTaskList(param,userId,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }


    //按应用分类获取esb信息,获取全部
    @PostMapping("/queryEsbInfoByAppALL")
    public Response queryEsbInfoByAppALL( HttpServletRequest request){
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.queryEsbInfoByAppALL();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //按类型分类获取ESB信息,获取个人
    @PostMapping("/queryEsbInfoByType")
    public Response queryEsbInfoByType(HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.queryEsbInfoByType(userId,userRole);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //按类型分类获取esb服务信息
    @PostMapping("/queryEsbInfoByTypeALL")
    public Response queryEsbInfoByTypeALL(HttpServletRequest request){
        List<EsbHomeHisto> result;
        try{
            result= esbHomeService.queryEsbInfoByTypeALL();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }
    @PostMapping("/queryEsbServerNodeStatus")
    public Response queryEsbServerNodeStatus(HttpServletRequest request){
        List<Map<String,String>>  result;
        try{
            result= esbHomeService.queryEsbServerNodeStatus();
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
            result= esbHomeService.queryTrafficAnalysis();
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }

    //获取当前环境地址
    @PostMapping("/queryAdminLocaltion")
    public Response queryAdminLocaltion (@RequestBody  CommonReqBean reqBean,HttpServletRequest request){
        EsbHomeHisto result=null;
        try{
            result= esbHomeService.queryAdminLocaltion(reqBean.getCon0());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
        return Response.ok().setData(result);
    }


}
