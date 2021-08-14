package com.definesys.dsgc.service.dess.DessBusiness;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName DBusController
 * @Description 定时调度业务
 * @Author Xueyunlong
 * @Date 2020-8-3 13:27
 * @Version 1.0
 **/

@RestController
@RequestMapping("/dsgc/dessBusiness")
public class DBusController {


    @Autowired
    private DBusService dBusService;


    /**
     * 获取业务列表
     *
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryBusinessList", method = RequestMethod.POST)
    public Response queryBusinessList(@RequestBody CommonReqBean param,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request) {
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)) {
            return Response.error("无权限操作");
        }
        PageQueryResult<DessBusiness> result;
        try {
            result = dBusService.queryBusinessList(param, pageSize, pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取列表失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 新增业务
     *
     * @param dessBusiness
     * @return
     */
    @RequestMapping(value = "/addBusiness", method = RequestMethod.POST)
    public Response addBusiness(@RequestBody DessBusiness dessBusiness) {
        try {
            dBusService.addBusiness(dessBusiness);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增发生错误");
        }
        return Response.ok();
    }

    /**
     * 验证业务名称
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/checkBusinessName", method = RequestMethod.POST)
    public Response checkBusinessName(@RequestBody CommonReqBean param) {
        try {
            Boolean isExist = dBusService.checkBusinessName(param);
            return Response.ok().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("验证作业编号失败！");
        }
    }

    /**
     * 删除业务
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delBusiness", method = RequestMethod.POST)
    public Response delBusiness(@RequestBody CommonReqBean param) {
        try {
            dBusService.delBusiness(param.getCon0());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("删除发生错误");
        }
        return Response.ok();
    }

    /**
     * 获取业务详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getBusinessDtl", method = RequestMethod.POST)
    public Response getBusinessDtl(@RequestBody CommonReqBean param) {
        DessBusiness dessBusiness = null;
        try {
            dessBusiness = dBusService.getBusinessDtl(param.getCon0());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取失败");
        }
        return Response.ok().data(dessBusiness);
    }

    @RequestMapping(value = "/updateBusinessDtl", method = RequestMethod.POST)
    public Response updateBusinessDtl(@RequestBody DessBusiness dessBusiness) {
        try {
            dBusService.updateBusinessDtl(dessBusiness);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("修改失败");
        }
        return Response.ok();
    }
}
