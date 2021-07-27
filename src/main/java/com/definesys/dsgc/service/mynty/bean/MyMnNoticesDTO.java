package com.definesys.dsgc.service.mynty.bean;

import com.definesys.mpaas.query.db.PageQueryResult;

import java.util.List;

public class MyMnNoticesDTO {
    private Integer allCount;
    private Integer unreadCount;
    private PageQueryResult<DSGCMnNotices> notices;

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

    public PageQueryResult<DSGCMnNotices> getNotices() {
        return notices;
    }

    public void setNotices(PageQueryResult<DSGCMnNotices> notices) {
        this.notices = notices;
    }
}
