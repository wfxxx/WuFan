package com.definesys.dsgc.service.ystar.report;

import com.definesys.dsgc.service.ystar.report.bean.*;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ReportService
 * @Description: TODO
 * @Author：ystar
 * @Date : 2021/3/15 12:00
 */
@Service("ReportService")
public class ReportService {

    @Autowired
    ReportDao reportDao;

    public List<SvcInfoVO> getAllSvcInfo() {
        return reportDao.getAllSvcInfo();
    }

    public List<SysInfoVO> getAllSysInfo() {
        return reportDao.getAllSysInfo();
    }


    public ReportResultVO getDayReportData(QueryReportBean reportBean) {
        ReportResultVO reportResultVO = new ReportResultVO();
        String svcCode = reportBean.getSvcCode();
        String sysCode = reportBean.getSysCode();
        String year = null;
        String month = null;
        String day = null;
        Date date = reportBean.getDate();
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR) + "";
            month = (calendar.get(Calendar.MONTH) + 1) + "";
            day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        }
        System.out.println("year->" + year + " month->" + month + " day->" + day);
        List<ReportDayVO> reportList = reportDao.getDayReportData(year, month, day, svcCode, sysCode);
        System.out.println(reportList.size());
        reportResultVO.setSvcCode(svcCode);
        reportResultVO.setType("day");
        reportResultVO.setMaxDate(24);
        reportResultVO.setReportDayList(reportList);
        if (reportList.size() > 0) {
            int[] costArr = getCostData(reportList);
            reportResultVO.setTotalCost(costArr[0]);
            reportResultVO.setTotalEsbCost(costArr[1]);
            reportResultVO.setTotalTrgCost(costArr[2]);
            reportResultVO.setAvgCost(costArr[3]);
            reportResultVO.setAvgEsbCost(costArr[3]);
            reportResultVO.setAvgTrgCost(costArr[4]);
        }
        return reportResultVO;
    }


    public ReportResultVO getMonthReportData(QueryReportBean reportBean) {
        ReportResultVO reportResultVO = new ReportResultVO();
        String svcCode = reportBean.getSvcCode();
        String sysCode = reportBean.getSysCode();
        String year = null;
        String month = null;
        int maxDate = 0;
        Date date = reportBean.getDate();
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR) + "";
            month = (calendar.get(Calendar.MONTH) + 1) + "";
            maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        System.out.println("year->" + year + " month->" + month);
        List<ReportMonthVO> reportList = reportDao.getMonthReportData(year, month, svcCode, sysCode);
        System.out.println(reportList.size());
        reportResultVO.setSvcCode(svcCode);
        reportResultVO.setType("month");
        reportResultVO.setMaxDate(maxDate);
        reportResultVO.setReportMonthList(reportList);
        if (reportList.size() > 0) {
            int[] costArr = getCostData(reportList);
            reportResultVO.setTotalCost(costArr[0]);
            reportResultVO.setTotalEsbCost(costArr[1]);
            reportResultVO.setTotalTrgCost(costArr[2]);
            reportResultVO.setAvgCost(costArr[3]);
            reportResultVO.setAvgEsbCost(costArr[3]);
            reportResultVO.setAvgTrgCost(costArr[4]);
        }
        return reportResultVO;
    }

    public ReportResultVO getYearReportData(QueryReportBean reportBean) {
        ReportResultVO reportResultVO = new ReportResultVO();
        String svcCode = reportBean.getSvcCode();
        String sysCode = reportBean.getSysCode();
        String year = null;
        Date date = reportBean.getDate();
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR) + "";
        }
        System.out.println("year->" + year);
        List<ReportYearVO> reportList = reportDao.getYearReportData(year, svcCode, sysCode);
        System.out.println(reportList.size());
        reportResultVO.setSvcCode(svcCode);
        reportResultVO.setType("year");
        reportResultVO.setMaxDate(12);
        reportResultVO.setReportYearList(reportList);
        if (reportList.size() > 0) {
            int[] costArr = getCostData(reportList);
            reportResultVO.setTotalTimes(costArr[0]);
            reportResultVO.setTotalCost(costArr[1]);
            reportResultVO.setTotalEsbCost(costArr[2]);
            reportResultVO.setTotalTrgCost(costArr[3]);
            reportResultVO.setAvgCost(costArr[4]);
            reportResultVO.setAvgEsbCost(costArr[5]);
            reportResultVO.setAvgTrgCost(costArr[6]);
        }
        return reportResultVO;
    }

    /*** 根据报表数据，返回统计总耗时及总平均耗时
     * @param reportList **/
    private int[] getCostData(List reportList) {
        int[] costArr = new int[7];
        for (Object reportObj : reportList) {
            ReportYearVO report = (ReportYearVO) reportObj;
            double totalTimes = report.getTotalTimes(); //总次数
            double avgCost = report.getAvgCost();//平均耗时
            double avgEsbCost = report.getEsbAvgCost();//ESB平均耗时
            double avgTrgCost = report.getTrgAvgCost();//目标系统平均耗时
            costArr[0] += totalTimes; //次数累计
            costArr[1] += avgCost * totalTimes; // 服务耗时累计
            costArr[2] += avgEsbCost * totalTimes; // ESB耗时累计
            costArr[3] += avgTrgCost * totalTimes; //目标系统耗时累计
        }
        if (costArr[0] == 0) {
            costArr[4] = 0;
            costArr[5] = 0;
            costArr[6] = 0;
        } else {
            costArr[4] = costArr[1] / costArr[0];// 服务平均耗时
            costArr[5] = costArr[2] / costArr[0];// ESB平均耗时
            costArr[6] = costArr[3] / costArr[0];//目标系统平均耗时
        }
        return costArr;
    }


    public PageQueryResult<ReportLogVO> getLogReportData(QueryReportBean reportBean, int pageIndex, int pageSize) {
        PageQueryResult<ReportLogVO> reportList = reportDao.getLogReportData(reportBean, pageIndex, pageSize);
        System.out.println(reportList.getResult().size());
        return reportList;
    }

}
