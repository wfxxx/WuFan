package com.definesys.dsgc.service.market.bean;

import com.definesys.mpaas.query.annotation.Column;

import java.util.Date;

/**
 * @ClassName MarketEntiy
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-11 11:21
 * @Version 1.0
 **/
public class MarketEntiy {

    private String servId;
    @Column(value = "servNo")
    private String servNo;
    @Column(value = "servName")
    private String servName;
    @Column(value = "servDesc")
    private String servDesc;
    @Column(value = "fromSys")
    private String fromSys;
    @Column(value = "marketStat")
    private String marketStat;
    @Column(value = "marketCategory")
    private String marketCategory;
   @Column(value = "totalTimes")
    private String totalTimes;
    @Column(value = "creationDate")
    private Date creationDate;

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
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

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public String getFromSys() {
        return fromSys;
    }

    public void setFromSys(String fromSys) {
        this.fromSys = fromSys;
    }

    public String getMarketStat() {
        return marketStat;
    }

    public void setMarketStat(String marketStat) {
        this.marketStat = marketStat;
    }

    public String getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(String marketCategory) {
        this.marketCategory = marketCategory;
    }

    public String getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(String totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
