package com.definesys.dsgc.service.dagclient.proxy.bean;

public class ReqTransPluginCfgVO {
    private TransReqCommonVO remove;
    private TransReqReplaceVO replace;
    private String http_method;
    private TransReqCommonVO add;
    private TransReqCommonVO append;
    private TransReqCommonVO rename;

    public TransReqCommonVO getRemove() {
        return remove;
    }

    public void setRemove(TransReqCommonVO remove) {
        this.remove = remove;
    }

    public TransReqReplaceVO getReplace() {
        return replace;
    }

    public void setReplace(TransReqReplaceVO replace) {
        this.replace = replace;
    }

    public String getHttp_method() {
        return http_method;
    }

    public void setHttp_method(String http_method) {
        this.http_method = http_method;
    }

    public TransReqCommonVO getAdd() {
        return add;
    }

    public void setAdd(TransReqCommonVO add) {
        this.add = add;
    }

    public TransReqCommonVO getAppend() {
        return append;
    }

    public void setAppend(TransReqCommonVO append) {
        this.append = append;
    }

    public TransReqCommonVO getRename() {
        return rename;
    }

    public void setRename(TransReqCommonVO rename) {
        this.rename = rename;
    }
}
