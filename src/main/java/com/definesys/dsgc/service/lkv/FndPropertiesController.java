package com.definesys.dsgc.service.lkv;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.dsgc.service.lkv.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.log.SWordLogger;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

//@AuthAspect(menuCode = "fnd-properties",menuName = "属性值管理")
@RequestMapping(value = "/dsgc/fndProperties")
@RestController
public class FndPropertiesController {

    @Autowired
    FndPropertiesService fndPropertiesService;

    @Autowired
    private SWordLogger logger;

//    @OperationAspect(value = "属性值管理--新增属性值信息")
    @RequestMapping(value = "/createFndProperties", method = RequestMethod.POST)
    public Response createUser(@RequestBody FndProperties u) {
        String id = this.fndPropertiesService.createFndProperties(u);
        this.logger.debug("id " + id);
        return Response.ok().data(id);
    }

//    @OperationAspect(value = "属性值管理--新增或修改属性值信息")
    @RequestMapping(value = "/modifyFndProperties", method = RequestMethod.POST)
    public Response modifyUser(@RequestBody FndProperties u) {
        if(StringUtil.isBlank(u.getPropertyKey())
                || StringUtil.isBlank(u.getPropertyValue())
                || StringUtil.isBlank(u.getPropertyDescription()) ){
            return Response.error("保存属性信息时缺少必要参数！").setCode("error").setMessage("保存属性信息时缺少必要参数！");
        }



        String id = "";
        try{
            //保存属性信息前，对属性key进行校重
            boolean bool = fndPropertiesService.checkPropertiesKeyIsExist(u);
            if(bool){
                throw new MpaasBusinessException("属性key已存在，请修改后重试！");
            }

            if (StringUtil.isBlank(u.getPropertyId())) {
                id = this.fndPropertiesService.createFndProperties(u);
            } else {
                id = this.fndPropertiesService.updateFndProperties(u);
            }
        }catch (Exception ex) {
            logger.error("%s", "保存属性信息出错！");
            return Response.error("保存属性信息出错！").setCode("error").setMessage(ex.getMessage());
        }

        this.logger.debug("id " + id);
        return Response.ok().data(id).setMessage("保存属性信息成功！");
    }

    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Response query(@RequestBody FndProperties u,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                          HttpServletResponse response) {
        this.logger.debug("pageSize " + pageSize);
        this.logger.debug("pageIndex " + pageIndex);
        this.logger.debug("datas " + u);
        PageQueryResult<FndProperties> datas = this.fndPropertiesService.query(u, pageSize, pageIndex);
        this.logger.debug("getResult " + datas.getResult());
        return Response.ok().data(datas);
    }

    @RequestMapping(value = "/queryOr", method = {RequestMethod.POST, RequestMethod.GET})
    public Response queryOr(@RequestBody FndProperties u,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
                          HttpServletResponse response) {
        PageQueryResult<FndProperties> datas = this.fndPropertiesService.queryOr(u, pageSize, pageIndex);
        this.logger.debug("getResult " + datas.getResult());
        return Response.ok().data(datas);
    }

    @RequestMapping(value = "/findFndPropertiesById", method = RequestMethod.POST)
    public Response findUserById(@RequestBody FndProperties u) {
        FndProperties fndProperties = this.fndPropertiesService.findFndPropertiesById(u);
        this.logger.debug("fndProperties " + fndProperties.getPropertyId());
        return Response.ok().data(fndProperties);
    }

//    @OperationAspect(value = "属性值管理--删除属性值信息")
    @RequestMapping(value = "/deleteFndPropertiesById", method = RequestMethod.POST)
    public Response deleteUserById(@RequestBody FndProperties u) {
        this.logger.debug("uid " + u.getPropertyId());
        String uid = this.fndPropertiesService.deleteFndPropertiesById(u);
        this.logger.debug("uid " + uid);
        return Response.ok().data(uid);
    }

    @RequestMapping(value = "/findFndPropertiesByKey",method = RequestMethod.POST)
    public Response findFndPropertiesByListKey(@RequestBody String listKey){
        JSONObject object = JSONObject.parseObject(listKey);
        JSONArray keys = object.getJSONArray("propertiesKeys");
        String strKey= keys.toString().substring(1);
        String key = strKey.substring(0,strKey.indexOf("]")).replaceAll("\"","");
        String[] list = key.split(",");
        List<FndProperties> result = fndPropertiesService.findFndPropertiesByListKey(list);
        if (result.size()>0){
            return Response.ok().data(result);
        }
        return Response.error("该属性key不存在").setCode("error");
    }



    /*********** 2020-11-28 */


    @RequestMapping(value = "/queryProperties", method = {RequestMethod.POST, RequestMethod.POST})
    public Response queryProperties(@RequestBody QueryPropertyParamBean property) {
        List<QueryPropertyParamBean> dataList = this.fndPropertiesService.queryProperties(property);
        return Response.ok().data(dataList);
    }

}
