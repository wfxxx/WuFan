package com.definesys.dsgc.service.interfacetest;

import com.definesys.dsgc.service.interfacetest.bean.CommonReqBean;
import com.definesys.dsgc.service.interfacetest.bean.DoTestVO;
import com.definesys.dsgc.service.interfacetest.bean.InterfaceBaseInfoVO;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/interfaceTest")
public class InterfaceTestController {
    @Autowired
    private InterfaceTestService interfaceTestService;

    @RequestMapping(value = "queryInterfaceInfo",method = RequestMethod.POST)
    public Response queryInterfaceInfo(@RequestBody CommonReqBean param){

        try {
            InterfaceBaseInfoVO result = interfaceTestService.queryInterfaceInfo(param);
            return Response.ok().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
    @RequestMapping(value = "doRestTest",method = RequestMethod.POST)
    public Response doRestTest(@RequestBody DoTestVO param){

        try {
          return Response.ok().setData(interfaceTestService.doRestTest(param));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
    @RequestMapping(value = "doSoapTest",method = RequestMethod.POST)
    public Response doSoapTest(@RequestBody DoTestVO param){

        try {
            return Response.ok().setData(interfaceTestService.doSoapTest(param));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
