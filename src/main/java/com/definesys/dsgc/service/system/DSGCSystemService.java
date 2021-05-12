package com.definesys.dsgc.service.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.system.bean.*;
import com.definesys.dsgc.service.users.DSGCUserDao;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author zhenglong
 * @Description:
 * @Date 2019/3/12 14:27
 */
@Service
public class DSGCSystemService {

    @Value("${wldevserver.hostname}")
    private String devhostName;
    @Value("${wldevserver.portname}")
    private String devportName;
    @Value("${wldevserver.username}")
    private String devuserName;
    @Value("${wldevserver.password}")
    private String devpassword;
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private DSGCSystemDao systemDao;

    @Autowired
    private DSGCUserDao userDao;

    public List<DSGCSystem> findAll() {
        return this.systemDao.findAll();
    }

    public PageQueryResult<DSGCServRouting> query(DSGCSystem systemAndInterface, int pageSize, int pageIndex) {
        return this.systemDao.query(systemAndInterface, pageIndex, pageSize);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        this.systemDao.deleteDSGCSystemEntities(systemEntities);
    }

    public PageQueryResult<DSGCSystemEntities> query(DSGCSystemEntities systemEntities, int pageIndex, int pageSize) {
        return this.systemDao.query(systemEntities, pageIndex, pageSize);
    }


    public DSGCSystemEntities findDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        return this.systemDao.findDSGCSystemEntities(systemEntities);
    }

    public PageQueryResult<DSGCSystemItems> query(DSGCSystemItems t, int pageIndex, int pageSize) {
        return this.systemDao.query(t, pageIndex, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDSGCSystemItems(DSGCSystemItems t) {
        this.systemDao.addDSGCSystemItems(t);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDSGCSystemItems(DSGCSystemItems t) {
        this.systemDao.updateDSGCSystemItems(t);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDSGCSystemItems(DSGCSystemItems t) {
        this.systemDao.deleteDSGCSystemItems(t);
    }


    public DSGCSystemItems findDSGCSystemItems(DSGCSystemItems t) {
        return this.systemDao.findDSGCSystemItems(t);
    }

    public DSGCSystemEntities checkSysCode(DSGCSystemEntities t) {
        return this.systemDao.checkSysCode(t);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addDSGCSystemAccess(String body) {
        JSONObject jo = JSONObject.parseObject(body);
        String servNo = jo.getString("servNo");
        JSONArray js = jo.getJSONArray("users");
        if (js != null && js.size() > 0) {
            for (int i = 0; i < js.size(); i++) {
                JSONObject jsonObject = js.getJSONObject(i);
                DSGCSystemAccess access = new DSGCSystemAccess();
                access.setServNo(servNo);
                access.setSysCode(jsonObject.getString("sysCode"));
                this.systemDao.addDSGCSystemAccess(access);
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDSGCSystemAccess(DSGCSystemAccess access) {
        this.systemDao.deleteDSGCSystemAccess(access);
    }

    public List<DSGCSystemAccess> findDSGCSystemAccess(DSGCSystemAccess access) {
        return this.systemDao.findDSGCSystemAccess(access);
    }

    public List<DSGCSystemUser> findSystemUserByUserId(String userId) {
        return systemDao.findSystemUserByUserId(userId);
    }

    public List<DSGCSystemEntities> queryAllSystemList(DSGCSystemEntities systemEntities) {
        return this.systemDao.queryAllSystemList(systemEntities);
    }


    public List<QuerySystemParamBean> querySystemList(QuerySystemParamBean paramBean) {
        String userId = paramBean.getUserId();
        if (StringUtil.isNotBlank(userId)) {
            DSGCUser user = this.userDao.findUserById(userId);
            paramBean.setRole(user.getUserRole());
            return this.systemDao.querySystemList(paramBean);
        } else {
            return null;
        }
    }
}
