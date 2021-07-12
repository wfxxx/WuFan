package com.definesys.dsgc.service.ystar.svcgen;

import com.definesys.dsgc.service.ystar.svcgen.conn.SvcGenConnService;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: SvcGenController
 * @Description: SvcGenController
 * @Author：ystar
 * @Date : 2021/4/15 17:00
 */
@RestController
@Api(value = "敏捷服务", tags = {"服务快速开发"})
@RequestMapping("/dsgc/ystar/svcgen")
public class SvcGenController {

    @Autowired
    private SvcGenConnService connService;

    @ApiOperation("服务连接配置")
    @PostMapping("/conn/pageQuerySvcGenConn")
    public Response pageQuerySvcGenConn(@RequestBody SvcgenConnBean svcgenConnBean,
                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.connService.pageQuerySvcGenConn(svcgenConnBean, pageIndex, pageSize);
    }

    @PostMapping("/conn/querySvcGenConnList")
    public Response pageQuerySvcGenConnInfo(@RequestBody SvcgenConnBean svcgenConnBean) {
        return this.connService.querySvcGenConnList(svcgenConnBean);
    }

    @RequestMapping(value = "/conn/checkConnectNameIsExist", method = RequestMethod.POST)
    public Response checkConnectNameIsExist(@RequestBody SvcgenConnBean svcgenConnBean) {
        return this.connService.checkConnectNameIsExist(svcgenConnBean);
    }

    @RequestMapping(value = "/conn/checkConnInfoValid", method = RequestMethod.POST)
    public Response checkConnInfoValid(@RequestBody SvcgenConnBean svcgenConnBean) {
        return this.connService.checkConnInfoValid(svcgenConnBean);
    }

    @RequestMapping(value = "/conn/saveSvcGenConnectInfo", method = RequestMethod.POST)
    public Response saveSvcGenConnectInfo(@RequestBody SvcgenConnBean svcgenConnBean) {
        return this.connService.saveSvcGenConnectInfo(svcgenConnBean);
    }







}
