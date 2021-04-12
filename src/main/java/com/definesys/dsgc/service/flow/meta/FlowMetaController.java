package com.definesys.dsgc.service.flow.meta;

import com.definesys.dsgc.service.flow.meta.dto.DataNodeDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaCommonDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaDTO;
import com.definesys.dsgc.service.flow.mng.FlowSvcService;
import com.definesys.mpaas.common.http.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/dsgc/flow/meta")
public class FlowMetaController {

    @Autowired
    private FlowMetaService flowMetaService;

    @Autowired
    private FlowSvcService flowSvcService;


    /**
     * 根据metaId获取meta
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMeta",method = RequestMethod.POST)
    public Response getFlowMeta(@RequestBody FlowMetaCommonDTO param){
        if (param == null
                || StringUtils.isBlank(param.getFlowId())
                || StringUtils.isBlank(param.getMetaId())
                || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }
//return Response.ok().data(this.getDemo(param.getFlowId(),param.getFlowVersion(),param.getMetaId()));
       return Response.ok().data(this.flowMetaService.getFlowMeta(param.getFlowId(),param.getFlowVersion(),param.getMetaId()));
    }

    private FlowMetaDTO getDemo(String flowId,String flowVersion,String metaId){
        FlowMetaDTO res = new FlowMetaDTO();
        res.setFlowId(flowId);
        res.setFlowVersion(flowVersion);
        res.setMetaId(metaId);
        res.setMetaName("ttttt");
        res.setSour("user");
        res.setMetaType("json");


        DataNodeDTO t1 = new DataNodeDTO();
        t1.setExpanded(true);
        t1.setKey("root");
        t1.setLeaf(false);
        t1.setTitle("root");

        DataNodeDTO t2 = new DataNodeDTO();
        t2.setExpanded(true);
        t2.setKey("col1");
        t2.setLeaf(true);
        t2.setTitle("col1");

        DataNodeDTO t3 = new DataNodeDTO();
        t3.setExpanded(true);
        t3.setKey("col2");
        t3.setLeaf(true);
        t3.setTitle("col2");

        List<DataNodeDTO> subList =new ArrayList<DataNodeDTO>();
        subList.add(t2);
        subList.add(t3);

        t1.setChildren(subList);

        res.setMetaTree(t1);

        return res;
    }

    /**
     * 获取所有的meta
     * @param param
     * @return
     */
    @RequestMapping(value = "/getAllMeta",method = RequestMethod.POST)
    public Response getAllFlowMeta(@RequestBody FlowMetaCommonDTO param){
        if (param == null
                || StringUtils.isBlank(param.getFlowId())
                || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }
        return Response.ok().data(this.flowMetaService.getAllFlowMeta(param.getFlowId(),param.getFlowVersion()));
    }

    /**
     * 添加或者更新meta
     * @return
     */
    @RequestMapping(value = "/merge",method = RequestMethod.POST)
    public Response mergeFlowMeta(@RequestBody FlowMetaDTO meta){

        if(meta == null || StringUtils.isBlank(meta.getFlowId()) || StringUtils.isBlank(meta.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }

        if(!this.flowSvcService.isAuthToEditFlow(meta.getFlowId())){
            return Response.error("非法的操作权限！");
        }

        String res = this.flowMetaService.mergeFlowMeta(meta);

        if(res != null && !res.startsWith("error:")){
            Map<String,String> resMap = new HashMap<String,String>();
            resMap.put("metaId",res);
            return Response.ok().data(resMap);
        } else {
            return Response.error(res);
        }
    }


    /**
     * 移除meta
     * @param param
     * @return
     */
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public Response removeFlowMeta(@RequestBody FlowMetaCommonDTO param){
        if (param == null
                || StringUtils.isBlank(param.getFlowId())
                || StringUtils.isBlank(param.getMetaId())
                || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }

        if(!this.flowSvcService.isAuthToEditFlow(param.getFlowId())){
            return Response.error("非法的操作权限！");
        }

        String res =this.flowMetaService.removeFlowMetaByMetaId(param.getFlowId(),param.getFlowVersion(),param.getMetaId());
        return "Y".equals(res) ? Response.ok() :Response.error(res);
    }

}
