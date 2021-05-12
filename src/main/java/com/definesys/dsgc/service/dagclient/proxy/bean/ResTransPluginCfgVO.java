package com.definesys.dsgc.service.dagclient.proxy.bean;

public class ResTransPluginCfgVO {
    private TransResRemoveVO remove;
    private TransResCommonVO replace;
    private TransResCommonVO add;
    private TransResCommonVO append;
    private TransResRenameVO rename;

    public TransResRemoveVO getRemove() {
        return remove;
    }

    public void setRemove(TransResRemoveVO remove) {
        this.remove = remove;
    }

    public TransResCommonVO getReplace() {
        return replace;
    }

    public void setReplace(TransResCommonVO replace) {
        this.replace = replace;
    }

    public TransResCommonVO getAdd() {
        return add;
    }

    public void setAdd(TransResCommonVO add) {
        this.add = add;
    }

    public TransResCommonVO getAppend() {
        return append;
    }

    public void setAppend(TransResCommonVO append) {
        this.append = append;
    }

    public TransResRenameVO getRename() {
        return rename;
    }

    public void setRename(TransResRenameVO rename) {
        this.rename = rename;
    }
}
