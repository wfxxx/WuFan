package com.definesys.dsgc.service.ystar.svcgen.svcdpl;

import com.definesys.dsgc.service.projdir.bean.DSGCSysProfileDir;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLog;
import com.definesys.dsgc.service.ystar.svcgen.svcdpl.bean.SvcGenDeployLogVO;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SvggenDeployLogDao
 * @Description: TODO
 * @Author：afan
 * @Date : 2020/1/11 11:09
 */
@Repository("svcGenDeployLogDao")
public class SvcGenDeployLogDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void insertDeployLog(SvcGenDeployLog svcgenDeployLog) {
        sw.buildQuery().doInsert(svcgenDeployLog);
    }


    public List<SvcGenDeployLog> queryDeployLog(String vId, String projId) {
        List<SvcGenDeployLog> QueryResult = sw.buildQuery()
                .eq("V_Id", vId)
                .eq("PROJ_ID", projId)
                .doQuery(SvcGenDeployLog.class);
        return QueryResult;
    }

    public String queryDeployMaxVersion(String projId) {
        List<Map<String, Object>> res = sw.buildQuery().sql("select max(V_ID) V_ID from dsgc_svcgen_deploy_log  where PROJ_ID =#projId")
                .setVar("projId", projId)
                .doQuery();
        if (res != null && res.size() > 0) {
            Map<String, Object> map = res.get(0);
            if (map != null) {
                return map.get("V_ID").toString();
            }
        }
        return null;
    }

    public void updateDeployLog(String vId) {
        sw.buildQuery().eq("vId", vId).update("DPL_STATUS", "Y").doUpdate(SvcGenDeployLog.class);
    }

    public SvcGenDeployLog updateDeployLogDate(String proId, String vId, String userId, String dplMsg, String timestamp) {
        SvcGenDeployLog log = sw.buildQuery().eq("proj_id", proId).eq("V_ID", vId).doQueryFirst(SvcGenDeployLog.class);
        if (log != null) {
            sw.buildQuery().eq("LOG_ID", log.getLogId())
                    .update("DPL_DATE", timestamp)
                    .update("USER_CODE", userId)
                    .update("DPL_MSG", dplMsg)
                    .doUpdate(SvcGenDeployLog.class);
        }
        return log;
    }

    public List<DSGCSysProfileDir> queryProjectInfo() {
        return sw.buildQuery().doQuery(DSGCSysProfileDir.class);
    }

    public PageQueryResult<SvcGenDeployLogVO> pageQueryDeployLog(SvcGenDeployLog service, int pageSize, int pageIndex) {
        return sw.buildViewQuery("svcgen_deploy_log")
                .eq("ENV_CODE", service.getEnvCode())
                .eq("USER_CODE", service.getUserCode())
                .orderBy("DPL_DATE", "DESC")
                .doPageQuery(pageIndex, pageSize, SvcGenDeployLogVO.class);
    }
}
