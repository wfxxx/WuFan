package com.definesys.dsgc.service.envinfo;

import com.definesys.dsgc.service.envinfo.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
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

    public DsgcEnvInfoCfgBean queryApiEnvCfg(String deicId){
        return sw.buildQuery()
                .eq("deicId",deicId)
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
              .sql("select de.*,row_number() over(partition by de.ENV_TYPE order by de.ENV_SEQ asc)  row_number from DSGC_ENV_INFO_CFG de ")
              .doQuery(DsgcEnvInfoCfgBean.class);
    }

    public void addOrUpdateEnvInfoCfg(DsgcEnvInfoCfgBean envInfoCfg) {
        MpaasQuery mq = sw.buildQuery();

        String deicId = envInfoCfg.getDeicId();
        if(StringUtil.isBlank(deicId)){
            mq.doInsert(envInfoCfg);
        }else{
            mq.eq("deic_id",envInfoCfg.getDeicId())
                    .doUpdate(envInfoCfg);
        }
    }


    public void delEnvMachineCfg(String envCode) {
        sw.buildQuery()
                .eq("env_code",envCode)
                .doDelete(DSGCEnvMachineCfg.class);
    }

    public List<DSGCEnvMachineCfg> getEnvMachineCfgByEnvCode(String envCode){
      return sw.buildQuery()
              .eq("env_code",envCode)
              .doQuery(DSGCEnvMachineCfg.class);
    }

    public void addEnvMachineCfg(DSGCEnvMachineCfg dsgcEnvMachineCfg){
      sw.buildQuery().doInsert(dsgcEnvMachineCfg);
    }

    public void delEnvServCfg(String envCode) {
        sw.buildQuery()
                .eq("env_code",envCode)
                .doDelete(DSGCEnvServerCfg.class);
    }

    public List<DSGCEnvServerCfg> getEnvServCfgByEnvCode(String envCode){
        return sw.buildQuery()
                .eq("env_code",envCode)
                .doQuery(DSGCEnvServerCfg.class);
    }

    public void addEnvServCfg(DSGCEnvServerCfg dsgcEnvServerCfg){
        sw.buildQuery().doInsert(dsgcEnvServerCfg);
    }

    public void delEnvDeployCfg(String envCode) {
        sw.buildQuery()
                .eq("env_code",envCode)
                .doDelete(SvcgenDeployControl.class);
    }

    public List<SvcgenDeployControl> getEnvDeployCfgByEnvCode(String envCode){
        return sw.buildQuery()
                .eq("env_code",envCode)
                .doQuery(SvcgenDeployControl.class);
    }

    public void addEnvDeployCfg(SvcgenDeployControl svcgenDeployControl){
        sw.buildQuery().doInsert(svcgenDeployControl);
    }
}
