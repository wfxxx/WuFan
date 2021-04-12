package com.definesys.dsgc.service.flow.meta;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.meta.dto.DataNodeDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaDTO;
import com.definesys.dsgc.service.flow.road.FlowRoadDao;
import com.definesys.mpaas.common.util.MpaasUtil;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowMetaService {

    @Autowired
    private FlowMetaDao flowMetaDao;

    @Autowired
    private FlowRoadDao flowRoadDao;


    /**
     * 根据metaid删除metadata
     * @param flowId
     * @param flowVersion
     * @param metaId
     * @return
     */
    public String removeFlowMetaByMetaId(String flowId,String flowVersion,String metaId){
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(road.getCreatedBy())) {
                return "非法的操作权限！";
            }

            this.flowMetaDao.deleteFlowMetaByMetaId(road.getRoadId(),metaId);

        } else {
            return "无效的编辑状态！";
        }

        return "Y";
    }


    /**
     * 更新或者新增meta
     * @param metaDto
     * @return
     */
    public String mergeFlowMeta(FlowMetaDTO metaDto){
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(metaDto.getFlowId(),metaDto.getFlowVersion());

        if (road != null) {
            String uid = MpaasSession.getCurrentUser();
            if (uid == null || uid != null && !uid.equals(road.getCreatedBy())) {
                return "error:非法的操作权限！";
            }

            FlowMetadatas metaBean = null;

            if(StringUtils.isBlank(metaDto.getMetaId())){
                metaBean = new FlowMetadatas();
                metaBean.setMetaId(MpaasUtil.guuid());
            } else {
                metaBean = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),metaDto.getMetaId());
                //查询数据库中是否已经存在
                if(metaBean == null){
                    return "error:未找到记录，无法更新！";
                }
            }

            metaBean.setRoadId(road.getRoadId());
            metaBean.setSour(metaDto.getSour());
            metaBean.setMetaType(metaDto.getMetaType());
            metaBean.setMetaTxt(metaDto.getMetaTree() != null ? JSONObject.toJSONString(metaDto.getMetaTree()): null);

            this.flowMetaDao.mergeFlowMeta(metaBean);

            return metaBean.getMetaId();

        } else {
            return "error:无效的编辑状态！";
        }
    }


    /**
     * 获取FlowMeata 状态自动识别
     * @param flowId
     * @param flowVersion
     * @param metaId
     * @return
     */
    public FlowMetaDTO getFlowMeta(String flowId,String flowVersion,String metaId){
        //优先获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
        if(road == null){
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
        }

        if(road == null){
            return null;
        } else {
            FlowMetadatas metaBean = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),metaId);
            return this.covertMetaToDTO(metaBean,flowId,flowVersion);
        }
    }

    public List<FlowMetaDTO> getAllFlowMeta(String flowId,String flowVersion){
        //优先获取正在编辑的
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);
        if(road == null){
            //没有开启编辑状态，则获取保存状态的
            road = this.flowRoadDao.getSavedFlowRoad(flowId,flowVersion);
        }

        if(road == null){
            return null;
        } else {
            List<FlowMetadatas> metaList = this.flowMetaDao.getAllFlowMeta(road.getRoadId());
            List<FlowMetaDTO> dtoList = new ArrayList<FlowMetaDTO>();
            if(metaList !=null && !metaList.isEmpty()) {
                for(FlowMetadatas metaBean : metaList){
                    dtoList.add(this.covertMetaToDTO(metaBean,flowId,flowVersion));
                }
            }
            return dtoList;
        }
    }

    /**
     * 转换为metadto
     * @param metaBean
     * @param flowId
     * @param flowVersion
     * @return
     */
    private FlowMetaDTO covertMetaToDTO(FlowMetadatas metaBean,String flowId,String flowVersion){
        if(metaBean != null){
            FlowMetaDTO metaDto = new FlowMetaDTO();
            metaDto.setFlowId(flowId);
            metaDto.setFlowVersion(flowVersion);
            metaDto.setMetaId(metaBean.getMetaId());
            metaDto.setMetaType(metaBean.getMetaType());
            metaDto.setSour(metaBean.getSour());
            if(StringUtils.isNotBlank(metaBean.getMetaTxt())){
                metaDto.setMetaTree(JSONObject.parseObject(metaBean.getMetaTxt(),DataNodeDTO.class));
            }
            return metaDto;
        } else {
            return null;
        }
    }


}
