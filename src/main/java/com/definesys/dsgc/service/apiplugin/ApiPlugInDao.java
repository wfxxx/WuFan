package com.definesys.dsgc.service.apiplugin;

import com.definesys.dsgc.service.apiplugin.bean.CommonReqBean;
import com.definesys.dsgc.service.apiplugin.bean.DAGPluginListVO;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiPlugInDao {


    @Autowired
    private MpaasQueryFactory sw;
    public PageQueryResult queryPluginList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer("select * from (\n" +
                "    select t1.*,case when e.sys_name is not null then  e.sys_name else '未知' end as app_name  from\n" +
                "           (select \n" +
                "           u.dpu_id as plugin_using_id,\n" +
                "           u.plugin_code,\n" +
                "           u.is_enable,\n" +
                "           s.plugin_name,\n" +
                "           v.sour_type,\n" +
                "           v.vid,\n" +
                "           v.v_name as config_name,\n" +
                "            v.sour_code,\n" +
                "           (case when lr.app_code is not null then lr.app_code \n" +
                "                 when  rou.app_code is not null then  rou.app_code \n" +
                "                 when bs.app_code is not null then bs.app_code else '未知' end) \n" +
                "           as app_code,\n" +
                "           stat.env_code,\n" +
                "           v.creation_date \n" +
                "           from dag_plugin_using u \n" +
                "           left join dag_plugin_store s on u.plugin_code=s.plugin_code\n" +
                "           left join  dag_code_version v on u.vid=v.vid\n" +
                "           left join dag_lr lr on lr.lr_name=v.sour_code\n" +
                "           left join dag_routes rou on rou.route_code =v.sour_code\n" +
                "           left join dag_bs bs on bs.bs_code=v.sour_code\n" +
                "           left join (select t.vid,to_char(wm_concat(t.env_code)) as env_code from DAG_DEPLOY_STAT t  group by t.vid)  stat on stat.vid=v.vid\n" +
                "        ) \n" +
                "        t1\n" +
                "        left join  dsgc_system_entities e on t1.app_code=e.sys_code\n" +
                "        order by t1.creation_date desc ) where 1=1 ");
        if ("SystemLeader".equals(userRole)&&sysCodeList.size()>0) {
            sqlStr.append(" and app_code in ( ");
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
        if(StringUtil.isNotBlank(param.getParamsType())&&!param.getParamsType().equals("ALL")){
            String[] conArray = param.getParamsType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and  env_code like '%" + s + "%'");
                }
            }
        }
        return sw.buildQuery().sql(sqlStr.toString()).doPageQuery(pageIndex, pageSize, DAGPluginListVO.class);
    }


    public List<DSGCEnvInfoCfg> queryDeplogDev(String envCode){
        String[] conArray = envCode.trim().split(",");
        return sw.buildQuery().in("ENV_CODE",conArray).doQuery(DSGCEnvInfoCfg.class);
    }

    private String generateLikeAndCluse(String con) {
        if("路由".equals(con)){
            con="route";
        }
        if("负载均衡".equals(con)){
            con="lr";
        }
        if("后端".equals(con)){
            con="bs";
        }
        String conUpper = con.toUpperCase();
        String conAnd = "  and  (UPPER(plugin_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(sour_type) like '%" + conUpper + "%'";
        conAnd += " or UPPER(app_name) like '%" + conUpper + "%' )";
        return conAnd;
    }

}
