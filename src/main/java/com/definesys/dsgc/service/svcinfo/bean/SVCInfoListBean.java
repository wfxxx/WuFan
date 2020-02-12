package com.definesys.dsgc.service.svcinfo.bean;

public class SVCInfoListBean {
    public static final String QUERY_LIST_SQL = "select * from (select s.SERV_NO," +
            "s.serv_name," +
            "(select e.sys_name from dsgc_system_entities e where e.sys_code = s.subordinate_system) ss_meaning," +
            "(select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'SVC_SHARE_TYPE' and fv.lookup_code = s.share_type) sst_meaning  from DSGC_SERVICES s) ";
    private String servNo;
    private String servName;
    private String ssMeaning;
    private String sstMeaning;

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

    public String getSsMeaning() {
        return ssMeaning;
    }

    public void setSsMeaning(String ssMeaning) {
        this.ssMeaning = ssMeaning;
    }

    public String getSstMeaning() {
        return sstMeaning;
    }

    public void setSstMeaning(String sstMeaning) {
        this.sstMeaning = sstMeaning;
    }
}

