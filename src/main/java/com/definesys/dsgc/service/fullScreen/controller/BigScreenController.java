package com.definesys.dsgc.service.fullScreen.controller;

import com.alibaba.fastjson.JSON;
import com.definesys.dsgc.service.fullScreen.service.BigScreenService;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qirenchao E-mail:3063973019@qq.com
 * @version 创建时间：2020/1/9 15:03
 * 中骏定制化
 */
@RequestMapping(value = "/dsgc/BigScreen")
@RestController
public class BigScreenController {

    @Autowired
    BigScreenService bigScreenService;

    /**
     * 服务发布
     */
    @RequestMapping(value = "/serviceAssets", method = RequestMethod.POST)
    public Object getServiceAssetsData() {
        return bigScreenService.getServiceAsset();
    }


    /**
     * 服务执行次数小时统计表（当天）
     */
    @RequestMapping(value = "/curDayExecuteTimes", method = RequestMethod.POST)
    public Response getCurDayExecuteTimesData()
    {
        return Response.ok().data(bigScreenService.getCurDayExecuteTimes());
    }


    /**
     * 服务执行成功次数top5(当天)
     */

    @RequestMapping(value = "/curDayInstanceSeTop", method = RequestMethod.POST)
    public Response getcurDayInstanceSeTopData() {
        return Response.ok().data(bigScreenService.getTotalTop5());
    }

    /**
     * 服务执行失败总次数top5统计表（当天）
     */
    @RequestMapping(value = "/curDayInstanceFailTop", method = RequestMethod.POST)
    public Response getCurDayInstanceFailTopData() {
        return Response.ok().data(bigScreenService.getFailTop5());
    }

    /**
     * 服务执行失败总次数top5统计表（当天）备份比
     */
    @RequestMapping(value = "/curDayInstanceFailProportionTop", method = RequestMethod.POST)
    public Response getcurDayInstanceFailProportionTop() {
        return Response.ok().data(bigScreenService.getFailTop5());
    }



    /**
     * 服务实例
     */
    @RequestMapping(value = "/serviceInstance", method = RequestMethod.POST)
    public Response getServiceInstanceData() {

        return Response.ok().data(bigScreenService.getServiceInstance());
    }

    /**
     * API 实时指标
     */
    @RequestMapping(value = "/quota", method = RequestMethod.POST)
    public Response getQuotaData()

    {
        return Response.ok().data(bigScreenService.getRealTime());
    }



    /**
     * 获取服务器数量及状态信息
     */
    @RequestMapping(value = "/getServerStatus", method = RequestMethod.POST)
    public Response getServerStatusData() {
        return Response.ok().data(bigScreenService.getServerStatus());
    }

    /**
     * 首页数据集合
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public Response Dashboard()

    {
        Map<String, Object> map = new HashMap<String, Object>();
        //服务分布
        map.put("serviceAssets",bigScreenService.getServiceAsset());
        //失败top5
        map.put("curDayInstanceFailTop",bigScreenService.getFailTop5());
        //成功top5
        map.put("curDayInstanceSeTop",bigScreenService.getTotalTop5());
        //失败top5百分比
        map.put("curDayInstanceFailProportionTop",bigScreenService.getFailTop5());
        //服务执行次数小时统计表
        map.put("curDayExecuteTimes",bigScreenService.getCurDayExecuteTimes());
        //服务实例
        map.put("serviceInstance",bigScreenService.getServiceInstance());
        //API实时指标
        map.put("quota",bigScreenService.getRealTime());
        //服务状态
        map.put("getServerStatus",bigScreenService.getServerStatus());

        return Response.ok().data(JSON.toJSONString(map));
    }

}
