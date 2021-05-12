package com.definesys.dsgc.service.mynty.bean;

import java.util.List;

public class MyMnNoticesDTO {
    private Integer allCount;
    private Integer unreadCount;
    private List<DSGCMnNotices> notices;

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<DSGCMnNotices> getNotices() {
        return notices;
    }

    public void setNotices(List<DSGCMnNotices> notices) {
        this.notices = notices;
    }
}
