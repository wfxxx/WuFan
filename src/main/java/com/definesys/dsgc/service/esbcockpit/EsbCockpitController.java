package com.definesys.dsgc.service.esbcockpit;

import com.definesys.dsgc.service.esbcockpit.bean.CommonReqBean;
import com.definesys.dsgc.service.esbcockpit.bean.RadarBean;
import com.definesys.dsgc.service.esbcockpit.bean.eCharts3DBean;
import com.definesys.dsgc.service.esbcockpit.bean.eChartsBean;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EsbCockpitController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-4-7 10:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/dsgc/esbcockpit")
public class EsbCockpitController {

    @Autowired
    private EsbCockpitService esbCockpitService;

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
            result= esbCockpitService.queryTotalRunTimes(beginOfDate,endOfDate);
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/queryDayRunTimes")
    public Response queryDayRunTimes(){
        try{
            Object times = esbCockpitService.queryDayRunTimes();
            return Response.ok().setData(times);
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
            result= esbCockpitService.queryTotalapp();
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
            result= esbCockpitService.queryAppDistri(commonReqBean.getStartTime(),commonReqBean.getEndTime());
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
            result= esbCockpitService.queryAppExecute(commonReqBean.getStartTime(),commonReqBean.getEndTime());
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
            Map<String,Object> reslt=esbCockpitService.dash();
            return Response.ok().setData(reslt);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    //查询所有系统的一周调用数(3DEchart图数据)
    @PostMapping("/3dechart")
    public Response select3DEchart(){
        try{
            List<eCharts3DBean> reslt = esbCockpitService.select3DEchart();
            return Response.ok().setData(reslt);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/radar")
    public Response selectRadar(){
        try{
            Map<String, Object> rst = esbCockpitService.selectRadar();
            return Response.ok().setData(rst);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }

    }

    @PostMapping("/cone")
    public Response selectConeData(){
        try{
            Map<String, Object> coneData = esbCockpitService.selectConeData();
            return Response.ok().setData(coneData);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @PostMapping("/balloon")
    public Response selectBalloon(){
        try {
            return Response.ok().setData(esbCockpitService.selectBalloon());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }


}
