package com.definesys.dsgc.service.apilr;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.apilr.bean.DagLrListDTO;
import com.definesys.dsgc.service.apilr.bean.DagLrbean;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiLrService {
    @Autowired
    private ApiLrDao apiLrDao;

    public PageQueryResult queryApiLrList(CommonReqBean param, String userId, String userRole, int pageSize, int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   apiLrDao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult queryApiBsList = apiLrDao.queryApiLrList(param,pageSize,pageIndex,userRole,sysCodeList);
        PageQueryResult result = new PageQueryResult();
        List<DagLrListDTO> listDTOS = new ArrayList<>();
        Iterator<DagLrbean> iterator = queryApiBsList.getResult().iterator();
        while (iterator.hasNext()){
            DagLrbean dagLrbean = iterator.next();
            DagLrListDTO dagLrListDTO = new DagLrListDTO();
            dagLrListDTO.setAppName(dagLrbean.getAppName());
            dagLrListDTO.setLrName(dagLrbean.getLrName());
            dagLrListDTO.setLrDesc(dagLrbean.getLrDesc());
            dagLrListDTO.setDlId(dagLrbean.getDlId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dagLrListDTO.setCreationDate(sdf.format(dagLrbean.getCreationDate()));
            listDTOS.add(dagLrListDTO);
        }
        result.setCount(queryApiBsList.getCount());
        result.setResult(listDTOS);
        return result;
    }

    public void addApiLr(DagLrbean dagLrbean){
        apiLrDao.addApiLr(dagLrbean);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApiLr(CommonReqBean param)throws Exception{
        DagLrbean dagLrbean = apiLrDao.queryApiLrById(param);
        if (dagLrbean != null){
            apiLrDao.delApiBs(param);
        }else {
            throw new Exception("服务不存在！");
        }

    }
}
