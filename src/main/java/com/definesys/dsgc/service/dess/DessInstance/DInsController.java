package com.definesys.dsgc.service.dess.DessInstance;


import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dess.CommonReqBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstSltBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstVO;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstVO2;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DInsController
 * @Description 定时调度实例
 * @Author Xueyunlong
 * @Date 2020-8-3 13:28
 * @Version 1.0
 **/
@RestController("DInsController")
@RequestMapping("/dsgc/dessInst")
public class DInsController {

    @Autowired
    private DInsService dInsService;


    /**
     * 获取调度实例列表
     *
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryJobInstaceList", method = RequestMethod.POST)
    public Response queryJobInstaceList(@RequestBody CommonReqBean param,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)) {
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = dInsService.queryJobInstaceList(param, pageSize, pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取作业列表失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 新增作业
     *
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/saveJobBaseInfo", method = RequestMethod.POST)
    public Response saveJobBaseInfo(@RequestBody DInstBean dinstBean, HttpServletRequest request) {
        try {
            dInsService.saveJobBaseInfo(dinstBean);
            DinstVO2 sendDinstVO = dInsService.getDinstVO(dinstBean.getJobNo());
            dInsService.UpdateDessTask(request, sendDinstVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增发生错误");
        }
        return Response.ok();
    }

    /**
     * 验证作业编号
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/checkJobNoIsExist", method = RequestMethod.POST)
    public Response checkJobNoIsExist(@RequestBody CommonReqBean param) {
        try {
            Boolean isExist = dInsService.checkJobNoIsExist(param);
            return Response.ok().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("验证作业编号失败！");
        }
    }

    /**
     * 删除作业实例
     *
     * @param commonReqBean
     * @return
     */

    @RequestMapping(value = "/delJobInstance", method = RequestMethod.POST)
    public Response delJobInstance(@RequestBody CommonReqBean commonReqBean, HttpServletRequest request) {
        try {
            dInsService.delJobInstance(commonReqBean.getCon0());
            DinstVO2 sendDinstVO = dInsService.getDinstVO(commonReqBean.getCon0());
            dInsService.pauseDessTask(request, sendDinstVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除发生错误");
        }
        return Response.ok();
    }

    /**
     * 停用作业
     *
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/updateJobInstanceStatus", method = RequestMethod.POST)
    public Response updateJobInstanceStatus(@RequestBody DInstBean dinstBean, HttpServletRequest request) {
        try {
            DinstVO2 sendDInstVO = dInsService.getDinstVO(dinstBean.getJobNo());
            sendDInstVO.setJobStatus(dinstBean.getJobStatus());
            if (sendDInstVO.getJobStatus().equals("1")) {
                dInsService.addDessTask(request, sendDInstVO);
            } else {
                //当用户停止调用时，下一次调度时间未知。
                dinstBean.setNextDoTime(null);
                dInsService.pauseDessTask(request, sendDInstVO);
            }
            dInsService.updateJobInstanceStatus(dinstBean);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("停用发生错误");
        }
        return Response.ok();
    }


    /**
     * 获取作业基本信息
     *
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/getJobBaseInfo")
    public Response getJobInstance(@RequestBody DInstBean dinstBean) {
        DInstBean result = new DInstBean();
        try {
            result = dInsService.getJobInstance(dinstBean.getJobNo());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取作业基础信息失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 保存作业基础信息
     *
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/saveJobInsDeatail", method = RequestMethod.POST)
    public Response saveJobInsDeatail(@RequestBody DInstBean dinstBean, HttpServletRequest request) {
        try {
            dInsService.saveJobInsDeatail(dinstBean);
            DinstVO2 sendDinstVO = dInsService.getDinstVO(dinstBean.getJobNo());
            dInsService.UpdateDessTask(request, sendDinstVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("更新发生错误");
        }
        return Response.ok();
    }

    /**
     * 保存调度时间
     *
     * @param dinstVO
     * @return
     */
    @RequestMapping(value = "/saveScheduling", method = RequestMethod.POST)
    public Response saveScheduling(@RequestBody DinstVO dinstVO, HttpServletRequest request) {
        System.out.println(dinstVO.toString());
        List<Map<String, String>> timeList = null;
        try {
            timeList = dInsService.saveScheduling(dinstVO, request);
            DinstVO2 sendDinstVO = dInsService.getDinstVO(dinstVO.getJobNo());
            dInsService.UpdateDessTask(request, sendDinstVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("保存发生错误");
        }
        return Response.ok().setData(timeList);
    }


    /**
     * 获取调度时间定义
     *
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/getJobScheduling", method = RequestMethod.POST)
    public Response getJobScheduling(@RequestBody DInstBean dinstBean, HttpServletRequest request) {
        try {
            DinstVO jobScheduling = dInsService.getJobScheduling(dinstBean.getJobNo(), request);
            return Response.ok().setData(jobScheduling);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取时间定义失败");
        }
    }


    /**
     * 手动调用定时任务
     *
     * @param reqParam
     * @return
     */
    @RequestMapping(value = "/manualJobInstance", method = RequestMethod.POST)
    public Response manualJobInstance(@RequestBody HashMap reqParam, HttpServletRequest request) {
        JSONObject jsonObject;
        try {
            jsonObject = dInsService.manualJobInstance(request, reqParam);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("手动调用发生错误");
        }
        return Response.ok().data(jsonObject);
    }


    @RequestMapping(value = "/getAllJobScheduleTime", method = RequestMethod.POST)
    public Response getAllJobScheduleTime(@RequestBody DInstSltBean bean, HttpServletRequest request) {
        return dInsService.getAllJobScheduleTime(bean, request);
    }


}
