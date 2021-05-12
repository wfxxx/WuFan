package com.definesys.dsgc.service.svclog;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.dsgc.service.svclog.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import com.definesys.mpaas.query.session.MpaasSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Clob;
import java.util.*;

@Component("dsgcLogInstanceDao")
public class DSGCLogInstanceDao {

    @Autowired
    private MpaasQueryFactory sw;

    @Autowired
    private SWordLogger logger;

    @Value("${database.type}")
    private String dbType;

    public PageQueryResult<DSGCLogInstance> query(List<Map<String, String>> keyword, String isAdmin, String uid, LogInstanceQueryDTO instance, int pageSize, int pageIndex, List<String> sysNoList) throws Exception {
        logger.debug(instance.toString());
        //不存在关键字时
        if (keyword == null) {
            MpaasQuery mq = sw.buildQuery();
            if (StringUtil.isNotBlank(instance.getServNo())) {
                mq.likeNocase("serv_no", instance.getServNo());
            }
            if (StringUtil.isNotBlank(instance.getServName())) {
                mq.likeNocase("serv_name", instance.getServName());
            }
            if (StringUtil.isNotBlank(instance.getToken())) {
                mq.likeNocase("token", instance.getToken());
            }
            if (StringUtil.isNotBlank(instance.getInstStatus())) {
                mq.eq("inst_status", instance.getInstStatus());
            }
            if (StringUtil.isNotBlank(instance.getBizStatus())) {
                mq.likeNocase("biz_status", instance.getBizStatus());
            }
            if (StringUtil.isNotBlank(instance.getReqFrom())) {
                mq.likeNocase("req_from", instance.getReqFrom());
            }
            if (StringUtil.isNotBlank(instance.getBizKey1())) {
                mq.likeNocase("biz_key1", instance.getBizKey1());
            }
            if (StringUtil.isNotBlank(instance.getBizStatusDtl())) {
                mq.likeNocase("biz_status_dtl", instance.getBizStatusDtl());
            }
            if (sysNoList != null && sysNoList.size() > 0) {
                mq.in("serv_no", sysNoList);
            }
            if (instance.getEndTimeDate() != null && !"2".equals(instance.getInstStatus())) {
                mq.lteq("end_time", instance.getEndTimeDate());
            }
            if (instance.getStartTimeDate() != null) {
                mq.gteq("start_time", instance.getStartTimeDate());
            }
            return mq.orderBy("creation_date", "desc")
                    .doPageQuery(pageIndex, pageSize, DSGCLogInstance.class);

        } else {
            //存在关键字时
            String COL1 = "";
            String COL2 = "";
            String COL3 = "";
            String COL4 = "";
            String COL5 = "";
            String COL6 = "";
            String COL7 = "";
            String COL8 = "";
            String COL9 = "";
            String COLTEN = "";
            MpaasQuery mq = null;
            for (int i = 0; i < keyword.size(); i++) {
                String inputValue = keyword.get(i).get("inputValue");
                String busKeyword = keyword.get(i).get("busKeyword");
                switch (busKeyword) {
                    case "COL1":
                        COL1 = inputValue;
                        break;
                    case "COL2":
                        COL2 = inputValue;
                        break;
                    case "COL3":
                        COL3 = inputValue;
                        break;
                    case "COL4":
                        COL4 = inputValue;
                        break;
                    case "COL5":
                        COL5 = inputValue;
                        break;
                    case "COL6":
                        COL6 = inputValue;
                        break;
                    case "COL7":
                        COL7 = inputValue;
                        break;
                    case "COL8":
                        COL8 = inputValue;
                        break;
                    case "COL9":
                        COL9 = inputValue;
                        break;
                    case "COL10":
                        COLTEN = inputValue;
                        break;
                }
            }
            mq = sw.buildViewQuery("Log_Instance_view")
                    .setVar("COL1", COL1)
                    .setVar("COL2", COL2)
                    .setVar("COL3", COL3)
                    .setVar("COL4", COL4)
                    .setVar("COL5", COL5)
                    .setVar("COL6", COL6)
                    .setVar("COL7", COL7)
                    .setVar("COL8", COL8)
                    .setVar("COL9", COL9)
                    .setVar("COLTEN", COLTEN)
                    .setVar("servNo", instance.getServNo())
                    .eq("serv_no", instance.getServNo())
                    .likeNocase("serv_name", instance.getServName())
                    .likeNocase("token", instance.getToken())
                    .likeNocase("inst_status", instance.getInstStatus())
                    .likeNocase("biz_status", instance.getBizStatus())
                    .likeNocase("req_from", instance.getReqFrom())
                    .likeNocase("biz_key1", instance.getBizKey1())
                    .likeNocase("biz_status_dtl", instance.getBizStatusDtl())
                    .in("serv_no", sysNoList)
                    .lteq("end_time", instance.getEndTimeDate())
                    .gteq("start_time", instance.getStartTimeDate())
                    .orderBy("creation_date", "desc");

            return mq.doPageQuery(pageIndex, pageSize, DSGCLogInstance.class);

        }
    }

    public PageQueryResult<DSGCLogInstance> query1(String userRole, DSGCLogInstance instance, int pageSize, int pageIndex, DSGCSystemUser systemUser) throws Exception {
        MpaasQuery mq = null;
        //如果是管理员角色
        if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole) || "SystemLeader".equals(userRole)) {
            if ("SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)) {
                mq = sw.buildViewQuery("Administrators_view");

            } else if ("SystemLeader".equals(userRole)) {
                //如果当前人是系统管理员，且被授权管理系统
                if (null != systemUser) {
                    String sysCode = systemUser.getSysCode();
                    mq = sw.buildViewQuery("Have_Sys_SystemLeader_view")
                            //整合成视图查询
                            .setVar("sysCode", sysCode);
                } else {
                    //如果系统人员未被授权系统,则该系统管理员只能查看待办与自己创建的服务
                    //整合成视图查询
                    mq = sw.buildViewQuery("UnHave_Sys_SystemLeader_view")
                            .setVar("uId", MpaasSession.getCurrentUser());
                }
            }
        } else {
            //普通用户
            mq = sw.buildViewQuery("Tourist_view")
                    //整合成视图查询
                    .setVar("userId", MpaasSession.getCurrentUser());
        }

        mq.likeNocase("servNo", instance.getServNo())
                .likeNocase("servName", instance.getServName())
                .likeNocase("token", instance.getToken())
                .likeNocase("instStatus", instance.getInstStatus())
                .likeNocase("bizStatus", instance.getBizStatus())
                .likeNocase("reqFrom", instance.getReqFrom())
                .likeNocase("bizKey1", instance.getBizKey1())
                .lteq("endTime", instance.getEndTimeDate())
                .gteq("startTime", instance.getStartTimeDate())
                .orderBy("creationDate", "desc");
        return mq.doPageQuery(pageIndex, pageSize, DSGCLogInstance.class);
    }

    public DSGCLogInstance findLogById(DSGCLogInstance instance) {
        logger.debug(instance.toString());
        return sw.buildQuery().eq("trackId", instance.getTrackId()).doQueryFirst(DSGCLogInstance.class);
    }

    public DSGCLogInstance findLogById(String trackId) {
        return sw.buildQuery().eq("trackId", trackId).doQueryFirst(DSGCLogInstance.class);
    }

    public List<String> getLogPartition() {
        List<String> list = new ArrayList<>();
        String sql = null;
        if ("oracle".equals(dbType)) {
            sql = "select PARTITION_NAME from USER_TAB_PARTITIONS where TABLE_NAME = 'DSGC_LOG_INSTANCE'";

        }
        if ("mysql".equals(dbType)) {
            sql = "SELECT PARTITION_NAME FROM INFORMATION_SCHEMA.PARTITIONS WHERE TABLE_SCHEMA='dsgc' AND TABLE_NAME = 'DSGC_LOG_INSTANCE'";

        }
        List<Map<String, Object>> result = sw.buildQuery()
                .sql(sql)
                .doQuery();
        for (Map<String, Object> item : result) {
            if (item != null && !item.isEmpty() && item.get("PARTITION_NAME") != null) {
                list.add((String) item.get("PARTITION_NAME"));
            }

        }
        return list;
    }


    //    public void doRetry(JSONArray jsonArray) {
//        if (jsonArray != null && jsonArray.size() > 0) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject jo = jsonArray.getJSONObject(i);
//                sw.buildQuery()
//                        .sql("insert into dsgc_serv_retry_job (job_id,track_id,retry_system,status) values (#job_id,#track_id,#retry_system,#status)")
//                        .setVar("job_id", UUID.randomUUID().toString())
//                        .setVar("track_id", jo.getString("trackId"))
//                        .setVar("retry_system", jo.getString("sys"))
//                        .setVar("status", "W")
//                        .doQuery();
//            }
//        }
//    }
    public void doRetry(String uid, LogRetryReqDTO param) {
        List<LogRetryDTO> retryList = param.getRetryList();
        if (retryList != null && !retryList.isEmpty()) {
            for (LogRetryDTO r : retryList) {
                sw.buildQuery()
                        .sql("insert into dsgc_serv_retry_job (job_id,track_id,retry_system,status,created_by) values (#job_id,#track_id,#retry_system,#status,#uid)")
                        .setVar("job_id", UUID.randomUUID().toString())
                        .setVar("track_id", r.getTrackId())
                        .setVar("retry_system", r.getSys())
                        .setVar("status", "W")
                        .setVar("uid", uid)
                        .doQuery();
            }
        }
    }

    public List<DSGCLogAudit> getAuditLog(String trackId) {
        return sw.buildQuery()
                .eq("trackId", trackId)
                .doQuery(DSGCLogAudit.class);
    }

    public List<DSGCLogOutBound> getStackLog(String trackId) {
        return sw.buildQuery()
                .eq("trackId", trackId)
                .doQuery(DSGCLogOutBound.class);
    }

    public String getHeaderPayload(String trackId, String ibLob) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("SELECT PAYLOAD_DATA FROM DSGC_LOG_HEADER_PAYLOAD WHERE TRACK_ID = #TRACK_ID and PL_ID =#PL_ID")
                .setVar("TRACK_ID", trackId)
                .setVar("PL_ID", ibLob)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object PAYLOAD_DATA = item.get("PAYLOAD_DATA");
            if (PAYLOAD_DATA != null) {
                return PAYLOAD_DATA.toString();
                //            return StringUtils.ClobToString((Clob) PAYLOAD_DATA);
            }
        }
        return "";
    }

    public String getBodyPayload(String ibLob) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("SELECT PAYLOAD_DATA FROM DSGC_LOG_BODY_PAYLOAD WHERE PL_ID =#PL_ID")
                .setVar("PL_ID", ibLob)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object PAYLOAD_DATA = item.get("PAYLOAD_DATA");
            if (PAYLOAD_DATA != null) {
//                return PAYLOAD_DATA.toString();
                //   return StringUtils.ClobToString((Clob) PAYLOAD_DATA);
                return String.valueOf(PAYLOAD_DATA);
            }
        }
        return "";
    }

    public String getErrMsg(String ibLob) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("SELECT PAYLOAD_DATA FROM DSGC_LOG_ERROR_PAYLOAD WHERE PL_ID =#PL_ID")
                .setVar("PL_ID", ibLob)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object PAYLOAD_DATA = item.get("PAYLOAD_DATA");
            if (PAYLOAD_DATA != null) {
//                return PAYLOAD_DATA.toString();
                // return StringUtils.ClobToString((Clob) PAYLOAD_DATA);
                return String.valueOf(PAYLOAD_DATA);
            }
        }
        return "";
    }

    /**
     * 查询所有系统，返回系统编码和系统名称
     *
     * @return
     */
    public List<DSGCSystemEntities> getAllSystemCodeAndName() {
        return sw.buildQuery()
                .select("sys_code,sys_name")
                .doQuery(DSGCSystemEntities.class);
    }

    public List<DSGCSystemUser> querySystemlistUserByUserId(String uid) {
        return sw.buildQuery()
                .eq("user_id", uid)
                .doQuery(DSGCSystemUser.class);
    }

    public List<TopologyVO> getServNoBySystem(List<String> systenCode) {
        return sw.buildViewQuery("DSGC_TOPOLOGY_V").
                or()
                .in("subordinate_system", systenCode)
                .in("sys_code", systenCode)
                .doQuery(TopologyVO.class);
    }

    public FndProperties findFndPropertiesByKey(String key) {
        return sw.buildQuery()
                .eq("property_key", key)
                .doQueryFirst(FndProperties.class);
    }

    public List<RetryJobDTO> getRetryDetial(String trackId) {
        return sw.buildQuery()
                .eq("track_id", trackId)
                .doQuery(RetryJobDTO.class);
    }

    public String getReqBodyRetry(String jobId) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("select * from DSGC_SERV_RETRY_JOB where job_id = #jobId")
                .setVar("jobId", jobId)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object REQ_CONTENT = item.get("REQ_CONTENT");
            if (REQ_CONTENT != null) {
                return StringUtils.ClobToString((Clob) REQ_CONTENT);
            }
        }
        return "";
    }

    public String getResBodyRetry(String jobId) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("select * from DSGC_SERV_RETRY_JOB where job_id = #jobId")
                .setVar("jobId", jobId)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object RES_CONTENT = item.get("RES_CONTENT");
            if (RES_CONTENT != null) {
                return StringUtils.ClobToString((Clob) RES_CONTENT);
            }
        }
        return "";
    }

    public String getErrMsgRetry(String jobId) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("select * from DSGC_SERV_RETRY_JOB where job_id = #jobId")
                .setVar("jobId", jobId)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object ERROR_MSG = item.get("ERROR_MSG");
            if (ERROR_MSG != null) {
                return StringUtils.ClobToString((Clob) ERROR_MSG);
            }
        }
        return "";
    }

    public Map<String, Object> getUserName(String uid) {
        return sw.buildQuery()
                .sql("select USER_NAME from dsgc_user where user_id = #uid")
                .setVar("uid", uid)
                .doQueryFirst();
    }

    public List<Map<String, String>> queryLogMark(String trackId, List<FndLookupValue> tagTypes, Integer runTimes) {
        List<Map<String, String>> result = new ArrayList<>();

        List<DSGCLogInstanceTag> logInstanceTags = sw.buildQuery().eq("track_id", trackId).doQuery(DSGCLogInstanceTag.class);
        for (DSGCLogInstanceTag item : logInstanceTags) {
            for (int i = 0; i < tagTypes.size(); i++) {
                if (item.getTagCode().equals(tagTypes.get(i).getLookupCode())) {
                    Map<String, String> tag = new HashMap<>();
                    tag.put("meaning", tagTypes.get(i).getMeaning());
                    tag.put("tagColor", tagTypes.get(i).getTag());
                    tag.put("tagCode", item.getTagCode());
                    tag.put("tagDesc", item.getTagDesc());
                    result.add(tag);
                }
            }
        }
        for (int i = 0; i < tagTypes.size(); i++) {
            if ("RETRY".equals(tagTypes.get(i).getLookupCode()) && runTimes > 1) {
                Map<String, String> retryTag = new HashMap<>();
                retryTag.put("meaning", tagTypes.get(i).getMeaning());
                retryTag.put("tagColor", tagTypes.get(i).getTag());
                retryTag.put("tagCode", "RETRY");
                retryTag.put("tagDesc", "");
                result.add(retryTag);
                break;
            }
        }
        return result;
    }
}
