package com.definesys.dsgc.service.mynty;

import com.definesys.dsgc.service.mynty.bean.*;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@Transactional
public class MyNtyDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private FndLookupTypeDao lkvDao;


    public PageQueryResult<MyNtyQueryListBean> queryMNRules(String uid,UserHelper uh,MyNtyQueryParamVO reqParam,int pageSize,int pageIndex) {

        String sql = "select r.rule_id,\n" +
                "       r.rule_title,\n" +
                "       r.rule_type,\n" +
                "       (select fv.meaning from fnd_lookup_types ft,fnd_lookup_values fv where ft.lookup_id = fv.lookup_id and ft.lookup_type = 'MN_RULE_ALERT_TYPE' and fv.lookup_code = r.rule_type) rule_type_meaning,\n" +
                "       r.rule_expr_desc,\n" +
                "       r.is_enable,\n" +
                "       r.created_by,\n" +
                "       (select u.user_name from dsgc_user u where u.user_id = r.created_by) creator,\n" +
                "       r.app_code,\n" +
                "       (select e.sys_name\n" +
                "          from dsgc_system_entities e\n" +
                "         where e.sys_code = r.app_code) app_code_meaning,\n" +
                "       r.alert_count,\n" +
                "       (select s.is_enable from dsgc_mn_subcribes s where s.scb_user = '" + uid + "' and s.mn_rule = r.rule_id) sub_stat\n" +
                "  from dsgc_mn_rules r";

        String queryAnd = "";
        if (reqParam.getRuleType() != null && !"ALL".equals(reqParam.getRuleType())) {
            queryAnd += " and rule_type = '" + reqParam.getRuleType() + "'";
        }

        if (reqParam.getIsEnable() != null && !"ALL".equals(reqParam.getIsEnable())) {
            queryAnd += " and is_enable = '" + reqParam.getIsEnable() + "'";
        }

        if (reqParam.getSubStat() != null && !"ALL".equals(reqParam.getIsEnable())) {
            queryAnd += " and sub_stat = '" + reqParam.getSubStat() + "'";
        }

        if (reqParam.getCon0() != null) {
            String[] conArray = reqParam.getCon0().split(" ");
            for (String c : conArray) {
                if (c.trim().length() > 0) {
                    queryAnd += this.generateLikeAndCluse(c.trim());
                }
            }
        }

        if (!(uh.isSuperAdministrator() || uh.isAdmin())) {
            //如果不是管理员，则不能查询所有的规则
            if (uh.isSystemMaintainer()) {
                queryAnd += " and ( created_by = '" + uid + "' \n" +
                        "          or sub_stat is not null\n" +
                        "          or app_code in (select sys_code from dsgc_system_user s where s.user_id = '" + uid + "')\n" +
                        "        )";

            } else {
                queryAnd += " and ( created_by = '" + uid + "' \n" +
                        "          or sub_stat is not null\n" +
                        "        )";
            }
        }

        if (queryAnd != null && queryAnd.trim().length() > 0) {
            sql = "select * from (" + sql + ") where 1 = 1 " + queryAnd;
        }

        System.out.println(sql);
        return sw.buildQuery().sql(sql).doPageQuery(pageIndex,pageSize,MyNtyQueryListBean.class);

    }


    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and (UPPER(rule_title) like '%" + conUpper + "%'";
        conAnd += " or UPPER(rule_type_meaning) like '%" + conUpper + "%'";
        conAnd += " or UPPER(rule_expr_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(creator) like '%" + conUpper + "%'";
        conAnd += " or UPPER(app_code_meaning) like '%" + conUpper + "%'";
        return conAnd;
    }


    public void cancelSubscribe(String uid,RuleStatSetVO reqParam) {
        if ("Y".equals(reqParam.getIsEnable()) || "N".equals(reqParam.getIsEnable())) {
            sw.buildQuery().update("IS_ENABLE",reqParam.getIsEnable()).eq("scbUser",uid).eq("mnRule",reqParam.getRuleId()).doUpdate(MyNtySubcribesBean.class);
        }
    }

    public void setRuleStat(RuleStatSetVO reqParam){
        if ("Y".equals(reqParam.getIsEnable()) || "N".equals(reqParam.getIsEnable())) {
            sw.buildQuery().update("IS_ENABLE",reqParam.getIsEnable()).eq("ruleId",reqParam.getRuleId()).doUpdate(MyNtyRulesBean.class);
        }
    }

    public MyNtyRulesBean getMyNtyRuleDtl(String ruleId){
       return  sw.buildQuery().eq("ruleId",ruleId).doQueryFirst(MyNtyRulesBean.class);
    }

    /**
     * 根据用户id和订阅规则类型，获取用户订阅规则
     *
     * @param userId
     * @param ruleType
     * @return
     * @deprecated
     */
    public List<MyNtyRulesBean> getMNRules(String userId,String ruleType) {
        return sw.buildViewQuery("V_GET_MN_RULES").setVar("userId",userId).setVar("ruleType",ruleType).doQuery(MyNtyRulesBean.class);
    }


    /**
     * 更新我的通知订阅规则
     *
     * @param userId
     * @param chs
     */
    public void updateMNRules(String userId,List<MyNtyRulesBean> chs) {
        if (chs != null && chs.size() > 0) {
            Iterator<MyNtyRulesBean> mnrbIter = chs.iterator();
            while (mnrbIter.hasNext()) {
                MyNtyRulesBean mnrb = mnrbIter.next();
                if (mnrb.getOperType() == -1) {
                    //删除用户订阅
                    sw.buildQuery().eq("mnRule",mnrb.getRuleId()).doDelete(MyNtySubcribesBean.class);
                    //删除订阅规则
                    sw.buildQuery().rowid("ruleId",mnrb.getRuleId()).doDelete(MyNtyRulesBean.class);
                    //删除订阅规则选择的服务
                    sw.buildQuery().eq("ruleId",mnrb.getRuleId()).doDelete(MyNtySubServBean.class);
                } else if (mnrb.getOperType() != 0 && (mnrb.getRuleId() == null || mnrb.getRuleId().trim().length() == 0)) {
                    //执行订阅规则新增
                    sw.buildQuery().doInsert(mnrb);
                    MyNtySubcribesBean mnsb = new MyNtySubcribesBean();
                    if (mnrb.getIsEnableBL()) {
                        mnsb.setIsEnable("Y");
                    } else {
                        mnsb.setIsEnable("N");
                    }
                    mnsb.setMnRule(mnrb.getRuleId());
                    mnsb.setScbUser(userId);
                    sw.buildQuery().doInsert(mnsb);
                } else if (mnrb.getOperType() != 0 && mnrb.getRuleId() != null && mnrb.getRuleId().trim().length() > 0) {
                    //执行订阅规则更新
                    sw.buildQuery().rowid("ruleId",mnrb.getRuleId()).doUpdate(mnrb);
                    if (mnrb.getIsEnableBL()) {
                        sw.buildQuery().update("IS_ENABLE","Y").eq("mnRule",mnrb.getRuleId()).eq("scbUser",userId).doUpdate(MyNtySubcribesBean.class);
                    } else {
                        sw.buildQuery().update("IS_ENABLE","N").eq("mnRule",mnrb.getRuleId()).eq("scbUser",userId).doUpdate(MyNtySubcribesBean.class);
                    }
                }
            }
        }
    }


    /**
     * 保存用户指定的订阅服务
     *
     * @param ruleType
     * @param ruleId
     * @param unSlt
     * @param newSlted
     */
    public void saveMNSubcributeServ(String ruleType,String ruleId,String[] unSlt,String[] newSlted) {
        if ("SE".equals(ruleType)) {
            this.saveMNSESubServ(ruleId,unSlt,newSlted);
        } else {
            this.saveMNSLASubServ(ruleId,unSlt,newSlted);
        }
    }

    /**
     * 保存服务异常通知规则订阅服务
     *
     * @param refExprId
     * @param unSlt
     * @param newSlted
     */
    public void saveMNSESubServ(String refExprId,String[] unSlt,String[] newSlted) {
        if (refExprId != null || refExprId.trim().length() > 0) {

            //删除取消订阅的服务
            if (unSlt != null && unSlt.length > 0) {
                sw.buildQuery().in("serv_no",unSlt).eq("expr_ref_id",refExprId).table("DSGC_MN_SERVICES").doDelete();
            }

            //添加新增订阅的服务
            if (newSlted != null && newSlted.length > 0) {
                List<ServExcptSubRulesBean> ruleExprList = sw.buildQuery().eq("seeId",refExprId).doQuery(ServExcptSubRulesBean.class);
                if (ruleExprList != null && ruleExprList.size() > 0) {
                    String ruleExpr = this.getServExcptRuleFilterExpr(ruleExprList.get(0));
                    //更新已经存在的
                    List<MyNtySubServBean> existedSubServList = sw.buildQuery().in("serv_no",newSlted).eq("expr_ref_id",refExprId).doQuery(MyNtySubServBean.class);
                    Set<String> existFlag = new HashSet<String>();
                    Iterator<MyNtySubServBean> existIter = existedSubServList.iterator();
                    while (existIter.hasNext()) {
                        MyNtySubServBean ssb = existIter.next();
                        ssb.setFilterExpr(ruleExpr);
                        existFlag.add(ssb.getServNo());//标记已经存在
                        sw.buildQuery().rowid("mnsId",ssb.getMnsId()).doUpdate(ssb);
                    }
                    //插入不存在的
                    for (int i = 0; i < newSlted.length; i++) {
                        String servNo = newSlted[i];
                        if (servNo != null && servNo.trim().length() > 0 && !existFlag.contains(servNo)) {
                            MyNtySubServBean newSsb = new MyNtySubServBean();
                            newSsb.setFilterExpr(ruleExpr);
                            newSsb.setExprRefId(refExprId);
                            newSsb.setServNo(servNo);
                            newSsb.setRuleId(ruleExprList.get(0).getRuleId());
                            sw.buildQuery().doInsert(newSsb);
                        }
                    }

                }
            }
        }
    }

    /**
     * 保存服务预警规则订阅服务
     *
     * @param ruleId
     * @param unSlt
     * @param newSlted
     */
    public void saveMNSLASubServ(String ruleId,String[] unSlt,String[] newSlted) {
        if (ruleId != null || ruleId.trim().length() > 0) {

            //删除取消订阅的服务
            if (unSlt != null && unSlt.length > 0) {
                sw.buildQuery().in("serv_no",unSlt).eq("RULE_ID",ruleId).table("DSGC_MN_SERVICES").doDelete();
            }

            //添加新增订阅的服务
            if (newSlted != null && newSlted.length > 0) {
                List<MyNtyRulesBean> ruleExprList = sw.buildQuery().eq("ruleId",ruleId).doQuery(MyNtyRulesBean.class);
                if (ruleExprList != null && ruleExprList.size() > 0) {
                    String ruleExpr = ruleExprList.get(0).getRuleExpr();
                    //更新已经存在的
                    List<MyNtySubServBean> existedSubServList = sw.buildQuery().in("serv_no",newSlted).eq("RULE_ID",ruleId).doQuery(MyNtySubServBean.class);
                    Set<String> existFlag = new HashSet<String>();
                    Iterator<MyNtySubServBean> existIter = existedSubServList.iterator();
                    while (existIter.hasNext()) {
                        MyNtySubServBean ssb = existIter.next();
                        ssb.setFilterExpr(ruleExpr);
                        existFlag.add(ssb.getServNo());//标记已经存在
                        sw.buildQuery().rowid("mnsId",ssb.getMnsId()).doUpdate(ssb);
                    }
                    //插入不存在的
                    for (int i = 0; i < newSlted.length; i++) {
                        String servNo = newSlted[i];
                        if (servNo != null && servNo.trim().length() > 0 && !existFlag.contains(servNo)) {
                            MyNtySubServBean newSsb = new MyNtySubServBean();
                            newSsb.setFilterExpr(ruleExpr);
                            newSsb.setExprRefId(null);
                            newSsb.setServNo(servNo);
                            newSsb.setRuleId(ruleId);
                            sw.buildQuery().doInsert(newSsb);
                        }
                    }

                }
            }
        }
    }

    /**
     * 根据用户查询条件检索订阅的服务
     *
     * @param ruleId
     * @param filterServNo
     * @param filterServName
     * @param filterSystem
     * @param unSlt
     * @param newSlted
     * @return
     */
    public List<MyNtyServSltInfoBean> getMNSubcributedServ(String ruleType,String ruleId,String filterServNo,String filterServName,String filterSystem,String[] unSlt,String[] newSlted) {
        List<MyNtyServSltInfoBean> res = new ArrayList<MyNtyServSltInfoBean>();
        if (ruleId == null || ruleId.trim().length() == 0) {
            return res;
        }

        StringBuilder sqlBuilder = new StringBuilder();
        if ("API".equals(ruleType)) {
            sqlBuilder.append("select t.api_code serv_no,t.api_name serv_name,(select sys_name from dsgc_system_entities where sys_code = t.app_code) serv_system,(select ss.creation_date from dsgc_mn_services ss where");
        } else {
            sqlBuilder.append("select t.serv_no,t.serv_name,(select sys_name from dsgc_system_entities where sys_code = t.subordinate_system) serv_system,(select ss.creation_date from dsgc_mn_services ss where");
        }
        if ("SE".equals(ruleType)) {
            sqlBuilder.append(" ss.expr_ref_id ='" + ruleId);
        } else {
            sqlBuilder.append(" ss.rule_id ='" + ruleId);
        }
        if ("API".equals(ruleType)) {
            sqlBuilder.append("' and ss.serv_no = t.api_code ) creation_date from dsgc_apis t");
        } else {
            sqlBuilder.append("' and ss.serv_no = t.serv_no ) creation_date from dsgc_services t");
        }

        String newSltedInStr = this.covertArrayToInStr(newSlted);
        if (newSltedInStr != null) {
            if ("API".equals(ruleType)) {
                sqlBuilder.append(" where (t.api_code in (" + newSltedInStr + ") or t.api_code in (select s.serv_no from dsgc_mn_services s");
            } else {
                sqlBuilder.append(" where (t.serv_no in (" + newSltedInStr + ") or t.serv_no in (select s.serv_no from dsgc_mn_services s");
            }
            if ("SE".equals(ruleType)) {
                sqlBuilder.append(" where s.expr_ref_id = '" + ruleId + "'))");
            } else {
                sqlBuilder.append(" where s.rule_id = '" + ruleId + "'))");
            }
        } else {
            if ("API".equals(ruleType)) {
                sqlBuilder.append(" where t.api_code in (select s.serv_no from dsgc_mn_services s");
            } else {
                sqlBuilder.append(" where t.serv_no in (select s.serv_no from dsgc_mn_services s");
            }
            if ("SE".equals(ruleType)) {
                sqlBuilder.append(" where s.expr_ref_id = '" + ruleId + "')");
            } else {
                sqlBuilder.append(" where s.rule_id = '" + ruleId + "')");
            }
        }

        String unSltInStr = this.covertArrayToInStr(unSlt);
        if (unSltInStr != null) {
            if ("API".equals(ruleType)) {
                sqlBuilder.append("and t.api_code not in (" + unSltInStr + ")");
            } else {
                sqlBuilder.append("and t.serv_no not in (" + unSltInStr + ")");
            }
        }

        if (filterServNo != null && filterServNo.trim().length() > 0) {
            if ("API".equals(ruleType)) {
                sqlBuilder.append(" and t.api_code like '%" + filterServNo + "%'");
            } else {
                sqlBuilder.append(" and t.serv_no like '%" + filterServNo + "%'");
            }
        }

        if (filterServName != null && filterServName.trim().length() > 0) {
            if ("API".equals(ruleType)) {
                sqlBuilder.append(" and t.api_name like  '%" + filterServName + "%'");
            } else {
                sqlBuilder.append(" and t.serv_name like  '%" + filterServName + "%'");
            }
        }

        if (filterSystem != null && filterSystem.trim().length() > 0) {
            if ("API".equals(ruleType)) {
                sqlBuilder.append(" and t.app_code = '" + filterSystem + "'");
            } else {
                sqlBuilder.append(" and t.subordinate_system = '" + filterSystem + "'");
            }
        }

        return sw.buildQuery().sql(sqlBuilder.toString()).doQuery(MyNtyServSltInfoBean.class);
    }


    /**
     * 转化为in（）中的内容
     *
     * @param ar
     * @return
     */
    private String covertArrayToInStr(String[] ar) {
        String res = null;
        if (ar != null && ar.length > 0) {
            for (int i = 0; i < ar.length; i++) {
                if (ar[i] != null && ar[i].trim().length() > 0) {
                    if (res == null) {
                        res = "'" + ar[i] + "'";
                    } else {
                        res = res + "," + "'" + ar[i] + "'";
                    }
                }
            }
        }
        return res;
    }

    /**
     * 根据用户id从数据库中获取接口异常订阅规则
     *
     * @param userId
     * @return
     */
    public List<ServExcptSubRulesBean> getServExcptSubRules(String userId) {
        return sw.buildViewQuery("Q_BY_USERID").setVar("userId",userId).doQuery(ServExcptSubRulesBean.class);
    }


    /**
     * 根据前端界面操作更新数据
     *
     * @param userId
     * @param chgs
     */
    public void updServExcptSubRules(String userId,List<ServExcptSubRulesBean> chgs) {
        String ruleId = initServExcptSubcribute(userId);
        if (chgs != null && chgs.size() > 0) {
            Iterator<ServExcptSubRulesBean> iters = chgs.iterator();
            while (iters.hasNext()) {
                ServExcptSubRulesBean ssrb = iters.next();
                if (ssrb.getOperType() == -1) {
                    //执行删除操作
                    this.deleteServExcptSubcribute(ssrb.getSeeId());
                } else if (ssrb.getOperType() != 0 && (ssrb.getSeeId() == null || ssrb.getSeeId().trim().length() == 0)) {
                    //执行新增操作
                    if (ssrb.getRuleId() == null || ssrb.getRuleId().trim().length() == 0) {
                        ssrb.setRuleId(ruleId);
                    }

                    sw.buildQuery().doInsert(ssrb);

                } else if (ssrb.getOperType() != 0 && ssrb.getSeeId() != null && ssrb.getSeeId().trim().length() > 0) {
                    //更新操作
                    this.updateServExcptSubscribute(ssrb);
                }
            }
        }
    }


    /**
     * 初始化服务异常监听订阅配置信息
     *
     * @param userId
     * @return
     */
    private String initServExcptSubcribute(String userId) {
        String ruleId = null;
        List<Map<String,Object>> isExist = sw.buildQuery().sql("select r.rule_id from dsgc_mn_subcribes s,dsgc_mn_rules r where s.mn_rule = r.rule_id and r.rule_type = 'SE' and s.scb_user = #userId and rownum  = 1")
                .setVar("userId",userId).doQuery();
        if (isExist.size() == 0 || isExist.get(0).size() == 0) {
            //添加订阅规则
            MyNtyRulesBean mnr = this.generateServExcptRule();
            sw.buildQuery().doInsert(mnr);
            ruleId = mnr.getRuleId();

            //添加订阅
            MyNtySubcribesBean addSub = new MyNtySubcribesBean();
            addSub.setScbUser(userId);
            addSub.setMnRule(ruleId);
            addSub.setIsEnable("Y");
            sw.buildQuery().doInsert(addSub);
        } else {
            ruleId = (String)isExist.get(0).get("RULE_ID");
        }
        return ruleId;
    }

    /**
     * 删除服务异常订阅信息
     *
     * @param seeId
     */
    private void deleteServExcptSubcribute(String seeId) {
        sw.buildQuery().rowid("seeId",seeId).doDelete(ServExcptSubRulesBean.class);
        sw.buildQuery().eq("exprRefId",seeId).doDelete(MyNtySubServBean.class);
    }

    /**
     * 更新服务异常订阅规则信息
     *
     * @param ssrb
     */
    private void updateServExcptSubscribute(ServExcptSubRulesBean ssrb) {
        sw.buildQuery().rowid("seeId",ssrb.getSeeId()).doUpdate(ssrb);

        //更新该规则下所有订阅的服务的过滤表达式
        String filterExpr = this.getServExcptRuleFilterExpr(ssrb);

        //通过组装后的filterExpr表达式更新所有订阅的服务
        sw.buildQuery().update("filter_expr",filterExpr).eq("exprRefId",ssrb.getSeeId()).doUpdate(MyNtySubServBean.class);
    }


    /**
     * 获取服务异常通知服务过滤规则表达式
     *
     * @param ssrb
     * @return
     */
    private String getServExcptRuleFilterExpr(ServExcptSubRulesBean ssrb) {
        String errorExpr = "";
        if ("Y".equals(ssrb.getErrorFail()) && !"Y".equals(ssrb.getBizFail())) {
            errorExpr = "(instStat = '0')";
        } else if (!"Y".equals(ssrb.getErrorFail()) && "Y".equals(ssrb.getBizFail())) {
            errorExpr = "(bizStat = '0')";
        } else {
            //默认接口错误和业务错误都订阅
            errorExpr = "(instStat = '0' or bizStat = '0')";
        }

        String filterExpr = ssrb.getReExpr();
        if (filterExpr != null && filterExpr.trim().length() > 0) {
            //进行转义
            filterExpr = this.covertDescToRealExpress(filterExpr,"MN_SE_EXPR_FILEDS");
            filterExpr = "(" + filterExpr + ") and " + errorExpr;
        } else {
            filterExpr = errorExpr;
        }
        return filterExpr;
    }

    /**
     * 讲中文表达式转化为真实可以执行的表达式
     *
     * @param exprDesc
     * @return
     */
    private String covertDescToRealExpress(String exprDesc,String lookupTypeCode) {
        Map<String,String> lkv = this.lkvDao.getlookupValues(lookupTypeCode);
        return covertDescToRealExpress(exprDesc,lkv);
    }

    public String covertDescToRealExpress(String exprDesc,Map<String,String> lkv) {
        String res = null;
        if (lkv != null && exprDesc != null && exprDesc.trim().length() > 0) {
            res = new String(exprDesc);
            Iterator<Map.Entry<String,String>> lkIter = lkv.entrySet().iterator();
            while (lkIter.hasNext()) {
                Map.Entry<String,String> e = lkIter.next();
                res = res.replaceAll(e.getValue(),e.getKey());
            }
            res = res.replaceAll("或","or");
            res = res.replaceAll("与","and");
        }
        return res;
    }


    /**
     * 生成一个初始化的服务异常通知规则
     *
     * @return
     */
    private MyNtyRulesBean generateServExcptRule() {
        MyNtyRulesBean mnr = new MyNtyRulesBean();
        mnr.setRuleTitle("SE");
        mnr.setRuleType("SE");
        mnr.setMnLevel(2);
        mnr.setDayBD("00:00:00");
        mnr.setDayED("23:59:59");
        mnr.setRunInterval(5 * 60 * 1000L);
        mnr.setRuleExpr("1=1");
        mnr.setIsEnable("Y");
        String disableDate = "9999.12.31 23:59:59";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            mnr.setDisableTime(sdf.parse(disableDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mnr;
    }

    public Map<String,String> getRuleExprDescLKV(String ruleType) {
        Map<String,String> lkv = null;
        if ("SE".equals(ruleType)) {
            lkv = this.lkvDao.getlookupValues("MN_SE_EXPR_FILEDS");
        } else if ("SLA".equals(ruleType)) {
            lkv = this.lkvDao.getlookupValues("MN_SLA_EXPR_FILEDS");
        }
        return lkv;
    }

    public List<DSGCMnNotices> findDSGCMnNotices(DSGCMnNotices dsgcMnNotices) {
        MpaasQuery mpaasQuery = this.sw.buildQuery()
                .eq("ntyUser",dsgcMnNotices.getNtyUser());
        if (StringUtils.isNotEmpty(dsgcMnNotices.getMnType())) {
            mpaasQuery = mpaasQuery.eq("mnType",dsgcMnNotices.getMnType());
        }
        if (StringUtils.isNotEmpty(dsgcMnNotices.getReadStat()) && !dsgcMnNotices.getReadStat().equals("all")) {
            if ("unread".equals(dsgcMnNotices.getReadStat())) {
                mpaasQuery = mpaasQuery.eq("readStat",'0');
            }
            if ("read".equals(dsgcMnNotices.getReadStat())) {
                mpaasQuery = mpaasQuery.eq("readStat",'1');
            }

        }
        List<DSGCMnNotices> dsgcMnNoticesList = mpaasQuery.doQuery(DSGCMnNotices.class);

        return dsgcMnNoticesList;
    }


    public List<DSGCMnNotices> findDSGCMnNoticesByMnTitle(DSGCMnNotices dsgcMnNotices) {
        MpaasQuery mpaasQuery = this.sw.buildQuery().eq("ntyUser",dsgcMnNotices.getNtyUser());
        if (StringUtils.isNotEmpty(dsgcMnNotices.getMnTitle())) {
            mpaasQuery = mpaasQuery.eq("mnTitle",dsgcMnNotices.getMnTitle());
        }
        List<DSGCMnNotices> dsgcMnNoticesList = mpaasQuery.doQuery(DSGCMnNotices.class);
//        if(dsgcMnNotices.getMnCnt() ==""||"".equals(dsgcMnNotices.getMnCnt())){
//            return sw.buildQuery()
//                    .sql("select * from  dsgc_mn_notices n where n.nty_user = #ntyUser")
//                    .setVar("ntyUser",dsgcMnNotices.getNtyUser())
//                    .doQuery(DSGCMnNotices.class);
//
//        }
//        return sw.buildQuery()
//                .sql("select * from  dsgc_mn_notices n where n.nty_user = #ntyUser and dbms_lob.instr(n.mn_cnt, #mnCnt) > 0")
//                .setVar("mnCnt", dsgcMnNotices.getMnCnt())
//                .setVar("ntyUser",dsgcMnNotices.getNtyUser())
//                .doQuery(DSGCMnNotices.class);
        return dsgcMnNoticesList;
    }

    public void updateDSGCMnNotices(DSGCMnNotices mnNotices) {
        this.sw.buildQuery()
                .update("readStat",mnNotices.getReadStat())
                .eq("ntyUser",mnNotices.getNtyUser())
                .doUpdate(mnNotices);
    }

    public void updateDSGCMnNoticesById(DSGCMnNotices mnNotices) {
        this.sw.buildQuery()
                .update("readStat",mnNotices.getReadStat())
                .eq("mnId",mnNotices.getMnId())
                .doUpdate(mnNotices);
    }

    public List<Map<String,Object>> getServByUser(DSGCUser dsgcUser) {
        return sw.buildQuery().sql("select serv.SERV_NO, serv.SERV_NAME, sys_u.SYS_CODE " +
                "from DSGC_SERVICES serv left join DSGC_SYSTEM_USER sys_u on sys_u.SYS_CODE = serv.SUBORDINATE_SYSTEM where USER_ID = #userId")
                .setVar("userId",dsgcUser.getUserId())
                .doQuery();

    }


    public List<MyNtyUserSltInfoBean> getMNSubUser(String ruleType,String ruleId,String filterUserName,String filterUserDesc,String[] unSlt,String[] newSlted) {
        List<MyNtyUserSltInfoBean> res = new ArrayList<MyNtyUserSltInfoBean>();
        if (ruleId == null || ruleId.trim().length() == 0) {
            return res;
        }
        StringBuilder sql = new StringBuilder();

        sql.append("select du.user_id,du.user_name,du.user_role,du.user_mail,du.user_phone,du.user_description,(select dmu.creation_date from dsgc_mn_user dmu where dmu.rule_id = '" + ruleId + "' and du.user_id = dmu.user_id ) creation_date from dsgc_user du");

        String newSltedInStr = this.covertArrayToInStr(newSlted);
        if (newSltedInStr != null) {
            sql.append(" where (du.user_id in (" + newSltedInStr + ") or du.user_id in (select u.user_id from dsgc_mn_user u where u.rule_id = '" + ruleId + "'))");
        } else {
            sql.append(" where du.user_id in (select u.user_id from dsgc_mn_user u where u.rule_id = '" + ruleId + "')");
        }

        String unSltInStr = this.covertArrayToInStr(unSlt);
        if (unSltInStr != null) {
            sql.append("and du.user_id not in (" + unSltInStr + ")");
        }


        if (filterUserName != null && filterUserName.trim().length() > 0) {
            sql.append(" and du.user_name like '%" + filterUserName + "%'");
        }

        if (filterUserDesc != null && filterUserDesc.trim().length() > 0) {
            sql.append(" and du.user_description like  '%" + filterUserDesc + "%'");
        }

        return sw.buildQuery().sql(sql.toString()).doQuery(MyNtyUserSltInfoBean.class);
    }


    /**
     * 保存服务预警规则订阅服务
     *
     * @param ruleId
     * @param unSlt
     * @param newSlted
     */
    public void saveMNSubUser(String ruleType,String ruleId,String[] unSlt,String[] newSlted) {
        if ("SE".equals(ruleType)) {
            this.saveMNSESubUser(ruleId,unSlt,newSlted);
        } else {
            this.saveMNSASubUser(ruleId,unSlt,newSlted);
        }
    }


    public void saveMNSASubUser(String ruleId,String[] unSlt,String[] newSlted) {
        if (ruleId != null || ruleId.trim().length() > 0) {

            //删除取消订阅的服务
            if (unSlt != null && unSlt.length > 0) {
                sw.buildQuery().in("user_id",unSlt).eq("RULE_ID",ruleId).table("DSGC_MN_USER").doDelete();
            }

            //添加新增订阅的服务
            if (newSlted != null && newSlted.length > 0) {
                List<MyNtyRulesBean> ruleExprList = sw.buildQuery().eq("ruleId",ruleId).doQuery(MyNtyRulesBean.class);
                if (ruleExprList != null && ruleExprList.size() > 0) {
                    String ruleExpr = ruleExprList.get(0).getRuleExpr();
                    //更新已经存在的
                    List<MyNtyUserBean> existedSubServList = sw.buildQuery().in("user_id",newSlted).eq("RULE_ID",ruleId).doQuery(MyNtyUserBean.class);
                    Set<String> existFlag = new HashSet<String>();
                    Iterator<MyNtyUserBean> existIter = existedSubServList.iterator();
                    while (existIter.hasNext()) {
                        MyNtyUserBean ssb = existIter.next();
                        existFlag.add(ssb.getUserId());//标记已经存在
                        sw.buildQuery().rowid("dmuId",ssb.getDmuId()).doUpdate(ssb);
                    }
                    //插入不存在的
                    for (int i = 0; i < newSlted.length; i++) {
                        String userId = newSlted[i];
                        if (userId != null && userId.trim().length() > 0 && !existFlag.contains(userId)) {
                            MyNtyUserBean newSsb = new MyNtyUserBean();
                            newSsb.setUserId(userId);
                            newSsb.setRuleId(ruleId);
                            sw.buildQuery().doInsert(newSsb);
                        }
                    }

                }
            }
        }
    }

    public void saveMNSESubUser(String ruleId,String[] unSlt,String[] newSlted) {
        if (ruleId != null || ruleId.trim().length() > 0) {

            //删除取消订阅的服务
            if (unSlt != null && unSlt.length > 0) {
                sw.buildQuery().in("user_id",unSlt).eq("RULE_ID",ruleId).table("DSGC_MN_USER").doDelete();
            }

            //添加新增订阅的服务
            if (newSlted != null && newSlted.length > 0) {
                List<ServExcptSubRulesBean> ruleExprList = sw.buildQuery().eq("seeId",ruleId).doQuery(ServExcptSubRulesBean.class);
                if (ruleExprList != null && ruleExprList.size() > 0) {
                    String ruleExpr = ruleExprList.get(0).getReExpr();
                    //更新已经存在的
                    List<MyNtyUserBean> existedSubServList = sw.buildQuery().in("user_id",newSlted).eq("RULE_ID",ruleId).doQuery(MyNtyUserBean.class);
                    Set<String> existFlag = new HashSet<String>();
                    Iterator<MyNtyUserBean> existIter = existedSubServList.iterator();
                    while (existIter.hasNext()) {
                        MyNtyUserBean ssb = existIter.next();
                        existFlag.add(ssb.getUserId());//标记已经存在
                        sw.buildQuery().rowid("dmuId",ssb.getDmuId()).doUpdate(ssb);
                    }
                    //插入不存在的
                    for (int i = 0; i < newSlted.length; i++) {
                        String userId = newSlted[i];
                        if (userId != null && userId.trim().length() > 0 && !existFlag.contains(userId)) {
                            MyNtyUserBean newSsb = new MyNtyUserBean();
                            newSsb.setUserId(userId);
                            newSsb.setRuleId(ruleId);
                            sw.buildQuery().doInsert(newSsb);
                        }
                    }

                }
            }
        }
    }
}
