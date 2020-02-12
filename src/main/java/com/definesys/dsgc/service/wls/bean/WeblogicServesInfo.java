package com.definesys.dsgc.service.wls.bean;

public class WeblogicServesInfo implements Comparable<WeblogicServesInfo>{
	private String host;
    private String port;
    private String service;
    private String activateDate;
    private String servicePort;
    private String listenAddress;
    private String serviceStatus;
    private String serviceHealthStatus;
    private String heapSizeMax;
    private String heapSizeCurrent;
    private String heapFreeCurrent;
    private String heapFreePercent;
    private String openSocketsCurrentCount;
    private String socketsOpenedTotalCount;
    private String executeThreadTotalCount;
    private String executeThreadIdleCount;
    private String standbyThreadCount;
    private String queueLength;
    private String throughput;
    private String activeExeThreadCount;
    private String suspended;
    private String pendingUserRequestCount;
    private String hoggingThreadCount;
    private String threadHealthStatus;
    private String memTotal;
    private String memUse;
	public String getHost() { 
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getActivateDate() {
		return activateDate;
	}
	public void setActivateDate(String activateDate) {
		this.activateDate = activateDate;
	}
	public String getServicePort() {
		return servicePort;
	}
	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
	}	
	public String getListenAddress() {
		return listenAddress;
	}
	public void setListenAddress(String listenAddress) {
		this.listenAddress = listenAddress;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getServiceHealthStatus() {
		return serviceHealthStatus;
	}
	public void setServiceHealthStatus(String serviceHealthStatus) {
		this.serviceHealthStatus = serviceHealthStatus;
	}
	public String getHeapSizeMax() {
		return heapSizeMax;
	}
	public void setHeapSizeMax(String heapSizeMax) {
		this.heapSizeMax = heapSizeMax;
	}
	public String getHeapSizeCurrent() {
		return heapSizeCurrent;
	}
	public void setHeapSizeCurrent(String heapSizeCurrent) {
		this.heapSizeCurrent = heapSizeCurrent;
	}
	public String getHeapFreeCurrent() {
		return heapFreeCurrent;
	}
	public void setHeapFreeCurrent(String heapFreeCurrent) {
		this.heapFreeCurrent = heapFreeCurrent;
	}
	public String getHeapFreePercent() {
		return heapFreePercent;
	}
	public void setHeapFreePercent(String heapFreePercent) {
		this.heapFreePercent = heapFreePercent;
	}
	public String getOpenSocketsCurrentCount() {
		return openSocketsCurrentCount;
	}
	public void setOpenSocketsCurrentCount(String openSocketsCurrentCount) {
		this.openSocketsCurrentCount = openSocketsCurrentCount;
	}
	public String getSocketsOpenedTotalCount() {
		return socketsOpenedTotalCount;
	}
	public void setSocketsOpenedTotalCount(String socketsOpenedTotalCount) {
		this.socketsOpenedTotalCount = socketsOpenedTotalCount;
	}
	public String getExecuteThreadTotalCount() {
		return executeThreadTotalCount;
	}
	public void setExecuteThreadTotalCount(String executeThreadTotalCount) {
		this.executeThreadTotalCount = executeThreadTotalCount;
	}
	public String getExecuteThreadIdleCount() {
		return executeThreadIdleCount;
	}
	public void setExecuteThreadIdleCount(String executeThreadIdleCount) {
		this.executeThreadIdleCount = executeThreadIdleCount;
	}
	public String getStandbyThreadCount() {
		return standbyThreadCount;
	}
	public void setStandbyThreadCount(String standbyThreadCount) {
		this.standbyThreadCount = standbyThreadCount;
	}
	public String getQueueLength() {
		return queueLength;
	}
	public void setQueueLength(String queueLength) {
		this.queueLength = queueLength;
	}
	public String getThroughput() {
		return throughput;
	}
	public void setThroughput(String throughput) {
		this.throughput = throughput;
	}
	public String getActiveExeThreadCount() {
		return activeExeThreadCount;
	}
	public void setActiveExeThreadCount(String activeExeThreadCount) {
		this.activeExeThreadCount = activeExeThreadCount;
	}
	public String getSuspended() {
		return suspended;
	}
	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}
	public String getPendingUserRequestCount() {
		return pendingUserRequestCount;
	}
	public void setPendingUserRequestCount(String pendingUserRequestCount) {
		this.pendingUserRequestCount = pendingUserRequestCount;
	}
	public String getHoggingThreadCount() {
		return hoggingThreadCount;
	}
	public void setHoggingThreadCount(String hoggingThreadCount) {
		this.hoggingThreadCount = hoggingThreadCount;
	}
	public String getThreadHealthStatus() {
		return threadHealthStatus;
	}
	public void setThreadHealthStatus(String threadHealthStatus) {
		this.threadHealthStatus = threadHealthStatus;
	}

	public String getMemTotal() {
		return memTotal;
	}

	public void setMemTotal(String memTotal) {
		this.memTotal = memTotal;
	}

	public String getMemUse() {
		return memUse;
	}

	public void setMemUse(String memUse) {
		this.memUse = memUse;
	}

	@Override
	public int compareTo(WeblogicServesInfo o) {
		return this.service.compareTo(o.getService());
	}
    

}
