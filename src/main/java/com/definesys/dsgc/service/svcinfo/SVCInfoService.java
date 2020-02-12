package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.SVCInfoListBean;
import com.definesys.dsgc.service.svcinfo.bean.SVCInfoQueryBean;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SVCInfoService {
    @Autowired
    private SVCInfoDao svcInfoDao;

    public PageQueryResult<SVCInfoListBean> querySvcInfoListByCon(SVCInfoQueryBean q,int pageSize,int pageIndex) {
        return svcInfoDao.querySvcInfoListByCon(q,pageSize,pageIndex);
    }

}
