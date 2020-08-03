package com.definesys.dsgc.service.dess;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.apilr.bean.CommonReqBean;
import com.definesys.dsgc.service.dess.bean.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DessService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 14:35
 * @Version 1.0
 **/
@Service
public class DessService {

    //服务调度程序的访问地址
    private  String dessServiceUrl;

    @Autowired
    private DessDao dessDao;

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
        return dessDao.queryJobInstaceList(param, pageSize, pageIndex);
    }


    public void saveJobBaseInfo(DinstBean dinstBean){
        dessDao.saveJobBaseInfo(dinstBean);
    }


    public boolean checkJobNoIsExist(CommonReqBean param){
        return dessDao.checkJobNoIsExist(param.getCon0());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delJobInstance (String jobNo)throws Exception{
        if(jobNo!=null){
            dessDao.delJobInstance(jobNo);
        }else{
            throw new Exception("编号不存在！");
        }
    }

    public void updateJobInstanceStatus(DinstBean dinstBean){
        if("3".equals(dinstBean.getStatus())){
            // 状态码为3 表明是停用作业调度
            dessDao.updateJobInstanceStatus(dinstBean);
        }else{
            // 状态码不确定 启用作业调度 获取作业实例
            DinstBean jobInstance = dessDao.getJobInstance(dinstBean.getJobNo());
            // 先判断该作业是否在存活时间以内
            boolean b = this.belongCalendar(jobInstance.getAliveStart(), jobInstance.getAliveEnd());
            if(b){
                dinstBean.setStatus("2");
                dessDao.updateJobInstanceStatus(dinstBean);
            }else{
                dinstBean.setStatus("1");
                dessDao.updateJobInstanceStatus(dinstBean);
            }
        }
    }

    public PageQueryResult queryJobLogList(CommonReqBean param, int pageSize, int pageIndex){
        PageQueryResult jobLogList = dessDao.queryJobLogList(param, pageSize, pageIndex);
        PageQueryResult result = new PageQueryResult();
        List<DessLogVO> listDTOS = new ArrayList<>();
        Iterator<DessLog> iterator = jobLogList.getResult().iterator();
        while (iterator.hasNext()){
            DessLog dessLog = iterator.next();
            DessLogVO dessLogVO = new DessLogVO();
            dessLogVO.setLogId(dessLog.getLogId());
            dessLogVO.setJobNo(dessLog.getJobNo());
            dessLogVO.setJobName(dessLog.getJobName());
            dessLogVO.setJobType(dessLog.getJobType());
            dessLogVO.setStatus(dessLog.getStatus());
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

    /**
     * 判断时间是否在时间段内
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public boolean belongCalendar(Date beginTime,Date endTime) {

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
        return dessDao.getJobInstance(jobNo);
    }

    public void saveJobInsDeatail(DinstBean dinstBean){
        dessDao.saveJobInsDeatail(dinstBean);
    }

    public void saveScheduling(DinstVO dinstVO){
        DinstBean dinstBean = new DinstBean();
        dinstBean.setJobNo(dinstVO.getJobNo());
        dinstBean.setAliveStart(dinstVO.getAliveStart());
        dinstBean.setAliveEnd(dinstVO.getAliveEnd());
        // 判断是否是定制时间
        if(dinstVO.getDateList()!=null&&dinstVO.getDateList().size()>0){
            List list = new ArrayList();
            for (Map<String,String> item: dinstVO.getDateList()) {
                list.add(item.get("nextDoTime"));
            };
            String frequency= StringUtils.join(list, ";");
            dinstBean.setFrequency(frequency);
        }else{
            // 转换成corn表达式存入frequency
            if(dinstVO.getFrequency()!=null){
                String cornExpression = this.getCornExpression(dinstVO);
                dinstBean.setFrequency(cornExpression);
            }
        }
        dessDao.saveScheduling(dinstBean);
    }

    public void saveJobDefinition(DessBusiness dessBusiness){
        dessDao.saveJobDefinition(dessBusiness);
    }

    public DessBusiness getJobDefinition(String jobNo){
       return dessDao.getJobDefinition(jobNo);
    }

    public DinstVO getJobScheduling(String jobNo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DinstBean job = dessDao.getJobInstance(jobNo);
        DinstVO dinstVO =new DinstVO();
        List<Map<String,String>> list = new ArrayList<>();
        // 判断是否是定制时间
        if(job.getAliveStart()==null&&job.getFrequency()!=null){
            String str[] = job.getFrequency().split(";");
            for (String s:str) {
                Map<String,String> m = new HashMap<>();
                m.put("nextDoTime",s);
                list.add(m);
            }
            dinstVO.setRate("定制");
            dinstVO.setDateList(list);
            return dinstVO;
        }else{
            // 不是定制时间则需要将正则转换为corn表达式
            return dinstVO;
        }
    }

    public String getCornExpression(DinstVO dinstVO){
        JSONObject frequency = dinstVO.getFrequency();
        StringBuffer fStr = new StringBuffer();
        String sec = frequency.getString("sec");
        String min = frequency.getString("min");
        String hour = frequency.getString("hour");
        String day = frequency.getString("day");
        String month = frequency.getString("month");
        String week = frequency.getString("week");
        String year = frequency.getString("year");
        fStr.append(sec+" ");
        fStr.append(min+" ");
        fStr.append(hour+" ");
        fStr.append(day+" ");
        fStr.append(month+" ");
        fStr.append(week+" ");
        fStr.append(year);
        return fStr.toString();
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
