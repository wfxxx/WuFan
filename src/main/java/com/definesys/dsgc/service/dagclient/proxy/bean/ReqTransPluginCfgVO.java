package com.definesys.dsgc.service.dagclient.proxy.bean;

public class ReqTransPluginCfgVO {
    private TransCommonVO remove;
    private TransReplaceVO replace;
    private String http_method;
    private TransCommonVO add;
    private TransCommonVO append;
    private TransCommonVO rename;

    public TransCommonVO getRemove() {
        return remove;
    }

    public void setRemove(TransCommonVO remove) {
        this.remove = remove;
    }

    public TransReplaceVO getReplace() {
        return replace;
    }

    public void setReplace(TransReplaceVO replace) {
        this.replace = replace;
    }

    public String getHttp_method() {
        return http_method;
    }

    public void setHttp_method(String http_method) {
        this.http_method = http_method;
    }

    public TransCommonVO getAdd() {
        return add;
    }

    public void setAdd(TransCommonVO add) {
        this.add = add;
    }

    public TransCommonVO getAppend() {
        return append;
    }

    public void setAppend(TransCommonVO append) {
        this.append = append;
    }

    public TransCommonVO getRename() {
        return rename;
    }

    public void setRename(TransCommonVO rename) {
        this.rename = rename;
    }
}
