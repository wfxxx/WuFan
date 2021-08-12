package com.definesys.dsgc.service.ystar.mg;

import com.definesys.dsgc.service.ystar.mg.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.mg.conn.MuleSvcConnService;
import com.definesys.dsgc.service.ystar.mg.conn.bean.MuleSvcConnBean;
import com.definesys.dsgc.service.ystar.mg.otr.OtrService;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryLktParamBean;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.ystar.mg.prj.MulePrjService;
import com.definesys.dsgc.service.ystar.mg.prj.bean.MulePrjInfoBean;
import com.definesys.dsgc.service.ystar.mg.svc.SvcInfoService;
import com.definesys.dsgc.service.ystar.mg.svc.bean.SvcInfoView;
import com.definesys.dsgc.service.ystar.mg.conn.bean.DBconnVO;
import com.definesys.dsgc.service.svcgen.bean.SVCCreateBean;
import com.definesys.dsgc.service.svcgen.bean.SvcGenObjReqBean;
import com.definesys.dsgc.service.svcgen.bean.SvcGenReqBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "Mule服务快速配置", tags = "Mule服务快速配置")
@RequestMapping(value = "/dsgc/mg")
@RestController("MuleGenController")
public class MuleGenController {
    @Autowired
    private MulePrjService prjService;
    @Autowired
    private SvcInfoService svcInfoService;
    @Autowired
    private OtrService otrService;
    @Autowired
    private MuleGenService muleGenService;
    @Autowired
    private MuleSvcConnService connService;

    /**************************************** 服务信息******************************/
    @PostMapping("/svc/listQuerySvcInfo")
    public Response listQuerySvcInfo(@RequestBody SvcInfoView svcInfoView, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return this.svcInfoService.listQuerySvcInfo(svcInfoView, userId);
    }

    /**************************************** 服务连接配置 MULE CONN******************************/
    @PostMapping("/conn/pageQueryMuleConn")
    public Response pageQuerySvcGenConn(@RequestBody MuleSvcConnBean connBean,
                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.connService.pageQuerySvcGenConn(connBean, pageIndex, pageSize);
    }

    @PostMapping("/conn/listQuerySvcGenConn")
    public Response listQuerySvcGenConn(@RequestBody MuleSvcConnBean connBean) {
        return this.connService.listQuerySvcGenConn(connBean);
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

    @RequestMapping(value = "/conn/checkConnectNameIsExist", method = RequestMethod.POST)
    public Response checkConnectNameIsExist(@RequestBody MuleSvcConnBean connBean) {
        return this.connService.checkConnectNameIsExist(connBean);
    }

    @RequestMapping(value = "/conn/checkConnInfoValid", method = RequestMethod.POST)
    public Response checkConnInfoValid(@RequestBody MuleSvcConnBean connBean) {
        return this.connService.checkConnInfoValid(connBean);
    }

    @RequestMapping(value = "/conn/saveSvcGenConnectInfo", method = RequestMethod.POST)
    public Response saveSvcGenConnectInfo(@RequestBody MuleSvcConnBean connBean) {
        return this.connService.saveSvcGenConnectInfo(connBean);
    }

    /**************************************** 服务目录管理 MULE PRJ MNG ***************************/
    @PostMapping("/prj/pageQueryMulePrjInfo")
    public Response pageQueryMulePrjInfo(@RequestBody CommonReqBean commonReqBean,
                                         @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.prjService.pageQueryMulePrjInfo(commonReqBean, pageIndex, pageSize);
    }

    @PostMapping("/prj/listQueryMulePrjInfo")
    public Response listQueryMulePrjInfo(@RequestBody MulePrjInfoBean prjInfoBean) {
        return this.prjService.listQueryMulePrjInfo(prjInfoBean);
    }

    @PostMapping("/prj/saveMulePrjInfo")
    public Response saveMulePrjInfo(@RequestBody MulePrjInfoBean prjInfoBean) {
        return this.prjService.saveMulePrjInfo(prjInfoBean);
    }

    @GetMapping("/prj/checkMulePrjNameIsExist")
    public Response checkMulePrjNameIsExist(@RequestParam String prjName) {
        return this.prjService.checkMulePrjNameIsExist(prjName);
    }

    @PostMapping("/prj/sigQueryMulePrjById")
    public Response sigQueryMulePrjById(@RequestBody MulePrjInfoBean prjInfoBean) {
        return this.prjService.sigQueryMulePrjById(prjInfoBean);
    }

    @GetMapping("/prj/delMulePrjInfoById")
    public Response delMulePrjInfoById(@RequestParam String prjId) {
        return this.prjService.delMulePrjInfoById(prjId);
    }

    @ApiOperation("初始化项目")
    @GetMapping(value = "/prj/initMuleProject")
    public Response initMuleProject(@RequestParam String prjId) {
        return this.prjService.initMuleProject(prjId);
    }

    /**************************************** mg home ******************************/
    @RequestMapping(value = "/mh/getSgSvcObjList", method = RequestMethod.POST)
    public Response getSgSvcObjList(@RequestBody SvcGenObjReqBean q, HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.muleGenService.getSgSvcObjList(q, uId, pageSize, pageIndex));
    }

    @RequestMapping(value = "/mh/getSgCmptObjList", method = RequestMethod.POST)
    public Response getSgCmptObjList(@RequestBody SvcGenObjReqBean q, HttpServletRequest request,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.muleGenService.getSgCmptObjList(q, uId, pageSize, pageIndex));
    }


    @RequestMapping(value = "/mh/getSvcGenObjInfo", method = RequestMethod.POST)
    public Response getSvcGenObjInfo(@RequestBody SvcGenObjReqBean q, HttpServletRequest request) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.muleGenService.getSvcGenObjInfo(q.getSgObjCode(), uId));
    }

    @RequestMapping(value = "/mh/bindingSgObjToNewServ", method = RequestMethod.POST)
    public Response bindingSgObjToNewServ(@RequestBody SVCCreateBean scc, HttpServletRequest request) {
        try {
            int res = this.muleGenService.bindingSgObjToNewServ(scc);
            if (-1 == res) {
                return Response.error("服务编号'" + scc.getServNo() + "'已存在，无法重复创建！");
            } else if (1 == res) {
                return Response.ok();
            } else {
                return Response.error("处理失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/mh/resolveServIBUriList", method = RequestMethod.POST)
    public Response resolveServIBUriList(@RequestBody SvcGenReqBean req) {
        try {
            return Response.ok().setData(this.muleGenService.resolveServIBUriList(req.getServNo()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/mh/deleteSvcGenObj", method = RequestMethod.POST)
    public Response deleteSvcGenObj(@RequestBody SvcGenObjReqBean q, HttpServletRequest request) {
        String uId = request.getHeader("uid");
        try {
            int res = this.muleGenService.deleteSvcGenObj(uId, q.getSgObjCode());
            if (res == -1) {
                return Response.error("无权限执行此操作！");
            } else if (res == 0) {
                return Response.ok().setMessage("由于当前资源已经部署运行环境或者已经上传代码库，无法直接删除，已标记为废弃状态并提交至管理员进行清理！");
            } else if (res == 1) {
                return Response.ok().setMessage("成功删除！");
            } else {
                return Response.error("未知错误，请联系管理员处理！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    /**************************************** OTR ******************************/
    @RequestMapping(value = "/otr/queryProperties", method = {RequestMethod.POST, RequestMethod.POST})
    public Response queryProperties(@RequestBody QueryPropertyParamBean property) {
        List<QueryPropertyParamBean> dataList = this.otrService.queryProperties(property);
        return Response.ok().data(dataList);
    }

    @RequestMapping(value = "/otr/queryLkvList", method = RequestMethod.POST)
    public Response queryLkvList(@RequestBody QueryLktParamBean lkvParamBean) {
        return Response.ok().data(this.otrService.queryLkvList(lkvParamBean));
    }
}
