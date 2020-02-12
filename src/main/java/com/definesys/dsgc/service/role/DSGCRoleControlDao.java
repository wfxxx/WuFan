package com.definesys.dsgc.service.role;

import com.definesys.dsgc.service.role.bean.DSGCRoleControl;
import com.definesys.dsgc.service.role.bean.DSGCRoleControlVO;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("roleControl")
public class DSGCRoleControlDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;
    public void addRoleControl(DSGCRoleControl roleControl) {
        this.sw.buildQuery()
                .doInsert(roleControl);
    }
    public PageQueryResult<DSGCRoleControlVO> queryRoleControl(DSGCRoleControl roleControl, int pageSize, int pageIndex) {
        logger.debug(roleControl.toString());
        return sw.buildViewQuery("role_view")
                .or()
                .likeNocase("roleName", roleControl.getRoleControlDescription())
                .likeNocase("menuBarName", roleControl.getRoleControlDescription())
                .doPageQuery(pageIndex, pageSize, DSGCRoleControlVO.class);

//        StringBuffer strSql = new StringBuffer("select * from dsgc_role_control  where 1 = 1 ");
//
//        MpaasQuery mq = sw.buildQuery();
//        if(StringUtil.isNotBlank(roleControl.getRoleControlDescription())){
//            strSql.append(" and upper(role_code) like #roleDesc or upper(menu_bar) like #roleDesc  or upper(is_see) like #roleDesc  or upper(is_edit) like #roleDesc  or upper(role_control_description) like #roleDesc");
//        }
//        mq.sql(strSql.toString());
//        if(StringUtil.isNotBlank(roleControl.getRoleControlDescription())){
//            mq.setVar("roleDesc","%"+roleControl.getRoleControlDescription().toUpperCase().trim()+"%");
//        }
//        return mq.doPageQuery(pageIndex,pageSize,DSGCRoleControl.class);
    }
    public List<Map<String,Object>>  checkRoleControl(DSGCRoleControl roleControl) {

        return sw.buildQuery()
                .sql("select count(*) as num from dsgc_role_control where ROLE_CODE = #roleCode and MENU_BAR = #menuBar")
                 .setVar("roleCode",roleControl.getRoleCode())
                 .setVar("menuBar",roleControl.getMenuBar())
                 .doQuery();

    }


    public String updateRoleControl(DSGCRoleControl roleControl) {
        this.sw.buildQuery()
                .eq("roleId",roleControl.getRoleId())
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
                .eq("roleCode",roleControl.getRoleCode())
                .doQuery(DSGCRoleControl.class);
    }

    public DSGCRoleControl findRoleControlById(String roleId) {
        return sw.buildQuery()
                .eq("role_id",roleId)
                .doQueryFirst(DSGCRoleControl.class);
    }

    /**
     * 根据角色代码与菜单编码获取角色菜单权限信息
     * @param userRole
     * @param menuCode
     * @return
     */
    public DSGCRoleControl findRoleControlByRoleCodeAndMenuCode(String userRole, String menuCode) {
        return sw.buildQuery()
                .select("role_code,menu_bar,is_see,is_edit")
                .eq("role_code",userRole)
                .eq("menu_bar",menuCode)
                .doQueryFirst(DSGCRoleControl.class);
    }
}
