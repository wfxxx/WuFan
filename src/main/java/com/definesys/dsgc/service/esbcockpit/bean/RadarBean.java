package com.definesys.dsgc.service.esbcockpit.bean;

/**
 * @Author wfxxx 2021/3/23 13:43
 */
public class RadarBean {

    /**年份*/
    private Integer year;
    /**月*/
    private Integer month;
    /**接口调用总数*/
    private Integer count;

    public RadarBean() {
    }

    public RadarBean(Integer year, Integer month, Integer count) {
        this.year = year;
        this.month = month;
        this.count = count;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "RadarBean{" +
                "year=" + year +
                ", month=" + month +
                ", count=" + count +
                '}';
    }
}
