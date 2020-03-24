package com.definesys.dsgc.service.apilog;

import com.definesys.dsgc.service.apilog.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
  public List<DSGCApisBean> queryApiNameData(String apiName){
      return sw.buildQuery().like("api_name",apiName).doQuery(DSGCApisBean.class);
  }
  public PageQueryResult<DagLogInstanceBean> doQueryApiLogInstanceList(QueryApiLogInstVO param,int pageIndex,int pageSize,String userRole,List<String> consumerCodeList,List<String> appCodeList){
      MpaasQuery mq =  sw.buildQuery();
      if (StringUtil.isNotBlank(param.getApiCode())){
          mq.eq("api_code",param.getApiCode());
      }
      if (StringUtil.isNotBlank(param.getConsumerCode())){
          mq.eq("csm_code",param.getConsumerCode());
      }
      if (StringUtil.isNotBlank(param.getStartTime()) && StringUtil.isNotBlank(param.getEndTime())){
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date startTime =null;
          Date endTime = null;
          try {
              startTime = format.parse(param.getStartTime());
              endTime = format.parse(param.getEndTime());
          } catch (ParseException e) {
              e.printStackTrace();
          }
          mq.gt("creation_date",startTime)
                  .lteq("creation_date",endTime);
      }
      if(userRole.equals("Tourist")){
          mq.in("csm_code",consumerCodeList);
      }
      if(userRole.equals("SystemLeader")){
          mq.in("extra_attr3",appCodeList);
      }
      mq.orderBy("creation_date","desc");
      return mq.doPageQuery(pageIndex,pageSize,DagLogInstanceBean.class);
  }
    public FndProperties findFndPropertiesByKey(String key) {
        return sw.buildQuery()
                .eq("property_key",key)
                .doQueryFirst(FndProperties.class);
    }
    public List<DSGCEnvInfoCfg> queryApiEnv(){
        return sw.buildQuery().eq("env_type","DAG").doQuery(DSGCEnvInfoCfg.class);
    }
    public DSGCApisBean queryApiByCode(String apiCode){
        return sw.buildQuery().eq("api_code",apiCode).doQueryFirst(DSGCApisBean.class);
    }
    public List<DSGCConsumerUsers> querytTouristConsumerList(String userId){
        return sw.buildQuery().eq("user_id",userId).doQuery(DSGCConsumerUsers.class);
    }
    public List<DSGCSystemUser> querySystemLeaderAppList(String userId){
        return sw.buildQuery().eq("user_id",userId).doQuery(DSGCSystemUser.class);
    }
    public List<DagLogBodyPayloadBean> queryApiLogPayloadData(String trackId){
        return sw.buildQuery().eq("track_id",trackId).doQuery(DagLogBodyPayloadBean.class);
    }
    public DagLogInstanceBean querApiLOgInstById(String trackId){
        return sw.buildQuery().eq("track_id",trackId).doQueryFirst(DagLogInstanceBean.class);
    }
}
