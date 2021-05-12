package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.ScheduleTask.ScheduleTask;
import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.dsgc.service.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppsTopologyService {
    @Autowired
    private AppsTopologyDao appsTopologyDao;

    public  List<TopologyVO> SystemRelationshipData = new ArrayList<>();

    public List<TopologyVO> getTopology(TopologyVO topologyVO) {
        SystemRelationshipData = appsTopologyDao.getTopology(topologyVO);
        return SystemRelationshipData;
    }

    public List<TopologyVO> getTopologyLastHour(TopologyVO topologyVO) {
        List<TopologyVO> result = appsTopologyDao.getTopologyLastHour(topologyVO);
        return result;
    }

    public List<TopologyVO> getAll(TopologyVO topologyVO) {
        List<TopologyVO> resultAll = null;
        if(StringUtil.isBlank(topologyVO.getIsInterface())){
            resultAll = appsTopologyDao.getTopology(topologyVO);
        }else{
            List<TopologyVO> resultNow = getTopologyLastHour(topologyVO);
            resultAll = resultNow;

        }
        return resultAll;
    }

    public void insertTopology(TopologyVO topologyVO){
        appsTopologyDao.insertTopology(topologyVO);
    }

    public boolean exist(TopologyVO topologyVO){
        return appsTopologyDao.exist(topologyVO);
    }

    public void delete (TopologyVO topologyVO){
        appsTopologyDao.delete(topologyVO);
    }

    public boolean compare(TopologyVO topologyVO1,TopologyVO topologyVO2){
        boolean result =true;
        if(! topologyVO1.getServNo().equals(topologyVO2.getServNo())){
            return false;
        }
        if(! topologyVO1.getSubordinateSystem().equals(topologyVO2.getSubordinateSystem())){
            return false;
        }
        if(! topologyVO1.getSysCode().equals(topologyVO2.getSysCode())){
            return false;
        }
        return result;
    }


}
