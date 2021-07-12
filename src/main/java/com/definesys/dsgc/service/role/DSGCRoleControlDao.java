package com.definesys.dsgc.service.role;

import com.definesys.dsgc.service.role.bean.DSGCRoleControl;
import com.definesys.dsgc.service.role.bean.DSGCRoleControlVO;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("roleControl")
public class DSGCRoleControlDao {
    @Value("${database.type}")
    private String dbType;
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    public void addRoleControl(DSGCRoleControl roleControl) {
        this.sw.buildQuery()
                .doInsert(roleControl);
    }

    public PageQueryResult<DSGCRoleControlVO> queryRoleControl2(DSGCRoleControl roleControl, int pageSize, int pageIndex) {
        StringBuffer sb = new StringBuffer("select * from (select c.role_id,c.role_code as role_code,");
        if ("mysql".equals(dbType.toLowerCase())) {
            sb.append("(case when u.user_name is null then v2.meaning else CONCAT(u.user_description,'-',u.user_name) end) as role_Name,");
        } else if ("oracle".equals(dbType.toLowerCase())) {
            sb.append(" (case when u.user_name is null then v2.meaning else (u.user_description||'-'||u.user_name) end) as role_Name,");
        }
        sb.append(" v1.meaning as menu_Bar_Name,c.is_see,c.is_edit,c.creation_date,c.menu_bar from dsgc_role_control c\n" +
                "  left join dsgc_user u on c.role_code=u.user_id left join fnd_lookup_values v1 on v1.lookup_code=c.menu_bar \n" +
                "  left join fnd_lookup_values v2 on v2.lookup_code=c.role_code ) m ");

        return sw.buildQuery().sql(sb.toString())
                .or()
                .likeNocase("roleName", roleControl.getRoleControlDescription())
                .likeNocase("menuBarName", roleControl.getRoleControlDescription())
                .orderBy("creationDate", "desc")
                .doPageQuery(pageIndex, pageSize, DSGCRoleControlVO.class);
    }

    public List<Map<String, Object>> checkRoleControl(DSGCRoleControl roleControl) {

        return sw.buildQuery()
                .sql("select count(*) as num from dsgc_role_control where ROLE_CODE = #roleCode and MENU_BAR = #menuBar")
                .setVar("roleCode", roleControl.getRoleCode())
                .setVar("menuBar", roleControl.getMenuBar())
                .doQuery();

    }


    public String updateRoleControl(DSGCRoleControl roleControl) {
        this.sw.buildQuery()
                .eq("roleId", roleControl.getRoleId())
                .doUpdate(roleControl);
        return roleControl.getRoleId();
    }

    public String removeRoleControl(DSGCRoleControl roleControl) {
        logger.debug(roleControl.toString());
        sw.buildQuery()
                .eq("roleId", roleControl.getRoleId())
                .doDelete(roleControl);
        return roleControl.getRoleId();
    }

    public List<DSGCRoleControl> queryRoleJurisdiction(DSGCRoleControl roleControl) {
        logger.debug(roleControl.toString());
        return sw.buildQuery()
                .or()
                .eq("roleCode", roleControl.getRoleCode())
                .eq("roleCode", roleControl.getAttribue5())
                .doQuery(DSGCRoleControl.class);
    }

    public DSGCRoleControl findRoleControlById(String roleId) {
        return sw.buildQuery()
                .eq("role_id", roleId)
                .doQueryFirst(DSGCRoleControl.class);
    }

    /**
     * 根据角色代码与菜单编码获取角色菜单权限信息
     *
     * @param userRole
     * @param menuCode
     * @return
     */
    public DSGCRoleControl findRoleControlByRoleCodeAndMenuCode(String userRole, String menuCode) {
        return sw.buildQuery()
                .select("role_code,menu_bar,is_see,is_edit")
                .eq("role_code", userRole)
                .eq("menu_bar", menuCode)
                .doQueryFirst(DSGCRoleControl.class);
    }
}
