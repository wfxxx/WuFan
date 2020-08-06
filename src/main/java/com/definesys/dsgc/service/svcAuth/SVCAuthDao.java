package com.definesys.dsgc.service.svcAuth;

import com.definesys.dsgc.service.apiauth.bean.DSGCApisAccess;
import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketEntiy;
import com.definesys.dsgc.service.svcAuth.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemAccess;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class SVCAuthDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<SVCHisBean> querySvcHis(SVCCommonReqBean param, int pageIndex, int pageSize){
//       MpaasQuery mq = sw.buildQuery().sql("select du.user_name userName from dsgc_svc_his dsh,dsgc_user du where dsh.created_by = du.user_id and dsh.serv_no = #servNo and dsh.upd_type = #updType");
//        if (param.getQueryType() != null) {
//            mq.setVar("updType",param.getQueryType());
//        } if (param.getCon0() != null) {
//            mq .setVar("servNo",param.getCon0());
//        }
//            return mq.doPageQuery(pageIndex,pageSize,SVCHisBean.class);
              // .eq("dsh.serv_no",param.getCon0()).and().eq("dsh.upd_type",param.getQueryType()).doPageQuery(pageIndex,pageSize,SVCHisBean.class);
    return sw.buildQuery().eq("servNo",param.getCon0()).and().eq("updType",param.getQueryType()).orderBy("lastUpdateDate","desc").doPageQuery(pageIndex,pageSize,SVCHisBean.class);
    }

//    public PageQueryResult<DSGCService> querySvcAuth(SVCCommonReqBean param, int pageIndex, int pageSize,String userRole, List<String> sysCodeList){
//        StringBuffer sqlStr;
//        MpaasQuery mq = sw.buildQuery();
//        if(param.getSelectSystemList() == null ||param.getSelectSystemList().size() == 0){
//            sqlStr = new StringBuffer("select distinct  ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType " +
//                    "from dsgc_services ds,dsgc_system_entities dse where ds.subordinate_system = dse.sys_code ");
//        }else {
//            sqlStr = new StringBuffer("select distinct  ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType " +
//                    "from dsgc_services ds,dsgc_system_access dsa,dsgc_system_entities dse where ds.serv_no = dsa.serv_no and ds.subordinate_system = dse.sys_code and dsa.sys_code in ( ");
//            for (int i = 0;i<param.getSelectSystemList().size();i++){
//                if(i<param.getSelectSystemList().size()-1){
//                    sqlStr.append("'"+param.getSelectSystemList().get(i)+"',");
//                }else {
//                    sqlStr.append("'"+param.getSelectSystemList().get(i)+"') ");
//                }
//            }
//        }
//
//        if(!"ALL".equals(param.getQueryType())){
//            sqlStr.append(" and ds.share_type = #queryType ");
//            mq.setVar("queryType",param.getQueryType());
//        }
//               if ("SystemLeader".equals(userRole)){
//                   sqlStr.append(" and ds.subordinate_system in ( ");
//                   for (int i = 0;i<sysCodeList.size();i++){
//                       if(i<sysCodeList.size()-1){
//                           sqlStr.append("'"+sysCodeList.get(i)+"',");
//                       }else {
//                           sqlStr.append("'"+sysCodeList.get(i)+"') ");
//                       }
//                   }
//       }
//        if (StringUtil.isNotBlank(param.getCon0()) ){
//            sqlStr.append(" and( upper(ds.serv_no) like #con0 or upper(ds.serv_name) like #con0 or upper(dse.sys_name) like #con0 ) ");
//            mq.setVar("con0", "%" + param.getCon0().toUpperCase() + "%");
//        }
//        System.out.println(param.getSelectSystemList().toString());
//        mq.sql(sqlStr.toString());
//        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);
//    }

    public PageQueryResult<DSGCService> querySvcAuth(SVCCommonReqBean param, int pageIndex, int pageSize,String userRole, List<String> sysCodeList){
        StringBuffer sqlStr;
        MpaasQuery mq = sw.buildQuery();
        if(param.getSelectSystemList() == null ||param.getSelectSystemList().size() == 0){
            sqlStr = new StringBuffer("select * from ( select  ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType " +
                    "from dsgc_services ds,dsgc_system_entities dse where ds.subordinate_system = dse.sys_code ) t where 1=1 ");
        }else {
            sqlStr = new StringBuffer("select t.*,dse.sys_name attribue1 from (select distinct ds.serv_no  servNo, " +
                    " ds.serv_name  servName," +
                    " ds.share_type shareType, " +
                    " ds.subordinate_system subordinateSystem,ds.creation_date " +
                    "  from dsgc_services ds, dsgc_system_access dsa " +
                    " where ds.serv_no = dsa.serv_no and ds.share_type != 'public' and (dsa.sys_code in ( ");
            for (int i = 0;i<param.getSelectSystemList().size();i++){
                if(i<param.getSelectSystemList().size()-1){
                    sqlStr.append("'"+param.getSelectSystemList().get(i)+"',");
                }else {
                    sqlStr.append("'"+param.getSelectSystemList().get(i)+"') ");
                }
            }
            sqlStr.append(" ) order by creation_date desc ) t,dsgc_system_entities dse where t.subordinateSystem = dse.sys_code ");
        }

        if(!"ALL".equals(param.getQueryType())){
            sqlStr.append(" and t.shareType = #queryType ");
            mq.setVar("queryType",param.getQueryType());
        }
        if ("SystemLeader".equals(userRole)){
            sqlStr.append(" and t.subordinateSystem in ( ");
            for (int i = 0;i<sysCodeList.size();i++){
                if(i<sysCodeList.size()-1){
                    sqlStr.append("'"+sysCodeList.get(i)+"',");
                }else {
                    sqlStr.append("'"+sysCodeList.get(i)+"') ");
                }
            }
        }
        if (StringUtil.isNotBlank(param.getCon0()) ){
            if(param.getSelectSystemList() == null ||param.getSelectSystemList().size() == 0){
                String[] conArray = param.getCon0().trim().split(" ");
                for (String s : conArray) {
                    if (s != null && s.length() > 0) {
                        sqlStr.append(this.generateLikeAndCluse1(s));
                    }
                }
              //  sqlStr.append(" and( upper(t.servNo) like #con0 or upper(t.servName) like #con0 or upper(t.attribue1) like #con0 ) ");
            }else {
                String[] conArray = param.getCon0().trim().split(" ");
                for (String s : conArray) {
                    if (s != null && s.length() > 0) {
                        sqlStr.append(this.generateLikeAndCluse2(s));
                    }
                }
              //  sqlStr.append(" and( upper(t.servNo) like #con0 or upper(t.servName) like #con0 or upper(dse.sys_name) like #con0 ) ");
            }

            mq.setVar("con0", "%" + param.getCon0().toUpperCase() + "%");
        }
        System.out.println(param.getSelectSystemList().toString());
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);
    }
    private String generateLikeAndCluse1(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(t.servNo) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t.servName) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t.attribue1) like '%" + conUpper + "%' )";
        return conAnd;
    }
    private String generateLikeAndCluse2(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(t.servNo) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t.servName) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public List<FndLookupValue> queryFndModuleByLookupType(String lookupType){
        FndLookupType fndLookupType = this.sw.buildQuery()
                .eq("lookupType", lookupType)
                .doQueryFirst(FndLookupType.class);
        List<FndLookupValue> values = this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doQuery(FndLookupValue.class);
        return values;
    }

    public List<DSGCSystemEntities> querySystem(){
        return sw.buildQuery().doQuery(DSGCSystemEntities.class);
    }
    public List<DSGCSystemEntities> querySystemLeaderSystem(String userId){
        return sw.buildQuery().sql("select dse.* from dsgc_system_user dsu,dsgc_system_entities dse where dsu.sys_code = dse.sys_code and dsu.user_id = #userId")
                .setVar("userId",userId).doQuery(DSGCSystemEntities.class);
    }

    public String queryAuthSystemCount(String servNo){
        Map<String, Object> map = sw.buildQuery().sql("select count(*) count from (select dsa.* " +
                "   from dsgc_services ds, dsgc_system_access dsa\n" +
                "  where ds.serv_no = dsa.serv_no\n" +
                "    and ds.serv_no = #servNo ) temp").setVar("servNo",servNo).doQueryFirst();
        return map.get("COUNT").toString();

    }

    public DSGCService querySvcAuthDetailBaseInfo(String servNo){
      return   sw.buildQuery().sql("select ds.serv_no servNo,ds.serv_name servName,ds.share_type shareType,dse.sys_name attribue1 from dsgc_services ds,dsgc_system_entities dse where ds.subordinate_system = dse.sys_code and ds.serv_no =#servNo")
                .setVar("servNo",servNo).doQueryFirst(DSGCService.class);

    }

    public void updateSvcAuthServShareType(SVCBaseInfoBean svcBaseInfoBean){
        sw.buildQuery().update("share_type",svcBaseInfoBean.getShareType()).eq("servNo",svcBaseInfoBean.getServNo()).doUpdate(DSGCService.class);
    }

    public PageQueryResult<DSGCSystemEntities> queryServAuthSystemList(SVCBaseInfoBean svcBaseInfoBean,int pageIndex,int pageSize){
       return sw.buildQuery().sql(" select temp1.sysCode sysCode,temp1.sysName sysName,temp1.lastUpdateDate lastUpdateDate,du.user_name lastUpdatedBy,temp1.saId attribue1 from  (select dse.sys_code sysCode,dse.sys_name sysName,temp.lastUpdateDate lastUpdateDate,temp.lastUpdatedBy  lastUpdatedBy,temp.saId saId from (select dsa.sys_code   sysCode, " +
               " dsa.last_update_date lastUpdateDate,dsa.last_updated_by lastUpdatedBy,dsa.sa_id saId from dsgc_services ds, dsgc_system_access dsa where ds.serv_no = dsa.serv_no" +
               " and ds.serv_no =  #servNo) temp,dsgc_system_entities dse where temp.sysCode = dse.sys_code) temp1,dsgc_user du where temp1.lastUpdatedBy = du.user_id order by lastUpdateDate desc")
                .setVar("servNo",svcBaseInfoBean.getServNo())
                .doPageQuery(pageIndex,pageSize,DSGCSystemEntities.class);
    }
    public PageQueryResult<DSGCConsumerEntities> queryServAuthConsumerList(SVCBaseInfoBean svcBaseInfoBean,int pageIndex,int pageSize){
        return sw.buildQuery().sql("select temp1.sysCode csmCode,temp1.sysName csmName,temp1.lastUpdateDate lastUpdateDate," +
                " du.user_name lastUpdatedBy,temp1.saId attribue1 from (select dce.csm_code sysCode,dce.csm_name sysName," +
                " temp.lastUpdateDate lastUpdateDate,temp.lastUpdatedBy  lastUpdatedBy,temp.saId saId " +
                " from (select dsa.sys_code sysCode,+dsa.last_update_date lastUpdateDate,dsa.last_updated_by lastUpdatedBy," +
                " dsa.sa_id saId from dsgc_services ds, dsgc_system_access dsa " +
                " where ds.serv_no = dsa.serv_no and ds.serv_no = #servNo) temp," +
                " dsgc_consumer_entities dce where temp.sysCode = dce.csm_code) temp1,dsgc_user du" +
                " where temp1.lastUpdatedBy = du.user_id order by lastUpdateDate desc")
                .setVar("servNo",svcBaseInfoBean.getServNo())
                .doPageQuery(pageIndex,pageSize,DSGCConsumerEntities.class);
    }
    public DSGCSystemAccess queryAccessSystem(String saId){
        return  sw.buildQuery().eq("saId",saId).doQueryFirst(DSGCSystemAccess.class);
    }
    public void deleteAccessSystem(String saId){
        sw.buildQuery().eq("saId",saId).doDelete(DSGCSystemAccess.class);
    }
    public Boolean checkAccessSystemIsExist(SVCAddAuthSystemReqBean param){
      List<DSGCSystemAccess> list =  sw.buildQuery().eq("servNo",param.getServNo()).and().eq("sysCode",param.getSysCode()).doQuery(DSGCSystemAccess.class);
      if(list.size() > 0){
        return true;
      }else {
          return false;
      }
    }
    public Boolean checkSystemEntitieIsExist(String sysCode){
        List<DSGCSystemEntities> list = sw.buildQuery().eq("sysCode",sysCode).doQuery(DSGCSystemEntities.class);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkConsumerEntitieIsExist(String csmCode){
        List<DSGCConsumerEntities> list = sw.buildQuery().eq("csmCode",csmCode).doQuery(DSGCConsumerEntities.class);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }
    public DSGCSystemEntities querySystemByCode(String sysCode){
        return sw.buildQuery().eq("sysCode",sysCode).doQueryFirst(DSGCSystemEntities.class);

    }
    public DSGCConsumerEntities queryConsumerByCode(String sysCode){
        return sw.buildQuery().eq("csmCode",sysCode).doQueryFirst(DSGCConsumerEntities.class);

    }
    public void addServAuthSytem(DSGCSystemAccess dsgcSystemAccess){
        sw.buildQuery().doInsert(dsgcSystemAccess);
    }

    public void addSvcHis(SVCHisBean svcHisBean){
        sw.buildQuery().doInsert(svcHisBean);
    }


    public void addApplyServAuthBus(ApplyAuthProBean applyAuthProBean){
        sw.buildQuery().doInsert(applyAuthProBean);
    }

    public ApplyAuthProBean getProcessBusinessInfo(String instanceId){
       return  sw.buildQuery().eq("instanceId",instanceId).doQueryFirst(ApplyAuthProBean.class);
    }

    public void delProcessBusinessInfo(String instanceId){
          sw.buildQuery().eq("instanceId",instanceId).doDelete();
    }

    public MarketEntiy queryApi(String servCode){
        return sw.buildQuery().sql("select t.api_code servNo,t.api_name servName,t.api_desc servDesc,e.sys_name fromSys, t.creation_date creationDate\n" +
                "from dsgc_apis t \n" +
                "left join dsgc_system_entities e on t.app_code=e.sys_code\n" +
                "where t.api_code=#servCode").setVar("servCode",servCode).doQueryFirst(MarketEntiy.class);
    }
    public MarketEntiy queryServ(String servCode){
        return sw.buildQuery().sql("select t.serv_no servNo,t.serv_name servName,t.serv_desc servDesc,e.sys_name fromSys, t.creation_date creationDate\n" +
                "                from DSGC_SERVICES t \n" +
                "                left join dsgc_system_entities e on t.subordinate_system=e.sys_code\n" +
                "                where t.serv_no=#servCode").setVar("servCode",servCode).doQueryFirst(MarketEntiy.class);
    }

    public List<DSGCSystemAccess> checkSerAuthIsExist(String servNo, List<String> customerList){
        return sw.buildQuery().sql("select * from （SELECT a.serv_no,a.sys_code  ,e.csm_name    FROM dsgc_system_access a\n" +
                "                left join  dsgc_consumer_entities  e on e.csm_code=a.sys_code） ")
                .in("sys_code",customerList)
                .eq("serv_no",servNo)
                .doQuery(DSGCSystemAccess.class);
    }
    public DSGCIpLimitBean checkIpRuleConfigIsExist(IPRuleConfigVO ipRuleConfigVO){
        DSGCIpLimitBean dsgcIpLimitBean =  sw.buildQuery().eq("limit_type",ipRuleConfigVO.getLimitType()).eq("limit_target",ipRuleConfigVO.getLimitTarget()).doQueryFirst(DSGCIpLimitBean.class);
        return dsgcIpLimitBean;
    }
    public void updateIpRuleConfig(DSGCIpLimitBean dsgcIpLimitBean){
        sw.buildQuery().eq("ipl_id",dsgcIpLimitBean.getIplId())
                .update("rule_type",dsgcIpLimitBean.getRuleType())
                .update("rule_cron",dsgcIpLimitBean.getRuleCron()).doUpdate(DSGCIpLimitBean.class);
    }
    public void addIpRuleConfig(DSGCIpLimitBean ipLimitBean){
        sw.buildQuery().doInsert(ipLimitBean);
    }
    public DSGCIpLimitBean queryIpRuleConfig(String limitType, String limitTarget){
       return sw.buildQuery().eq("limit_type",limitType).eq("limit_target",limitTarget).doQueryFirst(DSGCIpLimitBean.class);
    }
}
