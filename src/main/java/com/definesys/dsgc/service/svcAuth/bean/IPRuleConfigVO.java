package com.definesys.dsgc.service.svcAuth.bean;

public class IPRuleConfigVO {
    private String type;                 //规则类型，B/W (黑白)
    private String rule;                 //具体规则
    private String limitTarget;          //限制目标（服务编号/环境代码）
    private String limitType;            //限制种类   global/serv（整个环境/单一服务）

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getLimitTarget() {
        return limitTarget;
    }

    public void setLimitTarget(String limitTarget) {
        this.limitTarget = limitTarget;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }
}
