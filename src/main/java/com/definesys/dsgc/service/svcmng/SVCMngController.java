package com.definesys.dsgc.service.svcmng;

import com.definesys.dsgc.service.svcmng.bean.*;
import com.definesys.mpaas.common.http.Response;
import jdk.management.resource.internal.ResourceNatives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "dsgc/svcmng")
public class SVCMngController {

    @Autowired
    private SVCMngService svcMngService;

    @RequestMapping(value = "/querySvcMngServList",method = RequestMethod.POST)
    public Response querySvcMngServList(@RequestBody SVCCommonReqBean param,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole= request.getHeader("userRole");
        return Response.ok().setData(svcMngService.querySvcMngServList(param,pageIndex,pageSize,userId,userRole));
    }
    @RequestMapping(value = "/generateMsgExample",method = RequestMethod.POST)
    public Response generateMsgExample(@RequestBody SVCMngGenerateMsgVO param){
        Map<String,Object> result = null;
        try {
            result = svcMngService.generateMsgExample(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/generateMsgParameter",method = RequestMethod.POST)
    public Response generateMsgParameter(@RequestBody SVCMngGenerateMsgVO param){
        List<SVCMngIoParameterDTO> result = new ArrayList<>();
        try {
         result =  svcMngService.generateMsgParameter(param);

        }catch (Exception e){
            e.printStackTrace();
        }

        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/initSvcMngIoParameterData",method = RequestMethod.POST)
    public Response initSvcMngIoParameterData(@RequestBody SVCCommonReqBean param){
        Map<String ,Object> result =  svcMngService.initSvcMngIoParameterData(param.getCon0());
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryBasicInfoByServNo",method = RequestMethod.POST)
    public Response queryBasicInfoByServNo(@RequestBody SVCCommonReqBean param){
        SVCServBasicInfoDTO result =  svcMngService.queryBasicInfoByServNo(param.getCon0());
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/saveServBasicInfo",method = RequestMethod.POST)
    public Response saveServBasicInfo(@RequestBody SVCServBasicInfoDTO svcServBasicInfo){
        svcMngService.saveServBasicInfo(svcServBasicInfo);
        return Response.ok();
    }
    @RequestMapping(value = "/queryServUri",method = RequestMethod.POST)
    public Response queryServUri(@RequestBody SVCCommonReqBean param){
        List<ServUriDTO> result =  svcMngService.queryServUri(param.getCon0());
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/delServUri",method = RequestMethod.POST)
    public Response delServUri(@RequestBody SVCCommonReqBean param){
        List<ServUriDTO> result =  svcMngService.queryServUri(param.getCon0());
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryServUriParamter",method = RequestMethod.POST)
    public Response queryServUriParamter(@RequestBody SVCCommonReqBean param){
        List<ServUriParamterDTO> result =  svcMngService.queryServUriParamter(param.getCon0());
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/queryUriParamTemplateList",method = RequestMethod.GET)
    public Response queryUriParamTemplateList(){
        return Response.ok().setData(svcMngService.queryUriParamTemplateList());
    }
    @RequestMapping(value = "/saveAsTemplate",method = RequestMethod.POST)
    public Response saveAsTemplate(@RequestBody SaveAsTemplateVO vo,HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole= request.getHeader("userRole");
        if(!"SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
        return Response.error("无权限操作");
        }
        svcMngService.saveAsTemplate(vo);
        return Response.ok();
    }
    @RequestMapping(value = "/saveServLocationData",method = RequestMethod.POST)
    public Response saveServLocationData(@RequestBody SaveServLocationDataVO vo,HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole= request.getHeader("userRole");
        if(!"SuperAdministrators".equals(userRole) || "Administrators".equals(userRole)){
            return Response.error("无权限操作");
        }
       int temp = svcMngService.saveServLocationData(vo);
        if(temp == -1){

            return Response.error("服务URL不能为空");
        }else {
            return Response.ok();
        }

    }
    @RequestMapping(value = "/queryParamBaseData",method = RequestMethod.POST)
    public Response queryParamBaseData(@RequestBody SVCCommonReqBean param){
        ReqParamBaseDataDTO result =  svcMngService.queryParamBaseData(param);
        return Response.ok().setData(result);
    }
    @RequestMapping(value = "/saveParamData",method = RequestMethod.POST)
    public Response saveParamData(@RequestBody SaveParamDataVO param){
       svcMngService.saveParamData(param);
        return Response.ok();
    }
    @RequestMapping(value = "/queryServWsdlFunction",method = RequestMethod.POST)
    public Response queryServWsdlFunction(@RequestBody SVCCommonReqBean param){
        return Response.ok().setData(svcMngService.queryServWsdlFunction(param));
    }
    /**
     * 根据服务编号查找系统负责人;
     * @return
     */
    @RequestMapping(value = "/queryApprover",method = RequestMethod.GET)
    public Response queryApprover(@RequestParam(value = "servNo")String servNo,@RequestParam(value = "sourType")String sourType){
        return Response.ok().setData(svcMngService.queryApprover(servNo,sourType));
    }

    @RequestMapping(value = "/checkServNoIsExsit",method = RequestMethod.POST)
    public Response checkServNoIsExsit(@RequestBody SVCCommonReqBean param){
       Boolean isExist = svcMngService.checkServNoIsExsit(param);
       return Response.ok().setData(isExist);
    }
    @RequestMapping(value = "/addRestServ",method = RequestMethod.POST)
    public Response addRestServ(@RequestBody AddRestServVO addRestServVO){
        try {
            svcMngService.addRestServ(addRestServVO);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加服务失败");
        }

        return Response.ok();
    }
    @RequestMapping(value = "/addSoapServ",method = RequestMethod.POST)
    public Response addSoapServ(@RequestBody AddSoapServVO addSoapServVO){
        try {
            svcMngService.addSoapServ(addSoapServVO);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加服务失败");
        }

        return Response.ok();
    }
    @RequestMapping(value = "/addOtherServ",method = RequestMethod.POST)
    public Response addOtherServ(@RequestBody AddOtherServVO addOtherServVO){
        try {
            svcMngService.addOtherServ(addOtherServVO);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("添加服务失败");
        }

        return Response.ok();
    }
}
