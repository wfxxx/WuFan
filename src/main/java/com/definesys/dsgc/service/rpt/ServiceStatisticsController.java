package com.definesys.dsgc.service.rpt;


import com.definesys.dsgc.service.rpt.ServiceStatisticsService;
import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "服务统计接口",tags = "服务统计模块")
@RequestMapping("/dsgc/ServiceStatistics")
@RestController
public class ServiceStatisticsController {
    @Autowired
    ServiceStatisticsService serviceStatisticsService;

    @Autowired
    private SWordLogger logger;


    /*
     * 获取接口总执行次数排名前20的接口信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "获取接口总执行次数排名前二十的接口信息",notes = "返回总执行次数排名前二十的接口信息")
    @RequestMapping(value="/getServiceCount",method= RequestMethod.GET)
    public Response getServiceCount(){
        List<RpServTotal> serviceList=this.serviceStatisticsService.getServiceCount();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /*
     * 获取系统被调用次数排名前20的系统信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "获取系统被调用次数排名前20的系统信息",notes = "返回被调用次数排名前20的系统信息")
    @RequestMapping(value = "/getSystemCount",method = RequestMethod.GET)
    public Response getSystemCount(){
        List<RpSysTotal> systemList=this.serviceStatisticsService.getSystemCount();
        this.logger.debug("systemList : "+systemList);
        return Response.ok().data(systemList);
    }

    /*
     * 根据名称获取该接口当日执行信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接口编号获取该接口当日执行信息",notes = "返回该编号接口当日执行信息")
    @ApiImplicitParam(name = "servNo",value = "接口编号",dataType = "string")
    @RequestMapping(value = "/getInterfaceDetailsReportDay",method = RequestMethod.GET)
    public Response getInterfaceDetailsReportDay(@RequestParam("servNo") String servNo){
        this.logger.debug("service : "+servNo);
        List<RpServHour> rpServHourList=this.serviceStatisticsService.getInterfaceDetailsReportDay(servNo);
        this.logger.debug("rpServHourList : "+rpServHourList);
        return Response.ok().data(rpServHourList);
    }

    /*
     * 根据名称获取该接口当月执行信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接口编号获取该接口当月执行信息",notes = "返回该编号接口当月执行信息")
    @ApiImplicitParam(name = "servNo",value = "接口编号",dataType = "string")
    @RequestMapping(value = "/getInterfaceDetailsReportMonth",method = RequestMethod.GET)
    public Response getInterfaceDetailsReportMonth(@RequestParam("servNo") String servNo){
        this.logger.debug("service : "+servNo);
        List<RpServDay> rpServDayList=this.serviceStatisticsService.getInterfaceDetailsReportMonth(servNo);
        this.logger.debug("rpServDayList : "+rpServDayList);
        return Response.ok().data(rpServDayList);
    }

    /*
     * 根据名称获取该接口当年执行信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接口编号获取该接口当年执行信息",notes = "返回该编号接口当年执行信息")
    @ApiImplicitParam(name = "servNo",value = "接口编号",dataType = "string")
    @RequestMapping(value = "/getInterfaceDetailsReportYear",method = RequestMethod.GET)
    public Response getInterfaceDetailsReportYear(@RequestParam("servNo") String servNo){
        this.logger.debug("service : "+servNo);
        List<RpServMonth> rpServMonthList=this.serviceStatisticsService.getInterfaceDetailsReportYear(servNo);
        this.logger.debug("rpServMonthList : "+rpServMonthList);
        return Response.ok().data(rpServMonthList);
    }

    /*
     * 根据名称获取该接口至今为止总执行信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接口编号获取该接口至今为止总执行信息",notes = "返回该编号接口至今为止总执行信息")
    @ApiImplicitParam(name = "servNo",value = "接口编号",dataType = "string")
    @RequestMapping(value = "/getInterfaceDetailsReport",method = RequestMethod.GET)
    public Response getInterfaceDetailsReport(@RequestParam("servNo") String servNo){
        this.logger.debug("service : "+servNo);
        List<RpServYear> rpServYearList=this.serviceStatisticsService.getInterfaceDetailsReport(servNo);
        this.logger.debug("rpServYearList : "+rpServYearList);
        return Response.ok().data(rpServYearList);
    }

    /*
     * 根据名称获取该系统当日被访问信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接收系统名称获取该系统当日执行信息",notes = "返回该接收系统当日执行信息")
    @ApiImplicitParam(name = "receive",value = "接收系统",dataType = "string")
    @RequestMapping(value = "/getSystemDetailsReportDay",method = RequestMethod.GET)
    public Response getSystemDetailsReportDay(@RequestParam("receive") String receive){
        this.logger.debug("system : "+receive);
        List<RpSysHour> rpSysHourList=this.serviceStatisticsService.getSystemDetailsReportDay(receive);
        this.logger.debug("rpSysHourList : "+rpSysHourList);
        return Response.ok().data(rpSysHourList);
    }

    /*
     * 根据名称获取该系统当月被访问信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接收系统名称获取该系统当月执行信息",notes = "返回该接收系统当月执行信息")
    @ApiImplicitParam(name = "receive",value = "接收系统",dataType = "string")
    @RequestMapping(value="/getSystemDetailsReportMonth",method = RequestMethod.GET)
    public Response getSystemDetailsReportMonth(@RequestParam("receive") String receive){
        this.logger.debug("system : "+receive);
        List<RpSysDay> rpSysDayList=this.serviceStatisticsService.getSystemDetailsReportMonth(receive);
        this.logger.debug("rpSysDayList : "+rpSysDayList);
        return Response.ok().data(rpSysDayList);
    }

    /*
     * 根据名称获取该系统当年被访问信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接收系统名称获取该系统当年执行信息",notes = "返回该接收系统当年执行信息")
    @ApiImplicitParam(name = "receive",value = "接收系统",dataType = "string")
    @RequestMapping(value = "/getSystemDetailsReportYear",method = RequestMethod.GET)
    public Response getSystemDetailsReportYear(@RequestParam("receive") String receive){
        this.logger.debug("system : "+receive);
        List<RpSysMonth> rpSysMonthList=this.serviceStatisticsService.getSystemDetailsReportYear(receive);
        this.logger.debug("rpSysMonthList : "+rpSysMonthList);
        return Response.ok().data(rpSysMonthList);
    }

    /*
     * 根据名称获取该系统至今为止被访问信息
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "根据接收系统名称获取该系统至今为止总执行信息",notes = "返回该接收系统至今为止总执行信息")
    @ApiImplicitParam(name = "receive",value = "接收系统",dataType = "string")
    @RequestMapping(value = "/getSystemDetailsReport",method = RequestMethod.GET)
    public Response getSystemDetailsReport(@RequestParam("receive") String receive){
        this.logger.debug("system : "+receive);
        List<RpSysYear> rpSysYearList=this.serviceStatisticsService.getSystemDetailsReport(receive);
        this.logger.debug("rpSysYearList : "+rpSysYearList);
        return Response.ok().data(rpSysYearList);
    }

    /*
     * 获取所有系统名称
     * @return
     * @author 刘兴科
     * @time 2019.3.12*/
    @ApiOperation(value = "获取所以系统名称",notes = "返回接收系统名称")
    @RequestMapping(value = "/getSystemGroup",method = RequestMethod.GET)
    public Response getSystemGroup(){
       List<String> systemGroup=this.serviceStatisticsService.getSystemGroup();
       this.logger.debug("systemGroup : "+systemGroup);
       return Response.ok().data(systemGroup);
    }
}
