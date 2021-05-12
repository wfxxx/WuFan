package com.definesys.dsgc.service.ystar.utils;

import org.ow2.easywsdl.wsdl.WSDLFactory;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.WSDLReader;

import java.net.URL;
import java.util.List;

/**
 * wsdl解析
 *
 * @author afan
 * @version 1.0
 * @date 2020/8/31 14:55
 */
public class WSDLUtils {

    /**
     * 获取wsdl文档描述
     * @param wsdlUrl : wsdl完整地址
     * @return
     */
    public  Description getDesc(String wsdlUrl) {
        try {
            WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            Description desc = reader.read(new URL(wsdlUrl));
            return desc;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public  List<Service> getServices(String wsdlUrl){
        Description desc = this.getDesc(wsdlUrl);
        return desc.getServices();

    }
}
