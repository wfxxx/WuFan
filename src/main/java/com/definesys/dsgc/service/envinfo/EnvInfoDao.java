package com.definesys.dsgc.service.envinfo;

import com.definesys.dsgc.service.envinfo.bean.*;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnvInfoDao {
    @Autowired
    private MpaasQueryFactory sw;
    @Value("${database.type}")
    private String dbType;

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

    public List<DsgcEnvInfoCfgBean> queryApiEnvCfgList(CommonReqBean q){
        String str = null;
        if("oracle".equals(dbType)){
            str = "select de.*,row_number() over(partition by de.ENV_TYPE order by de.ENV_SEQ asc)  row_number from DSGC_ENV_INFO_CFG de ";

        }
        if("mysql".equals(dbType)){
            str = "SELECT de.*,IF(@temp=de.ENV_TYPE, @rank:=@rank+1, @rank:=1) row_number,@temp:=de.ENV_TYPE FROM dsgc.DSGC_ENV_INFO_CFG de ORDER BY de.ENV_TYPE, de.ENV_SEQ ASC ";
        }
        MpaasQuery sql=sw.buildQuery()
              .sql(str);
      if(q.getCon0()!=null&&!q.getCon0().equals("")){
          sql.eq("env_type",q.getCon0());
      }
        return sql.doQuery(DsgcEnvInfoCfgBean.class);
    }
    public List<DsgcEnvInfoCfgBean> queryEnvListDetail(String envType){
        return sw.buildQuery()
               .eq("env_type",envType)
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

    public List<EnvOptionDTO> getEnvList(){
        return this.sw.buildQuery().sql("SELECT C.ENV_CODE,C.ENV_NAME FROM DSGC_ENV_INFO_CFG C,FND_PROPERTIES P\n" +
                "WHERE P.PROPERTY_KEY = 'SHOW_SER_TYPE'\n" +
                "AND( P.PROPERTY_VALUE = 'ALL'\n" +
                "  OR P.PROPERTY_VALUE = 'ESB' AND C.ENV_TYPE = 'ESB'\n" +
                "  OR P.PROPERTY_VALUE = 'API' AND C.ENV_TYPE = 'DAG')").doQuery(EnvOptionDTO.class);
    }
    public List<EnvOptionDTO> getESBEnvList(){
        return this.sw.buildQuery().sql("SELECT C.ENV_CODE,C.ENV_NAME FROM DSGC_ENV_INFO_CFG C WHERE C.ENV_TYPE = 'ESB'").doQuery(EnvOptionDTO.class);
    }
    public List<EnvOptionDTO> getDAGEnvList(){
        return this.sw.buildQuery().sql("SELECT C.ENV_CODE,C.ENV_NAME FROM DSGC_ENV_INFO_CFG C WHERE C.ENV_TYPE = 'DAG'").doQuery(EnvOptionDTO.class);
    }
}
