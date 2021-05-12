package com.definesys.dsgc.service.dess.DessLog;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLogPayload;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Clob;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DLogDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:30
 * @Version 1.0
 **/
@Repository
public class DLogDao {

    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;

    public PageQueryResult queryJobLogList(CommonReqBean param, int pageSize, int pageIndex) {
        StringBuffer sqlStr = null;
        if ("oracle".equals(dbType)) {
            sqlStr = new StringBuffer("select dl.*,di.JOB_NAME,di.BUSINESS_ID,db.BUSINESS_TYPE from dess_log dl LEFT JOIN DESS_INSTANCE di on dl.JOB_NO = di.JOB_NO left join DESS_BUSINESS db on di.BUSINESS_ID = db.BUSINESS_ID where 1=1 ");
        } else if ("mysql".equals(dbType)) {
            sqlStr = new StringBuffer("select dl.*,di.JOB_NAME,di.BUSINESS_ID,db.BUSINESS_TYPE from dess_log dl LEFT JOIN DESS_INSTANCE di on dl.JOB_NO = di.JOB_NO left join DESS_BUSINESS db on di.BUSINESS_ID = db.BUSINESS_ID where 1=1 ");
        }
        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluseLog(s));
                }
            }
        }
        if (param.getQueryType().equals("ALL")) {
        } else {
            String[] conArray = param.getQueryType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and  db.BUSINESS_TYPE like '%" + s + "%'");
                }
            }
        }
        if ("oracle".equals(dbType)) {
            mq.sql(sqlStr.toString() + " order by dl.creation_date desc");
        } else if ("mysql".equals(dbType)) {
            mq.sql(sqlStr.toString() + " GROUP BY dl.log_id HAVING COUNT(1)> 0  order by dl.creation_date desc");
        }
        return mq.doPageQuery(pageIndex, pageSize, DessLog.class);
    }

    private String generateLikeAndCluseLog(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(dl.log_id) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dl.job_no) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(di.job_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public DessLogPayload findJobLogDetailByIdSwitch(String logId) {
        return sw.buildQuery()
                .eq("LOG_ID", logId)
                .doQueryFirst(DessLogPayload.class);
    }

    public List<Map<String, Object>> getUrl(String logId) {
        return sw.buildQuery()
                .sql("select * from (select dl.log_id,db.INVOKE_URL from DESS_LOG dl,DESS_INSTANCE di,DESS_BUSINESS db where dl.JOB_NO = di.JOB_NO and di.BUSINESS_ID  = db.BUSINESS_ID) log ")
                .eq("LOG_ID", logId)
                .doQuery();
    }

    public String getBodyPayload(String logId) {
        List<Map<String, Object>> result = this.sw.buildQuery()
                .sql("select * from DESS_LOG_PAYLOAD where log_id = #logId")
                .setVar("logId", logId)
                .doQuery();
        for (Map<String, Object> item : result) {
            Object BODY_PAYLOAD = item.get("BODY_PAYLOAD");
            if (BODY_PAYLOAD != null) {
                return StringUtils.ClobToString((Clob) BODY_PAYLOAD);
            }
        }
        return "";
    }

    public DessLog queryDessLogById(String logId) {
        return this.sw.buildQuery().eq("logId", logId).doQueryFirst(DessLog.class);
    }
}
