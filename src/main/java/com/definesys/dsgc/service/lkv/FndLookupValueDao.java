package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.lkv.bean.QueryLkvParamBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FndLookupValueDao {

    @Autowired
    private MpaasQueryFactory sw;


    /**
     * 根据lookupId 查询值列表子项数据
     *
     * @param lookupType
     * @return
     */
    public List<FndLookupValue> getLookupValuesByLookupId(FndLookupType lookupType) {
        return this.sw.buildQuery().eq("LOOKUP_ID", lookupType.getLookupId())
                .doQuery(FndLookupValue.class);
    }

    /**
     * 根据lookupValueId 查询值列表子项数据
     *
     * @param lookupValue
     * @return
     */
    public List getLookupValuesByLookupValueId(FndLookupValue lookupValue) {
        return this.sw.buildQuery().eq("LOOKUP_VALUE_ID", lookupValue.getLookupValueId())
                .doQuery(FndLookupValue.class);
    }

    /**
     * 根据lookupCode和meaning 查询值列表子项数据
     *
     * @param lookupValue
     * @return
     */
    public List queryLookupValues(FndLookupValue lookupValue) {
        return this.sw.buildQuery()
                .eq("LOOKUP_CODE", lookupValue.getLookupCode())
                .eq("MEANING", lookupValue.getMeaning())
                .doQuery(FndLookupValue.class);
    }

    /**
     * 新增
     *
     * @param fndLookupValue
     */
    public void addFndLookupValue(FndLookupValue fndLookupValue) {
        String lookupValueId = UUID.randomUUID().toString().replaceAll("-", "");
        fndLookupValue.setLookupValueId(lookupValueId);
        this.sw.buildQuery().doInsert(fndLookupValue);
    }

    /**
     * 更新
     *
     * @param fndLookupValue
     * @return
     */
    public void updateFndLookupValue(FndLookupValue fndLookupValue) {
        String lookupValueId = fndLookupValue.getLookupValueId();
        this.sw.buildQuery()
                .eq("lookupValueId", fndLookupValue.getLookupValueId())
                .doMerge(fndLookupValue);
    }

    /**
     * 删除
     *
     * @param fndLookupValue
     * @return
     */
    public String delFndLookupValue(FndLookupValue fndLookupValue) {
        this.sw.buildQuery()
                .eq("lookupValueId", fndLookupValue.getLookupValueId())
                .doDelete(FndLookupValue.class);
        return fndLookupValue.getLookupValueId();
    }

    public FndLookupValue getLookupValueByCode(FndLookupValue lookupValue) {
        return this.sw.buildQuery()
                .eq("lookupCode", lookupValue.getLookupCode())
                .doQueryFirst(FndLookupValue.class);
    }

    /**
     * 获取值列表
     *
     * @param lookupType
     * @return
     */
    public Map<String, String> getLookupValuesByType(String lookupType) {
        List<Map<String, Object>> res = sw.buildQuery().sql("select lookup_code,meaning from fnd_lookup_types t ,fnd_lookup_values v where t.lookup_id = v.lookup_id and t.lookup_type = #lookupType")
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

    /*** 根据type 查询值列表*/
    public List<QueryLkvParamBean> queryLkvParamBean(String lookupType) {
        FndLookupType type = this.sw.buildQuery().eq("lookupType", lookupType).doQueryFirst(FndLookupType.class);
        List<QueryLkvParamBean> lkvList = null;
        if (type != null) {
            String lookupId = type.getLookupId();
            String sql = "select s.lookup_code,s.meaning lookup_value,s.tag lookup_tag,s.description from fnd_lookup_values s where s.lookup_id = '" + lookupId + "'";
            lkvList = this.sw.buildQuery().sql(sql).doQuery(QueryLkvParamBean.class);
        }
        return lkvList;
    }

}
