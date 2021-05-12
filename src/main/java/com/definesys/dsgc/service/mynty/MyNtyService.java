package com.definesys.dsgc.service.mynty;

import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.lov.LovDao;
import com.definesys.dsgc.service.mynty.bean.*;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.*;

@Service
public class MyNtyService {

    @Autowired
    private MyNtyDao mndao;

    @Autowired
    private SVCAuthDao svcAuthDao;

    @Autowired
    private LovDao lovDao;

    @Autowired
    private UserHelper userHelper;

    public PageQueryResult<MyNtyQueryListBean> queryMNRules(String uid,MyNtyQueryParamVO reqParam,int pageSize,int pageIndex) {

        UserHelper uh = this.userHelper.user(uid);

        PageQueryResult<MyNtyQueryListBean> res = this.mndao.queryMNRules(uid,uh,reqParam,pageSize,pageIndex);

        List<MyNtyQueryListBean> resLst = res.getResult();
        if (resLst != null) {
            Iterator<MyNtyQueryListBean> resIter = resLst.iterator();
            while (resIter.hasNext()) {
                MyNtyQueryListBean ob = resIter.next();
                if (uid != null && uid.equals(ob.getCreatedBy()) || uh.isSuperAdministrator() || uh.isAdmin()) {
                    ob.setReadonly(false);
                } else if (uh.isSystemMaintainer() && uh.isSpecifySystemMaintainer(ob.getAppCode())) {
                    ob.setReadonly(false);
                } else {
                    ob.setReadonly(true);
                }
            }
        }
        return res;
    }

    public void cancelSubscribe(String uid,RuleStatSetVO reqParam) {
        this.mndao.cancelSubscribe(uid,reqParam);
    }

    public String setRuleStat(String uid,RuleStatSetVO reqParam) {
        MyNtyRulesBean rule = this.mndao.getMyNtyRuleDtl(reqParam.getRuleId());
        if (rule != null) {
            UserHelper uh = this.userHelper.user(uid);
            if (!(uh.isSuperAdministrator()
                    || uh.isAdmin()
                    || uh.isSystemMaintainer() && uh.isSpecifySystemMaintainer(rule.getAppCode())
                    || uid.equals(rule.getCreatedBy()))) {
                return "无效的操作权限！";
            }

            this.mndao.setRuleStat(reqParam);
        }
        return "S";
    }

    public void delMNRule(String uid,String ruleId) throws Exception{
        if(ruleId != null && ruleId.trim().length() > 0){
            MyNtyRulesBean rule = this.mndao.getMyNtyRuleDtl(ruleId);
            //执行更新操作要先判断权限
            UserHelper uh = this.userHelper.user(uid);
            //执行更新操作，要判断权限
            rule = this.mndao.getMyNtyRuleDtl(ruleId);
            if (!(uh.isSuperAdministrator()
                    || uh.isAdmin()
                    || uh.isSystemMaintainer() && uh.isSpecifySystemMaintainer(rule.getAppCode())
                    || uid.equals(rule.getCreatedBy()))) {
                throw new Exception("无效的操作权限！");
            }
            this.mndao.deleteMyNtyRule(ruleId);
        }

    }

    public MyNtyRuleDetailVO getMNRuleDetail(String ruleId) {
        MyNtyRuleDetailVO res = new MyNtyRuleDetailVO();

        MyNtyRulesBean rule = this.mndao.getMyNtyRuleDtl(ruleId);
        if (rule != null) {
            res.setRuleId(rule.getRuleId());
            res.setDisableTime(rule.getDisableTime());
            res.setMnLevel(rule.getMnLevel());
            res.setRuleExpr(rule.getRuleExprDesc());
            res.setRunInterval(rule.getRunInterval() / 60 / 60 / 1000);
            res.setRuleTitle(rule.getRuleTitle());
            res.setRuleType(rule.getRuleType());
            res.setAppCode(rule.getAppCode());
            res.setRuleTypeMeaning(this.mndao.getRuleTypeMeaningFromLKV(rule.getRuleType()));
            res.setAppName(this.mndao.getAppCodeName(rule.getAppCode()));
        }

        MyNtyServSltBean serSlt = new MyNtyServSltBean();
        serSlt.setRuleId(rule.getRuleId());
        serSlt.setRuleType(rule.getRuleType());
        res.setServSlt(this.getMNSubscributedServList(serSlt));

        MyNtyUserSltBean userSlt = new MyNtyUserSltBean();
        userSlt.setRuleId(rule.getRuleId());
        userSlt.setRuleType(rule.getRuleType());
        res.setUserSlt(userSlt);

        return res;
    }

    public MyNtyRuleDetailVO updateMNRuleDetail(String uid,MyNtyRuleDetailVO reqParam) throws Exception {
        MyNtyRulesBean rule = new MyNtyRulesBean();
        if (reqParam.getRuleId() != null && reqParam.getRuleId().trim().length() >0) {
            //执行更新操作要先判断权限
            UserHelper uh = this.userHelper.user(uid);
            //执行更新操作，要判断权限
            rule = this.mndao.getMyNtyRuleDtl(reqParam.getRuleId());
            if (!(uh.isSuperAdministrator()
                    || uh.isAdmin()
                    || uh.isSystemMaintainer() && uh.isSpecifySystemMaintainer(rule.getAppCode())
                    || uid.equals(rule.getCreatedBy()))) {
                throw new Exception("无效的操作权限！");
            }
        }

        rule.setRunInterval(reqParam.getRunInterval() * 60 * 60 * 1000);
        rule.setRuleExprDesc(reqParam.getRuleExpr());
        Map<String,String> lkv = mndao.getRuleExprDescLKV(reqParam.getRuleType());
        rule.setRuleExpr(mndao.covertDescToRealExpress(reqParam.getRuleExpr(),lkv));

        rule.setDisableTime(reqParam.getDisableTime());
        rule.setMnLevel(reqParam.getMnLevel());
        rule.setRuleTitle(reqParam.getRuleTitle());
        rule.setAppCode(reqParam.getAppCode());
        rule.setRuleType(reqParam.getRuleType());
        this.mndao.updateMNRule(rule);
        reqParam.setRuleId(rule.getRuleId());

        //更新选择的服务
        reqParam.getServSlt().setRuleId(reqParam.getRuleId());
        reqParam.getServSlt().setRuleType(reqParam.getRuleType());
        reqParam.setServSlt(this.saveMNSubcributeServList(reqParam.getServSlt()));

        //更新选择的用户
        reqParam.getUserSlt().setRuleId(reqParam.getRuleId());
        reqParam.getUserSlt().setRuleType(reqParam.getRuleType());

        reqParam.setUserSlt(this.saveMNSubUser(reqParam.getUserSlt()));
        return reqParam;
    }

    /**
     * 获取我的通知订阅规则
     *
     * @param userId
     * @param ruleType
     * @return
     * @deprecated
     */
    public List<MyNtyRulesBean> getMNRules(String userId,String ruleType) {
        List<MyNtyRulesBean> res = mndao.getMNRules(userId,ruleType);
        if (res != null) {
            Iterator<MyNtyRulesBean> mnrbIter = res.iterator();
            while (mnrbIter.hasNext()) {
                MyNtyRulesBean mnrb = mnrbIter.next();
                if ("Y".equals(mnrb.getIsEnable())) {
                    mnrb.setIsEnableBL(true);
                } else {
                    mnrb.setIsEnableBL(false);
                }
                mnrb.setRunInterval(mnrb.getRunInterval() / 60 / 60 / 1000);
                mnrb.setRuleExpr(mnrb.getRuleExprDesc());
            }
        }
        return res;
    }

    /**
     * 更新我的通知订阅规则
     *
     * @param userId
     * @param chgs
     * @deprecated
     */
    public void updateMNRules(String userId,List<MyNtyRulesBean> chgs) {
        if (chgs != null) {
            Iterator<MyNtyRulesBean> mnrbIter = chgs.iterator();
            Map<String,Map<String,String>> lkvStore = new HashMap<String,Map<String,String>>();
            while (mnrbIter.hasNext()) {
                MyNtyRulesBean mnrb = mnrbIter.next();
                if (mnrb.getIsEnableBL()) {
                    mnrb.setIsEnable("Y");
                } else {
                    mnrb.setIsEnable("N");
                }
                mnrb.setRunInterval(mnrb.getRunInterval() * 60 * 60 * 1000);
                mnrb.setRuleExprDesc(mnrb.getRuleExpr());
                Map<String,String> lkv = lkvStore.get(mnrb.getRuleType());
                if (lkv == null) {
                    lkv = mndao.getRuleExprDescLKV(mnrb.getRuleType());
                    lkvStore.put(mnrb.getRuleType(),lkv);
                }
                mnrb.setRuleExpr(mndao.covertDescToRealExpress(mnrb.getRuleExpr(),lkv));
            }
            mndao.updateMNRules(userId,chgs);
        }
    }

    /**
     * 保存我的通知订阅规则选择的服务
     *
     * @param sltReq
     * @return
     */
    public MyNtyServSltBean saveMNSubcributeServList(MyNtyServSltBean sltReq) {
        sltReq = this.refreshMNServSltBean(sltReq);
        //更新数据库
        mndao.saveMNSubcributeServ(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getOldDel(),sltReq.getOldAdd());

        //刷新查询结果
        sltReq.setSltServs(mndao.getMNSubcributedServ(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getFilterServNo(),sltReq.getFilterServName(),sltReq.getFilterServSystem(),sltReq.getOldDel(),sltReq.getOldAdd()));

        //保存后，重制用户的操作记录
        sltReq.setOldDel(null);
        sltReq.setOldAdd(null);
        return sltReq;
    }

    /**
     * 根据用户查询条件检索订阅的服务
     *
     * @param sltReq
     * @return
     */
    public MyNtyServSltBean getMNSubscributedServList(MyNtyServSltBean sltReq) {


        sltReq = this.refreshMNServSltBean(sltReq);
        sltReq.setSltServs(mndao.getMNSubcributedServ(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getFilterServNo(),sltReq.getFilterServName(),sltReq.getFilterServSystem(),sltReq.getOldDel(),sltReq.getOldAdd()));

        return sltReq;
    }

    /**
     * 根据前端的操作，总结操作的结果
     *
     * @param sltReq
     * @return
     */
    private MyNtyServSltBean refreshMNServSltBean(MyNtyServSltBean sltReq) {
        Set<String> oldAdd = new HashSet<String>();
        if (sltReq.getOldAdd() != null) {
            for (String servNo : sltReq.getOldAdd()) {
                if (!oldAdd.contains(servNo)) {
                    oldAdd.add(servNo);
                }
            }
        }

        Set<String> oldDel = new HashSet<String>();
        if (sltReq.getOldDel() != null) {
            for (String servNo : sltReq.getOldDel()) {
                if (!oldDel.contains(servNo)) {
                    oldDel.add(servNo);
                }
            }
        }

        if (sltReq.getCurChgs() != null && sltReq.getCurChgs().length > 0) {
            for (int i = 0; i < sltReq.getCurChgs().length; i++) {
                String servNo = sltReq.getCurChgs()[i];
                if (servNo != null && servNo.trim().length() > 0) {
                    if ("A".equals(sltReq.getCurOper())) {
                        if (oldDel.contains(servNo)) {
                            oldDel.remove(servNo);
                        } else {
                            if (!oldAdd.contains(servNo)) {
                                oldAdd.add(servNo);
                            }
                        }

                    } else if ("D".equals(sltReq.getCurOper())) {
                        if (oldAdd.contains(servNo)) {
                            oldAdd.remove(servNo);
                        } else {
                            if (!oldDel.contains(servNo)) {
                                oldDel.add(servNo);
                            }
                        }
                    }
                }
            }
        }

        sltReq.setCurOper(null);
        sltReq.setCurChgs(null);

        String[] newOldAdd = new String[oldAdd.size()];
        sltReq.setOldAdd(oldAdd.toArray(newOldAdd));

        String[] newOldDel = new String[oldDel.size()];
        sltReq.setOldDel(oldDel.toArray(newOldDel));

        return sltReq;
    }

    /**
     * 获取接口异常订阅规则接口
     *
     * @param userId
     * @return
     */
    public List<ServExcptSubRulesBean> getServExcptSubRules(String userId) {
        List<ServExcptSubRulesBean> res = mndao.getServExcptSubRules(userId);
        if (res != null) {
            Iterator<ServExcptSubRulesBean> iter = res.iterator();
            while (iter.hasNext()) {
                ServExcptSubRulesBean ssrb = iter.next();
                if ("Y".equals(ssrb.getErrorFail())) {
                    ssrb.setErrorFailBL(true);
                } else {
                    ssrb.setErrorFailBL(false);
                }
                if ("Y".equals(ssrb.getBizFail())) {
                    ssrb.setBizFailBL(true);
                } else {
                    ssrb.setBizFailBL(false);
                }
                if ("Y".equals(ssrb.getIsEnable())) {
                    ssrb.setIsEnableBL(true);
                } else {
                    ssrb.setIsEnableBL(false);
                }
            }
        }
        return res;
    }

    /**
     * 更新接口异常订阅规则接口
     *
     * @param chgs
     */
    public void updateServExcptSubRules(String userId,List<ServExcptSubRulesBean> chgs) {
        if (chgs != null) {
            Iterator<ServExcptSubRulesBean> iter = chgs.iterator();
            while (iter.hasNext()) {
                ServExcptSubRulesBean ssrb = iter.next();
                if (ssrb.getErrorFailBL()) {
                    ssrb.setErrorFail("Y");
                } else {
                    ssrb.setErrorFail("N");
                }
                if (ssrb.getBizFailBL()) {
                    ssrb.setBizFail("Y");
                } else {
                    ssrb.setBizFail("N");
                }
                if (ssrb.getIsEnableBL()) {
                    ssrb.setIsEnable("Y");
                } else {
                    ssrb.setIsEnable("N");
                }
            }
        }
        //将jsonbean转化为数据库实体bean
        mndao.updServExcptSubRules(userId,chgs);
    }

    @Transactional(rollbackFor = Exception.class)
    public MyMnNoticesDTO findDSGCMnNotices(DSGCMnNotices dsgcMnNotices) {
        MyMnNoticesDTO myMnNoticesDTO = new MyMnNoticesDTO();

        DSGCMnNotices noticesCount = this.mndao.getNoticesCount(dsgcMnNotices.getNtyUser());
        if(noticesCount!=null){
            myMnNoticesDTO.setAllCount(noticesCount.getAllCount());
            myMnNoticesDTO.setUnreadCount(noticesCount.getUnreadCount());
        }else {
            myMnNoticesDTO.setAllCount(0);
            myMnNoticesDTO.setUnreadCount(0);
        }

        List<DSGCMnNotices> mnNotices = this.mndao.findDSGCMnNotices(dsgcMnNotices);
        myMnNoticesDTO.setNotices(mnNotices);

        return myMnNoticesDTO;
    }


    public List<DSGCMnNotices> findDSGCMnNoticesByMnTitle(DSGCMnNotices dsgcMnNotices) {
        return this.mndao.findDSGCMnNoticesByMnTitle(dsgcMnNotices);
    }

    public List<Map<String,Object>> getServByUser(DSGCUser dsgcUser) {
        return mndao.getServByUser(dsgcUser);
    }

    public void updateDSGCMnNoticesById(DSGCMnNotices dsgcMnNotices) {
        mndao.updateDSGCMnNoticesById(dsgcMnNotices);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDSGCMnNotices(DSGCMnNotices mnNotices) {
        this.mndao.updateDSGCMnNotices(mnNotices);
    }


    public MyNtyUserSltBean getMNSubUser(MyNtyUserSltBean sltReq) {


        sltReq = this.refreshMNUserSltBean(sltReq);
        sltReq.setSltUsers(mndao.getMNSubUser(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getFilterUserName(),sltReq.getFilterUserDesc(),sltReq.getOldDel(),sltReq.getOldAdd()));

        return sltReq;
    }

    private MyNtyUserSltBean refreshMNUserSltBean(MyNtyUserSltBean sltReq) {
        Set<String> oldAdd = new HashSet<String>();
        if (sltReq.getOldAdd() != null) {
            for (String userId : sltReq.getOldAdd()) {
                if (!oldAdd.contains(userId)) {
                    oldAdd.add(userId);
                }
            }
        }

        Set<String> oldDel = new HashSet<String>();
        if (sltReq.getOldDel() != null) {
            for (String userId : sltReq.getOldDel()) {
                if (!oldDel.contains(userId)) {
                    oldDel.add(userId);
                }
            }
        }

        if (sltReq.getCurChgs() != null && sltReq.getCurChgs().length > 0) {
            for (int i = 0; i < sltReq.getCurChgs().length; i++) {
                String userId = sltReq.getCurChgs()[i];
                if (userId != null && userId.trim().length() > 0) {
                    if ("A".equals(sltReq.getCurOper())) {
                        if (oldDel.contains(userId)) {
                            oldDel.remove(userId);
                        } else {
                            if (!oldAdd.contains(userId)) {
                                oldAdd.add(userId);
                            }
                        }

                    } else if ("D".equals(sltReq.getCurOper())) {
                        if (oldAdd.contains(userId)) {
                            oldAdd.remove(userId);
                        } else {
                            if (!oldDel.contains(userId)) {
                                oldDel.add(userId);
                            }
                        }
                    }
                }
            }
        }

        sltReq.setCurOper(null);
        sltReq.setCurChgs(null);

        String[] newOldAdd = new String[oldAdd.size()];
        sltReq.setOldAdd(oldAdd.toArray(newOldAdd));

        String[] newOldDel = new String[oldDel.size()];
        sltReq.setOldDel(oldDel.toArray(newOldDel));

        return sltReq;
    }

    public MyNtyUserSltBean saveMNSubUser(MyNtyUserSltBean sltReq) {
        sltReq = this.refreshMNUserSltBean(sltReq);
        //更新数据库
        mndao.saveMNSubUser(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getOldDel(),sltReq.getOldAdd());

        //刷新查询结果
        sltReq.setSltUsers(mndao.getMNSubUser(sltReq.getRuleType(),sltReq.getRuleId(),sltReq.getFilterUserName(),sltReq.getFilterUserDesc(),sltReq.getOldDel(),sltReq.getOldAdd()));

        //保存后，重制用户的操作记录
        sltReq.setOldDel(null);
        sltReq.setOldAdd(null);
        return sltReq;
    }

    public PageQueryResult<UserResDTO> queryUserList(CommonReqBean commonReqBean,int pageSize,int pageIndex,String userRole){
        PageQueryResult<UserResDTO> resDTOPageQueryResult = new PageQueryResult<>();
        if("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)||"SystemLeader".equals(userRole)){
            List<FndLookupValue> lookupValueList = svcAuthDao.queryFndModuleByLookupType("plateFormRole");
            PageQueryResult<DSGCUser> userList = mndao.queryUserList(commonReqBean,pageSize,pageIndex);
            Long total = userList.getCount();
            List<UserResDTO> resDTOS = userDataMapping(userList.getResult(),lookupValueList);
            resDTOPageQueryResult.setCount(total);
            resDTOPageQueryResult.setResult(resDTOS);
        }else {
            return null;
        }
        return resDTOPageQueryResult;
    }

    public List<UserResDTO> userDataMapping(List<DSGCUser> dsgcUsers,List<FndLookupValue> lookupValueList){
        List<UserResDTO> result = new ArrayList<>();
        for (int i=0;i<dsgcUsers.size();i++){
            UserResDTO userResDTO = new UserResDTO();
            userResDTO.setUserId(dsgcUsers.get(i).getUserId());
            userResDTO.setUserName(dsgcUsers.get(i).getUserName());
            userResDTO.setUserRole(dsgcUsers.get(i).getUserRole());
            userResDTO.setUserDescription(dsgcUsers.get(i).getUserDescription());
            userResDTO.setUserMail(dsgcUsers.get(i).getUserMail());
            userResDTO.setUserPhone(dsgcUsers.get(i).getUserPhone());
            for (int j = 0;j<lookupValueList.size();j++){
                if (userResDTO.getUserRole().equals(lookupValueList.get(j).getLookupCode())){
                    userResDTO.setUserRoleName(lookupValueList.get(j).getMeaning());
                    break;
                }
            }
            result.add(userResDTO);
        }
        return result;
    }


    public MyMnNoticesCountDTO getUserMNNoticesCount(String userId){
        return this.mndao.getNoticesCountDTO(userId);
    }

    public boolean pushNotice(PushNoticeDTO notice) {
        if (notice != null) {
            try {
                Class pusherClass = Class.forName("com.definesys.dsgc.mnpush.NoticePush");
                Method pushMethod = pusherClass.getMethod("push",String.class,String.class,String.class,String.class,String.class,String.class,String.class);
                boolean res = (Boolean)pushMethod.invoke(pusherClass.newInstance(),notice.getSendTo(),notice.getNtyTitle(),notice.getNtySour(),notice.getMnLevel(),notice.getCntShort(),notice.getCntText(),notice.getCntFormat());
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }
}
