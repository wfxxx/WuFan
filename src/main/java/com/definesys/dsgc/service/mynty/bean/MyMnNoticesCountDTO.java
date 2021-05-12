package com.definesys.dsgc.service.mynty.bean;

public class MyMnNoticesCountDTO {

    private long allCount;
    private long unreadCount;

    public long getAllCount() {
        return allCount;
    }

    public void setAllCount(long allCount) {
        this.allCount = allCount;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
