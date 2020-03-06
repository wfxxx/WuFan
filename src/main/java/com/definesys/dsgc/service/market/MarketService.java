package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.apideploylog.ApiDeployLogDao;
import com.definesys.dsgc.service.apideploylog.bean.CommonReqBean;
import com.definesys.dsgc.service.apideploylog.bean.DagDeployStatBean;
import com.definesys.dsgc.service.apiroute.ApiRouteDao;
import com.definesys.dsgc.service.esbenv.DSGCBusCfgDao;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.market.bean.*;
import com.definesys.dsgc.service.market.bean.PayloadParamDTO;
import com.definesys.dsgc.service.market.bean.ServUriParamterDTO;
import com.definesys.dsgc.service.svcmng.SVCMngDao;
import com.definesys.dsgc.service.svcmng.bean.*;
import com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MarketService {
    @Autowired
    private MarketDao marketDao;

    @Autowired
    private DSGCBusCfgDao busCfgDao;

    @Autowired
    private SVCMngDao svcMngDao;

    @Autowired
    private ApiRouteDao apiRouteDao;

    @Autowired
    private ApiDeployLogDao apiDeployLogDao;

    public void addMarketCate(MarketCateVO marketcateVO) {
        marketDao.addMarketCate(marketcateVO);
    }

    public void deleteMarketCate(MarketCateVO marketcateVO) {
        marketDao.deleteMarketCate(marketcateVO);
    }

    public void updateMarketCate(MarketCateVO marketcateVO) {
        marketDao.updateMarketCate(marketcateVO);
    }

    public PageQueryResult<MarketCateVO> getAllMarketCate(MarketQueryBean q, int pageSize, int pageIndex) {
        return marketDao.getAllMarketCate(q,pageSize,pageIndex);
    }

    public PageQueryResult<MarketApiBean> getAllMarketPub(MarketQueryBean q, int pageSize, int pageIndex) {
        return marketDao.getAllMarketPub(q,pageSize, pageIndex);
    }

    public void updateMarketPub(MarketApiBean marketApiBean){
        marketDao.updateMarketPub(marketApiBean);
    }

    //条件获取Esb服务
    public PageQueryResult<MarketEntiy> getMarketEsb(Map<String,Object> mapValue, int pageSize, int pageIndex){
        if(mapValue.get("apply")!=null){
            String userId= (String) mapValue.get("apply");
            String[] servNoList=queryAppliedEsb(userId);
            mapValue.put("apply",servNoList);
        }
        return  this.marketDao.queryEsb(mapValue,pageSize,pageIndex);
    }
    //条件获取Api服务
    public PageQueryResult<MarketEntiy> getMarketApi(Map<String,Object> mapVlue, int pageSize, int pageIndex){
        if(mapVlue.get("apply")!=null){
            String userId= (String) mapVlue.get("apply");
            String[] servNoList=queryAppliedApi(userId);
            mapVlue.put("apply",servNoList);
        }
        return  this.marketDao.queryApi(mapVlue,pageSize,pageIndex);
    }


    public List<Map<String,Object>> getClassType(String sourceType){
        String parentType="API_CATE";
        switch(sourceType){
            case "apiSource":
                parentType="API_CATE";
                return this.marketDao.getApiClassType(parentType);
            case "servSource":
                parentType="SVC_CATE";
                return this.marketDao.getEsbClassType(parentType);
            default:
                return null;
        }
    }
    /**
     * 查询市场服务总数与用户申请总数，
     *  return
     * @author 薛云龙
     * @time 2020.2.10
     */
    public Map<String,Object> getPowerType(String sourceType,String userId){
        Map<String,Object> result=new HashMap<String,Object>();
        Object allTotal=0;
        Object applyTotal=0;
        switch(sourceType){
            case "apiSource":
                List<Map<String,Object>> apiTotal=this.marketDao.queryApiTotal();
                if(apiTotal.size()>0){
                    allTotal= (apiTotal.get(0).get("TOTAL"));
                }
                Map<String,Object> mapApi=new HashMap<String,Object>();
                mapApi.put("apply",userId);
                PageQueryResult<MarketEntiy> apiList= getMarketApi(mapApi,12,1);
                applyTotal=apiList.getCount()==null?0:apiList.getCount();
                break;
            case "servSource":
                List<Map<String,Object>> esbTotal=this.marketDao.queryEsbTotal();
                if(esbTotal.size()>0){
                    allTotal= (esbTotal.get(0).get("TOTAL"));
                }
                Map<String,Object> mapEsb=new HashMap<String,Object>();
                mapEsb.put("apply",userId);
                PageQueryResult<MarketEntiy> esbList= getMarketApi(mapEsb,12,1);
                applyTotal=esbList.getCount()==null?0:esbList.getCount();
                break;
            default:
        }
        result.put("allTotal",allTotal);
        result.put("applyTotal",applyTotal);
        return result;
    }

    /**
     * 查询市场用户申请的esb服务，返回servNo集合
     *  return
     * @author 薛云龙
     * @time 2020.2.11
     */
    private String[] queryAppliedEsb(String userId){
        List<Map<String,Object>>  servNoList=this.marketDao.queryAppliedEsb(userId);
        List<String> servNoResult=new ArrayList();
        for( Map<String,Object> value:servNoList){
            servNoResult.add((String) value.get("SERVNO"));
        }
        return  servNoResult.toArray(new String[servNoResult.size()]);
    }
    /**
     * 查询市场用户申请的api服务，返回servNo集合
     *  return
     * @author 薛云龙
     * @time 2020.2.10
     */
    private  String[] queryAppliedApi(String userId){
        List<Map<String,Object>>  servNoList=this.marketDao.queryAppliedApi(userId);
        List<String> servNoResult=new ArrayList();
        for( Map<String,Object> value:servNoList){
            servNoResult.add((String) value.get("SERVNO"));
        }
        return  servNoResult.toArray(new String[servNoResult.size()]);
    }

    /**
     * 集成服务详情查询api
     * @param servNo
     * @return
     */
    public SrevDetailInfoDTO queryServBaseInfo(String servNo){
        SrevDetailInfoDTO result = new SrevDetailInfoDTO();
        DSGCService dsgcService =svcMngDao.queryServByServNo(servNo);
        if (dsgcService == null){
            return null;
        }else {
            ServDetailBaseDTO servBaseDTO = new ServDetailBaseDTO();
            servBaseDTO.setServNo(dsgcService.getServNo());
            servBaseDTO.setServName(dsgcService.getServName());
            servBaseDTO.setServDesc(dsgcService.getServDesc());
            result.setServBase(servBaseDTO);
        }
        List<DSGCEnvInfoCfg> envInfoCfgs = marketDao.queryEsbEnv();
                //busCfgDao.queryEnvInfoCfgListPage();
        List<DSGCServicesUri> servicesUris = svcMngDao.queryServUri(servNo);
        StringBuilder restRequestSample = new StringBuilder("");
        StringBuilder soapRequestSample =  new StringBuilder("");
        List<ServLocation> locationList = new ArrayList<>();
        if (servicesUris != null && servicesUris.size()>0){
            Iterator<DSGCServicesUri> iterator = servicesUris.iterator();
            while (iterator.hasNext()){
                DSGCServicesUri dsgcServicesUri = iterator.next();
                if("REST".equals(dsgcServicesUri.getUriType())){
                    restRequestSample.append("http://ip:port/"+dsgcServicesUri.getIbUri());
                }
                if ("SOAP".equals(dsgcServicesUri.getUriType())){
                    soapRequestSample.append("http://ip:port/"+dsgcServicesUri.getIbUri());
                }
                if(envInfoCfgs != null && envInfoCfgs.size()>0){
                    Iterator<DSGCEnvInfoCfg> infoCfgIterator = envInfoCfgs.iterator();
                    while (infoCfgIterator.hasNext()){
                        DSGCEnvInfoCfg dsgcEnvInfoCfg = infoCfgIterator.next();
                        ServLocation servLocation = new ServLocation();
                        servLocation.setUrlType(dsgcServicesUri.getUriType());
                        servLocation.setEnvName(dsgcEnvInfoCfg.getEnvName());
                        String url ="http://"+ dsgcEnvInfoCfg.getEsbIp()+":"+dsgcEnvInfoCfg.getEsbPort()+"/"+dsgcServicesUri.getIbUri();
                        servLocation.setUrl(url);
                        locationList.add(servLocation);
                    }
                } else {
                    ServLocation servLocation = new ServLocation();
                    servLocation.setUrlType(dsgcServicesUri.getUriType());
                    servLocation.setUrl(dsgcServicesUri.getIbUri());
                    servLocation.setEnvName("");
                    locationList.add(servLocation);
                }
            }
            result.setLocationList(locationList);
        }
        List<DSGCUriParamsBean> paramsBeans = svcMngDao.queryServUriParamter(servNo);
        List<ServUriParamterDTO> paramterDTOS = new ArrayList<>();
        if (paramsBeans != null && paramsBeans.size()>0){
            Iterator<DSGCUriParamsBean> iterator = paramsBeans.iterator();
            while (iterator.hasNext()){
                DSGCUriParamsBean dsgcUriParamsBean = iterator.next();
                ServUriParamterDTO servUriParamterDTO = new ServUriParamterDTO();
                servUriParamterDTO.setParamCode(dsgcUriParamsBean.getParamCode());
                servUriParamterDTO.setParamSample(dsgcUriParamsBean.getParamSample());
                servUriParamterDTO.setParamDesc(dsgcUriParamsBean.getParamDesc());
                paramterDTOS.add(servUriParamterDTO);
            }
            String param = "";
            for (int i = 0; i < paramsBeans.size(); i++) {
                if (i == 0){
                  String  temp = "?"+paramsBeans.get(i).getParamCode()+"="+paramsBeans.get(i).getParamSample();
                  param += temp;
                }else {
                   String temp = "&"+paramsBeans.get(i).getParamCode()+"="+paramsBeans.get(i).getParamSample();
                   param += temp;
                }
            }
            restRequestSample.append(param);
            soapRequestSample.append(param);
        }
        CallSampleDTO restCallSample = new CallSampleDTO();
        restCallSample.setRequestSample(restRequestSample);
        CallSampleDTO soapCallSample = new CallSampleDTO();
        soapCallSample.setRequestSample(soapRequestSample);
        DSGCPayloadSampleBean sampleRestBean = new DSGCPayloadSampleBean();
        sampleRestBean.setResCode(servNo);
        sampleRestBean.setReqOrRes("REQ");
        sampleRestBean.setUriType("REST");
        DSGCPayloadSampleBean restReqSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleRestBean);
        sampleRestBean.setReqOrRes("RES");
        DSGCPayloadSampleBean restResSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleRestBean);
        if (restReqSample != null){
            restCallSample.setRequestMessage(restReqSample.getPlSample());
        }else {
            restCallSample.setRequestMessage("");
        }
        if (restResSample != null){
            restCallSample.setResponseMessage(restResSample.getPlSample());
        }else {
            restCallSample.setRequestMessage("");
        }
        DSGCPayloadSampleBean sampleSoapBean = new DSGCPayloadSampleBean();
        sampleSoapBean.setUriType("SOAP");
        sampleSoapBean.setResCode(servNo);
        sampleSoapBean.setReqOrRes("REQ");
        DSGCPayloadSampleBean soapReqSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleSoapBean);
        sampleSoapBean.setReqOrRes("RES");
        DSGCPayloadSampleBean soapResSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleSoapBean);
        if (soapReqSample != null){
            soapCallSample.setRequestMessage(soapReqSample.getPlSample());
        }else {
            soapCallSample.setRequestMessage("");
        }
        if (soapResSample != null){
            soapCallSample.setResponseMessage(soapResSample.getPlSample());
        }else {
            soapCallSample.setResponseMessage("");
        }

        result.setRestCallSample(restCallSample);
        result.setSoapCallSample(soapCallSample);
        result.setParamterList(paramterDTOS);

        SVCCommonReqBean param = new SVCCommonReqBean();
        param.setCon0(servNo);
        param.setQueryType("REQ");
        List<DSGCPayloadParamsBean> reqPayLoadParam = svcMngDao.queryServPayloadParam(param);
        param.setQueryType("RES");
        List<DSGCPayloadParamsBean> resPayLoadParam = svcMngDao.queryServPayloadParam(param);
        List<PayloadParamDTO> reqPayloadParamDTO = new ArrayList<>();
        List<PayloadParamDTO> resPayloadParamDTO = new ArrayList<>();
        if (reqPayLoadParam != null && reqPayLoadParam.size() > 0){
            Iterator<DSGCPayloadParamsBean> iterator = reqPayLoadParam.iterator();
            while (iterator.hasNext()){
                DSGCPayloadParamsBean temp = iterator.next();
                PayloadParamDTO payloadParamDTO = new PayloadParamDTO();
                payloadParamDTO.setParamCode(temp.getParamCode());
                payloadParamDTO.setParamDesc(temp.getParamDesc());
                payloadParamDTO.setParamNeed(temp.getParamNeed());
                payloadParamDTO.setParamSample(temp.getParamSample());
                payloadParamDTO.setParamType(temp.getParamType());
                reqPayloadParamDTO.add(payloadParamDTO);
            }
        }
        result.setReqPayloadParamList(reqPayloadParamDTO);

        if (resPayLoadParam != null && resPayLoadParam.size() > 0){
            Iterator<DSGCPayloadParamsBean> iterator = resPayLoadParam.iterator();
            while (iterator.hasNext()){
                DSGCPayloadParamsBean temp = iterator.next();
                PayloadParamDTO payloadParamDTO = new PayloadParamDTO();
                payloadParamDTO.setParamCode(temp.getParamCode());
                payloadParamDTO.setParamDesc(temp.getParamDesc());
                payloadParamDTO.setParamNeed(temp.getParamNeed());
                payloadParamDTO.setParamSample(temp.getParamSample());
                payloadParamDTO.setParamType(temp.getParamType());
                resPayloadParamDTO.add(payloadParamDTO);
            }
        }
        result.setResPayloadParamList(resPayloadParamDTO);
        return result;

    }

    public List<ServLocation> generateLocationList(List<DSGCServicesUri> servicesUris,List<DSGCEnvInfoCfg> envInfoCfgs){
        List<ServLocation> locationList = new ArrayList<>();
        Iterator<DSGCServicesUri> iterator = servicesUris.iterator();
        StringBuilder restRequestSample = new StringBuilder("");
        while (iterator.hasNext()){
            DSGCServicesUri dsgcServicesUri = iterator.next();
            restRequestSample.append("http://ip:port/"+dsgcServicesUri.getIbUri());
            if(envInfoCfgs != null && envInfoCfgs.size()>0){
                Iterator<DSGCEnvInfoCfg> infoCfgIterator = envInfoCfgs.iterator();
                while (infoCfgIterator.hasNext()){
                    DSGCEnvInfoCfg dsgcEnvInfoCfg = infoCfgIterator.next();
                    ServLocation servLocation = new ServLocation();
                    servLocation.setUrlType(dsgcServicesUri.getUriType());
                    servLocation.setEnvName(dsgcEnvInfoCfg.getEnvName());
                    servLocation.setHttpMethod(dsgcServicesUri.getHttpMethod());
                    String url ="http://"+ dsgcEnvInfoCfg.getEsbIp()+":"+dsgcEnvInfoCfg.getEsbPort()+"/"+dsgcServicesUri.getIbUri();
                    servLocation.setUrl(url);
                    locationList.add(servLocation);
                }
            } else {
                ServLocation servLocation = new ServLocation();
                servLocation.setUrlType(dsgcServicesUri.getUriType());
                servLocation.setUrl(dsgcServicesUri.getIbUri());
                servLocation.setEnvName("");
                locationList.add(servLocation);
            }
        }
        return locationList;
    }

    public CallSampleDTO generatePayloadSample(String apiCode){
        CallSampleDTO restCallSample = new CallSampleDTO();
        DSGCPayloadSampleBean sampleRestBean = new DSGCPayloadSampleBean();
        sampleRestBean.setResCode(apiCode);
        sampleRestBean.setReqOrRes("REQ");
        sampleRestBean.setUriType("REST");
        DSGCPayloadSampleBean restReqSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleRestBean);
        sampleRestBean.setReqOrRes("RES");
        DSGCPayloadSampleBean restResSample = svcMngDao.querySrvPaloadSoapOrRestSample(sampleRestBean);
        if (restReqSample != null){
            restCallSample.setRequestMessage(restReqSample.getPlSample());
        }else {
            restCallSample.setRequestMessage("");
        }
        if (restResSample != null){
            restCallSample.setResponseMessage(restResSample.getPlSample());
        }else {
            restCallSample.setResponseMessage("");
        }
        return restCallSample;
    }
    public StringBuilder generateRequestSample(List<DSGCServicesUri> servicesUris,List<DSGCUriParamsBean> paramsBeans){
        StringBuilder restRequestSample = new StringBuilder("");
        Iterator<DSGCServicesUri> iterator = servicesUris.iterator();
        while (iterator.hasNext()) {
            DSGCServicesUri dsgcServicesUri = iterator.next();
            restRequestSample.append("http://ip:port/"+dsgcServicesUri.getIbUri());
        }
        if (paramsBeans != null && paramsBeans.size()>0){
            String param = "";
            for (int i = 0; i < paramsBeans.size(); i++) {
                if (i == 0){
                    String  temp = "?"+paramsBeans.get(i).getParamCode()+"="+paramsBeans.get(i).getParamSample();
                    param += temp;
                }else {
                    String temp = "&"+paramsBeans.get(i).getParamCode()+"="+paramsBeans.get(i).getParamSample();
                    param += temp;
                }
            }
            restRequestSample.append(param);
        }

        return restRequestSample;
    }
    public List<ServUriParamterDTO> generateUriParam(List<DSGCUriParamsBean> paramsBeans ){
        Iterator<DSGCUriParamsBean> iterator = paramsBeans.iterator();
        List<ServUriParamterDTO> paramterDTOS = new ArrayList<>();
        while (iterator.hasNext()){
            DSGCUriParamsBean dsgcUriParamsBean = iterator.next();
            ServUriParamterDTO servUriParamterDTO = new ServUriParamterDTO();
            servUriParamterDTO.setParamCode(dsgcUriParamsBean.getParamCode());
            servUriParamterDTO.setParamSample(dsgcUriParamsBean.getParamSample());
            servUriParamterDTO.setParamDesc(dsgcUriParamsBean.getParamDesc());
            paramterDTOS.add(servUriParamterDTO);
        }
        return paramterDTOS;
    }

    public List<PayloadParamDTO> generatePayloadParam(List<DSGCPayloadParamsBean> payLoadParam){
            Iterator<DSGCPayloadParamsBean> iterator = payLoadParam.iterator();
        List<PayloadParamDTO> payloadParamList = new ArrayList<>();
            while (iterator.hasNext()){
                DSGCPayloadParamsBean temp = iterator.next();
                PayloadParamDTO payloadParamDTO = new PayloadParamDTO();
                payloadParamDTO.setParamCode(temp.getParamCode());
                payloadParamDTO.setParamDesc(temp.getParamDesc());
                payloadParamDTO.setParamNeed(temp.getParamNeed());
                payloadParamDTO.setParamSample(temp.getParamSample());
                payloadParamDTO.setParamType(temp.getParamType());
                payloadParamList.add(payloadParamDTO);
            }
        return payloadParamList;
    }
    public ApiDetailInfoDTO queryApiBaseInfo(String apiCode){
        DSGCApisBean apisBean = marketDao.queryApiByApiCode(apiCode);
        ApiDetailInfoDTO result = new ApiDetailInfoDTO();
        if (apisBean == null){
            return result;
        }else {
            ApiDetailBaseDTO apiDetailBaseDTO = new ApiDetailBaseDTO();
            apiDetailBaseDTO.setApiCode(apisBean.getApiCode());
            apiDetailBaseDTO.setApiName(apisBean.getApiName());
            apiDetailBaseDTO.setApiDesc(apisBean.getApiDesc());
            result.setApiBase(apiDetailBaseDTO);
        }
        List<DSGCEnvInfoCfg> envInfoCfgs =marketDao.queryApiEnv();
                //busCfgDao.queryEnvInfoCfgListPage();
        List<DSGCServicesUri> servicesUris = svcMngDao.queryServUri(apiCode);
        String routeCode = servicesUris.get(0).getProvider();
        if(StringUtil.isNotBlank(routeCode)) {
            CommonReqBean commonReqBean = new CommonReqBean();
            commonReqBean.setCon0(routeCode);
            commonReqBean.setQueryType("route");
            List<DagDeployStatBean> list = apiDeployLogDao.queryDeploySurvey(commonReqBean);
            if (list != null){
                List<DSGCEnvInfoCfg> dsgcEnvInfoCfgs = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < envInfoCfgs.size(); j++) {
                    if (list.get(i).getEnvCode().equals(envInfoCfgs.get(j).getEnvCode())) {
                        dsgcEnvInfoCfgs.add(envInfoCfgs.get(j));
                        break;
                    }
                }
            }
            List<ServLocation> locationList = generateLocationList(servicesUris, dsgcEnvInfoCfgs);
            result.setLocationList(locationList);
        }
        }
//        if (servicesUris != null && servicesUris.size()>0){
//            List<ServLocation> locationList = generateLocationList(servicesUris,envInfoCfgs);
//            result.setLocationList(locationList);
//        }
        List<DSGCUriParamsBean> paramsBeans = svcMngDao.queryServUriParamter(apiCode);
        if (paramsBeans != null && paramsBeans.size()>0){
            List<ServUriParamterDTO> uriParamterDTOS = generateUriParam(paramsBeans);
            result.setParamterList(uriParamterDTOS);
        }
        CallSampleDTO callSampleDTO =generatePayloadSample(apiCode);
        StringBuilder requestSample = generateRequestSample(servicesUris,paramsBeans);
        callSampleDTO.setRequestSample(requestSample);
        result.setRestCallSample(callSampleDTO);
        SVCCommonReqBean param = new SVCCommonReqBean();
        param.setCon0(apiCode);
        param.setQueryType("REQ");
        List<DSGCPayloadParamsBean> reqPayLoadParam = svcMngDao.queryServPayloadParam(param);
        param.setQueryType("RES");
        List<DSGCPayloadParamsBean> resPayLoadParam = svcMngDao.queryServPayloadParam(param);
        if (reqPayLoadParam != null && reqPayLoadParam.size() > 0){
            List<PayloadParamDTO> reqPayLoadParamList = generatePayloadParam(reqPayLoadParam);
            result.setReqPayloadParamList(reqPayLoadParamList);
        }
        if (resPayLoadParam != null && resPayLoadParam.size() > 0){
            List<PayloadParamDTO> resPayLoadParamList = generatePayloadParam(resPayLoadParam);
            result.setResPayloadParamList(resPayLoadParamList);
        }
        return result;
    }
}
