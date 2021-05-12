package com.definesys.dsgc.service.ystar.svcgen.svcdpl;

import com.definesys.dsgc.service.ystar.svcgen.bean.SvcgenServiceInfo;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: SvcgenServiceInfoDao
 * @Description: TODO
 * @Author：afan
 * @Date : 2020/1/10 11:13
 */

@Repository
public class SvcgenServiceInfoDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void insertSvcServInfo(SvcgenServiceInfo svcgenServiceInfo) {
        sw.buildQuery().doInsert(svcgenServiceInfo);
    }

    public List<SvcgenServiceInfo> queryServByservNo(String servNo) {
        return sw.buildQuery().eq("servNo", servNo).unSelect("sysName").doQuery(SvcgenServiceInfo.class);
    }

    public SvcgenServiceInfo querySvcGenInfoBySvcCode(String svcCode) {
        return sw.buildQuery().eq("SERV_NO", svcCode).doQueryFirst(SvcgenServiceInfo.class);
    }

    public PageQueryResult querySvcGenInfo(String q, int page, int
            pageSize) {
        PageQueryResult result = sw.buildViewQuery("svggen_service_v")
                .or()
                .likeNocase("SERV_NO", q)
                .like("SERV_NAME", q)
                .likeNocase("SYS_CODE", q)
                .orderBy("creationDate", "desc")
                .doPageQuery(page, pageSize, SvcgenServiceInfo.class);
        return result;
    }

    public void updateRestStatus(String servNo) {
        sw.buildQuery()
                .update("TEXT_ATTRIBUTE2", '1')
                .eq("servNo", servNo)
                .doUpdate(SvcgenServiceInfo.class);
    }

    public void updatePublishStatus(String svcCode,String status) {
        sw.buildQuery()
                .update("TEXT_ATTRIBUTE3", status)
                .eq("servNo", svcCode)
                .doUpdate(SvcgenServiceInfo.class);
    }
}
