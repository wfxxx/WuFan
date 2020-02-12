package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppsTopologyDao {
    @Autowired
    private MpaasQueryFactory sw;

    public List<TopologyVO> getTopology(TopologyVO topologyVO) {
        if ("true".equals(topologyVO.getIsInterface())) {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_V").
                    eq("subordinate_system", topologyVO.getSubordinateSystem()).
                    eq("sys_code", topologyVO.getSysCode()).

                    doQuery(TopologyVO.class);
        } else {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_V").
                    or().
                    eq("subordinate_system", topologyVO.getSubordinateSystem()).
                    eq("sys_code", topologyVO.getSysCode()).
//                    orderBy("subordinate_system","ASC").
        doQuery(TopologyVO.class);
        }

    }
}
