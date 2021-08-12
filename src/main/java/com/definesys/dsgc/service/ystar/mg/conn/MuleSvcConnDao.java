package com.definesys.dsgc.service.ystar.mg.conn;

import com.definesys.dsgc.service.ystar.mg.conn.bean.MuleSvcConnBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("MuleSvcConnDao")
public class MuleSvcConnDao {

    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult pageQuerySvcGenConn(MuleSvcConnBean muleSvcConnBean, int pageIndex, int pageSize) {
        return this.sw.buildQuery()
                .eq("connName", muleSvcConnBean.getConnName())
                .eq("connType", muleSvcConnBean.getConnType())
                .doPageQuery(pageIndex, pageSize, MuleSvcConnBean.class);
    }

    public List<MuleSvcConnBean> listQuerySvcGenConn(MuleSvcConnBean MuleSvcConnBean) {
        return this.sw.buildQuery()
                .eq("connName", MuleSvcConnBean.getConnName())
                .eq("connType", MuleSvcConnBean.getConnType())
                .doQuery(MuleSvcConnBean.class);
    }

    public List<MuleSvcConnBean> listQuerySvcGenConnByType(String dbType) {
        return this.sw.buildQuery()
                .eq("connType", "DB")
                .eq("attr1", dbType)
                .doQuery(MuleSvcConnBean.class);
    }


    public MuleSvcConnBean sigQuerySvcGenConn(MuleSvcConnBean MuleSvcConnBean) {
        return sw.buildQuery().ne("connId", MuleSvcConnBean.getConnId()).eq("connName", MuleSvcConnBean.getConnName()).doQueryFirst(MuleSvcConnBean.class);
    }

    public String addSvcGenDBConnectInfo(MuleSvcConnBean MuleSvcConnBean) {
        this.sw.buildQuery().doInsert(MuleSvcConnBean);
        return MuleSvcConnBean.getConnId();
    }

    public void removeSvcGenDBConnectInfoById(String connId) {
        this.sw.buildQuery().eq("connId", connId).doDelete(MuleSvcConnBean.class);
    }

    public MuleSvcConnBean querySvcGenConnectById(String connId) {
        return sw.buildQuery().eq("connId", connId).doQueryFirst(MuleSvcConnBean.class);
    }

    public MuleSvcConnBean querySvcGenConnectByName(String connName) {
        return sw.buildQuery().eq("connName", connName).doQueryFirst(MuleSvcConnBean.class);
    }

}
