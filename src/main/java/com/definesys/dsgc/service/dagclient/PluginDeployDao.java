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
        Map<String,Object> ipCfgMap = sw.buildQuery().sql("select WHITE_LIST,BLACK_LIST from PLUGIN_IP_RESTRICTION where DPU_ID = #dpuId").setVar("dpuId",dpuId).doQueryFirst();
        if(ipCfgMap != null){
           String whiteList = ipCfgMap.get("WHITE_LIST") != null ? ipCfgMap.get("WHITE_LIST").toString() : null;
           String blackList = ipCfgMap.get("BLACK_LIST") != null ? ipCfgMap.get("BLACK_LIST").toString() : null;

           if(whiteList != null && whiteList.trim().length() >0 || blackList != null && blackList.trim().length() >0 ){
               IpRestPluginCfgVO ipr = new IpRestPluginCfgVO();
               if(whiteList != null && whiteList.trim().length() >0){
                   String[] whiteArray = whiteList.trim().split(",");
                   List<String> wList = new ArrayList<String>();
                   for(String w: whiteArray){
                       if(w != null && w.trim().length() >0){
                           wList.add(w.trim());
                       }
                   }
                   if(wList.size() > 0) {
                       ipr.setWhitelist(wList);
                   }
               }

               if(blackList != null && blackList.trim().length() >0){
                   String[] blackArray = blackList.trim().split(",");
                   List<String> bList = new ArrayList<String>();
                   for(String b : blackArray){
                       if(b != null && b.trim().length() >0){
                           bList.add(b.trim());
                       }
                   }
                   if(bList.size() > 0){
                       ipr.setBlacklist(bList);
                   }
               }

               return ipr;
           }

        }
        return null;
    }

    public ResTransPluginCfgVO getResTransConfig(String dpuId) {
        return null;
    }


    public ReqTransPluginCfgVO getReqTransConfig(String dpuId) {
        if(dpuId != null){
            ReqTransPluginCfgBean res =  sw.buildQuery().sql("select HTTP_METHOD,\n" +
                    "       REMOVE_BODY,\n" +
                    "       REMOVE_HEADERS,\n" +
                    "       REMOVE_QUERYSTRING,\n" +
                    "       RENAME_BODY,\n" +
                    "       RENAME_HEADERS,\n" +
                    "       RENAME_QUERYSTRING,\n" +
                    "       REPLACE_BODY,\n" +
                    "       REPLACE_HEADERS,\n" +
                    "       REPALCE_QUERYSTRING,\n" +
                    "       REPLACE_URL,\n" +
                    "       ADD_BODY,\n" +
                    "       ADD_HEADERS,\n" +
                    "       ADD_QUERYSTRING,\n" +
                    "       APPEND_BODY,\n" +
                    "       APPEND_HEADERS,\n" +
                    "       APPEND_QUERYSTRING\n" +
                    "from PLUGIN_REQ_TRANS\n" +
                    "where DPU_ID = #dpuId\n").setVar("dpuId",dpuId).doQueryFirst(ReqTransPluginCfgBean.class);
            if(res != null){
                boolean hasValue = false;
                ReqTransPluginCfgVO reqTransCfg = new ReqTransPluginCfgVO();
                if(res.getHttpMethod() != null && res.getHttpMethod().trim().length() > 0){
                    hasValue = true;
                    reqTransCfg.setHttp_method(res.getHttpMethod().trim());
                }


                TransCommonVO remove= new TransCommonVO();
                remove.setBody(this.covertToListBySplit(res.getRemoveBody()));
                remove.setHeaders(this.covertToListBySplit(res.getRemoveHeaders()));
                remove.setQuerystring(this.covertToListBySplit(res.getRemoveQuerystring()));

                if(remove.getBody() != null && remove.getBody().size() >0
                    || remove.getHeaders() != null &&  remove.getHeaders().size() >0
                    || remove.getQuerystring() != null && remove.getQuerystring().size() >0){
                    hasValue = true;
                    reqTransCfg.setRemove(remove);
                }

                TransCommonVO rename = new TransCommonVO();
                rename.setBody(this.covertToListBySplit(res.getRenameBody()));
                rename.setHeaders(this.covertToListBySplit(res.getRenameHeaders()));
                rename.setQuerystring(this.covertToListBySplit(res.getRenameQuerystring()));

                if(rename.getBody() != null && rename.getBody().size() >0
                        || rename.getHeaders() != null &&  rename.getHeaders().size() >0
                        || rename.getQuerystring() != null && rename.getQuerystring().size() >0){
                    hasValue = true;
                    reqTransCfg.setRename(rename);
                }

                TransCommonVO add= new TransCommonVO();
                add.setBody(this.covertToListBySplit(res.getAddBody()));
                add.setHeaders(this.covertToListBySplit(res.getAddHeaders()));
                add.setQuerystring(this.covertToListBySplit(res.getAddQuerystring()));

                if(add.getBody() != null && add.getBody().size() >0
                        || add.getHeaders() != null &&  add.getHeaders().size() >0
                        || add.getQuerystring() != null && add.getQuerystring().size() >0){
                    hasValue = true;
                    reqTransCfg.setAdd(add);
                }

                TransCommonVO append= new TransCommonVO();
                append.setBody(this.covertToListBySplit(res.getAppendBody()));
                append.setHeaders(this.covertToListBySplit(res.getAppendHeaders()));
                append.setQuerystring(this.covertToListBySplit(res.getAppendQuerystring()));

                if(append.getBody() != null && append.getBody().size() >0
                        || append.getHeaders() != null &&  append.getHeaders().size() >0
                        || append.getQuerystring() != null && append.getQuerystring().size() >0){
                    hasValue = true;
                    reqTransCfg.setAppend(append);
                }


                TransReplaceVO replace= new TransReplaceVO();
                replace.setBody(this.covertToListBySplit(res.getReplaceBody()));
                replace.setHeaders(this.covertToListBySplit(res.getReplaceHeaders()));
                replace.setQuerystring(this.covertToListBySplit(res.getReplaceQuerystring()));
                if(res.getReplaceUrl() != null) {
                    replace.setUri(res.getReplaceUrl().trim());
                }
                if(replace.getBody() != null && replace.getBody().size() >0
                        || replace.getHeaders() != null &&  replace.getHeaders().size() >0
                        || replace.getQuerystring() != null && replace.getQuerystring().size() >0
                        || replace.getUri() != null && replace.getUri().length() >0){
                    hasValue = true;
                    reqTransCfg.setReplace(replace);
                }

                return hasValue ? reqTransCfg :null;
            }
        }
        return null;
    }

    private List<String> covertToListBySplit(String str){
        List<String> res = new ArrayList<String>();
        if(str != null ){
            String[]  arr = str.split(",");
            for(String a:arr){
                if(a != null && a.trim().length() >0){
                    res.add(a.trim());
                }
            }
        }

        if(res.size() > 0){
            return res;
        } else{
            return null;
        }
    }


    public RateLimitPluginCfgVO getRateLimitingConfig(String dpuId) {
        if(dpuId != null) {
            RateLimitPluginCfgVO rlc = sw.buildQuery().sql("select P_SECOND,P_MINUTE,P_HOUR,P_DAY,P_MONTH,P_YEAR from  PLUGIN_RATE_LIMITING where DPU_ID = #dpuId").setVar("dpuId",dpuId).doQueryFirst(RateLimitPluginCfgVO.class);
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
        CorrIdPluginCfgVO cic = new CorrIdPluginCfgVO();

        if(dpuId != null){
            Map<String,Object> res = sw.buildQuery().sql("select HEADER_NAME,GENERATOR,ECHO_DOWNSTREAM from PLUGIN_CORRELATION_ID where DPU_ID = #dpuId").setVar("dpuId",dpuId).doQueryFirst();
            if(res != null){
                if(res.get("HEADER_NAME") != null) {
                    cic.setHeader_name(res.get("HEADER_NAME").toString());
                }

                if(res.get("GENERATOR") != null){
                    cic.setGenerator(res.get("GENERATOR").toString());
                }

                if(res.get("ECHO_DOWNSTREAM") != null){
                    if("Y".equals(res.get("ECHO_DOWNSTREAM").toString())){
                        cic.setEcho_downstream(true);
                    } else{
                        cic.setEcho_downstream(false);
                    }
                }
            }
        }
        return cic;
    }


    public ReqSizePluginCfgVO getReqSizeLimitingConfig(String dpuId) {

        ReqSizePluginCfgVO rspc = new ReqSizePluginCfgVO();

        if(dpuId != null) {
            Map<String,Object> res = sw.buildQuery().sql("select ALLOWED_PAYLOAD_SIZE,SIZE_UNIT from PLUGIN_REQ_SIZE_LIMITING where DPU_ID = #dpuId").setVar("dpuId",dpuId).doQueryFirst();
            if(res != null){
                if(res.get("SIZE_UNIT") != null){
                    rspc.setSize_unit(res.get("SIZE_UNIT").toString());
                }
                if(res.get("ALLOWED_PAYLOAD_SIZE") != null){
                    rspc.setAllowed_payload_size(Long.valueOf(res.get("ALLOWED_PAYLOAD_SIZE").toString()));
                }
            }
        }
        return rspc;
    }


}
