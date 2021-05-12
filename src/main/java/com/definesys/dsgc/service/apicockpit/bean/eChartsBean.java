package com.definesys.dsgc.service.apicockpit.bean;



import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName eChartsBean
 * @Description 接收数据库返回的统计数据
 * @Author Xueyunlong
 * @Date 2020-3-30 10:52
 * @Version 1.0
 **/

public class eChartsBean {
    private String id; //echats中的id
    private String name; //echats中的名称

    private Integer value1;//echats中的一定需要的值
    private Integer value2;//echats中的可能需要的值
    private double rate;////echats中的可能需要的值
    private List<String> value3;////echats中的可能需要的值


    public eChartsBean(){
    }

    public eChartsBean(String name ,int value1){
        this.name=name;
        this.value1=value1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue1() {
        return value1;
    }

    public void setValue1(Integer value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<String> getValue3() {
        return value3;
    }

    public void setValue3(List<String> value3) {
        this.value3 = value3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
