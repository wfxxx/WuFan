package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.*;
import com.definesys.dsgc.service.apiroute.bean.DagEnvInfoCfgBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiBsDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;
    public PageQueryResult queryApiBsList(CommonReqBean param, int pageSize, int pageIndex, String userRole,List<String> sysCodeList){
        StringBuffer sqlStr = null;
        if("oracle".equals(dbType)){
             sqlStr = new StringBuffer("select t1.*,t2.env_code from \n" +
                    "        (select db.*,dse.sys_name appName from dag_bs db,dsgc_system_entities dse where 1=1 and db.app_code = dse.sys_code) t1,\n" +
                    "        (select * from( \n" +
                    "select t.*,row_number() over(partition by sour_code order by env_code) as rowrid   from(    \n" +
                    "  select v.vid,v.sour_code,stat.env_code \n" +
                    "  from dag_code_version v \n" +
                    "  left join (select t.vid,to_char(wm_concat(t.env_code)) as env_code from DAG_DEPLOY_STAT t  group by t.vid)  stat on stat.vid=v.vid  ) t) s where s.rowrid=1) t2\n" +
                    "        where 1=1 and  t1.bs_code=t2.sour_code ");
        }
    if ("mysql".equals(dbType)){
        sqlStr = new StringBuffer("SELECT t1.*,t2.env_code FROM (" +
                "SELECT db.*,dse.sys_name appName FROM dag_bs db,dsgc_system_entities dse WHERE 1 = 1 AND db.app_code = dse.sys_code ) t1," +
                "(SELECT * FROM(SELECT t.*,@row_number :=CASE WHEN @customer_no = t.sour_code THEN @row_number + 1 ELSE 1 END AS rowrid," +
                "@customer_no := t.sour_code temp FROM (SELECT v.vid,v.sour_code,stat.env_code FROM dag_code_version v " +
                "LEFT JOIN ( SELECT t.vid, CONVERT( group_concat( t.env_code ),char ) AS env_code FROM DAG_DEPLOY_STAT t GROUP BY t.vid ) stat ON stat.vid = v.vid ) t, ( SELECT @row_number := 0, @customer_no := 0 ) AS m ) s WHERE s.rowrid = 1 ) t2 " +
                "WHERE 1 = 1 AND t1.bs_code = t2.sour_code ");
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
            mq.sql(sqlStr.toString()+" order by t1.creation_date desc");
        }
        if ("mysql".equals(dbType)){
            mq.sql(sqlStr.toString()+" GROUP BY bs_id   HAVING COUNT(1)>1  order by t1.creation_date desc");
        }
        return mq.doPageQuery(pageIndex, pageSize, DagBsbean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(t1.bs_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.bs_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.appName) like '%" + conUpper + "%' )";

        return conAnd;
    }

    public DagBsbean checkSame(DagBsbean bean){
        return sw.buildQuery().eq("bs_code",bean.getBsCode()).doQueryFirst(DagBsbean.class);
    }


    public List<DagBsbean> queryApiBsByCustomInput(CommonReqBean param){
      return sw.buildQuery().like("bs_code",param.getCon0()).doQuery(DagBsbean.class);
    }

    public DagBsbean queryApiBsByCode(String code){
        return sw.buildQuery().like("bs_code",code).doQueryFirst(DagBsbean.class);
    }

    public void updateDagBs(DagBsbean dagBsbean){
        sw.buildQuery().rowid("bs_id",dagBsbean.getBsId()).doUpdate(dagBsbean);

    }

    public List<Map<String,Object>> queryDeployByVid(String vid){
       return  sw.buildQuery().sql("select t.vid from DAG_DEPLOY_STAT t where t.vid=#vid").setVar("vid",vid).doQuery();
    }

    public void addApiBs(DagBsbean dagBsbean){
        sw.buildQuery().doInsert(dagBsbean);
    }
    public DagBsbean queryApiBsById(CommonReqBean param){
        return sw.buildQuery().eq("bs_id",param.getCon0()).doQueryFirst(DagBsbean.class);
    }
    public void delApiBs(CommonReqBean param){
         sw.buildQuery().eq("bs_id",param.getCon0()).doDelete(DagBsbean.class);
    }

    public DagBsDtiBean queryDagBsDtiByVid(String params){
        return sw.buildQuery().eq("vid",params).doQueryFirst(DagBsDtiBean.class);
    }

    public DagBsDtiBean queryDagBsDtiByid(String params){
        return sw.buildQuery().eq("dbd_id",params).doQueryFirst(DagBsDtiBean.class);
    }

    public DagBsDtiBean updateDagBsDti(DagBsDtiBean dagBsDtiBean){
        sw.buildQuery().eq("dbd_id",dagBsDtiBean.getDbdId()).doUpdate(dagBsDtiBean);
        return queryDagBsDtiByid(dagBsDtiBean.getDbdId());
    }

    public void delDagBsDtiByVid(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(DagBsDtiBean.class);
    }

    public DagBsDtiBean addDagBsDti(DagBsDtiBean dagBsDtiBean){
         sw.buildQuery().doInsert(dagBsDtiBean);
        return queryDagBsDtiByid(dagBsDtiBean.getDbdId());
    }


    public void updatePluginUsing(DagPlugUsingBean dagPlugUsingBean){
        sw.buildQuery().eq("dpu_Id",dagPlugUsingBean.getDpuId()).doUpdate(dagPlugUsingBean);
    }

    public DagPlugUsingBean addPluginUsing(DagPlugUsingBean dagPlugUsingBean){
        sw.buildQuery().doInsert(dagPlugUsingBean);
        return dagPlugUsingBean;
    }

    public void delPluginUsing(String id){
        sw.buildQuery().rowid("dpu_id", id).doDelete(DagPlugUsingBean.class);
    }

    public void delPluginUsingByVid(String vid){
        sw.buildQuery().eq("vid", vid).doDelete(DagPlugUsingBean.class);
    }


    public DagPlugUsingBean queryPluginUsingByid(String id){
        return sw.buildQuery().eq("dpu_id",id).doQueryFirst(DagPlugUsingBean.class);
    }

    public List<DagPlugUsingBean> queryPluginUsing(String vid){
        return sw.buildQuery().eq("vid",vid).orderBy("creationDate","desc").doQuery(DagPlugUsingBean.class);
    }

    public List<DagPlugStoreBean> queryPluginStoreByType(String type){
        return sw.buildQuery().eq("plugin_type",type).doQuery(DagPlugStoreBean.class);
    }

    public DagPlugStoreBean queryPluginStoreByCode(String code){
        return sw.buildQuery().eq("plugin_code",code).doQueryFirst(DagPlugStoreBean.class);
    }

    public List<Map<String,Object>> queryPluginStoreTypeTotal(){
        return sw.buildQuery().sql("select t.plugin_type from dag_plugin_store t group by t.plugin_type").doQuery();
    }

    public List<DagCodeVersionBean> queryDagCodeVersionBySource(String sourceId){
        return sw.buildQuery().eq("sour_code",sourceId).doQuery(DagCodeVersionBean.class);
    }

    public DagCodeVersionBean queryDagCodeVersionByid(String id){
        return sw.buildQuery().eq("vid",id).doQueryFirst(DagCodeVersionBean.class);
    }


    public void delDagCodeVersionByid(String id){
         sw.buildQuery().rowid("vid",id).doDelete(DagCodeVersionBean.class);
    }

    public DagCodeVersionBean updateDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().rowid("vid",dagCodeVersionBean.getVid()).doUpdate(dagCodeVersionBean);
        return queryDagCodeVersionByid(dagCodeVersionBean.getVid());
    }

    public String addDagCodeVersion(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().doInsert(dagCodeVersionBean);
        return dagCodeVersionBean.getVid();
    }



    public Map<String,Object> querySysNameByCode(String code){
        return  sw.buildQuery().sql("select e.sys_name from DSGC_SYSTEM_ENTITIES e where e.sys_code=#code")
                .setVar("code",code).doQueryFirst();
    }

    public List<DagEnvInfoCfgBean> queryEnv(List<String> envCode){
        return  sw.buildQuery().in("envCode",envCode).doQuery(DagEnvInfoCfgBean.class);
    }

    public List<DSGCEnvInfoCfg> queryDeplogDev(String envCode){
        String[] conArray = envCode.trim().split(",");
        return sw.buildQuery().in("ENV_CODE",conArray).doQuery(DSGCEnvInfoCfg.class);
    }

    public void updatePluginUsingConsumer(String consumer,String dupId){
        sw.buildQuery()
                .update("consumer",consumer)
                .eq("dpu_id",dupId)
                .doUpdate(DagPlugUsingBean.class);
    }


}
