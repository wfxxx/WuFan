package com.definesys.dsgc.service.mynty.bean;

public class PushNoticeDTO {

    private String sendTo;
    private String ntyTitle;
    private String ntySour;
    private String mnLevel;
    private String cntShort;
    private String cntText;
    private String cntFormat;

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getNtyTitle() {
        return ntyTitle;
    }

    public void setNtyTitle(String ntyTitle) {
        this.ntyTitle = ntyTitle;
    }

    public String getCntShort() {
        return cntShort;
    }

    public void setCntShort(String cntShort) {
        this.cntShort = cntShort;
    }

    public String getCntText() {
        return cntText;
    }

    public void setCntText(String cntText) {
        this.cntText = cntText;
    }

    public String getCntFormat() {
        return cntFormat;
    }

    public void setCntFormat(String cntFormat) {
        this.cntFormat = cntFormat;
    }

    public String getNtySour() {
        return ntySour;
    }

    public void setNtySour(String ntySour) {
        this.ntySour = ntySour;
    }

    public String getMnLevel() {
        return mnLevel;
    }

    public void setMnLevel(String mnLevel) {
        this.mnLevel = mnLevel;
    }
}
