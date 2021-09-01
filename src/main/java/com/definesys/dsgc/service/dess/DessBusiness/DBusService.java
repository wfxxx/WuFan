package com.definesys.dsgc.service.dess.DessBusiness;

import com.alibaba.fastjson.JSONArray;
import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.svcgen.bean.OBHeaderBean;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName DBusService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:27
 * @Version 1.0
 **/
@Service
public class DBusService {

    @Autowired
    private DBusDao dBusDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;


    public PageQueryResult<DessBusiness> queryBusinessList(CommonReqBean param, int pageSize, int pageIndex) {
        PageQueryResult<DessBusiness> pageQueryResult = dBusDao.queryBusinessList(param, pageSize, pageIndex);
        List<DessBusiness> businesses = dBusDao.checkDel();
        for (DessBusiness item : pageQueryResult.getResult()) {
            item.setIsDel("true");
            for (DessBusiness business : businesses) {
                if (item.getBusinessId().equals(business.getBusinessId())) {
                    item.setIsDel("false");
                    break;
                }
            }
        }
        return pageQueryResult;
    }

    public void addBusiness(DessBusiness dessBusiness) {
        List<OBHeaderBean> headerBeans = dessBusiness.getHeaderBeanList();
        if (headerBeans != null) {
            dessBusiness.setHeaderPayload(JSONArray.toJSONString(headerBeans));
        }
        dBusDao.addBusiness(dessBusiness);
    }

    public boolean checkBusinessName(CommonReqBean param) {
        return dBusDao.checkBusinessName(param.getCon0());
    }

    public void delBusiness(String businessId) {
        dBusDao.delBusiness(businessId);
    }

    public DessBusiness getBusinessDtl(String id) {
        DessBusiness dessBusiness = dBusDao.getBusinessDtl(id);
        String headerPayload = dessBusiness.getHeaderPayload();
        if (StringUtil.isNotBlank(headerPayload)) {
            dessBusiness.setHeaderBeanList(JSONArray.parseArray(headerPayload, OBHeaderBean.class));
        }
        return dessBusiness;
    }

    public void updateBusinessDtl(DessBusiness dessBusiness) {
        List<OBHeaderBean> headerBeans = dessBusiness.getHeaderBeanList();
        dessBusiness.setHeaderPayload(JSONArray.toJSONString(headerBeans));
        dBusDao.updateBusinessDtl(dessBusiness);
    }
}
