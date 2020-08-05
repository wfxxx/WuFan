package com.definesys.dsgc.service.dess.DessInstance;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.dess.CommonReqBean;
import com.definesys.dsgc.service.dess.DessBusiness.bean.DessBusiness;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstBean;
import com.definesys.dsgc.service.dess.DessInstance.bean.DinstVO;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLog;
import com.definesys.dsgc.service.dess.DessLog.bean.DessLogVO;
import com.definesys.dsgc.service.lkv.FndPropertiesService;
import com.definesys.dsgc.service.lkv.bean.FndProperties;
import com.definesys.dsgc.service.utils.StringUtil;
import com.definesys.dsgc.service.utils.httpclient.HttpReqUtil;
import com.definesys.mpaas.query.db.PageQueryResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DInsService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-8-3 13:29
 * @Version 1.0
 **/

@Service
public class DInsService {


    //服务调度程序的访问地址
    private  String dessServiceUrl;

    @Autowired
    private DInsDao dInsDao;

    @Autowired
    private FndPropertiesService fndPropertiesService;

    @PostConstruct
    private void getDessServiceUrl(){
        FndProperties fndProperties =fndPropertiesService.findFndPropertiesByKey("DESS_SERVICE_URL");
        if(fndProperties!=null){
            dessServiceUrl=fndProperties.getPropertyValue();
        }else{
            dessServiceUrl="";
        }
        System.out.println(dessServiceUrl);
    }

    //插入，删除，更新调用下列方法，查询要紫泥做


    public PageQueryResult queryJobInstaceList(CommonReqBean param, int pageSize, int pageIndex) {
        return dInsDao.queryJobInstaceList(param, pageSize, pageIndex);
    }


    public void saveJobBaseInfo(DinstBean dinstBean){
        dInsDao.saveJobBaseInfo(dinstBean);
    }


    public boolean checkJobNoIsExist(CommonReqBean param){
        return dInsDao.checkJobNoIsExist(param.getCon0());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delJobInstance (String jobNo)throws Exception{
        if(jobNo!=null){
            dInsDao.delJobInstance(jobNo);
        }else{
            throw new Exception("编号不存在！");
        }
    }

    public void updateJobInstanceStatus(DinstBean dinstBean){
//        if("0".equals(dinstBean.getJobStatus())){
//            // 状态码为0 表明是停用作业调度
//            dInsDao.updateJobInstanceStatus(dinstBean);
//        }else{
//            // 状态码不确定 启用作业调度 获取作业实例
//            DinstBean jobInstance = dInsDao.getJobInstance(dinstBean.getJobNo());
//            // 先判断该作业是否在存活时间以内
//            boolean b = this.belongCalendar(jobInstance.getAliveStart(), jobInstance.getAliveEnd());
//            if(b){
//                dinstBean.setJobStatus("2");
//                dInsDao.updateJobInstanceStatus(dinstBean);
//            }else{
//                dinstBean.setJobStatus("1");
//                dInsDao.updateJobInstanceStatus(dinstBean);
//            }
//        }

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
        }catch (Exception e) {
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


    public DinstBean getJobInstance(String jobNo){
        return dInsDao.getJobInstance(jobNo);
    }

    public void saveJobInsDeatail(DinstBean dinstBean){
        dInsDao.saveJobInsDeatail(dinstBean);
    }

    public void saveScheduling(DinstVO dinstVO){
        DinstBean dinstBean = new DinstBean();
        dinstBean.setJobNo(dinstVO.getJobNo());
        dinstBean.setAliveStart(dinstVO.getAliveStart());
        dinstBean.setAliveEnd(dinstVO.getAliveEnd());
        dinstBean.setJobRate(dinstVO.getJobRate());
        // 判断是否是定制时间
        if("定制".equals(dinstVO.getJobRate())){
            List list = new ArrayList();
            for (Map<String,String> item: dinstVO.getDateList()) {
                list.add(item.get("nextDoTime"));
            };
            String frequency= StringUtils.join(list, ";");
            dinstBean.setJobFrequency(frequency);
        }else{
            // 转换成corn表达式存入frequency
            if(dinstVO.getJobFrequency()!=null){
                String cornExpression = this.getCornExpression(dinstVO);
                dinstBean.setJobFrequency(cornExpression);
            }
        }
        dInsDao.saveScheduling(dinstBean);
    }



    public String getCornExpression(DinstVO dinstVO){
        Date aliveStart = dinstVO.getAliveStart();
        // 格式化开始时间 得到 时分秒天
        SimpleDateFormat h = new SimpleDateFormat("HH");
        SimpleDateFormat m = new SimpleDateFormat("mm");
        SimpleDateFormat s = new SimpleDateFormat("ss");
        SimpleDateFormat d = new SimpleDateFormat("E",Locale.ENGLISH);

        JSONObject frequency = dinstVO.getJobFrequency();
        String str = "";
        if("每日".equals(dinstVO.getJobRate())){
            str =   s.format(aliveStart) + " " +
                    m.format(aliveStart) + " " +
                    h.format(aliveStart) + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        }else if("每周".equals(dinstVO.getJobRate())){
            str =   s.format(aliveStart) + " " +
                    m.format(aliveStart) + " " +
                    h.format(aliveStart) + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    d.format(aliveStart) + " " +
                    frequency.getString("year");
        }else{
            str =   s.format(aliveStart) + " " +
                    m.format(aliveStart) + " " +
                    h.format(aliveStart) + " " +
                    frequency.getString("day") + " " +
                    frequency.getString("month") + " " +
                    frequency.getString("week") + " " +
                    frequency.getString("year");
        }
        return str;
    }

    public DinstVO getJobScheduling(String jobNo){
        DinstBean job = dInsDao.getJobInstance(jobNo);
        DinstVO dinstVO =new DinstVO();
        // 返回频率类型字段
        dinstVO.setJobRate(job.getJobRate());
        List<Map<String,String>> list = new ArrayList<>();
        // 判断是否是定制时间
        if(job.getAliveStart()==null&&job.getJobFrequency()!=null){
            String str[] = job.getJobFrequency().split(";");
            for (String s:str) {
                Map<String,String> m = new HashMap<>();
                m.put("nextDoTime",s);
                list.add(m);
            }
            dinstVO.setDateList(list);
            return dinstVO;
        }else{
            // 不是定制时间则需要将正则转换为corn表达式
            dinstVO.setAliveStart(job.getAliveStart());
            dinstVO.setAliveEnd(job.getAliveEnd());
            JSONObject jsonObject =JSONObject.parseObject(this.getJobFrequencyJsonText(job));
            dinstVO.setJobFrequency(jsonObject);
            return dinstVO;
        }
    }

    public String getJobFrequencyJsonText(DinstBean dinstBean){
        String jobFrequency = dinstBean.getJobFrequency();
        if (StringUtil.isBlank(jobFrequency)) {
            return null;
        }
        String[] str = dinstBean.getJobFrequency().split(" ");
        jobFrequency = "{year:'"+str[6]+"',week:'"+str[5]+"',month:'"+str[4]+"',day:'"+str[3]+"',hour:'"+str[2]+"',min:'"+str[1]+"',sec:'"+str[0]+"'}";
        return jobFrequency;
    }








    //添加任务 TODO
    public void addDessTask(HttpServletRequest request, DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/add",dinstBeanObject, request);
    }

    //删除任务
    public void delDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/delete",dinstBeanObject, request);
    }

    //暂停任务
    public void pauseDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/pause",dinstBeanObject, request);
    }

    //恢复暂停任务
    public void startDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/start",dinstBeanObject, request);
    }

    //恢复暂停任务
    public void UpdateDessTask( HttpServletRequest request,DinstBean dinstBean){
        String dinstBeanStr = JSONObject.toJSONString(dinstBean);
        JSONObject dinstBeanObject = JSONObject.parseObject(dinstBeanStr);
        HttpReqUtil.sendPostRequest(dessServiceUrl+"/dess/update",dinstBeanObject, request);
    }
}
