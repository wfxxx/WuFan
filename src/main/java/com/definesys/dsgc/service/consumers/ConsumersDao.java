package com.definesys.dsgc.service.consumers;

import com.definesys.dsgc.service.consumers.bean.*;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsumersDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<DSGCConsumerEntities> queryconsumersList(CommonReqBean commonReqBean, int pageSize, int pageIndex, String userName, String userRole){
        StringBuffer strSql = new StringBuffer(" select * " +
                "  from (select cnt.dce_id," +
                "               cnt.csm_code," +
                "               cnt.csm_name," +
                "               cnt.csm_desc," +
                "               t.user_name owner " +
                "          from DSGC_CONSUMER_ENTITIES cnt," +
                "               (select listagg(u.user_name, ',') within GROUP(order by cu.csm_code) user_name," +
                "                       cu.csm_code" +
                "                  from DSGC_consumer_users cu, dsgc_user u " +
                "                 where cu.user_id = u.user_id " +
                "                 group by cu.csm_code) t " +
                "         where t.csm_code(+) = cnt.csm_code) " +
                " WHERE 1 = 1 ");
        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(commonReqBean.getCon0())) {
            String[] conArray = commonReqBean.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    strSql.append(this.generateLikeAndCluse(s));
                }
            }
        }
        if("SystemLeader".equals(userRole) || "Tourist".equals(userRole)){
            strSql.append(" and upper(owner) like #userName ");
            mq.setVar("userName",userName);
        }
        mq.sql(strSql.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCConsumerEntities.class);
    }
    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(csm_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(csm_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(owner) like '%" + conUpper + "%'";
        conAnd += " or UPPER(csm_desc) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void addConsumer(DSGCConsumerEntities consumerEntities){
        sw.buildQuery().doInsert(consumerEntities);
    }
    public void delConsumer(String dceId){
        sw.buildQuery().eq("dceId",dceId).doDelete(DSGCConsumerEntities.class);
    }
    public DSGCConsumerEntities queryConsumerEntByCsmCode(String csmCode){
        return sw.buildQuery().eq("csmCode",csmCode).doQueryFirst(DSGCConsumerEntities.class);
    }
//    public DSGCConsumerEntities queryConsumerEntById(String dceId){
//        return sw.buildQuery().eq("dceId",dceId).doQueryFirst(DSGCConsumerEntities.class);
//    }
    public List<DSGCUser> queryConsumerUserByCmsCode(String csmCode){
        StringBuffer strSql = new StringBuffer("select du.* from dsgc_consumer_users dcu,dsgc_user du where 1=1 and dcu.user_id = du.user_id and dcu.csm_code = #csmCode");
        MpaasQuery mq = sw.buildQuery();
        mq.setVar("csmCode",csmCode);
        return mq.sql(strSql.toString()).doQuery(DSGCUser.class);
    }

    public void updateConsumerData( DSGCConsumerEntities dsgcConsumerEntities){
        sw.buildQuery()
                .update("csm_code",dsgcConsumerEntities.getCsmCode())
                .update("csm_name",dsgcConsumerEntities.getCsmName())
                .update("csm_desc",dsgcConsumerEntities.getCsmDesc())
                .update("last_updated_by",dsgcConsumerEntities.getLastUpdatedBy())
                .eq("dce_id",dsgcConsumerEntities.getDceId())
                .doUpdate(DSGCConsumerEntities.class);
    }
    public void addConsumerUser(List<DSGCConsumerUsers> consumerUsers){
        for (DSGCConsumerUsers item:consumerUsers) {
            sw.buildQuery()
                    .doInsert(item);
        }

    }
    public void delConsumerUser(String csmCode){
        sw.buildQuery().eq("csm_code",csmCode).doDelete(DSGCConsumerUsers.class);
    }

    public List<DSGCConsumerAuth> queryConsumerBasicAuthData(String dceId){
       return sw.buildQuery()
                .sql("select dca.* from dsgc_consumer_entities dce,dsgc_consumer_auth dca where dce.csm_code = dca.csm_code and dce.dce_id = #dceId")
                .setVar("dceId",dceId)
                .doQuery(DSGCConsumerAuth.class);
    }
    public DSGCConsumerEntities queryConsumerEntById(String dceId){
        return sw.buildQuery().eq("dceId",dceId).doQueryFirst(DSGCConsumerEntities.class);
    }
    public DSGCConsumerAuth queryConsumerAuthByCsmCode(String csmCode){
        return sw.buildQuery().eq("csmCode",csmCode).doQueryFirst(DSGCConsumerAuth.class);
    }
    public void updateConsumerBasicAuthPwd(DSGCConsumerAuth dsgcConsumerAuth){
        DSGCConsumerAuth consumerAuth = sw.buildQuery().eq("csmCode",dsgcConsumerAuth.getCsmCode()).doQueryFirst(DSGCConsumerAuth.class);
        System.out.println(consumerAuth);
        if(consumerAuth != null){
            sw.buildQuery()
                    .update("ca_attr1",dsgcConsumerAuth.getCaAttr1())
                    .eq("csm_code",dsgcConsumerAuth.getCsmCode())
                    .doUpdate(DSGCConsumerAuth.class);
        }else {
            sw.buildQuery().doInsert(dsgcConsumerAuth);
        }
    }
    public List<DSGCConsumerEntities> queryConsumersBaseInfoList(){
       return sw.buildQuery().doQuery(DSGCConsumerEntities.class);
    }
}
