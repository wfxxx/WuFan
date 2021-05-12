package com.definesys.dsgc.service.projdir;

import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.dsgc.service.projdir.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.projdir.bean.SysProfileDirVO;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/6/21 上午10:34
 * @history: 1.2019/6/21 created by biao.luo
 */
@Service
public class DSGCProjDirManagerService {
    @Autowired
    private DSGCProjDirManagerDao projDirManagerDao;

    public PageQueryResult<SysProfileDirVO> querySysProfileDirListPage(int pageIndex, int pageSize, SysProfileDirVO sysProfileDirVO) {
        return projDirManagerDao.querySysProfileDirListPage(pageIndex, pageSize, sysProfileDirVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateSysProfileDir(SysProfileDirVO sysProfileDirVO) {
        if (StringUtil.isBlank(sysProfileDirVO.getProjId())) {
            DSGCSysProfileDir sysProfileDir = new DSGCSysProfileDir();
            sysProfileDir.setProjName(sysProfileDirVO.getProjName());
            sysProfileDir.setProjDesc(sysProfileDirVO.getProjDesc());
            sysProfileDir.setSysCode(sysProfileDirVO.getSysCode());
            sysProfileDir.setProjType(sysProfileDirVO.getProjType());
            sysProfileDir.setBranchName(sysProfileDirVO.getBranchName());
            sysProfileDir.setInitStatus(sysProfileDirVO.getInitStatus());
            sysProfileDir.setRepoUri(sysProfileDirVO.getRepoUri());
            sysProfileDir.setProjPort(sysProfileDirVO.getProjPort());
            //新增前对目录名称校重
            boolean chek = projDirManagerDao.checkSysProfileDirNameIsExist(sysProfileDirVO);
            if (chek) {
                throw new MpaasBusinessException("改项目目录已被占用，请选择其他目录后重试！");
            }
            projDirManagerDao.saveSysProfileDir(sysProfileDir);
        } else {
            //修改前对目录名称校重，排除本身
            DSGCSysProfileDir sysProfile = projDirManagerDao.querySysProfileDirByProjId(sysProfileDirVO.getProjId());
            sysProfile.setProjName(sysProfileDirVO.getProjName());
            sysProfile.setProjDesc(sysProfileDirVO.getProjDesc());
            sysProfile.setSysCode(sysProfileDirVO.getSysCode());
            sysProfile.setProjType(sysProfileDirVO.getProjType());
            sysProfile.setBranchName(sysProfileDirVO.getBranchName());
            sysProfile.setInitStatus(sysProfileDirVO.getInitStatus());
            sysProfile.setRepoUri(sysProfileDirVO.getRepoUri());
            sysProfile.setProjPort(sysProfileDirVO.getProjPort());
            boolean checknoself = projDirManagerDao.checkSysProfileDirNameIsExist(sysProfileDirVO);

            if (checknoself) {
                throw new MpaasBusinessException("改项目目录已被占用，请选择其他目录后重试！");
            }
            projDirManagerDao.updateSysProfileDir(sysProfile);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delSysProfileDirByProId(String[] proIds) {
        if (proIds.length > 0) {
            for (String proId : proIds) {
                projDirManagerDao.delSysProfileDirByProId(proId);
            }
        }
    }

    public DSGCSysProfileDir getSysProfileDirByProId(String proId) {
        return projDirManagerDao.getSysProfileDirByProId(proId);
    }


    public List<DSGCSystemEntities> querySystemListPage(String userRole, String uid) {
        //这里区分超管与系统管理员，系统管理员--只查询当前对应下的系统目录，超级管理员--查询所有项目目录
        //超管或平台管理员查询所有，新增系统目录时，选择一个系统下去创建
        if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole) || "Tourist".equals(userRole)) {
            return projDirManagerDao.querySystemListPage();
        } else if ("SystemLeader".equals(userRole)) {
            //系统管理员只查询被授权管理的系统
            List<DSGCSystemUser> codes = projDirManagerDao.querySystemlistUserByUserId(uid);
            List<String> codeList = new ArrayList<>();
            if (codes.size() < 1) {
                return null;
            } else {
                for (DSGCSystemUser user : codes) {
                    codeList.add(user.getSysCode());
                }
                return projDirManagerDao.querySingleSystem(codeList);
            }
        } else {
            return null;

        }
    }

    /**
     * 校验项目目录是否存在
     *
     * @param proDirName 项目目录
     * @return
     */
    public DSGCSysProfileDir checkProDir(String proDirName) {
        return projDirManagerDao.checkProDir(proDirName);
    }

    /**
     * 根据系统获取目录
     *
     * @param servNo 服务编号
     * @return
     */
    public List<DSGCSysProfileDir> getProDirBySystem(String servNo) {
        DSGCService dsgcService = projDirManagerDao.findServiceByServNo(servNo);
//        String sysCodess = dsgcService.getAttribue5();
        String sysCode = dsgcService.getSubordinateSystem();
        return projDirManagerDao.getProDirBySystem(sysCode);
    }

    public List<DSGCSystemEntities> querySystemBySubSystem(String subSystem) {
        return subSystem == null ? null : projDirManagerDao.querySystenBysubSystem(subSystem);
    }
}
