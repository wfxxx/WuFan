package com.definesys.dsgc.service.wls;


import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;


public class DefaultAuthenticatorAdapter {
    private static MBeanServerConnection connection;
    private static JMXConnector connector;
    private static ObjectName service;

    static {
        try {
            service =
                    new ObjectName("Security:Name=myrealmDefaultAuthenticator");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    public void connection(String hostname, String portString,
                           String username,
                           String password) throws IOException,
            MalformedURLException {
        String protocol = "t3";
        Integer portInteger = Integer.valueOf(portString);
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL =
                new JMXServiceURL(protocol, hostname, port, jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }

    public void createUser(String username, String password,
                           String description) throws InstanceNotFoundException,
            MBeanException,
            ReflectionException,
            IOException {
        Object[] params = {username, password, description};
        String[] signature = {"java.lang.String", "java.lang.String", "java.lang.String"};
        Object o = connection.invoke(service, "createUser", params, signature);
    }

    public void deleteUser(String username) throws InstanceNotFoundException,
            MBeanException,
            ReflectionException,
            IOException {
        Object[] params = {username};
        String[] signature = {"java.lang.String"};
        Object o = connection.invoke(service, "deleteUser", params, signature);
    }

    public void changeUserPassword(String username, String oldPassword, String newPassword) throws ReflectionException, MBeanException, InstanceNotFoundException, IOException {
        Object[] params = {username, oldPassword, newPassword};
        String[] signature = {"java.lang.String", "java.lang.String", "java.lang.String"};
        connection.invoke(service, "changeUserPassword", params, signature);
    }

    public void close() throws IOException {
        connector.close();
    }

    public static void main(String[] args) {
        String hostname = "168.168.3.9";
        String portString = "7001";
        String username = "weblogic";
        String password = "welcome1";
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DefaultAuthenticatorAdapter c = new DefaultAuthenticatorAdapter();
        try {
            c.connection(hostname, portString, username, password);
            c.deleteUser("zero");
//            c.createUser("zero", "welcome1", "Java代码创建用户" + format2.format(new Date()));
//        c.changeUserPassword("zero", "welcome1", "welcome4");

        } catch (Exception e) {
            System.out.println("message123 = " + e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
