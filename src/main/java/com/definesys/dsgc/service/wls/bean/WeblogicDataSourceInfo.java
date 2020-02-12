package com.definesys.dsgc.service.wls.bean;

/**
 * @author Trel
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @title WeblogicDataSourceInfo
 * @description
 * @history: 1.2019/5/8 created by Trel
 * @since 2019/5/8 14:09
 **/
public class WeblogicDataSourceInfo implements Comparable<WeblogicDataSourceInfo>{
    private String dataSourceName;
    private String dataSourceState;
    private int currCapacity;
    private int activeConnectionHighCount;
    private int connectionDelayTime;
    private int connectionTotalCount;
    private int activeConnectionsCurrCount;
    private int activeConnectionsAvgCount;
    private int failToReconnectCount;
    private int waitingConnectionCount;
    private int waitingConnectionFailTotal;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceState() {
        return dataSourceState;
    }

    public void setDataSourceState(String dataSourceState) {
        this.dataSourceState = dataSourceState;
    }

    public int getCurrCapacity() {
        return currCapacity;
    }

    public void setCurrCapacity(int currCapacity) {
        this.currCapacity = currCapacity;
    }

    public int getActiveConnectionHighCount() {
        return activeConnectionHighCount;
    }

    public void setActiveConnectionHighCount(int activeConnectionHighCount) {
        this.activeConnectionHighCount = activeConnectionHighCount;
    }

    public int getConnectionDelayTime() {
        return connectionDelayTime;
    }

    public void setConnectionDelayTime(int connectionDelayTime) {
        this.connectionDelayTime = connectionDelayTime;
    }

    public int getConnectionTotalCount() {
        return connectionTotalCount;
    }

    public void setConnectionTotalCount(int connectionTotalCount) {
        this.connectionTotalCount = connectionTotalCount;
    }

    public int getActiveConnectionsCurrCount() {
        return activeConnectionsCurrCount;
    }

    public void setActiveConnectionsCurrCount(int activeConnectionsCurrCount) {
        this.activeConnectionsCurrCount = activeConnectionsCurrCount;
    }

    public int getActiveConnectionsAvgCount() {
        return activeConnectionsAvgCount;
    }

    public void setActiveConnectionsAvgCount(int activeConnectionsAvgCount) {
        this.activeConnectionsAvgCount = activeConnectionsAvgCount;
    }

    public int getFailToReconnectCount() {
        return failToReconnectCount;
    }

    public void setFailToReconnectCount(int failToReconnectCount) {
        this.failToReconnectCount = failToReconnectCount;
    }

    public int getWaitingConnectionCount() {
        return waitingConnectionCount;
    }

    public void setWaitingConnectionCount(int waitingConnectionCount) {
        this.waitingConnectionCount = waitingConnectionCount;
    }

    public int getWaitingConnectionFailTotal() {
        return waitingConnectionFailTotal;
    }

    public void setWaitingConnectionFailTotal(int waitingConnectionFailTotal) {
        this.waitingConnectionFailTotal = waitingConnectionFailTotal;
    }

    @Override
    public int compareTo(WeblogicDataSourceInfo o) {
        return o.currCapacity-this.currCapacity;
    }
}
