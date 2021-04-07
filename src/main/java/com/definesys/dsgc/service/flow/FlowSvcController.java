package com.definesys.dsgc.service.flow;


import com.definesys.dsgc.service.flow.bean.FlowServices;
import com.definesys.dsgc.service.flow.dto.FlowReqCommonDTO;
import com.definesys.mpaas.common.http.Response;
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

        return null;
    }


    /**
     * 新增flowservice
     * @param param
     * @return
     */
    @RequestMapping(value = "/addFlowService",method = RequestMethod.POST)
    public Response addFlowService(@RequestBody FlowServices param){
        return null;
    }


    /**
     * 删除flowservice（逻辑删除）
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteFlowService",method = RequestMethod.POST)
    public Response deleteFlowService(@RequestBody FlowReqCommonDTO param){
        return null;
    }

}
