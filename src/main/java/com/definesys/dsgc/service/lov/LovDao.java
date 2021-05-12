package com.definesys.dsgc.service.lov;

import com.definesys.dsgc.service.lov.bean.LookupTypeLovBean;
import com.definesys.dsgc.service.lov.bean.SystemLovBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LovDao {



    @Autowired
    private MpaasQueryFactory sw;

    public List<LookupTypeLovBean> getLovListByLookupType(String lookupType){
       return  sw.buildQuery().sql(LookupTypeLovBean.SQL_GET_LOV_BY_LKT).setVar("lookupType",lookupType).doQuery(LookupTypeLovBean.class);
    }

    public List<SystemLovBean> getAllSystemLov(){
        return sw.buildQuery().sql(SystemLovBean.SQL_SYSTEM_LOV_ALL).doQuery(SystemLovBean.class);
    }

    public List<SystemLovBean> getAuthRangeSystemLov(String uid){
        return sw.buildQuery().sql(SystemLovBean.SQL_SYSTEM_LOV_AUTH).setVar("uid",uid).doQuery(SystemLovBean.class);
    }

}
