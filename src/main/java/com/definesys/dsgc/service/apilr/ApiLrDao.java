package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.*;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiLrDao {
    @Autowired
    private MpaasQueryFactory sw;

    public List<DSGCSystemUser> findUserSystemByUserId(String userId){
        return sw.buildQuery().eq("user_id",userId).doQuery(DSGCSystemUser.class);
    }

    public PageQueryResult queryApiLrList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer("select db.*,dse.sys_name appName from DAG_LR db,dsgc_system_entities dse where 1=1 and db.app_code = dse.sys_code ");
        MpaasQuery mq = sw.buildQuery();
        if ("SystemLeader".equals(userRole)&&sysCodeList.size()>0) {
            sqlStr.append(" and db.app_code in ( ");
            for (int i = 0; i < sysCodeList.size(); i++) {
                if (i < sysCodeList.size() - 1) {
                    sqlStr.append("'" + sysCodeList.get(i) + "',");
                } else {
                    sqlStr.append("'" + sysCodeList.get(i) + "') ");
                }
            }
        }
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
        mq.sql(sqlStr.toString()+"order by db.creation_date desc");
        return mq.doPageQuery(pageIndex, pageSize, DagLrbean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(db.lr_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(db.lr_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void addApiLr(DagLrbean dagLrbean){
        sw.buildQuery().doInsert(dagLrbean);
    }

    public void delApiLr(CommonReqBean param){
        sw.buildQuery().eq("dl_id",param.getCon0()).doDelete(DagLrbean.class);
    }

    public DagLrbean queryApiLrById(CommonReqBean param){
        return sw.buildQuery().eq("dl_id",param.getCon0()).doQueryFirst(DagLrbean.class);
    }

    public DagLrbean queryLrDetail(String param){
        return  sw.buildQuery().sql("select dr.*,dse.sys_name appName from DAG_LR dr,dsgc_system_entities dse where dr.APP_CODE = dse.sys_code and dr.LR_NAME = #lrName\n")
                .setVar("lrName",param)
                .doQueryFirst(DagLrbean.class);
    }

    public List<DagCodeVersionBean> queryLrConfigListBySourCode(CommonReqBean param){
        return sw.buildQuery().eq("sour_code",param.getCon0()).eq("sour_type",param.getQueryType()).orderBy("creationDate","desc").doQuery(DagCodeVersionBean.class);
    }

    public void addLrConfig(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().doInsert(dagCodeVersionBean);
    }

    public void deLrConfig(CommonReqBean param){
        sw.buildQuery().eq("vid",param.getCon0()).doDelete(DagCodeVersionBean.class);
    }

    public void updateLrDesc(DagLrbean param){
        sw.buildQuery().eq("lr_name",param.getLrName()).update("lr_desc",param.getLrDesc()).doUpdate(DagLrbean.class);
    }
    public List<DsgcEnvInfoCfgBean> queryApiEnvName(String[] envArr){
        return sw.buildQuery().sql("select env_name from DSGC_ENV_INFO_CFG ")
                .in("envCode",envArr)
                .doQuery(DsgcEnvInfoCfgBean.class);
    }
    public void addLrTarget(DagLrTargetBean dagLrTargetBean){
        sw.buildQuery().doInsert(dagLrTargetBean);
    }
    public void delLrTarget(CommonReqBean param){
        sw.buildQuery().eq("vid",param.getCon0()).doDelete(DagLrTargetBean.class);
    }
    public List<DagLrTargetBean> queryLrTarget(String vid){
        return sw.buildQuery().eq("vid",vid).doQuery(DagLrTargetBean.class);
    }

    public void updateLrConfig(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().rowid("vid",dagCodeVersionBean.getVid()).doUpdate(dagCodeVersionBean);
    }

    public void copyLrTargetList(DagLrTargetBean dagLrTargetBean){
        sw.buildQuery().doInsert(dagLrTargetBean);
    }
}
