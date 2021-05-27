package com.definesys.dsgc.service.ystar.svcgen.svccfg;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.ystar.svcgen.bean.SvcgenServiceInfo;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.DBConnVO;
import com.definesys.dsgc.service.ystar.svcgen.dbcfg.DBSvcCfgService;
import com.definesys.dsgc.service.ystar.svcgen.restcfg.RestServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.sapcfg.SapServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.soapcfg.SoapServiceConfigDTO;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: RestServiceConfigController
 * @Description: rest接口快速开发controller
 * @Author：afan
 * @Date : 2020/1/6 17:04
 */
@RestController
@Api(value = "Rest服务快速开发", tags = {"服务快速开发"})
@RequestMapping("/dsgc/serviceconfig")
public class ServiceConfigController {

    @Autowired
    private ServiceConfigService restServiceConfigService;

    @Autowired
    private DBSvcCfgService dbSvcCfgService;

    @ApiOperation("保存rest快速开发接口配置")
    @PostMapping("/saveRestServiceCode")
    public Response saveRestService(@RequestBody RestServiceConfigDTO restServiceConfigDTO, HttpServletRequest request) {
        System.out.println(restServiceConfigDTO);
        // 创建人
        String userName = request.getHeader("userName");
        return restServiceConfigService.saveRestService(restServiceConfigDTO, userName);
    }


    @ApiOperation("保存soap快速开发接口配置")
    @PostMapping("/saveSoapService")
    public Response saveSoapService(@Valid @RequestBody SoapServiceConfigDTO soapServiceConfigDTO,
                                    BindingResult result, HttpServletRequest request) {
        // 参数校验
        if (result.hasErrors()) {
            return Response.error(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 创建人
        String userName = request.getHeader("userName");
        return restServiceConfigService.saveSoapService(soapServiceConfigDTO, userName);
    }

    @ApiOperation("保存db快速开发接口配置")
    @PostMapping("/saveDBServiceCode")
    public Response saveDBServiceCode(@RequestBody TmplConfigBean tcb, HttpServletRequest request) {

        System.out.println(tcb);
        // 创建人
        String userName = request.getHeader("userName");
        return restServiceConfigService.saveDBService(tcb, userName);
    }

    @ApiOperation("保存sap快速开发接口配置")
    @PostMapping("/saveSapService")
    public Response saveSapService(@Valid @RequestBody SapServiceConfigDTO sapServiceConfigDTO,
                                   BindingResult result, HttpServletRequest request) {
        // 参数校验
        if (result.hasErrors()) {
            return Response.error(result.getAllErrors().get(0).getDefaultMessage());
        }
        // 创建人
        String userName = request.getHeader("userName");
        return restServiceConfigService.saveSapService(sapServiceConfigDTO, userName);
    }

    @ApiOperation("生成快速配置")
    @PostMapping("/generateServiceCode")
    public Response generateService(@RequestBody Map map, HttpServletRequest request) {
        String servNo = (String) map.get("servNo");
        return restServiceConfigService.generateService(servNo);
    }

    @ApiOperation("查询快速配置接口")
    @RequestMapping(value = "/pageQuerySvcGenInfo", method = RequestMethod.POST)
    public Response pageQuerySvcGenInfo(@RequestParam(required = false) String reqParam,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return restServiceConfigService.pageQuerySvcGenInfo(reqParam, pageIndex, pageSize);
    }

    @ApiOperation("根据条件查询一条数据")
    @PostMapping(value = "/querySvcGenFirstInfo")
    public Response querySvcGenFirstInfo(@RequestBody SvcgenServiceInfo svcgen) {
        return Response.ok().data(restServiceConfigService.querySvcGenFirstInfo(svcgen));
    }

    @ApiOperation("查询快速配置接口")
    @RequestMapping(value = "/querySvcGenInfoByCode", method = {RequestMethod.GET, RequestMethod.POST})
    public Response querySvcGenInfoByCode(@RequestParam(required = false) String svcCode) {
        return Response.ok().data(restServiceConfigService.querySvcGenInfoByCode(svcCode));
    }

    @ApiOperation("发布接口至项目")
    @RequestMapping(value = "/publishSvcCode", method = RequestMethod.GET)
    public Response publishSvcCode(@RequestParam String svcCode,@RequestParam String operate) {
        return restServiceConfigService.publishSvcCode(svcCode, operate);//type= ADD / DEL
    }


    @RequestMapping(value = "/getDBConnList", method = RequestMethod.GET)
    public Response getDBConnList(@RequestParam String dbType, HttpServletRequest request) {
        try {
            List<Map<String, String>> result = this.dbSvcCfgService.getDBConnList(dbType);
            return Response.ok().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }


    @RequestMapping(value = "/getDBConnDetailByName", method = RequestMethod.GET)
    public Response getDBConnDetailByName(@RequestParam String connName, HttpServletRequest request) {
        try {
            DBConnVO dbConnDetailByName = this.dbSvcCfgService.getDBConnDetailByName(connName);
            return Response.ok().setData(dbConnDetailByName);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/queryTableList", method = RequestMethod.GET)
    public Response queryTableList(@RequestParam String con0, @RequestParam String connectName,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            List<Map<String, Object>> map = this.dbSvcCfgService.queryTableList(con0, connectName);
            return Response.ok().setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/queryTableFileds", method = RequestMethod.GET)
    public Response queryTableFileds(@RequestParam String tableName, @RequestParam String connectName) {
        try {
            List<Map<String, Object>> map = this.dbSvcCfgService.queryTableFileds(tableName, connectName);
            return Response.ok().setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/generateTableTree", method = RequestMethod.POST)
    public Response generateTableTree(@RequestBody JSONObject jsonObject) {
        Object result = this.dbSvcCfgService.generateTableTree(jsonObject);
        System.out.println(jsonObject);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/generateSelectSql", method = RequestMethod.POST)
    public Response generateSelectSql(@RequestBody JSONObject jsonObject) {
        String result = this.dbSvcCfgService.generateSelectSql(jsonObject);
        return Response.ok().setData(result);
    }

    @RequestMapping(value = "/checkConnectNameIsExsit", method = RequestMethod.POST)
    public Response checkConnectNameIsExsit(@RequestBody Map<String, String> map) {
        Boolean result = this.dbSvcCfgService.checkConnectNameIsExsit(map);
        return Response.ok().setData(result);
    }


    private Response validUser(HttpServletRequest request) {
        //获取用户id
        String userId = request.getHeader("uid");
        if (userId == null) {
            return Response.error("无效的用户！");
        }
        //其它权限校验逻辑：
        return Response.ok();
    }

}

