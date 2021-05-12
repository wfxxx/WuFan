package com.definesys.dsgc.service.ystar.report.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "RPT_MONTH_VIEW", sql = Const.CMN_QUERY_REPORT_SQL + ",r.YEAR,r.MONTH,r.DAY from RP_SYS_DAY r \n" +
                "LEFT JOIN RP_SERV_DAY p on r.SERV_NO = p.SERV_NO and p.CLIENT = r.CLIENT and r.YEAR = p.YEAR and r.MONTH = p.MONTH and r.DAY = p.DAY ")
})
public class ReportMonthVO extends ReportYearVO {
    @Column(value = "DAY")
    private Integer day;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
