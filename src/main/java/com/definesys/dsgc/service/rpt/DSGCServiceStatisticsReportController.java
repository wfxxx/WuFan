package com.definesys.dsgc.service.rpt;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.definesys.dsgc.service.rpt.bean.*;
import com.definesys.dsgc.service.rpt.DSGCServiceStatisticsReportService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "serviceStatisticsReport",menuName = "服务报表")
@Api(description = "系统统计接口",tags = "系统统计模块")
@RequestMapping("/dsgc/ServiceStatisticsReport")
@RestController
public class DSGCServiceStatisticsReportController {

    @Autowired
    DSGCServiceStatisticsReportService serviceStatisticsReportService;
    @Autowired
    private SWordLogger logger;

    /**
      * 获取接口总执行次数排名前20的接口信息
      * @return
      * @author xiezhongyuan
      * @time 2019.6.17*/
    @ApiOperation(value = "获取接口总执行次数排名前10的接口信息",notes = "返回总执行次数排名前10的接口信息")
    @RequestMapping(value="/getRunTotalNum",method= RequestMethod.GET)
    public Response getRunTotalNum(){
        List<RpServTotal> serviceList=this.serviceStatisticsReportService.getRunTotalNum();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /**
  * 获取接口运行平均时间排名前10的接口信息
  * @return
  * @author xiezhongyuan
  * @time 2019.6.17*/
    @ApiOperation(value = "获取接口运行平均时间排名前10的接口信息",notes = "返回口运行平均时间排名前10的接口信息")
    @RequestMapping(value="/getAveragetime",method= RequestMethod.GET)
    public Response getAveragetime(){
        List<RpServTotal> serviceList=this.serviceStatisticsReportService.getAveragetime();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /**
  * 获取接口成功率排名10的接口信息
  * @return
  * @author xiezhongyuan
  * @time 2019.6.17*/
    @ApiOperation(value = "获取接口失败率排名10的接口信息",notes = "返回接口失败率排名10的接口信息")
    @RequestMapping(value="/getRate",method= RequestMethod.GET)
    public Response getRate(){
        List<RpServTotal> serviceList=this.serviceStatisticsReportService.getRate();
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }
    /**
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getServMinuteRunData",method= RequestMethod.POST)
    public Response getServMinuteRunData(@RequestBody RpServHour rpServHour){
        List<Map<String, Object>> serviceList=this.serviceStatisticsReportService.getServMinuteRunData(rpServHour);
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }
    /**
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getServHoursRunData",method= RequestMethod.POST)
    public Response getServHoursRunData(@RequestBody RpServHour rpServHour){
        List<Map<String, Object>> serviceList=this.serviceStatisticsReportService.getServHoursRunData(rpServHour);
        this.logger.debug("serviceList : "+serviceList);
        return Response.ok().data(serviceList);
    }

    /**
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getServDayRunData",method= RequestMethod.POST)
    public Response getServDayRunData(@RequestBody RpServDay rpServDay){
        List<Map<String, Object>>  serviceList=this.serviceStatisticsReportService.getServDayRunData(rpServDay);
        this.logger.debug("serviceList : "+serviceList);

        return Response.ok().data(serviceList);
    }


    /**
* 获取某接口某段时间的接口信息
* @return
* @author xiezhongyuan
* @time 2019.6.18*/
    @ApiOperation(value = "获取某接口某时间段信息",notes = "返回某接口某时间段信息")
    @RequestMapping(value="/getServMonthRunData",method= RequestMethod.POST)
    public Response getServMonthRunData(@RequestBody RpServMonth rpServMonth){
        List<Map<String, Object>> serviceList=this.serviceStatisticsReportService.getServMonthRunData(rpServMonth);
        this.logger.debug("serviceList : "+serviceList);

        return Response.ok().data(serviceList);
    }

  /**
   * 获取年份的接口信息
   * @param rpServYear
   * @return
   */
  @RequestMapping(value = "getServYearRunData", method = RequestMethod.POST)
    public Response getServYearRunData(@RequestBody RpServYear rpServYear){
      return null;
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpServDay
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllServDayData",method = RequestMethod.POST)
    public Response getAllServDayAvgCost(@RequestBody RpServDay rpServDay){
        ServiceStaticListVO allServDayData = serviceStatisticsReportService.getAllServDayData(rpServDay);
        this.logger.debug("AllServDayData : " + allServDayData);
        return Response.ok().setData(allServDayData);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpServMonth
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllServMonthData",method = RequestMethod.POST)
    public Response getAllServMonthData(@RequestBody RpServMonth rpServMonth){
        ServiceStaticListVO allServMonthData = serviceStatisticsReportService.getAllServMonthData(rpServMonth);
        this.logger.debug("AllServDayData : " + allServMonthData);
        return Response.ok().setData(allServMonthData);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpServHour
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllServHourData",method = RequestMethod.POST)
    public Response getAllServHourData(@RequestBody RpServHour rpServHour){
        ServiceStaticListVO allServHourData = serviceStatisticsReportService.getAllServHourData(rpServHour);
        this.logger.debug("AllServDayData : " + allServHourData);
        return Response.ok().setData(allServHourData);
    }

    /**
     * 获取所有服务的某时间段内段平均耗时
     * @author wenjianming
     * @param rpServYear
     * @return
     */
    @ApiOperation(value = "获取所有服务的某时间段内段平均耗时", notes = "返回耗时前十的服务编号" )
    @RequestMapping(value = "getAllServYearData",method = RequestMethod.POST)
    public Response getAllServYearData(@RequestBody RpServYear rpServYear){
        ServiceStaticListVO result = serviceStatisticsReportService.getAllServYearData(rpServYear);
        this.logger.debug("AllServDayData : " + result);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "getAllServerName", method = RequestMethod.GET)
    public Response getAllServerName(){
        List<Map<String,Object>> allServername = serviceStatisticsReportService.getAllServername();
        return Response.ok().data(allServername);
    }
    @RequestMapping(value = "getAllServerNameFilterByRole", method = RequestMethod.GET)
    public Response getAllServerNameFilterByRole(HttpServletRequest httpServletRequest){
        String userId = httpServletRequest.getHeader("uid");
        String userRole = httpServletRequest.getHeader("userRole");
        List<Map<String,Object>> allServername = serviceStatisticsReportService.getAllServerNameFilterByRole(userId,userRole);
        return Response.ok().data(allServername);
    }
}
