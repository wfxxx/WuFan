package com.definesys.dsgc.service.ystar.mg.util;

import com.definesys.dsgc.service.ystar.mg.prj.bean.MulePrjInfoBean;
import com.definesys.dsgc.service.ystar.mg.svg.bean.MuleSvgCodeBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MuleSvgUtil {


    public static void appendOrRemoveMuleCode(boolean removeFlag, String repoHome, MulePrjInfoBean prjInfoBean, MuleSvgCodeBean svgCodeBean) {
        String prjName = prjInfoBean.getPrjName();
        String appPath = repoHome + File.separator + prjName + File.separator + "src" + File.separator + "main" + File.separator + "app" + File.separator;
        String svgType = svgCodeBean.getSvgType();
        String svgCode = svgCodeBean.getSvgCode();
        String openInvoke = JsonUtil.getValueByKey(svgCodeBean.getComp1(), "auth");
        String toSystem = svgCodeBean.getToSystem();
        //获取配置
        List<Map<String, String>> mapList = getPropertiesMap(svgCodeBean);
        appendOrRemovePropertiesCode(removeFlag, appPath, mapList);
        //获取替换模板内容
        String svgTemplateContent = FileUtil.getMuleTemplateFileContent("xml", "svg-template");
        svgTemplateContent = svgTemplateContent.replaceAll("#svgCode#", svgCode).replaceAll("#toSystem#", toSystem);
        //取消安全认证
        if ("false".equals(openInvoke.toLowerCase())) {
            svgTemplateContent = svgTemplateContent.replace("<flow-ref name=\"Sub-Flow-Invoke\" doc:name=\"Invoke\"/>", "");
        }
        //System.out.println("svgTemplateContent->" + svgTemplateContent);
        // 接口flow代码
        appendSvgCode(removeFlag, appPath, svgType, svgTemplateContent);
    }


    // 根据svg代码 获取Map
    public static List<Map<String, String>> getPropertiesMap(MuleSvgCodeBean svgCodeBean) {
        String svgCode = svgCodeBean.getSvgCode();
        String svgType = svgCodeBean.getSvgType();
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> hostPropertiesMap = new HashMap<>();
        Map<String, String> appPropertiesMap = new HashMap<>();
        if ("rest".equals(svgType.toLowerCase())) {
            String proxyPath = JsonUtil.getValueByKey(svgCodeBean.getComp0(), "path");
            String comp4 = svgCodeBean.getComp4();
            String busMethod = JsonUtil.getValueByKey(comp4, "method");
            String busProtocol = JsonUtil.getValueByKey(comp4, "protocol");
            String busHost = JsonUtil.getValueByKey(comp4, "host");
            String busPort = JsonUtil.getValueByKey(comp4, "port");
            String busPath = JsonUtil.getValueByKey(comp4, "path");
            appPropertiesMap.put("http.listener.cfg." + svgCode + ".path", proxyPath);
            appPropertiesMap.put("http.request.cfg." + svgCode + ".protocol", busProtocol);
            hostPropertiesMap.put("http.request.cfg." + svgCode + ".host", busHost);
            appPropertiesMap.put("http.request.cfg." + svgCode + ".path", busPath);
            hostPropertiesMap.put("http.request.cfg." + svgCode + ".port", busPort);
            appPropertiesMap.put("http.request.cfg." + svgCode + ".method", busMethod);
        }
        mapList.add(hostPropertiesMap);
        mapList.add(appPropertiesMap);
        return mapList;
    }

    /*** 添加或移除配置文件 **/
    private static boolean appendOrRemovePropertiesCode(boolean removeFlag, String appPath, List<Map<String, String>> mapList) {
        Map<String, String> hostPropertiesMap = mapList.get(0);
        String hostPropertiesPath = appPath + "mule-host.properties";
        for (String key : hostPropertiesMap.keySet()) {
            FileUtil.removeOrReplaceFileContent(removeFlag, hostPropertiesPath, key, hostPropertiesMap.get(key));
        }
        Map<String, String> appPropertiesMap = mapList.get(1);
        String appPropertiesPath = appPath + "mule-app.properties";
        for (String key : appPropertiesMap.keySet()) {
            FileUtil.removeOrReplaceFileContent(removeFlag, appPropertiesPath, key, appPropertiesMap.get(key));
        }
        return true;
    }

    /*** 添加配置文件
     * @param removeFlag
     * @param appPath
     * @param svgTemplateContent **/
    private static boolean appendSvgCode(boolean removeFlag, String appPath, String svgType, String svgTemplateContent) {
        SAXReader reader = new SAXReader();
        Document svgDoc = null;
        Element svgRoot = null;
        File flowFile = null;
        Document flowDoc = null;
        Element flowRoot = null;
        String flowName = null;
        String cfgName = null;
        File cfgFile = null;
        Document cfgDoc = null;
        Element cfgRoot = null;
        switch (svgType.toUpperCase()) {
            case "REST":
                flowName = "rest-flow";
                cfgName = "http-req-cfg";
                break;
            case "SOAP":
                flowName = "soap-flow";
                cfgName = "cxf-cfg,ws-cs-cfg";
                break;
            case "DB":
                flowName = "db-flow";
                cfgName = "db-cfg";
                break;
            default:
                break;
        }
        try {
            svgDoc = DocumentHelper.parseText(svgTemplateContent);
            svgRoot = svgDoc.getRootElement();
            //flow文件
            flowFile = new File(appPath + flowName + ".xml");
            flowDoc = reader.read(flowFile);
            flowRoot = flowDoc.getRootElement();
            appendOrRemoveNodeByTypeName(removeFlag, flowRoot, svgRoot, "flow", flowName);
            saveToXml(flowFile, flowDoc);
            //cfg文件
            if (cfgName.contains(",")) {
                String[] nameArr = cfgName.split(",");
                for (String name : nameArr) {
                    cfgFile = new File(appPath + name + ".xml");
                    cfgDoc = reader.read(cfgFile);
                    cfgRoot = cfgDoc.getRootElement();
                    appendOrRemoveCfgNodeCode(removeFlag, cfgRoot, svgRoot, name);
                    saveToXml(cfgFile, cfgDoc);
                }
            } else {
                cfgFile = new File(appPath + cfgName + ".xml");
                cfgDoc = reader.read(cfgFile);
                cfgRoot = cfgDoc.getRootElement();
                appendOrRemoveCfgNodeCode(removeFlag, cfgRoot, svgRoot, cfgName);
                saveToXml(cfgFile, cfgDoc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /*** 根据类型和名称添加或删除节点代码 **/
    public static void appendOrRemoveNodeByTypeName(boolean removeFlag, Element flowRoot, Element svgRoot, String type, String flowName) {
        System.out.println("root->" + flowRoot.getName());
        //从模板中获取指定的flow
        Element flowEle = getMuleSigNodeByTypeName(svgRoot, type, flowName);
        //若已存在同名flow，先删除原有的
        if (flowEle != null) {
            removeFlowCode(flowRoot, flowEle.attributeValue("name"));
            if (!removeFlag) {
                flowEle.setParent(null);
                flowRoot.add(flowEle);
            }
        }
    }

    /*** 根据名称添加或移除代码 **/
    public static void appendOrRemoveCfgNodeCode(boolean removeFlag, Element cfgRoot, Element svgRoot, String name) {
        //从模板中获取指定的flow
        Element flowEle = getMuleSigNodeByName(svgRoot, name);
        //若已存在同名flow，先删除原有的
        if (flowEle != null) {
            removeCfgCode(cfgRoot, flowEle.attributeValue("name"));
            if (!removeFlag) {
                flowEle.setParent(null);
                cfgRoot.add(flowEle);
            }
        }
    }


    public static void removeFlowCode(Element svcRoot, String flowName) {
        //2-删除flow代码
        Map<String, String> flowMap = new HashMap<>();
        flowMap.put("flow", flowName);
        removeNode(svcRoot, flowMap);
    }

    public static void removeCfgCode(Element cfgRoot, String cfgName) {
        //2-删除flow代码
        List<Element> elementList = getMuleListNodeByName(cfgRoot, cfgName);
        for (Element e : elementList) {
            cfgRoot.remove(e);
        }
    }

    /*** 删除节点**/
    public static boolean removeNode(Element root, Map<String, String> removeMap) {
        for (String nodeType : removeMap.keySet()) {
            String nodeName = removeMap.get(nodeType);
            List<Element> elementList = getMuleListNodeByTypeName(root, nodeType, nodeName);
            for (Element e : elementList) {
                root.remove(e);
            }
        }
        return true;
    }

    // 查询单一节点
    private static Element getMuleSigNodeByName(Element root, String nameKey) {
        List<Element> elementList = root.elements();
        for (Element element : elementList) {
            String name = element.attributeValue("name");
            System.out.println(" name->" + name + " nameKey->" + nameKey);
            if (name.startsWith(nameKey)) {
                return element;
            }
        }
        return null;
    }

    private static Element getMuleSigNodeByTypeName(Element root, String nodeType, String nameKey) {
        List<Element> elementList = root.elements();
        for (Element element : elementList) {
            String type = element.getName();
            String name = element.attributeValue("name");
            //System.out.println("--name->" + name + " nameKey->" + nameKey + " nodeType->" + nodeType + " type->" + type);
            if (nodeType.equals(type) && name.startsWith(nameKey)) {
                System.out.println("getMuleSigNodeByTypeName--name->" + name + " nameKey->" + nameKey);
                return element;
            }
        }
        return null;
    }

    // 通过Name查询节点List
    private static List<Element> getMuleListNodeByName(Element root, String nodeName) {
        List<Element> elementList = root.elements();
        List<Element> resList = new ArrayList<>();
        for (Element element : elementList) {
            String name = element.attributeValue("name");
            if (nodeName.equals(name)) {
                resList.add(element);
            }
        }
        return resList;
    }

    // 通过Type和Name查询节点List
    private static List<Element> getMuleListNodeByTypeName(Element root, String nodeType, String nodeName) {
        List<Element> elementList = root.elements();
        List<Element> resList = new ArrayList<>();
        for (Element element : elementList) {
            String type = element.getName();
            String name = element.attributeValue("name");
            if (nodeType.equals(type) && nodeName.equals(name)) {
                System.out.println("getMuleListNodeByName--type->" + type + " name->" + name);
                resList.add(element);
            }
        }
        return resList;
    }

    // 将节点配置写入xml文件
    public static void saveToXml(File file, Document document) throws IOException {
        //        创建一种输出格式    每个节点元素可自动换行
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter xmlWriter = new XMLWriter(new FileWriter(file), outputFormat);//写入XML文件的位置 以及指定的格式
        xmlWriter.write(document);//开始写入XML文件   写入Document对象
        xmlWriter.close();
        System.out.println("文件更新成功！");
    }


}
