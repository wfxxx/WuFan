package com.definesys.dsgc.service.apicert;


import com.definesys.dsgc.service.apibs.bean.DagBsbean;
import com.definesys.dsgc.service.apicert.bean.CommonReqBean;
import com.definesys.dsgc.service.apicert.bean.DagCertRefIDbean;
import com.definesys.dsgc.service.apicert.bean.DagCertbean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ApiCertDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-24 18:17
 * @Version 1.0
 **/
@Repository
public class ApiCertDao {

    @Autowired
    private MpaasQueryFactory sw;


    public PageQueryResult queryApiCertList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        StringBuffer sqlStr = new StringBuffer(" select * from( select c.*,e.sys_name as app_name,info.env_name as env_name from dag_certs c\n" +
                "            left join dsgc_system_entities e on e.sys_code=c.app_code\n" +
                "            left join DSGC_ENV_INFO_CFG info on info.env_code=c.env_code\n" +
                "            where info.env_type='DAG' order by c.creation_date desc) s where 1=1");
        MpaasQuery mq = sw.buildQuery();
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
        mq.sql(sqlStr.toString());
        return mq.doPageQuery(pageIndex, pageSize, DagCertbean.class);
    }

    public void addApiCert(DagCertbean dagCertbean){
         sw.buildQuery().doInsert(dagCertbean);
    }

    public void updateApiCert(DagCertbean dagCertbean){
        sw.buildQuery().doUpdate(dagCertbean);
    }

    public void delApiCert(DagCertbean dagCertbean){
        sw.buildQuery().eq("dc_id",dagCertbean.getDcId()).doDelete(DagCertbean.class);
    }

    public DagCertbean checkSameName(String certName){
       return  sw.buildQuery().eq("cert_name",certName).doQueryFirst(DagCertbean.class);
    }

    private String generateLikeAndCluse(String con) {
        String conUpper = con.toUpperCase();
        String conAnd = " and  (UPPER(cert_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(app_name) like '%" + conUpper + "%'";
        conAnd += " or UPPER(cert_sni) like '%" + conUpper + "%' )";
        return conAnd;
    }


    public DagCertRefIDbean getRefIdByKeyId(String dcId){
      if(dcId != null) {
          return sw.buildQuery().sql("select REF_ID,ENV_CODE from DAG_CERTS where dc_id = #dcId").setVar("dcId",dcId).doQueryFirst(DagCertRefIDbean.class);
      }
        return null;
    }
}
