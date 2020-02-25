package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGRouteHostsBean;
import com.definesys.dsgc.service.dagclient.bean.DAGRouteInfoBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public class RouteDeployDao {
    @Autowired
    private MpaasQueryFactory sw;

    public DAGRouteInfoBean getRouteBasicInfo(String vid) {
        return sw.buildViewQuery("V_ROUTE_DTL").setVar("vidVar",vid).doQueryFirst(DAGRouteInfoBean.class);
    }

    public String[] getRouteHostNames(String vid) {
        List<DAGRouteHostsBean> res = sw.buildQuery().eq("vid",vid).doQuery(DAGRouteHostsBean.class);
        if (res != null && res.size() > 0) {
            String[] rtn = new String[res.size()];
            Iterator<DAGRouteHostsBean> iters = res.iterator();
            int i = 0;
            while (iters.hasNext()) {
                DAGRouteHostsBean host = iters.next();
                rtn[i] = host.getHostname();
                i++;
            }
            return rtn;
        } else {
            return null;
        }

    }

}
