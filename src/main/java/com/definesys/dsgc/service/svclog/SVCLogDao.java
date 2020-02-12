package com.definesys.dsgc.service.svclog;

import com.definesys.dsgc.service.svclog.bean.SVCLogListBean;
import com.definesys.dsgc.service.svclog.bean.SVCLogQueryBean;
import com.definesys.dsgc.service.svclog.bean.UpdateBizResolveVO;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCServInterfaceNode;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.svclog.bean.DSGCValidResutl;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SVCLogDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<DSGCService> querySvcLogRecordListByCon(SVCLogQueryBean q, int pageSize, int pageIndex,String userRole,String userId,List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer("select distinct ds.serv_id servId, ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem,ds.body_store_type bodyStoreType from dsgc_services ds,dsgc_system_entities dse where ds.subordinate_system = dse.sys_code ");
        MpaasQuery mq = sw.buildQuery();
        if("SuperAdministrators".equals(userRole) || "administrators".equals(userRole)){
        if("N".equals(q.getQueryType())){
            sqlStr.append(" and 1 != 1");
        }else {
            sqlStr.append(" and 1 = 1");
        }
        }else if("SystemLeader".equals(userRole)){
            switch (q.getQueryType()){
                case "ALL":
                    sqlStr.append(" and 1=1 ");
                    break;
                case "Y":
                    sqlStr.append(" and 1=1 and dse.sys_code in  (#str) ");
                    String str = "";
                   for (int i =0;i<sysCodeList.size();i++){
                       str += sysCodeList.get(i);
                       if (i<sysCodeList.size()-1){
                           str+=",";
                       }
                   }
                    mq.setVar("sysCode",str);
                    break;
                case "N":
                    sqlStr.append(" and 1=1 and dse.sys_code != (#str) ");
                    String str1 = "";
                    for (int i =0;i<sysCodeList.size();i++){
                        str1 += sysCodeList.get(i);
                        if (i<sysCodeList.size()-1){
                            str1+=",";
                        }
                    }
                    mq.setVar("sysCode",str1);
                  //  mq.setVar("sysCode",sysCode);
                    break;
            }
        }else {
            if("Y".equals(q.getQueryType())){
                sqlStr.append(" and 1 != 1");
            }else {
                sqlStr.append(" and 1 = 1");
            }
        }
        if (StringUtil.isNotBlank(q.getCon0()) ){
            sqlStr.append(" and( upper(ds.serv_no) like #con0 or upper(ds.serv_name) like #con0 or upper(dse.sys_name) like #con0 ) ");
            mq.setVar("con0", "%" + q.getCon0().toUpperCase() + "%");
            }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);
    }
    public List<DSGCSystemUser> findUserSystemByUserId(String userId){
       return sw.buildQuery().eq("user_id",userId).doQuery(DSGCSystemUser.class);
    }

    public DSGCService findServById(String servId){
        return sw.buildQuery().eq("servId",servId).doQueryFirst(DSGCService.class);
    }
    public DSGCService findServByServNo(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQueryFirst(DSGCService.class);
    }
    public void updateServLogSaveType(SVCLogListBean q){
        sw.buildQuery().update("body_store_type",q.getBodyStoreType()).eq("serv_id",q.getServId()).doUpdate(DSGCService.class);
    }

    public List<DSGCService> queryServCommonByCon(SVCLogQueryBean q,String userRole, List<String> sysCodeList){
        StringBuffer sqlStr;
        MpaasQuery mq = sw.buildQuery();
            sqlStr = new StringBuffer("select distinct ds.serv_id servId,ds.serv_no servNo,ds.serv_name servName,dse.sys_name attribue1,dse.sys_code subordinateSystem " +
                    "from dsgc_services ds,dsgc_system_entities dse where ds.subordinate_system = dse.sys_code ");
        if ("SystemLeader".equals(userRole) && sysCodeList.size()>0){
            sqlStr.append(" and ds.subordinate_system in ( ");
            for (int i = 0;i<sysCodeList.size();i++){
                if(i<sysCodeList.size()-1){
                    sqlStr.append("'"+sysCodeList.get(i)+"',");
                }else {
                    sqlStr.append("'"+sysCodeList.get(i)+"') ");
                }
            }
        }
        if (StringUtil.isNotBlank(q.getCon0()) ){
            sqlStr.append(" and( upper(ds.serv_no) like #con0 or upper(ds.serv_name) like #con0 or upper(dse.sys_name) like #con0 ) ");
            mq.setVar("con0", "%" + q.getCon0().toUpperCase() + "%");
        }
        mq.sql(sqlStr.toString());
        return mq.doQuery(DSGCService.class);
    }
    public PageQueryResult<DSGCValidResutl> querySvcLogResultByCon(SVCLogQueryBean q, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        StringBuffer sqlStr;
        MpaasQuery mq = sw.buildQuery();
        sqlStr = new StringBuffer("select distinct dvr.serv_no servNo,dvr.system_code systemCode,dvr.valid_type validType,dvr.valid_column validColumn," +
                "dvr.valid_value validValue,dvr.last_update_date lastUpdateDate,dsu.user_name lastUpdatedBy,ds.serv_name attribue1,dse.sys_name attribue2,dse1.sys_name attribue3 " +
                "from dsgc_valid_result dvr,dsgc_services ds,dsgc_system_entities dse,dsgc_user dsu,dsgc_system_entities dse1 " +
                "where dvr.serv_no = ds.serv_no and ds.subordinate_system = dse.sys_code and dvr.last_updated_By = dsu.user_id and dse1.sys_code = dvr.system_code ");
        if ("SystemLeader".equals(userRole) && sysCodeList.size()>0){
            sqlStr.append(" and ds.subordinate_system in ( ");
            for (int i = 0;i<sysCodeList.size();i++){
                if(i<sysCodeList.size()-1){
                    sqlStr.append("'"+sysCodeList.get(i)+"',");
                }else {
                    sqlStr.append("'"+sysCodeList.get(i)+"') ");
                }
            }
        }
        if (StringUtil.isNotBlank(q.getCon0())) {
            // sqlStr.append(" and ds.is_prod = 'N' ");
            String[] conArray = q.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
//        if (StringUtil.isNotBlank(q.getCon0()) ){
//            sqlStr.append(" and( upper(ds.serv_no) like #con0 or upper(ds.serv_name) like #con0 or upper(dse.sys_name) like #con0 ) ");
//            mq.setVar("con0", "%" + q.getCon0().toUpperCase() + "%");
//        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCValidResutl.class);
    }
    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(dvr.serv_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(ds.serv_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dvr.valid_type) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dvr.valid_column) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dvr.valid_value) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse1.sys_name) like '%" + conUpper + "%' )";

        return conAnd;
    }
    public DSGCService queryServiceByServNo(String servNo){
        return sw.buildQuery().eq("servNo",servNo).doQueryFirst(DSGCService.class);
    }

    public void deleteResultObj(DSGCValidResutl validResut){
        sw.buildQuery().eq("servNo",validResut.getServNo())
                .eq("systemCode",validResut.getSystemCode())
                .eq("validType",validResut.getValidType())
                .eq("validColumn",validResut.getValidColumn())
                .doDelete(validResut);
    }

    public void addResultObj(DSGCValidResutl validResut){
        sw.buildQuery().doInsert(validResut);
    }
    public PageQueryResult<DSGCService> querySvcLogBizkeyByCon(SVCLogQueryBean q, int pageSize, int pageIndex, String userRole, List<String> sysCodeList) {
        StringBuffer sqlStr = new StringBuffer(" select ds.serv_no servNo,ds.serv_name servName,ds.subordinate_system subordinateSystem,ds.biz_resolve bizResolve,dse.sys_name attribue1 " +
                                                " from dsgc_services ds, dsgc_system_entities dse " +
                                                " where exists (select serv_no " +
                                                " from dsgc_serv_interface_node dsin " +
                                                " where dsin.serv_no = ds.serv_no) " +
                                                " and ds.subordinate_system = dse.sys_code ");
        MpaasQuery mq = sw.buildQuery();
        if ("SystemLeader".equals(userRole) && sysCodeList.size()>0){
            sqlStr.append(" and ds.subordinate_system in ( ");
            for (int i = 0;i<sysCodeList.size();i++){
                if(i<sysCodeList.size()-1){
                    sqlStr.append("'"+sysCodeList.get(i)+"',");
                }else {
                    sqlStr.append("'"+sysCodeList.get(i)+"') ");
                }
            }
        }
        if (StringUtil.isNotBlank(q.getCon0())) {
            // sqlStr.append(" and ds.is_prod = 'N' ");
            String[] conArray = q.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.enerateBizkeyLikeAndCluse(s));
                }
            }
        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex,pageSize,DSGCService.class);

    }
    private String enerateBizkeyLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(ds.serv_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(ds.serv_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void addbizkeyObj(List<DSGCServInterfaceNode> list){
        sw.buildQuery().doBatchInsert(list);

    }
    public void delBizkeyByServNo(String servNo){
        sw.buildQuery().eq("servNo",servNo).doDelete(DSGCServInterfaceNode.class);
    }
    public void deleteBizkeyObj(String servNo){
        sw.buildQuery().eq("servNo",servNo)
                .doDelete(DSGCServInterfaceNode.class);
    }
    public List<DSGCServInterfaceNode> queryServBizkey(String servNo){
      return   sw.buildQuery().eq("servNo",servNo)
                .doQuery(DSGCServInterfaceNode.class);
    }
    public void updateBizResolve(UpdateBizResolveVO param){
        sw.buildQuery().update("biz_resolve",param.getBizResolve()).eq("servNo",param.getServNo()).doUpdate(DSGCService.class);
    }
}
