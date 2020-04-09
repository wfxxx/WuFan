package com.definesys.dsgc.service.apicockpit;

import com.definesys.dsgc.service.apicockpit.bean.CommonReqBean;
import com.definesys.dsgc.service.apicockpit.bean.eChartsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.definesys.mpaas.common.http.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName apiCockpitController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/dsgc/apicockpit")
public class ApiCockpitControlle {

    @Autowired
    private ApiCockpitService apiCockpitService;

    //api一段时间内总调用次数
    /**
     * @return result
     */
    @PostMapping("/queryTotalRunTimes")
    public Response queryTotalRunTimes(){
        eChartsBean result=null;
        try{
            Calendar cal = Calendar.getInstance();
            cal.set(1000, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            Date beginOfDate = cal.getTime();
            cal=Calendar.getInstance();
            cal.set( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            Date endOfDate = cal.getTime();
            result= apiCockpitService.queryTotalRunTimes(beginOfDate,endOfDate);
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    //平台接入应用数量
    /**
     * @return result
     */
    @PostMapping("/queryTotalapp")
    public Response queryTotalapp(){
        eChartsBean result=null;
        try{
            result= apiCockpitService.queryTotalapp();
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    //一段时间内API调用在各系统分布数量（成功的和失败的）
    /**
     * @param commonReqBean
     * @return result
     */
    @PostMapping("/queryAppDistri")
    public Response queryAppDistri(@RequestBody CommonReqBean commonReqBean){
        List<eChartsBean> result=null;
        try{
            result= apiCockpitService.queryAppDistri(commonReqBean.getStartTime(),commonReqBean.getEndTime());
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    //一段时间内调用到的API个数（注意不是次数）占总数的比例
    /**
     * @param commonReqBean
     * @return result
     */
    @PostMapping("/queryAppExecute")
    public Response queryAppExecute(@RequestBody CommonReqBean commonReqBean){
        eChartsBean result=null;
        try{
            result= apiCockpitService.queryAppExecute(commonReqBean.getStartTime(),commonReqBean.getEndTime());
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    //定时获取
    @PostMapping("/dash")
    public Response dash(){
        try{
            Map<String,Object> reslt=apiCockpitService.dash();
            return Response.ok().setData(reslt);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }



}
