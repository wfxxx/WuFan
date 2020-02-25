package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;
import com.definesys.mpaas.query.model.MpaasBasePojo;

@SQLQuery(value={
        @SQL(view="V_BS_DTL",sql="select v.vid,\n" +
                "       b.bs_code,\n" +
                "       d.protocal,\n" +
                "       d.host_name,\n" +
                "       d.port,\n" +
                "       d.paths,\n" +
                "       d.rty_count,\n" +
                "       d.connect_timeout,\n" +
                "       d.send_timeout,\n" +
                "       d.read_timeout\n" +
                "  from dag_code_version v, dag_bs b, dag_bs_dtl d\n" +
                " where v.sour_code = b.bs_code\n" +
                "   and v.vid = d.vid\n" +
                "   and v.vid = #vidVar")
})
public class DAGServiceInfoBean extends MpaasBasePojo {

    @Column(value = "vid", type = ColumnType.DB)
    private String vid;
    @Column(value = "bs_code", type = ColumnType.DB)
    private String bsCode;
    @Column(value = "protocal", type = ColumnType.DB)
    private String protocal;
    @Column(value = "host_name", type = ColumnType.DB)
    private String hostName;
    @Column(value = "port", type = ColumnType.DB)
    private String port;
    @Column(value = "paths", type = ColumnType.DB)
    private String paths;
    @Column(value = "rty_count", type = ColumnType.DB)
    private int rtyCount;
    @Column(value = "connect_timeout", type = ColumnType.DB)
    private long connectTimeout;
    @Column(value = "send_timeout", type = ColumnType.DB)
    private long sendTimeout;
    @Column(value = "read_timeout", type = ColumnType.DB)
    private long readTimeout;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public int getRtyCount() {
        return rtyCount;
    }

    public void setRtyCount(int rtyCount) {
        this.rtyCount = rtyCount;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getSendTimeout() {
        return sendTimeout;
    }

    public void setSendTimeout(long sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }
}
