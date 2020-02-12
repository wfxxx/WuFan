package com.definesys.dsgc.service.projdir;

import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.projdir.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.projdir.bean.SysProfileDirVO;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/6/21 上午10:49
 * @history: 1.2019/6/21 created by biao.luo
 */
@Repository
public class DSGCProjDirManagerDao {

    @Autowired
    private MpaasQueryFactory sw;


    public PageQueryResult<SysProfileDirVO> querySysProfileDirListPage(int pageIndex, int pageSize, SysProfileDirVO sysProfileDirVO) {
        StringBuffer strSql = new StringBuffer("SELECT *  FROM ( SELECT pd.proj_id, pd.proj_name, pd.proj_desc, pd.sys_code, ss.sys_name FROM DSGC_SVCGEN_PROJ_INFO pd, DSGC_SYSTEM_ENTITIES ss WHERE pd.sys_code = ss.sys_code(+) )  WHERE 1 = 1 ");

        MpaasQuery mq = sw.buildQuery();
        if(StringUtil.isNotBlank(sysProfileDirVO.getProjName())){
            strSql.append(" and upper(sys_name) like #proDirName or  upper(proj_name) like #proDirName or upper(proj_desc) like #proDirName or upper(sys_code) like #proDirName ");
        }

        mq.sql(strSql.toString());
        if(StringUtil.isNotBlank(sysProfileDirVO.getProjName())){
            mq.setVar("proDirName","%"+sysProfileDirVO.getProjName().toUpperCase().trim()+"%");
        }
        return mq.doPageQuery(pageIndex, pageSize, SysProfileDirVO.class);

    }


    public DSGCSysProfileDir querySysProfileDirByProjId(String proId) {
        return sw.buildQuery()
                .eq("proj_id",proId)
                .doQueryFirst(DSGCSysProfileDir.class);
    }


    public boolean checkSysProfileDirNameIsExist(SysProfileDirVO sysProfileDirVO) {
        MpaasQuery mq = sw.buildQuery()
                .eq("proj_name",sysProfileDirVO.getProjName());

        if(StringUtil.isNotBlank(sysProfileDirVO.getProjId())){
            mq.ne("proj_id",sysProfileDirVO.getProjId());
        }

        List<DSGCSysProfileDir> list = mq.doQuery(DSGCSysProfileDir.class);

        return list.size() > 0;
    }

    public boolean checkSysProfileDirNameIsExist(String projName) {
        List<DSGCSysProfileDir> list = sw.buildQuery()
                .eq("proj_name",projName)
                .doQuery(DSGCSysProfileDir.class);
        return list.size() > 0;
    }


    public DSGCSysProfileDir saveSysProfileDir(DSGCSysProfileDir sysProfileDir) {
        sw.buildQuery()
                .doInsert(sysProfileDir);
        return sysProfileDir;
    }


    public void updateSysProfileDir(DSGCSysProfileDir sysProfile) {
        sw.buildQuery()
                .eq("proj_id",sysProfile.getProjId())
                .doUpdate(sysProfile);
    }

    public void delSysProfileDirByProId(String proId) {
        sw.buildQuery()
                .eq("proj_id",proId)
                .doDelete(DSGCSysProfileDir.class);
    }


    public DSGCSysProfileDir getSysProfileDirByProId(String proId) {
        return sw.buildQuery()
                .eq("proj_id",proId)
                .doQueryFirst(DSGCSysProfileDir.class);
    }

    public List<DSGCSystemEntities> querySystemListPage() {
        return sw.buildQuery()
                .select("id,sys_code,sys_name")
                .doQuery(DSGCSystemEntities.class);
    }

    /**
     * 查询用户授权管理的系统信息
     * @param uid
     * @return
     */
    public DSGCSystemUser querySystemUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id",uid)
                .doQueryFirst(DSGCSystemUser.class);
    }

    /**
     *  根据系统编码查询系统信息
     * @param codeList
     * @return
     */
    public List<DSGCSystemEntities> querySingleSystem(List<String> codeList ) {
        return sw.buildQuery()
                .in("sys_code",codeList)
                .doQuery(DSGCSystemEntities.class);
    }


    public List<DSGCSystemUser> querySystemUserBySysCode(String sysCode){
        return sw.buildQuery()
                .eq("sys_code",sysCode)
                .doQuery(DSGCSystemUser.class);
    }

    /**
     * 校验项目目录是否存在
     * @param proDirName 项目目录
     * @return
     */
    public DSGCSysProfileDir checkProDir(String proDirName){
        return  sw.buildQuery()
                .eq("PROJ_NAME",proDirName)
                .doQueryFirst(DSGCSysProfileDir.class);
    }

    /**
     * 根据系统获取目录
     * @param sysCode 系统编码
     * @return
     */
    public List<DSGCSysProfileDir> getProDirBySystem(String sysCode){
        return sw.buildQuery()
                .eq("sys_code",sysCode)
                .doQuery(DSGCSysProfileDir.class);
    }

    public List<DSGCSystemUser> querySystemlistUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id",uid)
                .doQuery(DSGCSystemUser.class);
    }

    public List<DSGCSystemEntities> querySystenBysubSystem(String subSystem) {
        return sw.buildQuery()
                .eq("sys_code",subSystem)
                .doQuery(DSGCSystemEntities.class);
    }

    public DSGCService findServiceByServNo(String servNo) {
        DSGCService re = sw.buildQuery()
                .eq("servNo", servNo)
                .doQueryFirst(DSGCService.class);
        return re;

    }
}
