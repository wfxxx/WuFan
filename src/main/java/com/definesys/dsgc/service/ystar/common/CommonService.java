package com.definesys.dsgc.service.ystar.common;

import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("CommonService")
public class CommonService {
    @Autowired
    private CommonDao commonDao;

    public List<DSGCSystemEntities> getOnSystem() {
        return this.commonDao.getOnSystem();
    }

    public List<DSGCConsumerEntities> getDownSystem() {
        return this.commonDao.getDownSystm();
    }

    public PageQueryResult QueryList(int page, int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve){
        return this.commonDao.QueryList(page,pageSize,sysCode,csmCode,servNo,levelJ,toSolve);
    }
    public PageQueryResult QueryLists(int page, int pageSize,String sysCode, String csmCode, String servNo, String levelJ, String toSolve, String uid){
        return this.commonDao.Querylists(page,pageSize,sysCode,csmCode,servNo,levelJ,toSolve,uid);
    }

    public List<Map<String,Object>> QueryMap(){
        return this.commonDao.QueryMap();
    }


    public String Test(){
        return this.commonDao.Test();
    }
}
