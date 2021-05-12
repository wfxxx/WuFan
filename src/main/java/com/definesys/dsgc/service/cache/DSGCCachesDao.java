package com.definesys.dsgc.service.cache;

import com.definesys.dsgc.service.cache.bean.DSGCLogCacherefresh;
import com.definesys.dsgc.service.cache.bean.DSGCLogSysHeartbeat;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("dsgcCachesDao")
public class DSGCCachesDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;


    public List<DSGCLogSysHeartbeat> getCaches(){
        return sw.buildQuery()
                .doQuery( DSGCLogSysHeartbeat.class);
    }

    public List<DSGCLogCacherefresh> getChildCaches(String serverName){
        return sw.buildQuery()
                .like("server",serverName)
                .doQuery( DSGCLogCacherefresh.class);
    }

}
