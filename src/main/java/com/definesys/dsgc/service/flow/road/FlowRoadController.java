package com.definesys.dsgc.service.flow.road;

import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadEditReqDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadQueryDTO;
import com.definesys.dsgc.service.flow.mng.FlowSvcService;
import com.definesys.mpaas.common.http.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/flow/road")
public class FlowRoadController {

    @Autowired
    private FlowRoadService flowRoadService;

    @Autowired
    private FlowSvcService flowSvcService;

    /**
     * 获取flow路径图
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFlowRoad", method = RequestMethod.POST)
    public Response getFlowRoad(@RequestBody FlowRoadQueryDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("非法的请求参数！");
        }
        FlowRoadDTO res = this.flowRoadService.getFlowRoad(param);

        if (res == null) {
            return Response.error("对象不存在！");
        }

        return Response.ok().setData(res);
    }

    /**
     * 开启编辑模式
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/startEditting", method = RequestMethod.POST)
    public Response startFlowRoadEditting(@RequestBody FlowRoadEditReqDTO param) {
        if(param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("无效的请求参数！");
        }

        if(!this.flowSvcService.isAuthToEditFlow(param.getFlowId())){
            return Response.error("非法的操作权限！");
        }

        String res = this.flowRoadService.startFlowRoadEdit(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    /**
     * 放弃编辑
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/backoffEditting", method = RequestMethod.POST)
    public Response backoffFlowRoadEditting(@RequestBody FlowRoadEditReqDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("无效的请求参数！");
        }

        if (!this.flowSvcService.isAuthToEditFlow(param.getFlowId())) {
            return Response.error("非法的操作权限！");
        }

        String res = this.flowRoadService.backoffFlowRoadEdited(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    /**
     * 保存编辑
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Response saveFlowRoad(@RequestBody FlowRoadDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error("无效的请求参数！");
        }

        if (!this.flowSvcService.isAuthToEditFlow(param.getFlowId())) {
            return Response.error("非法的操作权限！");
        }
        String res = this.flowRoadService.saveFlowRoadWithCloseEditing(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }
}
