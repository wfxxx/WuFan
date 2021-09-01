package com.definesys.dsgc.service.ystar.svcgen.mule.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;


@SQLQuery(value = {
        @SQL(view = "V_MULE_SVG_DEPLOY", sql = " select d.*,(select c.ENV_NAME from dsgc_env_info_cfg c where c.env_code = d.env_code ) as ENV_NAME,\n" +
                "(select u.user_description from dsgc_user u where u.user_id = d.LAST_UPDATED_BY ) as LAST_UPDATED_BY_NAME,\n" +
                "(select u.user_description from dsgc_user u where u.user_id = d.DEPLOY_USER ) as DEPLOY_USER_NAME\n" +
                " from mule_svg_deploy d ")
})
@Table(value = "MULE_SVG_DEPLOY")
public class MuleSvgDeploy implements Serializable {
    @RowID( type = RowIDType.UUID)
    private String msdId;
    private String svgCode;
    private String envCode;
    @Column(type = ColumnType.JAVA)
    private String envName;
    private String msdName;
    private String bsUri;
    private String svgObHeader;
    private String attr1;
    private String attr2;
    private String attr3;
    private String deployUser;
    @Column(type = ColumnType.JAVA)
    private String deployUserName;
    private Date deployDate;

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

    public MuleSvgDeploy() {
    }

    public MuleSvgDeploy(String svgObHeader) {
        this.svgObHeader = svgObHeader;
    }

    public MuleSvgDeploy(String svgCode, String svgObHeader, String createdBy) {
        this.svgCode = svgCode;
        this.svgObHeader = svgObHeader;
        this.createdBy = createdBy;
    }

    public String getMsdId() {
        return msdId;
    }

    public void setMsdId(String msdId) {
        this.msdId = msdId;
    }

    public String getSvgCode() {
        return svgCode;
    }

    public Date getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    public void setSvgCode(String svgCode) {
        this.svgCode = svgCode;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getMsdName() {
        return msdName;
    }

    public void setMsdName(String msdName) {
        this.msdName = msdName;
    }

    public String getBsUri() {
        return bsUri;
    }

    public void setBsUri(String bsUri) {
        this.bsUri = bsUri;
    }

    public String getSvgObHeader() {
        return svgObHeader;
    }

    public void setSvgObHeader(String svgObHeader) {
        this.svgObHeader = svgObHeader;
    }

    public String getDeployUser() {
        return deployUser;
    }

    public void setDeployUser(String deployUser) {
        this.deployUser = deployUser;
    }

    public String getDeployUserName() {
        return deployUserName;
    }

    public void setDeployUserName(String deployUserName) {
        this.deployUserName = deployUserName;
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
