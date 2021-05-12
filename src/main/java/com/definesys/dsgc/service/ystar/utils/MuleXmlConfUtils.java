package com.definesys.dsgc.service.ystar.utils;

import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SapConnInfoJsonBean;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.svcgen.bean.ServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.dsgc.service.ystar.svcgen.restcfg.RestServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.sapcfg.SapServiceConfigDTO;
import com.definesys.dsgc.service.ystar.svcgen.soapcfg.SoapServiceConfigDTO;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: MuleXmlConfUtils
 * @Description: Mule 配置文件操作类
 * @Author：afan
 * @Date : 2020/1/7 11:27
 */
public class MuleXmlConfUtils {

    /**
     * 追加DB接口代码
     */
    public static boolean appendDBProjectCode(String proPath, String svcType, SvcgenConnBean svcgenConnBean, TmplConfigBean tmplConfigBean) {
        System.out.println("-appendDBConf->\n" + tmplConfigBean.toString());
        String svcCode = tmplConfigBean.getServNo();
        String proName = tmplConfigBean.getProjDir();
        try {
            SAXReader reader = new SAXReader();
            File cfgFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/global-cfg.xml"));
            Document cfgDoc = reader.read(cfgFile);
            Element cfgRoot = cfgDoc.getRootElement();
            File svcFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/" + proName + ".xml"));
            Document svcDoc = reader.read(svcFile);
            Element svcRoot = svcDoc.getRootElement();
            //1-先删除原有代码
            removeCfgCode(cfgRoot, svcType, svcCode);
            removeFlowCode(svcRoot, svcCode);
            saveToXml(cfgFile, cfgDoc);
            saveToXml(svcFile, svcDoc);
            //2-添加代码
            String connType = svcgenConnBean.getConnType();
            String dbType = svcgenConnBean.getAttr1();
            if ("DB".equals(connType)) {
                if ("ORACLE".equals(dbType.toUpperCase())) {
                    //1-添加db-configure
                    cfgRoot.addElement("db:oracle-config")
                            .addAttribute("name", "Oracle_Configuration_" + proName)
                            .addAttribute("host", svcgenConnBean.getAttr2()) // 业务服务IP
                            .addAttribute("port", svcgenConnBean.getAttr3()) //业务服务端口
                            .addAttribute("instance", svcgenConnBean.getAttr4())//用户名
                            .addAttribute("instance", svcgenConnBean.getAttr5())//密码
                            .addAttribute("instance", svcgenConnBean.getAttr7())//实例
                            .addAttribute("doc:name", "HTTP Request Configuration " + proName);
                    //2-添加flow接口代码
                    String psUri = StringUtil.isNotBlank(tmplConfigBean.getRestPSUri()) ? tmplConfigBean.getRestPSUri() : tmplConfigBean.getSoapPSUri();
                    // 新增flow
                    Element newFlow = svcRoot.addElement("flow").addAttribute("name", svcCode); // 设置接口名称
                    Element httpListener = newFlow.addElement("http:listener");
                    httpListener.addAttribute("config-ref", "HTTP_Listener_Configuration")
                            .addAttribute("path", psUri) // 设置访问路径
                            .addAttribute("doc:name", "HTTP Listener " + svcCode);
                    // 日志监控INST和OB节点
                    newFlow = addINSTAndOB(newFlow, tmplConfigBean.getToSystem());
                    //
                    String dbOpr = "S01".equals(tmplConfigBean.getDbOper()) ? "select" : "select";
                    //查询
                    Element dbCfg = newFlow.addElement("db:" + dbOpr);
                    dbCfg.addAttribute("config-ref", "Oracle_Configuration_" + proName) // 设置请求配置
                            .addAttribute("doc:name", "Database");
                    dbCfg.addElement("db:parameterized-query")
                            .setData("<![CDATA[" + tmplConfigBean.getSqlcode() + "]]");
                } else if ("MYSQL".equals(dbType.toUpperCase())) {
//                    //1-添加db-configure
//                    cfgRoot.addElement("http:request-config")
//                            .addAttribute("name", "HTTP_Request_Configuration_" + svcCode)
//                            .addAttribute("host", restConfigDTO.getBizReqIp()) // 业务服务IP
//                            .addAttribute("port", restConfigDTO.getBizReqPort()) //业务服务端口
//                            .addAttribute("doc:name", "HTTP Request Configuration " + restConfigDTO.getSvcCode());
                }
            }
            //5-保存代码至XML文件
            saveToXml(cfgFile, cfgDoc);
            saveToXml(svcFile, svcDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 项目追加代码
     * ystar 2020-12-29
     *
     * @param proPath
     * @param proName
     * @param svcType
     * @param svcConfigDTO
     * @return
     */
    public static boolean appendProjectCode(String proPath, String proName, String svcType, ServiceConfigDTO svcConfigDTO) {
        System.out.println("-appendConf->\n" + svcConfigDTO.toString());
        try {
            SAXReader reader = new SAXReader();
            File cfgFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/global-cfg.xml"));
            Document cfgDoc = reader.read(cfgFile);
            Element cfgRoot = cfgDoc.getRootElement();
            File svcFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/" + proName + ".xml"));
            Document svcDoc = reader.read(svcFile);
            Element svcRoot = svcDoc.getRootElement();

            //1-先删除原有代码
            removeCfgCode(cfgRoot, svcType, svcConfigDTO.svcCode);
            removeFlowCode(svcRoot, svcConfigDTO.svcCode);
            saveToXml(cfgFile, cfgDoc);
            saveToXml(svcFile, svcDoc);
            //2-添加代码
            if ("REST".equals(svcType)) {
                RestServiceConfigDTO restSvcConfigDTO = (RestServiceConfigDTO) svcConfigDTO;
                //1-添加http-request-configure
                appendHttpReqGlbCfg(cfgRoot, svcConfigDTO);
                //2-添加flow接口代码
                appendRestCode(svcRoot, restSvcConfigDTO);
            } else if ("SOAP".equals(svcType)) {
                SoapServiceConfigDTO soapSvcConfigDTO = (SoapServiceConfigDTO) svcConfigDTO;
                //1-保存 wsdl文件
                appendSoapWsdl(soapSvcConfigDTO);
                //2-新增 cxf-configuration
                appendCxfGlbCfg(cfgRoot, svcConfigDTO);
                //3-新增 webservice consumer
                appendConsumerGlbCfg(cfgRoot, soapSvcConfigDTO);
                //4-添加soap接口代码
                appendSoapCode(svcRoot, soapSvcConfigDTO);
            }
            //5-保存代码至XML文件
            saveToXml(cfgFile, cfgDoc);
            saveToXml(svcFile, svcDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /*** 添加 http-request 配置 */
    public static boolean appendHttpReqGlbCfg(Element root, ServiceConfigDTO restConfigDTO) {
        root.addElement("http:request-config")
                .addAttribute("name", "HTTP_Request_Configuration_" + restConfigDTO.getSvcCode())
                .addAttribute("host", restConfigDTO.getBizReqIp()) // 业务服务IP
                .addAttribute("port", restConfigDTO.getBizReqPort()) //业务服务端口
                .addAttribute("doc:name", "HTTP Request Configuration " + restConfigDTO.getSvcCode());
        return true;
    }

    /*** 添加 Rest接口代码 */
    public static boolean appendRestCode(Element root, RestServiceConfigDTO restConfigDTO) {
        try {
            System.out.println("--appendRestCode->\n" + restConfigDTO.toString());
            String svcCode = restConfigDTO.getSvcCode();
            // 新增flow
            Element newFlow = root.addElement("flow").addAttribute("name", svcCode); // 设置接口名称
            Element httpListener = newFlow.addElement("http:listener");
            httpListener.addAttribute("config-ref", "HTTP_Listener_Configuration")
                    .addAttribute("path", restConfigDTO.getSvcPath()) // 设置访问路径
                    .addAttribute("doc:name", "HTTP Listener " + svcCode);
            // 日志监控INST和OB节点
            newFlow = addINSTAndOB(newFlow, restConfigDTO.getToSystem());
            // 调用服务节点
            Element httpRequest = newFlow.addElement("http:request");
            httpRequest.addAttribute("config-ref", "HTTP_Request_Configuration_" + svcCode) // 设置请求配置
                    .addAttribute("path", restConfigDTO.getBizReqPath()) // 业务系统请求路径
                    .addAttribute("method", restConfigDTO.getBizReqMethod())// 设置请求方法
                    .addAttribute("doc:name", "HTTP Request " + svcCode);
            // 装入header属性
            if (restConfigDTO.getHeaders().size() > 0) {
                Element headerBuild = httpRequest.addElement("http:request-builder");
                List<Map<String, String>> headers = restConfigDTO.getHeaders();
                for (Map<String, String> header : headers) {
                    for (String hName : header.keySet()) {
                        headerBuild.addElement("http:header")
                                .addAttribute("headerName", hName)
                                .addAttribute("value", header.get(hName));
                    }
                }
            }
            // 日志监控OB_RES节点
            newFlow = addOBRes(newFlow, restConfigDTO.getToSystem());
            // 设置响应属性
            newFlow.addElement("set-property")
                    .addAttribute("propertyName", "Content-Type")
                    .addAttribute("value", "application/json;charset=utf-8")
                    .addAttribute("doc:name", "Property");
            // 添加异常捕获引用
            newFlow.addElement("exception-strategy")
                    .addAttribute("ref", "Global_Rest_Exception_Strategy")
                    .addAttribute("doc:name", "Reference Exception Strategy");
            System.out.println("--------1-------");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /*** 添加 wsdl 配置 */
    public static void appendSoapWsdl(SoapServiceConfigDTO soapConfig) throws IOException {
        // 保存wsdl逻辑
        String wsdlUri = soapConfig.getWsdlUri();
        String subStr = wsdlUri.substring(wsdlUri.lastIndexOf("/"));
        String wsdlName = subStr.substring(subStr.lastIndexOf("/") + 1, subStr.contains(".") ? subStr.lastIndexOf(".") : subStr.lastIndexOf("?"));
        String wsdlSavePath = soapConfig.getProjectName() + "/src/main/wsdl/" + wsdlName + ".wsdl";
        //保存WSDL文件
        WSDLUtils wsdlUtils = new WSDLUtils();
        Description desc = wsdlUtils.getDesc(wsdlUri);
        MuleXmlConfUtils.saveWSDL(wsdlUri, wsdlSavePath);
        //保存wsdlName 和 port
        soapConfig.setWsdlName(wsdlName);
        soapConfig.setBizReqPort(desc.getServices().get(0).getEndpoints().get(0).getName());
        soapConfig.setNameSpace(desc.getTargetNamespace());
        soapConfig.setService(desc.getServices().get(0));
    }

    /*** 添加 cxf 配置 */
    public static boolean appendCxfGlbCfg(Element root, ServiceConfigDTO restConfigDTO) {
        root.addElement("cxf:configuration")
                .addAttribute("name", "CXF_Configuration_" + restConfigDTO.getProjectName())
                .addAttribute("enableMuleSoapHeaders", "false")
                .addAttribute("initializeStaticBusInstance", "true")
                .addAttribute("doc:name", "CXF Configuration " + restConfigDTO.getProjectName());
        return true;
    }

    /*** 添加 consumer-config 配置 */
    public static boolean appendConsumerGlbCfg(Element root, SoapServiceConfigDTO soapConfig) {
        root.addElement("ws:consumer-config")
                .addAttribute("name", "Web_Service_Consumer_" + soapConfig.getSvcCode())
                .addAttribute("wsdlLocation", soapConfig.getWsdlName() + ".wsdl")
                .addAttribute("service", soapConfig.getSltPort())
                .addAttribute("port", soapConfig.getBizReqPort())
                .addAttribute("serviceAddress", soapConfig.getBizReqPath())
                .addAttribute("doc:name", "Web Service Consumer " + soapConfig.getProjectName());
        return true;
    }

    /*** 添加 Soap接口代码 */
    public static Boolean appendSoapCode(Element svcRoot, SoapServiceConfigDTO soapConfig) {
        String wsdlName = soapConfig.getWsdlName();
        String svcCode = soapConfig.getSvcCode();
        // 1-新增flow
        Element flow = svcRoot.addElement("flow").addAttribute("name", svcCode);
        // 2-新增httpListener
        Element httpListener = flow.addElement("http:listener")
                .addAttribute("config-ref", "HTTP_Listener_Configuration_" + svcCode)
                .addAttribute("path", soapConfig.getSvcPath()) // 设置访问路径
                .addAttribute("responseStreamingMode", "NEVER")
                .addAttribute("doc:name", "HTTP Listener Configuration " + svcCode);
        // 3-新增cxf
        flow.addElement("cxf:proxy-service")
                .addAttribute("configuration-ref", "CXF_Configuration_" + svcCode)
                //解析WSDL
                .addAttribute("namespace", soapConfig.getNameSpace())
                .addAttribute("service", soapConfig.getSltPort())
                .addAttribute("payload", "body")
                .addAttribute("port", soapConfig.getBizReqPort())
                .addAttribute("wsdlLocation", wsdlName + ".wsdl")
                .addAttribute("doc:name", "CXF Configuration " + svcCode);
        // 4-权限认证节点
        //flow = addDoAuth(flow, "SOAP");
        // 5-日志监控 INST和OB 节点
        flow = addINSTAndOB(flow, soapConfig.getToSystem());
        // 6-添加choice
        Element choice = flow.addElement("choice").addAttribute("doc:name", "Choice");
        Service service = soapConfig.getService();
        List<Endpoint> endpoints = service.getEndpoints();
        List<String> soapOpers = soapConfig.getSoapOpers();
        List<String> collect = endpoints.get(0).getBinding().getBindingOperations().stream().map(e -> e.getOperation().getQName().toString())
                .collect(Collectors.toList());
        List<String> opers = collect.stream().filter(e -> soapOpers.stream().anyMatch(s -> e.contains(s))).collect(Collectors.toList());
        // 根据选择暴露的方法添加choice分支
        for (String oper : opers) {
            Element when = choice.addElement("when")
                    .addAttribute("expression", "#[flowVars.cxf_operation.toString()=='" + oper + "']");
            Element consumer = when.addElement("ws:consumer");
            String operation = oper.substring(oper.lastIndexOf("}") + 1);
            consumer.addAttribute(" config-ref", wsdlName + "_consumer")
                    .addAttribute("operation", operation)
                    .addAttribute("doc:name", oper.substring(oper.lastIndexOf("}") + 1));
        }
        Element otherwise = choice.addElement("otherwise");
        otherwise.addElement("set-payload")
                .addAttribute("value", "#['<error>Unexposed method</error>']")
                .addAttribute("doc:name", "Set Payload");
        // 日志监控OB_RES节点
        flow = addOBRes(flow, soapConfig.getToSystem());
        // 全局异常捕获
        flow.addElement("exception-strategy")
                .addAttribute("ref", "Global_Soap_Exception_Strategy")
                .addAttribute("doc:name", "Reference Exception Strategy");
        return true;
    }

    public static Boolean saveWSDL(String wsdluri, String targetPath) throws IOException {
        //String url = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";
        String url = wsdluri;
        URL u = new URL(url);
        URLConnection uc = u.openConnection();
        InputStream raw = uc.getInputStream();
        InputStream buffer = new BufferedInputStream(raw);
        Reader r = new InputStreamReader(buffer);

        int c;
        String str = "";
        while ((c = r.read()) != -1) {
            str += (char) c;
            //System.out.print((char)c);
        }
        File writeName = new File(targetPath); // 相对路径，如果没有则要建立一个新的output.txt文件
        if (!writeName.exists()) {
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
        }
        FileWriter writer = new FileWriter(writeName);
        BufferedWriter out = new BufferedWriter(writer);
        out.write(str);
        out.flush();
        out.close();
        return true;
    }

    /**
     * 移除项目代码
     *
     * @return
     */
    public static boolean removeProjectCode(String proPath, String proName, String svcType, String svcCode) {
        try {
            SAXReader reader = new SAXReader();
            File cfgFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/global-cfg.xml"));
            Document cfgDoc = reader.read(cfgFile);
            Element cfgRoot = cfgDoc.getRootElement();
            File svcFile = new File(FileUtils.replaceFileSeparator(proPath + "/" + proName + "/src/main/app/" + proName + ".xml"));
            Document svcDoc = reader.read(svcFile);
            Element svcRoot = svcDoc.getRootElement();
            removeCfgCode(cfgRoot, svcType, svcCode);
            saveToXml(cfgFile, cfgDoc);
            removeFlowCode(svcRoot, svcCode);
            saveToXml(svcFile, svcDoc);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void removeCfgCode(Element cfgRoot, String svcType, String svcCode) {
        //1-删除global-cfg配置
        Map<String, String> cfgMap = new HashMap<>();
        if ("REST".equals(svcType)) {
            cfgMap.put("request-config", "HTTP_Request_Configuration_" + svcCode);
        } else if ("SOAP".equals(svcType)) {
            cfgMap.put("configuration", "CXF_Configuration_" + svcCode);
            cfgMap.put("consumer-config", "Web_Service_Consumer_" + svcCode);
        }
        removeNode(cfgRoot, cfgMap);
    }

    public static void removeFlowCode(Element svcRoot, String svcCode) {
        //2-删除flow代码
        Map<String, String> flowMap = new HashMap<>();
        flowMap.put("flow", svcCode);
        removeNode(svcRoot, flowMap);
    }

    /*** 删除节点**/
    public static boolean removeNode(Element root, Map<String, String> removeMap) {
        for (String nodeType : removeMap.keySet()) {
            String nodeName = removeMap.get(nodeType);
            List<Element> elementList = getMuleNodeByName(root, nodeType, nodeName);
            if (elementList != null) {
                for (Element e : elementList) {
                    root.remove(e);
                }
            }
        }
        return true;
    }

    // 处理properties文件
    public static Map<String, String> sapConfig(String propsPath, SapConnInfoJsonBean sapConnInfoJsonBean) {
        String connName = sapConnInfoJsonBean.getConnName().toUpperCase();
        PropsUtils propsUtils = new PropsUtils(propsPath);
        String hostKey = connName + ".jcoAsHost";
        String hostValue = propsUtils.getValue(hostKey);
        if (hostValue != null) {
            return null;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(hostKey, sapConnInfoJsonBean.getSapIp());
        hashMap.put(connName + ".jcoUser", sapConnInfoJsonBean.getConnUN());
        hashMap.put(connName + ".jcoPasswd", sapConnInfoJsonBean.getConnPD());
        hashMap.put(connName + ".jcoClient", sapConnInfoJsonBean.getSapClient());
        hashMap.put(connName + ".jcoSysnr", sapConnInfoJsonBean.getSn());
        hashMap.put(connName + ".jcoLang", sapConnInfoJsonBean.getLang());
        propsUtils.setValue(hashMap);
        // 设置
        return hashMap;
    }

    public static boolean appendSapConf(String workSapce, Map<String, String> sapConfigMap, SapServiceConfigDTO sapServiceConfigDTO, SapConnInfoJsonBean sapConnInfoByName) throws DocumentException, IOException {
        String globalPath = workSapce + "/src/main/app/global.xml";

        SAXReader saxReader = new SAXReader();
        File file = new File(globalPath);
        Document read = saxReader.read(file);
        Element root = read.getRootElement();
        Attribute attribute = root.attribute(0);
        Namespace xmlns = root.getNamespaceForPrefix("sap");
        // 判断是否有sap的命名空间没有的话自动加上
        if (xmlns == null) {
            addNameSpace(root, "SAP", file, read);
        }
        // 判断在配置文件是否sap连接
        String connName = sapConnInfoByName.getConnName().toLowerCase();
        Node sapConn = read.selectSingleNode("/mule/sap:connector[@name='" + connName + "']");
        if (sapConn == null) {
            // 新建sap配置
            root.addElement("sap:connector")
                    .addAttribute("name", connName)
                    .addAttribute("jcoHost", "${" + connName + ".jcoHost}")
                    .addAttribute("jcoUser", "${" + connName + ".jcoUser}")
                    .addAttribute("jcoPasswd", "${" + connName + ".jcoPasswd}")
                    .addAttribute("jcoSysnr", "${" + connName + ".jcoSysnr}")
                    .addAttribute("jcoClient", "${" + connName + ".jcoClient}")
                    .addAttribute("jcoLang", "${" + connName + ".jcoLang}");
        }
        // 新加接口配置
        String path = workSapce + "/src/main/app/" + sapServiceConfigDTO.getResConfig() + ".xml";
        SAXReader resReader = new SAXReader();
        File resConfig = new File(path);
        Document resDoc = saxReader.read(file);
        Element mule = read.getRootElement();
        // 新增flow
        Element newflow = mule.addElement("flow").addAttribute("name", sapServiceConfigDTO.getServName()); // 设置接口名称
        Element httpListener = newflow.addElement("http:listener");
        httpListener.addAttribute("config-ref", "HTTP_Listener_Configuration")
                .addAttribute("path", sapServiceConfigDTO.getAppUri()) // 设置访问路径
                .addAttribute("doc:name", "HTTP ");
        newflow = addDoAuth(newflow, "REST");
        newflow = addINSTAndOB(newflow, sapServiceConfigDTO.getToSystem());
        // 设置SAP请求
        Namespace sap = mule.getNamespaceForPrefix("sap");
        // 判断是否有sap的命名空间没有的话自动加上
        if (sap == null) {
            addNameSpace(mule, "SAP", resConfig, resDoc);
        }
        newflow.addElement("sap:outbound-endpoint")
                .addAttribute("exchange-pattern", "request-response")
                .addAttribute("connector-ref", connName)
                .addAttribute("type", "function")
                .addAttribute("functionName", sapServiceConfigDTO.getRfcName())
                .addAttribute("outputXml", "true")
                .addAttribute("responseTimeout", "10000")
                .addAttribute("doc:name", "SAP");

        addOBRes(newflow, sapServiceConfigDTO.getToSystem());// 全局异常捕获
        newflow.addElement("exception-strategy")
                .addAttribute("ref", "Global_Exception_Strategy")
                .addAttribute("doc:name", "Reference Exception Strategy");
        saveToXml(resConfig, resDoc);
        return true;
    }
    // 增加http节点

    // 增加权限校验节点
    public static Element addDoAuth(Element element, String type) {
        if ("REST".equals(type)) {
            Element doAuth = element.addElement("invoke");
            doAuth.addAttribute("object-ref", "doAuth")
                    .addAttribute("method", "doSecurityCheck")
                    .addAttribute("methodArguments", "#[message],#['1']")
                    .addAttribute("doc:name", "Invoke");
        } else {
            Element doAuth = element.addElement("invoke");
            doAuth.addAttribute("object-ref", "doAuth")
                    .addAttribute("method", "doSecurityCheck")
                    .addAttribute("methodArguments", "#[message],#[flowVars.cxf_operation]")
                    .addAttribute("doc:name", "Invoke");
        }


        return element;
    }

    // 增加INST——OB节点
    public static Element addINSTAndOB(Element element, String toSystem) {
        Element logInst = element.addElement("custom-transformer")
                .addAttribute("class", "com.definesys.dsgc.mule.processor.MuleMsgProcessor")
                .addAttribute("doc:name", "INST");
        logInst.addElement("spring:property")
                .addAttribute("name", "stage")
                .addAttribute("value", "INST");
        // 日志监控OB节点
        Element logOb = element.addElement("custom-transformer")
                .addAttribute("class", "com.definesys.dsgc.mule.processor.MuleMsgProcessor")
                .addAttribute("doc:name", "OB");
        logOb.addElement("spring:property") //
                .addAttribute("name", "stage")
                .addAttribute("value", "OB");
        logOb.addElement("spring:property")
                .addAttribute("name", "toSystem")
                .addAttribute("value", toSystem); // 设置发送系统
        return element;
    }

    // 增加OBRES节点
    public static Element addOBRes(Element element, String toSystem) {
        Element logObRes = element.addElement("custom-transformer")
                .addAttribute("class", "com.definesys.dsgc.mule.processor.MuleMsgProcessor")
                .addAttribute("doc:name", "OB_RES");
        logObRes.addElement("spring:property") //
                .addAttribute("name", "stage")
                .addAttribute("value", "OB_RES");
        logObRes.addElement("spring:property")
                .addAttribute("name", "toSystem")
                .addAttribute("value", toSystem);
        // 日志监控INST_RES节点
        Element logInstRes = element.addElement("custom-transformer")
                .addAttribute("class", "com.definesys.dsgc.mule.processor.MuleMsgProcessor")
                .addAttribute("doc:name", "INST_RES");
        logInstRes.addElement("spring:property")
                .addAttribute("name", "stage")
                .addAttribute("value", "INST_RES");

        return element;
    }

    // 将节点配置写入xml文件
    public static void saveToXml(File file, Document document) throws IOException {
        //        创建一种输出格式    每个节点元素可自动换行
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(file), outputFormat);//写入XML文件的位置 以及指定的格式
        xmlWriter.write(document);//开始写入XML文件   写入Document对象
        xmlWriter.close();
        System.out.println("写入成功！");
    }

    // 添加命名空间
    public static void addNameSpace(Element root, String type, File file, Document read) throws IOException {
        if ("SAP".equals(type)) {
            root.addNamespace("sap", "http://www.mulesoft.org/schema/mule/sap");
            String str = "http://www.mulesoft.org/schema/mule/sap http://www.mulesoft.org/schema/mule/sap/current/mule-sap.xsd";
            String s = root.attributeValue("schemaLocation");
            s = s + " " + str;
            root.addAttribute("schemaLocation", s);
            XMLWriter xmlWriter = new XMLWriter(new FileWriter(file));
            xmlWriter.write(read);//写入命名空间
            xmlWriter.close();
        }
    }

    // 查询节点List
    private static List<Element> getMuleNodeByName(Element root, String nodeType, String nodeName) {
        List<Element> elementList = root.elements();
        List<Element> resList = new ArrayList<>();
        System.out.println("nodeType->" + nodeType + " nodeName->" + nodeName);
        for (Element element : elementList) {
            String type = element.getName();
            String name = element.attributeValue("name");
            System.out.println("type->" + type + " name->" + name);
            if (nodeType.equals(type) && nodeName.equals(name)) {
                System.out.println("删除");
                resList.add(element);
            }
        }
        return resList;
    }
}
