package com.definesys.dsgc.service.esbHome.bean;

import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "error_instance_view", sql = "select M.*,B.max_day last_success_date,ds.subordinate_system res_from_sys from (select * from (SELECT A.track_id,A.serv_no,A.serv_name,A.req_from req_from_sys,A.creation_date last_error_date,A.INST_STATUS,A.INVOKE_RESULT FROM dsgc_log_instance A,(SELECT serv_no, max(CREATION_DATE) max_day FROM dsgc_log_instance GROUP BY serv_no) B WHERE A.serv_no = B.serv_no AND A.CREATION_DATE = B.max_day) C where C.INVOKE_RESULT != 'S' or C.INVOKE_RESULT is null ) M left join (SELECT serv_no, max(CREATION_DATE) max_day FROM dsgc_log_instance where INVOKE_RESULT = 'S'  GROUP BY serv_no) B on M.serv_no = B.serv_no left join dsgc_services ds on M.serv_no = ds.serv_no order by M.last_error_date desc,M.serv_no  ")
})
public class FailedInstanceResVO {
    private String trackId;
    private String servNo;
    private String servName;
    private String reqFromSys;
    private String resFromSys;
    private String reqFromSysName;
    private String resFromSysName;
    private String systemLeader;
    private String systemLeaderName;
    private String errorType;
    private String lastErrorDate;
    private String lastSuccessDate;
    private String invoke_result;

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public String getReqFromSysName() {
        return reqFromSysName;
    }

    public void setReqFromSysName(String reqFromSysName) {
        this.reqFromSysName = reqFromSysName;
    }

    public String getResFromSysName() {
        return resFromSysName;
    }

    public void setResFromSysName(String resFromSysName) {
        this.resFromSysName = resFromSysName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getReqFromSys() {
        return reqFromSys;
    }

    public void setReqFromSys(String reqFromSys) {
        this.reqFromSys = reqFromSys;
    }

    public String getResFromSys() {
        return resFromSys;
    }

    public void setResFromSys(String resFromSys) {
        this.resFromSys = resFromSys;
    }

    public String getSystemLeader() {
        return systemLeader;
    }

    public void setSystemLeader(String systemLeader) {
        this.systemLeader = systemLeader;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getLastErrorDate() {
        return lastErrorDate;
    }

    public void setLastErrorDate(String lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }

    public String getLastSuccessDate() {
        return lastSuccessDate;
    }

    public void setLastSuccessDate(String lastSuccessDate) {
        this.lastSuccessDate = lastSuccessDate;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getInvoke_result() {
        return invoke_result;
    }

    public void setInvoke_result(String invoke_result) {
        this.invoke_result = invoke_result;
    }

    public String getSystemLeaderName() {
        return systemLeaderName;
    }

    public void setSystemLeaderName(String systemLeaderName) {
        this.systemLeaderName = systemLeaderName;
    }

    @Override
    public String toString() {
        return "FailedInstanceResVO{" +
                "trackId='" + trackId + '\'' +
                ", servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", reqFromSys='" + reqFromSys + '\'' +
                ", resFromSys='" + resFromSys + '\'' +
                ", reqFromSysName='" + reqFromSysName + '\'' +
                ", resFromSysName='" + resFromSysName + '\'' +
                ", systemLeader='" + systemLeader + '\'' +
                ", systemLeaderName='" + systemLeaderName + '\'' +
                ", errorType='" + errorType + '\'' +
                ", lastErrorDate='" + lastErrorDate + '\'' +
                ", lastSuccessDate='" + lastSuccessDate + '\'' +
                ", invoke_result='" + invoke_result + '\'' +
                '}';
    }
}
