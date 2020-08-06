package com.definesys.dsgc.service.dess.DessBusiness;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.dess.DessInstance.DInsDao;
import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveJobDefinition(DessBusiness dessBusiness){
        dBusDao.saveJobDefinition(dessBusiness);
    }

    public DessBusiness getJobDefinition(String jobNo){
        return dBusDao.getJobDefinition(jobNo);
    }

    public PageQueryResult queryBusinessList(CommonReqBean param, int pageSize, int pageIndex){
        return dBusDao.queryBusinessList(param,pageSize,pageIndex);
    }
    public void addBusiness(DessBusiness dessBusiness){
        dBusDao.addBusiness(dessBusiness);
    }
    public boolean checkBusinessName(CommonReqBean param){
        return dBusDao.checkBusinessName(param.getCon0());
    }
}
