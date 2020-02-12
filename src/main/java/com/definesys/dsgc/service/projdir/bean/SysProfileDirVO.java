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
        @SQL(view = "pro_dir_view", sql = "SELECT pd.proj_id, pd.proj_name, pd.proj_desc, pd.sys_code, ss.sys_name FROM DSGC_SVCGEN_PROJ_INFO pd, DSGC_SYSTEM_ENTITIES ss WHERE pd.sys_code = ss.sys_code(+)")
})
public class SysProfileDirVO extends MpaasBasePojo {
    @RowID(sequence = "DSGC_SVCGEN_PROJ_INFO_S",type= RowIDType.AUTO)
    @ApiModelProperty(value = "系统工程id")
    private String projId;

    @ApiModelProperty(value = "系统工程目录名称",notes = "系统的工程目录名称，如：IAM,HR,OHS等")
    private String projName;

    @ApiModelProperty(value = "系统工程目录描述")
    private String projDesc;

    @ApiModelProperty(value = "系统表主键",notes = "关联系统表主键(DSGC_SYSTEM_ENTITIES.SYS_CODE)")
    private String sysCode;

    @ApiModelProperty(value = "系统名称")
    private String sysName;

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
