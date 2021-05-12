package com.definesys.dsgc.service.svcinfo;

import com.definesys.dsgc.service.svcinfo.bean.*;
import com.definesys.dsgc.service.svcmng.bean.DSGCServicesUri;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SVCInfoService")
public class SVCInfoService {
    @Autowired
    private SVCInfoDao svcInfoDao;

    public PageQueryResult<SVCInfoListBean> querySvcInfoListByCon(SVCInfoQueryBean q, int pageSize, int pageIndex) {
        return svcInfoDao.querySvcInfoListByCon(q, pageSize, pageIndex);
    }


    public List<SVCInfoVO> queryService(SVCInfoQueryBean svcInfoQueryBean) {
        return this.svcInfoDao.queryService(svcInfoQueryBean);
    }

    /***查找所有的服务uri信息（soap类型加上方法名） **/
    public List<DSGCServicesUri> queryAllSvcUri() {
        List<DSGCServicesUri> svcUriList = this.svcInfoDao.queryAllSvcUri();
        List<SVCInfoBean> svcInfoList = this.svcInfoDao.querySvcWithSpyOprInfo();
        for (DSGCServicesUri svcUri : svcUriList) {
            for (SVCInfoBean svcInfo : svcInfoList) {
                if (svcUri.getServNo().equals(svcInfo.getServNo())) {
                    String uri = svcUri.getIbUri();
                    String spyOpr = svcInfo.getSpyOper();
                    if (StringUtil.isNotBlank(spyOpr)) {
                        svcUri.setIbUri(uri + "_" + spyOpr);
                    }
                }
            }
        }
        return svcUriList;
    }


    public List<QuerySvcParamBean> querySvcList(QuerySvcParamBean svcParamBean) {
        return this.svcInfoDao.querySvcList(svcParamBean);
    }


    public Response querySvcBsInfoByCode(SvcBsInfoBean bsInfoBean) {
        return Response.ok().data(this.svcInfoDao.querySvcBsInfoByCode(bsInfoBean.getSvcCode(), bsInfoBean.getSysCode()));
    }

    public Response addSvcBsInfo(SvcBsInfoBean bsInfoBean) {
        String bsCode = bsInfoBean.getBsCode();
        String svcCode = bsInfoBean.getSvcCode();
        String sysCode = bsInfoBean.getSysCode();
        if (StringUtil.isNotBlank(bsCode) && StringUtil.isNotBlank(svcCode) && StringUtil.isNotBlank(sysCode)) {
            SvcBsInfoBean bsInfo = this.svcInfoDao.querySvcBsInfoByCode(svcCode, sysCode);
            if (bsInfo != null) {
                return Response.error("新增失败，该服务配置数据重复！").data(bsInfoBean);
            }
            return Response.ok().data(this.svcInfoDao.addSvcBsInfo(bsInfoBean));
        }
        return Response.error("业务编号、服务编号或目标系统编号不能为空！").data(bsInfoBean);
    }

    public String delSvcBsInfo(SvcBsInfoBean bsInfoBean) {
        return this.svcInfoDao.delSvcBsInfo(bsInfoBean);
    }

}
