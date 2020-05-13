package com.definesys.dsgc.service.svclog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SQLQuery(value = {
        @SQL(view = "Administrators_view", sql = "select loin.*, ss.attribue5 as sys_code from dsgc_log_instance loin,dsgc_services ss where loin.serv_no = ss.serv_no(+)  "),
        @SQL(view = "Have_Sys_SystemLeader_view", sql = "select * from( select loin.*, ss.attribue5 as sys_code from dsgc_log_instance loin,dsgc_services ss where loin.serv_no = ss.serv_no(+)) where sys_code = #sysCode "),
        @SQL(view = "UnHave_Sys_SystemLeader_view", sql = " select lgins.*,su.user_id,su.user_name from(select loin.*, ss.attribue5 as sys_code from dsgc_log_instance loin, dsgc_services ss where loin.serv_no = ss.serv_no(+)) lgins, dsgc_service_user su where lgins.serv_no = su.serv_no and su.user_id = #userId and su.is_show = 'Y'  "),
        @SQL(view = "Tourist_view", sql = "select lgins.*,su.user_id,su.user_name from(select loin.*, ss.attribue5 as sys_code from dsgc_log_instance loin, dsgc_services ss where loin.serv_no = ss.serv_no(+)) lgins, dsgc_service_user su where lgins.serv_no = su.serv_no and su.user_id = #userId and su.is_show = 'Y'  "),
        //日志实例查询使用到的视图 Log_Instance_view  、UnBizKey_Log_Ins_view
        @SQL(view = "Log_Instance_view",sql = " select logs.* from(select track_id from dsgc_log_bizkey biz where biz.bus_serv_no = #servNo and (biz.Col1 = #COL1 OR biz.Col2 = #COL2 OR biz.Col3 = #COL3 OR biz.Col4 = #COL4 OR biz.Col5 = #COL5 OR biz.Col6 = #COL6 OR biz.Col7 = #COL7 OR biz.Col8 = #COL8 OR biz.Col9 = #COL9 OR biz.Col10 = #COLTEN)) t_id, (select insv.*, bpv.payload_data from dsgc_log_instance_v insv, (select pv.track_id, xmlagg(xmlparse(content to_char(pv.payload_data) || '<======报文结束======>' wellformed) order by pv.track_id asc) .getclobval() payload_data from dsgc_log_body_payload_v pv group by pv.track_id) bpv where insv.track_id = bpv.track_id) logs where logs.track_id = t_id.track_id(+) "),
        @SQL(view = "UnBizKey_Log_Ins_view",sql = " select insv.*,bpv.payload_data from dsgc_log_instance_v insv,( select pv.track_id, xmlagg(xmlparse(content to_char(pv.payload_data) || '<======报文结束======>' wellformed) order by pv.track_id asc) .getclobval() payload_data from dsgc_log_body_payload_v pv group by pv.track_id) bpv where insv.track_id = bpv.track_id(+) "),
        @SQL(view = "HaveBizKey_Log_Ins_view",sql = " select logs.* from(select track_id from dsgc_log_bizkey biz where biz.bus_serv_no = #servNo and (biz.Col1 = #COL1 OR biz.Col2 = #COL2 OR biz.Col3 = #COL3 OR biz.Col4 = #COL4 OR biz.Col5 = #COL5 OR biz.Col6 = #COL6 OR biz.Col7 = #COL7 OR biz.Col8 = #COL8 OR biz.Col9 = #COL9 OR biz.Col10 = #COLTEN)) t_id, dsgc_log_instance logs where logs.track_id = t_id.track_id(+)  "),

        @SQL(view = "Tourist_Log_Instance_view",sql = "select temp.* from (select logs.* from(select track_id from dsgc_log_bizkey biz where biz.bus_serv_no = #servNo and (biz.Col1 = #COL1 OR biz.Col2 = #COL2 OR biz.Col3 = #COL3 OR biz.Col4 = #COL4 OR biz.Col5 = #COL5 OR biz.Col6 = #COL6 OR biz.Col7 = #COL7 OR biz.Col8 = #COL8 OR biz.Col9 = #COL9 OR biz.Col10 = #COLTEN)) t_id,dsgc_log_instance logs" +
                " where logs.track_id = t_id.track_id) temp,dsgc_service_user su where su.serv_no = temp.serv_no AND su.user_id = #userId AND su.is_show = 'Y' ")
})
@Table(value = "dsgc_log_instance")
public class DSGCLogInstance {

    @RowID(type = RowIDType.UUID)
    private String trackId;  //日志实例ID
    private String token;  //token名
    private String servNo; //服务编码
    private String servName; //服务名称
    private String reqFrom;  //请求方
    private String servOper;   //服务执行的方法

    private String startTime;  //请求时间
    private String endTime;   //结束时间

    @Column(type = ColumnType.JAVA)
    private String costDesc;  //耗时时长
    private String clientIp;  //请求方IP
    private String instStatus;//实例状态
    private String bizStatus;
    private String bizStatusDtl; //发送详情
    private Integer runTimes;  //执行次数
    private String resTime;   //同步响应时间
    private String invokeResult; //出栈反馈结果
    private String results;  //出栈响应结果
    private String ibLob;   //请求报文
    private Integer msgNum;//报文数量
    private Double msgSize;//报文大小
    @Column(type = ColumnType.JAVA)
    private String msDesc;//报文大小描述
    private String obLob;
    private String plCompress;
    private String plStoreType;
    private String server;   //运行的服务器
    private Long sequencesId;
    private String billGuid;
    private String aysnStatus;
    private String accessUser;

    private String bizKey1; // 业务关键字1

    private String contentType;

    private String userHeaders;
    //服务接收方（Java字段，不参与此表的增删改）
    @Column(type = ColumnType.JAVA)
    private String sendTo;

    //日志实例对应的报文
    @Column(type = ColumnType.JAVA)
    private String payloadData;

    @Column(type = ColumnType.JAVA)
    private Integer objectVersionNumber;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    private String urlParams;

    //add by legolas20200513
    private void custMSDescSet(Double msgSize){
        if(msgSize != null){
            double md = msgSize.doubleValue();
            if(md >= 1024d && md <1024d*1024d){
                BigDecimal b = new BigDecimal(md / (1024d*1024d)).setScale(3,RoundingMode.UP);
                this.msDesc = b.doubleValue() +"MB";
            } else if (md >= 1024d*1024d){
                BigDecimal b = new BigDecimal(md / (1024d*1024d)).setScale(3,RoundingMode.UP);
                this.msDesc = b.doubleValue() +"GB";
            } else {
                this.msDesc = md+"KB";
            }

        } else {
            this.msDesc="";
        }
    }

    //add by legolas20200513
    private void costDescSet(){
        try{
        if(this.startTime != null && this.endTime != null){
            long cost  = this.getEndTimeDate().getTime() - this.getStartTimeDate().getTime();
            if(cost < 1000){
                this.costDesc = cost +"毫秒";
            } else if (cost >= 1000 && cost < 60000){
                double costd = Double.valueOf(cost);
                BigDecimal b = new BigDecimal(costd / 1000).setScale(2,RoundingMode.UP);
                this.costDesc = b.doubleValue() + "秒";
            } else {
                double costd = Double.valueOf(cost);
                BigDecimal b = new BigDecimal(costd / 60000).setScale(2,RoundingMode.UP);
                this.costDesc = b.doubleValue() + "分钟";
            }
        } else {
            this.costDesc = "";
        }}catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getBugFixDate(String time) {
        if(time != null) {
            //倚天框架bug，毫秒最后的0会被去掉。。。。
            String s = time.substring(time.indexOf("."));
            if(s == null){
                time +=".000";
            }else if (s.length() == 1) {
                time += "000";
            } else if (s.length() == 2) {
                time += "00";
            } else if (s.length() == 3) {
                time += "0";
            }
        }
        return time;
    }

    private String userHeadersFormat(String userheaders){
        if(userheaders != null){
            return userheaders.replace("&"," \n ");
        } else {
            return null;
        }
    }

    public String getBizKey1() {
        return bizKey1;
    }

    public void setBizKey1(String bizKey1) {
        this.bizKey1 = bizKey1;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInstStatus() {
        return instStatus;
    }

    public void setInstStatus(String instStatus) {
        this.instStatus = instStatus;
    }

    public Integer getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Integer runTimes) {
        this.runTimes = runTimes;
    }

    public Integer getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(Integer msgNum) {
        this.msgNum = msgNum;
    }

    public Double getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(Double msgSize) {
        this.msgSize = msgSize;
        this.custMSDescSet(msgSize);
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public String getReqFrom() {
        return reqFrom;
    }

    public void setReqFrom(String reqFrom) {
        this.reqFrom = reqFrom;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = this.getBugFixDate(startTime);
        this.costDescSet();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime =  this.getBugFixDate(endTime);
        this.costDescSet();
    }

    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }

    public Date getStartTimeDate() throws ParseException {
        if(startTime == null){
            return null;
        }

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.parse(startTime);


    }

    public Date getEndTimeDate() throws Exception{
        if(endTime == null){
            return null;
        }


        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.parse(endTime);
    }





    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }





    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = resTime;
    }

    public String getInvokeResult() {
        return invokeResult;
    }

    public void setInvokeResult(String invokeResult) {
        this.invokeResult = invokeResult;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getBizStatusDtl() {
        return bizStatusDtl;
    }

    public void setBizStatusDtl(String bizStatusDtl) {
        this.bizStatusDtl = bizStatusDtl;
    }

    public String getIbLob() {
        return ibLob;
    }

    public void setIbLob(String ibLob) {
        this.ibLob = ibLob;
    }

    public String getServOper() {
        return servOper;
    }

    public void setServOper(String servOper) {
        this.servOper = servOper;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getObLob() {
        return obLob;
    }

    public void setObLob(String obLob) {
        this.obLob = obLob;
    }

    public String getPlCompress() {
        return plCompress;
    }

    public void setPlCompress(String plCompress) {
        this.plCompress = plCompress;
    }

    public String getPlStoreType() {
        return plStoreType;
    }

    public void setPlStoreType(String plStoreType) {
        this.plStoreType = plStoreType;
    }

    public Long getSequencesId() {
        return sequencesId;
    }

    public void setSequencesId(Long sequencesId) {
        this.sequencesId = sequencesId;
    }

    public String getBillGuid() {
        return billGuid;
    }

    public void setBillGuid(String billGuid) {
        this.billGuid = billGuid;
    }

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getAysnStatus() {
        return aysnStatus;
    }

    public void setAysnStatus(String aysnStatus) {
        this.aysnStatus = aysnStatus;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getPayloadData() {
        return payloadData;
    }

    public void setPayloadData(String payloadData) {
        this.payloadData = payloadData;
    }

    @Override
    public String toString() {
        return "DSGCLogInstance{" +
                "trackId='" + trackId + '\'' +
                ", token='" + token + '\'' +
                ", servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", reqFrom='" + reqFrom + '\'' +
                ", servOper='" + servOper + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", instStatus='" + instStatus + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", bizStatusDtl='" + bizStatusDtl + '\'' +
                ", runTimes=" + runTimes +
                ", resTime='" + resTime + '\'' +
                ", invokeResult='" + invokeResult + '\'' +
                ", results='" + results + '\'' +
                ", ibLob='" + ibLob + '\'' +
                ", msgNum=" + msgNum +
                ", msgSize=" + msgSize +
                ", obLob='" + obLob + '\'' +
                ", plCompress='" + plCompress + '\'' +
                ", plStoreType='" + plStoreType + '\'' +
                ", server='" + server + '\'' +
                ", sequencesId=" + sequencesId +
                ", billGuid='" + billGuid + '\'' +
                ", aysnStatus='" + aysnStatus + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getAccessUser() {
        return accessUser;
    }

    public void setAccessUser(String accessUser) {
        this.accessUser = accessUser;
    }

    public String getMsDesc() {
        return msDesc;
    }

    public void setMsDesc(String msDesc) {
        this.msDesc = msDesc;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUserHeaders() {
        return userHeaders;
    }

    public void setUserHeaders(String userHeaders) {

        this.userHeaders = this.userHeadersFormat(userHeaders);
    }
}
