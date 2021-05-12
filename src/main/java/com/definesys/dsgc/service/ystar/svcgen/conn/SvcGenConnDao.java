package com.definesys.dsgc.service.ystar.svcgen.conn;

import com.definesys.dsgc.service.ystar.svcgen.conn.bean.SvcgenConnBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SvcGenConnDao")
public class SvcGenConnDao {

    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult pageQuerySvcGenConn(SvcgenConnBean svcgenConnBean, int pageIndex, int pageSize) {
        return this.sw.buildQuery()
                .eq("connName", svcgenConnBean.getConnName())
                .eq("connType", svcgenConnBean.getConnType())
                .doPageQuery(pageIndex, pageSize, SvcgenConnBean.class);
    }

    public List<SvcgenConnBean> querySvcGenConnList(SvcgenConnBean svcgenConnBean) {
        return this.sw.buildQuery()
                .eq("connName", svcgenConnBean.getConnName())
                .eq("connType", svcgenConnBean.getConnType())
                .doQuery(SvcgenConnBean.class);
    }

    public SvcgenConnBean queryFirstSvcGenConnect(SvcgenConnBean svcgenConnBean) {
        return sw.buildQuery().ne("connId", svcgenConnBean.getConnId()).eq("connName", svcgenConnBean.getConnName()).doQueryFirst(SvcgenConnBean.class);
    }

    public String addSvcGenDBConnectInfo(SvcgenConnBean svcgenConnBean) {
        this.sw.buildQuery().doInsert(svcgenConnBean);
        return svcgenConnBean.getConnId();
    }

    public void removeSvcGenDBConnectInfoById(String connId) {
        this.sw.buildQuery().eq("connId", connId).doDelete(SvcgenConnBean.class);
    }

}
