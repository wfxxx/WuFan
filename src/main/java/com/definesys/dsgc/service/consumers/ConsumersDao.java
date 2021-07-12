package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.consumers.bean.*;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.swing.event.ListDataEvent;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumersDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Value("${database.type}")
    private String dbType;

    public PageQueryResult<DSGCConsumerEntities> queryconsumersList(CommonReqBean commonReqBean, int pageSize, int pageIndex, String userName, String userRole) {
        StringBuffer strSql = null;
        if ("oracle".equals(dbType)) {
            strSql = new StringBuffer(" select * from (select cnt.dce_id,cnt.csm_code,cnt.csm_name,cnt.csm_desc," +
                    "               cnt.deploy_env,t.user_name owner,cnt.creation_date creationDate " +
                    "          from DSGC_CONSUMER_ENTITIES cnt," +
                    "               (select listagg(u.user_name, ',') within GROUP(order by cu.csm_code) user_name," +
                    "                       cu.csm_code from DSGC_consumer_users cu, dsgc_user u " +
                    "                 where cu.user_id = u.user_id group by cu.csm_code) t " +
                    "         where t.csm_code(+) = cnt.csm_code) g " +
                    " WHERE 1 = 1 ");
        }
        if ("mysql".equals(dbType)) {
            strSql = new StringBuffer("SELECT * FROM( SELECT cnt.dce_id,\n" +
                    "cnt.csm_code, cnt.csm_name,cnt.csm_desc,cnt.deploy_env,t.user_name OWNER,cnt.creation_date \n" +
                    "FROM DSGC_CONSUMER_ENTITIES cnt left JOIN (SELECT group_concat( u.user_description ORDER BY cu.csm_code, ',' ) user_name,\n" +
                    "cu.csm_code  FROM DSGC_consumer_users cu, dsgc_user u  WHERE\n" +
                    "cu.user_id = u.user_id  GROUP BY cu.csm_code) t on\n" +
                    "t.csm_code = cnt.csm_code) g WHERE 1 = 1 ");

        }

        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(commonReqBean.getCon0())) {
            String[] conArray = commonReqBean.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    strSql.append(this.generateLikeAndCluse(s));
                }
            }
        }
        if ("SystemLeader".equals(userRole) || "Tourist".equals(userRole)) {
            strSql.append(" and upper(owner) like upper(#userName) ");
            mq.setVar("userName", "%" + userName + "%");
        }
        if (!"ALL".equals(commonReqBean.getQueryType()) && commonReqBean.getQueryType() != null) {
            strSql.append(" and upper(deploy_env) like '%" + commonReqBean.getQueryType().toUpperCase() + "%' ");
        }
        strSql.append(" order by g.creation_date desc ");
        mq.sql(strSql.toString());
        return mq.doPageQuery(pageIndex, pageSize, DSGCConsumerEntities.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(csm_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(csm_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(owner) like '%" + conUpper + "%'";
        conAnd += " or UPPER(csm_desc) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void addConsumer(DSGCConsumerEntities consumerEntities) {
        sw.buildQuery().doInsert(consumerEntities);
    }

    public void delConsumer(String dceId) {
        sw.buildQuery().eq("dceId", dceId).doDelete(DSGCConsumerEntities.class);
    }

    public DSGCConsumerEntities queryConsumerEntByCsmCode(String csmCode) {
        return sw.buildQuery().eq("csmCode", csmCode).doQueryFirst(DSGCConsumerEntities.class);
    }

    //    public DSGCConsumerEntities queryConsumerEntById(String dceId){
//        return sw.buildQuery().eq("dceId",dceId).doQueryFirst(DSGCConsumerEntities.class);
//    }
    public List<DSGCUser> queryConsumerUserByCmsCode(String csmCode) {
        StringBuffer strSql = new StringBuffer("select du.* from dsgc_consumer_users dcu,dsgc_user du where 1=1 and dcu.user_id = du.user_id and dcu.csm_code = #csmCode");
        MpaasQuery mq = sw.buildQuery();
        mq.setVar("csmCode", csmCode);
        return mq.sql(strSql.toString()).doQuery(DSGCUser.class);
    }

    public void updateConsumerData(DSGCConsumerEntities dsgcConsumerEntities) {
        sw.buildQuery()
                .update("csm_code", dsgcConsumerEntities.getCsmCode())
                .update("csm_name", dsgcConsumerEntities.getCsmName())
                .update("csm_desc", dsgcConsumerEntities.getCsmDesc())
                .update("last_updated_by", dsgcConsumerEntities.getLastUpdatedBy())
                .eq("dce_id", dsgcConsumerEntities.getDceId())
                .doUpdate(DSGCConsumerEntities.class);
    }

    public void updateConsumerDeployEnv(DSGCConsumerEntities dsgcConsumerEntities) {
        sw.buildQuery().update("deploy_env", dsgcConsumerEntities.getDeployEnv())
                .eq("dce_id", dsgcConsumerEntities.getDceId())
                .doUpdate(DSGCConsumerEntities.class);
    }

    public void addConsumerUser(List<DSGCConsumerUsers> consumerUsers) {
        for (DSGCConsumerUsers item : consumerUsers) {
            sw.buildQuery()
                    .doInsert(item);
        }

    }

    public void delConsumerUser(String csmCode) {
        sw.buildQuery().eq("csm_code", csmCode).doDelete(DSGCConsumerUsers.class);
    }

    public void delConsumerByUserId(String userId) {
        sw.buildQuery().eq("user_id", userId).doDelete(DSGCConsumerUsers.class);
    }

    public List<DSGCConsumerAuth> queryConsumerBasicAuthData(String dceId) {
        return sw.buildQuery()
                .sql("select dca.* from dsgc_consumer_entities dce,dsgc_consumer_auth dca where dce.csm_code = dca.csm_code and dce.dce_id = #dceId")
                .setVar("dceId", dceId)
                .doQuery(DSGCConsumerAuth.class);
    }

    public DSGCConsumerEntities queryConsumerEntById(String dceId) {
        return sw.buildQuery().eq("dceId", dceId).doQueryFirst(DSGCConsumerEntities.class);
    }

    public List<DSGCConsumerAuth> queryConsumerAuthByCsmCode(String csmCode) {
        return sw.buildQuery().eq("csmCode", csmCode).doQuery(DSGCConsumerAuth.class);
    }

    public void updateConsumerBasicAuthPwd(DSGCConsumerAuth dsgcConsumerAuth) {
        DSGCConsumerAuth consumerAuth = sw.buildQuery()
                .eq("csmCode", dsgcConsumerAuth.getCsmCode())
                .eq("env_code", dsgcConsumerAuth.getEnvCode())
                .doQueryFirst(DSGCConsumerAuth.class);
        System.out.println(consumerAuth);
        if (consumerAuth != null) {
            sw.buildQuery()
                    .update("ca_attr1", dsgcConsumerAuth.getCaAttr1())
                    .eq("csm_code", dsgcConsumerAuth.getCsmCode())
                    .eq("env_code", dsgcConsumerAuth.getEnvCode())
                    .doUpdate(DSGCConsumerAuth.class);
        } else {
            sw.buildQuery().doInsert(dsgcConsumerAuth);
        }
    }

    public List<DSGCConsumerEntities> queryConsumersBaseInfoList() {
        return sw.buildQuery().sql("select * from DSGC_CONSUMER_ENTITIES s order by upper(s.csm_name) asc")
                .doQuery(DSGCConsumerEntities.class);
    }

    public List<Map<String, Object>> queryConsumersListByUserId(String id) {
        return sw.buildQuery().sql("select e.csm_code as code,e.csm_name as name from DSGC_CONSUMER_USERS t,dsgc_consumer_entities e where t.csm_code=e.csm_code and t.user_id=#id")
                .setVar("id", id).doQuery();
    }

    public List<DSGCConsumerEntities> queryConsumersListByIds(List<String> codes) {
        return sw.buildQuery().in("csmCode", codes)
                .doQuery(DSGCConsumerEntities.class);
    }

    public List<DSGCEnvInfoCfg> queryApiEnv() {

        Map<String, Object> res = sw.buildQuery().sql("select PROPERTY_VALUE from FND_PROPERTIES where PROPERTY_KEY = 'SHOW_SER_TYPE'").doQueryFirst();
        String dislpayType = "ALL";
        if (res != null) {
            Object pv = res.get("PROPERTY_VALUE");
            if (pv != null) {
                dislpayType = pv.toString();
            }
        }
        if ("API".equals(dislpayType)) {
            return sw.buildQuery().eq("env_type", "DAG").doQuery(DSGCEnvInfoCfg.class);
        } else if ("ESB".equals(dislpayType)) {
            return sw.buildQuery().eq("env_type", "ESB").doQuery(DSGCEnvInfoCfg.class);
        } else {
            return sw.buildQuery().doQuery(DSGCEnvInfoCfg.class);
        }
    }

    public Boolean checkCsmCodeIsExsit(String csmCode) {
        DSGCConsumerEntities dsgcConsumerEntities = sw.buildQuery().eq("csm_code", csmCode).doQueryFirst(DSGCConsumerEntities.class);
        if (dsgcConsumerEntities != null) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkCsmNameIsExsit(String csmName) {
        DSGCConsumerEntities dsgcConsumerEntities = sw.buildQuery().eq("csm_name", csmName).doQueryFirst(DSGCConsumerEntities.class);
        if (dsgcConsumerEntities != null) {
            return true;
        } else {
            return false;
        }
    }

    public DSGCConsumerAuth queryConsumerDataByCsmCodeAndEnv(String csmCode, String envCode) {
        return sw.buildQuery().eq("csm_code", csmCode)
                .eq("env_code", envCode)
                .doQueryFirst(DSGCConsumerAuth.class);

    }

    public void saveConsumerJwtInfo(DagConsumerJwt dagConsumerJwt) {
        sw.buildQuery().doInsert(dagConsumerJwt);
    }

    public void saveConsumerToken(DagConsumerToken dagConsumerToken) {
        sw.buildQuery().doInsert(dagConsumerToken);
    }

    public PageQueryResult<DagConsumerToken> queryTokenList(String envCode, String csmCode, int pageIndex, int pageSize) {
        StringBuffer sql = new StringBuffer("select dct.*,eic.env_name from dag_consumer_token dct,dsgc_env_info_cfg eic where dct.env_code = eic.env_code and dct.csm_code = #csmCode ");
        MpaasQuery mq = sw.buildQuery();
        //  .sql("select dct.*,eic.env_name from dag_consumer_token dct left join dsgc_env_info_cfg eic on dct.env_code = eic.env_code and dct.csm_code = #csmCode");
        mq.setVar("csmCode", csmCode);
        if (!"ALL".equals(envCode)) {
            sql.append(" and dct.env_code = #envCode");
            mq.setVar("envCode", envCode);
        }
        sql.append(" order by dct.creation_date desc");
        mq.sql(sql.toString());
        return mq.doPageQuery(pageIndex, pageSize, DagConsumerToken.class);
    }

    public DSGCConsumerEntities queryConsumerDeployEnv(String dceId) {
        return sw.buildQuery().eq("dce_id", dceId).doQueryFirst(DSGCConsumerEntities.class);
    }

    public List<DagConsumerToken> queryConsumerToken(String envCode, String csmCode) {
        return sw.buildQuery().eq("env_code", envCode).eq("csm_code", csmCode).doQuery(DagConsumerToken.class);
    }


    public void updateConsumerStatus(DSGCConsumerAuth dsgcConsumerAuth) {
        DSGCConsumerAuth consumerAuth = sw.buildQuery()
                .eq("csmCode", dsgcConsumerAuth.getCsmCode())
                .eq("env_code", dsgcConsumerAuth.getEnvCode())
                .doQueryFirst(DSGCConsumerAuth.class);
        System.out.println(consumerAuth);
        if (consumerAuth != null) {
            sw.buildQuery()
                    .update("ca_attr1", dsgcConsumerAuth.getCaAttr1())
                    .eq("csm_code", dsgcConsumerAuth.getCsmCode())
                    .eq("env_code", dsgcConsumerAuth.getEnvCode())
                    .doUpdate(DSGCConsumerAuth.class);
        } else {
            sw.buildQuery().doInsert(dsgcConsumerAuth);
        }
    }
}
