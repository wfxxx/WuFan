package com.definesys.dsgc.service.ystar.svcgen.bean;

import java.util.List;

/**
 * TODO
 *
 * @author afan
 * @version 1.0
 * @date 2020/8/31 15:01
 */
public class WSDLObjectBean {

    private String portName;

    private String portAddress;

    private List<String> operations;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(String portAddress) {
        this.portAddress = portAddress;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
