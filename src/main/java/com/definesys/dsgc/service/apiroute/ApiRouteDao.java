package com.definesys.dsgc.service.apiroute;

import com.definesys.dsgc.service.apiroute.bean.CommonReqBean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
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
}
