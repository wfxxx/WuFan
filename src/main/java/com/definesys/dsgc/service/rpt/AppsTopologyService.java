package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppsTopologyService {
    @Autowired
    private AppsTopologyDao appsTopologyDao;

    public List<TopologyVO> getTopology(TopologyVO topologyVO) {
        return appsTopologyDao.getTopology(topologyVO);
    }
}
