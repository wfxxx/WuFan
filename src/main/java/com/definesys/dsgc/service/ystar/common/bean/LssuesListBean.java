package com.definesys.dsgc.service.ystar.common.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateDeserializer;
import com.definesys.mpaas.query.json.MpaasDateSerializer;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;


@Table(value = "Lssues_List")
public class LssuesListBean extends MpaasBasePojo {

       @RowID(sequence="LSSUES_LIST_S",type = RowIDType.AUTO)
       private Long lid;
       @Column(value = "SYS_CODE")
       private String sysCode;
       @Column(value = "CSM_CODE")
       private String csmCode;
       @Column(value = "SERV_NO")
       private String servNo;
       @Column(value = "DESCRIPTION")
       private String description;
       @Column(value = "CATEGROY")
       private String categroy;
       @Column(value = "LEVEL_J")
       private String levelJ;
       @Column(value = "TO_SOLVE")
       private String toSolve;

       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
       @JsonSerialize(using = MpaasDateSerializer.class)
       @JsonDeserialize(using = MpaasDateDeserializer.class)
       @SystemColumn(SystemColumnType.CREATE_ON)
       @Column(value = "creation_date")
       private Date creationDate;
       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
       @JsonSerialize(using = MpaasDateSerializer.class)
       @JsonDeserialize(using = MpaasDateDeserializer.class)
       @SystemColumn(SystemColumnType.LASTUPDATE_ON)
       @Column(value = "updation_date")
       private Date updationDate;

       private String sysName;
       private String csmName;
       private String servName;

       public String getSysName() {
              return sysName;
       }

       public void setSysName(String sysName) {
              this.sysName = sysName;
       }

       public String getCsmName() {
              return csmName;
       }

       public void setCsmName(String csmName) {
              this.csmName = csmName;
       }

       public String getServName() {
              return servName;
       }

       public void setServName(String servName) {
              this.servName = servName;
       }

       public Long getLid() {
              return lid;
       }

       public void setLid(Long lid) {
              this.lid = lid;
       }


       public String getDescription() {
              return description;
       }

       public void setDescription(String description) {
              this.description = description;
       }

       public String getCategroy() {
              return categroy;
       }

       public void setCategroy(String categroy) {
              this.categroy = categroy;
       }

       public String getLevelJ() {
              return levelJ;
       }

       public String getSysCode() {
              return sysCode;
       }

       public void setSysCode(String sysCode) {
              this.sysCode = sysCode;
       }

       public String getCsmCode() {
              return csmCode;
       }

       public void setCsmCode(String csmCode) {
              this.csmCode = csmCode;
       }

       public String getServNo() {
              return servNo;
       }

       public void setServNo(String servNo) {
              this.servNo = servNo;
       }

       public void setLevelJ(String levelJ) {
              this.levelJ = levelJ;
       }

       public String getToSolve() {
              return toSolve;
       }

       public void setToSolve(String toSolve) {
              this.toSolve = toSolve;
       }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(Date updationDate) {
        this.updationDate = updationDate;
    }

       @Override
       public String toString() {
              return "LssuesListBean{" +
                      "lid=" + lid +
                      ", sysCode='" + sysCode + '\'' +
                      ", csmCode='" + csmCode + '\'' +
                      ", servNo='" + servNo + '\'' +
                      ", description='" + description + '\'' +
                      ", categroy='" + categroy + '\'' +
                      ", levelJ='" + levelJ + '\'' +
                      ", toSolve='" + toSolve + '\'' +
                      ", creationDate=" + creationDate +
                      ", updationDate=" + updationDate +
                      '}';
       }
}
