package com.definesys.dsgc.service.ystar.mg.svg;

import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.dsgc.service.ystar.mg.bean.CommonReqBean;
import com.definesys.dsgc.service.ystar.mg.prj.MulePrjDao;
import com.definesys.dsgc.service.ystar.mg.prj.bean.MulePrjInfoBean;
import com.definesys.dsgc.service.ystar.mg.svc.SvcInfoDao;
import com.definesys.dsgc.service.ystar.mg.svg.bean.MuleSvgCodeBean;
import com.definesys.dsgc.service.ystar.mg.util.MuleSvgUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MuleSvgCodeService {

    @Autowired
    private MuleSvgCodeDao svgCodeDao;
    @Autowired
    private MulePrjDao prjDao;
    @Autowired
    private FndPropertiesService propertiesService;

    public Response pageQueryMuleSvgCode(CommonReqBean commonReqBean, int pageIndex, int pageSize) {
        PageQueryResult<MuleSvgCodeBean> result = null;
        String svgCode = commonReqBean.getCon0();
        String svgName = commonReqBean.getCon0();
        String sysCode = commonReqBean.getCon0();
        String sysName = commonReqBean.getCon0();

        try {
            result = svgCodeDao.pageQueryMuleSvgCode(pageIndex, pageSize, svgCode, svgName, sysCode, sysName);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }
        return Response.ok().setMessage("查询成功！").data(result);
    }

    public Response listQueryMuleSvgCode(MuleSvgCodeBean prjInfoBean) {
        List<MuleSvgCodeBean> result = null;
        try {
            result = svgCodeDao.listQueryMuleSvgCode(prjInfoBean);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response sigQueryMuleSvgCodeById(MuleSvgCodeBean bean) {
        MuleSvgCodeBean result = null;
        try {
            String id = bean.getMscId();
            if (StringUtil.isBlank(id)) {
                return Response.error("查询失败！ID不存在！");
            }
            result = svgCodeDao.sigQueryMuleSvgCodeById(id);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response sigQueryMuleSvgCodeByCode(MuleSvgCodeBean bean) {
        MuleSvgCodeBean result = null;
        try {
            String code = bean.getSvgCode();
            if (StringUtil.isBlank(code)) {
                return Response.error("查询失败！code不存在！");
            }
            result = svgCodeDao.sigQueryMuleSvgCodeByCode(code);
        } catch (Exception e) {
            return Response.error("查询失败！").data(e);
        }

        return Response.ok().data(result);
    }

    public Response checkMuleSvgCodeExist(String code) {
        MuleSvgCodeBean svgCodeBean = svgCodeDao.sigQueryMuleSvgCodeByCode(code);
        if (svgCodeBean == null) {
            return Response.ok().data(false);
        } else {
            return Response.ok().data(true);
        }
    }

    public Response checkMuleSvgNameExist(String name) {
        MuleSvgCodeBean svgCodeBean = svgCodeDao.sigQueryMuleSvgCodeByName(name);
        if (svgCodeBean == null) {
            return Response.ok().data(false);
        } else {
            return Response.ok().data(true);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Response saveMuleSvgCode(MuleSvgCodeBean bean) {
        String mscId = bean.getMscId();
        try {
            if (StringUtil.isBlank(mscId)) {
                svgCodeDao.addMuleSvgCode(bean);
            } else {
                svgCodeDao.updMuleSvgCode(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("保存失败！").data(e.getMessage());
        }
        return Response.ok().setMessage("保存成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public Response delMuleSvgCodeById(String id) {
        try {
            if (StringUtil.isNotBlank(id)) {
                svgCodeDao.delMuleSvgCodeById(id);
                return Response.ok().setMessage("删除成功！");
            } else {
                return Response.ok().setMessage("删除失败！ID不存在！");
            }
        } catch (Exception e) {
            return Response.error("发生异常").setMessage(e.getMessage());
        }
    }

    public Response appendOrRemoveMuleCode(String id, boolean removeFlag) {
        MuleSvgCodeBean svgCodeBean = this.svgCodeDao.sigQueryMuleSvgCodeById(id);
        MulePrjInfoBean prjInfoBean = this.prjDao.sigQueryMulePrjById(svgCodeBean.getPrjId());
        //获取本地仓库地址
        String repoHome = propertiesService.getLocalHome("repo");
        try {
            if (StringUtil.isNotBlank(id) && prjInfoBean != null) {
                MuleSvgUtil.appendOrRemoveMuleCode(removeFlag, repoHome, prjInfoBean, svgCodeBean);
                //更新状态
                this.svgCodeDao.updMuleSvgCodeStatus(svgCodeBean.getSvgCode(), removeFlag ? "0" : "1");
                return Response.ok().setMessage("代码" + (removeFlag ? "撤销" : "写入") + "成功！");
            } else {
                return Response.ok().setMessage("代码" + (removeFlag ? "撤销" : "写入") + "失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("发生异常").setMessage(e.getMessage());
        }
    }


}
