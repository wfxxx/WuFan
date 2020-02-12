package com.definesys.dsgc.service.rpt.bean;


import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

/**
 * @program: DSGCService
 * @description: 查询服务报表数据VO
 * @author: firename
 * @date: 2019-09-04
 */
@SQLQuery(
        value = {
                @SQL(view = "all_hour_or_view", sql ="select sd.SERV_NO, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST ,sum(TOTAL_TIMES_F) as SUM_TIMES_F "+
                        "from RP_SERV_HOUR where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY = #startDay AND HOUR >= #startHour ) OR ( YEAR = #endYear AND MONTH = #endMonth AND DAY = #endDay AND HOUR <= #endHour)) AND TOTAL_TIMES <> '0' group by SERV_NO having SERV_NO is not null ) sd "),

                @SQL(view = "all_month_or_view", sql = "select sd.SERV_NO,  ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SERV_MONTH where ((YEAR = #startYear  AND MONTH >= #startMonth ) OR ( YEAR = #endYear AND MONTH <= #endMonth )) AND TOTAL_TIMES <> '0'  group by SERV_NO having SERV_NO is not null ) sd " ),

                @SQL(view = "all_day_or_view", sql = "select sd.SERV_NO, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SERV_DAY where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY >= #startDay  ) OR ( YEAR = #endYear AND MONTH = #endMonth AND DAY <= #endDay )) AND TOTAL_TIMES <> '0' group by SERV_NO having SERV_NO is not null) sd"),

                @SQL(view = "all_hour_and_view", sql ="select sd.SERV_NO, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST ,sum(TOTAL_TIMES_F) as SUM_TIMES_F "+
                        "from RP_SERV_HOUR where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY = #startDay AND HOUR >= #startHour ) and ( YEAR = #endYear AND MONTH = #endMonth AND DAY = #endDay AND HOUR <= #endHour)) AND TOTAL_TIMES <> '0' group by SERV_NO having SERV_NO is not null ) sd "),

                @SQL(view = "all_month_and_view", sql = "select sd.SERV_NO,  ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SERV_MONTH where ((YEAR = #startYear  AND MONTH >= #startMonth ) and ( YEAR = #endYear AND MONTH <= #endMonth )) AND TOTAL_TIMES <> '0'  group by SERV_NO having SERV_NO is not null ) sd " ),

                @SQL(view = "all_day_and_view", sql = "select sd.SERV_NO, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select SERV_NO,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SERV_DAY where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY >= #startDay  ) and ( YEAR = #endYear AND MONTH = #endMonth AND DAY <= #endDay )) AND TOTAL_TIMES <> '0' group by SERV_NO having SERV_NO is not null) sd")

        }
)
public class ServicesStaticViewVO {

    private String servNo;
    private Double totalTimes;
    private Double avgCost;
    private Double totalTimesS;
    private Double totalTimesF;
    private Double failRate;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public Double getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Double totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    public Double getTotalTimesS() {
        return totalTimesS;
    }

    public void setTotalTimesS(Double totalTimesS) {
        this.totalTimesS = totalTimesS;
    }

    public Double getTotalTimesF() {
        return totalTimesF;
    }

    public void setTotalTimesF(Double totalTimesF) {
        this.totalTimesF = totalTimesF;
    }

    public Double getFailRate() {
        return failRate;
    }

    public void setFailRate(Double failRate) {
        this.failRate = failRate;
    }

    @Override
    public String toString() {
        return "ServicesStaticViewVO{" +
                "servNo='" + servNo + '\'' +
                ", totalTimes=" + totalTimes +
                ", avgCost=" + avgCost +
                ", totalTimesS=" + totalTimesS +
                ", totalTimesF=" + totalTimesF +
                ", failRate=" + failRate +
                '}';
    }

    public ServicesStaticViewVO() {
    }
}
