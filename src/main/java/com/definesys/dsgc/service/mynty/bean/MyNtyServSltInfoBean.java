package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class MyNtyServSltInfoBean {
    @RowID(type = RowIDType.UUID)
    @Column(value = "serv_no", type = ColumnType.DB)
    private String servNo;
    @Column(value = "serv_name", type = ColumnType.DB)
    private String servName;
    @Column(value = "serv_system", type = ColumnType.DB)
    private String servSystem;

    @JsonSerialize(using = MpaasDateTimeSerializer.class)
    @JsonDeserialize(using = MpaasDateTimeDeserializer.class)
    @Column(value = "creation_date", type = ColumnType.DB)
    private Date creationDate;


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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
