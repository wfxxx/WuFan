package com.definesys.dsgc.service.apilog;

import com.alibaba.fastjson.JSONObject;
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
import java.util.Map;

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
      List<Map<String,String>> keywordForm = param.getKeywordForm();
      if(keywordForm == null || keywordForm.isEmpty()) {


          if (StringUtil.isNotBlank(param.getApiCode())) {
              mq.eq("api_code", param.getApiCode());
          }
          if (StringUtil.isNotBlank(param.getConsumerCode())) {
              mq.eq("csm_code", param.getConsumerCode());
          }
          if (StringUtil.isNotBlank(param.getStartTime()) && StringUtil.isNotBlank(param.getEndTime())) {
              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date startTime = null;
              Date endTime = null;
              try {
                  startTime = format.parse(param.getStartTime());
                  endTime = format.parse(param.getEndTime());
              } catch (ParseException e) {
                  e.printStackTrace();
              }
              System.out.println(endTime);
              mq.gt("creation_date", startTime)
                      .lteq("creation_date", endTime);
          }
          if (userRole.equals("Tourist")) {
              mq.in("csm_code", consumerCodeList);
          }
          if (userRole.equals("SystemLeader")) {
              mq.in("extra_attr3", appCodeList);
          }
          mq.orderBy("creation_date", "desc");
          return mq.doPageQuery(pageIndex, pageSize, DagLogInstanceBean.class);
      }else {
          //存在关键字时
          String COL1 = "";
          String COL2 = "";
          String COL3 = "";
          String COL4 = "";
          String COL5 = "";
          String COL6 = "";
          String COL7 = "";
          String COL8 = "";
          String COL9 = "";
          String COLTEN = "";
          for (int i = 0; i < keywordForm.size(); i++) {
              Map<String,String> map = keywordForm.get(i);
              String inputValue = map.get("inputValue");
              String busKeyword = map.get("busKeyword");
              switch (busKeyword) {
                  case "COL1":
                      COL1 = inputValue;
                      break;
                  case "COL2":
                      COL2 = inputValue;
                      break;
                  case "COL3":
                      COL3 = inputValue;
                      break;
                  case "COL4":
                      COL4 = inputValue;
                      break;
                  case "COL5":
                      COL5 = inputValue;
                      break;
                  case "COL6":
                      COL6 = inputValue;
                      break;
                  case "COL7":
                      COL7 = inputValue;
                      break;
                  case "COL8":
                      COL8 = inputValue;
                      break;
                  case "COL9":
                      COL9 = inputValue;
                      break;
                  case "COL10":
                      COLTEN = inputValue;
                      break;
              }
          }
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date startTime = null;
          Date endTime = null;
          if(param.getStartTime() != null && param.getEndTime() != null){
              try {
                  startTime = format.parse(param.getStartTime());
                  endTime = format.parse(param.getEndTime());
              } catch (ParseException e) {
                  e.printStackTrace();
              }
          }
          mq = sw.buildViewQuery("have_bizKey_log_view")
                  .setVar("COL1", COL1)
                  .setVar("COL2", COL2)
                  .setVar("COL3", COL3)
                  .setVar("COL4", COL4)
                  .setVar("COL5", COL5)
                  .setVar("COL6", COL6)
                  .setVar("COL7", COL7)
                  .setVar("COL8", COL8)
                  .setVar("COL9", COL9)
                  .setVar("COLTEN", COLTEN)
                  .setVar("apiCode", param.getApiCode());
          if(startTime != null){
              mq.gt("start_time", startTime);
          }
          if(endTime != null){
             mq.lteq("start_time", endTime);
          }

          if(param.getConsumerCode() != null && !"".equals(param.getConsumerCode())){
              mq.eq("csm_code", param.getConsumerCode());
          }
                  mq.orderBy("creation_date", "desc");


          return mq.doPageQuery(pageIndex, pageSize, DagLogInstanceBean.class);
      }
  }
    public FndProperties findFndPropertiesByKey(String key) {
        return sw.buildQuery()
                .eq("property_key",key)
                .doQueryFirst(FndProperties.class);
    }
    public List<DSGCEnvInfoCfg> queryApiEnv(){
        return sw.buildQuery().eq("env_type","DAG").orderBy("env_seq","desc").doQuery(DSGCEnvInfoCfg.class);
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
