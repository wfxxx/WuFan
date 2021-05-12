package com.definesys.dsgc.service.dagclient.bean;

public class DeployResBean {
    private String rtnCode;
    private String rtnMsg;
    private String rtnValue;

    public static DeployResBean success(String value){
        DeployResBean r = new DeployResBean();
        r.setRtnCode("S");
        r.setRtnValue(value);
        return r;
    }

    public static DeployResBean success(){
        DeployResBean r = new DeployResBean();
        r.setRtnCode("S");
        return r;
    }

    public static DeployResBean error(String rtnMsg){
        DeployResBean r = new DeployResBean();
        r.setRtnCode("E");
        r.setRtnMsg(rtnMsg);
        return r;
    }

    public boolean isSuccess(){
        if("S".equals(rtnCode)){
            return true;
        } else{
            return false;
        }
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public String getRtnValue() {
        return rtnValue;
    }

    public void setRtnValue(String rtnValue) {
        this.rtnValue = rtnValue;
    }


}
