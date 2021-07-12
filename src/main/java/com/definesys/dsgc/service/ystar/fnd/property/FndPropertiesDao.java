package com.definesys.dsgc.service.ystar.fnd.property;

import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.lkv.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.utils.YStarUtil;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FndPropertiesDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    public String createFndProperties(FndProperties fndProperties) {
        logger.debug(fndProperties.toString());
        sw.buildQuery().
                doInsert(fndProperties);
        return fndProperties.getPropertyId();
    }

    public String updateFndProperties(FndProperties fndProperties) {
        logger.debug(fndProperties.toString());
        sw.buildQuery()
                .eq("propertyId", fndProperties.getPropertyId())
                .doUpdate(fndProperties);
        return fndProperties.getPropertyId();
    }

    public String deleteFndPropertiesById(FndProperties fndProperties) {
        logger.debug(fndProperties.toString());
        sw.buildQuery()
                .eq("propertyId", fndProperties.getPropertyId())
                .doDelete(fndProperties);
        return fndProperties.getPropertyId();
    }

    /**
     * 不区分大小写查询
     *
     * @param fndProperties
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public PageQueryResult<FndProperties> query(FndProperties fndProperties, int pageSize, int pageIndex, String orOrAnd) {

        StringBuffer strSql = new StringBuffer("SELECT * FROM FND_PROPERTIES  WHERE 1 = 1 ");

        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(fndProperties.getPropertyKey())) {
            strSql.append(" and (upper(property_key) like #propertyKey ");
        }
        if (StringUtil.isNotBlank(fndProperties.getPropertyValue())) {
            strSql.append(" " + orOrAnd + " upper(property_value) like #propertyValue ");
        }
        if (StringUtil.isNotBlank(fndProperties.getPropertyDescription())) {
            strSql.append(" " + orOrAnd + " upper(property_description) like #propertyDescription ) ");
        }
        mq.sql(strSql.toString());
        if (StringUtil.isNotBlank(fndProperties.getPropertyKey())) {
            mq.setVar("propertyKey", "%" + fndProperties.getPropertyKey().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(fndProperties.getPropertyValue())) {
            mq.setVar("propertyValue", "%" + fndProperties.getPropertyValue().toUpperCase().trim() + "%");
        }
        if (StringUtil.isNotBlank(fndProperties.getPropertyDescription())) {
            mq.setVar("propertyDescription", "%" + fndProperties.getPropertyDescription().toUpperCase().trim() + "%");
        }
        return mq.doPageQuery(pageIndex, pageSize, FndProperties.class);
    }

    public List<FndProperties> findAll() {
        return sw.buildQuery()
                .doQuery(FndProperties.class);
    }

    public FndProperties findFndPropertiesById(FndProperties fndProperties) {
        logger.debug(fndProperties.toString());
        return sw.buildQuery().eq("propertyId", fndProperties.getPropertyId()).doQueryFirst(FndProperties.class);
    }

    public List<FndProperties> findFndPropertiesByListKey(String[] listKey) {
        return sw.buildQuery()
                .in("property_key", listKey)
                .doQuery(FndProperties.class);
    }

    public boolean checkPropertiesKeyIsExist(FndProperties properties) {
        MpaasQuery mq = sw.buildQuery()
                .eq("property_key", properties.getPropertyKey());
        if (StringUtil.isNotBlank(properties.getPropertyId())) {
            mq.ne("property_id", properties.getPropertyId());
        }

        List<FndProperties> list = mq.doQuery(FndProperties.class);
        return list.size() > 0;
    }

    public FndProperties findFndPropertiesByKey(String key) {
        return sw.buildQuery()
                .eq("property_key", key)
                .doQueryFirst(FndProperties.class);
    }

    public Map<String, Object> getTagByCode(String code) {
        return sw.buildQuery().sql("select lookup_code,tag from fnd_lookup_values")
                .setVar("lookup_code", code).doQueryFirst();
    }

    public List<QueryPropertyParamBean> queryProperties(QueryPropertyParamBean property) {
        String key = property.getPropertyKey();
        String sql = " select s.property_key,s.property_value from fnd_properties s where 1=1 ";
        if (StringUtils.isEmpty(key)) {
            return null;
        } else if (!"ALL".equals(key.toUpperCase())) {
            sql += " and s.property_key in (" + YStarUtil.getSqlWhereClause(key) + ")";
        }
        return sw.buildQuery()
                .sql(sql)
                .doQuery(QueryPropertyParamBean.class);
    }

    public String queryPropertyByKey(String key) {
        FndProperties property = this.findFndPropertiesByKey(key);
        if (property != null) {
            return property.getPropertyValue();
        } else {
            return null;
        }

    }


}
