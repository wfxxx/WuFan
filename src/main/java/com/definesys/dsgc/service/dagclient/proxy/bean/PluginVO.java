package com.definesys.dsgc.service.dagclient.proxy.bean;

public class PluginVO {
    public String id;
    public String name;
    public boolean enabled;
    public Object config;
    public IdVO route;
    public IdVO service;
    public IdVO consumer;
    public String[] tags;
}
