package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.CommonReqBean;
import com.definesys.dsgc.service.apibs.bean.DagBsDtiBean;
import com.definesys.dsgc.service.apibs.bean.DagBsbean;
import com.definesys.dsgc.service.apiroute.bean.DagRoutesBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiBsDao {
    @Autowired
    private MpaasQueryFactory sw;
    public PageQueryResult queryApiBsList(CommonReqBean param, int pageSize, int pageIndex, String userRole,List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer("select db.*,dse.sys_name appName from dag_bs db,dsgc_system_entities dse where 1=1 and db.app_code = dse.sys_code ");
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
        return mq.doPageQuery(pageIndex, pageSize, DagBsbean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(db.bs_code) like '%" + conUpper + "%'";
        conAnd += " or UPPER(db.bs_desc) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dse.sys_name) like '%" + conUpper + "%' )";

        return conAnd;
    }

    public List<DagBsbean> queryApiBsByCustomInput(CommonReqBean param){
      return sw.buildQuery().like("bs_code",param.getCon0()).doQuery(DagBsbean.class);
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

    public DagBsDtiBean addDagBsDti(DagBsDtiBean dagBsDtiBean){
         sw.buildQuery().doInsert(dagBsDtiBean);
        return queryDagBsDtiByid(dagBsDtiBean.getDbdId());
    }


    public List<Map<String,Object>> queryProtocalList(){

        return null;
    }

}
