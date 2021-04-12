package com.definesys.dsgc.service.flow.mng;


import com.definesys.dsgc.service.flow.road.FlowRoadService;
import com.definesys.dsgc.service.flow.bean.FlowServices;
import com.definesys.dsgc.service.flow.dto.FlowReqCommonDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadEditReqDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadQueryDTO;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/dsgc/flow")
public class FlowSvcController {
    @Autowired
    private FlowSvcService flowSvcService;



    /**
     * 分页查询flowService列表
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryFlowServices",method = RequestMethod.POST)
    public Response queryFlowServcies(@RequestBody FlowReqCommonDTO param,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){

        String uid  = MpaasSession.getCurrentUser();
        return Response.ok().setData(this.flowSvcService.pageQueryFlowServices(param,uid,pageIndex,pageSize));
    }


    /**
     * 新增flowservice
     * @param param
     * @return
     */
    @RequestMapping(value = "/megreFlowService",method = RequestMethod.POST)
    public Response megreFlowService(@RequestBody FlowServices param){
        String uid = MpaasSession.getCurrentUser();
        String res = this.flowSvcService.megreFlowServices(param,uid);
        if("Y".equals(res)){
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }


    /**
     * 删除flowservice（逻辑删除）
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteFlowService",method = RequestMethod.POST)
    public Response deleteFlowService(@RequestBody FlowReqCommonDTO param){
        String uid = MpaasSession.getCurrentUser();
        String res = this.flowSvcService.deleteFlowServcie(param.getFlowId(),uid);
        if("Y".equals(res)){
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }


}
