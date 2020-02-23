package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApiRouteDao {
    @Autowired
    private MpaasQueryFactory sw;
    public PageQueryResult queryApiRouteList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList) {
        StringBuffer sqlStr = new StringBuffer("select dr.*,dse.sys_name appName from dag_routes dr,dsgc_system_entities dse where 1=1 and dr.app_code = dse.sys_code ");
        MpaasQuery mq = sw.buildQuery();
        if ("SystemLeader".equals(userRole)) {
            sqlStr.append(" and dr.app_code in ( ");
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
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex, pageSize, DagRoutesBean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(dr.route_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dr.bs_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dr.route_path) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dr.route_method) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dr.route_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";

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
    public DagRoutesBean queryRouteDetail(CommonReqBean param){
        return  sw.buildQuery().sql("select dr.*,dse.sys_name appName from dag_routes dr,dsgc_system_entities dse where dr.app_code = dse.sys_code and dr.route_code = #routeCode")
                .setVar("routeCode",param.getCon0())
                .doQueryFirst(DagRoutesBean.class);
    }
    public List<DagEnvInfoCfgBean> queryApiEnv(){
        return sw.buildQuery().doQuery(DagEnvInfoCfgBean.class);
    }
    public List<DagCodeVersionBean> queryApiCodeVersion(CommonReqBean param){
        return sw.buildQuery().eq("sour_code",param.getCon0()).eq("sour_type",param.getQueryType()).doQuery(DagCodeVersionBean.class);
    }
    public DagCodeVersionBean queryCodeVersionById(CommonReqBean param){
        return sw.buildQuery().eq("vid",param.getCon0()).doQueryFirst(DagCodeVersionBean.class);
    }
    public List<DagCodeVersionBean> queryRouteConfigListBySourCode(CommonReqBean param){
        return sw.buildQuery().eq("sour_code",param.getCon0()).eq("sour_type",param.getQueryType()).doQuery(DagCodeVersionBean.class);
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

    public List<DagRoutesHostnameBean> queryRouteAnotherRule(CommonReqBean param){
        return sw.buildQuery().eq("vid",param.getCon0()).doQuery(DagRoutesHostnameBean.class);
    }
    public List<DagPluginUsingBean> queryRoutePlugByVid(String vid){
        return sw.buildQuery().eq("vid",vid).doQuery(DagPluginUsingBean.class);
    }
    public void addRouteAnotherRule(DagRoutesHostnameBean dagRoutesHostnameBean){
        sw.buildQuery().doInsert(dagRoutesHostnameBean);
    }
}
