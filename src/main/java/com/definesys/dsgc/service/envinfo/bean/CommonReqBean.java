package com.definesys.dsgc.service.envinfo.bean;

import java.util.List;

public class CommonReqBean {
    private String con0;
    private List<DSGCEnvMachineCfg> machineList;
    private List<DSGCEnvServerCfg> servList;
    private List<SvcgenDeployControl> deployList;

    public String getCon0() {
        return con0;
    }

    public void setCon0(String con0) {
        this.con0 = con0;
    }

    public List<DSGCEnvMachineCfg> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<DSGCEnvMachineCfg> machineList) {
        this.machineList = machineList;
    }

    public List<DSGCEnvServerCfg> getServList() {
        return servList;
    }

    public void setServList(List<DSGCEnvServerCfg> servList) {
        this.servList = servList;
    }

    public List<SvcgenDeployControl> getDeployList() {
        return deployList;
    }

    public void setDeployList(List<SvcgenDeployControl> deployList) {
        this.deployList = deployList;
    }
}
