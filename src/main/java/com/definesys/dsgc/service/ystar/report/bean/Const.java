package com.definesys.dsgc.service.ystar.report.bean;


public class Const {

    //oracle SQL
    public static final String CMN_QUERY_REPORT_SQL = "select r.CLIENT as SYS_CODE,r.RECEIVE as TRG_SYS_CODE,r.SERV_NO as SVC_CODE,(select s.serv_name from DSGC_SERVICES s where s.SERV_NO = r.serv_no) as SVC_NAME,nvl(r.TOTAL_TIMES,0) as TOTAL_TIMES,nvl(p.AVG_COST,0) as AVG_COST,nvl(r.AVG_COST,0) as TRG_AVG_COST,nvl(p.AVG_COST-r.AVG_COST,0) as ESB_AVG_COST ";
    //mysql SQL
    //public static final String CMN_QUERY_REPORT_SQL = " select r.CLIENT as SYS_CODE,r.RECEIVE as TRG_SYS_CODE,r.SERV_NO as SVC_CODE,(select s.serv_name from DSGC_SERVICES s where s.SERV_NO = r.serv_no) as SVC_NAME,IFNULL(r.TOTAL_TIMES,0)  as TOTAL_TIMES,IFNULL(p.AVG_COST,0) as AVG_COST,IFNULL(r.AVG_COST,0) as TRG_AVG_COST,IFNULL(p.AVG_COST-r.AVG_COST,0) as ESB_AVG_COST ";


}
