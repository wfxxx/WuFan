package com.definesys.dsgc.service.svcmng;

import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import com.definesys.dsgc.service.svcmng.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SVCMngDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;


    public ServSgUriBean getSgInfoByServNo(String servNo) {
        if (servNo != null) {
            return sw.buildQuery().sql("select PROVIDER,IB_URI from DSGC_SERVICES_URI where SERV_NO = #servNo")
                    .setVar("servNo",servNo).doQueryFirst(ServSgUriBean.class);
        } else {
            return null;
        }
    }


    public PageQueryResult<DSGCService> querySvcMngServList(SVCCommonReqBean param, int pageIndex, int pageSize,String userRole, List<String> sysCodeList,String servIsOnline){
        StringBuffer sqlStr = null;
        MpaasQuery mq = sw.buildQuery();
        if("oracle".equals(dbType)){
            sqlStr = new StringBuffer("select distinct  ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType, ds.ibUri,ds.httpMethod, " +
                    "NVL(ds.info_full,0) infoFull,ds.is_prod isProd,ds.instance_id instanceId,ds.deployed_node deployedNode,dbn.node_name attribue2 " +
                    "from " +
                    "(select ds.*,t.ib_uri ibUri,t.http_method httpMethod from dsgc_services ds left join (select * from (select dsu.*,(row_number()over(partition by serv_no order by serv_no desc)) px " +
                    " from dsgc_services_uri dsu) e where px = 1) t on ds.serv_no = t.serv_no ) ds,"+
                    "dsgc_system_entities dse,dsgc_bpm_instance dbi,dsgc_bpm_nodes dbn " +
                    " where ds.subordinate_system = dse.sys_code  "+
                    " and ds.instance_id = dbi.inst_id(+) and dbi.cur_node = dbn.node_id(+) ");

            if(StringUtil.isNotBlank(servIsOnline)){
                sqlStr.append(" and ds.is_prod = #servIsOnline ");
                mq.setVar("servIsOnline",servIsOnline);
            }
            if(!"ALL".equals(param.getQueryType())){
                sqlStr.append(" and ds.share_type = #queryType ");
                mq.setVar("queryType",param.getQueryType());
            }
            if ("SystemLeader".equals(userRole)){
                sqlStr.append(" and ds.subordinate_system in ( ");
                for (int i = 0;i<sysCodeList.size();i++){
                    if(i<sysCodeList.size()-1){
                        sqlStr.append("'"+sysCodeList.get(i)+"',");
                    }else {
                        sqlStr.append("'"+sysCodeList.get(i)+"') ");
                    }
                }
            }



            if (StringUtil.isNotBlank(param.getCon0())) {
                // sqlStr.append(" and ds.is_prod = 'N' ");
                String[] conArray = param.getCon0().trim().split(" ");
                for (String s : conArray) {
                    if (s != null && s.length() > 0) {
                        sqlStr.append(this.generateLikeAndCluse(s));
                    }
                }
            }

            if("Y".equals(param.getIsComplete())){
                sqlStr.append(" and ( ds.info_full != 100 or ds.info_full is null) ");
            }



        }
      else  if ("mysql".equals(dbType)){
            sqlStr = new StringBuffer("SELECT g.*,dbn.node_name attribue2 FROM " +
                    " ( SELECT DISTINCT ds.serv_no servNo,ds.serv_name servName, " +
                    " dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType, " +
                    " ds.ibUri,ds.httpMethod,ifnull( ds.info_full, 0 ) infoFull,ds.is_prod isProd,ds.instance_id instanceId, " +
                    " ds.deployed_node deployedNode FROM ( SELECT ds.*,t.ib_uri ibUri,t.http_method httpMethod " +
                    " FROM dsgc_services ds LEFT JOIN ( SELECT * FROM ( SELECT dsu.*,@row_number :=CASE WHEN @customer_no = dsu.serv_no THEN " +
                    " @row_number + 1 ELSE 1 END AS px,@customer_no := dsu.serv_no temp FROM dsgc_services_uri dsu,( SELECT @row_number := 0, @customer_no := 0 ) AS t ) e " +
                    " WHERE px = 1 ) t ON ds.serv_no = t.serv_no ) ds,dsgc_system_entities dse WHERE ds.subordinate_system = dse.sys_code ) g LEFT JOIN dsgc_bpm_instance dbi ON g.instanceId = dbi.inst_id LEFT JOIN dsgc_bpm_nodes dbn ON dbi.cur_node = dbn.node_id where 1=1 ");
            if(StringUtil.isNotBlank(servIsOnline)){
                sqlStr.append(" and g.isProd = #servIsOnline ");
                mq.setVar("servIsOnline",servIsOnline);
            }
            if(!"ALL".equals(param.getQueryType())){
                sqlStr.append(" and g.shareType = #queryType ");
                mq.setVar("queryType",param.getQueryType());
            }
            if ("SystemLeader".equals(userRole)){
                sqlStr.append(" and g.subordinateSystem in ( ");
                for (int i = 0;i<sysCodeList.size();i++){
                    if(i<sysCodeList.size()-1){
                        sqlStr.append("'"+sysCodeList.get(i)+"',");
                    }else {
                        sqlStr.append("'"+sysCodeList.get(i)+"') ");
                    }
                }
            }



            if (StringUtil.isNotBlank(param.getCon0())) {
                // sqlStr.append(" and ds.is_prod = 'N' ");
                String[] conArray = param.getCon0().trim().split(" ");
                for (String s : conArray) {
                    if (s != null && s.length() > 0) {
                        sqlStr.append(this.generateLikeAndCluse1(s));
                    }
                }
            }

            if("Y".equals(param.getIsComplete())){
                sqlStr.append(" and ( g.infoFull != 100 or g.infoFull is null) ");
            }

        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(ds.serv_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(ds.serv_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dbn.node_name) like '%" + conUpper + "%' )";

        return conAnd;
    }
    private String generateLikeAndCluse1(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(g.servNo) like '%" + conUpper + "%'";
        conAnd += " or UPPER(g.servName) like '%" + conUpper + "%'";
        conAnd += " or UPPER(g.attribue1) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dbn.node_name) like '%" + conUpper + "%' )";

        return conAnd;
    }

    public DSGCService queryBasicInfoByServNo(String servNo){
      return  sw.buildQuery().eq("servNo",servNo).doQueryFirst(DSGCService.class);
    }
    public void saveServBasicInfo(DSGCService dsgcService){
        sw.buildQuery()
                .eq("serv_no",dsgcService.getServNo())
                .update("serv_name",dsgcService.getServName())
                .update("serv_desc",dsgcService.getServDesc())
                .update("share_type",dsgcService.getShareType())
                .update("subordinate_system",dsgcService.getSubordinateSystem())
                .doUpdate(DSGCService.class);
    }

    public List<DSGCServicesUri> queryServUri(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQuery(DSGCServicesUri.class);
    }
    public DSGCServicesUri queryServUriFirst(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQueryFirst(DSGCServicesUri.class);
    }
    public List<DSGCUriParamsBean> queryServUriParamter(String resCode){
        return sw.buildQuery().eq("resCode",resCode).doQuery(DSGCUriParamsBean.class);
    }

    public List<DSGCUriParamsTmplBean> queryUriParamTemplateList(){
        return sw.buildQuery().sql("select distinct dupt.tmpl_name from DSGC_URI_PARAMS_TMPL dupt ").doQuery(DSGCUriParamsTmplBean.class);
    }

    public List<DSGCUriParamsTmplBean> queryUriParamTemplateByTmplName(String tmplName){
        return sw.buildQuery().eq("tmplName",tmplName).doQuery(DSGCUriParamsTmplBean.class);
    }
    public void saveAsTemplate(List<DSGCUriParamsTmplBean> list){
        sw.buildQuery().doBatchInsert(list);
    }
    public void delServUri(String servNo){
       // sw.buildQuery().sql("delete from dsgc_service_uri dsu where dsu.servNo = #{servNo}").setVar("servNo",servNo).table("dsgc_service_uri").doDelete();
        sw.buildQuery().eq("serv_no",servNo).table("dsgc_services_uri").doDelete();

    }

    public void updateServUri(DSGCServicesUri servicesUri){
        sw.buildQuery().eq("serv_no", servicesUri.getServNo()).update("ib_uri",servicesUri.getIbUri())
                .doUpdate(DSGCServicesUri.class);
    }
    public void delServUriParamter(String servNo){
        sw.buildQuery().eq("res_code",servNo).doDelete(DSGCUriParamsBean.class);
    }
    public void addServ( DSGCService dsgcService){
        sw.buildQuery().doInsert(dsgcService);
    }
    public void addServUri(DSGCServicesUri servicesUri){
        sw.buildQuery()
                .doInsert(servicesUri);
//                .sql("insert into dsgc_service_uri (serv_no,ib_uri,uri_type) value (#{servNo},#{ibUri},#{uriType}) ")
//                .setVar("servNo",servicesUri.getServNo())
//                .setVar("ibUri",servicesUri.getIbUri())
//                .setVar("uriType",servicesUri.getUriType())
//                .doInsert();

    }
    public void addServUriParamter(DSGCUriParamsBean list){
        sw.buildQuery().doInsert(list);
    }
    public DSGCService queryServByServNo(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQueryFirst(DSGCService.class);
    }
    public void updateServDataCompletion(DSGCService dsgcService){
        sw.buildQuery().eq("serv_no",dsgcService.getServNo()).update("info_full",dsgcService.getInfoFull()).doUpdate(DSGCService.class);
    }

    public List<DSGCPayloadParamsBean> queryServPayloadParam(SVCCommonReqBean param){
        return sw.buildQuery()
                .eq("res_code",param.getCon0())
                .eq("req_or_res",param.getQueryType())
                .doQuery(DSGCPayloadParamsBean.class);
    }
    public List<DSGCPayloadSampleBean> querySrvPaloadSample(SVCCommonReqBean param){
        return sw.buildQuery()
                .eq("res_code",param.getCon0())
                .eq("req_or_res",param.getQueryType())
                .doQuery(DSGCPayloadSampleBean.class);
    }
    public DSGCPayloadSampleBean querySrvRestPaloadSample(String resCode){
        return sw.buildQuery()
                .eq("res_code",resCode)
                .eq("req_or_res","REQ")
                .eq("uri_type","REST")
                .doQueryFirst(DSGCPayloadSampleBean.class);
    }
    public DSGCPayloadSampleBean querySrvPaloadSoapOrRestSample(DSGCPayloadSampleBean param){
        return sw.buildQuery()
                .eq("res_code",param.getResCode())
                .eq("req_or_res",param.getReqOrRes())
                .eq("uri_type",param.getUriType())
                .doQueryFirst(DSGCPayloadSampleBean.class);
    }
    public void addServPayload(DSGCPayloadSampleBean bean){
        sw.buildQuery().doInsert(bean);
    }
    public void updateServPayloadById(DSGCPayloadSampleBean bean){
//        sw.buildQuery().sql("update DSGC_PAYLOAD_SAMPLE set pl_sample = #{plSample} where dpsam_id = #{dpsamId}")
//                .setVar("plSample",bean.getPlSample()).setVar("dpsamId",bean.getDpsamId())
//                .table("DSGC_PAYLOAD_SAMPLE")
//                .doUpdate();
        sw.buildQuery()
               // .update(new String[]{"pl_sample"})
                .update("pl_sample",bean.getPlSample())
                .eq("dpsam_id",bean.getDpsamId())
                .doUpdate(bean);
    }
    public void delServPayloadParam(DSGCPayloadParamsBean dsgcPayloadParamsBean){
        sw.buildQuery()
                .eq("res_code",dsgcPayloadParamsBean.getResCode())
                .eq("req_or_res",dsgcPayloadParamsBean.getReqOrRes())
                .doDelete(DSGCPayloadParamsBean.class);
    }

    public void addServPayloadParam(DSGCPayloadParamsBean payloadParamsBeans){
        sw.buildQuery()
                .doInsert(payloadParamsBeans);
    }
//    public void addServSoapPayload(DSGCPayloadSampleBean bean){
//        sw.buildQuery().doInsert(bean);
//    }

    public List<Map<String,Object>> getUrlByServNo(String servNo){
        return sw.buildQuery().sql("select t.ib_uri from DSGC_SERVICES_URI t where t.serv_no=#servNo and t.uri_type='SOAP'")
                .setVar("servNo",servNo).doQuery();
    }

    public List<Map<String,Object>> getEvnInfo(String envSeq){
        return sw.buildQuery().sql("select t.esb_ip,t.esb_port from DSGC_ENV_INFO_CFG t where t.env_seq=#envSeq")
                .setVar("envSeq",envSeq).doQuery();
    }

    /**
     * 根据serNo查找对应的系统负责人
     * @param servNo
     * @return
     */
    public List<Map<String,Object>> getSystemUserByServNo(String servNo){
        return sw.buildQuery().sql("select u.user_id code,u.user_name name from dsgc_user u,dsgc_system_user su,dsgc_services s \n" +
                "where s.serv_no=#servNo  and s.subordinate_system=su.sys_code and u.user_id= su.user_id and u.user_role='SystemLeader'")
                .setVar("servNo",servNo)
                .doQuery();
    }
    public List<Map<String,Object>> getSystemUserByApiNo(String servNo){
        return sw.buildQuery().sql("select u.user_id code,u.user_name name from dsgc_user u,dsgc_system_user su,dsgc_apis s \n" +
                "                where s.api_code='api_test5'  and s.app_code=su.sys_code \n" +
                "                and u.user_id= su.user_id and u.user_role='SystemLeader'")
                .setVar("servNo",servNo)
                .doQuery();
    }

public Boolean checkServNoIsExsit(String servNo){
    DSGCService dsgcService =  sw.buildQuery().eq("serv_no",servNo).doQueryFirst(DSGCService.class);
    if(dsgcService != null){
        return true;
    }else {
        return false;
    }
}

public PageQueryResult<DSGCSvcgenUriBean> querySvcSourceList(UserHelper uh,SVCCommonReqBean param,int pageIndex,int pageSize){
   MpaasQuery mq = sw.buildQuery();

   String baseSql = "select u.OBJ_CODE,\n" +
           "       o.obj_name,\n" +
           "       o.obj_desc,\n" +
           "       u.IB_URI,\n" +
           "       u.URI_TYPE,\n" +
           "       u.SOAP_OPER,\n" +
           "       u.TRANSPORT_TYPE,\n" +
           "       u.HTTP_METHOD,\n" +
           "       o.SYS_CODE app_code,\n" +
           "       (select e.SYS_NAME from DSGC_SYSTEM_ENTITIES e where e.SYS_CODE = o.sys_code) app_name,\n" +
           "       o.last_update_date,\n" +
           "       (select s.user_name\n" +
           "        from dsgc_user s\n" +
           "        where s.user_id = u.last_updated_by) update_user\n" +
           "from dsgc_svcgen_uri u, dsgc_svcgen_obj o\n" +
           "where u.obj_code = o.obj_code\n" +
           "  and (soap_oper is not null and\n" +
           "       (ib_uri, soap_oper) not in\n" +
           "       (select t.ib_uri, t.soap_oper\n" +
           "        from dsgc_services_uri t\n" +
           "        where t.soap_oper is not null) or\n" +
           "       soap_oper is null and\n" +
           "       (ib_uri) not in\n" +
           "       (select b.ib_uri from dsgc_services_uri b where b.soap_oper is null))";

   if(!(uh.isAdmin() || uh.isSuperAdministrator())){
       if(uh.isSystemMaintainer()){
          baseSql = baseSql + " and o.SYS_CODE in (select  s.SYS_CODE from DSGC_SYSTEM_USER  s where s.USER_ID = '"+uh.getUID()+"')";
       } else {
           baseSql = baseSql + " and 1 <> 1";
       }
   }

   baseSql = baseSql + " order by o.LAST_UPDATE_DATE desc";


    StringBuffer stringBuffer = new StringBuffer("select v.* from ("+baseSql+") v where 1 =1 ");
   if(StringUtil.isNotBlank(param.getQueryType())){
        stringBuffer.append(" and v.uri_type = #uriType");
       mq.setVar("uriType",param.getQueryType());

   }
    if (StringUtil.isNotBlank(param.getCon0())) {
        String[] conArray = param.getCon0().trim().split(" ");
        for (String s : conArray) {
            if (s != null && s.length() > 0) {
                stringBuffer.append(this.generateSourceLikeAndCluse(s));
            }
        }
    }
  return sw.buildQuery().sql(stringBuffer.toString()).doPageQuery(pageIndex,pageSize,DSGCSvcgenUriBean.class);
}
    private String generateSourceLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(v.obj_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(v.ib_uri) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void updateApiDataCompletion(DSGCApisBean apisBean){

    }
}
