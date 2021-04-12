package com.definesys.dsgc.service.flow.node;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.dto.FlowConstants;
import com.definesys.dsgc.service.flow.dto.FlowNodeCommonDTO;
import com.definesys.dsgc.service.flow.mng.FlowSvcService;
import com.definesys.dsgc.service.flow.node.dto.ParamPanelDTO;
import com.definesys.dsgc.service.flow.node.dto.param.RestListenerParam;
import com.definesys.mpaas.common.http.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/flow/node")
public class FlowNodeController {
    @Autowired
    private FlowNodeService flowNodeService;

    @Autowired
    private FlowSvcService flowSvcService;

    /**
     * 获取node的参数面板参数
     * @param reqParam
     * @return
     */
    @RequestMapping(value = "/getParam",method = RequestMethod.POST)
    public Response getFlowNodeParam(@RequestBody FlowNodeCommonDTO reqParam) {

        if (reqParam == null
                || StringUtils.isBlank(reqParam.getFlowId())
                || StringUtils.isBlank(reqParam.getNodeId())
                || StringUtils.isBlank(reqParam.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }

        FlowNodes node = this.flowNodeService.getFlowNode(reqParam.getFlowId(),reqParam.getFlowVersion(),reqParam.getNodeId());

        ParamPanelDTO panel = new ParamPanelDTO();

        panel.setFlowId(reqParam.getFlowId());
        panel.setFlowVersion(reqParam.getFlowVersion());
        panel.setNodeId(reqParam.getNodeId());

        panel.setInputType(node != null ? node.getInputType() : null);
        panel.setInputMeta(node != null ? node.getInputMeta() : null);
        panel.setInputValue(node != null ? node.getInputValue() : null);

        panel.setOutputType(node != null ? node.getOutputType() : null);
        panel.setOutputMeta(node != null ? node.getOutputMeta() : null);
        panel.setOutputValue(node != null ? node.getOutputValue() : null);


        JSONObject panelJsonObject = JSONObject.parseObject(JSONObject.toJSONString(panel));
        if (node != null && StringUtils.isNotBlank(node.getParams())) {
            //组合panel 与 params

            JSONObject paramJsonObject = JSONObject.parseObject(node.getParams());
            panelJsonObject.fluentPut(FlowConstants.FLOW_PANEL_PARAMS,paramJsonObject);
            return Response.ok().data(panelJsonObject);

        } else {
            return Response.ok().data(panelJsonObject);
        }
    }


    private JSONObject getDemo(){
        ParamPanelDTO panel  = new ParamPanelDTO();
        panel.setNodeId("111");
        panel.setInputMeta("sfsfsf");
        panel.setInputType("sfsfsf");
        panel.setInputValue("sfsfsf");
        panel.setOutputMeta("sfsfsf");
        panel.setOutputType("sfsfs");
        panel.setOutputValue("sfsfsfs");

        RestListenerParam p = new RestListenerParam();
        p.setTtt("tttt");
        panel.setParams(p);
        String json = "{\n" +
                "        \"flowId\": \"fdc3bfc2a2854b2395a6224ef89cce45\",\n" +
                "        \"flowVersion\": \"default\",\n" +
                "        \"title\": \"test1ABCDE\",\n" +
                "        \"startNodeId\": \"6f01e21e-9792-4cfd-b90a-dace12aa6bc9\",\n" +
                "        \"nodeList\": [\n" +
                "            {\n" +
                "                \"nodeId\": \"6f01e21e-9792-4cfd-b90a-dace12aa6bc9\",\n" +
                "                \"cnptCode\": \"REST\",\n" +
                "                \"title\": \"REST\",\n" +
                "                \"desc\": \"REST监听器\",\n" +
                "                \"type\": \"INPUT\",\n" +
                "                \"beforeNodeId\": null,\n" +
                "                \"afterNodeId\": \"03934f9e-a171-469e-b95b-02bed5fe5ce0\",\n" +
                "                \"children\": null\n" +
                "            },\n" +
                "            {\n" +
                "                \"nodeId\": \"03934f9e-a171-469e-b95b-02bed5fe5ce0\",\n" +
                "                \"cnptCode\": \"DB\",\n" +
                "                \"title\": \"DB查询\",\n" +
                "                \"desc\": \"DB查询\",\n" +
                "                \"type\": \"OUTPUT\",\n" +
                "                \"beforeNodeId\": null,\n" +
                "                \"afterNodeId\": null,\n" +
                "                \"children\": null\n" +
                "            }\n" +
                "        ]\n" +
                "    }";

        JSONObject jsonObj = JSONObject.parseObject(json);

        JSONObject jsonObj1 = JSONObject.parseObject(json);
        return jsonObj1;
    }


    @RequestMapping(value = "/mergeParam",method = RequestMethod.POST)
    public Response mergeFlowNodeParam(@RequestBody String body){
        if(body != null){
            ParamPanelDTO panel = JSONObject.parseObject(body,ParamPanelDTO.class);
            if(panel == null || StringUtils.isBlank(panel.getFlowId()) || StringUtils.isBlank(panel.getFlowVersion())
            || StringUtils.isBlank(panel.getNodeId())) {
                return Response.error("非法的请求参数！");
            }

            String paramJsonTxt = null;
            JSONObject param = JSONObject.parseObject(body).getJSONObject(FlowConstants.FLOW_PANEL_PARAMS);
            if(param != null){
                paramJsonTxt = param.toJSONString();
            }

            if(!this.flowSvcService.isAuthToEditFlow(panel.getFlowId())){
                return Response.error("非法的操作权限！");
            }

            String res = this.flowNodeService.mergeFlowNodeParam(panel,paramJsonTxt);
            return "Y".equals(res) ? Response.ok() : Response.error(res);

        }

        return Response.ok();
    }

    /**
     * 移除流程图中的节点
     * @param param
     * @return
     */
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public Response removeFlowNode(@RequestBody FlowNodeCommonDTO param){
        if(param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())
        || StringUtils.isBlank(param.getNodeId())) {
            return Response.error("非法的请求参数！");
        }

        if(!this.flowSvcService.isAuthToEditFlow(param.getFlowId())){
            return Response.error("非法的操作权限！");
        }

        String res = this.flowNodeService.removeFlowNode(param);
        return "Y".equals(res) ? Response.ok() : Response.error(res);
    }


}
