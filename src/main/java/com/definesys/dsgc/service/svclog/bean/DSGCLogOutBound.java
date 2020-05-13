package com.definesys.dsgc.service.svclog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Table(value = "DSGC_LOG_OUTBOUND")
public class DSGCLogOutBound {

    @RowID(type = RowIDType.UUID)
    private String obId;    //日志主键
    private String trackId;
    private String runTimes; //出栈批次
    private String  token;  //Token
    private String servNo;
    private String servName;//出栈请求服务名
    private String reqFrom; //请求方
    private String sendTo; //接收方
    private String reqTime; //请求时间
    private String resTime; // 响应时间
    @Column(type = ColumnType.JAVA)
    private String costDesc;
    private String resBodyLob; //异常信息
    private String reqHeaderLob; //出栈请求Header
    private String status; //出栈状态
    private String invokeResult; //出栈反馈结果
    private String results;  //出栈响应结果
    private String reqBodyLob;  //出栈请求报文（DB）
    private Long errorLob;
    private String server;
    private String plCompress;
    private String plStoreType;

    @Column(type = ColumnType.JAVA)
    private String attribute1;
    @Column(type = ColumnType.JAVA)
    private String attribute2;
    @Column(type = ColumnType.JAVA)
    private String attribute3;
    @Column(type = ColumnType.JAVA)
    private String attribute4;
    @Column(type = ColumnType.JAVA)
    private String attribute5;

    @Column(type = ColumnType.JAVA)
    private Integer objectVersionNumber;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(type = ColumnType.JAVA)
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(type = ColumnType.JAVA)
    private Date lastUpdateDate;

    //add by legolas20200513
    private void costDescSet(){
        try{
            if(this.reqTime != null && this.resTime != null){
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


    public Date getStartTimeDate() throws ParseException {
        if(this.reqTime == null){
            return null;
        }

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.parse(this.reqTime);


    }

    public Date getEndTimeDate() throws Exception{
        if(this.resTime == null){
            return null;
        }


        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.parse(this.resTime);
    }


    public String getCostDesc() {
        return costDesc;
    }

    public void setCostDesc(String costDesc) {
        this.costDesc = costDesc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(String runTimes) {
        this.runTimes = runTimes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = this.getBugFixDate(reqTime);
        this.costDescSet();
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = this.getBugFixDate(resTime);
        this.costDescSet();
    }

    public String getObId() {
        return obId;
    }

    public void setObId(String obId) {
        this.obId = obId;
    }

    public String getReqHeaderLob() {
        return reqHeaderLob;
    }

    public void setReqHeaderLob(String reqHeaderLob) {
        this.reqHeaderLob = reqHeaderLob;
    }

    public String getReqBodyLob() {
        return reqBodyLob;
    }

    public void setReqBodyLob(String reqBodyLob) {
        this.reqBodyLob = reqBodyLob;
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

    public String getResBodyLob() {
        return resBodyLob;
    }

    public void setResBodyLob(String resBodyLob) {
        this.resBodyLob = resBodyLob;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public Long getErrorLob() {
        return errorLob;
    }

    public void setErrorLob(Long errorLob) {
        this.errorLob = errorLob;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
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

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public Integer getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Integer objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString(){
        StringBuilder arg=new StringBuilder("");
        arg.append("obId : ").append(this.obId);
        arg.append("trackId : ").append(this.trackId);
        arg.append("runTimes : ").append(this.runTimes);
        arg.append("token : ").append(this.token);
        arg.append("servNo : ").append(this.servNo);
        arg.append("servName : ").append(this.servName);
        arg.append("reqFrom : ").append(this.reqFrom);
        arg.append("sendTo : ").append(this.sendTo);
        arg.append("reqTime : ").append(this.reqTime);
        arg.append("resTime : ").append(this.resTime);
        arg.append("resBodyLob : ").append(this.resBodyLob);
        arg.append("reqHeaderLob : ").append(this.reqHeaderLob);
        arg.append("status : ").append(this.status);
        arg.append("invokeResult : ").append(this.invokeResult);
        arg.append("results : ").append(this.results);
        arg.append("reqBodyLob : ").append(this.reqBodyLob);
        arg.append("errorLob : ").append(this.errorLob);
        arg.append("server : ").append(this.server);
        arg.append("creationDate : ").append(this.creationDate);
        arg.append("createdBy : ").append(this.createdBy);
        arg.append("lastUpdateDate : ").append(this.lastUpdateDate);
        arg.append("lastUpdatedBy : ").append(this.lastUpdatedBy);
        arg.append("plCompress : ").append(this.plCompress);
        arg.append("plStoreType : ").append(this.plStoreType);
        arg.append("Attribute1 : ").append(this.attribute1);
        arg.append("Attribute2 : ").append(this.attribute2);
        arg.append("Attribute3 : ").append(this.attribute3);
        arg.append("Attribute4 : ").append(this.attribute4);
        arg.append("Attribute5 : ").append(this.attribute5);
        return new String(arg);
    }
}
