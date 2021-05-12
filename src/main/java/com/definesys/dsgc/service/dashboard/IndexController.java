package com.definesys.dsgc.service.dashboard;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.lkv.FndLookupTypeService;
import com.definesys.dsgc.service.utils.OperatingSystemMXBeanUtil;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/dsgc/index")
@RestController
public class IndexController {
    @Autowired
    private IndexService indexService;
//
//    @Autowired
//    FndLookupTypeService fndLookupTypeService;
//
//    /**
//     * 首页报表
//     *
//     * @return
//     */
//    @RequestMapping(value = "/getIndexData", method = RequestMethod.GET)
//    public Response getIndexData() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        Map<String, Object> servermap = indexService.getServeInfo();
////        long totalMemorySize = OperatingSystemMXBeanUtil.getTotalMemorySize();
////        long usedMemory = OperatingSystemMXBeanUtil.getUsedMemory();
//////        servermap.put("MEM_TOTAL",totalMemorySize);
//////        servermap.put("MEM_USE",usedMemory);
////        String osName = OperatingSystemMXBeanUtil.getOsName();
////        System.out.println("服务器"+osName +"----------"+totalMemorySize+"----------"+ usedMemory);
//        Map<String, Object> serverExam = indexService.getServeExam();
//
//        JSONObject serverAsset = indexService.getServerAsset();
//        List<Map<String, Object>> totalTop5 = indexService.getTotalTop5();
//        Map<String, Object> realTime = indexService.getRealTime();
//        List<Map<String, Object>> serverDistriub = indexService.getServerDistriub();
//
//        Map<String,Object> avgResponseTime = indexService.getAvgResponseTime();
//        Map<String,Object> invokeTimes = indexService.getInvokeTimes();
//        List<Map<String,Object>> tableSpaceInfo = indexService.getTableSpaceInfo();
//
//        List<Map<String,Object>> cpuInfo = indexService.getCpuInfo();
//        List<Map<String,Object>> memInfo = indexService.getMemInfo();
//        List<Map<String,Object>> ioInfo = indexService.getIoInfo();
//        List<Map<String,Object>> netInfo = indexService.getNetInfo();
//        List<Map<String,Object>> diskInfo = indexService.getDiskInfo();
//
//        /**
//         * 将LOOKUP_CODE对应的值列表下面的所有值put进map
//         */
//        List<Map<String,Object>> serverInfo = fndLookupTypeService.getLookUpValue("COLLECT_PC_INFO");
//        for (Map<String,Object> serverInfoMap:
//                serverInfo
//        ){
//            String server = (String) serverInfoMap.get("LOOKUP_CODE");
//            List<Map<String, Object>> serverDescList   = fndLookupTypeService.getLookUpValue(server);
//            for (Map<String, Object> serverDesc: serverDescList
//            ){
//                serverInfoMap.put((String) serverDesc.get("LOOKUP_CODE"), serverDesc.get("MEANING"));
//            }
//        }
//
//        List<Map<String,Object>> serverName = indexService.getServerName();
//        HashMap<String, Object> dbMap = new HashMap<>();
//        dbMap.put("SERVER","index_db");
//        serverName.add(dbMap);
//        for (Map<String,Object> serverNameMap:
//                serverName
//        ){
//            String server = (String) serverNameMap.get("SERVER");
//            server += _SUFFIX;
//            List<Map<String, Object>> serverDescList   = fndLookupTypeService.getLookUpValue(server);
//            for (Map<String, Object> serverDesc: serverDescList
//            ){
//                serverNameMap.put((String) serverDesc.get("LOOKUP_CODE"), serverDesc.get("MEANING"));
//            }
//        }
//
//        List<Map<String,Object>> runServerInfo = indexService.getServerInfo();
//        List<Map<String,Object>> invokeTimesInfo = indexService.getInvokeTimesInfo();
//
//
//        map.put("topFive", totalTop5);   //失败数最多的前5个
//        map.put("serviceExample", serverExam);  //服务实例
//        map.put("serviceDistribution", serverDistriub);  //服务分布 index2未用
//        map.put("realTime", realTime);
//        map.put("serverAsset", serverAsset);
//        map.put("serverInfo", servermap);//获取服务器信息
//
//        map.put("avgResponseTime", avgResponseTime);
//        map.put("invokeTimes", invokeTimes);
//        map.put("tableSpaceInfo", tableSpaceInfo);
//
//
//
//        map.put("cpuInfo", cpuInfo);
//        map.put("memInfo", memInfo);
//        map.put("ioInfo", ioInfo);
//        map.put("netInfo", netInfo);
//        map.put("diskInfo", diskInfo);
//        map.put("tabServerInfo", serverInfo);
//
//
//        map.put("serverName", serverName);
//        map.put("runServerInfo", runServerInfo);
//        map.put("invokeTimesInfo", invokeTimesInfo);
//
//        return Response.ok().data(map);
//    }
//
    @RequestMapping(value = "/getSysDetail",method = RequestMethod.POST)
    public Response getSysDetail() {
        return Response.ok().data(this.indexService.getServerAsset());
    }
//
//    //获取服务器信息
//    @RequestMapping(value="/getServeInfo" ,method= RequestMethod.GET)
//    public Response getServeInfo() {
//        Map<String,Object> map=new HashMap<String,Object>();
//        Map<String, Object> servermap= indexService.getServeInfo();
//        double jvmRate = OperatingSystemMXBeanUtil.getProcessCpuLoad();
//        map.put("serveInfo", servermap);//获取服务实例
//        map.put("jvmRate", jvmRate +"");
//        return Response.ok().data(map);
//    }
//
//
//    //获取服务实例
//    @RequestMapping(value = "/getServeExam", method = RequestMethod.GET)
//    public Response getServeExam() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        Map<String, Object> serverExam = indexService.getServeExam();
//        map.put("ServerExam", serverExam);//获取服务实例
//        return Response.ok().data(map);
//    }
//
//    //获取服务资产数据
//    @RequestMapping(value = "/getServerAsset", method = RequestMethod.GET)
//    public Response getServerAsset() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        JSONObject serverAsset = indexService.getServerAsset();
//        map.put("ServerAsset", serverAsset);//获取服务资产数据
//        return Response.ok().data(map);
//    }
//
//    //调用统计top5数据
//    @RequestMapping(value = "/getTotalTop5", method = RequestMethod.GET)
//    public Response getTotalTop5() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        List<Map<String, Object>> totalTop5 = indexService.getTotalTop5();
//        map.put("TotalTop5", totalTop5);//调用统计top5数据
//        return Response.ok().data(map);
//    }
//
//    //实时指标
//    @RequestMapping(value = "/getRealTime", method = RequestMethod.GET)
//    public Response getRealTime() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        Map<String, Object> realTime = indexService.getRealTime();
//        map.put("RealTime", realTime);//实时指标
//        return Response.ok().data(map);
//    }
//
//    //服务分布数据
//    @RequestMapping(value = "/getServerDistriub", method = RequestMethod.GET)
//    public Response getServerDistriub() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        List<Map<String, Object>> serverDistriub = indexService.getServerDistriub();
//        map.put("ServerDistriub", serverDistriub);//服务分布数据
//        return Response.ok().data(map);
//    }
//
//    // 获取首页二Tab标签数据
//    @RequestMapping(value = "/getHeaderData", method = RequestMethod.GET)
//    public Response getHeaderData() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        // 获取系统数量和接口数量
//        JSONObject serverAsset = indexService.getServerAsset();
//        // 获取平均响应时间
//        Map<String,Object> avgResponseTime = indexService.getAvgResponseTime();
//        // 获取并发量
//        Map<String, Object> realTime = indexService.getRealTime();
//        // 获取接口运行次数和失败次数
//        Map<String,Object> invokeTimes = indexService.getInvokeTimes();
//        map.put("serverAsset", serverAsset);
//        map.put("avgResponseTime", avgResponseTime);
//        map.put("realTime", realTime);
//        map.put("invokeTimes", invokeTimes);
//        return Response.ok().data(map);
//    }
//
//    // 获取首页二服务数据
//    @RequestMapping(value = "/getServiceData",method = RequestMethod.GET)
//    public Response getServiceData() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        // 获取当天总执行次数
//        List<Map<String,Object>> invokeTimesInfo = indexService.getInvokeTimesInfo();
//        // 获取失败top5
//        List<Map<String, Object>> totalTop5 = indexService.getTotalTop5();
//        map.put("invokeTimesInfo",invokeTimesInfo);
//        map.put("topFive",totalTop5);
//        return Response.ok().data(map);
//    }
//
//    //获取首页二系统资源数据
//    @RequestMapping(value = "/getSystemResourceData",method = RequestMethod.GET)
//    public Response getSystemResourceData() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        //获取ohs信息
////        List<Map<String,Object>> ohsInfo = indexService.getOhsInfo();
//        // 获取服务器名称
//        Map<String, Object> servermap = indexService.getServer();
//        // 获取cpu信息
//        List<Map<String,Object>> cpuInfo = indexService.getCpuInfo();
//        // 获取内存使用情况
//        List<Map<String,Object>> memInfo = indexService.getMemInfo();
//        // 获取网络使用情况
//        List<Map<String,Object>> netInfo = indexService.getNetInfo();
//        // 获取IO使用情况
//        List<Map<String,Object>> ioInfo = indexService.getIoInfo();
//        // 获取磁盘使用情况
//        List<Map<String,Object>> diskInfo = indexService.getDiskInfo();
//        // 获取状态占比信息
//        /**
//         * 将LOOKUP_CODE对应的值列表下面的所有值put进map
//         */
//        List<Map<String,Object>> serverInfo = fndLookupTypeService.getLookUpValue("COLLECT_PC_INFO");
//        for (Map<String,Object> serverInfoMap:
//                serverInfo
//        ){
//            String server = (String) serverInfoMap.get("LOOKUP_CODE");
//            List<Map<String, Object>> serverDescList   = fndLookupTypeService.getLookUpValue(server);
//            for (Map<String, Object> serverDesc: serverDescList
//            ){
//                serverInfoMap.put((String) serverDesc.get("LOOKUP_CODE"), serverDesc.get("MEANING"));
//            }
//        }
//        // 获取tabs标签信息
//        List<Map<String,Object>> tableSpaceInfo = indexService.getTableSpaceInfo();
//
//
//        map.put("tableSpaceInfo", tableSpaceInfo);
//
////        map.put("ohsInfo",ohsInfo); // 获取ohs信息
//        map.put("tabServerInfo", serverInfo);
//        map.put("serverInfo", servermap);//获取服务器信息
//        map.put("cpuInfo", cpuInfo);
//        map.put("memInfo", memInfo);
//        map.put("netInfo", netInfo);
//        map.put("ioInfo", ioInfo);
//        map.put("diskInfo", diskInfo);
//        return Response.ok().data(map);
//    }
//
//
//    // 获取首页二JVM资源信息
//    @RequestMapping(value = "/getJVMResourceData", method = RequestMethod.GET)
//    public Response getJVMResourceData() {
//        Map<String, Object> map = new HashMap<>();
//        // 获取JVM资源状态占比
//        List<Map<String,Object>> serverName = indexService.getServerName();
//        HashMap<String, Object> dbMap = new HashMap<>();
//        dbMap.put("SERVER","index_db");
//        serverName.add(dbMap);
//        for (Map<String,Object> serverNameMap:
//                serverName
//        ){
//            String server = (String) serverNameMap.get("SERVER");
//            server += _SUFFIX;
//            List<Map<String, Object>> serverDescList   = fndLookupTypeService.getLookUpValue(server);
//            for (Map<String, Object> serverDesc: serverDescList
//            ){
//                serverNameMap.put((String) serverDesc.get("LOOKUP_CODE"), serverDesc.get("MEANING"));
//            }
//        }
//        List<Map<String,Object>> runServerInfo = indexService.getServerInfo();
//        map.put("runServerInfo", runServerInfo);
//        map.put("serverName", serverName);
//        return Response.ok().data(map);
//    }
}

