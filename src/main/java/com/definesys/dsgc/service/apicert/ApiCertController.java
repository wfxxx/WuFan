package com.definesys.dsgc.service.apicert;

import com.definesys.dsgc.service.apicert.bean.CommonReqBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ApiCertController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-24 18:17
 * @Version 1.0
 **/
@RestController
public class ApiCertController {

    @Autowired
    ApiCertService apiCertService;


    @RequestMapping(value = "/queryApiCertList",method = RequestMethod.POST)
    public Response queryApiCertList(@RequestBody CommonReqBean param,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                     @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex, HttpServletRequest request){
        String userId = request.getHeader("uid");
        String userRole = request.getHeader("userRole");
        if ("Tourist".equals(userRole)){
            return Response.error("无权限操作");
        }
        PageQueryResult result;
        try {
            result = apiCertService.queryApiCertList(param,userId,userRole,pageSize,pageIndex);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取服务列表失败");
        }

        return Response.ok().setData(result);
    }

}
