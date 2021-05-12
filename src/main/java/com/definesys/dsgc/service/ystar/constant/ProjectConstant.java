package com.definesys.dsgc.service.ystar.constant;

public class ProjectConstant {

    public static final String PRO_POM = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "\t<modelVersion>4.0.0</modelVersion>\n" +
            "\t<groupId>com.definesys.esb.mule</groupId>\n" +
            "\t<artifactId>#projectName#</artifactId>\n" +
            "\t<version>0.0.1-SNAPSHOT</version>\n" +
            "\t<packaging>war</packaging>\n" +
            "\t<build>\n" +
            "\t\t<finalName>#projectName#</finalName>\n" +
            "\t\t<plugins>\n" +
            "\t\t\t<plugin>\n" +
            "\t\t\t\t<groupId>org.apache.maven.plugins</groupId>\n" +
            "\t\t\t\t<artifactId>maven-war-plugin</artifactId>\n" +
            "\t\t\t\t<version>3.2.2</version>\n" +
            "\t\t\t\t<configuration>\n" +
            "\t\t\t\t\t<webResources>\n" +
            "\t\t\t\t\t\t<resource>\n" +
            "\t\t\t\t\t\t\t<targetPath>WEB-INF</targetPath>\n" +
            "\t\t\t\t\t\t\t<directory>src/main/app</directory>\n" +
            "\t\t\t\t\t\t\t<includes>\n" +
            "\t\t\t\t\t\t\t\t<include>*.xml</include>\n" +
            "\t\t\t\t\t\t\t</includes>\n" +
            "\t\t\t\t\t\t</resource>\n" +
            "\t\t\t\t\t\t<resource>\n" +
            "\t\t\t\t\t\t\t<targetPath>WEB-INF/classes</targetPath>\n" +
            "\t\t\t\t\t\t\t<directory>src/main/app</directory>\n" +
            "\t\t\t\t\t\t\t<includes>\n" +
            "\t\t\t\t\t\t\t\t<include>*.properties</include>\n" +
            "\t\t\t\t\t\t\t</includes>\n" +
            "\t\t\t\t\t\t</resource>\n" +
            "\t\t\t\t\t\t<resource>\n" +
            "\t\t\t\t\t\t\t<targetPath>WEB-INF/classes</targetPath>\n" +
            "\t\t\t\t\t\t\t<directory>src/main/wsdl</directory>\n" +
            "\t\t\t\t\t\t\t<includes>\n" +
            "\t\t\t\t\t\t\t\t<include>**/*</include>\n" +
            "\t\t\t\t\t\t\t</includes>\n" +
            "\t\t\t\t\t\t</resource>\n" +
            "\t\t\t\t\t</webResources>\n" +
            "\t\t\t\t\t<failOnMissingWebXml>false</failOnMissingWebXml>\n" +
            "\t\t\t\t</configuration>\n" +
            "\t\t\t</plugin>\n" +
            "\t\t\t<plugin>\n" +
            "\t\t\t\t<artifactId>maven-compiler-plugin</artifactId>\n" +
            "\t\t\t\t<version>3.1</version>\n" +
            "\t\t\t\t<configuration>\n" +
            "\t\t\t\t\t<source>1.8</source>\n" +
            "\t\t\t\t\t<target>1.8</target>\n" +
            "\t\t\t\t\t<encoding>utf8</encoding>\n" +
            "\t\t\t\t\t<compilerArguments>\n" +
            "\t\t\t\t\t\t<extdirs>lib</extdirs>\n" +
            "\t\t\t\t\t</compilerArguments>\n" +
            "\t\t\t\t</configuration>\n" +
            "\t\t\t</plugin>\n" +
            "\t\t</plugins>\n" +
            "\t</build>\n" +
            "</project>";
    public static final String PRO_MULE_PROJECT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<mule-project xmlns=\"http://www.mulesoft.com/tooling/project\" runtimeId=\"org.mule.tooling.server.3.9.4.ee\" schemaVersion=\"5.3.0.0\">\n" +
            "    <name>#projectName#</name>\n" +
            "    <description></description>\n" +
            "</mule-project>\n";
    public static final String PRO_SOAP_APP_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
            "<mule xmlns:cxf=\"http://www.mulesoft.org/schema/mule/cxf\" xmlns:ws=\"http://www.mulesoft.org/schema/mule/ws\" xmlns:mulexml=\"http://www.mulesoft.org/schema/mule/xml\" xmlns:dw=\"http://www.mulesoft.org/schema/mule/ee/dw\" xmlns:metadata=\"http://www.mulesoft.org/schema/mule/metadata\" xmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns:sap=\"http://www.mulesoft.org/schema/mule/sap\" xmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\" xmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:db=\"http://www.mulesoft.org/schema/mule/db\" xmlns:json=\"http://www.mulesoft.org/schema/mule/json\" xmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:core=\"http://www.mulesoft.org/schema/mule/core\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
            "http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/sap http://www.mulesoft.org/schema/mule/sap/current/mule-sap.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/dw http://www.mulesoft.org/schema/mule/ee/dw/current/dw.xsd\n" +
            "http://www.mulesoft.org/schema/mule/xml http://www.mulesoft.org/schema/mule/xml/current/mule-xml.xsd\n" +
            "http://www.mulesoft.org/schema/mule/cxf http://www.mulesoft.org/schema/mule/cxf/current/mule-cxf.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ws http://www.mulesoft.org/schema/mule/ws/current/mule-ws.xsd\"></mule>\n";

    public static final String PRO_SOAP_CFG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
            "<mule xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
            "\txmlns:db=\"http://www.mulesoft.org/schema/mule/db\"\n" +
            "\txmlns:ws=\"http://www.mulesoft.org/schema/mule/ws\"\n" +
            "\txmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
            "\txmlns:sap=\"http://www.mulesoft.org/schema/mule/sap\"\n" +
            "\txmlns:cxf=\"http://www.mulesoft.org/schema/mule/cxf\"\n" +
            "\txmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "\txmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txsi:schemaLocation=\"http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ws http://www.mulesoft.org/schema/mule/ws/current/mule-ws.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/sap http://www.mulesoft.org/schema/mule/sap/current/mule-sap.xsd\n" +
            "http://www.mulesoft.org/schema/mule/cxf http://www.mulesoft.org/schema/mule/cxf/current/mule-cxf.xsd\n" +
            "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\">" +
            "<!-- 引入属性文件 -->\n" +
            "    <context:property-placeholder location=\"dev.properties\"/>"+
            " <!-- Soap Exception -->\n" +
            "    <catch-exception-strategy name=\"CatchSoapExceptionStrategy\">\n" +
            "        <set-property propertyName=\"http.status\" value=\"500\" doc:name=\"Property\"/>\n" +
            "        <set-payload value=\"&lt;response&gt;\n" +
            "\t&lt;rtnCode&gt;U&lt;/rtnCode&gt;\n" +
            "\t&lt;rtnMsg&gt;&#35775;&#38382;&#22833;&#36133;&lt;/rtnMsg&gt;\n" +
            "&lt;/response&gt;\" doc:name=\"Set Payload\" encoding=\"UTF-8\"/>\n" +
            "\t\t<custom-transformer class=\"com.definesys.dsgc.mule.processor.MuleMsgProcessor\" doc:name=\"OB_RES\">\n" +
            "\t\t\t<spring:property name=\"stage\" value=\"OB_RES\"/>\n" +
            "\t\t</custom-transformer>\n" +
            "\t\t<custom-transformer class=\"com.definesys.dsgc.mule.processor.MuleMsgProcessor\" doc:name=\"INST_RES\">\n" +
            "\t\t\t<spring:property name=\"stage\" value=\"INST_RES\"/>\n" +
            "\t\t</custom-transformer>\n" +
            "    </catch-exception-strategy>\n" +
            "    <catch-exception-strategy when=\"#[exception.causeMatches('com.definesys.dsgc.mule.security.*')]\" name=\"securitySoapException\">\n" +
            "        <set-property propertyName=\"http.status\" value=\"401\" doc:name=\"Property\"/>\n" +
            "        <set-payload value=\"&lt;response&gt;\n" +
            "\t&lt;rtnCode&gt;U&lt;/rtnCode&gt;\n" +
            "\t&lt;rtnMsg&gt;&#35748;&#35777;&#22833;&#36133;&lt;/rtnMsg&gt;\n" +
            "&lt;/response&gt;\" doc:name=\"Set Payload\" encoding=\"UTF-8\"/>\n" +
            "    </catch-exception-strategy>\n" +
            "    <choice-exception-strategy name=\"Global_Soap_Exception_Strategy\">\n" +
            "        <exception-strategy ref=\"securitySoapException\" doc:name=\"Reference Exception Strategy\"/>\n" +
            "        <exception-strategy ref=\"CatchSoapExceptionStrategy\" doc:name=\"Reference Exception Strategy\"/>\n" +
            "    </choice-exception-strategy>\n" +
            "<!-- LES暴露的HTTP -->\n" +
            "    <http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"${#projectName#.http.port}\" doc:name=\"HTTP Listener Configuration\"/>" +
            "</mule>";

    public static final String PRO_REST_APP_XML = "<mule xmlns:json=\"http://www.mulesoft.org/schema/mule/json\"\n" +
            "\txmlns:metadata=\"http://www.mulesoft.org/schema/mule/metadata\"\n" +
            "\txmlns:http=\"http://www.mulesoft.org/schema/mule/http\" xmlns:ftp=\"http://www.mulesoft.org/schema/mule/ee/ftp\"\n" +
            "\txmlns:tracking=\"http://www.mulesoft.org/schema/mule/ee/tracking\" xmlns=\"http://www.mulesoft.org/schema/mule/core\"\n" +
            "\txmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "\txmlns:spring=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/ftp http://www.mulesoft.org/schema/mule/ee/ftp/current/mule-ftp-ee.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd\"></mule>\n";
    public static final String PRO_REST_CFG_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
            "<mule xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
            "\txmlns:db=\"http://www.mulesoft.org/schema/mule/db\"\n" +
            "\txmlns:ws=\"http://www.mulesoft.org/schema/mule/ws\"\n" +
            "\txmlns:http=\"http://www.mulesoft.org/schema/mule/http\"\n" +
            "\txmlns:sap=\"http://www.mulesoft.org/schema/mule/sap\"\n" +
            "\txmlns:cxf=\"http://www.mulesoft.org/schema/mule/cxf\"\n" +
            "\txmlns=\"http://www.mulesoft.org/schema/mule/core\" xmlns:doc=\"http://www.mulesoft.org/schema/mule/documentation\"\n" +
            "\txmlns:spring=\"http://www.springframework.org/schema/beans\" \n" +
            "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "\txsi:schemaLocation=\"http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/db http://www.mulesoft.org/schema/mule/db/current/mule-db.xsd\n" +
            "http://www.mulesoft.org/schema/mule/ws http://www.mulesoft.org/schema/mule/ws/current/mule-ws.xsd\n" +
            "http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd\n" +
            "http://www.mulesoft.org/schema/mule/sap http://www.mulesoft.org/schema/mule/sap/current/mule-sap.xsd\n" +
            "http://www.mulesoft.org/schema/mule/cxf http://www.mulesoft.org/schema/mule/cxf/current/mule-cxf.xsd\n" +
            "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd\n" +
            "http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd\">" +
            "<!-- 引入属性文件 -->\n" +
            "    <context:property-placeholder location=\"dev.properties\"/>"+
            "<!-- Rest Exception -->\n" +
            "    <catch-exception-strategy name=\"CatchExceptionStrategy\">\n" +
            "        <set-property propertyName=\"http.status\" value=\"500\" doc:name=\"Property\"/>\n" +
            "        <set-payload value=\"#['{&quot;rtnCode&quot;:&quot;U&quot;,&quot;rtnMsg&quot;:&quot;&#35775;&#38382;&#22833;&#36133;&#65281;&quot;}']\" doc:name=\"Set Payload\"/>\n" +
            "\t\t<custom-transformer class=\"com.definesys.dsgc.mule.processor.MuleMsgProcessor\" doc:name=\"OB_RES\">\n" +
            "\t\t\t<spring:property name=\"stage\" value=\"OB_RES\"/>\n" +
            "\t\t</custom-transformer>\n" +
            "\t\t<custom-transformer class=\"com.definesys.dsgc.mule.processor.MuleMsgProcessor\" doc:name=\"INST_RES\">\n" +
            "\t\t\t<spring:property name=\"stage\" value=\"INST_RES\"/>\n" +
            "\t\t</custom-transformer>\n" +
            "    </catch-exception-strategy>\n" +
            "    <catch-exception-strategy when=\"#[exception.causeMatches('com.definesys.dsgc.mule.security.*')]\" name=\"securityException\">\n" +
            "        <set-property propertyName=\"http.status\" value=\"401\" doc:name=\"Property\"/>\n" +
            "        <set-payload value=\"#['{&quot;rtnCode&quot;:&quot;401&quot;,&quot;rtnMsg&quot;:&quot;认证失败！&quot;']\" doc:name=\"Set Payload\"/>\n" +
            "    </catch-exception-strategy>\n" +
            "    <choice-exception-strategy name=\"Global_Rest_Exception_Strategy\">\n" +
            "        <exception-strategy ref=\"securityException\" doc:name=\"Reference Exception Strategy\"/>\n" +
            "        <exception-strategy ref=\"CatchExceptionStrategy\" doc:name=\"Reference Exception Strategy\"/>\n" +
            "    </choice-exception-strategy>" +
            "<!-- LES暴露的HTTP -->\n" +
            "    <http:listener-config name=\"HTTP_Listener_Configuration\" host=\"0.0.0.0\" port=\"${#projectName#.http.port}\" doc:name=\"HTTP Listener Configuration\"/>" +
            "</mule>";

    public static final String PRO_DEV_PROPERTIES = "# #projectName# HTTP\n" +
            "#projectName#.http.port=#projectPort#";
    public static final String PRO_PRD_PROPERTIES = "# #projectName# HTTP\n" +
            "#projectName#.http.port=#projectPort#";

    public static final String PRO_DEPLOY_PROPERTIES = "##** GENERATED CONTENT ** Mule Application Deployment Descriptor\n" +
            "domain=default\n" +
            "config.resources=#projectName#.xml\n" +
            "redeployment.enabled=true\n" +
            "encoding=UTF-8\n";
    public static final String PRO_RESOURCE_LOG4J2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<Configuration>\n" +
            "\n" +
            "\t<!--These are some of the loggers you can enable. \n" +
            "\t    There are several more you can find in the documentation. \n" +
            "        Besides this log4j configuration, you can also use Java VM environment variables\n" +
            "        to enable other logs like network (-Djavax.net.debug=ssl or all) and \n" +
            "        Garbage Collector (-XX:+PrintGC). These will be append to the console, so you will \n" +
            "        see them in the mule_ee.log file. -->\n" +
            "\n" +
            "\n" +
            "    <Appenders>\n" +
            "        <RollingFile name=\"file\" fileName=\"${sys:mule.home}${sys:file.separator}logs${sys:file.separator}#projectName#.log\" \n" +
            "                 filePattern=\"${sys:mule.home}${sys:file.separator}logs${sys:file.separator}#projectName#-%i.log\">\n" +
            "            <PatternLayout pattern=\"%d [%t] %-5p %c - %m%n\" />\n" +
            "            <SizeBasedTriggeringPolicy size=\"10 MB\" />\n" +
            "            <DefaultRolloverStrategy max=\"10\"/>\n" +
            "        </RollingFile>\n" +
            "    </Appenders>\n" +
            "    <Loggers>\n" +
            "    \t\n" +
            "    \t\n" +
            "\t\t<!-- Http Logger shows wire traffic on DEBUG -->\n" +
            "\t\t<AsyncLogger name=\"org.mule.module.http.internal.HttpMessageLogger\" level=\"WARN\"/>\n" +
            "\t\n" +
            "\t\t<!-- JDBC Logger shows queries and parameters values on DEBUG -->\n" +
            "\t\t<AsyncLogger name=\"com.mulesoft.mule.transport.jdbc\" level=\"WARN\"/>\n" +
            "    \n" +
            "        <!-- CXF is used heavily by Mule for web services -->\n" +
            "        <AsyncLogger name=\"org.apache.cxf\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Apache Commons tend to make a lot of noise which can clutter the log-->\n" +
            "        <AsyncLogger name=\"org.apache\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Reduce startup noise -->\n" +
            "        <AsyncLogger name=\"org.springframework.beans.factory\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Mule classes -->\n" +
            "        <AsyncLogger name=\"org.mule\" level=\"INFO\"/>\n" +
            "        <AsyncLogger name=\"com.mulesoft\" level=\"INFO\"/>\n" +
            "\n" +
            "        <!-- Reduce DM verbosity -->\n" +
            "        <AsyncLogger name=\"org.jetel\" level=\"WARN\"/>\n" +
            "        <AsyncLogger name=\"Tracking\" level=\"WARN\"/>\n" +
            "        \n" +
            "        <AsyncRoot level=\"INFO\">\n" +
            "            <AppenderRef ref=\"file\" />\n" +
            "        </AsyncRoot>\n" +
            "    </Loggers>\n" +
            "</Configuration>";
    public static final String PRO_RESOURCE_LOG4J2_TEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Configuration>\n" +
            "    <Appenders>\n" +
            "        <Console name=\"Console\" target=\"SYSTEM_OUT\">\n" +
            "            <PatternLayout pattern=\"%-5p %d [%t] %c: %m%n\"/>\n" +
            "        </Console>\n" +
            "    </Appenders>\n" +
            "\n" +
            "    <Loggers>\n" +
            "\n" +
            "        <!-- CXF is used heavily by Mule for web services -->\n" +
            "        <AsyncLogger name=\"org.apache.cxf\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Apache Commons tend to make a lot of noise which can clutter the log-->\n" +
            "        <AsyncLogger name=\"org.apache\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Reduce startup noise -->\n" +
            "        <AsyncLogger name=\"org.springframework.beans.factory\" level=\"WARN\"/>\n" +
            "\n" +
            "        <!-- Mule classes -->\n" +
            "        <AsyncLogger name=\"org.mule\" level=\"INFO\"/>\n" +
            "        <AsyncLogger name=\"com.mulesoft\" level=\"INFO\"/>\n" +
            "\n" +
            "        <!-- Reduce DM verbosity -->\n" +
            "        <AsyncLogger name=\"org.jetel\" level=\"WARN\"/>\n" +
            "        <AsyncLogger name=\"Tracking\" level=\"WARN\"/>\n" +
            "        \n" +
            "        <!--AsyncLogger name=\"httpclient.wire\" level=\"ALL\" /-->\n" +
            "\n" +
            "        <AsyncRoot level=\"INFO\">\n" +
            "            <AppenderRef ref=\"Console\"/>\n" +
            "        </AsyncRoot>\n" +
            "    </Loggers>\n" +
            "\n" +
            "</Configuration>";
    public static final String PRO_WEB_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<web-app>\n" +
            "\t<display-name>#projectName#</display-name>\n" +
            "\t<context-param>\n" +
            "\t\t<param-name>org.mule.config</param-name>\n" +
            "\t\t<param-value>WEB-INF/#projectName#.xml,WEB-INF/global-cfg.xml</param-value>\n" +
            "\t</context-param>\n" +
            "\n" +
            "\t<listener>\n" +
            "\t\t<listener-class>org.mule.config.builders.MuleXmlBuilderContextListener\n" +
            "\t\t</listener-class>\n" +
            "\t</listener>\n" +
            "\t<servlet>\n" +
            "\t\t<servlet-name>muleServlet</servlet-name>\n" +
            "\t\t<servlet-class>org.mule.transport.servlet.MuleReceiverServlet\n" +
            "\t\t</servlet-class>\n" +
            "\t</servlet>\n" +
            "\t<servlet-mapping>\n" +
            "\t\t<servlet-name>muleServlet</servlet-name>\n" +
            "\t\t<url-pattern>/*</url-pattern>\n" +
            "\t</servlet-mapping>\n" +
            "\t<resource-ref>\n" +
            "\t\t<res-ref-name>jdbc/DSGCDS</res-ref-name>\n" +
            "\t\t<res-type>javax.sql.DataSource</res-type>\n" +
            "\t\t<res-auth>Container</res-auth>\n" +
            "\t</resource-ref>\n" +
            "\t<resource-ref>\n" +
            "\t\t<res-ref-name>jdbc/DSGCCACHEDS</res-ref-name>\n" +
            "\t\t<res-type>javax.sql.DataSource</res-type>\n" +
            "\t\t<res-auth>Container</res-auth>\n" +
            "\t</resource-ref>\n" +
            "</web-app>";

}
