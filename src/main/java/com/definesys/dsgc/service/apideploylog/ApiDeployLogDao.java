package com.definesys.dsgc.service.apideploylog;

import com.definesys.dsgc.service.apideploylog.bean.CommonReqBean;
import com.definesys.dsgc.service.apideploylog.bean.DagDeployLogBean;
import com.definesys.dsgc.service.apideploylog.bean.DagDeployStatBean;
import com.definesys.dsgc.service.users.bean.DSGCUser;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiDeployLogDao {
    @Autowired
    private MpaasQueryFactory sw;
    public List<DagDeployStatBean> queryDeploySurvey(CommonReqBean param){
        return sw.buildQuery().sql("  select t.*,deic.env_name envName from (select des.*,dcv.v_name vName from dag_deploy_stat  des,dag_code_version dcv where dcv.sour_type =#queryType " +
                " and dcv.vid = des.vid and dcv.sour_code = #sourCode) t,dag_env_info_cfg deic where t.env_code = deic.env_code")
                .setVar("queryType",param.getQueryType())
                .setVar("sourCode",param.getCon0())
                .doQuery(DagDeployStatBean.class);
    }
    public PageQueryResult<DagDeployLogBean> queryDeployHis(CommonReqBean param, int pageIndex, int pageSize){
        return sw.buildQuery().sql(" select t.*,deic.env_name envName from (select del.* from dag_deploy_log del,dag_code_version dcv " +
                "  where dcv.sour_type =#queryType and dcv.vid = del.vid and dcv.sour_code = #sourCode) t,dag_env_info_cfg deic where t.env_code = deic.env_code")
                .setVar("queryType",param.getQueryType())
                .setVar("sourCode",param.getCon0())
                .doPageQuery(pageIndex,pageSize,DagDeployLogBean.class);
    }

    public DSGCUser queryUserNameById(String userId){
        return sw.buildQuery().eq("user_id",userId).doQueryFirst(DSGCUser.class);
    }
}
