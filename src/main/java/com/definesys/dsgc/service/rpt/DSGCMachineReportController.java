package com.definesys.dsgc.service.rpt;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "machineReport",menuName = "机器报表")
@Api(description = "机器报表",tags = "机器报表")
@RequestMapping("/dsgc/machineReport")
@RestController
public class DSGCMachineReportController {

    @Autowired
    private SWordLogger logger;
    @Autowired
    private DSGCMachineReportService machineReportService;
    @RequestMapping(value = "/getMachineMsg",method= RequestMethod.POST)
    public Response getMachineMsg(HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<String,Object>();
        String body = CommonUtils.charReader(request);
        if ("".equals(body)) {
            throw new MpaasBusinessException("请求数据为空");
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String serverName = jsonObject.getString("serverName");
       // System.out.println(startTime + " | "+ endTime + " | "+ serverName);
        Map<String,String> map= new HashMap<String,String>();
        map.put("serverName",serverName);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> memList =  machineReportService.getMachineMemMsg(map);
        List<Map<String,Object>> cpuList =  machineReportService.getMachineCpuMsg(map);
        List<Map<String,Object>> netList =  machineReportService.getMachineNetMsg(map);
        List<Map<String,Object>> ioList =  machineReportService.getMachineIoMsg(map);
        List<Map<String,Object>> diskList =  machineReportService.getMachineDiskMsg(map);
        resMap.put("memInfo",memList);
        resMap.put("cpuInfo",cpuList);
        resMap.put("netInfo",netList);
        resMap.put("ioInfo",ioList);
        resMap.put("diskInfo",diskList);
        return Response.ok().data(resMap);
    }
}
