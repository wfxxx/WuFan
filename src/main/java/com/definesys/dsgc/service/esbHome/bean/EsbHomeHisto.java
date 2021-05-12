package com.definesys.dsgc.service.esbHome.bean;

import com.definesys.mpaas.query.model.MpaasBasePojo;

/**
 * @ClassName ApiHomeHisto
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-28 10:47
 * @Version 1.0
 **/
public class EsbHomeHisto extends MpaasBasePojo {

    private String name;
    private Integer value=0;
    private String legendName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLegendName() {
        return legendName;
    }

    public void setLegendName(String legendName) {
        this.legendName = legendName;
    }
}
