package com.definesys.dsgc.service.rpt;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.dsgc.service.rpt.DSGCSystemStatisticsReportService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "systemStatisticsReport",menuName = "系统报表")
@Api(description = "系统统计接口",tags = "系统统计模块")
@RequestMapping("/dsgc/systemStatisticsReport")
@RestController
public class DSGCSystemStatisticsReportController {

    @Autowired
    DSGCSystemStatisticsReportService systemStatisticsReportService;
    @Autowired
    private SWordLogger logger;

    /*
      * 获取接口总执行次数排名前20的接口信息
      * @return
      * @author xiezhongyuan
      * @time 2019.6.17*/
    @ApiOperation(value = "获取接口总执行次数排名前10的接口信息",notes = "返回总执行次数排名前10的接口信息")
    @RequestMapping(value="/getSysRunTotalNum",method= RequestMethod.GET)
    public Response getSysRunTotalNum(){
        List<RpSysTotal> serviceList=this.systemStatisticsReportService.getSysRunTotalNum();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /*
  * 获取接口运行平均时间排名前10的接口信息
  * @return
  * @author xiezhongyuan
  * @time 2019.6.17*/
    @ApiOperation(value = "获取接口运行平均时间排名前10的接口信息",notes = "返回口运行平均时间排名前10的接口信息")
    @RequestMapping(value="/getSysAveragetime",method= RequestMethod.GET)
    public Response getSysAveragetime(){
        List<RpSysTotal> serviceList=this.systemStatisticsReportService.getSysAveragetime();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /*
  * 获取接口成功率排名10的接口信息
  * @return
  * @author xiezhongyuan
  * @time 2019.6.17*/
    @ApiOperation(value = "获取接口失败率排名10的接口信息",notes = "返回接口失败率排名10的接口信息")
    @RequestMapping(value="/getSysRate",method= RequestMethod.GET)
    public Response getSysRate(){
        List<RpSysTotal> serviceList=this.systemStatisticsReportService.getSysRate();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }
    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getSysMinuteRunData",method= RequestMethod.POST)
    public Response getSysMinuteRunData(@RequestBody RpSysHour rpSysHour){
        List<Map<String, Object>> serviceList=this.systemStatisticsReportService.getSysMinuteRunData(rpSysHour);
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }
    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getSysHoursRunData",method= RequestMethod.POST)
    public Response getSysHoursRunData(@RequestBody RpSysHour rpSysHour){
        List<Map<String, Object>> serviceList=this.systemStatisticsReportService.getSysHoursRunData(rpSysHour);
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getSysDayRunData",method= RequestMethod.POST)
    public Response getSysDayRunData(@RequestBody RpSysDay rpSysDay){
        List<Map<String, Object>>  serviceList=this.systemStatisticsReportService.getSysDayRunData(rpSysDay);
        this.logger.debug("serviceList : "+serviceList);

        return Response.ok().data(serviceList);
    }


    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getSysMonthRunData",method= RequestMethod.POST)
    public Response getSysMonthRunData(@RequestBody RpSysMonth rpSysMonth){
        List<Map<String, Object>> serviceList=this.systemStatisticsReportService.getSysMonthRunData(rpSysMonth);
        this.logger.debug("serviceList : "+serviceList);

        return Response.ok().data(serviceList);
    }



    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getReqSysHoursRunData",method= RequestMethod.POST)
    public Response getReqSysHoursRunData(@RequestBody RpSysHour rpSysHour){
        List<Map<String, Object>> serviceList=this.systemStatisticsReportService.getReqSysHoursRunData(rpSysHour);
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getReqSysDayRunData",method= RequestMethod.POST)
    public Response getReqSysDayRunData(@RequestBody RpSysDay rpSysDay){
        List<Map<String, Object>>  serviceList=this.systemStatisticsReportService.getReqSysDayRunData(rpSysDay);
        this.logger.debug("serviceList : "+serviceList);
        System.out.println(serviceList);
        return Response.ok().data(serviceList);
    }


    /*
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getReqSysMonthRunData",method= RequestMethod.POST)
    public Response getReqSysMonthRunData(@RequestBody RpSysMonth rpSysMonth){
        List<Map<String, Object>> serviceList=this.systemStatisticsReportService.getReqSysMonthRunData(rpSysMonth);
        this.logger.debug("serviceList : "+serviceList);

        return Response.ok().data(serviceList);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpSysDay
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllSysDayData",method = RequestMethod.POST)
    public Response getAllSysDayAvgCost(@RequestBody RpSysDay rpSysDay){
        SystemReportDataListVO allSysDayData = systemStatisticsReportService.getAllSysDayData(rpSysDay);
        this.logger.debug("AllSysDayData : " + allSysDayData);
        return Response.ok().setData(allSysDayData);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpSysMonth
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllSysMonthData",method = RequestMethod.POST)
    public Response getAllSysMonthData(@RequestBody RpSysMonth rpSysMonth){
        SystemReportDataListVO allSysMonthData = systemStatisticsReportService.getAllSysMonthData(rpSysMonth);
        this.logger.debug("AllSysDayData : " + allSysMonthData);
        return Response.ok().setData(allSysMonthData);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpSysHour
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllSysHourData",method = RequestMethod.POST)
    public Response getAllSysHourData(@RequestBody RpSysHour rpSysHour){
        SystemReportDataListVO allSysHourData = systemStatisticsReportService.getAllSysHourData(rpSysHour);
        this.logger.debug("AllSysDayData : " + allSysHourData);
        return Response.ok().setData(allSysHourData);
    }
}
