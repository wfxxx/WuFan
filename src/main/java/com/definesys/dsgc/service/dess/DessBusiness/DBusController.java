package com.definesys.dsgc.service.dess.DessBusiness;

import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.dess.DessInstance.DInsService;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
            dBusService.saveJobDefinition(dessBusiness);
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
            result = dBusService.getJobDefinition(dessBusiness.getJobNo());
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取业务信息失败");
        }
        return Response.ok().setData(result);
    }
}
