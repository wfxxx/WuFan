package com.definesys.dsgc.service.ystar.svcgen.proj;

import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.ystar.svcgen.proj.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.svcgen.proj.bean.DSGCSysProfileDir;
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


    public PageQueryResult<DSGCSysProfileDir> pageQuerySysProfileDir(int pageIndex, int pageSize, CommonReqBean commonReqBean) {
        String con = commonReqBean.getCon0();
        MpaasQuery mq = sw.buildViewQuery("V_DSGC_SVCGEN_PROJ_INFO");
        if (StringUtil.isNotBlank(con)) {
            mq.like("projName", con).like("projType", con).like("projDesc", con);
        }
        return mq.doPageQuery(pageIndex, pageSize, DSGCSysProfileDir.class);

    }


    public DSGCSysProfileDir querySysProfileDirByProjId(String proId) {
        return sw.buildQuery()
                .eq("proj_id", proId)
                .doQueryFirst(DSGCSysProfileDir.class);
    }


    public boolean checkSysProfileDirNameIsExist(DSGCSysProfileDir sysProfileDirVO) {
        MpaasQuery mq = sw.buildQuery()
                .eq("proj_name", sysProfileDirVO.getProjName());

        if (StringUtil.isNotBlank(sysProfileDirVO.getProjId())) {
            mq.ne("proj_id", sysProfileDirVO.getProjId());
        }

        List<DSGCSysProfileDir> list = mq.doQuery(DSGCSysProfileDir.class);

        return list.size() > 0;
    }

    public boolean checkSysProfileDirNameIsExist(String projName) {
        List<DSGCSysProfileDir> list = sw.buildQuery()
                .eq("proj_name", projName)
                .doQuery(DSGCSysProfileDir.class);
        return list.size() > 0;
    }


    public DSGCSysProfileDir saveSysProfileDir(DSGCSysProfileDir sysProfileDir) {
        System.out.println(sysProfileDir.toString());
        sw.buildQuery()
                .doInsert(sysProfileDir);
        return sysProfileDir;
    }


    public void updateSysProfileDir(DSGCSysProfileDir sysProfile) {
        sw.buildQuery()
                .eq("proj_id", sysProfile.getProjId())
                .doUpdate(sysProfile);
    }

    public void updateProjectVersion(String proId, String version) {
        sw.buildQuery()
                .eq("PROJ_ID", proId)
                .update("TEXT_ATTRIBUTE1", version)
                .doUpdate(DSGCSysProfileDir.class);
    }

    public void delSysProfileDirByProId(String proId) {
        sw.buildQuery()
                .eq("proj_id", proId)
                .doDelete(DSGCSysProfileDir.class);
    }


    public DSGCSysProfileDir getSysProfileDirByProId(String proId) {
        return sw.buildViewQuery("V_DSGC_SVCGEN_PROJ_INFO")
                .eq("projId", proId)
                .doQueryFirst(DSGCSysProfileDir.class);
    }

    public List<DSGCSystemEntities> querySystemListPage() {
        return sw.buildQuery()
                .select("id,sys_code,sys_name")
                .doQuery(DSGCSystemEntities.class);
    }

    /**
     * 查询用户授权管理的系统信息
     *
     * @param uid
     * @return
     */
    public DSGCSystemUser querySystemUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id", uid)
                .doQueryFirst(DSGCSystemUser.class);
    }

    /**
     * 根据系统编码查询系统信息
     *
     * @param codeList
     * @return
     */
    public List<DSGCSystemEntities> querySingleSystem(List<String> codeList) {
        return sw.buildQuery()
                .in("sys_code", codeList)
                .doQuery(DSGCSystemEntities.class);
    }


    public List<DSGCSystemUser> querySystemUserBySysCode(String sysCode) {
        return sw.buildQuery()
                .eq("sys_code", sysCode)
                .doQuery(DSGCSystemUser.class);
    }

    /**
     * 校验项目目录是否存在
     *
     * @param proDirName 项目目录
     * @return
     */
    public DSGCSysProfileDir checkProDir(String proDirName) {
        return sw.buildQuery()
                .eq("PROJ_NAME", proDirName)
                .doQueryFirst(DSGCSysProfileDir.class);
    }

    /**
     * 根据系统获取目录
     *
     * @param sysCode 系统编码
     * @return
     */
    public List<DSGCSysProfileDir> getProDirBySystem(String sysCode) {
        return sw.buildQuery()
                .eq("sys_code", sysCode)
                .doQuery(DSGCSysProfileDir.class);
    }

    public List<DSGCSystemUser> querySystemlistUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id", uid)
                .doQuery(DSGCSystemUser.class);
    }

    public List<DSGCSystemEntities> querySystenBysubSystem(String subSystem) {
        return sw.buildQuery()
                .eq("sys_code", subSystem)
                .doQuery(DSGCSystemEntities.class);
    }

    public DSGCService findServiceByServNo(String servNo) {
        DSGCService re = sw.buildQuery()
                .eq("servNo", servNo)
                .doQueryFirst(DSGCService.class);
        return re;

    }


    public DSGCSysProfileDir getProjectInfoByProName(String proName) {
        return sw.buildQuery()
                .eq("proj_name", proName)
                .doQueryFirst(DSGCSysProfileDir.class);
    }

    public List<DSGCSysProfileDir> querySysProfileDirList(DSGCSysProfileDir sysProfileDir) {
        return this.sw.buildViewQuery("V_DSGC_SVCGEN_PROJ_INFO").eq("projName", sysProfileDir.getProjName())
                .eq("projType", sysProfileDir.getProjType()).doQuery(DSGCSysProfileDir.class);
    }
}
