package com.definesys.dsgc.service.ystar.mg.svc;

import com.definesys.dsgc.service.ystar.mg.svc.bean.SvcInfoView;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SvcInfoDao {
    @Autowired
    private MpaasQueryFactory sw;

    public List<SvcInfoView> listQuerySvcInfo(SvcInfoView svcInfoView, List<String> svcNoList, List<String> sysCodeList) {
        MpaasQuery mq = sw.buildViewQuery("V_SVC_INFO")
                .eq("svcNo", svcInfoView.getSvcNo())
                .eq("sysCode", svcInfoView.getSysCode())
                .eq("svcStatus", svcInfoView.getSvcStatus())
                .like("svcName", svcInfoView.getSvcName())
                .like("svcDesc", svcInfoView.getSvcDesc());
        if (svcNoList != null && svcNoList.size() > 0) {
            mq.in("svcNo", svcNoList);
        }
        if (sysCodeList != null && sysCodeList.size() > 0) {
            mq.in("sysCode", sysCodeList);
        }
        return mq.doQuery(SvcInfoView.class);
    }

    public List<SvcInfoView> listQuerySysCode(String userId) {
        return sw.buildViewQuery("V_SYS_INFO")
                .setVar("userId", userId)
                .doQuery(SvcInfoView.class);
    }


    public List<SvcInfoView> listAllQuerySysCode() {
        return sw.buildViewQuery("V_ALL_SYS_INFO").doQuery(SvcInfoView.class);
    }


}
