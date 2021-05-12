package com.definesys.dsgc.service.projdir.bean;

import com.definesys.mpaas.query.annotation.RowID;
import com.definesys.mpaas.query.annotation.RowIDType;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import com.definesys.mpaas.query.model.MpaasBasePojo;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/6/21 上午10:24
 * @history: 1.2019/6/21 created by biao.luo
 */
@SQLQuery(value = {
        @SQL(view = "pro_dir_view", sql = "SELECT pd.proj_id, pd.proj_name, pd.proj_desc,pd.proj_port, pd.sys_code, ss.sys_name ,pd.text_attribute1,pd.text_attribute2,pd.text_attribute3 FROM DSGC_SVCGEN_PROJ_INFO pd, DSGC_SYSTEM_ENTITIES ss WHERE pd.sys_code = ss.sys_code(+)")
})
public class SysProfileDirVO extends MpaasBasePojo {
    @RowID(sequence = "DSGC_SVCGEN_PROJ_INFO_S", type = RowIDType.AUTO)
    @ApiModelProperty(value = "系统工程id")
    private String projId;

    @ApiModelProperty(value = "系统工程目录名称", notes = "系统的工程目录名称，如：IAM,HR,OHS等")
    private String projName;

    @ApiModelProperty(value = "系统工程目录描述")
    private String projDesc;

    @ApiModelProperty(value = "系统表主键", notes = "关联系统表主键(DSGC_SYSTEM_ENTITIES.SYS_CODE)")
    private String sysCode;

    @ApiModelProperty(value = "系统名称")
    private String sysName;

    @ApiModelProperty(value = "版本号")
    private String textAttribute1;

    @ApiModelProperty(value = "备用字段2")
    private String textAttribute2;
    @ApiModelProperty(value = "备用字段3")
    private String textAttribute3;

    public String getTextAttribute3() {
        return textAttribute3;
    }

    public void setTextAttribute3(String textAttribute3) {
        this.textAttribute3 = textAttribute3;
    }

    @ApiModelProperty(value = "项目类型", notes = "项目类型：SOAP、REST、DB、RFC")
    private String projType;
    @ApiModelProperty(value = "备用字段1", notes = "用于存放远程仓库地址")
    private String repoUri;

    @ApiModelProperty(value = "备用字段2", notes = "用于存放远程仓库 分支")
    private String branchName;

    @ApiModelProperty(value = "初始化状态")
    private String initStatus;

    @ApiModelProperty(value = "项目端口")
    private String projPort;

    public String getProjPort() {
        return projPort;
    }

    public void setProjPort(String projPort) {
        this.projPort = projPort;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public String getRepoUri() {
        return repoUri;
    }

    public void setRepoUri(String repoUri) {
        this.repoUri = repoUri;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getInitStatus() {
        return initStatus;
    }

    public void setInitStatus(String initStatus) {
        this.initStatus = initStatus;
    }

    public String getTextAttribute1() {
        return textAttribute1;
    }

    public void setTextAttribute1(String textAttribute1) {
        this.textAttribute1 = textAttribute1;
    }

    public String getTextAttribute2() {
        return textAttribute2;
    }

    public void setTextAttribute2(String textAttribute2) {
        this.textAttribute2 = textAttribute2;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjDesc() {
        return projDesc;
    }

    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

}
