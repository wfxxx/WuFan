package com.definesys.dsgc.service.ystar.mg.otr;

import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.ystar.mg.bean.UserInfoBean;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryLktParamBean;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryLkvParamBean;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.ystar.mg.util.QueryUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("OtrDao")
public class OtrDao {
    @Autowired
    private MpaasQueryFactory sw;

    public List<Map<String, Object>> listQueryEnvInfoCfgByCode(String envType, String envCode) {
        return this.sw.buildQuery()
                .sql(" select i.ENV_CODE,i.ENV_NAME,s.SERVER_NAME,m.MACHINE_IP,s.SERVER_PORT from dsgc_env_machine_cfg m,dsgc_env_info_cfg i,dsgc_env_server_cfg s\n" +
                        " where m.env_code = i.ENV_CODE and i.ENV_CODE = s.ENV_CODE and i.ENV_TYPE = #envType and i.ENV_CODE = #envCode")
                .setVar("envCode", envCode)
                .setVar("envType", envType)
                .doQuery();
    }

    public List<Map<String, Object>> listQueryAllEnvInfoCfg(String envType) {
        return this.sw.buildQuery()
                .sql(" select i.ENV_CODE,i.ENV_NAME,s.SERVER_NAME,m.MACHINE_IP,s.SERVER_PORT from dsgc_env_machine_cfg m,dsgc_env_info_cfg i,dsgc_env_server_cfg s\n" +
                        " where m.env_code = i.ENV_CODE and i.ENV_CODE = s.ENV_CODE and i.ENV_TYPE = #envType ")
                .setVar("envType", envType)
                .doQuery();
    }

    public UserInfoBean sigQueryUserById(String userId) {
        return this.sw.buildQuery()
                .eq("userId", userId).doQueryFirst(UserInfoBean.class);
    }

    public List<QueryPropertyParamBean> queryProperties(QueryPropertyParamBean property) {
        String key = property.getPropertyKey();
        String sql = " select s.property_key,s.property_value from fnd_properties s where 1=1 ";
        if (StringUtils.isEmpty(key)) {
            return null;
        } else if (!"ALL".equals(key.toUpperCase())) {
            sql += " and s.property_key in (" + QueryUtil.getSqlWhereClause(key) + ")";
        }
        return sw.buildQuery()
                .sql(sql)
                .doQuery(QueryPropertyParamBean.class);
    }

    /*** 根据type 查询值列表*/
    public List<QueryLkvParamBean> queryLkvParamBean(String lookupType) {
        FndLookupType type = this.sw.buildQuery().eq("lookupType", lookupType).doQueryFirst(FndLookupType.class);
        List<QueryLkvParamBean> lkvList = null;
        if (type != null) {
            String lookupId = type.getLookupId();
            String sql = "select s.lookup_code,s.meaning lookup_value,s.tag lookup_tag,s.description from fnd_lookup_values s where s.lookup_id = '" + lookupId + "'";
            lkvList = this.sw.buildQuery().sql(sql).doQuery(QueryLkvParamBean.class);
        }
        return lkvList;
    }

    public List<QueryLktParamBean> queryLkvList(QueryLktParamBean lkvParamBean) {
        String sql = "select t.lookup_type,t.lookup_name from fnd_lookup_types t where 1=1 ";
        String lktCode = lkvParamBean.getLookupType();
        if (StringUtils.isEmpty(lktCode)) {
            return null;
        } else if (!"ALL".equals(lktCode.toUpperCase())) {
            sql += " and t.lookup_type in (" + QueryUtil.getSqlWhereClause(lktCode) + ")";
        }
        return this.sw.buildQuery().sql(sql).doQuery(QueryLktParamBean.class);
    }
}
