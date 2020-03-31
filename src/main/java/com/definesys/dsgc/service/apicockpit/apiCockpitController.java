package com.definesys.dsgc.service.apicockpit;

import com.definesys.dsgc.service.apicockpit.bean.CommonReqBean;
import com.definesys.dsgc.service.apicockpit.bean.eChartsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.definesys.mpaas.common.http.Response;
import java.util.Date;
import java.util.List;

/**
 * @ClassName apiCockpitController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/cockpit")
public class apiCockpitController {

    @Autowired
    private  apiCockpitService  apiCockpitService;

    //api一段时间内总调用次数
    /**
     * @param commonReqBean
     * @return result
     */
    @PostMapping("/queryTotalRunTimes")
    public Response queryTotalRunTimes(@RequestBody CommonReqBean commonReqBean){
        eChartsBean result=null;
        try{
            result= apiCockpitService.queryTotalRunTimes(commonReqBean.getStartTime(),commonReqBean.getEndTime());
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
}
