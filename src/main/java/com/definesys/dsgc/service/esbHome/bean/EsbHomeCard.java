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
    private Double weekRate;//百分比
    private Double dayRate;//百分比
    private Integer dataAdd;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getWeekRate() {
        return weekRate;
    }

    public void setWeekRate(Double weekRate) {
        this.weekRate = weekRate;
    }

    public Double getDayRate() {
        return dayRate;
    }

    public void setDayRate(Double dayRate) {
        this.dayRate = dayRate;
    }

    public Integer getDataAdd() {
        return dataAdd;
    }

    public void setDataAdd(Integer dataAdd) {
        this.dataAdd = dataAdd;
    }
}
