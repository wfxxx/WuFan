package com.definesys.dsgc.service.ystar.report;

import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.report.bean.ReportSvcDataBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName: ReportAnalyzeController
 * @Description: TODO
 * @Author：ystar
 * @Date : 2020/12/16 12:00
 */
@RestController("ReportAnalyzeController")
@Api(value = "报表数据分析", tags = {"报表数据分析"})
@RequestMapping("/dsgc/newReport")
public class ReportAnalyzeController {

    @Autowired
    ReportAnalyzeService reportAnalyzeService;

    /*** 获取下游系统 一周的数据 */
    @ApiOperation("获取下游系统 一周的数据")
    @PostMapping("/getTrgSysWeekRpData")
    public Response getTrgSysWeekRpData(@Valid @RequestBody ReportSvcDataBean rpSvcDataBean, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if (StringUtil.isNotBlank(userId) || "SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)) {
            return Response.ok().data(reportAnalyzeService.getTrgSysWeekRpData(rpSvcDataBean));
        } else {
            return Response.error("用户权限不足！");
        }

    }

    /*** 获取指定时间 的90分位数据*/
    @ApiOperation("获取指定时间的90分位数据")
    @PostMapping("/getNineLineWeekRpData")
    public Response getNineLineWeekRpData(@Valid @RequestBody ReportSvcDataBean rpSvcDataBean, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if (StringUtil.isNotBlank(userId) || "SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)) {
            return Response.ok().data(reportAnalyzeService.getSvcCountData(rpSvcDataBean));
        } else {
            return Response.error("用户权限不足！");
        }

    }


    /**
     * 导入请求参数/响应参数
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/importParamData", method = RequestMethod.POST)
    public Response importParamData(@RequestParam(value = "file") MultipartFile file) {
        return this.reportAnalyzeService.importParamData(file);
    }

}
