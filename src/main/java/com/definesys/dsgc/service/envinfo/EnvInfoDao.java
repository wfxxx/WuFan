package com.definesys.dsgc.service.envinfo;

import com.definesys.dsgc.service.envinfo.bean.CommonReqBean;
import com.definesys.dsgc.service.envinfo.bean.DsgcEnvInfoCfgBean;
import com.definesys.dsgc.service.envinfo.bean.DagEnvInfoNodeBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnvInfoDao {
    @Autowired
    private MpaasQueryFactory sw;

  public void addApiInfoCfg(DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean){
      sw.buildQuery().doInsert(dsgcEnvInfoCfgBean);
  }

    public void addApiInfoNode(DagEnvInfoNodeBean dagEnvInfoNodeBean){
        sw.buildQuery().doInsert(dagEnvInfoNodeBean);
    }

    public DsgcEnvInfoCfgBean queryApiEnvCfg(CommonReqBean q){
        return sw.buildQuery()
                .eq("deicId",q.getCon0())
                .doQueryFirst(DsgcEnvInfoCfgBean.class);
    }

    public List<DagEnvInfoNodeBean> queryApiEnvNode(String envCode){
        return sw.buildQuery()
                .eq("envCode",envCode)
                .doQuery(DagEnvInfoNodeBean.class);
    }

    public void delApiInfoNode(String envCode){
      sw.buildQuery().eq("envCode",envCode).doDelete(DagEnvInfoNodeBean.class);
    }

    public void updateApiInfoCfg(DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean){
      sw.buildQuery().eq("env_code", dsgcEnvInfoCfgBean.getEnvCode()).doUpdate(dsgcEnvInfoCfgBean);
    }

    public List<DsgcEnvInfoCfgBean> queryApiEnvCfgList(){
      return sw.buildQuery()
              .select("DEIC_ID,ENV_NAME,ENV_CODE,ENV_SEQ,ENV_TYPE")
              .groupBy("ENV_CODE,DEIC_ID,ENV_NAME,ENV_SEQ,ENV_TYPE")
              .orderBy("ENV_SEQ","asc")
              .doQuery(DsgcEnvInfoCfgBean.class);
    }
}
