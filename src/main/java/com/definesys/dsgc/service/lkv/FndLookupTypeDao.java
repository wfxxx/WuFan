package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.lkv.bean.FndModules;
import com.definesys.dsgc.service.lkv.bean.QueryLktParamBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.utils.YStarUtil;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.conf.MpaasQueryConfig;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.mpaas.query.util.MpaasQueryUtil;
//import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FndLookupTypeDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    @Autowired
    private MpaasQueryConfig config;

    @Value("${database.type}")
    private String dbType;

    public List getLookupValuesByType(FndLookupType lookupType) {
        FndLookupType resultlookupType = this.sw.buildQuery()
                .eq("LOOKUP_TYPE", lookupType.getLookupType())
                .doQueryFirst(FndLookupType.class);


        return this.sw.buildQuery().eq("LOOKUP_ID", resultlookupType.getLookupId())
                .doQuery(FndLookupValue.class);
    }

    public FndLookupType getFndLookupTypeByTypeId(FndLookupType fndLookupType) {
        FndLookupType fndLookupType1 = this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doQueryFirst(FndLookupType.class);
        List<FndLookupValue> values = this.sw.buildQuery()
                .eq("lookupId", fndLookupType1.getLookupId())
                .doQuery(FndLookupValue.class);
        fndLookupType1.setValues(values);
        return fndLookupType1;
    }

    public FndLookupType findFndLookupTypeByType(FndLookupType fndLookupType) {
        FndLookupType fndLookupType1 = this.sw.buildQuery()
                .eq("lookupType", fndLookupType.getLookupType())
                .doQueryFirst(FndLookupType.class);
        FndModules modules = this.sw.buildQuery()
                .eq("moduleId", fndLookupType1.getModuleId())
                .doQueryFirst(FndModules.class);
        fndLookupType1.setFndModules(modules);
        List<FndLookupValue> values = this.sw.buildQuery()
                .eq("lookupId", fndLookupType1.getLookupId())
                .doQuery(FndLookupValue.class);
        fndLookupType1.setValues(values);
        return fndLookupType1;
    }


    public FndLookupType getFndLookupTypeByType(String type) {
        FndLookupType fndLookupType1 = this.sw.buildQuery()
                .eq("lookupType", type)
                .doQueryFirst(FndLookupType.class);
        // todo 此地容易出现空指针
        FndModules modules = this.sw.buildQuery()
                .eq("moduleId", fndLookupType1.getModuleId())
                .doQueryFirst(FndModules.class);
        fndLookupType1.setFndModules(modules);
        List<FndLookupValue> values = this.sw.buildQuery()
                .eq("lookupId", fndLookupType1.getLookupId())
                .doQuery(FndLookupValue.class);
        fndLookupType1.setValues(values);
        return fndLookupType1;
    }

    public PageQueryResult<Map<String, Object>> query(FndLookupType fndLookupType, int pageSize, int pageIndex, String orOrAnd) {
        String sql = null;
        if ("oracle".equals(dbType)) {
            sql = "select * from (select t.*,rownum rn from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and ( UPPER(lookup_type) like'%#lookup_type%' and UPPER(lookup_name) like'%#lookup_name%' and UPPER(lookup_description)  like'%#lookup_description%'))t where rownum<=#pageEnd) where rn>#pageStart";
        }
        if ("mysql".equals(dbType)) {
            sql = "SELECT * FROM ( SELECT t.* ,@rownum := @rownum +1 AS rn FROM ( SELECT DISTINCT t.lookup_type LOOKUP_TYPE,t.lookup_id LOOUP_ID,t.lookup_name LOOKUP_NAME,t.lookup_description LOOKUP_DESCRIPTION,m.module_name MODULE_NAME,m.module_id MODULE_ID FROM FND_LOOKUP_TYPES T left join FND_LOOKUP_VALUES V on T.LOOKUP_ID = V.LOOKUP_ID " +
                    "left join FND_MODULES m on  t.module_id = m.module_id WHERE( UPPER( t.lookup_type ) LIKE '%#lookup_type%' AND UPPER( t.lookup_name ) LIKE '%#lookup_name%' AND UPPER( t.lookup_description ) LIKE '%#lookup_description%' ) ) t ,(SELECT @rownum := 0) r " +
                    "WHERE @rownum <= #pageEnd) s where rn>#pageStart";
        }
        //  String sql = "select * from (select t.*,rownum rn from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and ( UPPER(lookup_type) like'%#lookup_type%' and UPPER(lookup_name) like'%#lookup_name%' and UPPER(lookup_description)  like'%#lookup_description%'))t where rownum<=#pageEnd) where rn>#pageStart";
        if ("or".equals(orOrAnd)) {
            if ("oracle".equals(dbType)) {
                sql = "select * from (select t.*,rownum rn from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and ( UPPER(lookup_type) like'%#lookup_type%' or UPPER(lookup_name) like'%#lookup_name%' or UPPER(lookup_description)  like'%#lookup_description%'))t where rownum<=#pageEnd) where rn>#pageStart";
            }
            if ("mysql".equals(dbType)) {
                sql = "select * from (select t.*,@rownum := @rownum +1 AS rn from (SELECT distinct t.lookup_type LOOKUP_TYPE,t.lookup_id LOOKUP_ID,t.lookup_name LOOKUP_NAME,t.lookup_description LOOKUP_DESCRIPTION,m.module_name MODULE_NAME,m.module_id MODULE_ID FROM FND_LOOKUP_TYPES T LEFT JOIN FND_LOOKUP_VALUES V on T.LOOKUP_ID = V.LOOKUP_ID LEFT JOIN FND_MODULES m on t.module_id = m.module_id WHERE ( UPPER(lookup_type) like'%#lookup_type%' or UPPER(lookup_name) like'%#lookup_name%' or UPPER(lookup_description)  like'%#lookup_description%'))t, (SELECT @rownum := 0) r where  @rownum<=#pageEnd) s where rn>#pageStart";
            }
            //  sql = "select * from (select t.*,rownum rn from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and ( UPPER(lookup_type) like'%#lookup_type%' or UPPER(lookup_name) like'%#lookup_name%' or UPPER(lookup_description)  like'%#lookup_description%'))t where rownum<=#pageEnd) where rn>#pageStart";
        }
        sql = sql.replace("#lookup_name", fndLookupType.getLookupName().toUpperCase());
        sql = sql.replace("#lookup_type", fndLookupType.getLookupType().toUpperCase());
        sql = sql.replace("#lookup_description", fndLookupType.getLookupDescription().toUpperCase());
        sql = sql.replace("#pageStart", new Integer((pageIndex - 1) * pageSize).toString());
        sql = sql.replace("#pageEnd", new Integer(pageIndex * pageSize).toString());
        String sql1 = null;
        if ("oracle".equals(dbType)) {
            sql1 = "SELECT COUNT(1) AS RESULTCOUNT from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and UPPER(T.lookup_type) like'%#lookup_type%')";
        }
        if ("mysql".equals(dbType)) {
            sql1 = "SELECT COUNT(1) AS RESULTCOUNT from (SELECT distinct t.lookup_type lookupType,t.lookup_id lookupId,t.lookup_name lookupName,t.lookup_description lookupDescription,m.module_name moduleName,m.module_id moduleId  FROM FND_LOOKUP_TYPES T left join FND_LOOKUP_VALUES V on T.LOOKUP_ID = V.LOOKUP_ID LEFT JOIN FND_MODULES m on t.module_id = m.module_id WHERE UPPER(T.lookup_type) like'%#lookup_type%') s";
        }
        // String sql1 = "SELECT COUNT(1) AS resultCount from (SELECT distinct t.lookup_type,t.lookup_id,t.lookup_name,t.lookup_description,m.module_name,m.module_id  FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V,FND_MODULES m  WHERE T.LOOKUP_ID = V.LOOKUP_ID(+) and t.module_id = m.module_id and UPPER(T.lookup_type) like'%#lookup_type%')";
        sql1 = sql1.replace("#lookup_type", fndLookupType.getLookupType().toUpperCase());
        PageQueryResult<Map<String, Object>> pageQueryResult = new PageQueryResult();
        List<Map<String, Object>> result = sw.buildQuery()
                .sql(sql)
                .doQuery();
        Map<String, Object> map = sw.buildQuery().sql(sql1).doQueryFirst();
        pageQueryResult.setResult(result);
        pageQueryResult.setCount(new Long(map.get("RESULTCOUNT").toString()));
        return pageQueryResult;
    }

    public String deleteFndLookupType(FndLookupType fndLookupType) {
        fndLookupType = this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doQueryFirst(FndLookupType.class);
        this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doDelete(FndLookupValue.class);
        this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doDelete(FndLookupType.class);
        return fndLookupType.getLookupId();
    }

    public String updateFndLookupType(FndLookupType fndLookupType) {
        this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doDelete(FndLookupValue.class);
        List<FndLookupValue> values = fndLookupType.getValues();
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                FndLookupValue v = values.get(i);
                v.setLookupId(fndLookupType.getLookupId());
                this.sw.buildQuery().doInsert(v);
            }
        }
        this.sw.buildQuery()
                .eq("lookupId", fndLookupType.getLookupId())
                .doUpdate(fndLookupType);
        return fndLookupType.getLookupId();
    }

    public String addFndLookupType(FndLookupType fndLookupType) {
        String lookupId = UUID.randomUUID().toString().replaceAll("-", "");
        fndLookupType.setLookupId(lookupId);
        List<FndLookupValue> values = fndLookupType.getValues();
        this.sw.buildQuery()
                .doInsert(fndLookupType);
        this.logger.debug("getLookupId  " + fndLookupType.getLookupId());
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                FndLookupValue va = values.get(i);
                va.setLookupId(lookupId);
                this.sw.buildQuery()
                        .doInsert(va);
            }
        }
        return fndLookupType.getLookupId();
    }

    /**
     * 对值列表类型校重
     *
     * @param lookupType
     * @return
     */
    public boolean checkLookUpType(FndLookupType lookupType) {
        MpaasQuery mq = sw.buildQuery()
                .eq("lookupType", lookupType.getLookupType());
        if (StringUtil.isNotBlank(lookupType.getLookupId())) {
            mq.ne("lookupId", lookupType.getLookupId());
        }

        List<FndLookupType> list = mq.doQuery(FndLookupType.class);

        return list.size() > 0;
    }


    public List<Map<String, Object>> getLookUpValue(String lookUpType) {
        String sql = "sELECT v.lookup_code, v.meaning, v.description FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V WHERE T.LOOKUP_ID = V.LOOKUP_ID and t.lookup_type =#lookUpType";
        List<Map<String, Object>> result = sw.buildQuery()
                .sql(sql)
                .setVar("lookUpType", lookUpType)
                .doQuery();
        return result;
    }

    /**
     * 查看是否存在lookUpID，用于判断值列表是否已经存在
     *
     * @param lookUpType
     * @return
     */
    public String getLookUpId(String lookUpType) {
        String sql = "SELECT T.LOOKUP_ID\n" +
                "FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V \n" +
                "WHERE T.LOOKUP_ID = V.LOOKUP_ID and t.lookup_type = #lookUpType";
        Map<String, Object> lookUpValue = sw.buildQuery().sql(sql).setVar("lookUpType", lookUpType).doQueryFirst();
        return lookUpValue.get("LOOKUP_ID").toString();
    }

    /**
     * 修改值列表
     *
     * @param fndLookupValue
     */
    public void mergeLookUpValue(FndLookupValue fndLookupValue) {
        sw.buildQuery().doMerge(fndLookupValue);
    }

    /**
     * 获取需要修改的值列表
     *
     * @param fndLookupValue
     * @return
     */
    public FndLookupValue getLookUpValue(FndLookupValue fndLookupValue) {
        return sw.buildQuery().
                eq("lookUp_Id", fndLookupValue.getLookupValueId()).
                eq("lookUp_code", fndLookupValue.getLookupCode()).
                doQueryFirst(FndLookupValue.class);
    }

    public void deleteLookupByIdAndCode(FndLookupValue fndLookupValue) {
        sw.buildQuery().
                eq("lookUp_Id", fndLookupValue.getLookupId()).
                eq("lookUp_code", fndLookupValue.getLookupCode()).
                doDelete(FndLookupValue.class);
    }


    /**
     * 获取值列表
     *
     * @param lookupType
     * @return
     */
    public Map<String, String> getlookupValues(String lookupType) {
        List<Map<String, Object>> res = sw.buildQuery().sql("select LOOKUP_CODE,MEANING from fnd_lookup_types t ,fnd_lookup_values v where t.lookup_id = v.lookup_id and t.lookup_type = #lookupType")
                .setVar("lookupType", lookupType)
                .doQuery();
        Map<String, String> lkv = new HashMap<String, String>();
        if (res != null) {
            Iterator<Map<String, Object>> iter = res.iterator();
            while (iter.hasNext()) {
                Map<String, Object> row = iter.next();
                String key = (String) row.get("LOOKUP_CODE");
                if (key != null) {
                    lkv.put(key, (String) row.get("MEANING"));
                }

            }
        }
        return lkv;
    }


    /**
     * 查看是否存在某值列表类型的某个value值
     *
     * @param lookUpType
     * @param lookupCode
     * @return
     */
    public boolean checkLookupValue(String lookUpType, String lookupCode) {
        String sql = "SELECT T.LOOKUP_ID\n" +
                "FROM FND_LOOKUP_TYPES T, FND_LOOKUP_VALUES V \n" +
                "WHERE T.LOOKUP_ID = V.LOOKUP_ID and t.lookup_type = #lookUpType and V.LOOKUP_CODE = #lookupCode ";
        Map<String, Object> lookUpValue = sw.buildQuery().sql(sql).setVar("lookUpType", lookUpType)
                .setVar("lookupCode", lookupCode).doQueryFirst();
        return lookUpValue.size() > 0;
    }


    public List<QueryLktParamBean> queryLkvList(QueryLktParamBean lkvParamBean) {
        String sql = "select t.lookup_type,t.lookup_name from fnd_lookup_types t where 1=1 ";
        String lktCode = lkvParamBean.getLookupType();
        if (StringUtils.isEmpty(lktCode)) {
            return null;
        } else if (!"ALL".equals(lktCode.toUpperCase())) {
            sql += " and t.lookup_type in (" + YStarUtil.getSqlWhereClause(lktCode) + ")";
        }
        return this.sw.buildQuery().sql(sql).doQuery(QueryLktParamBean.class);
    }


    public PageQueryResult<FndLookupType> pageQueryFndLkv(FndLookupType fndLookupType, int pageSize, int pageIndex) {
        return this.sw.buildQuery()
                .or()
                .like("lookupType", fndLookupType.getLookupType())
                .like("lookupName", fndLookupType.getLookupName())
                .like("lookupDescription", fndLookupType.getLookupDescription())
                .doPageQuery(pageIndex, pageSize, FndLookupType.class);
    }

    public List<FndLookupValue> queryLkvListById(String lookupId) {
        return this.sw.buildQuery().eq("lookupId", lookupId).doQuery(FndLookupValue.class);
    }
    public List<FndModules> queryModuleListById(String moduleId) {
        return this.sw.buildQuery().eq("moduleId", moduleId).doQuery(FndModules.class);
    }

}
