package com.definesys.dsgc.service.flow.road;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.bean.FlowServices;
import com.definesys.dsgc.service.flow.dto.*;
import com.definesys.dsgc.service.flow.mng.FlowSvcDao;
import com.definesys.dsgc.service.utils.UserHelper;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FlowRoadService {
    @Autowired
    private FlowRoadDao flowRoadDao;

    @Autowired
    private FlowSvcDao flowSvcDao;

    @Autowired
    private UserHelper userHelper;


    /**
     * 获取集成流路线图
     * @param param
     * @return
     */
    public FlowRoadDTO getFlowRoad(FlowRoadQueryDTO param) {

        FlowServices fs = this.flowSvcDao.getFlowServcieByFlowId(param.getFlowId());

        if (fs == null) {
            return null;
        }

        FlowRoadDTO res = new FlowRoadDTO();
        res.setFlowId(fs.getFlowId());
        res.setTitle(fs.getFlowName());

        FlowRoads road = this.flowRoadDao.getSavedFlowRoad(param.getFlowId(),param.getFlowVersion());

        if (road == null) {
            res.setFlowVersion(FlowConstants.FLOW_DFT_VERSION);
        } else {
            res.setFlowVersion(road.getFlowVersion());
            res.setStartNodeId(road.getStartNodeId());

//            List<FlowNodeDTO> nodelist = this.getDemoRoadGraph();
//            res.setStartNodeId(nodelist.get(0).getNodeId());
//            res.setNodeList(nodelist);

            if (road.getRoadGraph() != null) {
                List<FlowNodeDTO> nodelist = JSONObject.parseArray(road.getRoadGraph(),FlowNodeDTO.class);
                res.setNodeList(nodelist);
            }

        }
        return res;
    }

    private List<FlowNodeDTO>  getDemoRoadGraph(){
        FlowNodeDTO n = new FlowNodeDTO();
        n.setNodeId(UUID.randomUUID().toString());
        n.setCnptCode("REST");
        n.setDesc("REST监听器");
        n.setTitle("REST");
        n.setType("INPUT");


        FlowNodeDTO n1 = new FlowNodeDTO();
        n1.setNodeId(UUID.randomUUID().toString());
        n1.setCnptCode("DB");
        n1.setDesc("DB查询");
        n1.setTitle("DB查询");
        n1.setType("OUTPUT");

        n.setAfterNodeId(n1.getNodeId());

        List<FlowNodeDTO> roadGraph = new ArrayList<FlowNodeDTO>();
        roadGraph.add(n);
        roadGraph.add(n1);

        return roadGraph;

    }


    /**
     * 打开编辑状态
     * @param param
     */
    public String startFlowRoadEdit(FlowRoadEditReqDTO param){

        if(this.flowRoadDao.checkEditingIsExist(param.getFlowId(),param.getFlowVersion())) {
            return "编辑冲突，已存在编辑状态！";
        }

        this.flowRoadDao.startEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        return "Y";
    }

    /**
     * 保存并退出编辑状态
     */
    public String saveFlowRoadWithCloseEditing(FlowRoadDTO param) {

        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(road.getCreatedBy())) {
                return "非法的操作权限！";
            }

            //将之前的配置更新为旧的状态
            this.flowRoadDao.changeFlowRoadToOldStat(param.getFlowId(),param.getFlowVersion());

            //将当前的编辑状态更新为新的状态
            road.setFlowStat(FlowConstants.FLOW_STAT_SAVED);
            road.setStartNodeId(param.getStartNodeId());
            if (param.getNodeList() != null && !param.getNodeList().isEmpty()) {
                road.setRoadGraph(JSONObject.toJSONString(param.getNodeList()));
            } else {
                road.setRoadGraph(null);
            }

            this.flowRoadDao.saveFlowRoad(road);

        } else {
            return "无效的编辑状态！";
        }

        return "Y";
    }


    /**
     * 放弃编辑的内容
     */
    public String backoffFlowRoadEdited(FlowRoadEditReqDTO param) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(param.getFlowId(),param.getFlowVersion());

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid !=null && !uid.equals(road.getCreatedBy())) {
                return "非法的操作权限！";
            }

            this.flowRoadDao.deleteFlowRoadAndNodeInfoByRoadId(road.getRoadId());
        } else {
            return "无效的编辑状态！";
        }

        return "Y";

    }




}
