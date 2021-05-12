package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.QuerySvcParamBean;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.dsgc.service.svcinfo.bean.SvcBsInfoBean;
import com.definesys.mpaas.common.http.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(description = "服务资产信息相关api", tags = "服务资产信息")
@RequestMapping(value = "/dsgc/svcinfo")
@RestController
public class SVCInfoController {
    @Autowired
    private SVCInfoService svcInfoService;

    @RequestMapping(value = "/querySvcInfoListByCon", method = RequestMethod.POST)
    public Response querySvcInfoListByCon(@RequestBody SVCInfoQueryBean q,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return Response.ok().setData(this.svcInfoService.querySvcInfoListByCon(q, pageSize, pageIndex));
    }

    /*** 查询服务信息 **/
    @RequestMapping(value = "/querySVCInfo", method = RequestMethod.POST)
    public Response queryService(@RequestBody SVCInfoQueryBean svcInfoQueryBean) {
        return Response.ok().data(this.svcInfoService.queryService(svcInfoQueryBean));
    }

    /*** 查询所有服务uri信息 **/
    @RequestMapping(value = "/queryAllSvcUri", method = RequestMethod.GET)
    public Response queryAllSvcUri() {
        return Response.ok().data(this.svcInfoService.queryAllSvcUri());
    }

    /*** 查询所有服务信息 **/
    @RequestMapping(value = "/querySvcList", method = RequestMethod.POST)
    public Response querySvcList(@RequestBody QuerySvcParamBean svcParamBean) {
        List<QuerySvcParamBean> result = null;
        try {
            result = this.svcInfoService.querySvcList(svcParamBean);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("查询服务信息失败！");
        }
        return Response.ok().data(result);
    }

    /*** 根据服务编号和目标系统编号查询业务系统信息 **/
    @PostMapping(value = "/querySvcBsInfoByCode")
    public Response querySvcBsInfoByCode(@RequestBody SvcBsInfoBean bsInfoBean) {
        return this.svcInfoService.querySvcBsInfoByCode(bsInfoBean);
    }

    /*** 新增业务系统信息 **/
    @PostMapping(value = "/addSvcBsInfo")
    public Response addSvcBsInfo(@RequestBody SvcBsInfoBean bsInfoBean) {
        return this.svcInfoService.addSvcBsInfo(bsInfoBean);
    }

    /*** 删除业务系统信息 **/
    @PostMapping(value = "/delSvcBsInfo")
    public Response delSvcBsInfo(@RequestBody SvcBsInfoBean bsInfoBean) {
        return Response.ok().data(this.svcInfoService.delSvcBsInfo(bsInfoBean));
    }


}
