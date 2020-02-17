package com.definesys.dsgc.service.svcmng;



import com.definesys.dsgc.service.bpm.BpmDao;
import com.definesys.dsgc.service.bpm.BpmService;
import com.definesys.dsgc.service.bpm.bean.BpmCommonReqBean;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceBean;
import com.definesys.dsgc.service.bpm.bean.BpmInstanceDTO;
import com.definesys.dsgc.service.svcAuth.SVCAuthDao;
import com.definesys.dsgc.service.svcAuth.SVCAuthService;
import com.definesys.dsgc.service.svcgen.bean.WSDLResolveBean;
import com.definesys.dsgc.service.svcmng.utils.WsdlResolvProxy;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.svcmng.bean.*;

import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.dsgc.service.esbenv.bean.DSGCEnvInfoCfg;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.esbenv.DSGCBusCfgDao;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.db.PageQueryResult;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

@Service
public class SVCMngService {
    @Autowired
    private SVCMngDao svcMngDao;

    @Autowired
    private SVCAuthDao svcAuthDao;

    @Autowired
    private SVCLogDao sldao;

    @Autowired
    private BpmDao bpmdao;

    @Autowired
    private BpmService bpmService;

    @Autowired
    private SVCAuthService svcAuthService;



    @Autowired
    private DSGCBusCfgDao dsgcBusCfgDao;
    public PageQueryResult<SVCMngInfoListBean> querySvcMngServList(SVCCommonReqBean param,int pageIndex,int pageSize,String userId,String userRole){
        if ("Tourist".equals(userRole)){
            return new PageQueryResult<>();
        }else {

            List<FndLookupValue> fndLookupValueList =  svcAuthDao.queryFndModuleByLookupType("SVC_SHARE_TYPE");
            List<FndLookupValue> servIsCompleteList = svcAuthDao.queryFndModuleByLookupType("SERV_IS_COMPLETE");
            String servIsOnline = "";
            if (param.getCon0() != null && param.getCon0().trim().length() > 0) {
                String[] conArray = param.getCon0().trim().split(" ");
                Boolean temp = true;
                List<String> list1= Arrays.asList(conArray);
                List<String> arrList = new ArrayList<String>(list1);
                for (FndLookupValue item:servIsCompleteList) {
                    for (String s : conArray) {
                        if (temp && s.equals(item.getLookupCode())) {
                            servIsOnline += item.getMeaning();
                            arrList.remove(item.getLookupCode());
                            temp = false;
                        }

                    }
                    String string1 = String.join(" ",arrList);
                    param.setCon0(string1);
//                    if (param.getCon0().equals(item.getLookupCode())){
//                        servIsOnline += item.getMeaning();
//                    }
                }

            }


            List<String> sysCodeList = new ArrayList<>();
            if ("SystemLeader".equals(userRole)){
                List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
                Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
                while (iter.hasNext()) {
                    DSGCSystemUser s = (DSGCSystemUser) iter.next();
                    sysCodeList.add(s.getSysCode());
                }
            }
            PageQueryResult<DSGCService> pageQueryResult = svcMngDao.querySvcMngServList(param,pageIndex,pageSize,userRole,sysCodeList,servIsOnline);
            PageQueryResult<SVCMngInfoListBean> result = new PageQueryResult<>();
            result.setCount(pageQueryResult.getCount());
            List<SVCMngInfoListBean> list = new ArrayList<>();
            for (DSGCService item:pageQueryResult.getResult()) {
                SVCMngInfoListBean temp =svcMngBeanMapping(item);

                for (int  i =0;i<fndLookupValueList.size();i++){
                    if (temp.getShareTypeDesc() != null && temp.getShareTypeDesc().equals(fndLookupValueList.get(i).getLookupCode())){
                        temp.setShareTypeDesc(fndLookupValueList.get(i).getMeaning());
                        break;
                    }
                }
                list.add(temp);
            }
            result.setResult(list);
            return result;
        }
    }

    public SVCMngInfoListBean svcMngBeanMapping(DSGCService dsgcService){
        SVCMngInfoListBean svcMngInfoListBean = new SVCMngInfoListBean();
        svcMngInfoListBean.setServName(dsgcService.getServName());
        svcMngInfoListBean.setServNo(dsgcService.getServNo());
        svcMngInfoListBean.setSubSystem(dsgcService.getAttribue1());
        svcMngInfoListBean.setShareTypeDesc(dsgcService.getShareType());
        svcMngInfoListBean.setInfoFull(dsgcService.getInfoFull());
        svcMngInfoListBean.setIsProd(dsgcService.getIsProd());
      //  svcMngInfoListBean.setCurBpmNode(dsgcService.getAttribue2());
        if(dsgcService.getInstanceId() != null && !"".equals(dsgcService.getInstanceId())){
            BpmCommonReqBean bpmCommonReqBean = new BpmCommonReqBean();
            bpmCommonReqBean.setCon0(dsgcService.getInstanceId());
            BpmInstanceBean bpmInstanceBean = bpmdao.queryBpmInstanceBaseInfo(bpmCommonReqBean);
            BpmInstanceDTO bpmInstanceDTO =bpmService.instanceBeanMapping(bpmInstanceBean);
            svcMngInfoListBean.setCurBpmNode(bpmInstanceDTO.getCurNodeName());
        }
        if(dsgcService.getDeployedNode() != null){
            List<DSGCEnvInfoCfg>  dsgcEnvInfoCfgs =  dsgcBusCfgDao.queryEnvInfoCfgByEnvSeq(Integer.parseInt(dsgcService.getDeployedNode()));
            svcMngInfoListBean.setDeployedNode(dsgcEnvInfoCfgs.get(0).getEnvCode());
            svcMngInfoListBean.setDeployedNodeName(dsgcEnvInfoCfgs.get(0).getEnvName());
        }
        return svcMngInfoListBean;
    }
    public Map<String, Object> generateMsgExample(SVCMngGenerateMsgVO param){
        Map<String,Object> result = new HashMap<>();
        String msg = "{\n" +
                "  \"class\": {\n" +
                "    \"name\": \"提高班\",\n" +
                "    \"no\": \"1801\",\n" +
                "    \"students\": {\n" +
                "      \"student\": [\n" +
                "        {\n" +
                "          \"name\": \"宋海娇\",\n" +
                "          \"sex\": \"女\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"毛伟伟\",\n" +
                "          \"sex\": \"男\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        result.put("msg",msg);
        List<SVCMngIoParameterDTO> msgParameterList = new ArrayList<>();
        List<SVCMngIoParameterDTO> parameterList = param.getParameterList();
        List<SVCMngIoParameterDTO> handleResult =  paramterHandle(msgParameterList,parameterList);
        List<SVCMngIoParameterDTO> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            SVCMngIoParameterDTO svcMngIoParameterDTO = new SVCMngIoParameterDTO();
            svcMngIoParameterDTO.setNodeDesc("testdddddddddd");
            svcMngIoParameterDTO.setNodeName("xxxx");
            svcMngIoParameterDTO.setNodeType("generate");
            svcMngIoParameterDTO.setNodeValue("testtest");
            svcMngIoParameterDTO.setRequired(false);
            svcMngIoParameterDTO.setDataType("String");
            handleResult.add(svcMngIoParameterDTO);
        }
        result.put("parameters",handleResult);
        result.put("type","json");
        return result;
    }
    public List<SVCMngIoParameterDTO> generateMsgParameter(SVCMngGenerateMsgVO param) throws Exception{

        List<SVCMngIoParameterDTO> msgParameterList = new ArrayList<>();
        if(StringUtil.isNotBlank(param.getMsg())){
            msgParameterList = msgAnalysis(param.getMsg());
        }
        List<SVCMngIoParameterDTO> parameterList = param.getParameterList();
        return paramterHandle(msgParameterList,parameterList);
    }

    public List<SVCMngIoParameterDTO> paramterHandle(List<SVCMngIoParameterDTO> msgParameterList,List<SVCMngIoParameterDTO> parameterList){
        List<SVCMngIoParameterDTO>  customParameterList = new ArrayList<>();
        Iterator<SVCMngIoParameterDTO> iterator = parameterList.iterator();
        while (iterator.hasNext()) {
            SVCMngIoParameterDTO s = iterator.next();
            if(msgParameterList.size()>0){
                for (int j = 0; j < msgParameterList.size(); j++) {
                    if("generate".equals(s.getNodeType())
                            || (s.getNodeName().equals(msgParameterList.get(j).getNodeName())
                            && "".equals(s.getNodeDesc()))) {
                        iterator.remove();
                        break;
                    }
                }
            }else {
                if("generate".equals(s.getNodeType())){
                    iterator.remove();
                }
            }

        }
        parameterList.addAll(msgParameterList);
        return parameterList;
    }
    public List<SVCMngIoParameterDTO> msgAnalysis(String msg) throws Exception {
        String type = getType(msg);
        List<Map<String,String>> result = new ArrayList<>();
        if("Json".equals(type)) {
            result =  resloveJson(msg);
        } else if ("xml".equals(type)){
            result = resloveXML(msg);
        }else{
            throw new Exception("报文数据错误，解析失败");
        }

        List<SVCMngIoParameterDTO> list = new ArrayList<>();
        Iterator<Map<String,String>> iterator = result.iterator();
        while (iterator.hasNext()){
            Map<String,String> tempMap = iterator.next();
            SVCMngIoParameterDTO svcMngIoParameterDTO = new SVCMngIoParameterDTO();
            svcMngIoParameterDTO.setNodeName(tempMap.get("name"));
            svcMngIoParameterDTO.setNodeValue(tempMap.get("value"));
            svcMngIoParameterDTO.setNodeType("generate");
            svcMngIoParameterDTO.setRequired(false);
            svcMngIoParameterDTO.setDataType("String");
            svcMngIoParameterDTO.setNodeDesc("");
            list.add(svcMngIoParameterDTO);
        }
        return list;

    }
    public  String getType(String string) {
        if (isJson(string))
            return "Json";
        else if (isXML(string))
            return "xml";
        else
            return "String";
    }

    public  boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    public  boolean isXML(String value) {
        try {
            DocumentHelper.parseText(value);
        } catch (DocumentException e) {
            return false;
        }
        return true;
    }

    public Map<String ,Object> initSvcMngIoParameterData(String servNo){
        Map<String,Object> result = new HashMap<>();
        String msg = "{\n" +
                "  \"class\": {\n" +
                "    \"name\": \"提高班\",\n" +
                "    \"no\": \"1801\",\n" +
                "    \"students\": {\n" +
                "      \"student\": [\n" +
                "        {\n" +
                "          \"name\": \"宋海娇\",\n" +
                "          \"sex\": \"女\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"毛伟伟\",\n" +
                "          \"sex\": \"男\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        result.put("msg",msg);
        List<SVCMngIoParameterDTO> list = new ArrayList<>();
        for (int i = 0;i<10;i++){
            SVCMngIoParameterDTO svcMngIoParameterDTO = new SVCMngIoParameterDTO();
            svcMngIoParameterDTO.setNodeDesc("testdddddddddd");
            svcMngIoParameterDTO.setNodeName("xxxx");
            svcMngIoParameterDTO.setNodeType("generate");
            svcMngIoParameterDTO.setNodeValue("testtest");
            svcMngIoParameterDTO.setRequired(false);
            svcMngIoParameterDTO.setDataType("String");
            list.add(svcMngIoParameterDTO);
        }
        result.put("parameters",list);
        result.put("type","json");
        return result;
    }

    public SVCServBasicInfoDTO queryBasicInfoByServNo(String servNo){
       DSGCService dsgcService = svcMngDao.queryBasicInfoByServNo(servNo);
        SVCServBasicInfoDTO dto = new SVCServBasicInfoDTO();
        dto.setServNo(dsgcService.getServNo());
        dto.setServName(dsgcService.getServName());
        dto.setShareType(dsgcService.getShareType());
        dto.setServDesc(dsgcService.getServDesc());
        dto.setSubSystem(dsgcService.getSubordinateSystem());
        return dto;
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveServBasicInfo(SVCServBasicInfoDTO svcServBasicInfo){
        DSGCService dsgcService = new DSGCService();
        dsgcService.setSubordinateSystem(svcServBasicInfo.getSubSystem());
        dsgcService.setServName(svcServBasicInfo.getServName());
        dsgcService.setServDesc(svcServBasicInfo.getServDesc());
        dsgcService.setShareType(svcServBasicInfo.getShareType());
        dsgcService.setServNo(svcServBasicInfo.getServNo());
        svcMngDao.saveServBasicInfo(dsgcService);
        completionThread(dsgcService.getServNo());
    }

    public List<ServUriDTO> queryServUri(String servNo){
       List<DSGCServicesUri> uris = svcMngDao.queryServUri(servNo);
        List<ServUriDTO> result = new ArrayList<>();
       Iterator<DSGCServicesUri> iterator = uris.iterator();
       while (iterator.hasNext()){
           DSGCServicesUri temp = iterator.next();
           ServUriDTO servUriDTO = new ServUriDTO();
           servUriDTO.setIbUri(temp.getIbUri());
           servUriDTO.setUriType(temp.getUriType());
           result.add(servUriDTO);
       }
       return result;
    }
    public List<ServUriParamterDTO> queryServUriParamter(String resCode){
       List<DSGCUriParamsBean> dsgcUriParamsBeans = svcMngDao.queryServUriParamter(resCode);
       List<ServUriParamterDTO> result = new ArrayList<>();
       Iterator<DSGCUriParamsBean> iterator = dsgcUriParamsBeans.iterator();
       while (iterator.hasNext()){
           DSGCUriParamsBean temp = iterator.next();
           ServUriParamterDTO dto = new ServUriParamterDTO();
           dto.setParamCode(temp.getParamCode());
           dto.setParamDesc(temp.getParamDesc());
           dto.setParamSample(temp.getParamSample());
           result.add(dto);
       }
       return result;
    }

    public Map<String,List<ServUriParamterDTO>> queryUriParamTemplateList(){
        List<DSGCUriParamsTmplBean> list =  svcMngDao.queryUriParamTemplateList();
        Map<String,List<ServUriParamterDTO>> resultMap = new HashMap<String,List<ServUriParamterDTO>>();
        Iterator<DSGCUriParamsTmplBean> iterator = list.iterator();
        while (iterator.hasNext()){
            DSGCUriParamsTmplBean temp = iterator.next();
            List<DSGCUriParamsTmplBean> tmplBeanList =svcMngDao.queryUriParamTemplateByTmplName(temp.getTmplName());
            List<ServUriParamterDTO> resutList = new ArrayList<>();
            for (DSGCUriParamsTmplBean item:tmplBeanList) {
                ServUriParamterDTO servUriParamterDTO = new ServUriParamterDTO();
                servUriParamterDTO.setParamSample(item.getParamSample());
                servUriParamterDTO.setParamDesc(item.getParamDesc());
                servUriParamterDTO.setParamCode(item.getParamCode());
                resutList.add(servUriParamterDTO);
            }
            resultMap.put(temp.getTmplName(),resutList);
        }
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAsTemplate(SaveAsTemplateVO vo){
        List<DSGCUriParamsTmplBean> list = new ArrayList<>();
        Iterator<ServUriParamterDTO> iterator = vo.getParamterList().iterator();
        while (iterator.hasNext()){
            ServUriParamterDTO servUriParamterDTO = iterator.next();
            DSGCUriParamsTmplBean dsgcUriParamsTmplBean = new DSGCUriParamsTmplBean();
            dsgcUriParamsTmplBean.setParamCode(servUriParamterDTO.getParamCode());
            dsgcUriParamsTmplBean.setParamDesc(servUriParamterDTO.getParamDesc());
            dsgcUriParamsTmplBean.setParamSample(servUriParamterDTO.getParamSample());
            dsgcUriParamsTmplBean.setTmplName(vo.getTmplName());
            list.add(dsgcUriParamsTmplBean);
        }
        svcMngDao.saveAsTemplate(list);
    }
    @Transactional(rollbackFor = Exception.class)
    public int saveServLocationData(SaveServLocationDataVO vo){
        if(vo.getUriList() == null || vo.getUriList().size() == 0){
            return -1;
        }
        List<DSGCServicesUri> uris = svcMngDao.queryServUri(vo.getServNo());
        List<DSGCUriParamsBean> params = svcMngDao.queryServUriParamter(vo.getServNo());
        if(uris != null && uris.size() > 0){
            svcMngDao.delServUri(vo.getServNo());
        }
        if(params != null && params.size() > 0){
            svcMngDao.delServUriParamter(vo.getServNo());
        }
        List<DSGCServicesUri> servicesUriList = new ArrayList<>();
        Iterator<ServUriDTO> servUriDTOIterator = vo.getUriList().iterator();
        while (servUriDTOIterator.hasNext()){
            ServUriDTO temp = servUriDTOIterator.next();
            DSGCServicesUri dsgcServicesUri = new DSGCServicesUri();
            dsgcServicesUri.setIbUri(temp.getIbUri());
            dsgcServicesUri.setUriType(temp.getUriType());
            dsgcServicesUri.setServNo(vo.getServNo());
            svcMngDao.addServUri(dsgcServicesUri);
          //  servicesUriList.add(dsgcServicesUri);
        }
        if(vo.getParamterList() != null && vo.getUriList().size() > 0){
            List<DSGCUriParamsBean> list = new ArrayList<>();
            Iterator<ServUriParamterDTO>  paramterDTOIterator = vo.getParamterList().iterator();
            while (paramterDTOIterator.hasNext()){
                ServUriParamterDTO temp = paramterDTOIterator.next();
                DSGCUriParamsBean dsgcUriParamsBean = new DSGCUriParamsBean();
                dsgcUriParamsBean.setResCode(vo.getServNo());
                dsgcUriParamsBean.setParamCode(temp.getParamCode());
                dsgcUriParamsBean.setParamSample(temp.getParamSample());
                dsgcUriParamsBean.setParamDesc(temp.getParamDesc());
                dsgcUriParamsBean.setUriType("REST");
                svcMngDao.addServUriParamter(dsgcUriParamsBean);
               // list.add(dsgcUriParamsBean);
            }

        }
        completionThread(vo.getServNo());
        return 1;
    }

    public void completionThread(String servNo){
        Runnable myRunnable = new Runnable(){
            public void run(){
                updateServDataCompletion(servNo);
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateServDataCompletion(String servNo){
        int completion = 0;
        DSGCService dsgcService = svcMngDao.queryServByServNo(servNo);
        if(StringUtil.isNotBlank(dsgcService.getServNo())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcService.getServName())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcService.getSubordinateSystem())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcService.getShareType())){
            completion +=10;
        }
        if(StringUtil.isNotBlank(dsgcService.getServDesc())){
            completion +=10;
        }
        List<DSGCServicesUri> uris = svcMngDao.queryServUri(servNo);
        if(uris != null && uris.size() > 0){
            List<String> uriTypeList = new ArrayList<>();
            Iterator<DSGCServicesUri> iterator = uris.iterator();
            while (iterator.hasNext()){
                DSGCServicesUri dsgcServicesUri = iterator.next();
                uriTypeList.add(dsgcServicesUri.getUriType());
            }
            if (uriTypeList.contains("REST")){
                completion +=10;
            }
            if (uriTypeList.contains("SOAP")){
                completion +=10;
            }
        }
        SVCCommonReqBean param = new SVCCommonReqBean();
        param.setCon0(servNo);
        param.setQueryType("REQ");
        List<DSGCPayloadSampleBean> reqSampleBeans = svcMngDao.querySrvPaloadSample(param);
        List<DSGCPayloadParamsBean> reqSampleParamBeans = svcMngDao.queryServPayloadParam(param);
        param.setQueryType("RES");
        List<DSGCPayloadSampleBean> resSampleBeans = svcMngDao.querySrvPaloadSample(param);
        List<DSGCPayloadParamsBean> resSampleParamBeans = svcMngDao.queryServPayloadParam(param);
        if(reqSampleBeans.size()>0){
            completion += 10;
        }
        if(reqSampleParamBeans.size()>0){
            completion +=5;

        }
        if (resSampleBeans.size() >0){
            completion += 10;
        }
        if(resSampleParamBeans.size()>0){
            completion +=5;

        }

        DSGCService service = new DSGCService();
        service.setInfoFull(completion);
        service.setServNo(servNo);
        svcMngDao.updateServDataCompletion(service);
    }

    public ReqParamBaseDataDTO queryParamBaseData(SVCCommonReqBean param){
        ReqParamBaseDataDTO result = new ReqParamBaseDataDTO();
        List<DSGCPayloadParamsBean> dsgcPayloadParams =svcMngDao.queryServPayloadParam(param);
        List<SVCMngIoParameterDTO> paramDTOS = new ArrayList<>();
        if(dsgcPayloadParams != null){
            Iterator<DSGCPayloadParamsBean> iterator = dsgcPayloadParams.iterator();
            while (iterator.hasNext()){
                DSGCPayloadParamsBean temp = iterator.next();
               // PayloadParamDTO dto = new PayloadParamDTO();
                SVCMngIoParameterDTO dto = new SVCMngIoParameterDTO();
                dto.setNodeName(temp.getParamCode());
                dto.setNodeDesc(temp.getParamDesc());
                if("Y".equals(temp.getParamNeed())){
                    dto.setRequired(true);
                }else {
                    dto.setRequired(false);
                }
              //  dto.setRequired("Y".equals(temp.getParamNeed())?true:false);
                dto.setNodeValue(temp.getParamSample());
                dto.setDataType(temp.getParamType());
                dto.setNodeType("custom");
                paramDTOS.add(dto);
            }
        }
        result.setParamList(paramDTOS);
        List<DSGCPayloadSampleBean> payloadSampleBeans = svcMngDao.querySrvPaloadSample(param);
        Map<String,String> map = new HashMap<>();
        map.put("REST","");
        map.put("SOAP","");
        if(dsgcPayloadParams != null){
            Iterator<DSGCPayloadSampleBean> iterator = payloadSampleBeans.iterator();
            while (iterator.hasNext()){
                DSGCPayloadSampleBean temp = iterator.next();
                PayloadSampleDTO payloadSampleDTO = new PayloadSampleDTO();
                payloadSampleDTO.setResCode(temp.getResCode());
                payloadSampleDTO.setPlSample(temp.getPlSample());
                if ("REST".equals(temp.getUriType())){
                    map.put("REST",payloadSampleDTO.getPlSample());
                }
                if ("SOAP".equals(temp.getUriType())){
                    map.put("SOAP",payloadSampleDTO.getPlSample());
                }
            }

        }
        result.setSampleMap(map);
        return result;
    }

    /**
     * 获取wsdl中的方法
     * @param param
     * @return
     */
    public List<String> queryServWsdlFunction(SVCCommonReqBean param){
        List<String> functionList = new ArrayList<>();
        try {
            String wsdl =getWsdlUrl(param.getCon0());
             functionList =resolveWsdlFunction(wsdl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return functionList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveParamData(SaveParamDataVO param) {
        if (StringUtil.isBlank(param.getRestSample()) && StringUtil.isBlank(param.getSoapSample())) {
            return;
        }
        if (StringUtil.isNotBlank(param.getRestSample())) {
            DSGCPayloadSampleBean dsgcPayloadSampleBean = new DSGCPayloadSampleBean();
            dsgcPayloadSampleBean.setResCode(param.getServNo());
            dsgcPayloadSampleBean.setReqOrRes(param.getParamType());
            dsgcPayloadSampleBean.setUriType("REST");
            DSGCPayloadSampleBean payloadRestSampleBean = svcMngDao.querySrvPaloadSoapOrRestSample(dsgcPayloadSampleBean);
            if (payloadRestSampleBean != null) {
                DSGCPayloadSampleBean tempSample = new DSGCPayloadSampleBean();
                tempSample.setPlSample(param.getRestSample());
                tempSample.setDpsamId(payloadRestSampleBean.getDpsamId());
                try {
                    svcMngDao.updateServPayloadById(tempSample);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                DSGCPayloadSampleBean bean = new DSGCPayloadSampleBean();
                bean.setResCode(param.getServNo());
                bean.setReqOrRes(param.getParamType());
                bean.setUriType("REST");
                bean.setPlSample(param.getRestSample());
                bean.setPlType("JSON");
                svcMngDao.addServPayload(bean);
            }
        }
        if (StringUtil.isNotBlank(param.getSoapSample())) {
            DSGCPayloadSampleBean dsgcPayloadSampleBean = new DSGCPayloadSampleBean();
            dsgcPayloadSampleBean.setResCode(param.getServNo());
            dsgcPayloadSampleBean.setReqOrRes(param.getParamType());
            dsgcPayloadSampleBean.setUriType("SOAP");
            DSGCPayloadSampleBean payloadSoapSampleBean = svcMngDao.querySrvPaloadSoapOrRestSample(dsgcPayloadSampleBean);
            if (payloadSoapSampleBean != null) {
                payloadSoapSampleBean.setPlSample(param.getSoapSample());
                svcMngDao.updateServPayloadById(payloadSoapSampleBean);
            } else {
                DSGCPayloadSampleBean bean = new DSGCPayloadSampleBean();
                bean.setResCode(param.getServNo());
                bean.setReqOrRes(param.getParamType());
                bean.setUriType("SOAP");
                bean.setPlSample(param.getSoapSample());
                bean.setPlType("XML");
                svcMngDao.addServPayload(bean);
            }

        }


        if (param.getParamList() != null && param.getParamList().size() > 0) {
            SVCCommonReqBean svcCommonReqBean = new SVCCommonReqBean();
            svcCommonReqBean.setCon0(param.getServNo());
            svcCommonReqBean.setQueryType(param.getParamType());
            List<DSGCPayloadParamsBean> list = svcMngDao.queryServPayloadParam(svcCommonReqBean);
            if (list.size() > 0) {
                DSGCPayloadParamsBean dsgcPayloadParamsBean = new DSGCPayloadParamsBean();
                dsgcPayloadParamsBean.setResCode(param.getServNo());
                dsgcPayloadParamsBean.setReqOrRes(param.getParamType());
                svcMngDao.delServPayloadParam(dsgcPayloadParamsBean);
            }
            List<DSGCPayloadParamsBean> payloadParamsBeans = new ArrayList<>();
            Iterator<SVCMngIoParameterDTO> iterator = param.getParamList().iterator();
            while (iterator.hasNext()) {
                SVCMngIoParameterDTO dto = iterator.next();
                DSGCPayloadParamsBean paramsBean = new DSGCPayloadParamsBean();
                paramsBean.setResCode(param.getServNo());
                paramsBean.setReqOrRes(param.getParamType());
                paramsBean.setParamCode(dto.getNodeName());
                paramsBean.setParamType(dto.getDataType());
                paramsBean.setParamDesc(dto.getNodeDesc());
                if(dto.getRequired()){
                    paramsBean.setParamNeed("Y");
                } else {
                    paramsBean.setParamNeed("N");
                }
                if(StringUtil.isBlank(dto.getNodeValue())){

                    paramsBean.setParamSample("");
                }else {
                    paramsBean.setParamSample(dto.getNodeValue());
                }
                svcMngDao.addServPayloadParam(paramsBean);

            }
            completionThread(param.getServNo());

        }
    }
    //返回解析的字段Map<name,value>集合，若va;ue=null，表示该name节点为父节点，不包含内容。
    public List<Map<String,String>>  resloveXML(String XMLDemo){
        String patternXml="<(.*?)>";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(patternXml);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(XMLDemo);
        Map<String,String> value=new HashMap<String,String>();
        while (m.find()) {
            String node=m.group();
            //<value></value>,匹配到结束字符返回
            if(node.indexOf("</")>-1){
                continue;
            }
            String nodetext=node.substring(1,node.length()-1);
            //匹配<value>的文本
            String patternXmlContext="<"+nodetext+">"+"(.*?)"+"</"+nodetext+">";
            Pattern rr = Pattern.compile(patternXmlContext);
            Matcher mm = rr.matcher(XMLDemo);
            //<value>父节点没有内容为null
            value.put(nodetext,null);
            if(mm.find()){
                String ContextResult=mm.group();
                String Context=ContextResult.substring(ContextResult.indexOf(">")+1,(ContextResult.lastIndexOf("</")));
                //不包含子节点，内容为该节点文本
                if(Context.indexOf("<")<0){
                    value.put(nodetext,Context);
                }
            }
        }
        List<Map<String,String>> result =new ArrayList<>();
        value.keySet().stream().forEach(e->{
            Map<String,String> node=new HashMap<String,String>();
            node.put("name",e);
            node.put("value",value.get(e));
            result.add(node);
        });
        return result;
    }

    public List<Map<String,String>> resloveJson(String JSONDemo){
        String patternJson="\"[^\"]{0,}\":";
        Pattern r = Pattern.compile(patternJson);
        Matcher m = r.matcher(JSONDemo);
        Map<String,String> value=new HashMap<String,String>();
        //匹配节点
        while (m.find()) {
            String node=m.group();
            String nodetext=node.substring(1,node.length()-2);
            String patternXmlContext=nodetext+"\":( *)\"(.*?)\"";
            Pattern rr = Pattern.compile(patternXmlContext);
            Matcher mm = rr.matcher(JSONDemo);
            value.put(nodetext,null);
            //匹配节点对应的内容
            if(mm.find()){
                String ContextResult=mm.group();
                String Context=ContextResult.substring(ContextResult.indexOf("\":")+2,(ContextResult.lastIndexOf("\"")));
                if(Context.indexOf("{")<0){
                    //去除包含的"符号
                    Context=Context.trim();
                    Context=Context.substring(1,Context.length());
                    value.put(nodetext,Context);
                }
            }
        }
        List<Map<String,String>> result =new ArrayList<>();
        value.keySet().stream().forEach(e->{
            Map<String,String> node=new HashMap<String,String>();
            node.put("name",e);
            node.put("value",value.get(e));
            result.add(node);
        });
        return result;

    }


    /**
     * 根据WsdlUrl获取服务请求实例报文
     * @Prams   wsdl地址，方法名
     * @return
     */
    public String getWsdlBodyReq(String wsdl,String methodName) {
        //单元测试类已创建
        WsdlResolvProxy wsdlResolv= WsdlResolvProxy.newInstance();
        String result=wsdlResolv.getWsdlBodyReq(wsdl,methodName);
        return result;
    }

    /**
     * 根据WsdlUrl获取服务响应实例报文
     * @Prams   wsdl地址，方法名
     * @return
     */
    public String getWsdlBodyRes(String wsdl,String methodName) {
        //单元测试类已创建
        WsdlResolvProxy wsdlResolv= WsdlResolvProxy.newInstance();
        String result=wsdlResolv.getWsdlBodyRes(wsdl,methodName);
        return result;
    }


    /**
     * 根据ServNo获取完整的地址
     * @Prams   服务ServNo,环境code，默认DEV开发环境
     * @return
     */
    public String getWsdlUrl(String servNo,String envCode) {
        String wsdlUrl="";
        if(envCode==null||envCode==""){
            envCode="DEV";
        }
        envCode=envCode.toUpperCase();
        List<Map<String,Object>> urlResult=this.svcMngDao.getUrlByServNo(servNo);
        if(urlResult.size()==0){
            System.err.println("不存在该编号");
            return null;
        }
       String url= (String) (urlResult.get(0).get("IB_URI"));
        if(!url.startsWith("/")){
            url="/"+url;
        }
        List<Map<String,Object>> envResult=this.svcMngDao.getEvnInfo(envCode);
        if(envResult.size()==0){
            System.err.println("不存在该环境");
            return null;
        }
        String port= (String) (envResult.get(0).get("ESB_PORT"));
        String ip= (String) (envResult.get(0).get("ESB_IP"));
        wsdlUrl="https://"+ip+":"+port+url+"?wsdl";
        return wsdlUrl;
    }

    /**
     * 解析wsdl中的方法
     * @param wsdl
     * @return
     * @throws Exception
     */
    public List<String > resolveWsdlFunction(String wsdl) throws Exception {
        /**
         * TODO
         */
        return new ArrayList<>();

    }

}
