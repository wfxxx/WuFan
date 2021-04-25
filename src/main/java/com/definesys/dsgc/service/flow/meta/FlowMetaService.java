package com.definesys.dsgc.service.flow.meta;

import com.alibaba.fastjson.JSONObject;
import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.dsgc.service.flow.bean.FlowNodes;
import com.definesys.dsgc.service.flow.bean.FlowRoads;
import com.definesys.dsgc.service.flow.meta.dto.DataNodeDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaDTO;
import com.definesys.dsgc.service.flow.meta.dto.FlowMetaGenerateReqDTO;
import com.definesys.dsgc.service.flow.node.FlowNodeDao;
import com.definesys.dsgc.service.flow.road.FlowRoadDao;
import com.definesys.dsgc.service.flow.utils.FlowUtils;
import com.definesys.dsgc.service.svcgen.bean.TmplConfigBean;
import com.definesys.dsgc.service.svcgen.utils.ServiceGenerateProxy;
import com.definesys.dsgc.service.svcgen.utils.TreeNode;
import com.definesys.mpaas.common.util.MpaasUtil;
import com.definesys.mpaas.query.session.MpaasSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowMetaService {

    @Autowired
    private FlowMetaDao flowMetaDao;

    @Autowired
    private FlowRoadDao flowRoadDao;

    @Autowired
    private FlowNodeDao flowNodeDao;

    private ServiceGenerateProxy sgProxy = ServiceGenerateProxy.newInstance();


    /**
     * 根据metaid删除metadata
     * @param flowId
     * @param flowVersion
     * @param metaId
     * @return
     */
    public String removeFlowMetaByMetaId(String flowId,String flowVersion,String metaId) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(flowId,flowVersion);

        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return checRes;
        }

        this.flowMetaDao.deleteFlowMetaByMetaId(road.getRoadId(),metaId);

        return "Y";
    }


    /**
     * 更新或者新增meta
     * @param metaDto
     * @return
     */
    public String mergeFlowMeta(FlowMetaDTO metaDto) {
        FlowRoads road = this.flowRoadDao.getEditingFlowRoad(metaDto.getFlowId(),metaDto.getFlowVersion());

        String checRes = FlowUtils.checkFlowEditingUserAuth(road);
        if (!"Y".equals(checRes)) {
            return "error:" + checRes;
        }

        FlowMetadatas metaBean = null;

        if (StringUtils.isBlank(metaDto.getMetaId())) {
            metaBean = new FlowMetadatas();
            metaBean.setMetaId(MpaasUtil.guuid());
        } else {
            metaBean = this.flowMetaDao.getFlowMetaByMetaId(road.getRoadId(),metaDto.getMetaId());
            //查询数据库中是否已经存在
            if (metaBean == null) {
                return "error:未找到记录，无法更新！";
            }
        }

        metaBean.setRoadId(road.getRoadId());
        metaBean.setSour(metaDto.getSour());
        metaBean.setMetaType(metaDto.getMetaType());
        metaBean.setMetaName(metaDto.getMetaName());
        metaBean.setMetaTxt(metaDto.getMetaTree() != null ? JSONObject.toJSONString(metaDto.getMetaTree()) : null);

        this.flowMetaDao.mergeFlowMeta(metaBean);

        return metaBean.getMetaId();

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
            metaDto.setMetaName(metaBean.getMetaName());
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


    /**
     * 根据节点的配置生成参数结构
     * @param param
     * @return
     */
    public FlowMetaDTO generateNodeMeta(FlowMetaGenerateReqDTO param,FlowRoads road){

       FlowNodes node = this.flowNodeDao.getFlowNode(road.getRoadId(),param.getNodeId());

       FlowMetaDTO meta = new FlowMetaDTO();
       meta.setFlowId(param.getFlowId());
       meta.setFlowVersion(param.getFlowVersion());
       meta.setSour(param.getNodeId());


       meta.setMetaType("XML");
       String jsonInput ="{\n" +
               "            \"id\": \"QueryParams\",\n" +
               "            \"title\": \"QueryParams\",\n" +
               "            \"key\": \"ff9a8228-5cf1-4846-8805-e4f1f5c26e65\",\n" +
               "            \"expanded\": true,\n" +
               "            \"children\": [\n" +
               "                {\n" +
               "                    \"id\": \"QueryParams.bsId\",\n" +
               "                    \"title\": \"bsId\",\n" +
               "                    \"key\": \"19ab30c0-d695-4078-ba5b-206a37719dce\",\n" +
               "                    \"expanded\": false,\n" +
               "                    \"children\": null,\n" +
               "                    \"valueType\": \"String\",\n" +
               "                    \"minOccur\": \"1\",\n" +
               "                    \"maxOccur\": \"1\",\n" +
               "                    \"leaf\": true\n" +
               "                },\n" +
               "                {\n" +
               "                    \"id\": \"QueryParams.tstId\",\n" +
               "                    \"title\": \"tstId\",\n" +
               "                    \"key\": \"af51f5f6-42e4-4b7e-811a-e899aeb5fa73\",\n" +
               "                    \"expanded\": false,\n" +
               "                    \"children\": null,\n" +
               "                    \"valueType\": \"String\",\n" +
               "                    \"minOccur\": \"1\",\n" +
               "                    \"maxOccur\": \"1\",\n" +
               "                    \"leaf\": true\n" +
               "                }\n" +
               "            ],\n" +
               "            \"valueType\": \"Object\",\n" +
               "            \"minOccur\": \"1\",\n" +
               "            \"maxOccur\": \"1\",\n" +
               "            \"leaf\": false\n" +
               "        }";
//
       String jsonOutput = "{\n" +
               "            \"id\": \"DagBsCollection\",\n" +
               "            \"title\": \"DagBsCollection\",\n" +
               "            \"key\": \"8fc57be8-fa1e-4999-be61-6a67f5af5211\",\n" +
               "            \"expanded\": true,\n" +
               "            \"children\": [\n" +
               "                {\n" +
               "                    \"id\": \"DagBsCollection.DagBs\",\n" +
               "                    \"title\": \"DagBs\",\n" +
               "                    \"key\": \"99ec5502-c480-4848-841f-c6d5dd846fdd\",\n" +
               "                    \"expanded\": true,\n" +
               "                    \"children\": [\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.bsId\",\n" +
               "                            \"title\": \"bsId\",\n" +
               "                            \"key\": \"2ae9bca0-008b-40f0-be13-98ca9acd15fd\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"1\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.bsCode\",\n" +
               "                            \"title\": \"bsCode\",\n" +
               "                            \"key\": \"c338e2da-b1ee-4533-a631-3b93ad2f4e52\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.bsDesc\",\n" +
               "                            \"title\": \"bsDesc\",\n" +
               "                            \"key\": \"2ecc2942-f23a-4ee6-bead-1f6b3d091d3a\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.appCode\",\n" +
               "                            \"title\": \"appCode\",\n" +
               "                            \"key\": \"5635dba1-b4a2-4cb5-a929-e74196276405\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.createdBy\",\n" +
               "                            \"title\": \"createdBy\",\n" +
               "                            \"key\": \"12057ad7-8ef7-4b21-9407-7d8a5b9caed0\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.creationDate\",\n" +
               "                            \"title\": \"creationDate\",\n" +
               "                            \"key\": \"c7da1a4f-7eb1-4425-abb7-7bc42fba62cd\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Date\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.lastUpdatedBy\",\n" +
               "                            \"title\": \"lastUpdatedBy\",\n" +
               "                            \"key\": \"b4e5e03a-8209-4cb1-b964-c3c850087c4a\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"String\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.lastUpdateDate\",\n" +
               "                            \"title\": \"lastUpdateDate\",\n" +
               "                            \"key\": \"7aedd3ee-5cb1-4776-9f0f-bc4a94a4505c\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Date\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.objectVersionNumber\",\n" +
               "                            \"title\": \"objectVersionNumber\",\n" +
               "                            \"key\": \"d748aa7d-04dc-4158-9fbb-426cb0ad265b\",\n" +
               "                            \"expanded\": false,\n" +
               "                            \"children\": null,\n" +
               "                            \"valueType\": \"Number\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": true\n" +
               "                        },\n" +
               "                        {\n" +
               "                            \"id\": \"DagBsCollection.DagBs.sfsfs\",\n" +
               "                            \"title\": \"sfsfs\",\n" +
               "                            \"key\": \"86f8c797-7e1e-4e8e-ac27-11211d678498\",\n" +
               "                            \"expanded\": true,\n" +
               "                            \"children\": [\n" +
               "                                {\n" +
               "                                    \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl\",\n" +
               "                                    \"title\": \"DagBsDtl\",\n" +
               "                                    \"key\": \"2fcdf783-af56-4356-aa02-98f24d1bb06c\",\n" +
               "                                    \"expanded\": true,\n" +
               "                                    \"children\": [\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.vid\",\n" +
               "                                            \"title\": \"vid\",\n" +
               "                                            \"key\": \"e61f34f7-558a-4e29-b277-fa96a15580ab\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.protocal\",\n" +
               "                                            \"title\": \"protocal\",\n" +
               "                                            \"key\": \"811b37bb-287a-49a5-97ff-f5f786554643\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.hostName\",\n" +
               "                                            \"title\": \"hostName\",\n" +
               "                                            \"key\": \"4e607d22-e2c0-4eeb-b218-c4701fafd6e5\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.port\",\n" +
               "                                            \"title\": \"port\",\n" +
               "                                            \"key\": \"0904ab9b-1628-491c-8931-917cc8568ea7\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.paths\",\n" +
               "                                            \"title\": \"paths\",\n" +
               "                                            \"key\": \"0638a7d3-a144-4dfc-8786-d45349d66730\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.rtyCount\",\n" +
               "                                            \"title\": \"rtyCount\",\n" +
               "                                            \"key\": \"2144739a-fe1f-4517-b9c8-78abacb2f46a\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.connectTimeout\",\n" +
               "                                            \"title\": \"connectTimeout\",\n" +
               "                                            \"key\": \"54e45ee6-31f9-444a-9cdf-e55e3147ff68\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.sendTimeout\",\n" +
               "                                            \"title\": \"sendTimeout\",\n" +
               "                                            \"key\": \"aaec6e24-dbe5-4a35-8a97-e72fb4523655\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.readTimeout\",\n" +
               "                                            \"title\": \"readTimeout\",\n" +
               "                                            \"key\": \"1859a59e-d096-4a8e-b670-f317ff8d3b58\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.createdBy\",\n" +
               "                                            \"title\": \"createdBy\",\n" +
               "                                            \"key\": \"a05d6e3a-6140-48b4-a8f3-d710819c7ec7\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.creationDate\",\n" +
               "                                            \"title\": \"creationDate\",\n" +
               "                                            \"key\": \"7c7886ca-86f6-467f-acbb-e4d5ca36e77d\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Date\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.lastUpdatedBy\",\n" +
               "                                            \"title\": \"lastUpdatedBy\",\n" +
               "                                            \"key\": \"71d9393f-7366-4c0b-8993-4155c465fe32\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"String\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.lastUpdateDate\",\n" +
               "                                            \"title\": \"lastUpdateDate\",\n" +
               "                                            \"key\": \"3dc1ae79-793a-40e4-a383-081616d4b9e9\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Date\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        },\n" +
               "                                        {\n" +
               "                                            \"id\": \"DagBsCollection.DagBs.sfsfs.DagBsDtl.objectVersionNumber\",\n" +
               "                                            \"title\": \"objectVersionNumber\",\n" +
               "                                            \"key\": \"a1f8e747-cc69-4fdc-9beb-bb786cdcc12b\",\n" +
               "                                            \"expanded\": false,\n" +
               "                                            \"children\": null,\n" +
               "                                            \"valueType\": \"Number\",\n" +
               "                                            \"minOccur\": \"0\",\n" +
               "                                            \"maxOccur\": \"1\",\n" +
               "                                            \"leaf\": true\n" +
               "                                        }\n" +
               "                                    ],\n" +
               "                                    \"valueType\": \"Object\",\n" +
               "                                    \"minOccur\": \"0\",\n" +
               "                                    \"maxOccur\": \"2147483647\",\n" +
               "                                    \"leaf\": false\n" +
               "                                }\n" +
               "                            ],\n" +
               "                            \"valueType\": \"Object\",\n" +
               "                            \"minOccur\": \"0\",\n" +
               "                            \"maxOccur\": \"1\",\n" +
               "                            \"leaf\": false\n" +
               "                        }\n" +
               "                    ],\n" +
               "                    \"valueType\": \"Object\",\n" +
               "                    \"minOccur\": \"0\",\n" +
               "                    \"maxOccur\": \"2147483647\",\n" +
               "                    \"leaf\": false\n" +
               "                }\n" +
               "            ],\n" +
               "            \"valueType\": \"Object\",\n" +
               "            \"minOccur\": \"1\",\n" +
               "            \"maxOccur\": \"1\",\n" +
               "            \"leaf\": false\n" +
               "        }";

       if("INPUT".equals(param.getInOrOut())) {
           meta.setMetaTree(JSONObject.parseObject(jsonInput,DataNodeDTO.class));
       } else {
           meta.setMetaTree(JSONObject.parseObject(jsonOutput,DataNodeDTO.class));
       }


//       if(node != null && "DB".equals(node.getCnptCode())){
//           //解析db请求的参数
//           meta.setMetaType("XML");
//           this.createSampleMetaForDBConnector(param,road,node,meta);
//
//       }

       return meta;
    }


    private void createSampleMetaForDBConnector(FlowMetaGenerateReqDTO param,FlowRoads road,FlowNodes node,FlowMetaDTO meta) {
        if (node.getParams() == null) {
            return;
        }
        try {

            TmplConfigBean tcb = JSONObject.parseObject(node.getParams(),TmplConfigBean.class);
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("inOrOut",param.getInOrOut());
            String treeJsonTxt = this.sgProxy.createXsdTree(MpaasSession.getCurrentUser(),tcb,paramMap);
            if (treeJsonTxt != null) {
                meta.setMetaTree(JSONObject.parseObject(treeJsonTxt,DataNodeDTO.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
