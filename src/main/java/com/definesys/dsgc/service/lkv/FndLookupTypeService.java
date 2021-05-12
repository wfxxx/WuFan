package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.lkv.bean.QueryLktParamBean;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FndLookupTypeService {

    @Autowired
    FndLookupTypeDao fndLookupTypeDao;

    @Autowired
    FndLookupValueDao fndLookupValueDao;

    public FndLookupType getFndLookupTypeByTypeId(FndLookupType fndLookupType) {
        return this.fndLookupTypeDao.getFndLookupTypeByTypeId(fndLookupType);
    }

    public FndLookupType findFndLookupTypeByType(FndLookupType fndLookupType) {
        return this.fndLookupTypeDao.findFndLookupTypeByType(fndLookupType);
    }

    public PageQueryResult<Map<String, Object>> query(FndLookupType fndLookupType, int pageSize, int pageIndex) {
        return this.fndLookupTypeDao.query(fndLookupType, pageSize, pageIndex, "and");
    }

    public PageQueryResult<Map<String, Object>> queryOr(FndLookupType fndLookupType, int pageSize, int pageIndex) {
        return this.fndLookupTypeDao.query(fndLookupType, pageSize, pageIndex, "or");
    }

    @Transactional
    public String deleteFndLookupType(FndLookupType fndLookupType) {
        return this.fndLookupTypeDao.deleteFndLookupType(fndLookupType);
    }

    @Transactional
    public List<String> delFndLookupTypes(List<FndLookupType> list) {
        List<String> respList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            respList.add(this.fndLookupTypeDao.deleteFndLookupType(list.get(i)));
        }
        return respList;
    }

    @Transactional
    public String updateFndLookupType(FndLookupType fndLookupType) {
        return this.fndLookupTypeDao.updateFndLookupType(fndLookupType);
    }

    @Transactional
    public String addFndLookupType(FndLookupType fndLookupType) {
        return this.fndLookupTypeDao.addFndLookupType(fndLookupType);
    }

    public boolean checkLookUpType(FndLookupType lookupType) {
        return fndLookupTypeDao.checkLookUpType(lookupType);
    }

    public List<Map<String, Object>> getLookUpValue(String lookUpType) {
        return this.fndLookupTypeDao.getLookUpValue(lookUpType);
    }

    public void margeLookUpValue(FndLookupType fndLookupType) {
        String lookUpId = this.fndLookupTypeDao.getLookUpId(fndLookupType.getLookupType());
        List<FndLookupValue> values = fndLookupType.getValues();
        FndLookupValue fndLookupValue = values.get(0);
        if (lookUpId != null) { //修改值列表
            fndLookupValue.setLookupId(lookUpId);
            fndLookupTypeDao.deleteLookupByIdAndCode(fndLookupValue);
            fndLookupTypeDao.mergeLookUpValue(fndLookupValue);
        } else { // 新增值列表
            fndLookupTypeDao.addFndLookupType(fndLookupType);
        }
    }

    public List<FndLookupType> getLookupValuesByTypeList(List<FndLookupType> lookupTypes) {
        List<FndLookupType> fndTypes = new ArrayList<>();
        if (lookupTypes != null && lookupTypes.size() > 0) {
            for (FndLookupType type : lookupTypes) {
                fndTypes.add(fndLookupTypeDao.findFndLookupTypeByType(type));
            }
        }
        return fndTypes;
    }

    public List<QueryLktParamBean> queryLkvList(QueryLktParamBean lkvParamBean) {
        List<QueryLktParamBean> lktList = this.fndLookupTypeDao.queryLkvList(lkvParamBean);
        if (lktList != null && lktList.size() > 0) {
            for (QueryLktParamBean lkt : lktList) {
                lkt.setValues(this.fndLookupValueDao.queryLkvParamBean(lkt.getLookupType()));
            }
        }
        return lktList;
    }
}
