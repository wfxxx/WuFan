package com.definesys.dsgc.service.ystar.excel.util;

import com.definesys.dsgc.service.svcmng.bean.DSGCPayloadSampleBean;
import com.definesys.dsgc.service.ystar.excel.bean.SvcPayloadParam;

import java.util.List;

public class PayloadUtil {

    public static DSGCPayloadSampleBean[] getPayloadSample(List<SvcPayloadParam> paramList, String svcCode, String svcType) {
        DSGCPayloadSampleBean reqSampleBean = new DSGCPayloadSampleBean(svcCode, "REQ", svcType);
        DSGCPayloadSampleBean resSampleBean = new DSGCPayloadSampleBean(svcCode, "RES", svcType);
        if ("REST".equals(svcType)) {
            reqSampleBean.setPlType("JSON");
            resSampleBean.setPlType("JSON");
            StringBuffer reqSb = new StringBuffer("{\n");
            StringBuffer resSb = new StringBuffer("{\n");
            for (SvcPayloadParam param : paramList) {
                if ("REQ".equals(param.getReqOrRes())) {
                    reqSb.append("\t\"" + param.getParamCode() + "\" : \"" + param.getParamSample() + "\"");
                } else if ("RES".equals(param.getReqOrRes())) {
                    resSb.append("\t\"" + param.getParamCode() + "\" : \"" + param.getParamSample() + "\"");
                }
            }
            reqSb.append("}");
            resSb.append("}");
            reqSampleBean.setPlSample(reqSb.toString());
            resSampleBean.setPlSample(resSb.toString());
        } else if ("SOAP".equals(svcType)) {
            reqSampleBean.setPlType("XML");
            resSampleBean.setPlType("XML");
            StringBuffer reqSb = new StringBuffer("<Body>\n");
            StringBuffer resSb = new StringBuffer("<Body>\n");
            for (SvcPayloadParam param : paramList) {
                if ("REQ".equals(param.getReqOrRes())) {
                    reqSb.append("\t<" + param.getParamCode() + ">" + param.getParamSample() + "<" + param.getParamCode() + "/>\n");
                } else if ("RES".equals(param.getReqOrRes())) {
                    resSb.append("\t<" + param.getParamCode() + ">" + param.getParamSample() + "<" + param.getParamCode() + "/>\n");
                }
            }
            reqSb.append("</Body>");
            resSb.append("</Body>");
            reqSampleBean.setPlSample(reqSb.toString());
            resSampleBean.setPlSample(resSb.toString());
        }
        return new DSGCPayloadSampleBean[]{reqSampleBean, resSampleBean};
    }

}
