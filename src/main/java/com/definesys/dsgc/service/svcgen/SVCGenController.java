package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.*;
//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@AuthAspect(menuCode = "服务资产", menuName = "服务快速配置")
@Api(description = "服务快速配置", tags = "服务快速配置")
@RequestMapping(value = "/dsgc/svcgen")
@RestController
public class SVCGenController {

    @Autowired
    private SVCGenService svc;

    @Autowired
    private RFCStepsService rfcs;

    @RequestMapping(value = "/getUserCanDeployProfiles", method = RequestMethod.POST)
    public Response getUserCanDeployProfiles(@RequestBody DeployServiceJsonBean dsj, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return Response.ok().setData(svc.getUserCanDeployList(userId, dsj.getServNo()));
    }


    @RequestMapping(value = "/queryProjectDir", method = RequestMethod.GET)
    public Response queryProjectDir(HttpServletRequest request) {
        String uId = request.getHeader("uid");
        List<SvcgenProjInfoBean> list = svc.queryProjectDir(uId);
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/getServcieAccountList", method = RequestMethod.GET)
    public Response getServcieAccountList(HttpServletRequest request) {
        try {
            String uId = request.getHeader("uid");
            return Response.ok().data(svc.getServcieAccountList(uId));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，获取失败！请查看服务器日志");
        }
    }

    @RequestMapping(value = "/queryAllSystem", method = RequestMethod.GET)
    public Response queryAllSystem(HttpServletRequest request) {
        String uId = request.getHeader("uid");
        List<DSGCSystemEntities> list = svc.queryAllSystem();
        return Response.ok().data(list);
    }


    @RequestMapping(value = "/getSapConnInfoByName", method = RequestMethod.POST)
    public Response getSapConnInfoByName(@RequestBody SapConnInfoJsonBean sci, HttpServletRequest request) {
        try {
            Response vaildRes = this.validUser(request);
            if (Response.CODE_OK.equals(vaildRes.getCode())) {
                return Response.ok().data(this.rfcs.getSapConnInfoByName(sci.getConnName()));
            } else {
                return vaildRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getSapConnList", method = RequestMethod.GET)
    public Response getSapConnList(HttpServletRequest request) {
        try {
            Response vaildRes = this.validUser(request);
            if (Response.CODE_OK.equals(vaildRes.getCode())) {
                return Response.ok().data(this.rfcs.getSapConnList());
            } else {
                return vaildRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/syncRfcFromSap", method = RequestMethod.POST)
    public Response vaildSapConnInfo(@RequestBody RFCSyncJsonBean sj, HttpServletRequest request) {
        try {
            Response vaildRes = this.validUser(request);
            if (Response.CODE_OK.equals(vaildRes.getCode())) {
                String userId = request.getHeader("uid");
                return Response.ok().data(this.rfcs.syncRfcFromSap(userId, sj.getConnName()));
            } else {
                return vaildRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getRfcDeployProfileDetail", method = RequestMethod.POST)
    public Response getRfcDeployProfileDetail(@RequestBody RfcDeployProfileBean rfcDpl, HttpServletRequest request) {
        return Response.ok().setData(rfcs.getRfcDeployProfileDetail(rfcDpl.getDpId()));
    }

    @RequestMapping(value = "/queryRfc", method = RequestMethod.POST)
    @ResponseBody
    public Response queryRfc(@RequestBody RFCInfoQueryBean queryInfo,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<RFCInfoBean> list = rfcs.queryRfc(queryInfo, pageSize, pageIndex);
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/getIdeDeployProfileDetail", method = RequestMethod.POST)
    public Response getIdeDeployProfileDetail(@RequestBody IdeDeployProfileBean ideDpl, HttpServletRequest request) {
        try {
            return Response.ok().setData(svc.getIdeDeployProfileByDpId(ideDpl.getDpId()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getCommonDeployProfileByDpId", method = RequestMethod.POST)
    public Response getCommonDeployProfileByDpId(@RequestBody SvcGenReqBean sgr, HttpServletRequest request) {
        try {
            return Response.ok().setData(this.svc.getCommonDeployProfileByDpId(sgr.getDpId()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getCommonTmplConfigByServNo", method = RequestMethod.POST)
    public Response getCommonTmplConfigByServNo(@RequestBody SvcGenReqBean sgr, HttpServletRequest request) {
        try {
            return Response.ok().setData(this.svc.getCommonTmplConfigByServNo(sgr.getServNo()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getSgSvcObjList", method = RequestMethod.POST)
    public Response getSgSvcObjList(@RequestBody SvcGenObjReqBean q, HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSgSvcObjList(q, uId, pageSize, pageIndex));
    }

    @RequestMapping(value = "/getSgCmptObjList", method = RequestMethod.POST)
    public Response getSgCmptObjList(@RequestBody SvcGenObjReqBean q, HttpServletRequest request,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSgCmptObjList(q, uId, pageSize, pageIndex));
    }


    @RequestMapping(value = "/getSvcGenObjInfo", method = RequestMethod.POST)
    public Response getSvcGenObjInfo(@RequestBody SvcGenObjReqBean q, HttpServletRequest request) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSvcGenObjInfo(q.getSgObjCode(), uId));
    }


    @RequestMapping(value = "/deleteSvcGenObj", method = RequestMethod.POST)
    public Response deleteSvcGenObj(@RequestBody SvcGenObjReqBean q, HttpServletRequest request) {
        String uId = request.getHeader("uid");
        try {
            int res = this.svc.deleteSvcGenObj(uId, q.getSgObjCode());
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

    @RequestMapping(value = "/resolveMuleWsdl", method = RequestMethod.POST)
    public Response resolveWsdl(@RequestBody WSDLResolveBean wsdl,HttpServletRequest request) {
        try {
            if (wsdl.getWsdlUri() == null || wsdl.getWsdlUri().trim().length() == 0) {
                return Response.error("WSDL地址不能为空！");
            }
            return this.svc.resolveMuleWsdl(wsdl);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/generateServiceCode", method = RequestMethod.POST)
    public Response generateServiceCode(@RequestBody TmplConfigBean tcb,HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return svc.generateServiceCode(userId,tcb);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
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
