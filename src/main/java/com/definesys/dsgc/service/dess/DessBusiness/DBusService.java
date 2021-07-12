package com.definesys.dsgc.service.dess.DessBusiness;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
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



    public PageQueryResult<DessBusiness> queryBusinessList(CommonReqBean param, int pageSize, int pageIndex){
        PageQueryResult<DessBusiness> pageQueryResult = dBusDao.queryBusinessList(param,pageSize,pageIndex);
        List<DessBusiness> cantDelbsName = dBusDao.checkDel();
        for (DessBusiness item:pageQueryResult.getResult()) {
            for (DessBusiness ele:cantDelbsName) {
                if(item.getBusinessName().equals(ele.getBusinessName())){
                    item.setIsDel("false");
                    break;
                }else{
                    item.setIsDel("true");
                }
            }
        }
        return pageQueryResult;
    }
    public void addBusiness(DessBusiness dessBusiness){
        dBusDao.addBusiness(dessBusiness);
    }
    public boolean checkBusinessName(CommonReqBean param){
        return dBusDao.checkBusinessName(param.getCon0());
    }
    public void delBusiness(String businessId){
        dBusDao.delBusiness(businessId);
    }
    public DessBusiness getBusinessDtl(String id){
        return dBusDao.getBusinessDtl(id);
    }
    public void updateBusinessDtl(DessBusiness dessBusiness){
        dBusDao.updateBusinessDtl(dessBusiness);
    }
}
