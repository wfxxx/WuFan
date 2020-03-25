package com.definesys.dsgc.service.apicert;


import com.definesys.dsgc.service.apicert.bean.CommonReqBean;
import com.definesys.dsgc.service.apicert.bean.DagCertbeanVO;
import com.definesys.mpaas.query.MpaasQueryFactory;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName ApiCertDao
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-24 18:17
 * @Version 1.0
 **/
@Repository
public class ApiCertDao {

    @Autowired
    private MpaasQueryFactory sw;


    public PageQueryResult queryApiCertList(CommonReqBean param, int pageSize, int pageIndex, String userRole, List<String> sysCodeList){
        return sw.buildQuery().doPageQuery(pageIndex,pageSize,DagCertbeanVO.class);
    }
}
