package com.definesys.dsgc.service.dess.DessInstance;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dess.CommonReqBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DInstSltBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstVO;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstVO2;
import com.definesys.dsgc.service.dess.util.CornUtils;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesDao;
import com.definesys.dsgc.service.ystar.fnd.property.FndPropertiesService;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.dsgc.service.utils.httpclient.ResultVO;
import com.definesys.mpaas.common.http.Response;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DInsService
 * @Description TODO
 * @Author ystar
 * @Date 2020-10-3 13:29
 * @Version 1.0
 **/

@Service("DInsService")
public class DInsService {

    public static final String PROPERTY_KEY_DESS_SERVICE_URL = "DESS_SERVICE_URL";

    @Autowired
    private DInsDao dInsDao;

    @Autowired
    private FndPropertiesDao propertiesDao;


    @Autowired
    private FndPropertiesService fndPropertiesService;

    //插入，删除，更新调用下列方法，查询要紫泥做

    public PageQueryResult queryJobInstaceList(CommonReqBean param, int pageSize, int pageIndex) {
        return dInsDao.queryJobInstaceList(param, pageSize, pageIndex);
    }

    public DinstVO2 getDinstVO(String jobNo) {
        return dInsDao.getJobVO(jobNo);
    }


    public void saveJobBaseInfo(DInstBean dinstBean) {
        dInsDao.saveJobBaseInfo(dinstBean);
    }


    public boolean checkJobNoIsExist(CommonReqBean param) {
        return dInsDao.checkJobNoIsExist(param.getCon0());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delJobInstance(String jobNo) throws Exception {
        if (jobNo != null) {
            dInsDao.delJobInstance(jobNo);
        } else {
            throw new Exception("编号不存在！");
        }
    }

    public void updateJobInstanceStatus(DInstBean dinstBean) {
        dInsDao.updateJobInstanceStatus(dinstBean);
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public boolean belongCalendar(Date beginTime, Date endTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        try {
            now = df.parse(df.format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar date = Calendar.getInstance();
        date.setTime(now);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    public DInstBean getJobInstance(String jobNo) {
        return dInsDao.getJobInstance(jobNo);
    }

    public void saveJobInsDeatail(DInstBean dinstBean) {
        dInsDao.saveJobInsDeatail(dinstBean);
    }

    public List<Map<String, String>> saveScheduling(DinstVO dinstVO, HttpServletRequest request) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        List<Map<String, String>> parserList = new ArrayList<>();
        DInstBean dinstBean = new DInstBean();
        dinstBean.setJobNo(dinstVO.getJobNo());
        dinstBean.setAliveStart(dinstVO.getAliveStart());
        dinstBean.setAliveEnd(dinstVO.getAliveEnd());
        dinstBean.setJobRate(dinstVO.getJobRate());
        // 定制时间无需corn表达式
        if ("定制".equals(dinstVO.getJobRate())) {
            List list = new ArrayList();
            for (Map<String, String> item : dinstVO.getDateList()) {
                list.add(item.get("nextDoTime"));
            }
            ;
            String frequency = StringUtils.join(list, ";");
            dinstBean.setJobFrequency(frequency);
            dInsDao.saveScheduling(dinstBean);
        } else {
            // 转换成corn表达式转为调度时间集合返回给前端
            if (dinstVO.getJobFrequency() != null) {
                String cornExpression = this.getCornExpression(dinstVO);
                System.out.println("cornExpression--->" + cornExpression);
                // 判断是否是每月或者每年 解决第几周与周几的冲突
                if (cornExpression.contains("#")) {
                    String[] cronExpressionList = this.getCronExpressionList(cornExpression);
                    for (int i = 0; i < cronExpressionList.length; i++) {
                        List<Map<String, String>> parser = CornUtils.getNextTenRunTimes(request, dessServiceUrl, cronExpressionList[i], dinstVO.getAliveStart(), dinstVO.getAliveEnd());
                        parserList.addAll(parser);
                    }
                    dinstBean.setJobFrequency(StringUtils.join(cronExpressionList, ";"));
                } else {
                    //parserList = this.parser(cornExpression, dinstVO.getAliveStart(), dinstVO.getAliveEnd());
                    dinstBean.setJobFrequency(cornExpression);
                    parserList = CornUtils.getNextTenRunTimes(request, dessServiceUrl, cornExpression, dinstVO.getAliveStart(), dinstVO.getAliveEnd());
                }
                if (parserList.size() > 0) {
                    dInsDao.saveScheduling(dinstBean);
                }
            }
        }


        return parserList;
    }


    public String getCornExpression(DinstVO dinstVO) {
        Date aliveStart = dinstVO.getAliveStart();
        // 格式化开始时间 得到 时分秒天
        SimpleDateFormat h = new SimpleDateFormat("HH");
        SimpleDateFormat m = new SimpleDateFormat("mm");
        SimpleDateFormat s = new SimpleDateFormat("ss");
        SimpleDateFormat d = new SimpleDateFormat("E", Locale.ENGLISH);

        JSONObject frequency = dinstVO.getJobFrequency();
        String str = "";
        if ("day".equals(dinstVO.getJobRate()) || "week".equals(dinstVO.getJobRate()) || "month".equals(dinstVO.getJobRate()) || "year".equals(dinstVO.getJobRate())) {
            str = s.format(aliveStart) + " " +
                    m.format(aliveStart) + " " +
                    h.format(aliveStart) + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        } else if ("hour".equals(dinstVO.getJobRate())) {
            str = s.format(aliveStart) + " " +
                    m.format(aliveStart) + " " +
                    frequency.getString("hour") + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        } else if ("min".equals(dinstVO.getJobRate())) {
            str = s.format(aliveStart) + " " +
                    frequency.getString("min") + " " +
                    frequency.getString("hour") + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        } else {
            str = frequency.getString("sec") + " " +
                    frequency.getString("min") + " " +
                    frequency.getString("hour") + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        }
        return str;
    }

    public DinstVO getJobScheduling(String jobNo, HttpServletRequest request) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        DInstBean job = dInsDao.getJobInstance(jobNo);
        DinstVO dinstVO = new DinstVO();
        // 返回频率类型字段
        dinstVO.setJobRate(job.getJobRate());
        List<Map<String, String>> list = new ArrayList<>();
        // 判断是否是定制时间
        if (job.getAliveStart() == null && job.getJobFrequency() != null) {
            String str[] = job.getJobFrequency().split(";");
            for (String s : str) {
                Map<String, String> m = new HashMap<>();
                m.put("nextDoTime", s);
                list.add(m);
            }
            dinstVO.setDateList(list);
            return dinstVO;
        } else {
            // 将corn表达式转换成调度时间
            //List<Map<String, String>> parser = this.parser(job.getJobFrequency(), job.getAliveStart(), job.getAliveEnd());
            List<Map<String, String>> parser = CornUtils.getNextTenRunTimes(request, dessServiceUrl, job.getJobFrequency(), job.getAliveStart(), job.getAliveEnd());
            dinstVO.setDateList(parser);
            return dinstVO;
        }
    }


    //添加,启动任务 TODO
    public void addDessTask(HttpServletRequest request, DinstVO2 dInstVO) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        String dInstBeanStr = JSONObject.toJSONString(dInstVO);
        JSONObject dInstBeanObject = JSONObject.parseObject(dInstBeanStr);
        System.out.println(dInstBeanObject);
        ResultVO resultVO = HttpReqUtil.sendPostRequest(dessServiceUrl + "/dess/add", dInstBeanObject, request);

        System.out.println("data1111->" + resultVO);

    }


    //删除，暂停任务
    public void pauseDessTask(HttpServletRequest request, DinstVO2 dInstVO) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        String dInstBeanStr = JSONObject.toJSONString(dInstVO);
        JSONObject dInstBeanObject = JSONObject.parseObject(dInstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl + "/dess/pauseJob", dInstBeanObject, request);
    }


    //更新，重启任务,注意重启会放弃之前的已有的调度。
    public void UpdateDessTask(HttpServletRequest request, DinstVO2 dinstVO) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        String dinstBeanStr = JSONObject.toJSONString(dinstVO);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        System.out.println("dessServiceUrl->" + dessServiceUrl);
        HttpReqUtil.sendPostRequest(dessServiceUrl + "/dess/refresh", dinstBeanObject, request);
    }

    //手动调用定时任务 TODO
    public JSONObject manualJobInstance(HttpServletRequest request, HashMap reqParam) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        List<String> jobNoList = (List<String>) reqParam.get("jobNo");
        String body = String.valueOf(reqParam.get("body"));
        String header = String.valueOf(reqParam.get("header"));
        List<String> failList = new ArrayList<>();
        for (String jobNo : jobNoList) {
            DInstBean dinstBean = dInsDao.getBusinessIdByJobNo(jobNo);
            String businessId = dinstBean.getBusinessId();
            if (StringUtil.isNotBlank(businessId)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("businessId", businessId);
                jsonObject.put("header", header);
                jsonObject.put("body", body);
                HttpReqUtil.sendPostRequest(dessServiceUrl + "/dess/manualJob", jsonObject, request);
            } else {
                jobNoList.remove(jobNo);
                failList.add(jobNo);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("successJob", jobNoList);
        jsonObject.put("failJob", failList);
        return jsonObject;
    }


    public String[] getCronExpressionList(String cronExpression) {
        String[] s = cronExpression.split(" ");
        String dayofWeekStr = "";
        StringBuffer newCronExpression = new StringBuffer();
        for (int i = 0; i < s.length; i++) {
            if (s[i].contains("#")) {
                dayofWeekStr = s[i];
                break;
            }
        }
        String strBf = StringUtils.substringBefore(dayofWeekStr, "#");
        String strAf = StringUtils.substringAfter(dayofWeekStr, "#");
        String[] bf = strBf.split(",");
        String[] af = strAf.split(",");
        StringBuffer dws = new StringBuffer();
        for (int i = 0; i < bf.length; i++) {
            for (int j = 0; j < af.length; j++) {
                dws.append(bf[i] + "#" + af[j] + " ");
            }
        }
        String nDws = dws.toString();
        String[] allDws = nDws.split(" ");

        for (int i = 0; i < allDws.length; i++) {
            s[5] = allDws[i];
            newCronExpression.append(StringUtils.join(s, " ") + ";");
        }
        String[] cronExpressionList = newCronExpression.toString().split(";");
        return cronExpressionList;
    }


    public Response getAllJobScheduleTime(DInstSltBean bean, HttpServletRequest request) {
        String dessServiceUrl = this.propertiesDao.queryPropertyByKey(PROPERTY_KEY_DESS_SERVICE_URL);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime", bean.getStartTime());
        jsonObject.put("endTime", bean.getEndTime());
        jsonObject.put("limitTime", bean.getLimitTime());
        return Response.ok().data(HttpReqUtil.sendPostRequest(dessServiceUrl + "/dessInst/getAllJobScheduleTime", jsonObject, request).getData());
    }
}
