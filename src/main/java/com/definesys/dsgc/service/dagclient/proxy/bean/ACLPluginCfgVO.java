package com.definesys.dsgc.service.dagclient.proxy.bean;

public class ACLPluginCfgVO {

    private String[] whitelist;
    private String[] blacklist;

    public String[] getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String[] whitelist) {
        this.whitelist = whitelist;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String[] blacklist) {
        this.blacklist = blacklist;
    }
}
