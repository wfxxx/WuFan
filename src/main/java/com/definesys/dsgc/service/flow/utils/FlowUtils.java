package com.definesys.dsgc.service.flow.utils;

import com.definesys.dsgc.service.flow.dto.FlowNodeDTO;
import com.definesys.dsgc.service.flow.dto.FlowRoadDTO;
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


}
