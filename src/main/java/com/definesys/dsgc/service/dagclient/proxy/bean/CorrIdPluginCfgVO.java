package com.definesys.dsgc.service.dagclient.proxy.bean;

public class CorrIdPluginCfgVO {
    private String header_name = "DAG-Request-ID";
    private String generator = "uuid#counter";
    private boolean echo_downstream = false;

    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public boolean isEcho_downstream() {
        return echo_downstream;
    }

    public void setEcho_downstream(boolean echo_downstream) {
        this.echo_downstream = echo_downstream;
    }
}
