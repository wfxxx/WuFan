package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.svcmng.bean.DeployedEnvInfoBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiRouteDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;
    public PageQueryResult queryApiRouteList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList) {
        StringBuffer sqlStr = null;
        StringBuffer sqlend = null;
        if("oracle".equals(dbType)){
            sqlStr = new StringBuffer("  select * from (                               \n" +
                    "    select t.*,row_number() over(partition by t.route_code order by 1) as rowrid from \n" +
                    "    (    " +
                    "         select  db.*,dse.sys_name appName,stat.env_code from dag_routes db  \n" +
                    "         left join dsgc_system_entities dse on   db.app_code = dse.sys_code\n" +
                    "         left join  dag_code_version ver on db.route_code =ver.sour_code\n" +
                    "         left join  DAG_DEPLOY_STAT stat on stat.vid =ver.vid\n" +
                    "        where 1=1 \n"
            );
            sqlend =new StringBuffer( ") t \n" +
                    "       ) where rowrid=1");

        }
        if ("mysql".equals(dbType)){
           sqlStr = new StringBuffer("SELECT t1.*,t2.env_code FROM ( SELECT dr.*, dse.sys_name appName FROM dag_routes dr,dsgc_system_entities dse WHERE 1 = 1 \n" +
                    "AND dr.app_code = dse.sys_code ) t1,( SELECT *  FROM (SELECT t.*,@row_number := CASE WHEN @customer_no = t.sour_code THEN\n" +
                    "@row_number + 1 ELSE 1 END AS rowrid,@customer_no := t.sour_code temp FROM (SELECT v.vid,v.sour_code,stat.env_code FROM\n" +
                    "dag_code_version v LEFT JOIN ( SELECT t.vid, convert( group_concat( t.env_code ) ,char) AS env_code FROM DAG_DEPLOY_STAT t GROUP BY t.vid ) stat ON stat.vid = v.vid ) t, ( SELECT @row_number := 0, @customer_no := 0 ) AS m) s WHERE s.rowrid = 1 ) t2 WHERE\n" +
                    "1 = 1 AND t1.route_code = t2.sour_code ");
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
            sqlStr.append(" order by creation_date desc");
        }
        if("mysql".equals(dbType)){
            sqlStr.append(" GROUP BY dr_id   HAVING COUNT(1)>1  order by t1.creation_date desc ");
        }
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex, pageSize, DagRoutesBean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(t1.route_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.bs_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.route_path) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.route_method) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.route_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(t1.appName) like '%" + conUpper + "%' )";

        return conAnd;
    }
    public void addApiRoute(DagRoutesBean dagRoutesBean){
        sw.buildQuery().doInsert(dagRoutesBean);
    }
    public DagRoutesBean queryApiRouteById(CommonReqBean param){
        return sw.buildQuery().eq("dr_id",param.getCon0()).doQueryFirst(DagRoutesBean.class);
    }
    public void delApiRoute(CommonReqBean param){
        sw.buildQuery().eq("dr_id",param.getCon0()).doDelete(DagRoutesBean.class);
    }
    public DagRoutesBean queryRouteDetail(String  routeCode){
        return  sw.buildQuery().sql("select dr.*,dse.sys_name appName from dag_routes dr,dsgc_system_entities dse where dr.app_code = dse.sys_code and dr.route_code = #routeCode")
                .setVar("routeCode",routeCode)
                .doQueryFirst(DagRoutesBean.class);
    }
    public List<DSGCEnvInfoCfg> queryApiEnv(){
        return sw.buildQuery().eq("env_type","DAG").doQuery(DSGCEnvInfoCfg.class);
    }
    public List<DagCodeVersionBean> queryApiCodeVersion(CommonReqBean param){
        return sw.buildQuery().eq("sour_code",param.getCon0()).eq("sour_type",param.getQueryType()).doQuery(DagCodeVersionBean.class);
    }
    public DagCodeVersionBean queryCodeVersionById(CommonReqBean param){
        return sw.buildQuery().eq("vid",param.getCon0()).doQueryFirst(DagCodeVersionBean.class);
    }
    public List<DagCodeVersionBean> queryRouteConfigListBySourCode(String sourCode,String sourType){
        return sw.buildQuery().eq("sour_code",sourCode).eq("sour_type",sourType).doQuery(DagCodeVersionBean.class);
    }
    public void addRouteConfig(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().doInsert(dagCodeVersionBean);
    }
    public void updateRouteConfig(DagCodeVersionBean dagCodeVersionBean){
        sw.buildQuery().eq("vid",dagCodeVersionBean.getVid()).update("v_name",dagCodeVersionBean.getvName()).update("env_targets",dagCodeVersionBean.getEnvTargets()).doUpdate(DagCodeVersionBean.class);
    }
    public void updateRoutePathStrip(DagRoutesBean param){
        sw.buildQuery().eq("route_code",param.getRouteCode()).update("strip_path",param.getStripPath()).doUpdate(DagRoutesBean.class);
    }
    public void updateRouteDesc(DagRoutesBean param){
        sw.buildQuery().eq("route_code",param.getRouteCode()).update("route_desc",param.getRouteDesc()).doUpdate(DagRoutesBean.class);
    }
    public DagCodeVersionBean queryRouteConfigByid(String vid){
        return sw.buildQuery().eq("vid",vid).doQueryFirst(DagCodeVersionBean.class);
    }
    public void delRouteConfig(String id){
        sw.buildQuery().eq("vid",id).doDelete(DagCodeVersionBean.class);
    }

    public PageQueryResult<DagPluginUsingBean> queryRoutePlug(String vid,int pageIndex,int pageSize){
        return sw.buildQuery().sql("select dpu.*,dps.plugin_name pluginName from dag_plugin_using dpu,dag_plugin_store dps where dpu.plugin_code = dps.plugin_code and dpu.vid = #vid")
                .setVar("vid",vid).doPageQuery(pageIndex,pageSize,DagPluginUsingBean.class);
    }
    public void addRoutePlugin(DagPluginUsingBean pluginUsingBean){
        sw.buildQuery().doInsert(pluginUsingBean);
    }
    public void delRoutePlugin(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(DagPluginUsingBean.class);
    }
    public void delRouteAnotherRuleById(String vid){
        sw.buildQuery().eq("vid",vid).doDelete(DagRoutesHostnameBean.class);
    }

    public Boolean queryRouteConfigIsDeployed(String vid){
       List<DagDeployStatBean>  result = sw.buildQuery().eq("vid",vid).doQuery(DagDeployStatBean.class);
       if(result != null && result.size() > 0){
           return true;
       }else {
           return false;
       }
    }

    public List<DagRoutesHostnameBean> queryRouteAnotherRule(CommonReqBean param){
        return sw.buildQuery().eq("vid",param.getCon0()).doQuery(DagRoutesHostnameBean.class);
    }
    public List<DagPluginUsingBean> queryRoutePlugByVid(String vid){
        return sw.buildQuery().eq("vid",vid).doQuery(DagPluginUsingBean.class);
    }
    public void addRouteAnotherRule(DagRoutesHostnameBean dagRoutesHostnameBean){
        sw.buildQuery().doInsert(dagRoutesHostnameBean);
    }
    public DagRoutesHostnameBean queryRouteDomainById(String drhId){
        return sw.buildQuery().eq("drh_id",drhId).doQueryFirst(DagRoutesHostnameBean.class);
    }
    public void delRouteDomain(String drhId){
        sw.buildQuery().eq("drh_id",drhId).doDelete(DagRoutesHostnameBean.class);
    }
    public void addDagDeployStat(DagDeployStatBean dagDeployStatBean){
        sw.buildQuery().doInsert(dagDeployStatBean);
    }
    public Boolean queryDagDeployStatIsExist(DagDeployStatBean dagDeployStatBean){
      DagDeployStatBean dagDeployStatBean1 =  sw.buildQuery()
              .eq("vid",dagDeployStatBean.getVid())
              .eq("env_code",dagDeployStatBean.getEnvCode())
              .doQueryFirst(DagDeployStatBean.class);
      if(dagDeployStatBean1 != null){
          return true;
      }else {
          return false;
      }
    }

    public void delDagDeployStat(DagDeployStatBean dagDeployStatBean){
        sw.buildQuery().eq("vid",dagDeployStatBean.getVid()).eq("env_code",dagDeployStatBean.getEnvCode()).doDelete(DagDeployStatBean.class);
    }
    public void addDagDeployLog(DagDeployLogBean dagDeployLogBean){
        sw.buildQuery().doInsert(dagDeployLogBean);
    }

    public Boolean checkRouteCodeIsExsit(String routeCode){
        DagRoutesBean dagRoutesBean = sw.buildQuery().eq("route_code",routeCode).doQueryFirst(DagRoutesBean.class);
        if (dagRoutesBean == null){
            return false;
        }else {
            return true;
        }
    }


    public List<DeployedEnvInfoBean> queryDeplogDev(String code) {
        return sw.buildQuery().sql("       select env.env_code,env.env_name from dag_code_version ver,DAG_DEPLOY_STAT stat,DSGC_ENV_INFO_CFG env \n" +
                "              where ver.sour_code =#code  and stat.vid =ver.vid\n" +
                "              and  stat.env_code =env.env_code")
                .setVar("code", code)
                .doQuery(DeployedEnvInfoBean.class);
    }

    public DagRoutesBean queryRouteByUri(String uri){
        return sw.buildQuery().eq("route_path",uri).doQueryFirst(DagRoutesBean.class);
    }

    public DagDeployStatBean queryDeployVid(String sourCode,String envCode){
       return sw.buildQuery().sql("select dds.* from dag_deploy_stat dds,dag_code_version dcv where dds.vid = dcv.vid and dcv.sour_type = 'route' and dcv.sour_code = #sourCode and dds.env_code = #envCode")
                .setVar("sourCode",sourCode).setVar("envCode",envCode)
                .doQueryFirst(DagDeployStatBean.class);

    }
}
