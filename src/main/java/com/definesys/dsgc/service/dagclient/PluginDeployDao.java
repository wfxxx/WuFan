package com.definesys.dsgc.service.dagclient;

import com.definesys.dsgc.service.dagclient.bean.DAGPluginUsingBean;
import com.definesys.dsgc.service.dagclient.proxy.bean.*;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PluginDeployDao {
    @Autowired
    private MpaasQueryFactory sw;


    public String getConsumerName(String csmCode) {
        if (csmCode != null && csmCode.trim().length() > 0) {
            Map<String,Object> res = sw.buildQuery().sql("select csm_name from dsgc_consumer_entities where csm_code = #csmCode").setVar("csmCode",csmCode).doQueryFirst();
            if (res != null) {
                Object csmNameObj = res.get("CSM_NAME");
                return csmNameObj == null ? csmCode : csmNameObj.toString();
            } else {
                return csmCode;
            }
        } else {
            return csmCode;
        }
    }


    public List<PluginSettingVO> loadPluginConfig(String vid) {

        List<PluginSettingVO> cfgList = new ArrayList<PluginSettingVO>();

        List<DAGPluginUsingBean> pu = this.sw.buildQuery().eq("vid",vid).doQuery(DAGPluginUsingBean.class);

        if (pu != null && pu.size() > 0) {
            Iterator<DAGPluginUsingBean> iter = pu.iterator();
            while (iter.hasNext()) {
                DAGPluginUsingBean puCfg = iter.next();
                if (puCfg != null) {
                    PluginSettingVO pCfg = this.getPluginConfigDtl(puCfg);
                    if(pCfg != null && pCfg.getConfig() != null) {
                        cfgList.add(pCfg);
                    }
                }
            }
        }
        return cfgList;
    }


    private PluginSettingVO getPluginConfigDtl(DAGPluginUsingBean pluginUsing) {
        PluginSettingVO ps = new PluginSettingVO();
        if ("Y".equals(pluginUsing.getIsEnable())) {
            ps.setEnabled(true);
        } else {
            ps.setEnabled(false);
        }
        ps.setCsmCode(pluginUsing.getConsumer());
        ps.setPluginCode(pluginUsing.getPluginCode());

        if ("basic-auth".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getBasicAuthConfig(pluginUsing.getDpuId()));

        } else if ("key-auth".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getKeyAuthConfig(pluginUsing.getDpuId()));

        } else if ("oauth2".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getOauth2Config(pluginUsing.getDpuId()));

        } else if ("acl".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getACLConfig(pluginUsing.getDpuId()));

        } else if ("ip-restriction".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getIpRestrictionConfig(pluginUsing.getDpuId()));

        } else if ("rate-limiting".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getRateLimitingConfig(pluginUsing.getDpuId()));

        } else if ("request-size-limiting".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getReqSizeLimitingConfig(pluginUsing.getDpuId()));

        } else if ("request-transformer".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getReqTransConfig(pluginUsing.getDpuId()));

        } else if ("response-transformer".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getResTransConfig(pluginUsing.getDpuId()));

        } else if ("correlation-id".equals(pluginUsing.getPluginCode())) {

            ps.setConfig(this.getCorrelationIdConfig(pluginUsing.getDpuId()));

        }

        return ps;
    }


    public BasicAuthPluginCfgVO getBasicAuthConfig(String dpuId) {
        BasicAuthPluginCfgVO ba = new BasicAuthPluginCfgVO();
        ba.setHide_credentials(true);
        return ba;
    }

    public KeyAuthPluginCfgVO getKeyAuthConfig(String dpuId) {
        return null;
    }

    public ACLPluginCfgVO getACLConfig(String dpuId) {
        ACLPluginCfgVO acl = new ACLPluginCfgVO();
        acl.setBlacklist(new String[]{"defalut"});
        return acl;
    }

    public IpRestPluginCfgVO getIpRestrictionConfig(String dpuId) {
        return null;
    }

    public ResTransPluginCfgVO getResTransConfig(String dpuId) {
        return null;
    }


    public ReqTransPluginCfgVO getReqTransConfig(String dpuId) {
        return null;
    }


    public RateLimitPluginCfgVO getRateLimitingConfig(String dpuId) {
        if(dpuId != null) {
            RateLimitPluginCfgVO rlc = sw.buildQuery().sql("select P_SECOND,P_MINUTE,P_HOUR,P_DAY,P_MONTH,P_YEAR from  PLUGIN_RATE_LIMITING where DPU_ID = #dpuID").setVar("dpuID",dpuId).doQueryFirst(RateLimitPluginCfgVO.class);
            if (rlc != null) {
                rlc.setSecond(rlc.getSecond() != null && rlc.getSecond().intValue() == 0 ? null : rlc.getSecond());
                rlc.setMinute(rlc.getMinute() != null && rlc.getMinute().intValue() == 0 ? null : rlc.getMinute());
                rlc.setHour(rlc.getHour() != null && rlc.getHour().intValue() == 0 ? null : rlc.getHour());
                rlc.setDay(rlc.getDay() != null && rlc.getDay().intValue() == 0 ? null : rlc.getDay());
                rlc.setMonth(rlc.getMonth() != null && rlc.getMonth().intValue() == 0 ? null : rlc.getMonth());
                rlc.setYear(rlc.getYear() != null && rlc.getYear().intValue() == 0 ? null : rlc.getYear());
            }
            return rlc;
        }
        return null;
    }


    public Oauth2PluginCfgVO getOauth2Config(String dpuId) {
        return null;
    }


    public CorrIdPluginCfgVO getCorrelationIdConfig(String dpuId) {
        return null;
    }


    public ReqSizePluginCfgVO getReqSizeLimitingConfig(String dpuId) {
        return null;
    }


}
