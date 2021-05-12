package com.definesys.dsgc.service.ystar.token;

import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.ystar.token.bean.DSGCUserTokenBean;
import com.definesys.dsgc.service.ystar.token.bean.WorkWcTokenBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class UserTokenDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DSGCUserTokenBean createTokenByUserId(DSGCUserTokenBean userToken) {
        //先删除原有Token
        this.deleteTokenByUserId(userToken.getUserId());
        this.sw.buildQuery().doInsert(userToken);
        return userToken;
    }

    public DSGCUserTokenBean getUserTokenByUserId(String userId) {
        return sw.buildQuery().eq("userId", userId).doQueryFirst(DSGCUserTokenBean.class);
    }

    public List<DSGCUserTokenBean> getUserTokenListByUserId(String userId) {
        return sw.buildQuery().eq("userId", userId).doQuery(DSGCUserTokenBean.class);
    }

    public DSGCUserTokenBean getUserTokenByUserName(String userName, String userPwd) {
        return sw.buildQuery().eq("userName", userName).eq("userPwd", userPwd).doQueryFirst(DSGCUserTokenBean.class);
    }

    public DSGCUser findUserById(String userId) {
        return sw.buildQuery().eq("userId", userId).doQueryFirst(DSGCUser.class);
    }

    public void deleteTokenByUserId(String userId) {
        this.sw.buildQuery().eq("userId", userId).doDelete(DSGCUserTokenBean.class);
    }

    public void deleteInvalidToken() {
        this.sw.buildQuery().lt("offLineDate", new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000)).doDelete(DSGCUserTokenBean.class);
    }

    public DSGCUserTokenBean findUserTokenById(String userId, String tokenId) {
        return this.sw.buildQuery().eq("userId", userId).eq("tokenId", tokenId).doQueryFirst(DSGCUserTokenBean.class);
    }

    public boolean checkTokenValid(DSGCUserTokenBean tokenBean) {
        DSGCUserTokenBean token = this.sw.buildQuery().eq("userId", tokenBean.getUserId()).eq("tokenId", tokenBean.getTokenId()).gteq("offLineDate", new Date()).doQueryFirst(DSGCUserTokenBean.class);
        if (token == null) {
            return false;
        }
        return true;
    }

    public WorkWcTokenBean queryWorkWxToken(String corpId, String corpSecret) {
        System.out.println("corpId->" + corpId + "\ncorpSecret->" + corpSecret);
        Date date = new Date();
        return sw.buildQuery().eq("corpId", corpId)
                .eq("corpSecret", corpSecret)
                .lteq("startTime", date)
                .gteq("endTime", date)
                .doQueryFirst(WorkWcTokenBean.class);
    }

    public WorkWcTokenBean createWorkWxToken(WorkWcTokenBean tokenBean) {
        Date date = new Date();
        tokenBean.setStartTime(date);
        tokenBean.setEndTime(new Date(date.getTime() + 2 * 60 * 60 * 1000));
        sw.buildQuery().doInsert(tokenBean);
        return tokenBean;
    }

    public void deleteToken(WorkWcTokenBean tokenBean) {
        this.sw.buildQuery().eq("corpId", tokenBean.getCorpId()).eq("corpSecret", tokenBean.getCorpSecret())
                .doDelete(WorkWcTokenBean.class);
    }
}
