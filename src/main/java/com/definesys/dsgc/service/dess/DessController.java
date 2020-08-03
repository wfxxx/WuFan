package com.definesys.dsgc.service.dess;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.bean.DessBusiness;
import com.definesys.dsgc.service.dess.bean.DinstBean;
import com.definesys.dsgc.service.dess.bean.DinstVO;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName DessController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 14:35
 * @Version 1.0
 **/
@RestController
@RequestMapping("/dsgc/dess")
public class DessController {

    @Autowired
    private DessService dessService;


    /**
     * 获取调度实例列表
     * @param param
     * @param pageSize
     * @param pageIndex
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryJobInstaceList",method = RequestMethod.POST)
    public Response queryJobInstaceList(@RequestBody CommonReqBean param,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = dessService.queryJobInstaceList(param,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取作业列表失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 新增作业
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/saveJobBaseInfo",method = RequestMethod.POST)
    public Response saveJobBaseInfo(@RequestBody DinstBean dinstBean){
        try {
            dessService.saveJobBaseInfo(dinstBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增发生错误");
        }
        return Response.ok();
    }

    /**
     * 验证作业编号
     * @param param
     * @return
     */
    @RequestMapping(value = "/checkJobNoIsExist",method = RequestMethod.POST)
    public Response checkJobNoIsExist(@RequestBody CommonReqBean param){
        try {
            Boolean isExist = dessService.checkJobNoIsExist(param);
            return Response.ok().setData(isExist);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("验证作业编号失败！");
        }
    }

    /**
     * 删除作业实例
     * @param commonReqBean
     * @return
     */
    @RequestMapping(value = "/delJobInstance",method = RequestMethod.POST)
    public Response delJobInstance(@RequestBody CommonReqBean commonReqBean){
        try {
            dessService.delJobInstance(commonReqBean.getCon0());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除发生错误");
        }
        return Response.ok();
    }

    /**
     * 停用作业
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/updateJobInstanceStatus",method = RequestMethod.POST)
    public Response updateJobInstanceStatus(@RequestBody DinstBean dinstBean){
        try {
            dessService.updateJobInstanceStatus(dinstBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("停用发生错误");
        }
        return Response.ok();
    }

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
            result = dessService.queryJobLogList(param,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取日志列表失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 获取作业基本信息
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/getJobBaseInfo")
    public Response getJobInstance(@RequestBody DinstBean dinstBean){
        DinstBean result = new DinstBean();
        try {
            result = dessService.getJobInstance(dinstBean.getJobNo());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取作业基础信息失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 保存作业基础信息
     * @param dinstBean
     * @return
     */
    @RequestMapping(value = "/saveJobInsDeatail",method = RequestMethod.POST)
    public Response saveJobInsDeatail(@RequestBody DinstBean dinstBean){
        try {
            dessService.saveJobInsDeatail(dinstBean);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新发生错误");
        }
        return Response.ok();
    }

    /**
     * 保存调度时间
     * @param dinstVO
     * @return
     */
    @RequestMapping(value = "/saveScheduling",method = RequestMethod.POST)
    public Response saveScheduling(@RequestBody DinstVO dinstVO){
        try {
            dessService.saveScheduling(dinstVO);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("保存发生错误");
        }
        return Response.ok();
    }

    /**
     * 保存业job所属的业务信息
     * @param dessBusiness
     * @return
     */
    @RequestMapping(value = "/saveJobDefinition",method = RequestMethod.POST)
    public Response saveJobDefinition(@RequestBody DessBusiness dessBusiness){
        if(StringUtil.isBlank(dessBusiness.getJobNo())){
            return Response.error("作业编号为空");
        }
        try {
            dessService.saveJobDefinition(dessBusiness);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("保存发生错误");
        }
        return Response.ok();
    }

    /**
     * 获取job所属的业务信息
     * @param dessBusiness
     * @return
     */
    @RequestMapping(value = "/getJobDefinition")
    public Response getJobDefinition(@RequestBody DessBusiness dessBusiness){
        DessBusiness result = new DessBusiness();
        try {
            result = dessService.getJobDefinition(dessBusiness.getJobNo());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取业务信息失败");
        }
        return Response.ok().setData(result);
    }

    /**
     * 获取调度时间定义
     * @param dinstVO
     * @return
     */
    @RequestMapping(value = "/getJobScheduling",method = RequestMethod.POST)
    public Response getJobScheduling(@RequestBody DinstVO dinstVO){
        try {
            DinstVO jobScheduling = dessService.getJobScheduling(dinstVO.getJobNo());
            return Response.ok().setData(jobScheduling);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取时间定义失败");
        }
    }
}
