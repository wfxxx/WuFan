package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoBean;
import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Transactional
public class SVCGenDao {
    @Autowired
    private MpaasQueryFactory sw;

    public List<SvcgenProjInfoBean> queryAllProjectDir() {
        return sw.buildQuery().doQuery(SvcgenProjInfoBean.class);

    }

    public List<SvcgenProjInfoBean> queryProjectDirByUserId(String uId) {
        return sw.buildQuery().sql("select dspi.* from DSGC_SVCGEN_PROJ_INFO dspi,dsgc_system_user dsu where dspi.sys_code = dsu.sys_code and dsu.user_id = #uId")
                .setVar("uId",uId)
                .doQuery(SvcgenProjInfoBean.class);
    }

    public List<DSGCSystemEntities> queryAllSystem() {
        return sw.buildQuery().doQuery(DSGCSystemEntities.class);
    }


    public List<DeployProfileSltBean> getUserCanDeployProfiles(String uid,String servNo) {
        return sw.buildViewQuery("V_GET_DPL_LIST").setVar("UID",uid).setVar("SERVNO",servNo).doQuery(DeployProfileSltBean.class);
    }


    public IdeDeployProfileBean getIdeDeployProfileByDpId(String dpId) throws Exception {
        Map<String,Object> res = sw.buildQuery().sql("select d.dp_name,d.env_code,(select e.env_name from dsgc_env_info_cfg e where e.env_code = d.env_code) env_name,d.dlp_cust from dsgc_svcgen_deploy_profiles d where d.dp_id = #dpId ")
                .setVar("dpId",dpId).doQueryFirst();
        IdeDeployProfileBean dpl = null;
        if (res != null && !res.isEmpty()) {
            dpl = new IdeDeployProfileBean();
            dpl.setDplName((String)res.get("DP_NAME"));
            dpl.setEnvCode((String)res.get("ENV_CODE"));
            java.sql.Clob dplCustClob = (java.sql.Clob)res.get("DLP_CUST");
            String dplCust = dplCustClob == null ? null : dplCustClob.getSubString(1,(int)dplCustClob.length());
//            if (dplCust != null) {
//                dplCust = Base64.getEncoder().encodeToString(dplCust.getBytes(Charset.forName("UTF-8")));
//            }
            dpl.setDplCust(dplCust);
            dpl.setEnvCodeMeaning((String)res.get("ENV_NAME"));
        }
        return dpl;
    }

    public TmplConfigBean getDeployProfileByDPId(String dpId) throws Exception {
        Map<String,Object> res = sw.buildQuery().sql("SELECT D.DP_NAME,\n" +
                "       D.ENV_CODE,\n" +
                "       D.IS_ENABLE,\n" +
                "       T.DEVE_ID,\n" +
                "       T.SERV_NO,\n" +
                "       T.FLAG || '' TMPL_FLAG,\n" +
                "       T.PROJECT_DIRE,\n" +
                "       T.INTERFACE_DIRE,\n" +
                "       T.SERV_CODE_NAME,\n" +
                "       T.PROVIDER,\n" +
                "       T.BUSI_SERV_URI,\n" +
                "       T.PROXY_SERV_URI,\n" +
                "       T.PROXY_SERV_URI1,\n" +
                "       T.TEXT_ATTRIBUTE1,\n" +
                "       T.TEXT_ATTRIBUTE2,\n" +
                "       T.TEXT_ATTRIBUTE3,\n" +
                "       T.VISIT_USER_NAME,\n" +
                "       T.VISIT_USER_PSWD,\n" +
                "       T.SA_CODE\n" +
                "  FROM DSGC_SVCGEN_TMPL T, DSGC_SVCGEN_DEPLOY_PROFILES D\n" +
                " WHERE T.DEVE_ID = D.DEVE_ID\n" +
                "   AND D.DP_ID = #dpId\n").setVar("dpId",dpId).doQueryFirst();
        TmplConfigBean tcb = null;
        if (res != null && !res.isEmpty()) {
            tcb = new TmplConfigBean();
            this.covertTmplCodeBean(tcb,res);
        }

        return tcb;
    }

    public TmplConfigBean getTmplConfigByServNo(String servNo) throws Exception {
        Map<String,Object> res = sw.buildQuery().sql("SELECT null DP_NAME,\n" +
                "       null ENV_CODE,\n" +
                "       null IS_ENABLE,\n" +
                "       T.DEVE_ID,\n" +
                "       T.SERV_NO,\n" +
                "       T.FLAG || '' TMPL_FLAG,\n" +
                "       T.PROJECT_DIRE,\n" +
                "       T.INTERFACE_DIRE,\n" +
                "       T.SERV_CODE_NAME,\n" +
                "       T.PROVIDER,\n" +
                "       T.BUSI_SERV_URI,\n" +
                "       T.PROXY_SERV_URI,\n" +
                "       T.PROXY_SERV_URI1,\n" +
                "       T.TEXT_ATTRIBUTE1,\n" +
                "       T.TEXT_ATTRIBUTE2,\n" +
                "       T.TEXT_ATTRIBUTE3,\n" +
                "       T.VISIT_USER_NAME,\n" +
                "       T.VISIT_USER_PSWD,\n" +
                "       T.SA_CODE\n" +
                "  FROM DSGC_SVCGEN_TMPL T\n" +
                " WHERE T.IS_PROFILE = 'N'\n" +
                "   AND T.SERV_NO = #servNo").setVar("servNo",servNo).doQueryFirst();
        TmplConfigBean tcb = null;
        if (res != null && !res.isEmpty()) {
            tcb = new TmplConfigBean();
            this.covertTmplCodeBean(tcb,res);
        }
        return tcb;
    }

    private void covertTmplCodeBean(TmplConfigBean tcb,Map<String,Object> res) throws Exception {
        tcb.setDpName((String)res.get("DP_NAME"));
        tcb.setEnvCode((String)res.get("ENV_CODE"));
        tcb.setIsEnable((String)res.get("IS_ENABLE"));
        String deveId = (String)res.get("DEVE_ID");
        tcb.setDeveId(deveId);
        tcb.setServNo((String)res.get("SERV_NO"));
        tcb.setProjDir((String)res.get("PROJECT_DIRE"));
        tcb.setServDir((String)res.get("INTERFACE_DIRE"));
        tcb.setServNameCode((String)res.get("SERV_CODE_NAME"));
        tcb.setToSystem((String)res.get("PROVIDER"));
        tcb.setBsUri((String)res.get("BUSI_SERV_URI"));
        String tmplCode = (String)res.get("TMPL_FLAG");
        if ("0".equals(tmplCode)) {
            //SOAP配置项
            tcb.setSoapPSUri((String)res.get("PROXY_SERV_URI"));
            tcb.setRestPSUri((String)res.get("PROXY_SERV_URI1"));
            tcb.setWsdlUri((String)res.get("TEXT_ATTRIBUTE1"));
            tcb.setWsdlUN((String)res.get("TEXT_ATTRIBUTE2"));
            tcb.setWsdlPD((String)res.get("TEXT_ATTRIBUTE3"));
        } else if ("1".equals(tmplCode)) {
            //REST配置项
            tcb.setRestPSUri((String)res.get("PROXY_SERV_URI"));
        } else if ("2".equals(tmplCode)) {
            //IDE配置项
        } else if ("3".equals(tmplCode)) {
            //RFC配置项
            tcb.setSoapPSUri((String)res.get("PROXY_SERV_URI"));
            tcb.setRestPSUri((String)res.get("PROXY_SERV_URI1"));
            tcb.setSapConn((String)res.get("TEXT_ATTRIBUTE1"));
            tcb.setRfcName((String)res.get("TEXT_ATTRIBUTE2"));
        } else if("51".equals(tmplCode)){
            tcb.setSaUN((String)res.get("VISIT_USER_NAME"));
            tcb.setSaPD((String)res.get("VISIT_USER_PSWD"));
        }
        tcb.setTmplFlag(tmplCode);
        tcb.setSaCode((String)res.get("SA_CODE"));

        List<Map<String,Object>> hres = sw.buildQuery().sql("SELECT H.POP_NAME,H.POP_VALUE FROM DSGC_SVCGEN_OB_HTTPHDS H WHERE H.DEVE_ID = #deveId").setVar("deveId",deveId).doQuery();

        if (hres != null && !hres.isEmpty()) {
            List<OBHeaderBean> ohList = new ArrayList<OBHeaderBean>();
            Iterator<Map<String,Object>> iter = hres.iterator();
            while (iter.hasNext()) {
                Map<String,Object> hd = iter.next();
                OBHeaderBean ohb = new OBHeaderBean();
                ohb.setHeaderName((String)hd.get("POP_NAME"));
                ohb.setHeaderValue((String)hd.get("POP_VALUE"));
                ohList.add(ohb);
            }
            tcb.setObHeaders(ohList);
        }

    }

    public List<ServcieAccountBean> getServcieAccountList(String uid,UserHelper uh) throws Exception {
        String sql = "select o.obj_code sa_code, o.obj_desc sa_name, t.visit_user_name sa_user_code\n" +
                "  from dsgc_svcgen_obj o, dsgc_svcgen_tmpl t\n" +
                " where o.obj_code = t.serv_no\n" +
                "   and t.is_profile = 'N'\n" +
                "   and o.obj_type = 'SA'\n" +
                "   and t.flag = 51";

        if(!(uh.isAdmin() || uh.isSuperAdministrator())){
            //如果不是超级管理员或者管理员，则需要添加限定条件
            if(uh.isSystemMaintainer()){
                sql += " and o.sys_code in (select su.sys_code from dsgc_system_user su where su.user_id = '"+uid+"')";
            }else {
                sql += " and 1 <> 1";
            }
        }
        return sw.buildQuery().sql(sql).doQuery(ServcieAccountBean.class);
    }


    public SABean getSaInfoBySaCode(String saCode) {
        return sw.buildQuery().sql("select t.visit_user_name un,t.visit_user_pswd pd,o.sys_code system_code\n" +
                "  from dsgc_svcgen_obj o, dsgc_svcgen_tmpl t\n" +
                " where o.obj_code = t.serv_no\n" +
                "   and t.is_profile = 'N'\n" +
                "   and o.obj_type = 'SA'\n" +
                "   and t.flag = 51\n" +
                "   and o.obj_code = #saCode").setVar("saCode",saCode).doQueryFirst(SABean.class);

    }


    public PageQueryResult<SvcGenObjJsonBean> getSgObjList(boolean isSvcType,SvcGenObjReqBean q,String uid,UserHelper uh,int pageSize,int pageIndex) {
        String baseSql = SvcGenObjJsonBean.getBaseQuerySql(isSvcType);
        String sql = this.getSvcGenObjQuerySql(baseSql,uid,uh);

        if (q.getObjStat() != null) {
            if ("Y".equals(q.getObjStat())) {
                sql += " and enabled = 'Y' ";
            } else {
                sql += " and enabled = 'N' ";
            }
        }

        if (q.getBindingStat() != null && q.getBindingStat().trim().length() > 0) {
            if ("1".equals(q.getBindingStat())) {
                sql += " and serv_no  is not null ";
            } else if ("0".equals(q.getBindingStat())) {
                sql += " and serv_no  is null ";
            }
        }

        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sql += this.generateLikeAndCluse(s);
                }
            }
        }


        return sw.buildQuery().sql(sql).doPageQuery(pageIndex,pageSize,SvcGenObjJsonBean.class);

    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and (UPPER(obj_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(obj_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(obj_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_no) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(sys_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(sys_code_meaning) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_system) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_system_meaning) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_share_type) like '%" + conUpper + "%'";
        conAnd += " or UPPER(serv_share_type_meaning) like '%" + conUpper + "%'";
        conAnd += " or UPPER(vc_stat) like '%" + conUpper + "%'";
        conAnd += " or UPPER(vc_stat_meaning) like '%" + conUpper + "%'";
        conAnd += " or UPPER(last_updated_by_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(tmpl_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(tmpl_code_meaning) like '%" + conUpper + "%')";
        return conAnd;
    }

    public SvcGenObjJsonBean getSvcGenObjInfo(String sgObjCode,String uid,UserHelper uh) {
        String baseSql = SvcGenObjJsonBean.getBaseQueryAllSql();
        String sql = this.getSvcGenObjQuerySql(baseSql,uid,uh) + " and obj_code = '" + sgObjCode + "'";
        return sw.buildQuery().sql(sql).doQueryFirst(SvcGenObjJsonBean.class);
    }

    private String getSvcGenObjQuerySql(String baseSql,String uid,UserHelper uh) {
        String sql = "";

        if (uh.isAdmin() || uh.isSuperAdministrator() || uh.isSystemMaintainer()) {
            sql = "select * from (" + baseSql + ") t where 1 = 1 ";
            if (uh.isSystemMaintainer()) {
                //查询系统范围内的对象,或者自己创建还未绑定接口的资源对象
                sql = "select * from (" + baseSql + ") " +
                        " where (serv_no is null and (sys_code is not null and sys_code in (select su.sys_code from dsgc_system_user su where su.user_id = '"+uid+"')  or sys_code is null and last_updated_by = '" + uid + "' or created_by = '" + uid + "') " +
                        " or serv_no is not null and serv_system in (select su.sys_code from dsgc_system_user su where su.user_id = '" + uid + "')) ";
            }

        } else {
            sql = "select * from (" + baseSql + ") where 1 <> 1";
        }
        return sql;
    }


    /**
     * 绑定服务资源到一个新建的服务资产中
     *
     * @param scc
     * @param uriList
     * @deprecated
     */
    public int bindingSgObjToNewServ(SVCCreateBean scc,List<SVCUriBean> uriList) {
        //查询服务编号是否重复
        Map<String,Object> res = sw.buildQuery().sql("select count(1) IS_EXIST from dsgc_services where serv_no = #servNo").setVar("servNo",scc.getServNo()).doQueryFirst();
        if (res != null && !res.isEmpty()) {
            int count = Integer.parseInt(res.get("IS_EXIST").toString());
            if (count > 0) {
                return -1;
            }
        }
        //创建服务资产
        SVCInfoBean svc = new SVCInfoBean();
        svc.setServNo(scc.getServNo());
        svc.setServName(scc.getServName());
        svc.setSubordinateSystem(scc.getServSystem());
        svc.setShareType(scc.getServShareType());
        sw.buildQuery().doInsert(svc);
        //绑定到sgObj
        sw.buildQuery().update("SERV_NO",scc.getServNo()).update("SYS_CODE",scc.getServSystem()).eq("objCode",scc.getSgObjCode()).doUpdate(SvcGenObjBean.class);
        //删除旧的服务uri
        sw.buildQuery().eq("servNo",scc.getServNo()).doDelete(SVCUriBean.class);
        //添加服务uri
        if (uriList != null) {
            Iterator<SVCUriBean> uriIter = uriList.iterator();
            while (uriIter.hasNext()) {
                SVCUriBean uri = uriIter.next();
                uri.setServNo(scc.getServNo());
                sw.buildQuery().doInsert(uri);
            }
        }
        return 1;
    }

    /**
     *
     * @param obj
     * @param uid
     * @param uh
     * @return -1无权限操作 1 删除成功 0 不能立即删除，先被disable掉
     */
    public int deleteSvcGenObj(SvcGenObjJsonBean obj,String uid,UserHelper uh) throws Exception{
        int res = 0;
        if (obj.isReadonly()) {
            res = -1;//无权限操作
        } else {

            if ("Y".equals(obj.getEnabled())) {
                boolean canPurge = true;
                //获取部署状态
                Map<String,Object> dplRes = sw.buildQuery().sql("select count(1) DPL_COUNT from dsgc_svcgen_tmpl t ,dsgc_svcgen_deploy_profiles d \n" +
                        "where t.deve_id = d.deve_id \n" +
                        "and t.is_profile = 'Y' and d.dpl_flag = 'Y'\n" +
                        "and t.serv_no = #servNo").setVar("servNo",obj.getObjCode()).doQueryFirst();
                if (dplRes != null) {
                    int dplCount = Integer.parseInt(dplRes.get("DPL_COUNT").toString());
                    if (dplCount > 0) {
                        canPurge = false;
                    }
                }
                //获取版本库上传状态
                Map<String,Object> vcRes = sw.buildQuery().sql("select count(1) VC_COUNT from dsgc_svcgen_files_header h,dsgc_svcgen_vc v\n" +
                        " where h.vid = v.vid\n" +
                        " and v.stat = 'Y'\n" +
                        " and h.serv_no = #servNo").setVar("servNo",obj.getObjCode()).doQueryFirst();
                if (vcRes != null) {
                    int vcCount = Integer.parseInt(vcRes.get("VC_COUNT").toString());
                    if (vcCount > 0) {
                        canPurge = false;
                    }
                }

                if (canPurge) {
                    //如果没有部署过以及没有上传版本库，则直接删除
                    this.purgeSvcGenObj(obj.getObjCode());
                    res = 1;
                } else {
                    //否则设置为废弃状态，提交给管理员去清理
                    this.disableSvcGenObj(obj.getObjCode());
                    res = 0;
                }
            } else {
                //已经废弃，直接删除
                //判断是否为ESB管理员，否则无权限
                if (uh.isAdmin() || uh.isSuperAdministrator()) {
                    this.purgeSvcGenObj(obj.getObjCode());
                    res = 1;//执行成功
                } else {
                    res = -1;//无权限操作
                }
            }
        }
        return res;
    }

    private void disableSvcGenObj(String sgObjCode) {
        sw.buildQuery().update("is_enable","N").eq("objCode",sgObjCode).doUpdate(SvcGenObjBean.class);
    }

    private void purgeSvcGenObj(String sgObjCode) {
        //清理dsgc_svcgen_obj表
        sw.buildQuery().table("dsgc_svcgen_obj").eq("obj_code",sgObjCode).doDelete();

        List<Map<String,Object>> deveIdres = sw.buildQuery().sql("select deve_id from dsgc_svcgen_tmpl where serv_no = #servNo").setVar("servNo",sgObjCode).doQuery();
        if (deveIdres != null) {
            Iterator<Map<String,Object>> iter = deveIdres.iterator();
            while (iter.hasNext()) {
                Map<String,Object> row = iter.next();
                String deveId = (String)row.get("DEVE_ID");
                if (deveId != null) {
                    //清理dsgc_svcgen_ob_httphds表
                    sw.buildQuery().table("dsgc_svcgen_ob_httphds").eq("deve_id",deveId).doDelete();
                    //清理DSGC_SVCGEN_DEPLOY_PROFILES表
                    sw.buildQuery().table("DSGC_SVCGEN_DEPLOY_PROFILES").eq("deve_id",deveId).doDelete();
                }
            }
        }

        //清理dsgc_svcgen_tmpl表
        sw.buildQuery().table("dsgc_svcgen_tmpl").eq("serv_no",sgObjCode).doDelete();

        List<Map<String,Object>> vidRes = sw.buildQuery().sql("select vid from dsgc_svcgen_files_header where serv_no = #servNo").setVar("servNo",sgObjCode).doQuery();
        if (vidRes != null) {
            Iterator<Map<String,Object>> iter = vidRes.iterator();
            while (iter.hasNext()) {
                Map<String,Object> row = iter.next();
                String vid = (String)row.get("VID");
                if (vid != null) {
                    //清理dsgc_svcgen_files
                    sw.buildQuery().table("dsgc_svcgen_files").eq("vid",vid).doDelete();

                    //清理dsgc_svcgen_vc表
                    sw.buildQuery().table("dsgc_svcgen_vc").eq("vid",vid).doDelete();
                }
            }
        }
        //清理dsgc_svcgen_files_header
        sw.buildQuery().table("dsgc_svcgen_files_header").eq("serv_no",sgObjCode).doDelete();
    }

}
