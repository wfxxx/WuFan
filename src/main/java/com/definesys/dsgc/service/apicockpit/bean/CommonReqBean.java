package com.definesys.dsgc.service.apicockpit.bean;

import java.util.Date;

/**
 * @ClassName CommonReqBean
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:58
 * @Version 1.0
 **/
public class CommonReqBean {
    private String con0;
    private String queryType;
    private Date startTime;
    private Date endTime;

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
