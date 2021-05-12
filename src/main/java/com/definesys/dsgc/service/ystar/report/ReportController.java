package com.definesys.dsgc.service.ystar.report;

import com.definesys.dsgc.service.ystar.report.bean.QueryReportBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: ReportController
 * @Description: TODO
 * @Author：ystar
 * @Date : 2021/3/15 12:00
 */
@RestController("ReportController")
@Api(value = "平台报表", tags = {"平台报表"})
@RequestMapping("/dsgc/ystar/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/getAllSvcInfo")
    public Response getAllSvcInfo() {
        return Response.ok().data(this.reportService.getAllSvcInfo());
    }

    @GetMapping(value = "/getAllSysInfo")
    public Response getAllSysInfo() {
        return Response.ok().data(this.reportService.getAllSysInfo());
    }

    @RequestMapping(value = "/getDayReportData", method = RequestMethod.POST)
    public Response getDayReport(@RequestBody QueryReportBean reportBean) {
        return Response.ok().data(this.reportService.getDayReportData(reportBean));
    }


    @RequestMapping(value = "/getMonthReportData", method = RequestMethod.POST)
    public Response getMonthReport(@RequestBody QueryReportBean reportBean) {
        return Response.ok().data(this.reportService.getMonthReportData(reportBean));
    }


    @RequestMapping(value = "/getYearReportData", method = RequestMethod.POST)
    public Response getYearReport(@RequestBody QueryReportBean reportBean) {
        return Response.ok().data(this.reportService.getYearReportData(reportBean));
    }

    @RequestMapping(value = "/getLogReportData", method = RequestMethod.POST)
    public Response getLogReport(@RequestBody QueryReportBean reportBean,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex ) {
        return Response.ok().data(this.reportService.getLogReportData(reportBean,pageIndex,pageSize));
    }


}
