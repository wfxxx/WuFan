package com.definesys.dsgc.service.dagclient.proxy.bean;

public class BasicAuthPluginCfgVO{
    private boolean hide_credentials = true;
    private String anonymous;

    public boolean isHide_credentials() {
        return hide_credentials;
    }

    public void setHide_credentials(boolean hide_credentials) {
        this.hide_credentials = hide_credentials;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }
}
