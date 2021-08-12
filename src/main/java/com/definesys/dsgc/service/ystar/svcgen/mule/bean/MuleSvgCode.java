package com.definesys.dsgc.service.ystar.svcgen.mule.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@SQLQuery(value = {
        @SQL(view = "V_MULE_SVG_CODE", sql = "select c.*," +
                "(select s.SYS_NAME from dsgc_system_entities s where s.SYS_CODE = c.SYS_CODE) as SYS_NAME," +
                "(select s.SYS_NAME from dsgc_system_entities s where s.SYS_CODE = c.TO_SYSTEM) as TO_SYSTEM_NAME," +
                "(select u.user_description from dsgc_user u where u.user_id = c.LAST_UPDATED_BY ) last_updated_by_name from mule_svg_code c ")
})
@Table(value = "MULE_SVG_CODE")
public class MuleSvgCode implements Serializable {

    private String mscId;
    private String svgCode;
    private String svgName;
    private String svgType;
    private String svgStatus;//状态 0-未发布 1-已发布
    private String sysCode;
    @Column(type = ColumnType.JAVA)
    private String sysName;
    private String toSystem;
    @Column(type = ColumnType.JAVA)
    private String toSystemName;
    private String psUri;
    private String prjName;
    private String svgObHeader;
    private String svcCode;

    private String attr1;
    private String attr2;
    private String attr3;
    private String attr4;
    private String attr5;
    private String attr6;
    private String attr7;
    private String attr8;
    private String attr9;
    private String attr10;
    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "object_version_number")
    private Integer objectVersionNumber;

    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;

    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;

    @Column(type = ColumnType.JAVA)
    private String lastUpdatedByName;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public MuleSvgCode() {
    }

    public MuleSvgCode(String svgObHeader) {
        this.svgObHeader = svgObHeader;
    }

    public MuleSvgCode(String svgCode, String svgName, String svgStatus, String sysCode, String toSystem, String prjName, String svgObHeader, String createdBy) {
        this.svgCode = svgCode;
        this.svgName = svgName;
        this.svgStatus = svgStatus;
        this.sysCode = sysCode;
        this.toSystem = toSystem;
        this.prjName = prjName;
        this.svgObHeader = svgObHeader;
        this.createdBy = createdBy;
    }

    public MuleSvgCode(String svgType, String svgObHeader) {
        this.svgType = svgType;
        this.svgObHeader = svgObHeader;
    }

    public String getPsUri() {
        return psUri;
    }

    public void setPsUri(String psUri) {
        this.psUri = psUri;
    }

    public String getSvgName() {
        return svgName;
    }

    public void setSvgName(String svgName) {
        this.svgName = svgName;
    }

    public String getMscId() {
        return mscId;
    }

    public void setMscId(String mscId) {
        this.mscId = mscId;
    }

    public String getSvgCode() {
        return svgCode;
    }

    public void setSvgCode(String svgCode) {
        this.svgCode = svgCode;
    }

    public String getSvgType() {
        return svgType;
    }

    public void setSvgType(String svgType) {
        this.svgType = svgType;
    }

    public String getSvgStatus() {
        return svgStatus;
    }

    public void setSvgStatus(String svgStatus) {
        this.svgStatus = svgStatus;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getToSystem() {
        return toSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }

    public String getToSystemName() {
        return toSystemName;
    }

    public void setToSystemName(String toSystemName) {
        this.toSystemName = toSystemName;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getSvgObHeader() {
        return svgObHeader;
    }

    public void setSvgObHeader(String svgObHeader) {
        this.svgObHeader = svgObHeader;
    }

    public String getSvcCode() {
        return svcCode;
    }

    public void setSvcCode(String svcCode) {
        this.svcCode = svcCode;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public String getAttr6() {
        return attr6;
    }

    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    public String getAttr7() {
        return attr7;
    }

    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    public String getAttr8() {
        return attr8;
    }

    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    public String getAttr9() {
        return attr9;
    }

    public void setAttr9(String attr9) {
        this.attr9 = attr9;
    }

    public String getAttr10() {
        return attr10;
    }

    public void setAttr10(String attr10) {
        this.attr10 = attr10;
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

    public String getLastUpdatedByName() {
        return lastUpdatedByName;
    }

    public void setLastUpdatedByName(String lastUpdatedByName) {
        this.lastUpdatedByName = lastUpdatedByName;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
