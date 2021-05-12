package com.definesys.dsgc.service.esbenv;

import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvMachineCfg;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvServerCfg;
import com.definesys.dsgc.service.esbenv.bean.SvcgenDeployControl;
import com.definesys.dsgc.service.esbenv.bean.DSGCBusCfgVO;
import com.definesys.mpaas.log.SWordLogger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: biao.luo
 * @since: 2019/7/18 下午6:25
 * @history: 1.2019/7/18 created by biao.luo
 */
@Service
public class DSGCBusCfgService {
    @Autowired
    private SWordLogger logger;

    @Autowired
    private DSGCBusCfgDao busCfgDao;


    public List<DSGCEnvInfoCfg> queryEnvInfoCfgListPage() {
        return busCfgDao.queryEnvInfoCfgListPage();
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateEnvInfoCfg(DSGCEnvInfoCfg envInfoCfg) {
        busCfgDao.insertOrUpdateEnvInfoCfg(envInfoCfg);
    }


    public DSGCBusCfgVO findBusCfgDetailsByEnvCode(String deicId) {
        DSGCBusCfgVO busCfgVO = new DSGCBusCfgVO();
        DSGCEnvInfoCfg envInfoCfg = busCfgDao.findEnvInfoCfgByDeicId(deicId);

        if(null != envInfoCfg){
            BeanUtils.copyProperties(envInfoCfg,busCfgVO);
            String envCode = envInfoCfg.getEnvCode();
            //查询子表By - envCode
            List<DSGCEnvMachineCfg> envMachineCfgs = busCfgDao.findEnvMachineCfgByEnvCode(envCode);
            List<DSGCEnvServerCfg> envServerCfgs = busCfgDao.findEnvServerCfgByEnvCode(envCode);
            List<SvcgenDeployControl> deployControls = busCfgDao.findSvcgenDeployControlByEnvCode(envCode);

            //设置服务器和节点的个数
            busCfgVO.setServerCount(envMachineCfgs.size());
            busCfgVO.setNodeCount(envServerCfgs.size());

            busCfgVO.setEnvMachineCfgs(envMachineCfgs);
            busCfgVO.setEnvServerCfgs(envServerCfgs);
            busCfgVO.setDeployControls(deployControls);

            return busCfgVO;
        }else{
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateEnvMachineCfg(DSGCEnvMachineCfg envMachineCfg) {
        busCfgDao.insertOrUpdateEnvMachineCfg(envMachineCfg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateEnvServerCfg(DSGCEnvServerCfg envServerCfg) {
        busCfgDao.insertOrUpdateEnvServerCfg(envServerCfg);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateSvcgenDeployControl(SvcgenDeployControl deployControl) {
        busCfgDao.insertOrUpdateSvcgenDeployControl(deployControl);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delEnvMachineCfgByDemcId(String[] rowIds) {
        for(String  demcId : rowIds) {
            busCfgDao.delEnvMachineCfgByDemcId(demcId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delEnvServerCfgBydDescId(String[] rowIds) {
        for(String  descId : rowIds) {
            busCfgDao.delEnvServerCfgBydDescId(descId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delSvcgenDeployControlBySdcId(String[] rowIds) {
        for(String  sdcId : rowIds) {
            busCfgDao.delSvcgenDeployControlBySdcId(sdcId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delEnvInfoByDeid(DSGCEnvInfoCfg dsgcEnvInfoCfg){
        // 查询该环境子表信息
        List<DSGCEnvMachineCfg> envMachineCfgs = busCfgDao.findEnvMachineCfgByEnvCode(dsgcEnvInfoCfg.getEnvCode());
        List<DSGCEnvServerCfg> envServerCfgs = busCfgDao.findEnvServerCfgByEnvCode(dsgcEnvInfoCfg.getEnvCode());
        List<SvcgenDeployControl> deployControls = busCfgDao.findSvcgenDeployControlByEnvCode(dsgcEnvInfoCfg.getEnvCode());

        // 机器信息
        if (envMachineCfgs.size()>0) {
            for (DSGCEnvMachineCfg dsgcEnvMachineCfg:envMachineCfgs) {
                busCfgDao.delEnvMachineCfgByDemcId(dsgcEnvMachineCfg.getDemcId());
            }
        }
        // 部署控制信息
        if (deployControls.size()>0) {
            for (SvcgenDeployControl svcgenDeployControl:deployControls) {
                busCfgDao.delSvcgenDeployControlBySdcId(svcgenDeployControl.getSdcId());
            }
        }
        // 节点服务信息
        if (envServerCfgs.size()>0) {
            for (DSGCEnvServerCfg dsgcEnvServerCfg:envServerCfgs) {
                busCfgDao.delEnvServerCfgBydDescId(dsgcEnvServerCfg.getDescId());
            }
        }

        // 环境信息
        busCfgDao.delEnvInfoByDeid(dsgcEnvInfoCfg.getDeicId());

    }
}
