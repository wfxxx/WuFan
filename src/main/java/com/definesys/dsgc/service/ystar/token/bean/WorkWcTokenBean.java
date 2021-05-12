package com.definesys.dsgc.service.ystar.token.bean;

import com.definesys.mpaas.query.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table("DSGC_WORK_WC_TOKEN")
public class WorkWcTokenBean {

    @RowID(type = RowIDType.UUID)
    @Column("TOKEN_ID")
    private String tokenId;
    @Column("CORP_ID")
    private String corpId;
    @Column("CORP_SECRET")
    private String corpSecret;
    @Column("TOKEN")
    private String token;

    @Column("START_TIME")
    @SystemColumn(SystemColumnType.CREATE_ON)
    @DateTimeFormat(style = "yyyy-MM-dd HH24:mi:ss")
    private Date startTime;
    @Column("END_TIME")
    @DateTimeFormat(style = "yyyy-MM-dd HH24:mi:ss")
    private Date endTime;


    public WorkWcTokenBean() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WorkWcTokenBean{" +
                "tokenId='" + tokenId + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpSecret='" + corpSecret + '\'' +
                ", token='" + token + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
