package com.definesys.dsgc.service.dess.DessBusiness;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName DBusDao
 * @Description TODO
 * @Author ystar
 * @Date 2020-8-3 13:28
 * @Version 1.0
 **/
@Repository
public class DBusDao {

    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;

    public PageQueryResult<DessBusiness> queryBusinessList(CommonReqBean param, int pageSize, int pageIndex) {
        StringBuffer sqlStr = null;
        if ("oracle".equals(dbType)) {
            sqlStr = new StringBuffer("select dl.BUSINESS_ID,dl.BUSINESS_NAME,dl.BUSINESS_DESC,dl.INVOKE_URL,dl.WEBSERVICE_TYPE,dl.INVOKE_OPERATION,dl.CREATION_DATE,dl.BUSINESS_TYPE from DESS_BUSINESS dl where 1=1 ");
        }
        if ("mysql".equals(dbType)) {
            sqlStr = new StringBuffer("select dl.BUSINESS_ID,dl.BUSINESS_NAME,dl.BUSINESS_DESC,dl.INVOKE_URL,dl.WEBSERVICE_TYPE,dl.INVOKE_OPERATION,dl.CREATION_DATE,dl.BUSINESS_TYPE from DESS_BUSINESS dl where 1=1 ");
        }
        MpaasQuery mq = sw.buildQuery();
        if (StringUtil.isNotBlank(param.getCon0())) {
            String[] conArray = param.getCon0().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append(this.generateLikeAndCluse(s));
                }
            }
        }
        if (param.getQueryType().equals("ALL")) {
        } else {
            String[] conArray = param.getQueryType().trim().split(" ");
            for (String s : conArray) {
                if (s != null && s.length() > 0) {
                    sqlStr.append("and  dl.business_type like '%" + s + "%'");
                }
            }
        }
        if ("oracle".equals(dbType)) {
            mq.sql(sqlStr.toString() + " order by dl.creation_date desc");
        }
        if ("mysql".equals(dbType)) {
            mq.sql(sqlStr.toString() + " GROUP BY dl.business_id HAVING COUNT(1)>0  order by dl.creation_date desc");
        }

        return mq.doPageQuery(pageIndex, pageSize, DessBusiness.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(dl.business_id) like '%" + conUpper + "%'";
        conAnd += " or UPPER(dl.business_name) like '%" + conUpper + "%' ";
        conAnd += " or UPPER(dl.business_type) like '%" + conUpper + "%' )";
        return conAnd;
    }

    public void addBusiness(DessBusiness dessBusiness) {
        sw.buildQuery()
                .doInsert(dessBusiness);
    }

    public boolean checkBusinessName(String businessName) {
        DessBusiness business_name = sw.buildQuery().eq("business_name", businessName).doQueryFirst(DessBusiness.class);
        if (business_name == null) {
            return false;
        } else {
            return true;
        }
    }

    public List<DessBusiness> checkDel() {
        return sw.buildQuery()
                .sql("select db.BUSINESS_NAME from DESS_INSTANCE di left join DESS_BUSINESS db on db.BUSINESS_ID = DI.BUSINESS_ID")
                .doQuery(DessBusiness.class);
    }

    public void delBusiness(String businessId) {
        sw.buildQuery()
                .eq("business_id", businessId)
                .doDelete(DessBusiness.class);
    }

    public DessBusiness getBusinessDtl(String id) {
        DessBusiness dessBusiness = sw.buildQuery()
                .eq("business_id", id)
                .doQueryFirst(DessBusiness.class);
        return dessBusiness;
    }

    public void updateBusinessDtl(DessBusiness dessBusiness) {
        sw.buildQuery()
                .eq("business_id", dessBusiness.getBusinessId())
                .doUpdate(dessBusiness);
    }
}
