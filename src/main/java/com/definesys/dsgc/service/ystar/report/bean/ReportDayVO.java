package com.definesys.dsgc.service.ystar.report.bean;


import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "RPT_DAY_VIEW", sql = Const.CMN_QUERY_REPORT_SQL )
})
public class ReportDayVO extends ReportMonthVO {
    @Column(value = "HOUR")
    private Integer hour;

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
