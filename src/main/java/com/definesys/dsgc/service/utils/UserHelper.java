package com.definesys.dsgc.service.utils;

import com.definesys.dsgc.service.users.bean.UserInfoBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class UserHelper {
    private UserInfoBean uib = null;

    @Autowired
    private MpaasQueryFactory sw ;

    public UserHelper(String uid) {

    }

    public UserHelper(){

    }

    public UserHelper(MpaasQueryFactory sw){
        this.sw = sw;
    }

    public UserHelper user(String uid) {
        UserHelper uh = new UserHelper(this.sw);
        uh.uib = new UserInfoBean(uid);
        uh.loadUserBasicInfo();
        uh.loadUserRoleInfo();
        uh.loadUserSystemInfo();
        return uh;
    }


    public boolean isSuperAdministrator() {
        if (this.uib.roleSet.contains(UserInfoBean.ROLE_SUPER_ADMINISTRATOR)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAdmin() {
        if (this.uib.roleSet.contains(UserInfoBean.ROLE_ADMIN)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSystemMaintainer() {
        if (this.uib.roleSet.contains(UserInfoBean.ROLE_SYSTEM_ADMIN)) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isSpecifySystemMaintainer(String systemCode) {
        if (this.uib.systemSet.contains(systemCode)) {
            return true;
        } else {
            return false;
        }
    }


    private void loadUserBasicInfo() {
        Map<String,Object> res = sw.buildQuery().sql(UserInfoBean.V_USER_INFO).setVar("uid",this.uib.uid).doQueryFirst();
        if (res != null) {
            this.uib.userName = (String)res.get("USER_NAME");
            this.uib.isLocked = (String)res.get("IS_LOCKED");
            this.uib.userDept = (String)res.get("USER_DEPT");
            this.uib.userMail = (String)res.get("USER_MAIL");
            this.uib.isLocked = (String)res.get("IS_LOCKED");
            this.uib.workNumber = (String)res.get("WORK_NUMBER");

        }
    }

    private void loadUserRoleInfo() {
        List<Map<String,Object>> res = sw.buildQuery().sql(UserInfoBean.V_USER_ROLE).setVar("uid",this.uib.uid).doQuery();
        if (res != null) {
            Iterator<Map<String,Object>> rowIters = res.iterator();
            while (rowIters.hasNext()) {
                Map<String,Object> row = rowIters.next();
                this.uib.roleSet.add((String)row.get("USER_ROLE"));
            }
        }
    }

    private void loadUserSystemInfo() {
        List<Map<String,Object>> res = sw.buildQuery().sql(UserInfoBean.V_USER_SYSTEM).setVar("uid",this.uib.uid).doQuery();
        if (res != null) {
            Iterator<Map<String,Object>> rowIters = res.iterator();
            while (rowIters.hasNext()) {
                Map<String,Object> row = rowIters.next();
                this.uib.systemSet.add((String)row.get("SYS_CODE"));
            }
        }
    }

    /**
     * 判断当前用户对服务的快速编辑是否有权限
     *
     * @param servNo
     * @return
     */
    public boolean isSvcGenEditorByServNo(String servNo) {
        boolean isCanToEdit = false;
        //获取服务所属系统，判断当前用户是否有权限修改，需要服务所属系统进行判断；
        boolean isAdmin = (this.isAdmin() || this.isSuperAdministrator());
        if (isAdmin) {
            //如果是管理员，则可以编辑
            isCanToEdit = true;
        } else {
            //如果是系统负责人
            if (this.isSystemMaintainer()) {
                String sysCode = this.getServSystemCode(servNo);
                if (this.isSpecifySystemMaintainer(sysCode)) {
                    //如果是系统的负责人，且角色是系统负责人角色，则可以编辑
                    isCanToEdit = true;
                }
            }
        }
        return isCanToEdit;
    }


    /**
     * 获取接口所属系统
     * @return
     */
    private String getServSystemCode(String servNo){
        String sysCode = null;
        Map<String,Object> servInfoRes = this.sw.buildQuery().sql("select t.subordinate_system SYS_CODE from dsgc_services t where t.serv_no = #servNo").setVar("servNo",servNo).doQueryFirst();
        if (servInfoRes != null) {
            sysCode = (String)servInfoRes.get("SYS_CODE");
        }
        return sysCode;
    }




}
