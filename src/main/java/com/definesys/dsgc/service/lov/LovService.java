package com.definesys.dsgc.service.lov;

import com.definesys.dsgc.service.lov.bean.LookupTypeLovBean;
import com.definesys.dsgc.service.lov.bean.SystemLovBean;
import com.definesys.dsgc.service.utils.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LovService {
    @Autowired
    private LovDao lovDao;

    @Autowired
    private UserHelper userHelper;

    public List<LookupTypeLovBean> getLovListByLookupType(String lookupType){
        return lovDao.getLovListByLookupType(lookupType);
    }

    public List<SystemLovBean> getAllSystemLov(){
        return lovDao.getAllSystemLov();
    }

    public List<SystemLovBean> getAuthRangeSystemLov(String uid){
        UserHelper uh = this.userHelper.user(uid);
        if(uh.isAdmin() || uh.isSuperAdministrator()){
            return this.getAllSystemLov();
        } else if(uh.isSystemMaintainer()){
            return lovDao.getAuthRangeSystemLov(uid);
        } else{
            return new ArrayList<SystemLovBean>();
        }
    }


}
