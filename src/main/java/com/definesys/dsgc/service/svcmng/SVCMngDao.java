package com.definesys.dsgc.service.svcmng;

import com.definesys.dsgc.service.svcmng.bean.DSGCUriParamsBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCUriParamsTmplBean;
import com.definesys.dsgc.service.svcmng.bean.SVCCommonReqBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SVCMngDao {
    @Autowired
    private MpaasQueryFactory sw;
    public PageQueryResult<DSGCService> querySvcMngServList(SVCCommonReqBean param, int pageIndex, int pageSize,String userRole, List<String> sysCodeList,String servIsOnline){
        StringBuffer sqlStr;
        MpaasQuery mq = sw.buildQuery();
            sqlStr = new StringBuffer("select distinct  ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.share_type shareType, " +
                    "NVL(ds.info_full,0) infoFull,ds.is_prod isProd,ds.instance_id instanceId,ds.deployed_node deployedNode,dbn.node_name attribue2 " +
                    "from dsgc_services ds,dsgc_system_entities dse,dsgc_bpm_instance dbi,dsgc_bpm_nodes dbn where ds.subordinate_system = dse.sys_code "+
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
//        if (StringUtil.isNotBlank(param.getCon0()) && StringUtil.isBlank(servIsOnline) ){
//            sqlStr.append(" and ds.is_prod = 'N' ");
//            sqlStr.append(" and( upper(ds.serv_no) like #con0 or upper(ds.serv_name) like #con0 or upper(dse.sys_name) like #con0 or upper(dbn.node_name) like #con0 )  ");
//            //or upper(dbn.node_name) like #con0
//            mq.setVar("con0", "%" + param.getCon0().toUpperCase() + "%");
//        }


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
    public void delServUriParamter(String servNo){
        sw.buildQuery().eq("res_code",servNo).doDelete(DSGCUriParamsBean.class);
    }
    public void addServUri(DSGCServicesUri servicesUri){
        sw.buildQuery().sql("insert into dsgc_service_uri (serv_no,ib_uri,uri_type) value (#{servNo},#{ibUri},#{uriType}) ")
                .setVar("servNo",servicesUri.getServNo())
                .setVar("ibUri",servicesUri.getIbUri())
                .setVar("uriType",servicesUri.getUriType());
    }
    public void addServUriParamter(List<DSGCUriParamsBean> list){
        sw.buildQuery().doBatchInsert(list);
    }
}
