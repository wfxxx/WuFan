package com.definesys.dsgc.service.apps;

import com.definesys.dsgc.service.apps.bean.CommonReqBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppsDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<DSGCUser> queryUserList(CommonReqBean commonReqBean, int pageSize, int pageIndex){
      MpaasQuery mpaasQuery =  sw.buildQuery();
        StringBuffer sqlStr= new StringBuffer("select * from dsgc_user du where 1=1 ");
        if (StringUtil.isNotBlank(commonReqBean.getCon0())) {
            String[] conArray = commonReqBean.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
        if(!commonReqBean.getUserRole().equals("ALL")){
            sqlStr.append(" and du.user_role = #userRole ");
            mpaasQuery.setVar("userRole",commonReqBean.getUserRole());
         }
        mpaasQuery.sql(sqlStr.toString());
            return mpaasQuery.doPageQuery(pageIndex,pageSize,DSGCUser.class);
    }
    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(du.user_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(du.user_description) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public PageQueryResult<DSGCSystemEntities> queryAppsList(CommonReqBean commonReqBean, int pageSize, int pageIndex,String userName,String userRole){
        StringBuffer strSql = new StringBuffer("select * from( select ent.id,ent.sys_code, ent.sys_name, ent.sys_desc,ent.creation_date, t.user_name owner  from DSGC_SYSTEM_ENTITIES ent, (select listagg(u.user_description, ',') within GROUP(order by su.sys_code) user_name, su.sys_code from DSGC_SYSTEM_user su, dsgc_user u where su.user_id = u.user_id group by su.sys_code) t where t.sys_code(+) = ent.sys_code and (ent.attribue1 != 'N'or ent.attribue1 is null))  WHERE 1 = 1 ");
        MpaasQuery mq = sw.buildQuery();

        if (StringUtil.isNotBlank(commonReqBean.getCon0())) {
            String[] conArray = commonReqBean.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    strSql.append(this.generateLikeAndCluse1(s));
                }
            }
        }
//        if("SystemLeader".equals(userRole)){
//            strSql.append(" and upper(owner) like upper(#userName) ");
//            mq.setVar("userName","%"+userName+"%");
//        }
        mq.sql(strSql.toString()+" order by creation_date desc ");


        return mq.doPageQuery(pageIndex,pageSize,DSGCSystemEntities.class);
    }
    private String generateLikeAndCluse1(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(sys_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(owner) like '%" + conUpper + "%'";
        conAnd += " or UPPER(sys_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public DSGCSystemEntities querySystemEnt(String id){
        return sw.buildQuery().eq("id",id).doQueryFirst(DSGCSystemEntities.class);
    }
    public void delSytemEnt(String id){
        sw.buildQuery().eq("id",id).doDelete(DSGCSystemEntities.class);
    }

    public void updateSystemEnt(String id) {
        sw.buildQuery()
                .update("attribue1","N")
                .eq("id",id).doUpdate(DSGCSystemEntities.class);
    }
    public List<DSGCService> queryServBySysCode(String sysCode){
        return sw.buildQuery().eq("subordinateSystem",sysCode).doQuery(DSGCService.class);
    }
    public void delSytemUser(String sysCode){
        sw.buildQuery().eq("sysCode",sysCode).doDelete(DSGCSystemUser.class);
    }
    public List<DSGCUser> querySystemUserBySysCode(String sysCode){
        StringBuffer strSql = new StringBuffer("select du.* from dsgc_system_user dsu,dsgc_user du where 1=1 and dsu.user_id = du.user_id and dsu.sys_code = #sysCode");
        MpaasQuery mq = sw.buildQuery();
        mq.setVar("sysCode",sysCode);
       return mq.sql(strSql.toString()).doQuery(DSGCUser.class);
    }

    public List<DSGCUser> queryUserListByUserIdArr(List<String> userIdList){
        return sw.buildQuery().in("userId",userIdList)
                .doQuery(DSGCUser.class);
    }

    public void addAppData(DSGCSystemEntities dsgcSystemEntities){
        sw.buildQuery().doInsert(dsgcSystemEntities);
    }
    public void updateAppData(DSGCSystemEntities dsgcSystemEntities){
        sw.buildQuery()
                .update("sys_code",dsgcSystemEntities.getSysCode())
                .update("sys_name",dsgcSystemEntities.getSysName())
                .update("sys_desc",dsgcSystemEntities.getSysDesc())
                .update("last_updated_by",dsgcSystemEntities.getLastUpdatedBy())
                .eq("id",dsgcSystemEntities.getId())
                .doUpdate(DSGCSystemEntities.class);
    }
    public void addSystemUser(List<DSGCSystemUser> userList){
        sw.buildQuery()
                .doBatchInsert(userList);
    }
    public Boolean checkSystemCodeIsExist(String sysCode){
        DSGCSystemEntities dsgcSystemEntities = sw.buildQuery().eq("sys_code",sysCode).doQueryFirst(DSGCSystemEntities.class);
        if (dsgcSystemEntities != null){
            return true;
        }else {
            return false;
        }
    }
}
