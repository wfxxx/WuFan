package com.definesys.dsgc.service.lkv;

import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FndPropertiesService {

    @Autowired
    FndPropertiesDao fndPropertiesDao;

    @Transactional
    public String createFndProperties(FndProperties fndProperties) {
        return fndPropertiesDao.createFndProperties(fndProperties);
    }
    @Transactional
    public String updateFndProperties(FndProperties fndProperties) {
    return fndPropertiesDao.updateFndProperties(fndProperties);
    }

    @Transactional
    public String deleteFndPropertiesById(FndProperties fndProperties) {
        return fndPropertiesDao.deleteFndPropertiesById(fndProperties);
    }

    public PageQueryResult<FndProperties> query(FndProperties fndProperties, int pageSize, int pageIndex) {
        return fndPropertiesDao.query(fndProperties, pageSize, pageIndex,"and");
    }
    public PageQueryResult<FndProperties> queryOr(FndProperties fndProperties, int pageSize, int pageIndex) {
        return fndPropertiesDao.query(fndProperties, pageSize, pageIndex,"or");
    }

    public FndProperties findFndPropertiesById(FndProperties fndProperties) {
        return fndPropertiesDao.findFndPropertiesById(fndProperties);
    }

    public List<FndProperties> findFndPropertiesByListKey(String[] listKey){
        return fndPropertiesDao.findFndPropertiesByListKey(listKey);
    }

    public boolean checkPropertiesKeyIsExist(FndProperties properties) {
        return fndPropertiesDao.checkPropertiesKeyIsExist(properties);
    }
}
