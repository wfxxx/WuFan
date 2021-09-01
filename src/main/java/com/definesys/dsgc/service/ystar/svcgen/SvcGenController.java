package com.definesys.dsgc.service.ystar.svcgen;

import com.definesys.dsgc.service.svcgen.bean.SvcGenObjReqBean;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.svcgen.bean.WSDLResolveBean;
import com.definesys.dsgc.service.ystar.mg.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.mg.conn.bean.DBconnVO;
import com.definesys.dsgc.service.ystar.svcgen.conn.SvcGenConnService;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.mule.MuleSvcGenService;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSvgCode;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSvgDeploy;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private MuleSvcGenService muleSvcGenService;

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

    @GetMapping("/conn/listQuerySvcGenConnByType")
    public Response listQuerySvcGenConnByType(@RequestParam String dbType) {
        return this.connService.listQuerySvcGenConnByType(dbType);
    }


    @RequestMapping(value = "/conn/sigQueryDBConnByName", method = RequestMethod.GET)
    public Response sigQueryDBConnByName(@RequestParam String connName) {
        try {
            DBconnVO dbConnDetailByName = this.connService.sigQueryDBConnByName(connName);
            return Response.ok().setData(dbConnDetailByName);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }


    @RequestMapping(value = "/conn/saveSvcGenConnectInfo", method = RequestMethod.POST)
    public Response saveSvcGenConnectInfo(@RequestBody SvcgenConnBean svcgenConnBean) {
        return this.connService.saveSvcGenConnectInfo(svcgenConnBean);
    }

    @GetMapping(value = "/conn/delSvcGenConnById")
    public Response delSvcGenConnById(@RequestParam String connId) {
        return this.connService.removeSvcGenConnById(connId);
    }

    @RequestMapping(value = "/svg/saveMuleGenerateCode", method = RequestMethod.POST)
    public Response saveMuleGenerateCode(@RequestBody TmplConfigBean tcb, HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return muleSvcGenService.saveMuleGenerateCode(userId, tcb);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/svg/saveMuleGenerateSACode", method = RequestMethod.POST)
    public Response saveMuleGenerateSACode(@RequestBody TmplConfigBean tcb, HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return muleSvcGenService.saveMuleGenerateSACode(userId, tcb);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/svg/pageQueryMuleSvgSACode", method = RequestMethod.POST)
    public Response getSgCmptObjList(@RequestBody SvcGenObjReqBean q, HttpServletRequest request,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.muleSvcGenService.pageQueryMuleSvgSACode(q, uId, pageSize, pageIndex));
    }

    @RequestMapping(value = "/svg/resolveMuleSoapWsdl", method = RequestMethod.POST)
    public Response resolveWsdl(@RequestBody WSDLResolveBean wsdl) {
        try {
            if (wsdl.getWsdlUri() == null || wsdl.getWsdlUri().trim().length() == 0) {
                return Response.error("WSDL地址不能为空！");
            }
            return this.muleSvcGenService.resolveMuleSoapWsdl(wsdl);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/svg/pageQueryMuleSvgCode", method = RequestMethod.POST)
    public Response pageQueryMuleSvgCode(@RequestBody CommonReqBean commonReqBean, HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return this.muleSvcGenService.pageQueryMuleSvgCode(commonReqBean, pageIndex, pageSize);
    }

    @PostMapping(value = "/svg/listQueryMuleSvgCode")
    public Response listQueryMuleSvgCode(@RequestBody MuleSvgCode muleSvgCode) {
        return this.muleSvcGenService.listQueryMuleSvgCode(muleSvgCode);
    }


    @RequestMapping(value = "/svg/delMuleSvgById", method = RequestMethod.GET)
    public Response delMuleSvgById(@RequestParam String mscId) {
        return this.muleSvcGenService.delMuleSvgById(mscId);
    }

    @RequestMapping(value = "/svg/sigQueryMuleSvgByCode", method = RequestMethod.GET)
    public Response sigQueryMuleSvgByCode(@RequestParam String svgCode) {
        return this.muleSvcGenService.sigQueryMuleSvgByCode(svgCode);
    }


    @RequestMapping(value = "/svg/listQueryMuleSvgDeployByCode", method = RequestMethod.GET)
    public Response listQueryMuleSvgDeployByCode(@RequestParam String svgCode) {
        return this.muleSvcGenService.listQueryMuleSvgDeployByCode(svgCode);
    }

    @RequestMapping(value = "/svg/sigQueryMuleSvgDeployById", method = RequestMethod.GET)
    public Response sigQueryMuleSvgDeployById(@RequestParam String msdId) {
        return this.muleSvcGenService.sigQueryMuleSvgDeployById(msdId);
    }

    @PostMapping(value = "/svg/saveMuleSvgDeploy")
    public Response saveMuleSvgDeploy(@RequestBody MuleSvgDeploy muleSvgDeploy) {
        return this.muleSvcGenService.saveMuleSvgDeploy(muleSvgDeploy);
    }

    @PostMapping(value = "/svg/deploySvgMuleCode")
    public Response deploySvgMuleCode(@RequestBody MuleSvgDeploy muleSvgDeploy, HttpServletRequest request) {
        String uid = request.getHeader("uid");
        return this.muleSvcGenService.deploySvgMuleCode(muleSvgDeploy, uid);
    }

    @GetMapping(value = "/svg/delDeployProfileById")
    public Response delDeployProfileById(@RequestParam String msdId) {
        return this.muleSvcGenService.delDeployProfileById(msdId);
    }

}
