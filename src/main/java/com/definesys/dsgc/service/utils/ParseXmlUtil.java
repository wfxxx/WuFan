package com.definesys.dsgc.service.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description: xpath 按命名空间解析xml 工具类
 * @author: biao.luo
 * @since: 2019/5/17 下午10:08
 * @history: 1.2019/5/17 created by biao.luo
 */
public class ParseXmlUtil {
    private static final String XML_NODE_NAME = "con:wsdl";
    private static final String XML_NODE_ATTR = "ref";

    //定义成员变量list，用于存放解析出来的文件
    public List<String> resList = new ArrayList<String>();


    public void parseXmlToList(String rootPath ,String initFilePath)  {
        System.out.println("------------------开始解析xml-----------------");

        File file = new File(initFilePath);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String fileParentPath = initFilePath.substring(0,initFilePath.lastIndexOf("/") + 1 );

        System.out.println("文件后缀  "+suffix);
        System.out.println("文件父路径 ："+fileParentPath);

        //创建xml解析对象
        SAXReader saxReader = new SAXReader();

        //创建文档对象
        Document document = null;

        try {
            //读取XML文件
            document = saxReader.read(rootPath+File.separator+initFilePath);


            getElementList1(document, suffix,fileParentPath,rootPath);
            System.out.println("------------------xml解析完毕-----------------");
        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }


    public void getElementList1(Document document, String suffix,String fileParentPath,String rootPath) {
        //获取xml跟元素
        Element root = document.getRootElement();
        //把解析路径切换到/beans下
        //Node node=document.selectSingleNode("ser");

        if (".bix".equals(suffix)) {
            Map<String, String> bixMap = new HashMap<String, String>();

            bixMap.put("ser", "http://xmlns.oracle.com/servicebus/business/config");
            bixMap.put("tran", "http://www.bea.com/wli/sb/transports");
            bixMap.put("jca", "http://www.bea.com/wli/sb/transports/jca");
            bixMap.put("con", "http://www.bea.com/wli/sb/services/bindings/config");

            Map<String, String> queryMap = new HashMap<>();

            queryMap.put("con:wsdl", "ref");
            queryMap.put("jca:jca-file", "ref");

            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                String xpathExpression = "//" + entry.getKey() + "/@" + entry.getValue();
                System.out.println(xpathExpression);
                XPath xPath = root.createXPath(xpathExpression);
                xPath.setNamespaceURIs(bixMap);
                String value = xPath.valueOf(root);
                System.out.println("FILE ====  " + value);
                if (null != value && !"".equals(value)) {
                    // 如果引用的文件没有父路径，则使用当前文件路径
                    if( ! value.contains("/")){
                        value = fileParentPath + value;
                    }


                    if (entry.getKey().contains("jca")) {
                        if (!resList.contains(value + ".jca")) {
                            resList.add(value + ".jca");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".jca")){
                                this.parseXmlToList(rootPath,value+ ".jca");
                            }
                        }

                    }
                    if (entry.getKey().contains("wsdl")) {
                        if (!resList.contains(value + ".wsdl")) {
                            resList.add(value + ".wsdl");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".wsdl")){
                                this.parseXmlToList(rootPath,value+ ".wsdl");
                            }
                        }
                    }

                }

            }


        } else if (".pipeline".equals(suffix)) {
            Map<String, String> pipelineMap = new HashMap<String, String>();

            pipelineMap.put("con", "http://www.bea.com/wli/sb/pipeline/config");
            pipelineMap.put("con1", "http://www.bea.com/wli/sb/stages/logging/config");
            pipelineMap.put("con1", "http://www.bea.com/wli/sb/stages/routing/config");
            pipelineMap.put("con2", "http://www.bea.com/wli/sb/stages/config");


            Map<String, String> piQueryMap = new HashMap<>();

            piQueryMap.put("con:wsdl", "ref");
            piQueryMap.put("con1:service", "ref");
            piQueryMap.put("con2:resource", "ref");
            piQueryMap.put("con:template", "ref");

            for (Map.Entry<String, String> entry : piQueryMap.entrySet()) {

                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                String xpathExpression = "//" + entry.getKey() + "/@" + entry.getValue();
                System.out.println(xpathExpression);
                XPath xPath = root.createXPath(xpathExpression);
                xPath.setNamespaceURIs(pipelineMap);
                String value = xPath.valueOf(root);
                System.out.println("FILE ====  " + value);
                if (null != value && !"".equals(value)) {
                    // 如果引用的文件没有父路径，则使用当前文件路径
                    if( ! value.contains("/")){
                        value = fileParentPath + value;
                    }

                    if (entry.getKey().contains("con1:service")) {
                        if (!resList.contains(value + ".bix")) {
                            resList.add(value + ".bix");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".bix")){
                                this.parseXmlToList(rootPath,value+ ".bix");
                            }
                        }

                    }

                    if (entry.getKey().contains("wsdl")) {
                        if (!resList.contains(value + ".wsdl")) {
                            resList.add(value + ".wsdl");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value + ".wsdl")){
                                this.parseXmlToList(rootPath,value + ".wsdl");
                            }
                        }
                    }

                    if (entry.getKey().contains("con2:resource")) {
                        if (!resList.contains(value + ".xsl")) {
                            resList.add(value + ".xsl");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".xsl")){
                                this.parseXmlToList(rootPath,value+ ".xsl");
                            }
                        }

                    }
                    if (entry.getKey().contains("con:template")) {
                        if (!resList.contains(value + ".ptx")) {
                            resList.add(value + ".ptx");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".ptx")){
                                this.parseXmlToList(rootPath,value+ ".ptx");
                            }
                        }
                    }

                }

            }



        } else if (".proxy".equals(suffix)) {

            Map<String, String> proxyMap = new HashMap<>();


            proxyMap.put("ser", "http://www.bea.com/wli/sb/services");
            proxyMap.put("con", "http://www.bea.com/wli/sb/services/bindings/config");

            Map<String, String> proxyQueryMap = new HashMap<String, String>();
            proxyQueryMap.put("con:wsdl", "ref");
            proxyQueryMap.put("ser:invoke", "ref");


            for (Map.Entry<String, String> entry : proxyQueryMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                String xpathExpression = "//" + entry.getKey() + "/@" + entry.getValue();
                System.out.println(xpathExpression);
                XPath xPath = root.createXPath(xpathExpression);
                xPath.setNamespaceURIs(proxyMap);
                String value = xPath.valueOf(root);
                System.out.println("FILE ====  " + value);

                if (null != value && !"".equals(value)) {
                    // 如果引用的文件没有父路径，则使用当前文件路径
                    if( ! value.contains("/")){
                        value = fileParentPath + value;
                    }

                    if (entry.getKey().contains("ser:invoke")) {
                        if (!resList.contains(value + ".pipeline")) {
                            resList.add(value + ".pipeline");
                            //调用递归，继续检索文件

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value + ".pipeline")){
                                this.parseXmlToList(rootPath,value + ".pipeline");
                            }
                        }

                    }
                    if (entry.getKey().contains("wsdl")) {
                        if (!resList.contains(value + ".wsdl")) {
                            resList.add(value + ".wsdl");

                            //此处调用递归
                            if(checkFileIsReferByFileSuffix(value+ ".wsdl")){
                                this.parseXmlToList(rootPath,value+ ".wsdl");
                            }
                        }
                    }

                }

            }



        } else if (".wsdl".equals(suffix)) {
            Map<String, String> wsdlMap = new HashMap<String, String>();

            wsdlMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
            wsdlMap.put("wsdl", "http://schemas.xmlsoap.org/wsdl/");
            wsdlMap.put("plt", "http://schemas.xmlsoap.org/ws/2003/05/partner-link/");
            wsdlMap.put("wsdl", "http://schemas.xmlsoap.org/wsdl/");

            Map<String, String> wsdlQueryMap = new HashMap<String, String>();
            wsdlQueryMap.put("xsd:import", "schemaLocation");
            wsdlQueryMap.put("wsdl:import", "location");
            wsdlQueryMap.put("import", "schemaLocation");


            for (Map.Entry<String, String> entry : wsdlQueryMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                String xpathExpression = "//" + entry.getKey() + "/@" + entry.getValue();
                System.out.println(xpathExpression);
                XPath xPath = root.createXPath(xpathExpression);
                xPath.setNamespaceURIs(wsdlMap);
                String value = xPath.valueOf(root);
                System.out.println("FILE ====  " + value);

                if (null != value && !"".equals(value)) {
                    // 如果引用的文件没有父路径，则使用当前文件路径
                    if( ! value.contains("/")){
                        value = fileParentPath + value;
                    }

                    //此处文件统一设置成   /Users/mac/Desktop/象屿项目文件/DistributeDemoProj/Resources
                    if (!resList.contains(value )) {
                        resList.add( value);

                        if(checkFileIsReferByFileSuffix( value)){
                            this.parseXmlToList(rootPath,value);
                        }
                    }
                    //调用递归，继续检索文件

                }

            }



        } else if (".xsl".equals(suffix)) {
            Map<String, String> xslMap = new HashMap<String, String>();

            xslMap.put("oracle-xsl-mapper", "http://www.oracle.com/xsl/mapper/schemas");


            Map<String, String> xslQueryMap = new HashMap<String, String>();
            xslQueryMap.put("oracle-xsl-mapper:schema", "location");


            for (Map.Entry<String, String> entry : xslQueryMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
                String xpathExpression = "//" + entry.getKey() + "/@" + entry.getValue();
                System.out.println(xpathExpression);
                XPath xPath = root.createXPath(xpathExpression);
                xPath.setNamespaceURIs(xslMap);
                String value = xPath.valueOf(root);
                System.out.println("FILE ====  " + value);


                if (null != value && !"".equals(value)) {
                    // 如果引用的文件没有父路径，则使用当前文件路径
                    if( ! value.contains("/")){
                        value = fileParentPath + value;
                    }

                    //此处文件统一设置成   DistributeDemoProj/Resourcess
                    if (!resList.contains(value )) {
                        resList.add( value);

                        //调用递归，继续检索文件
                        if(checkFileIsReferByFileSuffix(value)){
                            this.parseXmlToList(rootPath, value);
                        }
                    }


                }

            }


        } else if (".jca".equals(suffix)) {
            Map<String, String> jcaMap = new HashMap<String, String>();

            Element es = root.element("endpoint-interaction");

            Element sses = es.element("interaction-spec");
            List<Element> node = sses.elements();

            for (Element node1 : node) {
                if ("MappingsMetaDataURL".equals(node1.attributeValue("name"))) {
                    String value = node1.attributeValue("value");
                    System.out.println(value);
                    if (null != value && !"".equals(value)) {
                        // 如果引用的文件没有父路径，则使用当前文件路径
                        if( ! value.contains("/")){
                            value = fileParentPath + value;
                        }

                        if (!resList.contains( value )) {
                            resList.add(value);

                            if(checkFileIsReferByFileSuffix( value)){
                                this.parseXmlToList(rootPath,value);
                            }
                        }
                    }
                }

            }


        }


    }


    public boolean checkFileIsReferByFileSuffix(String FilePath){
        File file = new File(FilePath);
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        if(".xml".equals(suffix) ||  ".ptx".equals(suffix)  || ".sboverview".equals(suffix)  || ".xsd".equals(suffix) ){
            return false;
        }
        return true;
    }


    /**
     * 开始解析,递归遍历传入节点下所有节点元素
     */
    public void getElementList(Element root) {

        //获取指定节点名称下的所有节点
        Element elements = root.element("con:wsdl");
        //把解析路径切换到/beans下
        Node node = root.selectSingleNode("/beans");
        //System.out.println(elements.attributeValue("ref"));
        //List elements = root.elements();
        //System.out.println(elements.attributeValue(ParseFileXml.XML_NODE_ATTR));
        //如果节点集合非空
//        if( !elements.isEmpty()){
//            //获取迭代器
//            Iterator<?> iterator = elements.iterator();
//
//            //如果存在下一个元素，开始循环
//            while(iterator.hasNext()){
//                //获取第一个元素
//                Element  element = (Element) iterator.next();
//
//                System.out.println(element.getNamespaceForPrefix("con:wsdl"));
//
//                //获取指定节点属性名称的属性
//                String attrValue = element.attributeValue(ParseFileXml.XML_NODE_ATTR);
//
//                //如果引用的文件不为空或者null，将其添加进list
//                if(null  != attrValue && !"".equals(attrValue.trim())){
//                    list.add( attrValue+".wsdl" );
//                    //parseXmlToList(attrValue+".wsdl");
//                }
//
//                //getElementList(root);
//
//            }


        //       }
    }


}
