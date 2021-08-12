package com.definesys.dsgc.service.ystar.mg.svg;

import com.definesys.dsgc.service.ystar.mg.svg.bean.MuleSvgCodeBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;

import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MuleSvgCodeDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<MuleSvgCodeBean> pageQueryMuleSvgCode(int pageIndex, int pageSize, String svgCode, String svgName, String sysCode, String sysName) {
        MpaasQuery mq = sw.buildViewQuery("V_MULE_SVG_CODE").or();
        if (StringUtil.isNotBlank(svgCode)) {
            mq.like("svgCode", svgCode);
        }
        if (StringUtil.isNotBlank(svgName)) {
            mq.like("svgName", svgName);
        }
        if (StringUtil.isNotBlank(sysCode)) {
            mq.like("sysCode", sysCode);
        }
        if (StringUtil.isNotBlank(sysName)) {
            mq.like("sysName", sysName);
        }

        return mq.doPageQuery(pageIndex, pageSize, MuleSvgCodeBean.class);
    }

    public List<MuleSvgCodeBean> listQueryMuleSvgCode(MuleSvgCodeBean svgCodeBean) {
        return sw.buildViewQuery("V_MULE_SVG_CODE")
                .eq("svgCode", svgCodeBean.getSvgCode())
                .eq("svgName", svgCodeBean.getSvgName())
                .doQuery(MuleSvgCodeBean.class);
    }

    public MuleSvgCodeBean sigQueryMuleSvgCodeById(String id) {
        return sw.buildQuery()
                .eq("mscId", id)
                .doQueryFirst(MuleSvgCodeBean.class);
    }

    public MuleSvgCodeBean sigQueryMuleSvgCodeByCode(String code) {
        return sw.buildQuery()
                .eq("svgCode", code)
                .doQueryFirst(MuleSvgCodeBean.class);
    }

    public MuleSvgCodeBean sigQueryMuleSvgCodeByName(String name) {
        return sw.buildQuery()
                .eq("svgName", name)
                .doQueryFirst(MuleSvgCodeBean.class);
    }

    public void addMuleSvgCode(MuleSvgCodeBean svgCodeBean) {
        this.sw.buildQuery().doInsert(svgCodeBean);
    }


    public void updMuleSvgCode(MuleSvgCodeBean svgCodeBean) {
        sw.buildQuery()
                .eq("mscId", svgCodeBean.getMscId())
                .update("svgName",svgCodeBean.getSvgName())
                .update("sysCode",svgCodeBean.getSysCode())
                .update("toSystem",svgCodeBean.getToSystem())
                .update("svgDesc",svgCodeBean.getSvgDesc())
                .doUpdate(svgCodeBean);
    }

    public void updMuleSvgCodeStatus(String svgCode, String status) {
        sw.buildQuery()
                .eq("svgCode", svgCode)
                .update("SVG_STATUS", status)
                .doUpdate(MuleSvgCodeBean.class);
    }


    public void delMuleSvgCodeById(String id) {
        sw.buildQuery()
                .eq("mscId", id)
                .doDelete(MuleSvgCodeBean.class);
    }

}
