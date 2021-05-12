package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndModules;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FndModulesDao {
    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;


    public String createFndModules(FndModules t) {
        logger.debug(t.toString());
        sw.buildQuery().
                doInsert(t);
        return t.getModuleId();
    }

    public String updateFndModules(FndModules t) {
        logger.debug(t.toString());
        sw.buildQuery()
                .eq("moduleId", t.getModuleId())
                .doUpdate(t);
        return t.getModuleId();
    }

    public String deleteFndModulesById(FndModules t) {
        logger.debug(t.toString());
        sw.buildQuery()
                .eq("moduleId", t.getModuleId())
                .doDelete(t);
        return t.getModuleId();
    }

    /**
     * 模块管理--分页查询--不区分大小查询
     * @param modules
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public PageQueryResult<FndModules> query(FndModules modules, int pageSize, int pageIndex) {
        logger.debug(modules.toString());

        StringBuffer strSql = new StringBuffer("SELECT * FROM FND_MODULES  WHERE 1 = 1 ");

        MpaasQuery mq = sw.buildQuery();
        if(StringUtil.isNotBlank(modules.getModuleCode())){
            strSql.append(" and upper(module_code) like #moduleCode ");
        }
        if(StringUtil.isNotBlank(modules.getModuleName())){
            strSql.append(" and upper(module_name) like #moduleName ");
        }
        if(StringUtil.isNotBlank(modules.getModuleDescription())){
            strSql.append(" and upper(module_description) like #moduleDescription ");
        }
        mq.sql(strSql.toString());
        if(StringUtil.isNotBlank(modules.getModuleCode())){
            mq.setVar("moduleCode","%"+modules.getModuleCode().toUpperCase().trim()+"%");
        }
        if(StringUtil.isNotBlank(modules.getModuleName())){
            mq.setVar("moduleName","%"+modules.getModuleName().toUpperCase().trim()+"%");
        }
        if(StringUtil.isNotBlank(modules.getModuleDescription())){
            mq.setVar("moduleDescription","%"+modules.getModuleDescription().toUpperCase().trim()+"%");
        }
        return mq.doPageQuery(pageIndex,pageSize,FndModules.class);
    }

    public FndModules findFndModulesById(FndModules t) {
        logger.debug(t.toString());
        return sw.buildQuery().eq("moduleId", t.getModuleId()).doQueryFirst(FndModules.class);
    }


    /**
     *  对模块编码校重,新增时，对整个数据表进行校重，修改时，排除本身
     * @return
     */
    public boolean checkModuleCodeIsExist(FndModules modules) {
        MpaasQuery mq = sw.buildQuery()
                .eq("moduleCode",modules.getModuleCode());

        if(StringUtil.isNotBlank(modules.getModuleId())){
            mq.ne("moduleId",modules.getModuleId());
        }
        List<FndModules> list = mq.doQuery(FndModules.class);
        return list.size() > 0;
    }

    /**
     * 批量删除模块
     * @param modId
     */
    public void deleteBatchModuleById(String modId) {
        sw.buildQuery()
                .eq("moduleId",modId)
                .doDelete(FndModules.class);
    }
}
