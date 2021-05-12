package com.definesys.dsgc.service.esbcockpit.bean;


/**
 * @Author wfxxx 2021/3/19 14:56
 */

public class eCharts3DBean {

    private String code;
    private String name;
    private Integer allCount;
    private Integer sucessCount;

    public eCharts3DBean() {
    }

    public eCharts3DBean(String code, String name, Integer allCount, Integer sucessCount) {
        this.code = code;
        this.name = name;
        this.allCount = allCount;
        this.sucessCount = sucessCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(Integer sucessCount) {
        this.sucessCount = sucessCount;
    }

    @Override
    public String toString() {
        return "eCharts3DBean{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", allCount=" + allCount +
                ", sucessCount=" + sucessCount +
                '}';
    }
}
