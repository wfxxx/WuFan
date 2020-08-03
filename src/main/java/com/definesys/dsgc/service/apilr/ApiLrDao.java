package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.*;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcmng.bean.DeployedEnvInfoBean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiLrDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;

    public List<DSGCSystemUser> findUserSystemByUserId(String userId){
        return sw.buildQuery().eq("user_id",userId).doQuery(DSGCSystemUser.class);
    }

    public PageQueryResult queryApiLrList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        StringBuffer sqlStr = null;
        StringBuffer sqlend = null;
        if("oracle".equals(dbType)){
            sqlStr = new StringBuffer("  select * from (                               \n" +
                    "    select t.*,row_number() over(partition by t.lr_name order by 1) as rowrid from \n" +
                    "    (    " +
                    "         select  db.*,dse.sys_name appName,stat.env_code from dag_lr db  \n" +
                    "         left join dsgc_system_entities dse on   db.app_code = dse.sys_code\n" +
                    "         left join  dag_code_version ver on db.lr_name =ver.sour_code\n" +
                    "         left join  DAG_DEPLOY_STAT stat on stat.vid =ver.vid\n" +
                    "        where 1=1 \n"
            );
            sqlend =new StringBuffer( ") t \n" +
                    "       ) where rowrid=1");
        }
        if ("mysql".equals(dbType)){
            sqlStr = new StringBuffer("SELECT t1.*, t2.env_code FROM ( SELECT db.*,dse.sys_name appName FROM DAG_LR db,dsgc_system_entities dse WHERE 1 = 1 AND db.app_code = dse.sys_code ) t1,(SELECT *  FROM (SELECT t.*,@row_number :=CASE WHEN @customer_no = t.sour_code THEN @row_number + 1 ELSE 1 \n" +
                    "END AS rowrid,@customer_no := t.sour_code temp FROM (SELECT v.vid,v.sour_code,stat.env_code FROM dag_code_version v\n" +
                    "LEFT JOIN ( SELECT t.vid, CONVERT( group_concat( t.env_code ),char) AS env_code FROM DAG_DEPLOY_STAT t GROUP BY t.vid ) stat ON stat.vid = v.vid ) t, ( SELECT @row_number := 0, @customer_no := 0 ) AS m ) s WHERE s.rowrid = 1 ) t2 \n" +
                    "WHERE 1 = 1 AND t1.lr_name = t2.sour_code");
        }
        MpaasQuery mq = sw.buildQuery();
        if ("SystemLeader".equals(userRole)&&sysCodeList.size()>0) {
            sqlStr.append(" and t1.app_code in ( ");
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
        if(param.getQueryType().equals("ALL")){
        } else if(param.getQueryType().equals("notDeploy")){
            sqlStr.append("and (env_code is null or env_code ='')");
        } else{
            String[] conArray = param.getQueryType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and  env_code like '%" + s + "%'");
                }
            }
        }
        if("oracle".equals(dbType)){
            sqlStr.append(sqlend);
            mq.sql(sqlStr.toString()+" order by creation_date desc");
        }
        if ("mysql".equals(dbType)){
            mq.sql(sqlStr.toString()+" GROUP BY dl_id   HAVING COUNT(1)>1  order by t1.creation_date desc");
        }

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


    public List<DeployedEnvInfoBean> queryDeplogDev(String code){
        return sw.buildQuery().sql("       select env.env_code,env.env_name from dag_code_version ver,DAG_DEPLOY_STAT stat,DSGC_ENV_INFO_CFG env \n" +
                "              where ver.sour_code =#code  and stat.vid =ver.vid\n" +
                "              and  stat.env_code =env.env_code" +
                "              and ver.sour_type='lr'")
                .setVar("code",code)
                .doQuery(DeployedEnvInfoBean.class);
    }

    public Boolean checkLrTargetIsExist(String dltId){
        DagLrTargetBean dagLrTargetBean = sw.buildQuery().eq("dlt_id",dltId).doQueryFirst(DagLrTargetBean.class);
        return dagLrTargetBean == null? false:true;
    }

    public void delTarget(String dltId){
        sw.buildQuery().eq("dlt_id",dltId).doDelete(DagLrTargetBean.class);
    }
}
