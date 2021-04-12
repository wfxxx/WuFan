package com.definesys.dsgc.service.flow.meta;

import com.definesys.dsgc.service.flow.bean.FlowMetadatas;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowMetaDao {

    @Autowired
    private MpaasQueryFactory sw;


    public void deleteFlowMetaByMetaId(String roadId,String metaId){
        if(StringUtils.isNotBlank(metaId)
        && StringUtils.isNotBlank(roadId)){
            this.sw.buildQuery().eq("roadId",roadId)
                    .eq("metaId",metaId)
                    .doDelete(FlowMetadatas.class);
        }
    }

    /**
     * 删除节点自动创建的meta
     * @param roadId
     * @param nodeId
     */
    public void deleteFlowNodeMetaByNodeId(String roadId,String nodeId) {
        if (StringUtils.isNotBlank(roadId)
                && StringUtils.isNotBlank(nodeId)) {
            this.sw.buildQuery().eq("roadId",roadId)
                    .eq("sour",nodeId)
                    .doDelete(FlowMetadatas.class);
        }
    }

    /**
     * 根据metaid获取meta
      * @param roadId
     * @param metaId
     * @return
     */
    public FlowMetadatas getFlowMetaByMetaId(String roadId,String metaId) {
        if (StringUtils.isBlank(roadId) || StringUtils.isBlank(metaId)) {
            return null;
        } else {
            return this.sw.buildQuery().eq("roadId",roadId)
                    .eq("metaId",metaId)
                    .doQueryFirst(FlowMetadatas.class);
        }
    }

    /**
     * 根据roadId获取所有的meta
     * @param roadId
     * @return
     */
    public List<FlowMetadatas> getAllFlowMeta(String roadId){
        if (StringUtils.isBlank(roadId)) {
            return null;
        } else {
            return this.sw.buildQuery().eq("roadId",roadId)
                    .doQuery(FlowMetadatas.class);
        }
    }


    public void mergeFlowMeta(FlowMetadatas meta){
        if(meta != null){
            if(StringUtils.isNotBlank(meta.getFmId())){
                this.sw.buildQuery().eq("fmId",meta.getFmId()).doUpdate(meta);
            } else {
                this.sw.buildQuery().doInsert(meta);
            }
        }
    }






}
