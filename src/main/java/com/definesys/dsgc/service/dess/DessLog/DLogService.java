package com.definesys.dsgc.service.dess.DessLog;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLogVO;
import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName DLogService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:30
 * @Version 1.0
 **/
@Service
public class DLogService {

    @Autowired
    private DLogDao dLogDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;

    public PageQueryResult queryJobLogList(CommonReqBean param, int pageSize, int pageIndex){
        PageQueryResult jobLogList = dLogDao.queryJobLogList(param, pageSize, pageIndex);
        PageQueryResult result = new PageQueryResult();
        List<DessLogVO> listDTOS = new ArrayList<>();
        Iterator<DessLog> iterator = jobLogList.getResult().iterator();
        while (iterator.hasNext()){
            DessLog dessLog = iterator.next();
            DessLogVO dessLogVO = new DessLogVO();
            dessLogVO.setLogId(dessLog.getLogId());
            dessLogVO.setJobNo(dessLog.getJobNo());
            dessLogVO.setJobName(dessLog.getJobName());
            dessLogVO.setBusinessType(dessLog.getBusinessType());
            dessLogVO.setLogStatus(dessLog.getLogStatus());
            dessLogVO.setRetryTimes(dessLog.getRetryTimes());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dessLogVO.setCreationDate(sdf.format(dessLog.getCreationDate()));
            dessLogVO.setDoTime(sdf.format(dessLog.getDoTime()));
            listDTOS.add(dessLogVO);
        }
        result.setCount(jobLogList.getCount());
        result.setResult(listDTOS);
        return result;
    }
}
