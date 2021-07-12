package com.definesys.dsgc.service.lkv;

//import com.definesys.dsgc.aspect.annotation.AuthAspect;
//import com.definesys.dsgc.aspect.annotation.OperationAspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.lkv.bean.FndLookupType;
import com.definesys.dsgc.service.lkv.bean.FndLookupValue;
import com.definesys.dsgc.service.lkv.FndLookupTypeService;
import com.definesys.dsgc.service.lkv.bean.QueryLktParamBean;
import com.definesys.dsgc.service.utils.CommonUtils;
import com.definesys.mpaas.common.exception.MpaasBusinessException;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@AuthAspect(menuCode = "look-value",menuName = "值列表管理")
@RequestMapping(value = "/dsgc/fndLookupType")
@RestController
public class FndLookupTypeController {

    @Autowired
    FndLookupTypeService fndLookupTypeService;

    @RequestMapping(value = "/findFndLookupTypeById", method = RequestMethod.POST)
    public Response findFndLookupTypeById(@RequestBody FndLookupType u) {
        FndLookupType fndModules = this.fndLookupTypeService.getFndLookupTypeByTypeId(u);
        return Response.ok().data(fndModules);
    }

    @RequestMapping(value = "/findFndLookupTypeByType", method = RequestMethod.POST)
    public Response findFndLookupTypeByType(@RequestBody FndLookupType u) {
        FndLookupType fndModules = this.fndLookupTypeService.findFndLookupTypeByType(u);
        return Response.ok().data(fndModules);
    }

    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Response query(@RequestBody FndLookupType u,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex) {
        PageQueryResult<Map<String, Object>> list = this.fndLookupTypeService.query(u, pageSize, pageIndex);
        return Response.ok().data(list);
    }

    @RequestMapping(value = "/pageQueryFndLkv", method = {RequestMethod.POST, RequestMethod.GET})
    public Response queryOr(@RequestBody FndLookupType u,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex) {
        PageQueryResult<FndLookupType> list = this.fndLookupTypeService.pageQueryFndLkv(u, pageSize, pageIndex);
        return Response.ok().data(list);
    }

    //    @OperationAspect(value = "值列表管理--删除值列表类型信息")
    @RequestMapping(value = "/deleteFndLookupType", method = RequestMethod.POST)
    public Response deleteFndLookupType(@RequestBody FndLookupType u) {
        String id = this.fndLookupTypeService.deleteFndLookupType(u);
        return Response.ok().data(id);
    }

    /**
     * 批量删除
     *
     * @param list 所需删除的id
     * @return
     */
//    @OperationAspect(value = "值列表管理--批量删除值列表类型信息")
    @RequestMapping(value = "/delFndLookupTypes", method = RequestMethod.POST)
    public Response delFndLookupTypes(@RequestBody List<FndLookupType> list) {
        List<String> stringList = this.fndLookupTypeService.delFndLookupTypes(list);
        return Response.ok().data(stringList);
    }

    //    @OperationAspect(value = "值列表管理--修改值列表类型信息")
    @RequestMapping(value = "/updateFndLookupType", method = RequestMethod.POST)
    public Response updateFndLookupType(@RequestBody FndLookupType u) {
        String id = this.fndLookupTypeService.updateFndLookupType(u);
        return Response.ok().data(id);
    }

    //    @OperationAspect(value = "值列表管理--新增值列表类型信息")
    @RequestMapping(value = "/addFndLookupType", method = RequestMethod.POST)
    public Response addFndLookupType(@RequestBody FndLookupType u) {
        String id = this.fndLookupTypeService.addFndLookupType(u);
        return Response.ok().data(id);
    }

    //    @OperationAspect(value = "值列表管理--修改值列表Type与值列表Value信息")
    @RequestMapping(value = "/modifyFndLookupType", method = RequestMethod.POST)
    public Response modifyUser(HttpServletRequest request) {
        String body = CommonUtils.charReader(request);
        if ("".equals(body)) {
            throw new MpaasBusinessException("请求数据为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject lookupType = jsonObject.getJSONObject("lookupType");
        JSONArray lookupValue = jsonObject.getJSONArray("lookupValue");
        FndLookupType u = new FndLookupType();
        u.setLookupType(lookupType.getString("lookupType"));
        u.setLookupName(lookupType.getString("lookupName"));
        u.setLookupId(lookupType.getString("lookupId"));
        u.setLookupDescription(lookupType.getString("lookupDescription"));
        u.setModuleId(lookupType.getString("moduleId"));

        if (lookupValue != null && lookupValue.size() > 0) {
            List<FndLookupValue> values = new ArrayList<>();
            for (int i = 0; i < lookupValue.size(); i++) {
                JSONObject jo = lookupValue.getJSONObject(i);
                FndLookupValue va = new FndLookupValue();
                va.setLookupCode(jo.getString("lookupCode"));
                va.setMeaning(jo.getString("meaning"));
                va.setDescription(jo.getString("description"));
//                va.setDisplaySequence(jo.getInt("displaySequence"));
//                va.setEnabledFlag(jo.getString("enabledFlag"));
//                va.setStartDateActive(jo.getString("startDateActive"));
//                va.setEndDateActive(jo.getString("endDateActive"));
                va.setTag(jo.getString("tag"));
                values.add(va);
            }
            u.setValues(values);
        }

        String id = "";
        if (u.getLookupId() == null || u.getLookupId().trim().length() == 0) {
            id = this.fndLookupTypeService.addFndLookupType(u);
        } else {
            id = this.fndLookupTypeService.updateFndLookupType(u);
        }
        return Response.ok().data(id);
    }

    /**
     * 值列表类型校重接口
     *
     * @param lookupType
     * @return
     */
    @RequestMapping(value = "/checkLookUpType", method = RequestMethod.POST)
    public Response checkLookUpType(@RequestBody FndLookupType lookupType) {
        boolean bool = fndLookupTypeService.checkLookUpType(lookupType);

        if (bool) {
            return Response.error("值列表类型已存在，请修改后重试！");
        }
        return Response.ok().setMessage("值可以使用！");
    }

    @RequestMapping(value = "/updateLookupValue", method = RequestMethod.POST)
    public Response updateLookupValue(@RequestBody FndLookupType fndLookupType) {
        fndLookupTypeService.margeLookUpValue(fndLookupType);
        return Response.ok();
    }

    /**
     * 通过值列表类型Code查询值列表
     *
     * @param lookupTypes
     * @return
     */
    @RequestMapping(value = "/getLookupValuesByTypeList", method = RequestMethod.POST)
    public Response getLookupValuesByTypeList(@RequestBody List<FndLookupType> lookupTypes) {
        System.out.println("list->" + lookupTypes);
        return Response.ok().data(fndLookupTypeService.getLookupValuesByTypeList(lookupTypes));
    }


    /**
     * 根据传入type 查找子项
     *
     * @param lkvParamBean
     * @return
     */
    @RequestMapping(value = "/queryLkvList", method = RequestMethod.POST)
    public Response queryLkvList(@RequestBody QueryLktParamBean lkvParamBean) {
        return Response.ok().data(fndLookupTypeService.queryLkvList(lkvParamBean));
    }
}
