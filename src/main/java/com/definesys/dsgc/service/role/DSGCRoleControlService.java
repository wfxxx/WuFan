package com.definesys.dsgc.service.role;

import com.definesys.dsgc.service.role.bean.DSGCRoleControl;
import com.definesys.dsgc.service.role.bean.DSGCRoleControlVO;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("roleControlService")
public class DSGCRoleControlService {
    @Autowired
    DSGCRoleControlDao roleControlDao;

    @Transactional(rollbackFor = Exception.class)
    public void addRoleControl(DSGCRoleControl roleControl){
     roleControlDao.addRoleControl(roleControl);
    }
    public PageQueryResult<DSGCRoleControlVO> queryRoleControl(DSGCRoleControl roleControl, int pageSize, int pageIndex) {
        return this.roleControlDao.queryRoleControl2(roleControl, pageSize, pageIndex);
    }
    public int  checkRoleControl(DSGCRoleControl roleControl){
        List<Map<String,Object>> list =  roleControlDao.checkRoleControl(roleControl);
        int num = Integer.parseInt(String.valueOf(list.get(0).get("NUM")) );
        return num;
    }
    @Transactional(rollbackFor = Exception.class)
    public String  updateRoleControl(DSGCRoleControl roleControl){

        DSGCRoleControl control =  roleControlDao.findRoleControlById(roleControl.getRoleId());
        control.setIsEdit(roleControl.getIsEdit());
        control.setIsSee(roleControl.getIsSee());

      return  roleControlDao.updateRoleControl(control);
    }
    @Transactional(rollbackFor = Exception.class)
    public String  removeRoleControl(DSGCRoleControl roleControl){
        return  roleControlDao.removeRoleControl(roleControl);
    }

    public List<DSGCRoleControl> queryRoleJurisdiction(DSGCRoleControl roleControl) {
        return this.roleControlDao.queryRoleJurisdiction(roleControl);
    }
}
