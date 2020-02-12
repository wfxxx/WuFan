package com.definesys.dsgc.service.bpm;

import com.definesys.dsgc.service.bpm.bean.*;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dsgc/bpm")
public class BpmController {
    @Autowired
    private BpmService bpmService;

    @RequestMapping(value = "/generateBpmInst", method = RequestMethod.POST)
    public Response generateBpmInst(HttpServletRequest request){
    String instId =  bpmService.generateBpmInst();
        return Response.ok().data(instId);
    }

    /**
     * 审批接口
     * @return
     */
    @RequestMapping(value = "/approveTask", method = RequestMethod.POST)
    public Response approveTask(HttpServletRequest request,
                                @RequestBody BpmApproveBean param){
        String userId = request.getHeader("uid");
        try {
            bpmService.approveTask(param,userId);
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
        return Response.ok();
    }

    /**
     * 代办获取接口
     * @return
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public Response getTaskList(HttpServletRequest request,
                                @RequestBody BpmCommonReqBean param,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        //返回分页List<BpmTaskBean>
        String userId = request.getHeader("uid");
        PageQueryResult<BpmInstanceDTO> result = bpmService.getTaskList(param,userId,pageSize,pageIndex);
        return Response.ok().data(result);
    }

    /**
     * 获取代办总数的接口
     * @return
     */
    @RequestMapping(value = "/getTaskCount", method = RequestMethod.GET)
    public Response getTaskCount(HttpServletRequest request){
        String userId = request.getHeader("uid");
        List<Map<String, Object>> list = bpmService.getTaskCount(userId);
        String count = list.get(0).get("COUNT").toString();
        return Response.ok().setTotal(Long.parseLong(count));
    }

    /**
     * 获取我的申请列表接口
     * @return
     */
    @RequestMapping(value = "/myApply", method = RequestMethod.POST)
    public Response myApply(HttpServletRequest request,
                                @RequestBody BpmCommonReqBean param,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        //返回分页List<BpmMyApplyBean>
        String userId = request.getHeader("uid");
        PageQueryResult<BpmInstanceDTO> result = bpmService.myApply(param,userId,pageSize,pageIndex);
        return Response.ok().data(result);
    }


    /**
     * 获取审批历史接口
     * @param param
     * @return
     */
    @RequestMapping(value = "/getInstHistory", method = RequestMethod.POST)
    public Response getInstHistory(@RequestBody BpmCommonReqBean param,HttpServletRequest request){
        //返回List<BpmHistoryBean>
        String userId = request.getHeader("uid");
        List<BpmHistoryDTO> list = bpmService.getInstHistory(param);
        return Response.ok().data(list);
    }

    /**
     * 发起审批测试接口
     * @param
     * @return
     */
    @RequestMapping(value = "/generateBpmInstance", method = RequestMethod.POST)
    public Response generateBpmInstance(@RequestBody BpmSubBean bpmSubBean, HttpServletRequest request){
        //返回List<BpmHistoryBean>
        String userId = request.getHeader("uid");
        BpmInstanceBean bpmInstanceBean = new BpmInstanceBean();
        bpmInstanceBean.setProcessId(bpmSubBean.getProcessId());
        bpmInstanceBean.setInstTitle(bpmSubBean.getInstTitle());
        bpmService.generateBpmInstance(bpmInstanceBean,bpmSubBean.getApprover(),userId);
        return Response.ok();
    }

    /**
     * 查询流程实例接口
     * @param
     * @return
     */
    @RequestMapping(value = "/queryBpmInstanceBaseInfo", method = RequestMethod.POST)
    public Response queryBpmInstanceBaseInfo(@RequestBody BpmCommonReqBean param, HttpServletRequest request){
        String userId = request.getHeader("uid");
       BpmInstanceDTO bpmInstanceDTO = bpmService.queryBpmInstanceBaseInfo(param);
        return Response.ok().setData(bpmInstanceDTO);
    }

    /**
     * 查询流程实例接口
     * @param
     * @return
     */
    @RequestMapping(value = "/queryProcessTypeList", method = RequestMethod.GET)
    public Response queryProcessTypeList(){
        return Response.ok().setData(bpmService.queryProcessTypeList());
    }



}
