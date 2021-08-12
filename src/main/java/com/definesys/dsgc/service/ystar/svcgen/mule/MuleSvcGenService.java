package com.definesys.dsgc.service.ystar.svcgen.mule;

import com.definesys.dsgc.service.svcgen.bean.*;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.dsgc.service.ystar.mg.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.mg.svg.bean.MuleSvgCodeBean;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSaCode;
import com.definesys.dsgc.service.ystar.svcgen.mule.bean.MuleSvgCode;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;


/**
 * @ClassName: SvcGenConnService
 * @Description: SvcGenConnService
 * @Author：ystar
 * @Date : 2021/4/15 17:00
 */
@Service("MuleSvcGenService")
public class MuleSvcGenService {

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private MuleSvcGenDao muleSvcGenDao;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();

    /**
     * 生成服务代码
     *
     * @param loginUser
     * @param tcb
     * @return
     * @throws Exception
     */
    public Response saveMuleGenerateCode(String loginUser, TmplConfigBean tcb) throws Exception {
        System.out.println(tcb.toString());
        if ("0".equals(tcb.getTmplFlag())) {
            if (tcb.getSaForWsdl() != null && tcb.getSaForWsdl().trim().length() > 0) {
                SABean sa = this.muleSvcGenDao.getSaInfoBySaCode(tcb.getSaForWsdl());
                tcb.setWsdlUN(sa.getUn());
                tcb.setWsdlPD(sa.getPd());
            }
        }
        this.muleSvcGenDao.saveMuleGenerateCode(loginUser, tcb);
        return Response.ok().setMessage("写入成功！");
    }

    /*** 保存SA代码 ***/
    public Response saveMuleGenerateSACode(String loginUser, TmplConfigBean tcb) {
        if (!"51".equals(tcb.getTmplFlag())) {
            return Response.error("模板创建错误！");
        }
        MuleSaCode saCode = new MuleSaCode(tcb.getServNo(), tcb.getSaUN(), tcb.getSaPD(), tcb.getAppCode(), tcb.getToSystem(), tcb.getPrjName(), loginUser);
        this.muleSvcGenDao.saveMuleGenerateSACode(saCode);
        return Response.ok().setMessage("保存成功！");
    }

    public Response pageQueryMuleSvgSACode(SvcGenObjReqBean q, String uId, int pageSize, int pageIndex) {
        PageQueryResult<MuleSaCode> result = null;
        String status = q.getObjStat();
        String svgCode = q.getCon0();
        String sysCode = q.getCon0();
        String sysName = q.getCon0();
        String toSystem = q.getCon0();
        try {
            result = this.muleSvcGenDao.pageQueryMuleSvgSACode(status, svgCode, sysCode, sysName, toSystem, pageIndex, pageSize);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }
        return Response.ok().setMessage("查询成功！").data(result);
    }

    public Response resolveMuleSoapWsdl(WSDLResolveBean wsdl) throws Exception {
        String saCode = wsdl.getSaCode();
        if (saCode != null && saCode.trim().length() > 0) {
            SABean sa = this.muleSvcGenDao.getSaInfoBySaCode(saCode);
            //根据系统编号，默认设置下一步的提供方系统
            Response res = this.sgProxy.parseSpyWSDL(wsdl.getWsdlUri(), sa.getUn(), sa.getPd());
            if (Response.CODE_OK.equals(res.getCode())) {
                return res.setMessage(sa.getSystemCode());
            } else {
                return res;
            }
        }
        return this.sgProxy.parseSpyWSDL(wsdl.getWsdlUri(), null, null);
    }

    public Response pageQueryMuleSvgCode(CommonReqBean commonReqBean, int pageIndex, int pageSize) {
        PageQueryResult<MuleSvgCode> result = null;
        String svgCode = commonReqBean.getCon0();
        String sysCode = commonReqBean.getCon0();
        String sysName = commonReqBean.getCon0();

        try {
            result = muleSvcGenDao.pageQueryMuleSvgCode(pageIndex, pageSize, svgCode, sysCode, sysName);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }
        return Response.ok().setMessage("查询成功！").data(result);
    }


    private void initReadOnlyForSvcGenObjBean(SvcGenObjJsonBean ob, String uid) {
        UserHelper uh = this.userHelper.user(uid);
        ob.setReadonly(true);
        if (uid != null && ob != null) {
            if (uh.isAdmin() || uh.isSuperAdministrator()) {
                ob.setReadonly(false);
            } else {
                if ("Y".equals(ob.getEnabled())) {
                    if (ob.getServNo() == null) {
                        //如果还没有服务资产化，则应该为系统负责人且是资源创建人，或者更新人时可以编辑
                        if (uh.isSystemMaintainer() && (uid.equals(ob.getLastUpdatedBy())
                                || uid.equals(ob.getCreatedBy()) || uh.isSpecifySystemMaintainer(ob.getSysCode()))) {
                            ob.setReadonly(false);
                        }

                    } else {
                        if (uh.isSpecifySystemMaintainer(ob.getServSystem())) {
                            ob.setReadonly(false);
                        }
                    }
                } else {
                    //已经被废弃了，无法操作权限
                    ob.setReadonly(true);
                }
            }
        }
    }


}

