package com.definesys.dsgc.service.apicockpit.bean;

import com.definesys.mpaas.query.annotation.Column;
import com.definesys.mpaas.query.annotation.ColumnType;
import com.definesys.mpaas.query.annotation.SystemColumn;
import com.definesys.mpaas.query.annotation.SystemColumnType;
import com.definesys.mpaas.query.json.MpaasDateTimeDeserializer;
import com.definesys.mpaas.query.json.MpaasDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * @ClassName eChartsBean
 * @Description 接收数据库返回的统计数据
 * @Author Xueyunlong
 * @Date 2020-3-30 10:52
 * @Version 1.0
 **/

public class eChartsBean {

    private String name; //echats中的名称
    private Integer value1;//echats中的一定需要的值
    private Integer value2;//echats中的可能需要的值
    private double rate;////echats中的可能需要的值


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
}
