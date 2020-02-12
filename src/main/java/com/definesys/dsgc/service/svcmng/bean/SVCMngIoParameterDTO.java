package com.definesys.dsgc.service.svcmng.bean;

import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;

public class SVCMngIoParameterDTO {
    private String nodeName;
    private String nodeValue;
    private String nodeType;
    private String nodeDesc;
    private String dataType;
    private Boolean required; //是否必输

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }


}
