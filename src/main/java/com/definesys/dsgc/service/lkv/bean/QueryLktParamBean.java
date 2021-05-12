package com.definesys.dsgc.service.lkv.bean;

import java.util.List;

public class QueryLktParamBean {
    public String lookupType;

    public String lookupName;

    public List<QueryLkvParamBean> values;

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupName() {
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public List<QueryLkvParamBean> getValues() {
        return values;
    }

    public void setValues(List<QueryLkvParamBean> values) {
        this.values = values;
    }
}
