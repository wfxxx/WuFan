package com.definesys.dsgc.service.apibs;

import com.definesys.dsgc.service.apibs.bean.CommonReqBean;
import com.definesys.dsgc.service.apibs.bean.DagBsListDTO;
import com.definesys.dsgc.service.apibs.bean.DagBsbean;
import com.definesys.dsgc.service.svclog.SVCLogDao;
import com.definesys.dsgc.service.system.bean.DSGCSystemUser;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ApiBsService {
    @Autowired
    private ApiBsDao apiBsDao;

    @Autowired
    private SVCLogDao sldao;

    public PageQueryResult queryApiBsList(CommonReqBean param,String userId,String userRole,int pageSize,int pageIndex ){
        List<String> sysCodeList = new ArrayList<>();
        if ("SystemLeader".equals(userRole)){
            List<DSGCSystemUser> dsgcSystemUsers =   sldao.findUserSystemByUserId(userId);
            Iterator<DSGCSystemUser> iter = dsgcSystemUsers.iterator();
            while (iter.hasNext()) {
                DSGCSystemUser s = (DSGCSystemUser) iter.next();
                sysCodeList.add(s.getSysCode());
            }
        }
        PageQueryResult queryApiBsList = apiBsDao.queryApiBsList(param,pageSize,pageIndex,userRole,sysCodeList);
        PageQueryResult result = new PageQueryResult();
        List<DagBsListDTO> listDTOS = new ArrayList<>();
        Iterator<DagBsbean> iterator = queryApiBsList.getResult().iterator();
        while (iterator.hasNext()){
            DagBsbean dagBsbean = iterator.next();
            DagBsListDTO dagBsListDTO = new DagBsListDTO();
            dagBsListDTO.setAppName(dagBsbean.getAppName());
            dagBsListDTO.setBsCode(dagBsbean.getBsCode());
            dagBsListDTO.setBsDesc(dagBsbean.getBsDesc());
            dagBsListDTO.setBsId(dagBsbean.getBsId());
            listDTOS.add(dagBsListDTO);
        }
        result.setCount(queryApiBsList.getCount());
        result.setResult(listDTOS);
        return result;
    }

    public List<String> queryApiBsByCustomInput(CommonReqBean param){
    List<DagBsbean> list = apiBsDao.queryApiBsByCustomInput(param);
    Iterator<DagBsbean> iterator = list.iterator();
    List<String> result = new ArrayList<>();
    while (iterator.hasNext()){
        DagBsbean dagBsbean = iterator.next();
        result.add(dagBsbean.getBsCode());
    }
    return result;
    }

    public void addApiBs(DagBsbean dagBsbean){
        apiBsDao.addApiBs(dagBsbean);
    }
    @Transactional(rollbackFor = Exception.class)
    public void delApiBs(CommonReqBean param)throws Exception{
       DagBsbean dagBsbean = apiBsDao.queryApiBsById(param);
       if (dagBsbean != null){
           apiBsDao.delApiBs(param);
       }else {
           throw new Exception("服务不存在！");
       }

    }
}
