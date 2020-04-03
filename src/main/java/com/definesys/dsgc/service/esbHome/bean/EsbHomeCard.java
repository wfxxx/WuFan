package com.definesys.dsgc.service.esbHome.bean;

/**
 * @ClassName ApiHomeCard
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-28 10:43
 * @Version 1.0
 **/
public class EsbHomeCard {
//apiTotal，sysTotal，userTotal，apiVisitTotal


    private Integer total;
    private Integer weekRate;//百分比
    private Integer dayRate;//百分比
    private Integer dataAdd;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getWeekRate() {
        return weekRate;
    }

    public void setWeekRate(Integer weekRate) {
        this.weekRate = weekRate;
    }

    public Integer getDayRate() {
        return dayRate;
    }

    public void setDayRate(Integer dayRate) {
        this.dayRate = dayRate;
    }

    public Integer getDataAdd() {
        return dataAdd;
    }

    public void setDataAdd(Integer dataAdd) {
        this.dataAdd = dataAdd;
    }
}
