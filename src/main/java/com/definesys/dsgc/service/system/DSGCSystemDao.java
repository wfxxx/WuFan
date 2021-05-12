package com.definesys.dsgc.service.system;

import com.definesys.dsgc.service.system.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.utils.YStarUtil;
import com.definesys.dsgc.service.ystar.utils.ZJUtil;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhenglong
 * @Description:
 * @Date 2019/3/12 14:33
 */
@Repository("system")
public class DSGCSystemDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    @Value("${database.type}")
    private String dbType;

    /**
     * 查询所有
     */

    public List<DSGCSystem> findAll() {
        return sw.buildQuery().doQuery(DSGCSystem.class);
    }

    /**
     * 查询系统与接口对应的关系
     *
     * @param systemAndInterface
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public PageQueryResult<DSGCServRouting> query(DSGCSystem systemAndInterface, int pageIndex, int pageSize) {
        logger.debug(systemAndInterface.toString());
        PageQueryResult<DSGCServRouting> routeSystemCode = sw.buildQuery()
                .like("routeSystemCode", systemAndInterface.getSystemCode())
                .doPageQuery(DSGCServRouting.class);
        return routeSystemCode;
    }

    /**
     * 系统查询--模糊查询参数整合成一个，统一使用sysCode传值
     * 查询不区分大小写
     *
     * @param systemEntities
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageQueryResult<DSGCSystemEntities> query(DSGCSystemEntities systemEntities, int pageIndex, int pageSize) {
        StringBuffer strSql = null;
        if ("oracle".equals(dbType)) {
            strSql = new StringBuffer("select * from( select ent.id,ent.sys_code, ent.sys_name, ent.pwd, t.user_name owner from DSGC_SYSTEM_ENTITIES ent, (select listagg(u.user_name, ',') within GROUP(order by su.sys_code) user_name, su.sys_code from DSGC_SYSTEM_user su, dsgc_user u where su.user_id = u.user_id group by su.sys_code) t where t.sys_code(+) = ent.sys_code)  WHERE 1 = 1 ");

        }
        if ("mysql".equals(dbType)) {
            strSql = new StringBuffer("SELECT *  FROM ( SELECT ent.id,ent.sys_code,ent.sys_name,ent.pwd,t.user_name OWNER FROM DSGC_SYSTEM_ENTITIES ent RIGHT JOIN\n" +
                    "(SELECT group_concat( u.user_name order by su.sys_code, ',' )user_name,su.sys_code FROM DSGC_SYSTEM_user su,dsgc_user u WHERE\n" +
                    "su.user_id = u.user_id GROUP BY su.sys_code ) t on t.sys_code  = ent.sys_code ) s WHERE 1 = 1 ");
        }

        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(systemEntities.getSysCode())) {
            strSql.append(" and upper(sys_code) like #sysCode or upper(sys_name) like #sysCode ");
        }
        mq.sql(strSql.toString());
        if (StringUtil.isNotBlank(systemEntities.getSysCode())) {
            mq.setVar("sysCode", "%" + systemEntities.getSysCode().toUpperCase().trim() + "%");
        }
        return mq.doPageQuery(pageIndex, pageSize, DSGCSystemEntities.class);
    }

    public void addDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        this.sw.buildQuery()
                .eq("sysCode", systemEntities.getSysCode())
                .doDelete(DSGCSystemUser.class);
        List<DSGCSystemUser> users = systemEntities.getDsgcSystemUserList();
        if (users != null && users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                DSGCSystemUser user = users.get(i);
                this.sw.buildQuery()
                        .doInsert(user);
            }
        }

        this.sw.buildQuery()
                .doInsert(systemEntities);
    }

    public void updateDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        this.sw.buildQuery()
                .eq("sysCode", systemEntities.getSysCode())
                .doDelete(DSGCSystemUser.class);
        List<DSGCSystemUser> users = systemEntities.getDsgcSystemUserList();
        if (users != null && users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                DSGCSystemUser user = users.get(i);
                this.sw.buildQuery()
                        .doInsert(user);
            }
        }
        this.sw.buildQuery()
                .eq("id", systemEntities.getId())
                .doUpdate(systemEntities);
    }

    public void deleteDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        this.sw.buildQuery()
                .eq("id", systemEntities.getId())
                .doDelete(DSGCSystemEntities.class);
    }


    public DSGCSystemEntities findDSGCSystemEntities(DSGCSystemEntities systemEntities) {
        systemEntities = this.sw.buildQuery()
                .eq("id", systemEntities.getId())
                .doQueryFirst(DSGCSystemEntities.class);
        List<DSGCSystemUser> users = this.sw.buildQuery()
                .eq("sysCode", systemEntities.getSysCode())
                .doQuery(DSGCSystemUser.class);
        systemEntities.setDsgcSystemUserList(users);
        return systemEntities;
    }


    public PageQueryResult<DSGCSystemItems> query(DSGCSystemItems t, int pageIndex, int pageSize) {
        return this.sw
                .buildQuery()
                .eq("sysCode", t.getSysCode())
                .like("objType", t.getObjType())
                .doPageQuery(pageIndex, pageSize, DSGCSystemItems.class);
    }

    public void addDSGCSystemItems(DSGCSystemItems t) {
        this.sw.buildQuery()
                .doInsert(t);
    }

    public void updateDSGCSystemItems(DSGCSystemItems t) {
        this.sw.buildQuery()
                .eq("id", t.getId())
                .doUpdate(t);
    }

    public void deleteDSGCSystemItems(DSGCSystemItems t) {
        this.sw.buildQuery()
                .eq("id", t.getId())
                .doDelete(DSGCSystemItems.class);
    }


    public DSGCSystemItems findDSGCSystemItems(DSGCSystemItems t) {
        return this.sw.buildQuery()
                .eq("id", t.getId())
                .doQueryFirst(DSGCSystemItems.class);
    }

    public DSGCSystemEntities checkSysCode(DSGCSystemEntities systemEntities) {
        return this.sw.buildQuery()
                .eq("sysCode", systemEntities.getSysCode())
                .doQueryFirst(DSGCSystemEntities.class);
    }

    public void addDSGCSystemAccess(DSGCSystemAccess access) {
        this.sw.buildQuery()
                .doInsert(access);
    }

    public void deleteDSGCSystemAccess(DSGCSystemAccess access) {
        this.sw.buildQuery()
                .eq("saId", access.getSaId())
                .doDelete(access);
    }

    public List<DSGCSystemAccess> findDSGCSystemAccess(DSGCSystemAccess access) {
        if (access.getServNo() == null) {
            List<DSGCSystemAccess> list = new ArrayList<DSGCSystemAccess>();
            return list;
        }
        return this.sw.buildQuery()
                .eq("servNo", access.getServNo())
                .doQuery(DSGCSystemAccess.class);
    }

    public List<DSGCSystemUser> findSystemUserByUserId(String userId) {
        return sw.buildQuery()
                .eq("user_id", userId)
                .doQuery(DSGCSystemUser.class);
    }


    /*** 查询系统list**/
    public List<DSGCSystemEntities> queryAllSystemList(DSGCSystemEntities systemEntities) {
        String flag = systemEntities.getAttribue1();
        flag = StringUtil.isBlank(flag) ? null : flag.toUpperCase();
        String sysCode = systemEntities.getSysCode();
        sysCode = (StringUtil.isNotBlank(sysCode) && !"ALL".equals(sysCode.toUpperCase())) ? sysCode : null;
        String sysName = systemEntities.getSysName();
        MpaasQuery mpaasQuery = null;
        //若为空，或者是传入"ALL"标识，查所有系统信息
        if (StringUtil.isBlank(flag) || "ALL".equals(flag)) {
            mpaasQuery = this.sw.buildQuery();
        } else if ("SVC".equals(flag)) {
            mpaasQuery = this.sw.buildViewQuery("QUERY_SVC_SYSTEM_VIEW");
        }
        if (StringUtil.isNotBlank(sysCode) && sysCode.contains(",")) {
            String[] sysList = sysCode.split(",");
            //System.out.println("sysList->" + sysList + " --->" + ZJUtil.getListByStringArr(sysList));
            mpaasQuery = mpaasQuery.in("SYS_CODE", ZJUtil.getListByStringArr(sysList));
        } else {
            mpaasQuery = mpaasQuery.eq("SYS_CODE", sysCode);
        }
        mpaasQuery = mpaasQuery.like("SYS_NAME", sysName);
        return mpaasQuery.doQuery(DSGCSystemEntities.class);
    }

    public DSGCSystemEntities getSysNameByCode(String sysCode) {
        return this.sw.buildQuery()
                .eq("sysCode", sysCode)
                .doQueryFirst(DSGCSystemEntities.class);
    }

    /***********************start*******************************/
    /*** 获取所有系统数量 ** @return*/
    public Object getAllSysCount() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select count(*) from dsgc_sysTEM_ENTITIES")
                .doQuery();
        return result.get(0).get("COUNT(*)");
    }

    public List<QuerySystemParamBean> querySystemList(QuerySystemParamBean paramBean) {
        String querySql = " select s.sys_code,s.sys_name from dsgc_system_entities s where 1=1 ";
        //根据传入系统编码信息筛选
        String sysCode = paramBean.getSysCode();
        if (StringUtils.isEmpty(sysCode)) {
            return null;
        } else if (!"ALL".equals(sysCode.toUpperCase())) {
            querySql += " and sys_code in (" + YStarUtil.getSqlWhereClause(sysCode) + ")";
        }
        //根据 系统状态筛选（1已注册并启用，0未注册或已注册未启用）
        String status = paramBean.getStatus();
        if (StringUtils.isEmpty(status)) {
            return null;
        } else if (!"ALL".equals(status.toUpperCase())) {
            querySql += " and sys_code in (select sv.subordinate_system from dsgc_services sv where sv.serv_status = '" + status + "') ";
        }
        //根据用户ID和角色筛选
        String userId = paramBean.getUserId();
        String role = paramBean.getRole();
        String sql1 = " and s.sys_code in (select u.sys_code from dsgc_system_user u where u.user_id in (#userId ))";
        if (StringUtils.isNotEmpty(userId) && (StringUtils.isEmpty(role) || "SystemLeader".equals(role))) {
            sql1 = sql1.replace("#userId", "'" + userId + "'");
            querySql += sql1;
        } else if (StringUtils.isNotEmpty(role) && ("SystemLeader".equals(role) || "Tourist".equals(role))) {
            sql1 = sql1.replace("#userId", "''");
            querySql += sql1;
        }
        //新增排序
        querySql += " order by s.sys_code asc ";

        return this.sw.buildQuery()
                .sql(querySql)
                .doQuery(QuerySystemParamBean.class);
    }


    public List<DSGCSystemUser> querySystemListUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id", uid)
                .doQuery(DSGCSystemUser.class);
    }
}
