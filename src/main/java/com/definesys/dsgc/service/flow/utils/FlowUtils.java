package com.definesys.dsgc.service.flow.utils;

import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.dto.FlowConstants;
import com.definesys.dsgc.service.flow.dto.FlowNodeDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;

public class FlowUtils {

    /**
     * 从流图中获取节点信息
     * @param road
     * @param nodeId
     * @return
     */
    public static FlowNodeDTO getNodeFromRoad(FlowRoadDTO road,String nodeId) {
        if (StringUtils.isNotBlank(nodeId) && road != null && road.getNodeList() != null && !road.getNodeList().isEmpty()) {
            for (FlowNodeDTO node : road.getNodeList()) {
                if (nodeId.equals(node.getNodeId())) {
                    return node;
                }
            }
        }
        return null;
    }


    public static String checkFlowEditingUserAuth(FlowRoads edittingRoad) {
        if (edittingRoad == null) {
            return FlowConstants.FLOW_TIPS_INVAILD_EDITTING_STAT;
        } else {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(edittingRoad.getCreatedBy())) {
                return FlowConstants.FLOW_TIPS_INVAILD_AUTH;
            } else {
                return "Y";
            }
        }
    }


}
