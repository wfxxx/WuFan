package com.definesys.dsgc.service.apimng;

import com.definesys.dsgc.service.apimng.bean.ApiBasicInfoDTO;
import com.definesys.dsgc.service.apimng.bean.CommonReqBean;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/dag/")
public class ApiMngController {
    @Autowired
    private ApiMngService apiMngService;


    @RequestMapping(value = "/saveApiBasicInfo",method = RequestMethod.POST)
    public Response saveApiBasicInfo(@RequestBody ApiBasicInfoDTO param){
        apiMngService.saveApiBasicInfo(param);
    return Response.ok();
    }
    @RequestMapping(value = "/queryBasicInfoByApiCode",method = RequestMethod.POST)
    public Response queryBasicInfoByApiCode(@RequestBody CommonReqBean param){
        ApiBasicInfoDTO result =  apiMngService.queryBasicInfoByApiCode(param.getCon0());
        return Response.ok().setData(result);
    }
}
