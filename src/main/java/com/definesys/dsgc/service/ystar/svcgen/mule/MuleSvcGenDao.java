package com.definesys.dsgc.service.ystar.svcgen.mule;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.dsgc.service.ystar.mg.svg.bean.MuleSvgCodeBean;
import com.definesys.dsgc.service.ystar.svcgen.conn.SvcGenConnDao;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSaCode;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSvgCode;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSvgDeploy;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository("MuleSvcGenDao")
public class MuleSvcGenDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SvcGenConnDao svcGenConnDao;

    /**
     * 保存Mule敏捷服务代码
     *
     * @param loginUser
     * @param cfg
     * @return
     * @throws Exception
     */
    public void saveMuleGenerateCode(String loginUser, TmplConfigBean cfg) {
        System.out.println(cfg.toString());
        //设置httpheaders
        List<OBHeaderBean> ohList = cfg.getObHeaders();
        JSONObject headers = covertListToMap(ohList);
        String header = headers == null ? null : JSONObject.toJSONString(headers);
        transformMuleSvgBean(loginUser, header, cfg);
    }

    public void saveMuleGenerateSACode(MuleSaCode saCode) {
        this.sw.buildQuery().doInsert(saCode);
    }

    public SABean getSaInfoBySaCode(String saCode) {
        return sw.buildQuery().sql("select t.visit_user_name un,t.visit_user_pswd pd,o.sys_code system_code\n" +
                "  from dsgc_svcgen_obj o, dsgc_svcgen_tmpl t\n" +
                " where o.obj_code = t.serv_no\n" +
                "   and t.is_profile = 'N'\n" +
                "   and o.obj_type = 'SA'\n" +
                "   and t.flag = 51\n" +
                "   and o.obj_code = #saCode").setVar("saCode", saCode).doQueryFirst(SABean.class);
    }

    public JSONObject covertListToMap(List<OBHeaderBean> ohs) {
        JSONObject jsonObject = null;
        if (ohs != null && ohs.size() > 0) {
            jsonObject = new JSONObject();
            Iterator<OBHeaderBean> iters = ohs.iterator();
            while (iters.hasNext()) {
                OBHeaderBean ohb = iters.next();
                if (ohb.getHeaderName() != null && ohb.getHeaderName().trim().length() > 0) {
                    jsonObject.put(ohb.getHeaderName(), ohb.getHeaderValue());
                }
            }
        }
        return jsonObject;
    }

    public void transformMuleSvgBean(String loginUser, String obHeader, TmplConfigBean cfg) {
        String svgCode = cfg.getServNo();
        String svgName = cfg.getServNameCode();
        String prjName = cfg.getPrjName();
        String sysCode = cfg.getAppCode();
        String toSystem = cfg.getToSystem();

        MuleSvgCode mg = new MuleSvgCode(svgCode, svgName, "0", sysCode, toSystem, prjName, obHeader, loginUser);
        String flag = cfg.getTmplFlag();
        if ("3".equals(flag)) {//rfc配置方式
            mg.setSvgType("RFC");
            mg.setPsUri(cfg.getSoapPSUri());
        } else if ("2".equals(flag)) {//ide配置方式
            mg.setSvgType("IDE");
        } else if ("1".equals(flag)) {//Rest配置方式
            mg.setSvgType("REST");
            mg.setPsUri(cfg.getRestPSUri());
            mg.setAttr1(cfg.getAttr1());
            mg.setAttr2(cfg.getAttr2());
            mg.setAttr3(cfg.getAttr3());
            mg.setAttr4(cfg.getAttr4());
            mg.setAttr5(cfg.getAttr5());
            mg.setAttr6(cfg.getAttr6());
            mg.setAttr7(cfg.getAttr7());
            mg.setAttr10(cfg.getAttr10());
        } else if ("0".equals(flag)) {//SOAP配置方式
            mg.setSvgType("SOAP");
            mg.setPsUri(cfg.getSoapPSUri());
            mg.setAttr1(cfg.getAttr1());
            mg.setAttr2(cfg.getAttr2());
            mg.setAttr3(cfg.getAttr3());
            mg.setAttr4(cfg.getAttr4());
            mg.setAttr5(cfg.getAttr5());
            mg.setAttr6(cfg.getAttr6());
            mg.setAttr7(cfg.getAttr7());
            mg.setAttr8(cfg.getAttr8());
            mg.setAttr9(cfg.getAttr9());
            mg.setAttr10(cfg.getAttr10());
        } else if ("30".equals(flag)) {//DB配置方式-查询
            mg.setSvgType("DB");
            mg.setPsUri(cfg.getRestPSUri());
            mg.setAttr1(cfg.getAttr1());
            String dbOperate = cfg.getDbOper();
            String sqlCode = cfg.getSqlcode();
            String connName = cfg.getDbConn();
            SvcgenConnBean connBean = svcGenConnDao.sigQuerySvcGenConnect(connName, "DB", cfg.getAttr1());
            mg.setAttr2(connBean.getAttr2());
            mg.setAttr3(connBean.getAttr3());
            mg.setAttr4(connBean.getAttr4());
            mg.setAttr5(connBean.getAttr5());
            mg.setAttr6(connBean.getAttr6());
            mg.setAttr7(connBean.getAttr7());
            mg.setAttr8(dbOperate);
            mg.setAttr9(sqlCode);
        }
        this.sw.buildQuery().doInsert(mg);
    }

    public PageQueryResult<MuleSvgCode> pageQueryMuleSvgCode(int pageIndex, int pageSize, String svgStatus, String svgCode, String sysCode, String sysName) {
        MpaasQuery mq = sw.buildViewQuery("V_MULE_SVG_CODE")
                .eq("svgStatus", svgStatus).or();
        if (StringUtil.isNotBlank(svgCode)) {
            mq.like("svgCode", svgCode);
        }
        if (StringUtil.isNotBlank(sysCode)) {
            mq.like("sysCode", sysCode);
        }
        if (StringUtil.isNotBlank(sysName)) {
            mq.like("sysName", sysName);
        }
        return mq.doPageQuery(pageIndex, pageSize, MuleSvgCode.class);
    }

    public List<MuleSvgCode> listQueryMuleSvgCode(MuleSvgCode svgCodeBean) {
        return sw.buildViewQuery("V_MULE_SVG_CODE").or()
                .eq("svgCode", svgCodeBean.getSvgCode())
                .eq("svgName", svgCodeBean.getSvgName())
                .doQuery(MuleSvgCode.class);
    }

    public MuleSvgCode sigQueryMuleSvgCodeById(String id) {
        return sw.buildQuery()
                .eq("mscId", id)
                .doQueryFirst(MuleSvgCode.class);
    }

    public MuleSvgCode sigQueryMuleSvgCodeByCode(String code) {
        return sw.buildQuery()
                .eq("svgCode", code)
                .doQueryFirst(MuleSvgCode.class);
    }


    /**
     * @param obj
     * @param uid
     * @param uh
     * @return -1无权限操作 1 删除成功 0 不能立即删除，先被disable掉
     */
    public int deleteSvcGenObj(SvcGenObjJsonBean obj, String uid, UserHelper uh) {
        int res = 0;
        if (obj.isReadonly()) {
            res = -1;//无权限操作
        } else {

            if ("Y".equals(obj.getEnabled())) {
                boolean canPurge = true;
                //获取部署状态
                Map<String, Object> dplRes = sw.buildQuery().sql("select count(1) DPL_COUNT from dsgc_svcgen_tmpl t ,dsgc_svcgen_deploy_profiles d \n" +
                        "where t.deve_id = d.deve_id \n" +
                        "and t.is_profile = 'Y' and d.dpl_flag = 'Y'\n" +
                        "and t.serv_no = #servNo").setVar("servNo", obj.getObjCode()).doQueryFirst();
                if (dplRes != null) {
                    int dplCount = Integer.parseInt(dplRes.get("DPL_COUNT").toString());
                    if (dplCount > 0) {
                        canPurge = false;
                    }
                }

                if (canPurge) {
                    //如果没有部署过以及没有上传版本库，则直接删除
                    //this.purgeSvcGenObj(obj.getObjCode());
                    res = 1;
                } else {
                    //否则设置为废弃状态，提交给管理员去清理
                    //this.disableSvcGenObj(obj.getObjCode());
                    res = 0;
                }
            } else {
                //已经废弃，直接删除
                //判断是否为ESB管理员，否则无权限
                if (uh.isAdmin() || uh.isSuperAdministrator()) {
                    //this.purgeSvcGenObj(obj.getObjCode());
                    res = 1;//执行成功
                } else {
                    res = -1;//无权限操作
                }
            }
        }
        return res;
    }


    public PageQueryResult<MuleSaCode> pageQueryMuleSvgSACode(String status, String svgCode, String sysCode, String sysName, String toSystem, int pageIndex, int pageSize) {
        MpaasQuery mq = sw.buildViewQuery("V_MULE_SA_CODE").or();
        if (StringUtil.isNotBlank(svgCode)) {
            mq.like("svgCode", svgCode);
        }
        if (StringUtil.isNotBlank(sysCode)) {
            mq.like("sysCode", sysCode);
        }
        if (StringUtil.isNotBlank(sysName)) {
            mq.like("sysName", sysName);
        }
        return mq.doPageQuery(pageIndex, pageSize, MuleSaCode.class);
    }

    public void delMuleSvgById(String mscId) {
        this.sw.buildQuery().eq("mscId", mscId).doDelete(MuleSvgCode.class);
    }

    public MuleSvgCode sigQueryMuleSvgByCode(String svgCode) {
        return this.sw.buildViewQuery("V_MULE_SVG_CODE").eq("svgCode", svgCode).doQueryFirst(MuleSvgCode.class);
    }


    public List<MuleSvgDeploy> listQueryMuleSvgDeployByCode(String svgCode) {
        return this.sw.buildViewQuery("V_MULE_SVG_DEPLOY").eq("svgCode", svgCode).doQuery(MuleSvgDeploy.class);
    }

    public MuleSvgDeploy sigQueryMuleSvgDeployById(String msdId) {
        return this.sw.buildQuery().eq("msdId", msdId).doQueryFirst(MuleSvgDeploy.class);
    }

    public void saveMuleSvgDeploy(MuleSvgDeploy muleSvgDeploy) {
        this.sw.buildQuery().doInsert(muleSvgDeploy);
    }

    public void deploySvgMuleCode(String svgCode, String envCode) {
        this.sw.buildQuery()
                .eq("SVG_CODE", svgCode)
                .update("ENV_CODE", envCode)
                .update("SVG_STATUS", "1")
                .table("mule_svg_code")
                .doUpdate();
    }

    public void updMuleDeployDate(String svgCode, String uid) {
        this.sw.buildQuery()
                .eq("SVG_CODE", svgCode)
                .update("DEPLOY_DATE", new Date())
                .update("DEPLOY_USER", uid)
                .table("mule_svg_deploy").doUpdate();

    }

    public void delDeployProfileById(String msdId) {
        this.sw.buildQuery()
                .eq("msdId", msdId)
                .doDelete(MuleSvgDeploy.class);
    }
}
