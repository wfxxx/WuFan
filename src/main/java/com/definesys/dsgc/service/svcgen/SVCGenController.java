package com.definesys.dsgc.service.svcgen;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.svcgen.bean.*;
//import com.definesys.dsgc.aspect.annotation.AuthAspect;
import com.definesys.dsgc.service.system.bean.DSGCSystemEntities;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import io.swagger.annotations.Api;
//import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "服务资产", menuName = "服务快速配置")
@Api(description = "服务快速配置", tags = "服务快速配置")
@RequestMapping(value = "/dsgc/svcgen")
@RestController
public class SVCGenController {

    @Autowired
    private SVCGenService svc;

    @Autowired
    private IDEStepsService ides;

    @Autowired
    private RFCStepsService rfcs;

    /**
     * 获取服务快速配置基础信息
     *
     * @param srb
     * @param request
     * @return
     */

    @RequestMapping(value = "/getSvcGenBasicInfo", method = RequestMethod.POST)
    public Response getServGenBasicInfo(@RequestBody SvcGenReqBean srb,HttpServletRequest request) {
        Response validRes = this.validUser(request);
        String userId = request.getHeader("uid");
        if (Response.CODE_OK.equals(validRes.getCode())) {
            Object res = svc.getSvcGenBasicInfo(userId,srb.getServNo());
            if (res == null) {
                return Response.error("获取信息失败！");
            }
            return Response.ok().setData(res);
        } else {
            return validRes;
        }
    }

    @RequestMapping(value = "/getSvcCurUsingFileList", method = RequestMethod.POST)
    public Response getServCurrentUsingFileList(@RequestBody SvcGenReqBean srb,HttpServletRequest request) {
        Response validRes = this.validUser(request);

        if (Response.CODE_OK.equals(validRes.getCode())) {
            Object res = svc.getCurrentUsingSvcGenFileList(srb.getServNo());
            if (res == null) {
                return Response.error("获取文件列表失败！");
            }
            return Response.ok().setData(res);
        } else {
            return validRes;
        }
    }

    @RequestMapping(value = "/getSvcDeployProfileList", method = RequestMethod.POST)
    public Response getSvcDeployProfileList(@RequestBody SvcGenReqBean srb,HttpServletRequest request) {
        Response validRes = this.validUser(request);
        String userId = request.getHeader("uid");
        if (Response.CODE_OK.equals(validRes.getCode())) {
            Object res = svc.getServDeployProfileList(userId,srb.getServNo());
            if (res == null) {
                return Response.error("获取部署配置项失败！");
            }
            return Response.ok().setData(res);
        } else {
            return validRes;
        }
    }

    @RequestMapping(value = "/disableSvcDeployByDPId", method = RequestMethod.POST)
    public Response disableSvcDeployProfileByDPId(@RequestBody SvcGenReqBean srb,HttpServletRequest request) {

        String userId = request.getHeader("uid");

        int res = svc.disableDeployProfileByDPId(userId,srb.getDpId());

        if (res < 0) {
            return Response.error("无权限执行此操作！");
        } else if (res == 0) {
            return Response.error("未找到对应的部署配置项！");
        } else {
            return Response.ok().setMessage("处理成功！");
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

    @RequestMapping(value = "/generateServiceCodeAsset", method = RequestMethod.POST)
    public Response generateServiceCodeAsset(@RequestBody TmplConfigBean tcb,HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return svc.generateServiceCodeAsset(userId,tcb);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }



    @RequestMapping(value = "/generateIDEServiceCode", method = RequestMethod.POST)
    public Response generateIDEServiceCode(@RequestBody TmplConfigBean tcb,HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return Response.ok().setData(svc.generateIDEServiceCode(userId,tcb));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/deployService", method = RequestMethod.POST)
    public Response deployService(@RequestBody DeployServiceJsonBean dpl,HttpServletRequest request) {

        String userId = request.getHeader("uid");
        try {
            return svc.deployServcie(userId,dpl);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getUserCanDeployProfiles", method = RequestMethod.POST)
    public Response getUserCanDeployProfiles(@RequestBody DeployServiceJsonBean dsj,HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return Response.ok().setData(svc.getUserCanDeployList(userId,dsj.getServNo()));
    }


    @RequestMapping(value = "/queryProjectDir", method = RequestMethod.GET)
    public Response queryProjectDir(HttpServletRequest request) {
        String uId = request.getHeader("uid");
        List<SvcgenProjInfoBean> list = svc.queryProjectDir(uId);
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/queryServDirInProj", method = RequestMethod.POST)
    public Response queryServDirInProj(@RequestBody ServDirJsonBean dir,HttpServletRequest request) {
        try {
            return Response.ok().data(svc.queryServDirByProjDir(dir.getParentDirName()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，获取失败！请查看服务器日志");
        }
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


//    /**
//     * 迁移过来的代码，将被废弃
//     *
//     * @param filePath
//     * @return
//     * @deprecated
//     */
//    @RequestMapping(value = "/queryInterDirByProj", method = RequestMethod.POST)
//    public Response queryInterDirByProj(@RequestBody String filePath) {
//        JSONObject json = JSONObject.fromObject(filePath);
//        String path = json.getString("filePath");
//        if (StringUtil.isBlank(path)) {
//            return Response.error("请求参数文件路径不能为空！").setCode("error");
//        }
//        try {
//            Map<String,Object> filePathList = svc.queryInterDirByProj(path);
//            return Response.ok().data(filePathList);
//        } catch (Exception ex) {
//            return Response.error("检索文件失败").setCode("error").setMessage(ex.getMessage());
//        }
//    }

    @RequestMapping(value = "/queryAllSystem", method = RequestMethod.GET)
    public Response queryAllSystem(HttpServletRequest request) {
        String uId = request.getHeader("uid");
        List<DSGCSystemEntities> list = svc.queryAllSystem();
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/listTeamRepoTreeList", method = RequestMethod.POST)
    public Response listTeamRepoTreeList(@RequestBody VCObjectJsonBean voj,HttpServletRequest request) {
        try {
            return Response.ok().data(this.svc.listTeamRepoTreeList(voj.getRelativePath(),voj.getFileFilter()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/generateIDECustomSettingCnt", method = RequestMethod.POST)
    public Response generateIDECustomSettingCnt(@RequestBody CustomSettingJsonBean cust,HttpServletRequest request) {
        try {
            return Response.ok().setData(this.ides.generateIDECustomSettingCnt(cust.getServNo()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }


    @RequestMapping(value = "/getSapConnInfoByName", method = RequestMethod.POST)
    public Response getSapConnInfoByName(@RequestBody SapConnInfoJsonBean sci,HttpServletRequest request) {
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

    @RequestMapping(value = "/vaildSapConnInfo", method = RequestMethod.POST)
    public Response vaildSapConnInfo(@RequestBody SapConnInfoJsonBean sci,HttpServletRequest request) {
        try {
            Response vaildRes = this.validUser(request);
            if (Response.CODE_OK.equals(vaildRes.getCode())) {
                String userId = request.getHeader("uid");
                return Response.ok().data(this.rfcs.vaildSapConnInfo(userId,sci));
            } else {
                return vaildRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/syncRfcFromSap", method = RequestMethod.POST)
    public Response vaildSapConnInfo(@RequestBody RFCSyncJsonBean sj,HttpServletRequest request) {
        try {
            Response vaildRes = this.validUser(request);
            if (Response.CODE_OK.equals(vaildRes.getCode())) {
                String userId = request.getHeader("uid");
                return Response.ok().data(this.rfcs.syncRfcFromSap(userId,sj.getConnName()));
            } else {
                return vaildRes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }


    @RequestMapping(value = "/newRfcDeployProfile", method = RequestMethod.POST)
    public Response newRfcDeployProfile(@RequestBody RfcDeployProfileBean rfcDpl,HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return rfcs.newRfcDeployProfile(userId,rfcDpl);
    }

    @RequestMapping(value = "/getRfcDeployProfileDetail", method = RequestMethod.POST)
    public Response getRfcDeployProfileDetail(@RequestBody RfcDeployProfileBean rfcDpl,HttpServletRequest request) {
        return Response.ok().setData(rfcs.getRfcDeployProfileDetail(rfcDpl.getDpId()));
    }

    @RequestMapping(value = "/queryRfc", method = RequestMethod.POST)
    @ResponseBody
    public Response queryRfc(@RequestBody RFCInfoQueryBean queryInfo,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageQueryResult<RFCInfoBean> list = rfcs.queryRfc(queryInfo,pageSize,pageIndex);
        return Response.ok().data(list);
    }


    @RequestMapping(value = "/newIdeDeployProfile", method = RequestMethod.POST)
    public Response newIdeDeployProfile(@RequestBody IdeDeployProfileBean ideDpl,HttpServletRequest request) {
        String userId = request.getHeader("uid");
        return Response.ok().setData(ides.newIdeDeployProfile(userId,ideDpl));
    }

    @RequestMapping(value = "/getIdeDeployProfileDetail", method = RequestMethod.POST)
    public Response getIdeDeployProfileDetail(@RequestBody IdeDeployProfileBean ideDpl,HttpServletRequest request) {
        try {
            return Response.ok().setData(svc.getIdeDeployProfileByDpId(ideDpl.getDpId()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getCommonDeployProfileByDpId", method = RequestMethod.POST)
    public Response getCommonDeployProfileByDpId(@RequestBody SvcGenReqBean sgr,HttpServletRequest request) {
        try {
            return Response.ok().setData(this.svc.getCommonDeployProfileByDpId(sgr.getDpId()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getCommonTmplConfigByServNo", method = RequestMethod.POST)
    public Response getCommonTmplConfigByServNo(@RequestBody SvcGenReqBean sgr,HttpServletRequest request) {
        try {
            return Response.ok().setData(this.svc.getCommonTmplConfigByServNo(sgr.getServNo()));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/newOsbRestDeployProfile", method = RequestMethod.POST)
    public Response newOsbRestDeployProfile(@RequestBody RestDeployProfileBean dpl,HttpServletRequest request) {
        try {
            String uId = request.getHeader("uid");
            return Response.ok().setData(this.svc.newOsbRestDeployProfile(uId,dpl));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/newOsbSoapDeployProfile", method = RequestMethod.POST)
    public Response newOsbSoapDeployProfile(@RequestBody SoapDeployProfileBean dpl,HttpServletRequest request) {
        try {
            String uId = request.getHeader("uid");
            return Response.ok().setData(this.svc.newOsbSoapDeployProfile(uId,dpl));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/newOsbSaCmptDeployProfile", method = RequestMethod.POST)
    public Response newOsbSaCmptDeployProfile(@RequestBody SaCmptDeployProfileBean dpl,HttpServletRequest request) {
        try {
            String uId = request.getHeader("uid");
            return Response.ok().setData(this.svc.newOsbSaCmptDeployProfile(uId,dpl));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/resolveWsdl", method = RequestMethod.POST)
    public Response resolveWsdl(@RequestBody WSDLResolveBean wsdl,HttpServletRequest request) {
        try {
            if (wsdl.getWsdlUri() == null || wsdl.getWsdlUri().trim().length() == 0) {
                return Response.error("WSDL地址不能为空！");
            }
            return this.svc.resolveWsdl(wsdl);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getSgSvcObjList", method = RequestMethod.POST)
    public Response getSgSvcObjList(@RequestBody SvcGenObjReqBean q,HttpServletRequest request,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSgSvcObjList(q,uId,pageSize,pageIndex));
    }

    @RequestMapping(value = "/getSgCmptObjList", method = RequestMethod.POST)
    public Response getSgCmptObjList(@RequestBody SvcGenObjReqBean q,HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSgCmptObjList(q,uId,pageSize,pageIndex));
    }


    @RequestMapping(value = "/getSvcGenObjInfo", method = RequestMethod.POST)
    public Response getSvcGenObjInfo(@RequestBody SvcGenObjReqBean q,HttpServletRequest request) {
        String uId = request.getHeader("uid");
        return Response.ok().setData(this.svc.getSvcGenObjInfo(q.getSgObjCode(),uId));
    }

    @RequestMapping(value = "/bindingSgObjToNewServ", method = RequestMethod.POST)
    public Response bindingSgObjToNewServ(@RequestBody SVCCreateBean scc,HttpServletRequest request){
        try {
            int res = this.svc.bindingSgObjToNewServ(scc);
            if(-1 == res ){
                return Response.error("服务编号'"+scc.getServNo()+"'已存在，无法重复创建！");
            } else if (1 == res){
                return Response.ok();
            } else{
                return Response.error("处理失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/resolveServIBUriList", method = RequestMethod.POST)
    public Response resolveServIBUriList(@RequestBody SvcGenReqBean req){
        try{
            return Response.ok().setData(this.svc.resolveServIBUriList(req.getServNo()));
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/deleteSvcGenObj", method = RequestMethod.POST)
    public Response deleteSvcGenObj(@RequestBody SvcGenObjReqBean q,HttpServletRequest request){
        String uId = request.getHeader("uid");
        try{
            int res = this.svc.deleteSvcGenObj(uId,q.getSgObjCode());
            if(res == -1){
                return Response.error("无权限执行此操作！");
            } else if(res == 0){
                return Response.ok().setMessage("由于当前资源已经部署运行环境或者已经上传代码库，无法直接删除，已标记为废弃状态并提交至管理员进行清理！");
            } else if (res == 1){
                return Response.ok().setMessage("成功删除！");
            } else{
                return Response.error("未知错误，请联系管理员处理！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/deployServMetaDataByServNo", method = RequestMethod.POST)
    public Response deployServMetaDataByServNo(@RequestBody SvcGenReqBean q,HttpServletRequest request) {
        String uId = request.getHeader("uid");
        try {
            return this.svc.deployServMetaDataByServNo(uId,q.getServNo());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }

    @RequestMapping(value = "/getDBConnList",method = RequestMethod.GET)
    public Response getDBConnList(@RequestParam String dbType,HttpServletRequest request) {
        try {
            List<Map<String,String>> result = this.svc.getDBConnList(dbType);
            return Response.ok().setData(result);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }
    @RequestMapping(value = "/vaildDBConnInfo", method = RequestMethod.POST)
    public Response vaildDBConnInfo(@RequestBody DBconnVO dBconnVO){
        try {
            this.svc.vaildDBConnInfo(dBconnVO);
            return Response.ok();
        }catch (Exception e){
          e.printStackTrace();
          return Response.error("测试DB连接失败，请稍后再试！");
        }

    }

    @RequestMapping(value = "/getDBConnDetailByName",method = RequestMethod.GET)
    public Response getDBConnDetailByName(@RequestParam String connName,HttpServletRequest request) {
        try {
            DBconnVO dbConnDetailByName = this.svc.getDBConnDetailByName(connName);
            return Response.ok().setData(dbConnDetailByName);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }
    @RequestMapping(value = "/queryTableList",method = RequestMethod.GET)
    public Response queryTableList(@RequestParam String con0,@RequestParam String connectName,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            List<Map<String,Object>> map =this.svc.queryTableList(con0,connectName);
            return Response.ok().setData(map);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }
    @RequestMapping(value = "/queryTableFileds",method = RequestMethod.GET)
    public Response queryTableFileds(@RequestParam String tableName,@RequestParam String connectName) {
        try {
            List<Map<String,Object>> map =this.svc.queryTableFileds(tableName,connectName);
            return Response.ok().setData(map);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("发生错误，请联系管理员处理！");
        }
    }
    @RequestMapping(value = "/generateTableTree", method = RequestMethod.POST)
    public Response generateTableTree(@RequestBody JSONObject jsonObject){
        Object result = this.svc.generateTableTree(jsonObject);
        System.out.println(jsonObject);
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/generateSelectSql", method = RequestMethod.POST)
    public Response generateSelectSql(@RequestBody JSONObject jsonObject){
        String result = this.svc.generateSelectSql(jsonObject);
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
