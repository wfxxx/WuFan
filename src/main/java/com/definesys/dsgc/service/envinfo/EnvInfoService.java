package com.definesys.dsgc.service.envinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.envinfo.bean.*;
import com.definesys.dsgc.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        }else {
            envInfoDao.delApiInfoNode(envCode);
        }
    }

    public DagEnvInfoDTO queryApiEnvCfg(CommonReqBean q) {
        DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean = envInfoDao.queryApiEnvCfg(q);
        DagEnvInfoDTO dto = new DagEnvInfoDTO();
        dto.setEnvCode(dsgcEnvInfoCfgBean.getEnvCode());
        dto.setEnvName(dsgcEnvInfoCfgBean.getEnvName());
        dto.setEnvSeq(dsgcEnvInfoCfgBean.getEnvSeq());
        dto.setReqLocation(dsgcEnvInfoCfgBean.getReqLocation());
        dto.setAdminLocation(dsgcEnvInfoCfgBean.getAdminLocation());
        dto.setAdminUser(dsgcEnvInfoCfgBean.getAdminUser());
        dto.setAdminPd(dsgcEnvInfoCfgBean.getAdminPd());
        return dto;
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

    public List<DagEnvInfoDTO> queryApiEnvCfgList() {
        List<DagEnvInfoDTO> result = new ArrayList<>();
        List<DsgcEnvInfoCfgBean> cfgList = envInfoDao.queryApiEnvCfgList();
        Iterator<DsgcEnvInfoCfgBean> cfg = cfgList.iterator();
        while (cfg.hasNext()) {
            DagEnvInfoDTO dto = new DagEnvInfoDTO();
            DsgcEnvInfoCfgBean dsgcEnvInfoCfgBean = cfg.next();
            dto.setEnvCode(dsgcEnvInfoCfgBean.getEnvCode());
            dto.setEnvName(dsgcEnvInfoCfgBean.getEnvName());
            dto.setEnvSeq(dsgcEnvInfoCfgBean.getEnvSeq());
            dto.setReqLocation(dsgcEnvInfoCfgBean.getReqLocation());
            dto.setAdminLocation(dsgcEnvInfoCfgBean.getAdminLocation());
            dto.setDeicId(dsgcEnvInfoCfgBean.getDeicId());
            dto.setEnvType(dsgcEnvInfoCfgBean.getEnvType());
            result.add(dto);
        }
        return result;

    }
}
