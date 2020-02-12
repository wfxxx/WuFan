package com.definesys.dsgc.service.svcgen;

import com.definesys.dsgc.service.svcgen.bean.CustomSettingJsonBean;
import com.definesys.dsgc.service.svcgen.bean.IdeDeployProfileBean;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IDEStepsService {

    @Autowired
    private UserHelper userHelper;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();

    /**
     * 生成部署客户化设置文件内容
     *
     * @param servNo
     * @return
     * @throws Exception
     */
    public CustomSettingJsonBean generateIDECustomSettingCnt(String servNo) throws Exception {
        return this.sgProxy.generateIDECustomSettingCnt(servNo);
    }


    /**
     * 新增rfc部署配置项
     *
     * @param uid
     * @param ideDpl
     * @return
     */
    public Response newIdeDeployProfile(String uid,IdeDeployProfileBean ideDpl) {
        UserHelper uh = this.userHelper.user(uid);
        boolean isEditor = uh.isSvcGenEditorByServNo(ideDpl.getServNo());
        if (!isEditor) {
            return Response.error("您无权限执行此操作！");
        } else {
            try {
                String dplCustEnCode = ideDpl.getDplCust();
                if (dplCustEnCode != null && dplCustEnCode.trim().length() > 0) {
                    //String dplCust = new String(Base64.getDecoder().decode(ideDpl.getDplCust()),Charset.forName("UTF-8"));
                    String dpId = this.sgProxy.newIdeDeployProfile(uid,ideDpl.getServNo(),ideDpl.getDplName(),ideDpl.getEnvCode(),dplCustEnCode);
                    return Response.ok().setData(dpId).setMessage("新增成功！");
                } else {
                    return Response.error("部署替换项内容不能为空！");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return Response.error("执行出错，请联系管理员处理！");
            }
        }
    }



}
