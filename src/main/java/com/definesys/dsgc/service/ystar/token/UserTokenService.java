package com.definesys.dsgc.service.ystar.token;

import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.token.bean.DSGCUserTokenBean;
import com.definesys.dsgc.service.ystar.token.bean.WorkWcTokenBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenDao userTokenDao;

    public DSGCUserTokenBean getUserToken(DSGCUser user) {
        String userId = user.getUserId();
        String userName = user.getUserName();
        String userPwd = user.getUserPassword();
        if (StringUtil.isNotBlank(user.getUserId())) {
            return this.userTokenDao.getUserTokenByUserId(userId);
        } else if (StringUtil.isNotBlank(userName) && StringUtil.isNotBlank(userPwd)) {
            return this.userTokenDao.getUserTokenByUserName(userName, userPwd);
        }
        return new DSGCUserTokenBean();
    }

    @Transactional(rollbackFor = Exception.class)
    public DSGCUserTokenBean createTokenByUserId(DSGCUser user) {
        String userId = user.getUserId();
        //先判断用户是否存在
        DSGCUser dsgcUser = userTokenDao.findUserById(userId);
        if (dsgcUser != null) {
            DSGCUserTokenBean userTokenBean = new DSGCUserTokenBean(userId);
            return this.userTokenDao.createTokenByUserId(userTokenBean);
        }
        return new DSGCUserTokenBean();
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteInvalidToken(DSGCUser user) {
        String userId = user.getUserId();
        String userRole = user.getUserRole();
        //先判断用户是否存在
        DSGCUser dsgcUser = userTokenDao.findUserById(userId);
        if (dsgcUser != null && "SuperAdministrators".equals(userRole)) {
            this.userTokenDao.deleteInvalidToken();
            return userId;
        }
        return null;
    }

    public boolean checkTokenValid(DSGCUserTokenBean tokenBean) {
        String userId = tokenBean.getUserId();
        String tokenId = tokenBean.getTokenId();
        DSGCUserTokenBean token = this.userTokenDao.findUserTokenById(userId, tokenId);
        if (token != null) {
            return this.userTokenDao.checkTokenValid(tokenBean);
        }
        return false;
    }


    public String queryWorkWxToken(WorkWcTokenBean tokenBean) {
        String corpId = tokenBean.getCorpId();
        String corpSecret = tokenBean.getCorpSecret();
        if (StringUtil.isNotBlank(corpId) && StringUtil.isNotBlank(corpSecret)) {
            WorkWcTokenBean token = this.userTokenDao.queryWorkWxToken(corpId, corpSecret);
            if (token != null) {
                return token.getToken();
            }
        }
        return null;
    }

    @Transactional
    public WorkWcTokenBean createWorkWxToken(WorkWcTokenBean tokenBean) {
        String corpId = tokenBean.getCorpId();
        String corpSecret = tokenBean.getCorpSecret();
        String token = tokenBean.getToken();
        if (StringUtil.isNotBlank(corpId) && StringUtil.isNotBlank(corpSecret) && StringUtil.isNotBlank(token)) {
            this.userTokenDao.deleteToken(tokenBean);
            return this.userTokenDao.createWorkWxToken(tokenBean);
        }
        return null;
    }
}
