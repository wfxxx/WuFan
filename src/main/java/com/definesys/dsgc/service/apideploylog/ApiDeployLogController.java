package com.definesys.dsgc.service.apideploylog;

import com.definesys.dsgc.service.apideploylog.bean.CommonReqBean;
import com.definesys.dsgc.service.apideploylog.bean.DagDeployStatBean;
import com.definesys.dsgc.service.apideploylog.bean.QueryDeployLogDTO;
import com.definesys.dsgc.service.apideploylog.bean.QueryDeployStatDTO;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/apideploylog")
public class ApiDeployLogController
{
    @Autowired
    private ApiDeployLogService apiDeployLogService;
    @RequestMapping(value = "/queryDeploySurvey",method = RequestMethod.POST)
    public Response queryDeploySurvey(@RequestBody CommonReqBean param){
        List<QueryDeployStatDTO> result = null;
        try {
            result = apiDeployLogService.queryDeploySurvey(param);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询部署概况失败");
        }
        return Response.ok().setData(result);

    }
    @RequestMapping(value = "/queryDeployHis",method = RequestMethod.POST)
    public Response queryDeployHis(@RequestBody CommonReqBean param,@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        PageQueryResult<QueryDeployLogDTO> result = null;
        try {
            result = apiDeployLogService.queryDeployHis(param,pageIndex,pageSize);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("查询部署日志失败");
        }
        return Response.ok().setData(result);

    }


    @RequestMapping(value = "/addDagDeployStat",method = RequestMethod.POST)
    public Response addDagDeployStat(@RequestBody DagDeployStatBean dagDeployStatBean){
        try {
             apiDeployLogService.addDagDeployStat(dagDeployStatBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("部署失败");
        }
        return Response.ok();

    }
}
