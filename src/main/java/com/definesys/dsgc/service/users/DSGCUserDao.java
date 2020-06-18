package com.definesys.dsgc.service.users;

import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.users.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("user")
public class DSGCUserDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    @Value("${database.type}")
    private String dbType;

    public DSGCUser login(DSGCUser user) {
        DSGCUser u = sw.buildQuery()
                .eq("userName", user.getUserName())
                .eq("userPassword", user.getUserPassword())
//                .unSelect("userPassword")
                .doQueryFirst(DSGCUser.class);
        if (u == null) {
            throw new MpaasBusinessException("用户名错误或密码不存在");
        }
        return u;
    }

    public List<Map<String,Object>> findAppCodeByUser(String userName) {
        return sw.buildQuery().sql("select dsu.sys_code as sysCode from dsgc_system_user dsu,dsgc_user du where dsu.user_id = du.user_id and du.user_Name=#userName")
                .setVar("userName",userName).doQuery();
    }

    public String changePwd(DSGCUser user) {
//        logger.debug(user.toString());
//        DSGCUser user2 = this.findUserById(user);
//        user2.setUserPassword(user.getUserPassword());
        sw.buildQuery()
                .eq("userId", user.getUserId())
                .doUpdate(user);
        return user.getUserId();
    }


    public String createUser(DSGCUser user) {
        logger.debug(user.toString());
        sw.buildQuery().
                doInsert(user);
        return user.getUserId();
    }

    public String updateUser(DSGCUser user) {
        logger.debug(user.toString());
        sw.buildQuery()
                .eq("userId", user.getUserId())
                .doUpdate(user);
        return user.getUserId();
    }

    public String deleteUserById(DSGCUser user) {
        logger.debug(user.toString());
        sw.buildQuery()
                .eq("userId", user.getUserId())
                .doDelete(user);
        return user.getUserId();
    }

    public DSGCUser findUserByUserName(String userName) {
        return sw.buildQuery()
                .eq("userName", userName)
                .doQueryFirst(DSGCUser.class);
    }

    public PageQueryResult<DSGCUser> query(DSGCUser user, int pageSize, int pageIndex) {
        logger.debug(user.toString());
        if("oracle".equals(dbType)){
            return sw.buildViewQuery("query_all_user")
//                .sql("select * from (select us.user_id, us.user_name, us.user_dept, us.user_mail, us.user_phone, us.is_admin, us.user_description, us.created_by, us.creation_date, us.last_updated_by, us.last_update_date, us.user_role, us.is_locked, us.work_number, lookup.meaning role_name from dsgc_user us,( select vl.lookup_code, vl.meaning from fnd_lookup_values vl where vl.lookup_id in ( select tp.lookup_id from fnd_lookup_types tp where tp.lookup_type = 'plateFormRole') ) lookup where us.user_role = lookup.lookup_code(+) )")
                    .or()
                    .likeNocase("user_name", user.getUserDescription())
                    .likeNocase("user_mail", user.getUserDescription())
                    .likeNocase("user_dept", user.getUserDescription())
                    .likeNocase("user_phone", user.getUserDescription())
                    .likeNocase("role_name", user.getUserDescription())
                    .likeNocase("user_description", user.getUserDescription())
                    .doPageQuery(pageIndex, pageSize, DSGCUser.class);
        }
        if("mysql".equals(dbType)){
            return sw.buildViewQuery("mysql_query_all_user")
                    .or()
                    .likeNocase("user_name", user.getUserDescription())
                    .likeNocase("user_mail", user.getUserDescription())
                    .likeNocase("user_dept", user.getUserDescription())
                    .likeNocase("user_phone", user.getUserDescription())
                    .likeNocase("role_name", user.getUserDescription())
                    .likeNocase("user_description", user.getUserDescription())
                    .doPageQuery(pageIndex, pageSize, DSGCUser.class);

        }
        return null;
    }

    public DSGCUser findUserById(DSGCUser user) {
        logger.debug(user.toString());
        return sw.buildQuery().eq("userId", user.getUserId()).doQueryFirst(DSGCUser.class);
    }

    public DSGCUser findUserById(String userid) {

        return sw.buildQuery().eq("userId", userid).doQueryFirst(DSGCUser.class);
    }

    public List<DSGCUser> findAll() {
        return sw.buildQuery()
                .unSelect("userPassword")
                .doQuery(DSGCUser.class);
    }

    public DSGCUser findUserByUserId(String userId) {
        return sw.buildQuery()
                .eq("user_id", userId)
                .doQueryFirst(DSGCUser.class);
    }

    public DSGCUser findByUserName(DSGCUser user) {
        return sw.buildQuery()
                .eq("user_name", user.getUserName())
                .doQueryFirst(DSGCUser.class);
    }

    public DSGCUser checkUserName(String uid, String account) {
        return sw.buildQuery()
                .eq("user_id", uid)
                .eq("user_name", account)
                .doQueryFirst(DSGCUser.class);
    }

    /**
     * 新建或修改用户时，检查用户是否已经存在
     *
     * @param user
     * @return
     */
    public boolean checkUserNameIsExistByName(DSGCUser user) {
        MpaasQuery mq = sw.buildQuery()
                .eq("user_name", user.getUserName());

        //修改时，userId存在
        if (StringUtil.isNotBlank(user.getUserId())) {
            mq.ne("user_id", user.getUserId());
        }
        List<DSGCUser> list = mq.doQuery(DSGCUser.class);
        return list.size() > 0;
    }

    /**
     * 系统管理--添加系统负责人选人接口
     * 此查询过滤了普通用户，以及已经被授权管理xx系统的用户
     *
     * @return
     */
    public List<DSGCUser> queryAllSysUser() {
        return sw.buildQuery()
                .doQuery(DSGCUser.class);
    }

    /**
     * 口令插入
     *
     * @param dsgcUserKs
     */
    public boolean insertPassword(DSGCUserKs dsgcUserKs) {
        List<DSGCUserKs> list = this.sw
                .buildQuery()
                .eq("userId", dsgcUserKs.getUserId())
                .eq("envCode", dsgcUserKs.getEnvCode())
                .eq("ksName", dsgcUserKs.getKsName())
                .doQuery(DSGCUserKs.class);
        if (list.size() == 0) {
            this.logger.debug(" dsgcUserKs : " + dsgcUserKs);
            this.sw
                    .buildQuery()
                    .doInsert(dsgcUserKs);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 口令查询
     *
     * @param userId
     * @return
     */
    public List<DSGCUserKs> getKsPassword(String userId) {
        this.logger.debug(" userId : " + userId);
        return sw.buildQuery()
                .sql("select ks.KS_ID,ks.KS_NAME,ks.KS_PASSWD,ks.ENV_CODE,ks.CREATION_DATE,cfg.ENV_NAME from DSGC_USER_KS ks left join DSGC_ENV_INFO_CFG cfg on ks.ENV_CODE=cfg.ENV_CODE")
                .eq("userId", userId)
                .doQuery(DSGCUserKs.class);
    }

    /**
     * 口令更新
     * @param dsgcUserKs
     */
    public boolean updateKsPassword(DSGCUserKs dsgcUserKs) {
        List<DSGCUserKs> list = this.sw
                .buildQuery()
                .eq("userId", dsgcUserKs.getUserId())
                .eq("envCode", dsgcUserKs.getEnvCode())
                .eq("ksName", dsgcUserKs.getKsName())
                .eq("ksPasswd",dsgcUserKs.getKsPasswd())
                .doQuery(DSGCUserKs.class);
        if (list.size() == 0){
            this.logger.debug(" dsgcUserKs : " + dsgcUserKs);
            this.sw
                    .buildQuery()
                    .update("env_code",dsgcUserKs.getEnvCode())
                    .update("ks_name",dsgcUserKs.getKsName())
                    .update("ks_passwd",dsgcUserKs.getKsPasswd())
                    .eq("ksId", dsgcUserKs.getKsId())
                    .doUpdate(dsgcUserKs);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 口令删除
     * @param dsgcUserKs
     */
    public void deleteKsPassword(DSGCUserKs dsgcUserKs) {
        this.logger.debug("dsgcUserKs : " + dsgcUserKs);
        this.sw
                .buildQuery()
                .eq("userId", dsgcUserKs.getUserId())
                .eq("envCode", dsgcUserKs.getEnvCode())
                .eq("ksName", dsgcUserKs.getKsName())
                .doDelete(dsgcUserKs);
    }


    /**
     * 获取环境
     * @return
     */
    public List<Map<String, Object>> getEnvCode(){
        return sw
                .buildQuery()
                .sql("select env_code,env_name from dsgc_env_info_cfg")
                .doQuery();
    }

    /**
     * 根据角色获取用户列表
     */
    public List<DSGCUser> getUserListByRole(String role){
        return sw.buildQuery()
                .eq("user_role",role)
                .doQuery(DSGCUser.class);
    }

    public List<DSGCServiceUser> findServiceUserByUserId(String userId) {
        return this.sw.buildQuery()
                .eq("userId", userId)
                .doQuery(DSGCServiceUser.class);
    }

    /**
     * 用户管理--授权服务权限给用户--服务模糊查询不区分大小写
     *
     * @param service
     * @return
     */
    public PageQueryResult<DSGCService> findAllService(DSGCServiceQueryVO service) {
        StringBuffer strSql;
        if (!"".equals(service.getTag())) {
            strSql = new StringBuffer("select serv.* from dsgc_services serv,dsgc_tab tab,dsgc_tab_serv tabServ where 1 = 1 ");

        } else {
            strSql = new StringBuffer("select serv.* from dsgc_services serv where 1 = 1 ");
        }
        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(service.getServNo())) {
            strSql.append(" and upper(serv.serv_no) like #servNo ");
        }
        if (StringUtil.isNotBlank(service.getServName())) {
            strSql.append(" and upper(serv.serv_name) like #servName ");
        }
        if (StringUtil.isNotBlank(service.getServDesc())) {
            strSql.append(" and upper(serv.serv_desc) like #servDesc ");
        }
        if (StringUtil.isNotBlank(service.getSubordinateSystem())) {
            strSql.append(" and upper(serv.subordinate_system) like #subordinateSystem ");
        }
        if (StringUtil.isNotBlank(service.getServStatus())) {
            strSql.append(" and serv.serv_status like #servStatus ");
        }
        if (StringUtil.isNotBlank(service.getTag())) {
            strSql.append(" and tab.id = tabServ.tab_id and tab.name like #tag and tabServ.serv_no = serv.serv_no");
        }
        mq.sql(strSql.toString());
        if (StringUtil.isNotBlank(service.getServNo())) {
            mq.setVar("servNo", "%" + service.getServNo().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(service.getServName())) {
            mq.setVar("servName", "%" + service.getServName().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(service.getServDesc())) {
            mq.setVar("servDesc", "%" + service.getServDesc().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(service.getSubordinateSystem())) {
            mq.setVar("subordinateSystem", "%" + service.getSubordinateSystem().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(service.getServStatus())) {
            mq.setVar("servStatus", "%" + service.getServStatus().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(service.getTag())) {
            mq.setVar("tag", "%" + service.getTag().toUpperCase().trim() + "%");
        }
        return mq.doPageQuery(service.getPageIndex(), service.getPageSize(), DSGCService.class);
    }

    public List<Map<String, Object>> queryTab(String servNo) {
        String sql = "select tab.id,tab.name,tab.color,ts.serv_no from dsgc_tab tab, dsgc_tab_serv ts where tab.id(+) = ts.tab_id and ts.serv_no = '#servNo'";
        sql = sql.replace("#servNo", servNo);
        return this.sw.buildQuery()
                .sql(sql)
                .doQuery();
    }

    public DSGCUserNtyCfgBean modifyUserNtyCfg(DSGCUserNtyCfgBean userNtyCfg) {
        String userId = userNtyCfg.getUserId();
        //先查询当前用户是否存在配置信息
        DSGCUserNtyCfgBean userNtyCfgBean =this.sw.buildQuery().eq("userId",userId).doQueryFirst(DSGCUserNtyCfgBean.class);
        if(userNtyCfgBean != null ){
            this.sw.buildQuery().eq("userId",userId).doDelete(DSGCUserNtyCfgBean.class);
        }
        this.sw.buildQuery().doInsert(userNtyCfg);
        return userNtyCfg;
    }
}
