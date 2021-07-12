package com.definesys.dsgc.service.ystar.excel;

import com.definesys.dsgc.service.svcinfo.bean.SVCUriBean;
import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadSampleBean;
import com.definesys.dsgc.service.ystar.excel.bean.SvcPayloadParam;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PayloadParamDao {

    @Autowired
    private MpaasQueryFactory sw;

    //保存
    public void savePayloadParam(SvcPayloadParam svcPayloadParam) {
        sw.buildQuery()
                .doInsert(svcPayloadParam);
    }

    //删除
    public void removePayloadParam(String svcCode) {
        List<SvcPayloadParam> svcPayloadParams = sw.buildQuery().eq("resCode", svcCode).doQuery(SvcPayloadParam.class);
        for (SvcPayloadParam svcParam : svcPayloadParams) {
            this.sw.buildQuery()
                    .eq("dpparamId", svcParam.getDpparamId())
                    .doDelete(SvcPayloadParam.class);
        }

    }

    public SVCUriBean querySvcUriBySvcCode(String svcCode) {
        return this.sw.buildQuery().eq("servNo", svcCode).doQueryFirst(SVCUriBean.class);
    }

    //保存报文示例
    public void savePayloadSample(DSGCPayloadSampleBean sampleBean) {
        this.sw.buildQuery().doInsert(sampleBean);
    }

    //删除报文示例
    public void removePayloadSample(String svcCode) {
        List<DSGCPayloadSampleBean> sampleBeans = this.sw.buildQuery().eq("resCode", svcCode).doQuery(DSGCPayloadSampleBean.class);
        for (DSGCPayloadSampleBean sample : sampleBeans) {
            this.sw.buildQuery().eq("resCode", sample.getResCode()).doDelete(DSGCPayloadSampleBean.class);
        }
    }
}
