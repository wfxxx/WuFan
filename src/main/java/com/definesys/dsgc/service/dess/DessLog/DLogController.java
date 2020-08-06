package com.definesys.dsgc.service.dess.DessLog;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessInstance.DInsService;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName DLogController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:30
 * @Version 1.0
 **/

@RestController
@RequestMapping("/dsgc/dessLog")
public class DLogController {


    @Autowired
    private DLogService dLogService;

    /**
     * 查询作业日志列表
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryJobLogList",method = RequestMethod.POST)
    public Response queryJobLogList(@RequestBody CommonReqBean param,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = dLogService.queryJobLogList(param,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取日志列表失败");
        }
        return Response.ok().setData(result);
    }

}
