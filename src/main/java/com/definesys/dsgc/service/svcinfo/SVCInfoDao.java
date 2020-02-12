package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.SVCInfoListBean;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.mpaas.query.MpaasQuery;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SVCInfoDao {
    @Autowired
    private MpaasQueryFactory sw;

    public PageQueryResult<SVCInfoListBean> querySvcInfoListByCon(SVCInfoQueryBean q,int pageSize,int pageIndex){
        MpaasQuery query =  sw.buildQuery().sql(SVCInfoListBean.QUERY_LIST_SQL);
        if(q.getShareType() != null && q.getShareType().trim().length() >0){
            if(!"ALL".equals(q.getShareType().trim())){
                String shareTypNoSpace = q.getShareType().trim();
                query.and().eq("sst_meaning",shareTypNoSpace);
            }
        }
        if(q.getCon0() != null && q.getCon0().trim().length() >0){
            String[] conArray = q.getCon0().trim().split(" ");
            for(String con:conArray){
                if(con != null && con.trim().length()>0){
                    String conNoSpace = con.trim();
                    query.conjuctionAnd().or()
                            .likeNocase("SERV_NO",conNoSpace)
                            .likeNocase("serv_name",conNoSpace)
                            .likeNocase("ss_meaning",conNoSpace)
                            .likeNocase("sst_meaning",conNoSpace);
                }
            }
        }

        return query.doPageQuery(pageIndex,pageSize,SVCInfoListBean.class);
    }
}
