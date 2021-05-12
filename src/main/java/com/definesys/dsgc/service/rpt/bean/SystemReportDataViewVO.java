package com.definesys.dsgc.service.rpt.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

/**
 * @program: DSGCService
 * @description: 系统报表VO
 * @author: firename
 * @date: 2019-09-05
 */
@SQLQuery(
        value = {
                @SQL(view = "all_hour_or_view", sql ="select sd.RECEIVE, sys.SYS_NAME, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST ,sum(TOTAL_TIMES_F) as SUM_TIMES_F "+
                        "from RP_SYS_HOUR where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY = #startDay AND HOUR >= #startHour ) OR ( YEAR = #endYear AND MONTH = #endMonth AND DAY = #endDay AND HOUR <= #endHour)) AND TOTAL_TIMES <> '0' group by RECEIVE having RECEIVE is not null ) sd, dsgc_system_entities sys where sd.receive=sys.sys_code"),

                @SQL(view = "all_month_or_view", sql = "select sd.RECEIVE, sys.SYS_NAME,  ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SYS_MONTH where ((YEAR = #startYear  AND MONTH >= #startMonth ) OR ( YEAR = #endYear AND MONTH <= #endMonth )) AND TOTAL_TIMES <> '0'  group by RECEIVE having RECEIVE is not null ) sd, dsgc_system_entities sys where sd.receive=sys.sys_code " ),

                @SQL(view = "all_day_or_view", sql = "select sd.RECEIVE, sys.SYS_NAME, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SYS_DAY where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY >= #startDay  ) OR ( YEAR = #endYear AND MONTH = #endMonth AND DAY <= #endDay )) AND TOTAL_TIMES <> '0' group by RECEIVE having RECEIVE is not null) sd, dsgc_system_entities sys where sd.receive=sys.sys_code"),

                @SQL(view = "all_hour_and_view", sql ="select sd.RECEIVE, sys.SYS_NAME, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST ,sum(TOTAL_TIMES_F) as SUM_TIMES_F "+
                        "from RP_SYS_HOUR where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY = #startDay AND HOUR >= #startHour ) and ( YEAR = #endYear AND MONTH = #endMonth AND DAY = #endDay AND HOUR <= #endHour)) AND TOTAL_TIMES <> '0' group by RECEIVE having RECEIVE is not null ) sd, dsgc_system_entities sys where sd.receive=sys.sys_code"),

                @SQL(view = "all_month_and_view", sql = "select sd.RECEIVE, sys.SYS_NAME,  ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SYS_MONTH where ((YEAR = #startYear  AND MONTH >= #startMonth ) and ( YEAR = #endYear AND MONTH <= #endMonth )) AND TOTAL_TIMES <> '0'  group by RECEIVE having RECEIVE is not null ) sd, dsgc_system_entities sys where sd.receive=sys.sys_code " ),

                @SQL(view = "all_day_and_view", sql = "select sd.RECEIVE, sys.SYS_NAME, ROUND((sd.SUM_COST/sd.SUM_TOTAL_TIMES),2) as AVG_COST, sd.SUM_TOTAL_TIMES as TOTAL_TIMES, sd.SUM_TIMES_F as TOTAL_TIMES_F, (sd.SUM_TIMES_F/sd.SUM_TOTAL_TIMES) as FAIL_RATE from ( select RECEIVE,  sum(TOTAL_TIMES) as SUM_TOTAL_TIMES , sum(TOTAL_TIMES*AVG_COST) as SUM_COST , sum(TOTAL_TIMES_F) as SUM_TIMES_F " +
                        "from RP_SYS_DAY where ( (YEAR = #startYear  AND MONTH = #startMonth AND DAY >= #startDay  ) and ( YEAR = #endYear AND MONTH = #endMonth AND DAY <= #endDay )) AND TOTAL_TIMES <> '0' group by RECEIVE having RECEIVE is not null) sd, dsgc_system_entities sys where sd.receive=sys.sys_code")
        }
)
public class SystemReportDataViewVO {

        private String receive;
        private String sysName;
        private Double avgCost;
        private Double totalTimes;
        private Double totalTimesF;
        private Double failRate;

        public SystemReportDataViewVO() {
        }

        public String getSysName() {
                return sysName;
        }

        public void setSysName(String sysName) {
                this.sysName = sysName;
        }

        public Double getAvgCost() {
                return avgCost;
        }

        public void setAvgCost(Double avgCost) {
                this.avgCost = avgCost;
        }

        public Double getTotalTimes() {
                return totalTimes;
        }

        public void setTotalTimes(Double totalTimes) {
                this.totalTimes = totalTimes;
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

        public String getReceive() {
                return receive;
        }

        public void setReceive(String receive) {
                this.receive = receive;
        }

        @Override
        public String toString() {
                return "SystemReportDataViewVO{" +
                        "receive='" + receive + '\'' +
                        ", sysName='" + sysName + '\'' +
                        ", avgCost=" + avgCost +
                        ", totalTimes=" + totalTimes +
                        ", totalTimesF=" + totalTimesF +
                        ", failRate=" + failRate +
                        '}';
        }
}
