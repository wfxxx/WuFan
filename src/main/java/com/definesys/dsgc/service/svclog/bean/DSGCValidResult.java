package com.definesys.dsgc.service.svclog.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @author zhenglong
 * @Date 2019/3/13 12:27
 */


@Table(value = "DSGC_VALID_RESULT")
@ApiModel(value = "有效对应信息", description = "系统服务对应信息")
public class DSGCValidResult extends MpaasBasePojo implements Serializable {
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="demo_emp_seq")
//    @SequenceGenerator(name="demo_emp_seq1", sequenceName="DEMO_SEQ1")
    @Column(value = "serv_no")
    private String servNo;
    @Column(value = "system_code")
    private String systemCode;
    @Column(value = "valid_type")
    private String validType;
    @Column(value = "valid_column")
    private String  validColumn;
    @Column (value = "valid_value")
    private String validValue;
    @Column (value = "warning_value")
    private String warningValue;
    @Column (value = "msg_value")
    private String msgValue;

    @Column(value = "attribue1")
    private String attribue1;
    @Column(value = "attribue2")
    private String attribue2;
    @Column(value = "attribue3")
    private String attribue3;
    @Column(value = "attribue4")
    private String attribue4;
    @Column(value = "attribue5")
    private String attribue5;


    @SystemColumn(SystemColumnType.OBJECT_VERSION)
    @Column(value = "version_number")
    private Integer versionNumber;
    @SystemColumn(SystemColumnType.CREATE_BY)
    @Column(value = "created_by")
    private String createdBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.CREATE_ON)
    @Column(value = "creation_date")
    private Date creationDate;
    @SystemColumn(SystemColumnType.LASTUPDATE_BY)
    @Column(value = "last_updated_by")
    private String lastUpdatedBy;
    @JsonSerialize(using = MpaasDateSerializer.class)
    @JsonDeserialize(using = MpaasDateDeserializer.class)
    @SystemColumn(SystemColumnType.LASTUPDATE_ON)
    @Column(value = "last_update_date")
    private Date lastUpdateDate;


    public DSGCValidResult(){
        super();
    }

    public DSGCValidResult(String servNo, String systemCode, String validType, String validColumn, String validValue, String attribue1, String attribue2, String attribue3, String attribue4, String attribue5, Integer versionNumber, String createdBy, Date creationDate, String lastUpdatedBy, Date lastUpdateDate) {
        this.servNo = servNo;
        this.systemCode = systemCode;
        this.validType = validType;
        this.validColumn = validColumn;
        this.validValue = validValue;
        this.attribue1 = attribue1;
        this.attribue2 = attribue2;
        this.attribue3 = attribue3;
        this.attribue4 = attribue4;
        this.attribue5 = attribue5;
        this.versionNumber = versionNumber;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getServNo() {

        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public String getValidColumn() {
        return validColumn;
    }

    public void setValidColumn(String validColumn) {
        this.validColumn = validColumn;
    }

    public String getValidValue() {
        return validValue;
    }

    public void setValidValue(String validValue) {
        this.validValue = validValue;
    }

    public String getAttribue1() {
        return attribue1;
    }

    public void setAttribue1(String attribue1) {
        this.attribue1 = attribue1;
    }

    public String getAttribue2() {
        return attribue2;
    }

    public void setAttribue2(String attribue2) {
        this.attribue2 = attribue2;
    }

    public String getAttribue3() {
        return attribue3;
    }

    public void setAttribue3(String attribue3) {
        this.attribue3 = attribue3;
    }

    public String getAttribue4() {
        return attribue4;
    }

    public void setAttribue4(String attribue4) {
        this.attribue4 = attribue4;
    }

    public String getAttribue5() {
        return attribue5;
    }

    public void setAttribue5(String attribue5) {
        this.attribue5 = attribue5;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
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

    public String getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(String warningValue) {
        this.warningValue = warningValue;
    }

    public String getMsgValue() {
        return msgValue;
    }

    public void setMsgValue(String msgValue) {
        this.msgValue = msgValue;
    }

    @Override
    public String toString() {
        return "DSGCValidResult{" +
                "servNo='" + servNo + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", validType='" + validType + '\'' +
                ", validColumn='" + validColumn + '\'' +
                ", validValue='" + validValue + '\'' +
                ", warningValue='" + warningValue + '\'' +
                ", msgValue='" + msgValue + '\'' +
                ", attribue1='" + attribue1 + '\'' +
                ", attribue2='" + attribue2 + '\'' +
                ", attribue3='" + attribue3 + '\'' +
                ", attribue4='" + attribue4 + '\'' +
                ", attribue5='" + attribue5 + '\'' +
                ", versionNumber=" + versionNumber +
                ", createdBy='" + createdBy + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
