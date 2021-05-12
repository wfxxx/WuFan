package com.definesys.dsgc.service.bpm.bean;

public class BpmHistoryDTO extends BpmHistoryBean{
    private String nodeName;
    private String instTitle;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getInstTitle() {
        return instTitle;
    }

    public void setInstTitle(String instTitle) {
        this.instTitle = instTitle;
    }
}
