package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndModules;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FndModulesService {

    @Autowired
    FndModulesDao fndModulesDao;

    @Transactional(rollbackFor = Exception.class)
    public String createFndModules(FndModules t) {

        boolean bool = fndModulesDao.checkModuleCodeIsExist(t);
        if(bool){
            throw new MpaasBusinessException("模块编码已存在，请修改后重试！");
        }
        return fndModulesDao.createFndModules(t);
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateFndModules(FndModules t) {

        boolean bool = fndModulesDao.checkModuleCodeIsExist(t);
        if(bool){
            throw new MpaasBusinessException("模块编码已存在，请修改后重试！");
        }
        return fndModulesDao.updateFndModules(t);
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteFndModulesById(FndModules t) {
        return fndModulesDao.deleteFndModulesById(t);
    }

    public PageQueryResult<FndModules> query(FndModules t, int pageSize, int pageIndex) {
        return fndModulesDao.query(t, pageSize, pageIndex);
    }

    public FndModules findFndModulesById(FndModules t) {
        return fndModulesDao.findFndModulesById(t);
    }

    /**
     * 模块编码校重
     * @param modules
     * @return
     */
    public boolean checkModuleCodeIsExist(FndModules modules) {
        return fndModulesDao.checkModuleCodeIsExist(modules);
    }

    /**
     * 批量删除模块
     * @param modules
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchModuleById(String[] modules) {
        for(String modId : modules){
            if(StringUtil.isNotBlank(modId)) {
                fndModulesDao.deleteBatchModuleById(modId);
            }
        }

    }
}
