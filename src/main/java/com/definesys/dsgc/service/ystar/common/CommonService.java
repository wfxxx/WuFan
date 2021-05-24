package com.definesys.dsgc.service.ystar.common;

import com.definesys.dsgc.service.consumers.bean.DSGCConsumerEntities;
import com.definesys.dsgc.service.svclog.bean.DSGCLogInstance;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.utils.MsgCompressUtil;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.ystar.util.ResolverUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public PageQueryResult QueryList(int page, int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve) {
        return this.commonDao.QueryList(page, pageSize, sysCode, csmCode, servNo, levelJ, toSolve);
    }

    public PageQueryResult QueryLists(int page, int pageSize, String sysCode, String csmCode, String servNo, String levelJ, String toSolve, String uid) {
        return this.commonDao.Querylists(page, pageSize, sysCode, csmCode, servNo, levelJ, toSolve, uid);
    }

    public List<Map<String, Object>> QueryMap() {
        return this.commonDao.QueryMap();
    }


    public String Test() {
        return this.commonDao.Test();
    }

    public String updInstBusCnt(String svcCode,String key,String startTime, String endTime) {
        List<DSGCLogInstance> logInstances = this.commonDao.queryLogInstances(svcCode,startTime, endTime);
        if (logInstances.size() > 0) {
            List<String> idList = new ArrayList<>();
            for (DSGCLogInstance log : logInstances) {
                String id = log.getTrackId();
                if (!idList.contains(id)) {
                    idList.add(id);
                }
            }
            for (String id : idList) {
                Map<String, Object> payload = this.commonDao.queryBodyPayloadById(id);
                if (payload != null) {
                    String body = String.valueOf(payload.get("PAYLOAD_DATA"));
                    body = MsgCompressUtil.deCompress(body).replaceAll("<soapenv:Body xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">", "").replaceAll("</soapenv:Body>", "")
                            .replaceAll("<soap-env:Body xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\">", "").replaceAll("</soap-env:Body>", "");
                    if ("{NullPayload}".equals(body.trim())) {
                        System.out.println("body--1->>>" + body);
                    } else {
                        String count = ResolverUtil.preciseResolver(body, key);
                        System.out.println("result--->>>" + count);
                        if (StringUtil.isNotBlank(count)) {
                            this.commonDao.updLogInstanceBusCount(id, count);
                        }
                    }
                }
            }
        }
        System.out.println(logInstances.size());
        return null;
    }
}
