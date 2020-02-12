package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketCateVO;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.definesys.mpaas.query.MpaasQuery;


@Repository
@Transactional
public class MarketDao {
    @Autowired
    private MpaasQueryFactory sw;

    public void addMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .doInsert(marketcateVO);
    }

    public void deleteMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .eq("mcId", marketcateVO.getMcId())
                .doDelete(marketcateVO);
    }

    public void updateMarketCate(MarketCateVO marketcateVO) {
        sw.buildQuery()
                .eq("mcId", marketcateVO.getMcId())
                .doUpdate(marketcateVO);
    }

    public PageQueryResult<MarketCateVO> getAllMarketCate(SVCInfoQueryBean q,int pageSize, int pageIndex) {
        MpaasQuery query = sw.buildQuery().sql("select * from DSGC_MARKET_CATEGORY");
        if (q.getCon0() != null && q.getCon0().trim().length() > 0) {
            String[] conArray = q.getCon0().trim().split(" ");
            for (String con : conArray) {
                if (con != null && con.trim().length() > 0) {
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("cateName", conNoSpace)
                            .likeNocase("cateOrder", conNoSpace)
                            .likeNocase("parentCate", conNoSpace);
                }
            }
        }
        return query.doPageQuery(pageIndex,pageSize,MarketCateVO.class);
    }

    public PageQueryResult<MarketApiBean> getAllMarketPub(int pageSize, int pageIndex) {
        return sw.buildQuery()
                .sql("select api.API_ID,api.API_NAME,api.API_DESC,api.APP_CODE,api.MARKET_CATEGORY,api.MARKET_STAT,'API_CATE' type from DSGC_APIS api UNION ALL\n" +
                        "select serv.SERV_ID,serv.SERV_NAME,serv.SERV_DESC,serv.SUBORDINATE_SYSTEM ,serv.MARKET_CATEGORY ,serv.MARKET_STAT,'SVC_CATE' type from DSGC_SERVICES serv")
                .doPageQuery(pageIndex, pageSize, MarketApiBean.class);
    }
}
