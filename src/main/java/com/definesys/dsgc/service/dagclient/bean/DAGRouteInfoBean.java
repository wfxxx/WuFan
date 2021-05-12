package com.definesys.dsgc.service.dagclient.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.SQL;
import com.definesys.mpaas.query.annotation.SQLQuery;

@SQLQuery(value = {
        @SQL(view = "V_ROUTE_DTL", sql = "SELECT V.VID,\n" +
                "    R.ROUTE_CODE,\n" +
                "    R.BS_CODE,\n" +
                "    R.ROUTE_PATH,\n" +
                "    R.ROUTE_METHOD,\n" +
                "    R.STRIP_PATH\n" +
                "    FROM DAG_CODE_VERSION V, DAG_ROUTES R\n" +
                "    WHERE V.SOUR_CODE = R.ROUTE_CODE\n" +
                "    AND V.VID = #vidVar")
})
public class DAGRouteInfoBean {
    @Column(value = "vid", type = ColumnType.DB)
    private String vid;

    @Column(value = "ROUTE_CODE", type = ColumnType.DB)
    private String routeCode;

    @Column(value = "BS_CODE", type = ColumnType.DB)
    private String bsCode;

    @Column(value = "ROUTE_PATH", type = ColumnType.DB)
    private String routePath;

    @Column(value = "ROUTE_METHOD", type = ColumnType.DB)
    private String routeMethod;

    @Column(value = "STRIP_PATH", type = ColumnType.DB)
    private String stripPath;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getBsCode() {
        return bsCode;
    }

    public void setBsCode(String bsCode) {
        this.bsCode = bsCode;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getRouteMethod() {
        return routeMethod;
    }

    public void setRouteMethod(String routeMethod) {
        this.routeMethod = routeMethod;
    }

    public String getStripPath() {
        return stripPath;
    }

    public void setStripPath(String stripPath) {
        this.stripPath = stripPath;
    }
}
