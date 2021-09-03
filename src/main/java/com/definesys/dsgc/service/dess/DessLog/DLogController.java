package com.definesys.dsgc.service.dess.DessLog;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.svclog.DSGCLogInstanceService;
import com.definesys.dsgc.service.utils.MsgCompressUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName DLogController
 * @Description TODO
 * @Author ystar
 * @Date 2020-8-3 13:30
 * @Version 1.0
 **/

@RestController
@RequestMapping("/dsgc/dessLog")
public class DLogController {


    @Autowired
    private DLogService dLogService;

    @Autowired
    private DSGCLogInstanceService logService;

    /**
     * 查询作业日志列表
     *
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryJobLogList", method = RequestMethod.POST)
    public Response queryJobLogList(@RequestBody CommonReqBean param,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)) {
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = dLogService.queryJobLogList(param, pageSize, pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取日志列表失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 获取日志详情
     *
     * @param dessLog
     * @return
     */
    @RequestMapping(value = "/findJobLogDetailByIdSwitch", method = RequestMethod.POST)
    public Response findJobLogDetailByIdSwitch(@RequestBody DessLog dessLog) {
        DessLog log = null;
        try {
            log = dLogService.findJobLogDetailByIdSwitch(dessLog.getLogId());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取日志失败");
        }
        return Response.ok().setData(log);
    }

    /**
     * 获取请求报文
     *
     * @param envCode
     * @param logId
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/getDessBodyPayload", method = RequestMethod.GET)
    public void getDessBodyPayload(@RequestParam("envCode") String envCode,
                                   @RequestParam("logId") String logId,
                                   HttpServletResponse response) {
        try {
            String switchUrl = this.logService.getSwitchUrl(envCode);
            if (switchUrl != null) {
                String res = HttpReqUtil.getObject(switchUrl + "/dsgc/dessLog/getBodyPayload?logId=" + logId + "&envCode=" + envCode, String.class);
                this.logService.showData(response, res);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String str = "";
            str = dLogService.getBodyPayload(logId);
            if (str != null) {
                if (str.trim().length() == 0) {
                    logService.showData(response, "报文为空");
                } else {
                    str.replaceAll(" ", "");
//                        System.out.println("|" + str + "|");
                    if (str.contains("<")) {
//                            System.out.println("---xx---");
                        logService.showData(response, str);
                    } else {
//                     String s = MsgZLibUtil.decompress(str);
                        String s = MsgCompressUtil.deCompress(str);
                        logService.showData(response, s);
                    }
                }

            } else {
                dLogService.noPayload(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
