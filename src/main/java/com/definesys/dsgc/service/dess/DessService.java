package com.definesys.dsgc.service.dess;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dess.bean.DinstBean;
import com.definesys.dsgc.service.lkv.FndLookupTypeDao;
import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName DessService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 14:35
 * @Version 1.0
 **/
@Service
public class DessService {

    //服务调度程序的访问地址
    private  String dessServiceUrl;

    @Autowired
    private DessDao dessDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;

    @PostConstruct
    private void getDessServiceUrl(){
        FndProperties fndProperties =fndPropertiesService.findFndPropertiesByKey("DESS_SERVICE_URL");
        if(fndProperties!=null){
            dessServiceUrl=fndProperties.getPropertyValue();
        }else{
            dessServiceUrl="";
        }
        System.out.println(dessServiceUrl);
    }

    //插入，删除，更新调用下列方法，查询要紫泥做








    //添加任务 TODO
    public void addDessTask(HttpServletRequest request, DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/add",dinstBeanObject, request);
    }

    //删除任务
    public void delDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/delete",dinstBeanObject, request);
    }

    //暂停任务
    public void pauseDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/pause",dinstBeanObject, request);
    }

    //恢复暂停任务
    public void startDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/start",dinstBeanObject, request);
    }

    //更新任务
    public void UpdateDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/update",dinstBeanObject, request);
    }

}
