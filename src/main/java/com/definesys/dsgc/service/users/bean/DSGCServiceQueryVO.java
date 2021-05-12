package com.definesys.dsgc.service.users.bean;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/6/13 上午10:37
 * @history: 1.2019/6/13 created by biao.luo
 */
public class DSGCServiceQueryVO {
    private String servNo;

    private String servName;

    private String servDesc;
    private String subordinateSystem;

    //标签条件查询
    private String tag;
    //服务状态
    private String servStatus;

    private Integer pageIndex;

    private Integer pageSize;

    private String uid;

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

    public String getServDesc() {
        return servDesc;
    }

    public void setServDesc(String servDesc) {
        this.servDesc = servDesc;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSubordinateSystem() {
        return subordinateSystem;
    }

    public void setSubordinateSystem(String subordinateSystem) {
        this.subordinateSystem = subordinateSystem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getServStatus() {
        return servStatus;
    }

    public void setServStatus(String servStatus) {
        this.servStatus = servStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "DSGCServiceQueryVO{" +
                "servNo='" + servNo + '\'' +
                ", servName='" + servName + '\'' +
                ", servDesc='" + servDesc + '\'' +
                ", subordinateSystem='" + subordinateSystem + '\'' +
                ", tag='" + tag + '\'' +
                ", servStatus='" + servStatus + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
