package com.definesys.dsgc.service.rpt;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.rpt.DSGCServerReportService;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import io.swagger.annotations.Api;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "serverReport",menuName = "服务器报表")
@Api(description = "服务器报表",tags = "服务器报表")
@RequestMapping("/dsgc/serverReport")
@RestController
public class DSGCServerReportController {

    @Autowired
    private SWordLogger logger;
    @Autowired
    private DSGCServerReportService serverReportService;
    @RequestMapping(value = "/getServerMsg",method= RequestMethod.POST)
    public Response getServerMsg(HttpServletRequest request){
        Map<String,Object> resMap=new HashMap<String,Object>();
        String body = CommonUtils.charReader(request);
        if ("".equals(body)) {
            throw new MpaasBusinessException("请求数据为空");
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String serverName = jsonObject.getString("serverName");
        Map<String,String> map= new HashMap<String,String>();
        map.put("serverName",serverName);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> List =  serverReportService.getServerMsg(map);
//        List<Map<String,Object>> cpuList =  serverReportService.getServerCpuMsg(map);
//        List<Map<String,Object>> netList =  serverReportService.getServerNetMsg(map);
//        List<Map<String,Object>> ioList =  serverReportService.getServerIoMsg(map);
//        List<Map<String,Object>> diskList =  serverReportService.getServerDiskMsg(map);
        resMap.put("serverInfo",List);
//        resMap.put("cpuInfo",cpuList);
//        resMap.put("netInfo",netList);
//        resMap.put("ioInfo",ioList);
//        resMap.put("diskInfo",diskList);
        return Response.ok().data(resMap);
    }
}
