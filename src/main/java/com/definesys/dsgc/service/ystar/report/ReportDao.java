package com.definesys.dsgc.service.ystar.report;

import com.definesys.dsgc.service.ystar.report.bean.*;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: ReportDao
 * @Description: TODO
 * @Author：ystar
 * @Date : 2021/3/15 12:00
 */
@Repository("ReportDao")
public class ReportDao {
    @Value("${database.type}")
    private String dbType;

    @Autowired
    private MpaasQueryFactory sw;

    //查询日报表数据
    public List<ReportDayVO> getDayReportData(String year, String month, String day, String svcCode, String sysCode) {
        return this.sw
                .buildViewQuery("RPT_DAY_VIEW")
                .eq("year", year)
                .eq("month", month)
                .eq("day", day)
                .eq("svcCode", svcCode)
                .eq("sysCode", sysCode)
                .doQuery(ReportDayVO.class);
    }

    //查询月报表数据
    public List<ReportMonthVO> getMonthReportData(String year, String month, String svcCode, String sysCode) {
        return this.sw
                .buildViewQuery("RPT_MONTH_VIEW")
                .eq("year", year)
                .eq("month", month)
                .eq("svcCode", svcCode)
                .eq("sysCode", sysCode)
                .doQuery(ReportMonthVO.class);
    }

    //查询年报表数据
    public List<ReportYearVO> getYearReportData(String year, String svcCode, String sysCode) {
        return this.sw
                .buildViewQuery("RPT_YEAR_VIEW")
                .eq("year", year)
                .eq("svcCode", svcCode)
                .eq("sysCode", sysCode)
                .doQuery(ReportYearVO.class);
    }

    //查询日志报表数据
    public PageQueryResult<ReportLogVO> getLogReportData(QueryReportBean reportBean, int pageIndex, int pageSize) {
        return this.sw
                .buildViewQuery("RPT_LOG_VIEW")
                .eq("svcCode", reportBean.getSvcCode())
                .eq("token", reportBean.getToken())
                .eq("sysCode", reportBean.getSysCode())
                .eq("trgSysCode", reportBean.getTrgSysCode())
                .eq("status", reportBean.getStatus())
                .gteq("reqTime", reportBean.getStartTime())
                .lteq("reqTime", reportBean.getEndTime())
                .doPageQuery(pageIndex, pageSize, ReportLogVO.class);
    }


    //查询所有服务编号+名称列表信息
    public List<SvcInfoVO> getAllSvcInfo() {
        return this.sw
                .buildViewQuery("QUERY_ALL_SVC_VIEW")
                .doQuery(SvcInfoVO.class);
    }

    //查询所有系统编号+名称列表信息
    public List<SysInfoVO> getAllSysInfo() {
        return this.sw
                .buildViewQuery("QUERY_ALL_SYS_VIEW")
                .doQuery(SysInfoVO.class);
    }

}
