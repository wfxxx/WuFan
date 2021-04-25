package com.definesys.dsgc.service.flow.road;

import com.definesys.dsgc.service.flow.dto.*;
import com.definesys.dsgc.service.flow.mng.FlowSvcService;
import com.definesys.dsgc.service.svcgen.SVCGenService;
import com.definesys.dsgc.service.svcgen.bean.DBDeployProfileBean;
import com.definesys.dsgc.service.svcgen.bean.DeployProfileJsonBean;
import com.definesys.dsgc.service.svcgen.bean.DeployServiceJsonBean;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/dsgc/flow/road")
public class FlowRoadController {

    @Autowired
    private FlowRoadService flowRoadService;

    @Autowired
    private FlowSvcService flowSvcService;

    @Autowired
    private SVCGenService svcGenService;

    /**
     * 获取flow路径图
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFlowRoad", method = RequestMethod.POST)
    public Response getFlowRoad(@RequestBody FlowRoadQueryDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_PARAMS);
        }
        FlowRoadDTO res = this.flowRoadService.getFlowRoad(param);

        if (res == null) {
            return Response.error("对象不存在！");
        }

        return Response.ok().setData(res);
    }

    /**
     * 开启编辑模式
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/startEditting", method = RequestMethod.POST)
    public Response startFlowRoadEditting(@RequestBody FlowRoadEditReqDTO param) {
        if(param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_PARAMS);
        }

        if(!this.flowSvcService.isAuthToEditFlow(param.getFlowId())){
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_AUTH);
        }

        String res = this.flowRoadService.startFlowRoadEdit(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    /**
     * 放弃编辑
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/backoffEditting", method = RequestMethod.POST)
    public Response backoffFlowRoadEditting(@RequestBody FlowRoadEditReqDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_PARAMS);
        }

        if (!this.flowSvcService.isAuthToEditFlow(param.getFlowId())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_AUTH);
        }

        String res = this.flowRoadService.backoffFlowRoadEdited(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    /**
     * 保存编辑
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Response saveFlowRoad(@RequestBody FlowRoadDTO param) {
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_PARAMS);
        }

        if (!this.flowSvcService.isAuthToEditFlow(param.getFlowId())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_AUTH);
        }
        String res = this.flowRoadService.saveFlowRoadWithCloseEditing(param);
        if ("Y".equals(res)) {
            return Response.ok();
        } else {
            return Response.error(res);
        }
    }

    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    public Response deployFlowRoad(@RequestBody FlowDeployDTO param) throws Exception{
        if (param == null || StringUtils.isBlank(param.getFlowId()) || StringUtils.isBlank(param.getFlowVersion())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_PARAMS);
        }

        if (!this.flowSvcService.isAuthToEditFlow(param.getFlowId())) {
            return Response.error(FlowConstants.FLOW_TIPS_INVAILD_AUTH);
        }

        TmplConfigBean tc = this.flowRoadService.getSvcGenConfig(param.getFlowId(),param.getFlowVersion());

        if(tc == null){
            return Response.ok();
        }

        if(StringUtils.isNotBlank(tc.getErroMsg())){
            return Response.error(tc.getErroMsg());
        }


        //生成接口代码
        Response res = this.svcGenService.generateServiceCode(MpaasSession.getCurrentUser(),tc);

        if(!Response.CODE_OK.equals(res.getCode())){
            return res;
        }

        //生成部署文件
        List<DeployProfileJsonBean> dpList = this.svcGenService.getServDeployProfileList(MpaasSession.getCurrentUser(),param.getFlowId()+"-"+param.getFlowVersion());

        if(dpList == null || dpList.isEmpty()){
            //创建部署文件
            DBDeployProfileBean dpf = new DBDeployProfileBean();
            dpf.setDplName("演示环境部署文件");
            dpf.setEnvCode("ESB_DEV");
            dpf.setJndiName("eis/DB/DSGCDS");
            dpf.setServNo(param.getFlowId()+"-"+param.getFlowVersion());
            Response newDPRes = this.svcGenService.newDBDeployProfile(MpaasSession.getCurrentUser(),dpf);
            if(!Response.CODE_OK.equals(newDPRes.getCode())){
                return newDPRes;
            }
            dpList = this.svcGenService.getServDeployProfileList(MpaasSession.getCurrentUser(),param.getFlowId()+"-"+param.getFlowVersion());
        }

        //部署接口
        DeployProfileJsonBean dp = dpList.get(0);

        DeployServiceJsonBean  deployBean = new DeployServiceJsonBean();
        if(StringUtils.isNotBlank(param.getDeployDesc())) {
            deployBean.setDeployDesc(param.getDeployDesc());
        } else {
            deployBean.setDeployDesc("发布集成流"+System.currentTimeMillis());
        }
        deployBean.setDpId(dp.getDpId());
        deployBean.setServNo(param.getFlowId()+"-"+param.getFlowVersion());
        Response deployRes = this.svcGenService.deployServcie(MpaasSession.getCurrentUser(),deployBean);

        return deployRes;

    }


}
