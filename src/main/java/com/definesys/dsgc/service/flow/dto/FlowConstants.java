package com.definesys.dsgc.service.flow.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FlowConstants {
    public final static String FLOW_STAT_OLD = "old";
    public final static String FLOW_STAT_EDITING = "editing";
    public final static String FLOW_STAT_SAVED = "saved";
    public final static String FLOW_DFT_VERSION = "default";

    public final static String FLOW_PANEL_PARAMS = "params";

    public final static String FLOW_VAR_PAYLOAD ="Payload";

    public final static String FLOW_VAR_TYPE_ANY = "Any";


    public final static String FLOW_TIPS_INVAILD_EDITTING_STAT = "无效的编辑状态！";

    public final static String FLOW_TIPS_INVAILD_AUTH = "非法的操作权限！";

    public final static String FLOW_TIPS_INVAILD_PARAMS = "非法的请求参数！";
}
