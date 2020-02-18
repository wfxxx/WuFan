package com.definesys.dsgc.service.svcmng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SVCMngServiceTest {

    @Autowired
    SVCMngService svcMngService;



    @Test
    public void getWsdlBodyReq() {
        String wsdlUrl="http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl";
        String method="getCountryCityByIp";
        String result =svcMngService.getWsdlBodyReq(wsdlUrl,method);
        System.out.println(result);
    }

    @Test
    public void getWsdlBodyRes() {
        String wsdlUrl="http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl";
        String method="getCountryCityByIp";
        String result =svcMngService.getWsdlBodyRes(wsdlUrl,method);
        System.out.println(result);
    }

    @Test
    public void getWsdlUrl() {
        System.err.println(this.svcMngService.getWsdlUrl("OA_001","1"));
    }
}