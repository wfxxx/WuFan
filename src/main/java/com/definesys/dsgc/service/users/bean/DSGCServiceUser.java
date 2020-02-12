package com.definesys.dsgc.service.users.bean;

import com.definesys.mpaas.query.annotation.*;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@Table(value = "dsgc_service_user")
public class DSGCServiceUser extends MpaasBasePojo {
    @RowID(type = RowIDType.UUID)
    private String servUserId;
    private String serviceId;
    private String servNo;
//    @Column(type = ColumnType.JAVA)
    private String userName;
    private String userId;
    private String isShow;
    private String isModify;
    private String remark;

    public String getServUserId() {
        return servUserId;
    }

    public void setServUserId(String servUserId) {
        this.servUserId = servUserId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsModify() {
        return isModify;
    }

    public void setIsModify(String isModify) {
        this.isModify = isModify;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString(){
        StringBuilder arg=new StringBuilder("");
        arg.append("servUserId : ").append(this.servUserId).append(",");
        arg.append(" serviceId : ").append(this.serviceId).append(",");
        arg.append(" servNo : ").append(this.servNo).append(",");
        arg.append(" userId : ").append(this.userId).append(",");
        arg.append(" isShow : ").append(this.isShow).append(",");
        arg.append(" isModify : ").append(this.isModify).append(",");
        arg.append(" remark : ").append(this.remark).append(",");
        return arg.toString();
    }

}
