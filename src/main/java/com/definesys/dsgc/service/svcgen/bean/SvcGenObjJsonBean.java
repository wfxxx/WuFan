package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class SvcGenObjJsonBean {
    private static final String BASE_SQL= "select t.obj_id,\n" +
            "        t.obj_type,\n" +
            "        t.obj_code,\n" +
            "        (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'SVCGEN_OBJ_TYPE' and fv.lookup_code = t.obj_type) obj_type_meaning,\n" +
            "        t.obj_name,\n" +
            "        t.obj_desc,\n" +
            "        t.sys_code,\n" +
            "        (select e.sys_name from dsgc_system_entities e where e.sys_code = t.sys_code) sys_code_meaning,\n" +
            "        t.serv_no,\n" +
            "        s.serv_name,\n" +
            "        s.subordinate_system serv_system,\n" +
            "        (select e.sys_name from dsgc_system_entities e where e.sys_code = s.subordinate_system) serv_system_meaning,\n" +
            "        s.share_type serv_share_type,\n" +
            "        (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'SVC_SHARE_TYPE' and fv.lookup_code = s.share_type) serv_share_type_meaning,\n" +
            "        t.is_enable enabled,\n" +
            "        (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'SVCGEN_OBJ_ENABLED' and fv.lookup_code = t.is_enable) enabled_meaning,\n" +
            "        v.stat vc_stat,\n" +
            "        (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'SVCGEN_VC_STAT' and fv.lookup_code = v.stat) vc_stat_meaning,\n" +
            "        p.last_update_date,\n" +
            "        p.last_updated_by,\n" +
            "        p.created_by,\n" +
            "        (select u.user_name from dsgc_user u where u.user_id = p.last_updated_by) last_updated_by_name,\n" +
            "        h.tmpl_code,\n" +
            "         (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and (ft.lookup_type = 'SVCGEN_TMPL_LIST' or ft.lookup_type = 'SVCGEN_CMPT_TMPL_LIST') and fv.lookup_code = h.tmpl_code||'') tmpl_code_meaning\n" +
            "   from dsgc_svcgen_obj t,\n" +
            "        dsgc_services s,\n" +
            "        dsgc_svcgen_vc v,\n" +
            "        dsgc_svcgen_files_header h,\n" +
            "        dsgc_svcgen_tmpl p\n" +
            "  where t.obj_code = p.serv_no\n" +
            "    and p.is_profile = 'N'\n" +
            "    and t.serv_no = s.serv_no(+)\n" +
            "    and t.obj_code = h.serv_no(+)\n" +
            "    and h.is_enable(+) = 'Y'\n" +
            "    and h.vid = v.vid(+) \n";

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
