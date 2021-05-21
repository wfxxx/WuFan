package com.definesys.dsgc.service.ystar.svcgen.bean;


import com.definesys.dsgc.service.ystar.svcgen.proj.DSGCProjDirManagerDao;
import com.definesys.dsgc.service.svclog.bean.DSGCValidResult;
import com.definesys.dsgc.service.svcmng.bean.DSGCService;
import com.definesys.mpaas.log.SWordLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenglong
 * @Description:
 * @Date 2019/3/13 16:30
 */
@Service
public class DSGCServiceService {
    @Autowired
    private DSGCServiceDao dsgcServiceDao;
    @Autowired
    SWordLogger logger;
    @Autowired
    private DSGCProjDirManagerDao projDirManagerDao;

    @Transactional
    public String insertDsgcService(DSGCServiceVO serviceVO) {
        Map<String, Object> map = GetInstace(serviceVO);
        DSGCService ds = (DSGCService) map.get("DSGCService");
        System.out.println(ds.toString());
//        List<DSGCValidResult> as = (List<DSGCValidResult>) map.get("DSGCValidResult");
        List<DSGCServicesUri> as = (List<DSGCServicesUri>) map.get("DSGCServicesUri");

        List<DSGCTab> tags = (List<DSGCTab>) map.get("tags");
        return this.dsgcServiceDao.insertDsgcService(ds, as, tags);
    }

    @Transactional
    public String updateDsgcService(DSGCServiceVO serviceVO) {
        Map<String, Object> map = GetInstace(serviceVO);
        DSGCService ds = (DSGCService) map.get("DSGCService");
        System.out.println(ds.toString());
        List<DSGCServicesUri> as = (List<DSGCServicesUri>) map.get("DSGCServicesUri");
        List<DSGCTab> tags = (List<DSGCTab>) map.get("tags");
        this.dsgcServiceDao.updateDsgcService(ds, as, tags);
        return serviceVO.getServNo();
    }

    @Transactional
    public String deleteById(DSGCServiceVO serviceVO) {
        Map<String, Object> map = GetInstace(serviceVO);
        DSGCService ds = (DSGCService) map.get("DSGCService");

        DSGCValidResult as = (DSGCValidResult) map.get("DSGCServicesUri");
        this.dsgcServiceDao.deleteById(ds, as);
        return ds.getServNo();
    }
    /**
     * 提取共同的封装对象的方法
     */
    public Map<String, Object> GetInstace(DSGCServiceVO serviceVO) {

        DSGCService ds = new DSGCService();
        ds.setServNo(serviceVO.getServNo());
        ds.setServStatus(serviceVO.getServStatus());
        ds.setServName(serviceVO.getServName());
        ds.setServUrl(serviceVO.getServUrl());
        ds.setTechType(serviceVO.getTechType());
        ds.setTransType(serviceVO.getTransType());
        ds.setCategoryLevel1(serviceVO.getCategoryLevel1());
        ds.setCategoryLevel2(serviceVO.getCategoryLevel2());
        ds.setCategoryLevel3(serviceVO.getCategoryLevel3());
        ds.setCategoryLevel4(serviceVO.getCategoryLevel4());
        ds.setBizDesc(serviceVO.getBizDesc());
        ds.setTechDesc(serviceVO.getTechDesc());
        ds.setServDesc(serviceVO.getServDesc());
        ds.setFileName(serviceVO.getFileName());
        ds.setFilePath(serviceVO.getFilePath());
        ds.setServId(serviceVO.getServId());
        ds.setServTempLate(serviceVO.getServTemplate());
        ds.setDataTypeCode(serviceVO.getDataTypeCode());
        ds.setSubordinateSystem(serviceVO.getSubordinateSystem());
        ds.setAttribue4(serviceVO.getAttribue4());
        ds.setSpyOper(serviceVO.getSpyOper());
        DSGCValidResult as = new DSGCValidResult();
        List<DSGCServicesUri> list = serviceVO.getServicesUris();
        List<DSGCTab> tags = serviceVO.getTags();

        Map<String, Object> map = new HashMap<>();
        map.put("DSGCService", ds);
        map.put("tags", tags);
        map.put("DSGCServicesUri", list);
        return map;
    }

}