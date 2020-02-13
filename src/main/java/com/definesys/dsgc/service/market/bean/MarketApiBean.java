package com.definesys.dsgc.service.market.bean;

import com.definesys.mpaas.query.annotation.*;

@Table("DSGC_APIS")
public class MarketApiBean {

    @RowID(type = RowIDType.UUID)
    private String apiId;
    private String apiName;
    private String apiDesc;
    private String appCode;
    private String marketCategory;
    private String marketStat;
    @Column(type = ColumnType.JAVA)
    private String type;
    @Column(type = ColumnType.JAVA)
    private String cateName;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getMarketCategory() {
        return marketCategory;
    }

    public void setMarketCategory(String marketCategory) {
        this.marketCategory = marketCategory;
    }

    public String getMarketStat() {
        return marketStat;
    }

    public void setMarketStat(String marketStat) {
        this.marketStat = marketStat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
