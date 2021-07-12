package com.definesys.dsgc.service.ystar.svcgen.util;

import com.definesys.dsgc.common.bean.RtnMsgBean;
import com.definesys.dsgc.svcgen.bean.PortBean;
import com.definesys.dsgc.svcgen.bean.WsdlParseBean;
import com.definesys.dsgc.svcgen.utils.WsdlParserUtils;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import oracle.wsdl.WSDLDocument;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.wsdl.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.*;

public class WsdlUtil {
    public static void resolveWsdl(String wsdlUrl) {
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", true);
            reader.setFeature("javax.wsdl.importDocuments", true);
            Definition def = reader.readWSDL(wsdlUrl);
            WSDLWriter writer = factory.newWSDLWriter();
            writer.writeWSDL(def, System.out);
        } catch (WSDLException e) {
            e.printStackTrace();
        }
    }

    public static WsdlParseBean resolveWsdlByUrl(String wsdlUrl, String userName, String passWord) {
        WsdlParseBean wsdlParseBean = new WsdlParseBean();
        try {
            String res = WsdlParserUtils.urlToString(wsdlUrl, userName, passWord);
            System.out.println("res--->\n"+res);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringReader sr = new StringReader(res);
            InputSource is = new InputSource(sr);
            Document doc = documentBuilder.parse(is);
//            wsdlParseBean = WsdlUtil.resolveWsdlByDoc(doc);
            WSDLDocument wsdlDocument = new WSDLDocument(res);
            wsdlDocument.getSOAPNamespaceURI();
            return wsdlParseBean;
        } catch (Exception e) {
            wsdlParseBean.getRmb().setRtnCode(RtnMsgBean.RTN_CODE_E);
            wsdlParseBean.getRmb().setRtnMsg("解析WSDL失败：目标wsdl找不到对应的port节点内容!");
            e.printStackTrace();
            return wsdlParseBean;
        }
    }
    /*** 解析javax.wsdl **/
//    public static WsdlParseBean resolveWsdlByDoc(Document doc) throws WSDLException {
//        WsdlParseBean wsdlParseBean = new WsdlParseBean();
//        WSDLFactory factory = WSDLFactory.newInstance();
//        WSDLReader reader = factory.newWSDLReader();
//        reader.setFeature("javax.wsdl.verbose", true);
//        reader.setFeature("javax.wsdl.importDocuments", true);
//        Definition def = reader.readWSDL(null, doc);
//        Map<QName, Service> serviceMap = def.getServices();
//        for (QName qName : serviceMap.keySet()) {
//            String nameSpaceUri = qName.getNamespaceURI();
//            String serviceName = qName.getLocalPart();
//            System.out.println("nameSpaceUri-->>>>>" + nameSpaceUri + "\nserviceName-->>>>" + serviceName);
//            Service service = serviceMap.get(qName);
//            Map<String, Port> portMap = service.getPorts();
//            List<PortBean> portBeanList = new ArrayList<>();
//            for (String str : portMap.keySet()) {
//                PortBean portBean = new PortBean();
//                Port port = portMap.get(str);
//                System.out.println("portName-->>>>" + port.getName());
//                portBean.setPortName(port.getName());
//                Binding binding = port.getBinding();
////                    System.out.println(binding);
//                QName qn = binding.getQName();
//                String nameSpace = qName.getNamespaceURI();
//                System.out.println("binding-->>" + qn.getLocalPart());
//                portBean.setBindingName(qn.getLocalPart());
//                List<BindingOperation> operations = binding.getBindingOperations();
//                System.out.println("binding name->" + qn);
//                List<String> operationList = new ArrayList<>();
//                for (BindingOperation operation : operations) {
//                    System.out.println("operation name-->>>" + operation.getName());
//                    operationList.add(operation.getName());
//                }
//                portBean.setOperationList(operationList);
//                System.out.println(portBean.toString());
//
//                Iterator eeIter = port.getExtensibilityElements().iterator();
//                while (eeIter.hasNext()) {
//                    SOAPAddressImpl ee = (SOAPAddressImpl) eeIter.next();
//                    String uri = ee.getLocationURI();
//                    System.out.println(uri);
//                    portBean.setPortAddress(uri);
//                }
//                portBeanList.add(portBean);
//            }
//            wsdlParseBean.setPorts(portBeanList);
//        }
//        if (wsdlParseBean.getPorts().size() != 0) {
//            wsdlParseBean.getRmb().setRtnCode(RtnMsgBean.RTN_CODE_S);
//            wsdlParseBean.getRmb().setRtnMsg("parse wsdl success!");
//        } else {
//            wsdlParseBean.getRmb().setRtnCode(RtnMsgBean.RTN_CODE_E);
//            wsdlParseBean.getRmb().setRtnMsg("解析WSDL失败：目标wsdl找不到对应的port节点内容!");
//        }
//        return wsdlParseBean;
//    }
}
