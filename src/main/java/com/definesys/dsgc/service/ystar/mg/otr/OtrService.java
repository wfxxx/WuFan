package com.definesys.dsgc.service.ystar.mg.otr;

import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryLktParamBean;
import com.definesys.dsgc.service.ystar.mg.otr.bean.QueryPropertyParamBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("OtrService")
public class OtrService {
    @Autowired
    private OtrDao otrDao;

    public Response listQueryEnvInfoCfg(String envCode) {
        if(StringUtil.isBlank(envCode)){
            return Response.ok().data(this.otrDao.listQueryAllEnvInfoCfg("ESB"));
        }else{
            return Response.ok().data(this.otrDao.listQueryEnvInfoCfgByCode("ESB", envCode));
        }
    }

    public List<QueryPropertyParamBean> queryProperties(QueryPropertyParamBean property) {
        return this.otrDao.queryProperties(property);
    }

    public List<QueryLktParamBean> queryLkvList(QueryLktParamBean lkvParamBean) {
        List<QueryLktParamBean> lktList = this.otrDao.queryLkvList(lkvParamBean);
        if (lktList != null && lktList.size() > 0) {
            for (QueryLktParamBean lkt : lktList) {
                lkt.setValues(this.otrDao.queryLkvParamBean(lkt.getLookupType()));
            }
        }
        return lktList;
    }

}
