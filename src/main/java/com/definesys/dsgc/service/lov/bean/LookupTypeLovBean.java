package com.definesys.dsgc.service.lov.bean;

public class LookupTypeLovBean {

    public static final String SQL_GET_LOV_BY_LKT = "select v.lookup_code, v.meaning, v.tag\n" +
            "        from fnd_lookup_types t, fnd_lookup_values v\n" +
            "        where t.lookup_id = v.lookup_id\n" +
            "        and t.lookup_type = #lookupType";

    private String lookupCode;
    private String meaning;
    private String tag;

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
