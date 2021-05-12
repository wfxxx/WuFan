package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.*;
import com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.utils.YStarUtil;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository("SVCInfoDao")
@Transactional
public class SVCInfoDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<SVCInfoListBean> querySvcInfoListByCon(SVCInfoQueryBean q, int pageSize, int pageIndex) {
        MpaasQuery query = sw.buildQuery().sql(SVCInfoListBean.QUERY_LIST_SQL);
        if (q.getShareType() != null && q.getShareType().trim().length() > 0) {
            if (!"ALL".equals(q.getShareType().trim())) {
                String shareTypNoSpace = q.getShareType().trim();
                query.and().eq("sst_meaning", shareTypNoSpace);
            }
        }
        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String con : conArray) {
                if (con != null && con.trim().length() > 0) {
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("SERV_NO", conNoSpace)
                            .likeNocase("serv_name", conNoSpace)
                            .likeNocase("ss_meaning", conNoSpace)
                            .likeNocase("sst_meaning", conNoSpace);
                }
            }
        }

        return query.doPageQuery(pageIndex, pageSize, SVCInfoListBean.class);
    }


    public List<SVCInfoVO> queryService(SVCInfoQueryBean svcInfoQueryBean) {
        String svcCode = svcInfoQueryBean.getSvcCode();
        String svcName = svcInfoQueryBean.getSvcName();
        List<String> svcCodeList = svcInfoQueryBean.getSvcCodeList();
        String sysCode = svcInfoQueryBean.getSysCode();
        List<String> sysCodeList = svcInfoQueryBean.getSysCodeList();
        String compCode = svcInfoQueryBean.getCompCode();
        List<String> compCodeList = svcInfoQueryBean.getCompCodeList();
        MpaasQuery mq = sw.buildViewQuery("SVC_INFO_V");
        if (StringUtil.isNotBlank(svcCode)) {
            mq.like("svcCode", svcCode);
        }
        if (StringUtil.isNotBlank(svcName)) {
            mq.like("svcName", svcName);
        }
        if (svcCodeList != null && svcCodeList.size() > 0) {
            mq.in("svcCode", svcCodeList);
        }
        if (StringUtil.isNotBlank(sysCode) && !"ALL".equals(sysCode)) {
            mq.like("sysCode", sysCode);
        }
        if (sysCodeList != null) {
            mq.in("sysCode", sysCodeList);
        }
        if (StringUtil.isNotBlank(compCode) && !"ALL".equals(compCode)) {
            mq.like("compCode", compCode);
        }
        if (compCodeList != null) {
            mq.in("compCode", compCodeList);
        }
        return mq.orderBy("svcCode", "ASC")
                .doQuery(SVCInfoVO.class);
    }

    /*** 获取接口数量** @return*/
    public Object getSvcCount() {
        List<Map<String, Object>> result = sw.buildQuery()
                .sql("select count(*) from dsgc_SERVICES")
                .doQuery();
        return result.get(0).get("COUNT(*)");
    }

    /*** 查询服务uri信息** @param servicesUri * @return*/
    public List<DSGCServicesUri> queryAllSvcUri() {
        return this.sw.buildQuery().doQuery(DSGCServicesUri.class);
    }

    /*** 查询服务方法不为空的服务资产信息** @param servicesUri * @return*/
    public List<SVCInfoBean> querySvcWithSpyOprInfo() {
        return this.sw.buildQuery().isNotNull("spyOper").doQuery(SVCInfoBean.class);
    }

    /*** 根据服务资产编号 查询服务信息**/
    public SVCInfoBean querySvcInfoBySvcNo(String svcNo) {
        return this.sw.buildQuery()
                .eq("servNo", svcNo)
                .doQueryFirst(SVCInfoBean.class);
    }

    /*** 根据服务资产ID 更新服务url**/
    public void updSvcUri(SVCInfoBean svc) {
        sw.buildQuery()
                .eq("serv_id", svc.getServId())
                .doUpdate(svc);
    }

    public List<QuerySvcParamBean> querySvcList(QuerySvcParamBean svcParamBean) {
        String querySql = "select s.serv_no svcCode,s.serv_name svcName,s.subordinate_system sysCode from dsgc_services s where 1=1 ";
        String svcCode = svcParamBean.getSvcCode();
        String sysCode = svcParamBean.getSysCode();
        if (StringUtils.isEmpty(svcCode)) {
            return null;
        } else if (!"ALL".equals(svcCode.toUpperCase())) {
            querySql += " and s.serv_no in (" + YStarUtil.getSqlWhereClause(svcCode) + ")";
        }
        if (StringUtils.isEmpty(sysCode)) {
            return null;
        }
        if (!"ALL".equals(sysCode.toUpperCase())) {
            querySql += " and s.subordinate_system in (" + YStarUtil.getSqlWhereClause(sysCode) + ")";
        }
        //新增排序
        querySql += " order by s.serv_no asc ";
        return this.sw.buildQuery()
                .sql(querySql)
                .doQuery(QuerySvcParamBean.class);
    }


    /*** ystar 20210310 */

    public SvcBsInfoBean querySvcBsInfoByCode(String svcCode, String sysCode) {
        return this.sw.buildQuery()
                .eq("SVC_CODE", svcCode)
                .eq("SYS_CODE", sysCode)
                .doQueryFirst(SvcBsInfoBean.class);
    }


    public String addSvcBsInfo(SvcBsInfoBean bsInfoBean) {
        this.sw.buildQuery()
                .doInsert(bsInfoBean);
        return bsInfoBean.getBsId();
    }

    public String delSvcBsInfo(SvcBsInfoBean bsInfoBean) {
        this.sw.buildQuery()
                .eq("BS_CODE", bsInfoBean.getBsCode())
                .doDelete(bsInfoBean);
        return bsInfoBean.getBsId();
    }

    public void updSvcBsInfo(SvcBsInfoBean bsInfoBean) {
        this.sw.buildQuery()
                .eq("BS_CODE", bsInfoBean.getBsCode())
                .doInsert(bsInfoBean);
    }

}
