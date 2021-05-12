package com.definesys.dsgc.service.ystar.report;

import com.definesys.dsgc.service.utils.StringUtils;
import com.definesys.dsgc.service.ystar.report.bean.ReportSvcDataBean;
import com.definesys.dsgc.service.ystar.report.util.ReportUtils;
import com.definesys.mpaas.common.http.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ReportAnalyzeService
 * @Description: TODO
 * @Author：ystar
 * @Date : 2020/12/16 12:00
 */
@Service("ReportAnalyzeService")
public class ReportAnalyzeService {

    @Autowired
    ReportAnalyzeDao reportAnalyzeDao;

    /**
     * 根据某年和某周返回上一周及本周报表数据
     *
     * @param rpSvcDataBean
     * @return
     */
    public ReportSvcDataBean getTrgSysWeekRpData(ReportSvcDataBean rpSvcDataBean) {
        String startTime = rpSvcDataBean.getStartTime();
        String endTime = rpSvcDataBean.getEndTime();
        System.out.println("startTime->" + startTime + " endTime->" + endTime);
        rpSvcDataBean.setCurList(reportAnalyzeDao.getWeekRpData(startTime, endTime));
        rpSvcDataBean.setLastList(reportAnalyzeDao.getWeekRpData(ReportUtils.getLastWeekDateTime(startTime), ReportUtils.getLastWeekDateTime(endTime)));
        return rpSvcDataBean;
    }


    public ReportSvcDataBean getSvcCountData(ReportSvcDataBean rpSvcDataBean) {
        List<Map<String, Object>> svcCountList = new ArrayList<>();
        //判断是查总耗时还是ESB内耗
        String costType = rpSvcDataBean.getCostType();
        String startTime = rpSvcDataBean.getStartTime();
        String endTime = rpSvcDataBean.getEndTime();
        if ("ALL".equals(costType)) {
            String techType = rpSvcDataBean.getServiceType();
            svcCountList = reportAnalyzeDao.getSvcAllCostData(startTime, endTime, techType);
            rpSvcDataBean.setStandard("A".equalsIgnoreCase(techType) ? 5000 : 3000);
            //rpSvcDataBean.setList(getNineLineData(svcCountList));
            rpSvcDataBean.setCurList(getSysCostList(svcCountList));
            rpSvcDataBean.setLastList(getSysCostList(reportAnalyzeDao.getSvcAllCostData(ReportUtils.getLastWeekDateTime(startTime), ReportUtils.getLastWeekDateTime(endTime), techType)));
        } else if ("ESB".equals(costType)) {
            svcCountList = reportAnalyzeDao.getSvcEsbCostData(startTime, endTime);
            rpSvcDataBean.setStandard(100);
            //rpSvcDataBean.setList(getNineLineData(svcCountList));
            rpSvcDataBean.setCurList(getSysCostList(svcCountList));
            rpSvcDataBean.setLastList(getSysCostList(reportAnalyzeDao.getSvcEsbCostData(ReportUtils.getLastWeekDateTime(startTime), ReportUtils.getLastWeekDateTime(endTime))));
        }
        return rpSvcDataBean;
    }


    private List<Map<String, Object>> getSysCostList(List<Map<String, Object>> dataList) {
        List<Map<String, Object>> resDataList = new ArrayList<>();
        //保存系统编码及对应响应时间集合
        Map<String, List<Float>> sysCostMap = new HashMap<>();

        System.out.println(dataList.size());
        //变量数据
        for (Map<String, Object> svcCount : dataList) {
            //获取系统编码
            String sysCode = String.valueOf(svcCount.get("SYS_CODE"));
            Object instCostObj = svcCount.get("INST_COST");
            //获取响应时间
            float instCost = instCostObj == null ? 0 : Float.parseFloat(String.valueOf(instCostObj));
            //2-获取各系统集合
            List<Float> costObj = sysCostMap.get(sysCode);
            List<Float> costList = new ArrayList<>();
            if (costObj != null) {
                costList = costObj;
            }
            costList.add(instCost < 0 ? 0 : instCost);
            //System.out.println("sys->" + sysCode + " cost->" + instCost);
            sysCostMap.put(sysCode, costList);
        }

        //3-对各系统集合数据进行遍历，查找第90分位的数据值
        for (String sysCode : sysCostMap.keySet()) {
            List<Float> costList = sysCostMap.get(sysCode);
            //获取90分位值
            int size = (int) Math.round(costList.size() * 0.9);
            Map<String, Object> costMap = new HashMap<>();
            costMap.put("trg_sys_code", sysCode);
            costMap.put("cost", costList.get(size - 1));
            System.out.println("sys->" + sysCode + " 总数：" + costList.size() + " 第90分位个数位置：" + size + " 值为：" + costList.get(size - 1));
            resDataList.add(costMap);
        }
        return resDataList;
    }

    public Response importParamData(MultipartFile file) {

        return null;
    }


}
