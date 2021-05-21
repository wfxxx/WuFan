package com.definesys.dsgc.service.ystar.file.bean;

public class ScpConnectEntity {
    String filePath;
    private String trgIp;
    private String trgPath;
    private String userName;
    private String passWord;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getTrgIp() {
        return trgIp;
    }

    public void setTrgIp(String trgIp) {
        this.trgIp = trgIp;
    }

    public String getTrgPath() {
        return trgPath;
    }

    public void setTrgPath(String trgPath) {
        this.trgPath = trgPath;
    }
}
