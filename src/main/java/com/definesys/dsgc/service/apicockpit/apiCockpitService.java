package com.definesys.dsgc.service.apicockpit;

import com.definesys.dsgc.service.apicockpit.bean.eChartsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName apiCockpitService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-3-30 10:40
 * @Version 1.0
 **/
@Service
public class apiCockpitService {
    @Autowired
    private apiCockpitDao apiCockpitDao;

    //api一段时间内总调用次数
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryTotalRunTimes(Date startDate, Date endDate){

        return apiCockpitDao.queryTotalRunTimes(startDate,endDate);
    }

    //平台接入应用数量
    /**
     * @return result
     */
    public eChartsBean queryTotalapp(){

        return apiCockpitDao.queryTotalapp();
    }

    //一段时间内API调用在各系统分布数量（成功的和失败的）
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public List<eChartsBean> queryAppDistri(Date startDate, Date endDate){
        //value1为调用总数，value2为调用成功数，失败数为计算数量，rate为成功占比
        List<eChartsBean> result= apiCockpitDao.queryAppDistri(startDate,endDate);
        for(eChartsBean item :result){
            item.setRate(item.getValue2()/Double.valueOf(item.getValue1()));
        }
        return result;
    }

    //一段时间内调用到的API个数（注意不是次数）占总数的比例
    /**
     * @param startDate
     * @param endDate
     * @return result
     */
    public eChartsBean queryAppExecute(Date startDate, Date endDate){
        eChartsBean result=apiCockpitDao.queryAppExecute(startDate,endDate);
        eChartsBean appTotal=apiCockpitDao.queryTotalapp();
        result.setRate(result.getValue1()/Double.valueOf(appTotal.getValue1()));
        return result;
    }
}
