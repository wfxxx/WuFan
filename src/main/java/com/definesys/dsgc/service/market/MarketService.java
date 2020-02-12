package com.definesys.dsgc.service.market;

import com.definesys.dsgc.service.market.bean.MarketApiBean;
import com.definesys.dsgc.service.market.bean.MarketCateVO;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MarketService {
    @Autowired
    private MarketDao marketDao;

    public void addMarketCate(MarketCateVO marketcateVO) {
        marketDao.addMarketCate(marketcateVO);
    }

    public void deleteMarketCate(MarketCateVO marketcateVO) {
        marketDao.deleteMarketCate(marketcateVO);
    }

    public void updateMarketCate(MarketCateVO marketcateVO) {
        marketDao.updateMarketCate(marketcateVO);
    }

    public PageQueryResult<MarketCateVO> getAllMarketCate(SVCInfoQueryBean q,int pageSize, int pageIndex) {
        return marketDao.getAllMarketCate(q,pageSize,pageIndex);
    }

    public PageQueryResult<MarketApiBean> getAllMarketPub(int pageSize, int pageIndex) {
        return marketDao.getAllMarketPub(pageSize, pageIndex);
    }
}
