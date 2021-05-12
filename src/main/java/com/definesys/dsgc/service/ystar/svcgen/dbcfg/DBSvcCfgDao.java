package com.definesys.dsgc.service.ystar.svcgen.dbcfg;

import com.definesys.dsgc.service.ystar.svcgen.dbcfg.bean.DBDeployProfileBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("DBSvcCfgDao")
public class DBSvcCfgDao {

    @Autowired
    private MpaasQueryFactory sw;

    public List<SvcgenConnBean> getDBConnList(String dbType) {
        return sw.buildQuery().eq("CONN_TYPE", "DB").eq("ATTR1", dbType).doQuery(SvcgenConnBean.class);
    }

    public SvcgenConnBean queryDBConnList(String dbType, String dbConn) {
        return sw.buildQuery().eq("CONN_TYPE", "DB").eq("ATTR1", dbType)
                .eq("CONN_NAME", dbConn)
                .doQueryFirst(SvcgenConnBean.class);
    }


    public void saveDBConnectInfo(SvcgenConnBean svcgenConnBean) {
        sw.buildQuery().doInsert(svcgenConnBean);
    }

    public SvcgenConnBean getDBConnDetailByName(String connName) {
        return sw.buildQuery().eq("conn_name", connName).doQueryFirst(SvcgenConnBean.class);
    }

    public DBDeployProfileBean getDBDeployProfileDetail(String dpId) {
        Map<String, Object> res = sw.buildQuery().sql("select d.dp_name,d.env_code,(select e.env_name from dsgc_env_info_cfg e where e.env_code = d.env_code) env_name,t.busi_serv_uri from dsgc_svcgen_deploy_profiles d,dsgc_svcgen_tmpl t where d.deve_id = t.deve_id and d.dp_id = #dpId ")
                .setVar("dpId", dpId).doQueryFirst();
        DBDeployProfileBean dpl = null;
        if (res != null && !res.isEmpty()) {
            dpl = new DBDeployProfileBean();
            dpl.setDplName((String) res.get("DP_NAME"));
            dpl.setEnvCode((String) res.get("ENV_CODE"));
            dpl.setJndiName((String) res.get("BUSI_SERV_URI"));
            dpl.setEnvCodeMeaning((String) res.get("ENV_NAME"));
        }
        return dpl;
    }
}
