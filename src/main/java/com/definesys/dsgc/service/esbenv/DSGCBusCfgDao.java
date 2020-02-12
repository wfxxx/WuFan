package com.definesys.dsgc.service.esbenv;

import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.esbenv.bean.SvcgenDeployControl;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvMachineCfg;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvServerCfg;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/7/18 下午6:24
 * @history: 1.2019/7/18 created by biao.luo
 */
@Repository
public class DSGCBusCfgDao {

    @Autowired
    private MpaasQueryFactory sw;


    public List<DSGCEnvInfoCfg> queryEnvInfoCfgListPage() {
        return sw.buildViewQuery("bus_allInfo_view")
                .doQuery(DSGCEnvInfoCfg.class);
    }


    public void insertOrUpdateEnvInfoCfg(DSGCEnvInfoCfg envInfoCfg) {
        MpaasQuery mq = sw.buildQuery();

        String deicId = envInfoCfg.getDeicId();
        if(StringUtil.isBlank(deicId)){
            mq.doInsert(envInfoCfg);
        }else{
            mq.eq("deic_id",envInfoCfg.getDeicId())
                    .doUpdate(envInfoCfg);
        }
    }

    public DSGCEnvInfoCfg findEnvInfoCfgByDeicId(String deicId) {
        return sw.buildQuery()
                .eq("deicId",deicId)
                .doQueryFirst(DSGCEnvInfoCfg.class);
    }

    public List<DSGCEnvMachineCfg> findEnvMachineCfgByEnvCode(String envCode) {
        return sw.buildQuery()
                .eq("env_code",envCode)
                .doQuery(DSGCEnvMachineCfg.class);
    }

    public List<DSGCEnvServerCfg> findEnvServerCfgByEnvCode(String envCode) {
        return sw.buildQuery()
                .eq("env_code",envCode)
                .doQuery(DSGCEnvServerCfg.class);
    }


    public List<SvcgenDeployControl> findSvcgenDeployControlByEnvCode(String envCode) {
        return sw.buildQuery()
                .eq("env_code",envCode)
                .doQuery(SvcgenDeployControl.class);
    }

    public void insertOrUpdateEnvMachineCfg(DSGCEnvMachineCfg envMachineCfg) {
        MpaasQuery mq = sw.buildQuery();

        String demcId = envMachineCfg.getDemcId();
        if(StringUtil.isBlank(demcId)){
            mq.doInsert(envMachineCfg);
        }else{
            mq.eq("demc_id",envMachineCfg.getDemcId())
                    .doUpdate(envMachineCfg);
        }
    }


    public void insertOrUpdateEnvServerCfg(DSGCEnvServerCfg envServerCfg) {
        MpaasQuery mq = sw.buildQuery();

        String descId = envServerCfg.getDescId();
        if(StringUtil.isBlank(descId)){
            mq.doInsert(envServerCfg);
        }else{
            mq.eq("desc_id",envServerCfg.getDescId())
                    .doUpdate(envServerCfg);
        }
    }

    public void insertOrUpdateSvcgenDeployControl(SvcgenDeployControl deployControl) {
        MpaasQuery mq = sw.buildQuery();

        String sdcId = deployControl.getSdcId();
        if(StringUtil.isBlank(sdcId)){
            mq.doInsert(deployControl);
        }else{
            mq.eq("sdc_id",deployControl.getSdcId())
                    .doUpdate(deployControl);
        }
    }

    public void delEnvMachineCfgByDemcId(String demcId) {
        sw.buildQuery()
                .eq("demc_id",demcId)
                .doDelete(DSGCEnvMachineCfg.class);
    }

    public void delEnvServerCfgBydDescId(String descId) {
        sw.buildQuery()
                .eq("desc_id",descId)
                .doDelete(DSGCEnvServerCfg.class);
    }

    public void delSvcgenDeployControlBySdcId(String sdcId) {
        sw.buildQuery()
                .eq("sdc_id",sdcId)
                .doDelete(SvcgenDeployControl.class);
    }

    public void delEnvInfoByDeid(String deid){
        sw.buildQuery().eq("deic_id",deid)
                .doDelete(DSGCEnvInfoCfg.class);
    }
    public List<DSGCEnvInfoCfg> queryEnvInfoCfgByEnvSeq(int envSeq){
    return sw.buildQuery().eq("envSeq",envSeq).doQuery(DSGCEnvInfoCfg.class);
    }
}
