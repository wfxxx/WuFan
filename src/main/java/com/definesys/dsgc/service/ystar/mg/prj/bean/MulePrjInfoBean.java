package com.definesys.dsgc.service.ystar.mg.prj.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: ystar
 * @since: 2021/7/9 上午9:59
 * @history: upd by ystar 2020-12-27
 */
@SQLQuery(value = {
        @SQL(view = "V_MULE_PRJ_INFO", sql = "select prj_id,prj_name,prj_desc,sub_prj_name,prj_status,s.sys_code,(select e.SYS_NAME from dsgc_system_entities e where e.SYS_CODE = s.SYS_CODE) sys_name " +
                "from mule_prj_info s")
})
@Table("mule_prj_info")
@ApiModel(value = "系统项目目录表", description = "此表用于项目划分系统部署")
public class MulePrjInfoBean extends MpaasBasePojo implements Serializable {
    @ApiModelProperty(value = "系统工程id")
    private String prjId;
    @ApiModelProperty(value = "系统工程目录名称", notes = "系统的工程目录名称，如：IAM,HR,OHS等")
    private String prjName;
    @ApiModelProperty(value = "系统工程目录描述")
    private String prjDesc;
    @ApiModelProperty(value = "子目录")
    private String subPrjName;
    @Column(type = ColumnType.JAVA)
    private String subPrjNameList;
    @ApiModelProperty(value = "子目录名称")
    private String sysCode;
    @ApiModelProperty(value = "项目状态，0-弃用;1-启用")
    private String prjStatus;
    @ApiModelProperty(value = "备用字段1", notes = "")
    private String textAttribute1;
    @ApiModelProperty(value = "备用字段2", notes = "")
    private String textAttribute2;
    @ApiModelProperty(value = "备用字段3", notes = "")
    private String textAttribute3;
    @Column(type = ColumnType.JAVA)
    private String sysName; //系统名称

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

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;

    public String getSubPrjNameList() {
        if (StringUtil.isNotBlank(this.subPrjName)) {
            this.subPrjNameList = "";
            JSONArray arr = JSONArray.parseArray(this.subPrjName);
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                subPrjNameList += obj.get("code") + ",";
            }
            if (subPrjNameList.contains(",")) {
                subPrjNameList = subPrjNameList.substring(0, subPrjNameList.length() - 1);
            }
        }
        return subPrjNameList;
    }

    public String getPrjId() {
        return prjId;
    }

    public void setPrjId(String prjId) {
        this.prjId = prjId;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getPrjDesc() {
        return prjDesc;
    }

    public void setPrjDesc(String prjDesc) {
        this.prjDesc = prjDesc;
    }

    public String getSubPrjName() {
        return subPrjName;
    }

    public void setSubPrjName(String subPrjName) {
        this.subPrjName = subPrjName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getPrjStatus() {
        return prjStatus;
    }

    public void setPrjStatus(String prjStatus) {
        this.prjStatus = prjStatus;
    }

    public String getTextAttribute1() {
        return textAttribute1;
    }

    public void setTextAttribute1(String textAttribute1) {
        this.textAttribute1 = textAttribute1;
    }

    public String getTextAttribute2() {
        return textAttribute2;
    }

    public void setTextAttribute2(String textAttribute2) {
        this.textAttribute2 = textAttribute2;
    }

    public String getTextAttribute3() {
        return textAttribute3;
    }

    public void setTextAttribute3(String textAttribute3) {
        this.textAttribute3 = textAttribute3;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
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

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return "MulePrjInfoBean{" +
                "prjId='" + prjId + '\'' +
                ", prjName='" + prjName + '\'' +
                ", prjDesc='" + prjDesc + '\'' +
                ", subPrjName='" + subPrjName + '\'' +
                ", subPrjNameList='" + subPrjNameList + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", prjStatus='" + prjStatus + '\'' +
                ", textAttribute1='" + textAttribute1 + '\'' +
                ", textAttribute2='" + textAttribute2 + '\'' +
                ", textAttribute3='" + textAttribute3 + '\'' +
                ", sysName='" + sysName + '\'' +
                ", objectVersionNumber=" + objectVersionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
