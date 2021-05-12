package com.definesys.dsgc.service.bpm.bean;

public class BpmInstanceDTO extends BpmInstanceBean {

    private String curNodeName;
    private String processName;



    public String getCurNodeName() {
        return curNodeName;
    }

    public void setCurNodeName(String curNodeName) {
        this.curNodeName = curNodeName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
