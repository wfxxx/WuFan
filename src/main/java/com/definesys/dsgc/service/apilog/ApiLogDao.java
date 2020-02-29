package com.definesys.dsgc.service.apilog;

import com.definesys.dsgc.service.apilog.bean.DSGCApisBean;
import com.definesys.dsgc.service.apilog.bean.DagLogBodyPayloadBean;
import com.definesys.dsgc.service.apilog.bean.DagLogInstanceBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApiLogDao {
    @Autowired
    private MpaasQueryFactory sw;
  public void addLogInstance(DagLogInstanceBean dagLogInstanceBean){
      sw.buildQuery().doInsert(dagLogInstanceBean);
  }

  public void addLogPayload(DagLogBodyPayloadBean dagLogBodyReqPayloadBean,DagLogBodyPayloadBean dagLogBodyResPayloadBean){
      sw.buildQuery().doInsert(dagLogBodyReqPayloadBean);
      sw.buildQuery().doInsert(dagLogBodyResPayloadBean);
  }

  public DSGCApisBean queryApiMsgByPath(String uri,String method){
      return sw.buildQuery().sql("select t.*,dse.sys_name appName from (select da.* from dsgc_apis da,dsgc_services_uri dsu where da.api_code = dsu.serv_no and dsu.ib_uri = #uri and dsu.http_method = #method) t,dsgc_system_entities dse where t.app_code = dse.sys_code")
              .setVar("uri",uri)
              .setVar("method",method)
              .doQueryFirst(DSGCApisBean.class);
  }
}
