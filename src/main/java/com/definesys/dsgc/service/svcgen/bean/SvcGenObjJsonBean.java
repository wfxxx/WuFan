package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class SvcGenObjJsonBean {
    private static final String BASE_SQL=
    "SELECT t.obj_id,t.obj_type,t.obj_code,(SELECT fv.meaning FROM fnd_lookup_types ft,fnd_lookup_values fv " +
            "WHERE ft.lookup_id = fv.lookup_id AND ft.lookup_type = 'SVCGEN_OBJ_TYPE' AND fv.lookup_code = t.obj_type ) obj_type_meaning," +
            "t.obj_name,t.obj_desc,t.sys_code,( SELECT e.sys_name FROM dsgc_system_entities e WHERE e.sys_code = t.sys_code ) sys_code_meaning," +
            "t.serv_no,s.serv_name,s.subordinate_system serv_system,( SELECT e.sys_name FROM dsgc_system_entities e WHERE e.sys_code = s.subordinate_system ) serv_system_meaning,s.share_type serv_share_type,(SELECT fv.meaning FROM fnd_lookup_types ft,fnd_lookup_values fv WHERE ft.lookup_id = fv.lookup_id AND ft.lookup_type = 'SVC_SHARE_TYPE' AND fv.lookup_code = s.share_type " +
            ") serv_share_type_meaning,t.is_enable enabled,(SELECT fv.meaning FROM fnd_lookup_types ft,fnd_lookup_values fv WHERE " +
            "ft.lookup_id = fv.lookup_id AND ft.lookup_type = 'SVCGEN_OBJ_ENABLED' AND fv.lookup_code = t.is_enable ) enabled_meaning,v.stat vc_stat,(SELECT fv.meaning FROM fnd_lookup_types ft,fnd_lookup_values fv WHERE ft.lookup_id = fv.lookup_id " +
            "AND ft.lookup_type = 'SVCGEN_VC_STAT' AND fv.lookup_code = v.stat ) vc_stat_meaning,p.last_update_date,p.last_updated_by," +
            "p.created_by,( SELECT u.user_name FROM dsgc_user u WHERE u.user_id = p.last_updated_by ) last_updated_by_name,h.tmpl_code," +
            "(SELECT fv.meaning FROM fnd_lookup_types ft,fnd_lookup_values fv WHERE ft.lookup_id = fv.lookup_id " +
            "AND ( ft.lookup_type = 'SVCGEN_TMPL_LIST' OR ft.lookup_type = 'SVCGEN_CMPT_TMPL_LIST' ) AND fv.lookup_code = h.tmpl_code || '' " +
            ") tmpl_code_meaning FROM dsgc_svcgen_obj t left join dsgc_svcgen_tmpl p on t.obj_code = p.serv_no left join dsgc_services s " +
            "on t.serv_no = s.serv_no left join dsgc_svcgen_files_header h on t.obj_code = h.serv_no and h.is_enable = 'Y' left join dsgc_svcgen_vc v " +
            "on h.vid = v.vid where p.is_profile = 'N' ";

    private String objId;
    private String objType;
    private String objTypeMeaning;
    private String objCode;
    private String objName;
    private String objDesc;
    private String sysCode;
    private String sysCodeMeaning;
    private String servNo;
    private String servName;
    private String servSystem;
    private String servSystemMeaning;
    private String servShareType;
    private String servShareTypeMeaning;
    private String enabled;
    private String enabledMeaning;
    private String vcStat;
    private String vcStatMeaning;
    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    private Date lastUpdateDate;
    private String lastUpdatedBy;
    private String lastUpdatedByName;
    private String tmplCode;
    private String tmplCodeMeaning;
    private String createdBy;
    private boolean readonly = true;

    public static String getBaseQueryAllSql(){
        return  BASE_SQL + " order by  p.last_update_date desc ";
    }

    public static String getBaseQuerySql(boolean isSvcType){
        String sql = BASE_SQL;
        if(isSvcType){
            sql += "    and t.obj_type = 'SVC' \n";
        } else {
            sql += "    and t.obj_type <> 'SVC' \n";
        }
       return sql += " order by  p.last_update_date desc ";
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjTypeMeaning() {
        return objTypeMeaning;
    }

    public void setObjTypeMeaning(String objTypeMeaning) {
        this.objTypeMeaning = objTypeMeaning;
    }

    public String getObjCode() {
        return objCode;
    }

    public void setObjCode(String objCode) {
        this.objCode = objCode;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
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

    public String getServSystem() {
        return servSystem;
    }

    public void setServSystem(String servSystem) {
        this.servSystem = servSystem;
    }

    public String getServSystemMeaning() {
        return servSystemMeaning;
    }

    public void setServSystemMeaning(String servSystemMeaning) {
        this.servSystemMeaning = servSystemMeaning;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getEnabledMeaning() {
        return enabledMeaning;
    }

    public void setEnabledMeaning(String enabledMeaning) {
        this.enabledMeaning = enabledMeaning;
    }

    public String getVcStat() {
        return vcStat;
    }

    public void setVcStat(String vcStat) {
        this.vcStat = vcStat;
    }

    public String getVcStatMeaning() {
        return vcStatMeaning;
    }

    public void setVcStatMeaning(String vcStatMeaning) {
        this.vcStatMeaning = vcStatMeaning;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedByName() {
        return lastUpdatedByName;
    }

    public void setLastUpdatedByName(String lastUpdatedByName) {
        this.lastUpdatedByName = lastUpdatedByName;
    }

    public String getTmplCode() {
        return tmplCode;
    }

    public void setTmplCode(String tmplCode) {
        this.tmplCode = tmplCode;
    }

    public String getTmplCodeMeaning() {
        return tmplCodeMeaning;
    }

    public void setTmplCodeMeaning(String tmplCodeMeaning) {
        this.tmplCodeMeaning = tmplCodeMeaning;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getServShareType() {
        return servShareType;
    }

    public void setServShareType(String servShareType) {
        this.servShareType = servShareType;
    }

    public String getServShareTypeMeaning() {
        return servShareTypeMeaning;
    }

    public void setServShareTypeMeaning(String servShareTypeMeaning) {
        this.servShareTypeMeaning = servShareTypeMeaning;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysCodeMeaning() {
        return sysCodeMeaning;
    }

    public void setSysCodeMeaning(String sysCodeMeaning) {
        this.sysCodeMeaning = sysCodeMeaning;
    }

    public String getObjDesc() {
        return objDesc;
    }

    public void setObjDesc(String objDesc) {
        this.objDesc = objDesc;
    }
}
