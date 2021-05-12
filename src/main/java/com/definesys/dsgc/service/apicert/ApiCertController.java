package com.definesys.dsgc.service.apicert;

import com.definesys.dsgc.service.apicert.bean.CommonReqBean;
import com.definesys.dsgc.service.apicert.bean.DagCertRefIDbean;
import com.definesys.dsgc.service.apicert.bean.DagCertbean;
import com.definesys.dsgc.service.dagclient.CertificateDeployService;
import com.definesys.dsgc.service.dagclient.bean.DeployResBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName ApiCertController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-24 18:17
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "dsgc/apicert")
public class ApiCertController {

    @Autowired
    ApiCertService apiCertService;

    @Autowired
    private CertificateDeployService certificateDeployService;


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

    @RequestMapping(value = "/addApiCert",method = RequestMethod.POST)
    public Response addApiCert(@RequestBody DagCertbean dagCertbean, HttpServletRequest request){
        try {
            DeployResBean res = this.certificateDeployService.deployCertificate(null,dagCertbean.getEnvCode(),dagCertbean.getCertCnt(),dagCertbean.getCertKey(),dagCertbean.getCertSni());
            if(res != null && res.isSuccess()) {
                dagCertbean.setRefId(res.getRtnValue());
                apiCertService.addApiCert(dagCertbean);
            } else {
                if(res != null) {
                    return Response.error(res.getRtnMsg());
                } else{
                    return Response.error("新增证书失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增证书失败");
        }
        return Response.ok();
    }


    @RequestMapping(value = "/updateApiCert",method = RequestMethod.POST)
    public Response updateApiCert(@RequestBody DagCertbean dagCertbean, HttpServletRequest request){
        try {
            DagCertRefIDbean refIdBean = this.apiCertService.getRefIdByKeyId(dagCertbean.getDcId());
            if(refIdBean != null) {
                if(refIdBean.getEnvCode()!= null && refIdBean.getEnvCode().equals(dagCertbean.getEnvCode())) {
                    DeployResBean res = this.certificateDeployService.deployCertificate(refIdBean.getRefId(),dagCertbean.getEnvCode(),dagCertbean.getCertCnt(),dagCertbean.getCertKey(),dagCertbean.getCertSni());
                    if (res != null && res.isSuccess()) {
                        dagCertbean.setRefId(res.getRtnValue());
                        apiCertService.updateApiCert(dagCertbean);
                    } else {
                        if (res != null) {
                            return Response.error(res.getRtnMsg());
                        } else {
                            return Response.error("更新证书失败!");
                        }
                    }
                } else {
                    return Response.error("更新证书失败，不能变更环境！");
                }
            } else{
                return Response.error("更新证书失败，未找到对应的证书信息！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("更新证书失败");
        }
        return Response.ok();
    }

    @RequestMapping(value = "/delApiCert",method = RequestMethod.POST)
    public Response delApiCert(@RequestBody DagCertbean dagCertbean, HttpServletRequest request){
        try {
            DagCertRefIDbean refIdBean = this.apiCertService.getRefIdByKeyId(dagCertbean.getDcId());
            DeployResBean res = null;
            if(refIdBean != null && refIdBean.getRefId() != null){
                res = this.certificateDeployService.deleteCertificate(refIdBean.getRefId(),refIdBean.getEnvCode());
            }
            if(res == null || res != null && res.isSuccess()) {
                apiCertService.delApiCert(dagCertbean);
            }

            if(res != null && !res.isSuccess()){
                return Response.error(res.getRtnMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除证书失败");
        }
        return Response.ok();
    }

        //查询存在同名
    @RequestMapping(value = "/checkCertSameName",method = RequestMethod.POST)
    public Response checkCertSameName(@RequestBody CommonReqBean param, HttpServletRequest request){
        DagCertbean result=null;
        try {
            result=apiCertService.checkSameName(param.getCon0());
            if(result!=null){
                return Response.error("存在同名");
            }else{
                return Response.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("检测同名失败");
        }
    }


}
