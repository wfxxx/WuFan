package com.definesys.dsgc.service.dess.DessLog;

import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.DBusDao;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.dess.DessInstance.DInsDao;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstBean;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLogPayload;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLogVO;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName DLogService
 * @Description TODO
 * @Author ystar
 * @Date 2020-8-3 13:30
 * @Version 1.0
 **/
@Service
public class DLogService {

    @Autowired
    private DLogDao dLogDao;
    @Autowired
    private DBusDao dBusDao;
    @Autowired
    private DInsDao dInsDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;

    public PageQueryResult queryJobLogList(CommonReqBean param, int pageSize, int pageIndex) {
        PageQueryResult jobLogList = dLogDao.queryJobLogList(param, pageSize, pageIndex);
        PageQueryResult result = new PageQueryResult();
        List<DessLogVO> listDTOS = new ArrayList<>();
        Iterator<DessLog> iterator = jobLogList.getResult().iterator();
        while (iterator.hasNext()) {
            DessLog dessLog = iterator.next();
            DessLogVO dessLogVO = new DessLogVO();
            dessLogVO.setLogId(dessLog.getLogId());
            dessLogVO.setJobNo(dessLog.getJobNo());
            dessLogVO.setJobName(dessLog.getJobName());
            dessLogVO.setBusinessType(dessLog.getBusinessType());
            dessLogVO.setLogStatus(dessLog.getLogStatus());
            dessLogVO.setRetryTimes(dessLog.getRetryTimes());
            dessLogVO.setRunTime(dessLog.getRunTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dessLogVO.setDoTime(sdf.format(dessLog.getDoTime()));
            listDTOS.add(dessLogVO);
        }
        result.setCount(jobLogList.getCount());
        result.setResult(listDTOS);
        return result;
    }

    public DessLog findJobLogDetailByIdSwitch(String logId) {
//        String url = null;
//        List<Map<String, Object>> urlList = dLogDao.getUrl(logId);
//        if (urlList != null && urlList.size() > 0) {
//            Map<String, Object> urlMap = urlList.get(0);
//            if (urlMap != null) {
//                Object urlObj = urlMap.get("INVOKE_URL");
//                url = String.valueOf(urlObj);
//            }
//        }
        //1-根据logId查找DessLog
        DessLog log = dLogDao.queryDessLogById(logId);
        //2-查找请求和响应bodyPayload
        DessLogPayload reqPayload = dLogDao.findJobLogDetailByIdSwitch(logId + "req");
        DessLogPayload resPayload = dLogDao.findJobLogDetailByIdSwitch(logId + "res");
        log.setReqPayload(reqPayload);
        log.setResPayload(resPayload);
        //3-根据jobNo查找DInstBean
        DInstBean instBean = dInsDao.getJobInstance(log.getJobNo());
        //4-根据businessId查找url
        DessBusiness dessBusiness = dBusDao.getBusinessDtl(instBean.getBusinessId());
        log.setReqUrl(dessBusiness.getInvokeUrl());
        return log;
    }

    public String getBodyPayload(String logId) {
        return dLogDao.getBodyPayload(logId);
    }

    public void showData(HttpServletResponse response, String str) {
        if (str.startsWith("<")) {
            response.setContentType("text/xml;charset=UTF-8");
        } else {
            response.setContentType("text/plain;charset=UTF-8");
        }

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(str);
        out.flush();
        out.close();
    }

    public void noPayload(HttpServletResponse response) {
        String s = "该报文不存在，请联系管理员";
        showData(response, s);
    }


}
