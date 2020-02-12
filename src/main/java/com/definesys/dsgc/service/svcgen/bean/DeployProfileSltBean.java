package com.definesys.dsgc.service.svcgen.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@SQLQuery(value={
        @SQL(view="V_GET_DPL_LIST",sql="SELECT D.DP_ID,D.DP_NAME,D.ENV_CODE,(SELECT E.ENV_NAME FROM DSGC_ENV_INFO_CFG E WHERE E.ENV_CODE = D.ENV_CODE) ENV_NAME\n" +
                "  FROM DSGC_SVCGEN_DEPLOY_PROFILES D ,DSGC_SVCGEN_TMPL T\n" +
                " WHERE D.ENV_CODE IN\n" +
                "       (SELECT C.ENV_CODE\n" +
                "          FROM DSGC_SVCGEN_DEPLOY_CONTROL C, DSGC_USER U\n" +
                "         WHERE U.USER_ROLE = C.ROLE_CODE AND U.USER_ID = #UID)\n" +
                "   AND D.DEVE_ID = T.DEVE_ID\n" +
                "   AND D.IS_ENABLE = 'Y'\n" +
                "   AND T.IS_PROFILE = 'Y'\n" +
                "   AND T.SERV_NO = #SERVNO")
})
public class DeployProfileSltBean extends MpaasBasePojo {

    @Column(value = "DP_ID", type = ColumnType.DB)
    private String dpId;
    @Column(value = "DP_NAME", type = ColumnType.DB)
    private String dpName;
    @Column(value = "ENV_CODE", type = ColumnType.DB)
    private String envCode;
    @Column(value = "ENV_NAME", type = ColumnType.DB)
    private String envName;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }
}
