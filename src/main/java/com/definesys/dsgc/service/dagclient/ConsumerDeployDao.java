package com.definesys.dsgc.service.dagclient;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumerDeployDao {
    @Autowired
    private MpaasQueryFactory sw;


    public String getConsumerBasicAuth(String consumerCode,String envCode) {
        Map<String,Object> res = sw.buildQuery().sql("select ca_attr1 BA_PD from dsgc_consumer_auth where csm_code = #consumerCode and env_code = #envCode")
                .setVar("consumerCode",consumerCode)
                .setVar("envCode",envCode)
                .doQueryFirst();
        if (res != null) {
            Object pdObj = res.get("BA_PD");
            if (pdObj != null) {
                return pdObj.toString();
            }
        }
        return null;
    }



    public List<String> getConsumerGroups(String consumerCode){

        List<Map<String,Object>> res = sw.buildQuery().sql("select u.ib_uri,u.http_method from dsgc_apis_access c,dsgc_apis a, dsgc_services_uri u\n" +
                "where c.api_code = a.api_code\n" +
                "  and a.api_code = u.serv_no\n" +
                "  and c.csm_code = #csmCode").setVar("csmCode",consumerCode).doQuery();

        List<String> groups = new ArrayList<String>();

        if(res != null && res.size() > 0){
            Iterator<Map<String,Object>> iters = res.iterator();
            while(iters.hasNext()){
                Map<String,Object> row = iters.next();
                String uri = row.get("IB_URI")  == null ? null : row.get("IB_URI").toString();
                String md = row.get("HTTP_METHOD") == null ? null : row.get("HTTP_METHOD").toString();

                String groupName = this.getGroupByUriAndMedthod(md,uri);
                if(groupName != null && groupName.trim().length()>0){
                    groups.add(md+uri);
                }
            }
        }

        return groups;
    }


    private String getGroupByUriAndMedthod(String md,String uri){
        if(uri != null && uri.trim().length() >0 && md != null && md.trim().length() >0){
            return md+uri;
        } else {
            return null;
        }
    }

    public String getGroupByApiCode(String apiCode) {
        Map<String,Object> row = sw.buildQuery().sql("select u.ib_uri,u.http_method from dsgc_apis a, dsgc_services_uri u\n" +
                "                where a.api_code = u.serv_no\n" +
                "                  and a.api_code = #apiCode").setVar("apiCode",apiCode).doQueryFirst();
        if (row != null) {
            String uri = row.get("IB_URI") == null ? null : row.get("IB_URI").toString();
            String md = row.get("HTTP_METHOD") == null ? null : row.get("HTTP_METHOD").toString();

            return this.getGroupByUriAndMedthod(md,uri);

        } else {
            return null;
        }

    }

}
