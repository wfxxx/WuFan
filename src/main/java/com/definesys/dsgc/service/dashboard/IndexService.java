package com.definesys.dsgc.service.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.wls.WeblogicInfoComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {

    @Autowired
    private WeblogicInfoComponent weblogicInfoComponent;

    @Autowired
    private IndexDao indexDao;


    public Map<String, Object> getServeInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = weblogicInfoComponent.getServerInfo();
        } catch (Exception e) {
            map.put("error", "获取服务器信息失败");
            e.printStackTrace();
        }
        return map;
    }

    //获取服务实例
    public Map<String, Object> getServeExam() {
      Map<String, Object> map =new HashMap<>();

        map = indexDao.getServeExam();

        return map;
    }

    //获取服务资产数据
    public JSONObject getServerAsset() {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("systemCount", indexDao.getSystems());
        jsonObject.put("serviceCount", indexDao.getServices());
//        System.out.println(jsonObject);
        return jsonObject;
    }

    //调用统计top5数据
    public List<Map<String, Object>> getTotalTop5() {
        List<Map<String, Object>> list = new ArrayList<>();

        list = indexDao.getTotalTop5();
        return list;
    }

    //实时指标
    public Map<String, Object> getRealTime() {
        Map<String, Object> map = new HashMap<>();
        map = indexDao.getRealTime();
        return map;
    }

    //服务分布数据
    public List<Map<String, Object>> getServerDistriub() {
        List<Map<String, Object>> list = new ArrayList<>();

        list = indexDao.getServerDistriub();

        return list;
    }

    /**
     * 获取平均响应时间
     * @return
     */
    public Map<String, Object> getAvgResponseTime() {
        Map<String, Object> map = new HashMap<>();
        map = indexDao.getAvgResponseTime();
        return map;
    }

    /**
     * 获取平均响应时间
     * @return
     */
    public Map<String, Object> getInvokeTimes() {
        Map<String, Object> map = new HashMap<>();
        map = indexDao.getInvokeTimes();
        return map;
    }


    //获取表空间使用情况
    public List<Map<String, Object>> getTableSpaceInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getTableSpaceInfo();
        return list;
    }

    /**
     * 获取cpu信息
     * @return
     */
    public List<Map<String, Object>> getCpuInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getCpuInfo();
        return list;
    }

    /**
     * 获取内存信息
     * @return
     */
    public List<Map<String, Object>> getMemInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getMemInfo();
        return list;
    }

    /**
     * 获取io信息
     * @return
     */
    public List<Map<String, Object>> getIoInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getIoInfo();
        return list;
    }

    /**
     * 获取网络信息
     * @return
     */
    public List<Map<String, Object>> getNetInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getNetInfo();
        return list;
    }

    /**
     * 获取磁盘信息
     * @return
     */
    public List<Map<String, Object>> getDiskInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getDiskInfo();
        return list;
    }

    /**
     * 获取ohs信息
     * @return
     */
    public List<Map<String,Object>> getOhsInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getOhsInfo();
        return list;
    }

    /**
     * 获取服务器名字
     * @return
     */
    public List<Map<String, Object>> getServerName() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getServerName();
        return list;
    }

    /**
     * 获取服务器信息
     * @return
     */
    public List<Map<String, Object>> getServerInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getServerInfo();
        return list;
    }

    /**
     * 获取总执行次数，按月
     * @return
     */
    public List<Map<String, Object>> getInvokeTimesInfo() {
        List<Map<String, Object>> list = new ArrayList<>();
        list = indexDao.getInvokeTimesInfo();
        return list;
    }

    public Map<String, Object>  getServer() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = weblogicInfoComponent.getServer();
        } catch (Exception e) {
            map.put("error", "获取服务器信息失败");
            e.printStackTrace();
        }
        return map;
    }
}
