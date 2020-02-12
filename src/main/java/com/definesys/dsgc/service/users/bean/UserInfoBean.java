package com.definesys.dsgc.service.users.bean;
import java.util.HashSet;
import java.util.Set;

public class UserInfoBean {

    public static String V_USER_INFO = "SELECT US.USER_ID,\n" +
            "        US.USER_NAME,\n" +
            "        US.USER_DEPT,\n" +
            "        US.USER_MAIL,\n" +
            "        US.USER_PHONE,\n" +
            "        US.IS_LOCKED,\n" +
            "        US.WORK_NUMBER\n" +
            "   FROM DSGC_USER US\n" +
            "  WHERE US.USER_ID = #uid";
    public static String V_USER_ROLE  = "select us.USER_ROLE from dsgc_user us where us.user_id = #uid ";

    public static String V_USER_SYSTEM = "select su.SYS_CODE from dsgc_system_user su where su.user_id = #uid";

    public static String ROLE_SUPER_ADMINISTRATOR = "SuperAdministrators";

    public static String ROLE_ADMIN = "Administrators";

    public static String ROLE_SYSTEM_ADMIN = "SystemLeader";

    public static String ROLE_USER = "Tourist";

    public String uid;
    public String userName;
    public String userDept;
    public String userMail;
    public String userPhone;
    public String isLocked;
    public String workNumber;

    //拥有的角色 角色代码-角色名称
    public Set<String> roleSet =new HashSet<String>();
    //负责的系统 系统代码-系统名称
    public Set<String> systemSet = new HashSet<String>();

    public UserInfoBean(String uid){
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }
}
