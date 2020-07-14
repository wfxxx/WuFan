package com.definesys.dsgc.service.svcmng;

import com.definesys.dsgc.service.apimng.bean.DSGCApisBean;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcmng.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemAccess;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
            return sw.buildQuery().sql("select (select s.OBJ_CODE from DSGC_SVCGEN_URI s where s.IB_URI = tt.IB_URI and rownum = 1) PROVIDER, tt.IB_URI \n" +
                    "from DSGC_SERVICES_URI tt \n" +
                    "where tt.SERV_NO = #servNo")
                    .setVar("servNo",servNo).doQueryFirst(ServSgUriBean.class);
        } else {
            return null;
        }
    }


    public PageQueryResult<DSGCService> querySvcMngServList(SVCCommonReqBean param,int pageIndex,int pageSize,String userRole,List<String> sysCodeList,String servIsOnline) {
        StringBuffer sqlStr = null;
        MpaasQuery mq = sw.buildQuery();

        sqlStr = new StringBuffer("SELECT DS.SERV_NO,\n" +
                "       DS.SERV_NAME,\n" +
                "       DS.SERV_STATUS, " +
                "       DSE.SYS_NAME attribue1,\n" +
                "       DS.SUBORDINATE_SYSTEM,\n" +
                "       DS.SHARE_TYPE,\n" +
                "       LV.MEANING SHARE_TYPE_MEANING,\n" +
                "       U.IB_URI,\n" +
                "       U.HTTP_METHOD,\n" +
                "       CASE\n" +
                "         WHEN DS.INFO_FULL IS NULL THEN\n" +
                "          0\n" +
                "         ELSE\n" +
                "          DS.INFO_FULL\n" +
                "       END INFO_FULL\n" +
                "  FROM DSGC_SERVICES DS, DSGC_SERVICES_URI U, DSGC_SYSTEM_ENTITIES DSE,\n" +
                "       (SELECT FV.LOOKUP_CODE,FV.MEANING\n" +
                "          FROM FND_LOOKUP_TYPES FT, FND_LOOKUP_VALUES FV\n" +
                "         WHERE FT.LOOKUP_ID = FV.LOOKUP_ID\n" +
                "           AND FT.LOOKUP_TYPE = 'SVC_SHARE_TYPE') LV\n" +
                " WHERE DS.SUBORDINATE_SYSTEM = DSE.SYS_CODE\n" +
                "   AND DS.SERV_NO = U.SERV_NO\n"+
                "   AND DS.SHARE_TYPE = LV.LOOKUP_CODE");


        if (!"ALL".equals(param.getQueryType())) {
            sqlStr.append(" and DS.share_type = #queryType ");
            mq.setVar("queryType",param.getQueryType());
        }
        if("0".equals(param.getIsEnable())){
            sqlStr.append(" and DS.serv_status = #isEnable");
            mq.setVar("isEnable",param.getIsEnable());
        }

        if(!"ALL".equals(param.getEnvCode()) && param.getEnvCode() != null){
            String envIf = param.getEnvCode();
            if(param.getEnvCode().startsWith("NON:"))
            {
                  envIf = param.getEnvCode().substring(4);
                sqlStr.append(" AND NOT EXISTS");
            } else {
                sqlStr.append(" AND EXISTS");
            }
            sqlStr.append(" (SELECT LE.ENV_CODE FROM DSGC_URI_DPL_ENV LE WHERE LE.ENV_CODE = '"+envIf+"' AND LE.IB_URI = U.IB_URI)");
        }

        if ("SystemLeader".equals(userRole)) {
            sqlStr.append(" and DS.subordinate_system in ( ");
            for (int i = 0; i < sysCodeList.size(); i++) {
                if (i < sysCodeList.size() - 1) {
                    sqlStr.append("'" + sysCodeList.get(i) + "',");
                } else {
                    sqlStr.append("'" + sysCodeList.get(i) + "') ");
                }
            }
        }


        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }

        if ("Y".equals(param.getIsComplete())) {
            sqlStr.append(" and ( DS.info_full != 100 or DS.info_full is null) ");
        }

        sqlStr.append(" ORDER BY DS.CREATION_DATE DESC");

        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(DS.serv_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(DS.serv_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(DSE.sys_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(DSE.SYS_CODE) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(U.IB_URI) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(U.HTTP_METHOD) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(LV.MEANING) like '%" + conUpper + "%' )";

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

    public List<DeployedEnvInfoBean> getDeployEnvInfo(String ibUri){
        if(ibUri != null) {
            return sw.buildQuery().sql("SELECT C.ENV_CODE,C.ENV_NAME FROM DSGC_URI_DPL_ENV E ,DSGC_ENV_INFO_CFG C WHERE E.ENV_CODE = C.ENV_CODE and e.ib_uri = #ibUri ORDER BY C.ENV_SEQ DESC")
                    .setVar("ibUri",ibUri).doQuery(DeployedEnvInfoBean.class);
        } else {
            return new ArrayList<DeployedEnvInfoBean>();
        }
    }

    /*** ystar 20200608****/
    public List<Map<String, Object>> querySvcGenList(String uid){
        String sql = " SELECT u.IB_URI,u.SYS_CODE FROM (" +
                " SELECT I.IB_URI," +
                "       (SELECT S.SUBORDINATE_SYSTEM " +
                "          FROM DSGC_SERVICES S " +
                "         WHERE S.SERV_NO = I.SERV_NO) SYS_CODE " +
                "  FROM DSGC_SERVICES_URI I) u where 1 = 1 " ;
        if(StringUtil.isNotBlank(uid)){
            sql +=" and u.SYS_CODE in (SELECT su.sys_code FROM dsgc_system_user su WHERE su.user_id = #uid ) ";
        }

        sql += " GROUP BY u.IB_URI,u.SYS_CODE ";

        MpaasQuery mq = this.sw.buildQuery().sql(sql);
        if(StringUtil.isNotBlank(uid)){
            mq = mq.setVar("uid",uid);
        }
        return mq.doQuery();
    }

    public List<DSGCEnvInfoCfg> queryEnvInfoCfgByEnvType(String envType){
        return sw.buildQuery().eq("envType",envType).doQuery(DSGCEnvInfoCfg.class);
    }

    public void removeUriDpl(String envCode,String uri){
        //System.out.println("==删除查找==>envCode->"+envCode+" uri->"+uri);
        DSGCUriDplEnvBean uriDplEnv = this.sw.buildQuery()
                .eq("envCode",envCode)
                .eq("ibUri",uri)
                .doQueryFirst(DSGCUriDplEnvBean.class);

        if(uriDplEnv != null){
            this.sw.buildQuery()
                    .eq("envCode",envCode)
                    .eq("ibUri",uri)
                    .doDelete(DSGCUriDplEnvBean.class);
            //System.out.println("==成功删除==>envCode->"+envCode+" uri->"+uri);
        }
    }

    public void addUriDplEnv(String envCode,String uri) {
        DSGCUriDplEnvBean uriDpl = this.sw.buildQuery()
                .eq("envCode",envCode)
                .eq("ibUri",uri)
                .doQueryFirst(DSGCUriDplEnvBean.class);
        if(uriDpl == null){
            uriDpl = new DSGCUriDplEnvBean(envCode,uri);
            this.sw.buildQuery()
                    .doInsert(uriDpl);
            //System.out.println("==新增====>envCode->"+envCode+" uri->"+uri);
        }
    }
    public void modifyServStatus(Map<String,String> map){
        sw.buildQuery().update("serv_status",map.get("label")).eq("serv_no",map.get("servNo")).doUpdate(DSGCService.class);
    }
    public void delServ(String servNo){
        DSGCServicesUri dsgcServicesUri = sw.buildQuery().eq("serv_no",servNo).doQueryFirst(DSGCServicesUri.class);
        DSGCSystemAccess dsgcSystemAccess = sw.buildQuery().eq("serv_no",servNo).doQueryFirst(DSGCSystemAccess.class);
        if(dsgcServicesUri != null){
            sw.buildQuery().eq("serv_no",servNo).doDelete(DSGCServicesUri.class);
        }
        if(dsgcSystemAccess != null){
            sw.buildQuery().eq("serv_no",servNo).doDelete(DSGCSystemAccess.class);
        }
           sw.buildQuery().eq("serv_no",servNo).doDelete(DSGCService.class);


    }
}
