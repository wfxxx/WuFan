package com.definesys.dsgc.service.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.system.bean.DSGCSystem;
import com.definesys.dsgc.service.system.bean.DSGCServRouting;
import com.definesys.dsgc.service.system.bean.DSGCSystemAccess;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemItems;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.wls.DefaultAuthenticatorAdapter;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public List<DSGCSystem> findAll() {
        return this.systemDao.findAll();
    }

    public PageQueryResult<DSGCServRouting> query(DSGCSystem systemAndInterface, int pageSize, int pageIndex) {
        return this.systemDao.query(systemAndInterface, pageIndex, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        DefaultAuthenticatorAdapter c = new DefaultAuthenticatorAdapter();
        try {
            c.connection(devhostName, devportName, devuserName, devpassword);
            c.createUser(systemEntities.getSysCode(), systemEntities.getPwd(), "接口可访问权限用户" + format2.format(new Date()));
            this.systemDao.addDSGCSystemEntities(systemEntities);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (ReflectionException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (MBeanException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败，密码至少包含八个以上字符！");
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        DSGCSystemEntities t = this.systemDao.findDSGCSystemEntities(systemEntities);
        DefaultAuthenticatorAdapter c = new DefaultAuthenticatorAdapter();
        try {
            c.connection(devhostName, devportName, devuserName, devpassword);
            c.changeUserPassword(systemEntities.getSysCode(), t.getPwd(),systemEntities.getPwd());
            this.systemDao.updateDSGCSystemEntities(systemEntities);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (ReflectionException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败！");
        } catch (MBeanException e) {
            e.printStackTrace();
            throw new MpaasBusinessException("保存失败，密码至少包含八个以上字符！");
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

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
    public void addDSGCSystemAccess(String  body){
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
    public void deleteDSGCSystemAccess(DSGCSystemAccess access){
        this.systemDao.deleteDSGCSystemAccess(access);
    }

    public List<DSGCSystemAccess> findDSGCSystemAccess(DSGCSystemAccess access){
        return  this.systemDao.findDSGCSystemAccess(access);
    }

    public List<DSGCSystemUser> findSystemUserByUserId(String userId){
        return systemDao.findSystemUserByUserId(userId);
    }
}
