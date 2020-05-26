package com.definesys.dsgc.service.envinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.envinfo.bean.*;
import com.definesys.dsgc.service.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EnvInfoService {
    @Autowired
    private EnvInfoDao envInfoDao;

    public void addApiInfoCfg(DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean) {
        envInfoDao.addApiInfoCfg(dsgcEnvInfoCfgBean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addApiInfoNode(String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        JSONArray nodeList = jsonObject.getJSONArray("nodeList");
        String envCode = jsonObject.getString("envCode");
        if (nodeList != null && !nodeList.isEmpty()) {
            // 获取envCode
            List<DagEnvInfoNodeBean> nodes = envInfoDao.queryApiEnvNode(envCode);

            if (nodes == null || nodes.isEmpty()) {
                DagEnvInfoNodeBean dagEnvInfoNodeBean = new DagEnvInfoNodeBean();
                for (int i = 0; i < nodeList.size(); i++) {
                    JSONObject jo = nodeList.getJSONObject(i);
                    dagEnvInfoNodeBean.setEnvCode(envCode);
                    dagEnvInfoNodeBean.setLocation(jo.getString("location"));
                    dagEnvInfoNodeBean.setAccessPort(Integer.valueOf(jo.getString("accessPort")));
                    dagEnvInfoNodeBean.setMngPort(Integer.valueOf(jo.getString("mngPort")));
                    envInfoDao.addApiInfoNode(dagEnvInfoNodeBean);
                }
            } else {
                envInfoDao.delApiInfoNode(envCode);
                DagEnvInfoNodeBean dagEnvInfoNodeBean = new DagEnvInfoNodeBean();
                for (int i = 0; i < nodeList.size(); i++) {
                    JSONObject jo = nodeList.getJSONObject(i);
                    dagEnvInfoNodeBean.setEnvCode(envCode);
                    dagEnvInfoNodeBean.setLocation(jo.getString("location"));
                    dagEnvInfoNodeBean.setAccessPort(Integer.valueOf(jo.getString("accessPort")));
                    dagEnvInfoNodeBean.setMngPort(Integer.valueOf(jo.getString("mngPort")));
                    envInfoDao.addApiInfoNode(dagEnvInfoNodeBean);
                }
            }
        } else {
            envInfoDao.delApiInfoNode(envCode);
        }
    }

    public DagEnvInfoDTO queryApiEnvCfg(CommonReqBean q) {
        DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean = envInfoDao.queryApiEnvCfg(q.getCon0());
        DagEnvInfoDTO dto = new DagEnvInfoDTO();
        if (null != dsgcEnvInfoCfgBean) {
            BeanUtils.copyProperties(dsgcEnvInfoCfgBean, dto);
            String envCode = dsgcEnvInfoCfgBean.getEnvCode();
            //查询子表By - envCode
            List<DagEnvInfoNodeBean> dagEnvInfoNodeBeans = envInfoDao.queryApiEnvNode(envCode);
            dto.setNodeList(dagEnvInfoNodeBeans);
            return dto;
        } else {
            return null;
        }
    }

    public List<DagEnvNodeDTO> queryApiEnvNode(CommonReqBean q) {
        List<DagEnvNodeDTO> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(q.getCon0())) {
            List<DagEnvInfoNodeBean> nodeList = envInfoDao.queryApiEnvNode(q.getCon0());
            Iterator<DagEnvInfoNodeBean> iterator = nodeList.iterator();
            while (iterator.hasNext()) {
                DagEnvNodeDTO dto = new DagEnvNodeDTO();
                DagEnvInfoNodeBean dagEnvInfoNodeBean = iterator.next();
                dto.setEinId(dagEnvInfoNodeBean.getEinId());
                dto.setAccessPort(dagEnvInfoNodeBean.getAccessPort());
                dto.setMngPort(dagEnvInfoNodeBean.getMngPort());
                dto.setLocation(dagEnvInfoNodeBean.getLocation());
                result.add(dto);
            }
            return result;
        } else {
            return result;
        }

    }


    public void updateApiInfoCfg(DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean) {
        envInfoDao.updateApiInfoCfg(dsgcEnvInfoCfgBean);
    }

    public List<DagEnvInfoDTO> queryApiEnvCfgList(CommonReqBean q) {
        List<DagEnvInfoDTO> result = new ArrayList<>();
        List<DsgcEnvInfoCfgBean> cfgList = envInfoDao.queryApiEnvCfgList(q);
        Iterator<DsgcEnvInfoCfgBean> cfg = cfgList.iterator();
        while (cfg.hasNext()) {
            DagEnvInfoDTO dto = new DagEnvInfoDTO();
            DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean = cfg.next();
            dto.setEnvCode(dsgcEnvInfoCfgBean.getEnvCode());
            dto.setEnvName(dsgcEnvInfoCfgBean.getEnvName());
            dto.setEnvSeq(dsgcEnvInfoCfgBean.getEnvSeq());
            dto.setReqLocation(dsgcEnvInfoCfgBean.getReqLocation());
            dto.setDsgcAdmin(dsgcEnvInfoCfgBean.getDsgcAdmin());
            dto.setAdminLocation(dsgcEnvInfoCfgBean.getAdminLocation());
            dto.setDeicId(dsgcEnvInfoCfgBean.getDeicId());
            dto.setEnvType(dsgcEnvInfoCfgBean.getEnvType());
            dto.setTechType(dsgcEnvInfoCfgBean.getTechType());
            result.add(dto);
        }
        return result;

    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateEnvInfoCfg(DsgcEnvInfoCfgBean envInfoCfg) {
        String adminLocation = envInfoCfg.getAdminLocation();
        String reqLocation = envInfoCfg.getReqLocation();

        envInfoCfg.setAdminIp(this.getIpFromUrl(adminLocation));
        envInfoCfg.setAdminPort(this.getPortFromUrl(adminLocation));

        envInfoCfg.setEsbIp(this.getIpFromUrl(reqLocation));
        envInfoCfg.setEsbPort(this.getPortFromUrl(reqLocation));

        envInfoCfg.setDsgcIp(this.getIpFromUrl(envInfoCfg.getDsgcAdmin()));
        envInfoCfg.setDsgcPort(this.getPortFromUrl(envInfoCfg.getDsgcAdmin()));

        envInfoDao.addOrUpdateEnvInfoCfg(envInfoCfg);
    }

    /**
     * 从url中分析出 IP
     */
    public String getIpFromUrl(String url) {
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    /**
     * 从url中分析出 PORT
     */
    public String getPortFromUrl(String url) {
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host.substring(host.indexOf(":") + 1, host.length());
    }


    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateEnvMachineCfg(CommonReqBean params) {
        String envCode = params.getCon0();
        List<DSGCEnvMachineCfg> machineAddList = params.getMachineList();
        if (machineAddList != null && !machineAddList.isEmpty()) {
            List<DSGCEnvMachineCfg> machineGetList = envInfoDao.getEnvMachineCfgByEnvCode(envCode);
            if (machineGetList == null && machineGetList.isEmpty()) {
                for (int i = 0; i < machineAddList.size(); i++) {
                    DSGCEnvMachineCfg machineCfg = machineAddList.get(i);
                    envInfoDao.addEnvMachineCfg(machineCfg);
                }
            } else {
                envInfoDao.delEnvMachineCfg(envCode);
                for (int i = 0; i < machineAddList.size(); i++) {
                    DSGCEnvMachineCfg machineCfg = machineAddList.get(i);
                    envInfoDao.addEnvMachineCfg(machineCfg);
                }
            }
        } else {
            envInfoDao.delEnvMachineCfg(envCode);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateEnvServerCfg(CommonReqBean params) {
        String envCode = params.getCon0();
        List<DSGCEnvServerCfg> servAddList = params.getServList();
        if (servAddList != null && !servAddList.isEmpty()) {
            List<DSGCEnvServerCfg> servGetList = envInfoDao.getEnvServCfgByEnvCode(envCode);
            if (servGetList == null && servGetList.isEmpty()) {
                for (int i = 0; i < servAddList.size(); i++) {
                    DSGCEnvServerCfg dsgcEnvServerCfg = servAddList.get(i);
                    envInfoDao.addEnvServCfg(dsgcEnvServerCfg);
                }
            } else {
                envInfoDao.delEnvServCfg(envCode);
                for (int i = 0; i < servAddList.size(); i++) {
                    DSGCEnvServerCfg dsgcEnvServerCfg = servAddList.get(i);
                    envInfoDao.addEnvServCfg(dsgcEnvServerCfg);
                }
            }
        } else {
            envInfoDao.delEnvServCfg(envCode);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateSvcgenDeployControl(CommonReqBean params) {
        String envCode = params.getCon0();
        List<SvcgenDeployControl> deployAddList = params.getDeployList();
        if (deployAddList != null && !deployAddList.isEmpty()) {
            List<SvcgenDeployControl> deployGetList = envInfoDao.getEnvDeployCfgByEnvCode(envCode);
            if (deployGetList == null && deployGetList.isEmpty()) {
                for (int i = 0; i < deployAddList.size(); i++) {
                    SvcgenDeployControl svcgenDeployControl = deployAddList.get(i);
                    envInfoDao.addEnvDeployCfg(svcgenDeployControl);
                }
            } else {
                envInfoDao.delEnvDeployCfg(envCode);
                for (int i = 0; i < deployAddList.size(); i++) {
                    SvcgenDeployControl svcgenDeployControl = deployAddList.get(i);
                    envInfoDao.addEnvDeployCfg(svcgenDeployControl);
                }
            }
        } else {
            envInfoDao.delEnvDeployCfg(envCode);
        }
    }

    public DSGCBusCfgVO queryEsbCfgDetails(String deicId) {
        DSGCBusCfgVO busCfgVO = new DSGCBusCfgVO();
        DsgcEnvInfoCfgBean envInfoCfg = envInfoDao.queryApiEnvCfg(deicId);
        if (null != envInfoCfg) {
            BeanUtils.copyProperties(envInfoCfg, busCfgVO);
            String envCode = envInfoCfg.getEnvCode();
            //查询子表By - envCode
            List<DSGCEnvMachineCfg> envMachineCfgs = envInfoDao.getEnvMachineCfgByEnvCode(envCode);
            List<DSGCEnvServerCfg> envServerCfgs = envInfoDao.getEnvServCfgByEnvCode(envCode);
            List<SvcgenDeployControl> deployControls = envInfoDao.getEnvDeployCfgByEnvCode(envCode);

            //设置服务器和节点的个数
            busCfgVO.setServerCount(envMachineCfgs.size());
            busCfgVO.setNodeCount(envServerCfgs.size());

            busCfgVO.setEnvMachineCfgs(envMachineCfgs);
            busCfgVO.setEnvServerCfgs(envServerCfgs);
            busCfgVO.setDeployControls(deployControls);

            return busCfgVO;
        } else {
            return null;
        }

    }
    public List<DagEnvInfoDTO> queryEnvListDetail(String envType) {
        List<DagEnvInfoDTO> result = new ArrayList<>();
        List<DsgcEnvInfoCfgBean> cfgList = envInfoDao.queryEnvListDetail(envType);
        Iterator<DsgcEnvInfoCfgBean> cfg = cfgList.iterator();
        while (cfg.hasNext()) {
            DagEnvInfoDTO dto = new DagEnvInfoDTO();
            DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean = cfg.next();
            dto.setEnvCode(dsgcEnvInfoCfgBean.getEnvCode());
            dto.setEnvName(dsgcEnvInfoCfgBean.getEnvName());
            dto.setEnvSeq(dsgcEnvInfoCfgBean.getEnvSeq());
            dto.setReqLocation(dsgcEnvInfoCfgBean.getReqLocation());
            dto.setAdminLocation(dsgcEnvInfoCfgBean.getAdminLocation());
            dto.setDsgcAdmin(dsgcEnvInfoCfgBean.getDsgcAdmin());
            dto.setDeicId(dsgcEnvInfoCfgBean.getDeicId());
            dto.setEnvType(dsgcEnvInfoCfgBean.getEnvType());
            dto.setTechType(dsgcEnvInfoCfgBean.getTechType());
            result.add(dto);
        }
        return result;

    }
}
