package com.definesys.dsgc.service.ystar.mg.prj;

import com.definesys.dsgc.service.ystar.mg.prj.bean.MulePrjInfoBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: ystar
 * @since: 2021/7/9 上午10:49
 * @history: 1.2021/7/9 created by ystar
 */
@Repository
public class MulePrjDao {

    @Autowired
    private MpaasQueryFactory sw;


    public PageQueryResult<MulePrjInfoBean> pageQueryMulePrjInfo(int pageIndex, int pageSize, String prjName, String prjDesc) {
        MpaasQuery mq = sw.buildViewQuery("V_MULE_PRJ_INFO").or();
        if (StringUtil.isNotBlank(prjName)) {
            mq.like("prjName", prjName);
        }
        if (StringUtil.isNotBlank(prjDesc)) {
            mq.like("prjDesc", prjDesc);
        }
        return mq.doPageQuery(pageIndex, pageSize, MulePrjInfoBean.class);
    }

    public List<MulePrjInfoBean> listQueryMulePrjInfo(MulePrjInfoBean prjInfoBean) {
        return sw.buildViewQuery("V_MULE_PRJ_INFO")
                .eq("prjId", prjInfoBean.getPrjId())
                .eq("prjName", prjInfoBean.getPrjName())
                .doQuery(MulePrjInfoBean.class);
    }

    public MulePrjInfoBean sigQueryMulePrjById(String prjId) {
        return sw.buildQuery()
                .eq("prjId", prjId)
                .doQueryFirst(MulePrjInfoBean.class);
    }

    public MulePrjInfoBean sigQueryMulePrjInfoByName(String prjName) {
        return sw.buildQuery()
                .eq("prjName", prjName)
                .doQueryFirst(MulePrjInfoBean.class);
    }

    public void addMulePrjInfo(MulePrjInfoBean prjInfoBean) {
        this.sw.buildQuery().doInsert(prjInfoBean);
    }


    public void updMulePrjInfo(MulePrjInfoBean prjInfoBean) {
        sw.buildQuery()
                .eq("prjId", prjInfoBean.getPrjId())
                .doUpdate(prjInfoBean);
    }

    public void updMulePrjStatus(String prjId,String status) {
        sw.buildQuery()
                .eq("prjId", prjId)
                .update("PRJ_STATUS",status)
                .doUpdate(MulePrjInfoBean.class);
    }

    public void updMulePrjInfoVersion(String prjId, String version) {
        sw.buildQuery()
                .eq("prjId", prjId)
                .update("CUR_VERSION", version)
                .doUpdate(MulePrjInfoBean.class);
    }


    public void delMulePrjInfoById(String prjId) {
        sw.buildQuery()
                .eq("prjId", prjId)
                .doDelete(MulePrjInfoBean.class);
    }

}
