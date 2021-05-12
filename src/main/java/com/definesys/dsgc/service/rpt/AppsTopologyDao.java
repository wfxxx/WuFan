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
        } else if ("false".equals(topologyVO.getIsInterface())) {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_V").
                    or().
                    eq("subordinate_system", topologyVO.getSubordinateSystem()).
                    eq("sys_code", topologyVO.getSysCode()).doQuery(TopologyVO.class);
        } else {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_V").doQuery(TopologyVO.class);
        }
    }

    public List<TopologyVO> getTopologyLastHour(TopologyVO topologyVO) {
        if ("true".equals(topologyVO.getIsInterface())) {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_lasthour_V").
                    eq("subordinate_system", topologyVO.getSubordinateSystem()).
                    eq("sys_code", topologyVO.getSysCode()).
                    doQuery(TopologyVO.class);
        } else {
            return this.sw.buildViewQuery("DSGC_TOPOLOGY_lasthour_V").
                    or().
                    eq("subordinate_system", topologyVO.getSubordinateSystem()).
                    eq("sys_code", topologyVO.getSysCode()).
//                    orderBy("subordinate_system","ASC").
        doQuery(TopologyVO.class);
        }
    }

    public List<TopologyVO> getAll() {
        return this.sw.buildQuery().doQuery(TopologyVO.class);
    }

    public void insertTopology(TopologyVO topologyVO) {
        this.sw.buildQuery().doInsert(topologyVO);
    }

    public boolean exist(TopologyVO topologyVO) {
        TopologyVO result = this.sw.buildQuery()
                .eq("serv_no", topologyVO.getServNo())
                .eq("subordinate_system", topologyVO.getSubordinateSystem())
                .eq("sys_code", topologyVO.getSysCode())
                .doQueryFirst(TopologyVO.class);
        if (result != null && result.getServNo() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void delete(TopologyVO topologyVO) {
        this.sw.buildQuery()
                .eq("serv_no", topologyVO.getServNo())
                .eq("subordinate_system", topologyVO.getSubordinateSystem())
                .eq("sys_code", topologyVO.getSysCode())
                .doDelete(TopologyVO.class);
    }
}
