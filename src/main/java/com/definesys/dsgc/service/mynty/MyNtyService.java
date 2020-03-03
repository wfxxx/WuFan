package com.definesys.dsgc.service.mynty;

import com.definesys.dsgc.service.mynty.bean.*;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MyNtyService {

    @Autowired
    private MyNtyDao mndao;


    /**
     * 获取我的通知订阅规则
     * @param userId
     * @param ruleType
     * @return
     */
    public List<MyNtyRulesBean> getMNRules(String userId,String ruleType){
        List<MyNtyRulesBean> res = mndao.getMNRules(userId,ruleType);
        if(res != null){
            Iterator<MyNtyRulesBean> mnrbIter = res.iterator();
            while(mnrbIter.hasNext()){
                MyNtyRulesBean mnrb = mnrbIter.next();
                if("Y".equals(mnrb.getIsEnable())){
                    mnrb.setIsEnableBL(true);
                }else{
                    mnrb.setIsEnableBL(false);
                }
                mnrb.setRunInterval(mnrb.getRunInterval()/60/60/1000);
                mnrb.setRuleExpr(mnrb.getRuleExprDesc());
            }
        }
        return res;
    }

    /**
     * 更新我的通知订阅规则
     * @param userId
     * @param chgs
     */
    public void updateMNRules(String userId,List<MyNtyRulesBean> chgs){
        if(chgs != null){
            Iterator<MyNtyRulesBean> mnrbIter = chgs.iterator();
            Map<String,Map<String,String>> lkvStore = new HashMap<String,Map<String,String>>();
            while(mnrbIter.hasNext()){
                MyNtyRulesBean mnrb = mnrbIter.next();
                if(mnrb.getIsEnableBL()){
                    mnrb.setIsEnable("Y");
                }else{
                    mnrb.setIsEnable("N");
                }
                mnrb.setRunInterval(mnrb.getRunInterval()*60*60*1000);
                mnrb.setRuleExprDesc(mnrb.getRuleExpr());
                Map<String,String> lkv = lkvStore.get(mnrb.getRuleType());
                if(lkv == null){
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

    public List<DSGCMnNotices> findDSGCMnNotices(DSGCMnNotices dsgcMnNotices) {
        return this.mndao.findDSGCMnNotices(dsgcMnNotices);
    }


    public List<DSGCMnNotices> findDSGCMnNoticesByMnTitle(DSGCMnNotices dsgcMnNotices) {
        return this.mndao.findDSGCMnNoticesByMnTitle(dsgcMnNotices);
    }

    public List<Map<String, Object>> getServByUser(DSGCUser dsgcUser){
        return mndao.getServByUser(dsgcUser);
    }

    public void updateDSGCMnNoticesById(DSGCMnNotices dsgcMnNotices){
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
}
