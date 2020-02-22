package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.apilr.bean.DagLrbean;
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
        if ("SystemLeader".equals(userRole)) {
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
        mq.sql(sqlStr.toString());
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

    public void delApiBs(CommonReqBean param){
        sw.buildQuery().eq("dl_id",param.getCon0()).doDelete(DagLrbean.class);
    }

    public DagLrbean queryApiLrById(CommonReqBean param){
        return sw.buildQuery().eq("dl_id",param.getCon0()).doQueryFirst(DagLrbean.class);
    }
}
