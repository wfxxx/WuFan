package com.definesys.dsgc.service.dagclient.proxy.bean;

import java.util.List;

public class IpRestPluginCfgVO {
    private List<String> blacklist;
    private List<String> whitelist;

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }
}
