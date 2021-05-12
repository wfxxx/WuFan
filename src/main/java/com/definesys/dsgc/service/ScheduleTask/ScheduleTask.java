package com.definesys.dsgc.service.ScheduleTask;

import com.definesys.dsgc.service.rpt.AppsTopologyService;
import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableScheduling
@EnableAsync
public class ScheduleTask {
    @Autowired
    private AppsTopologyService appsTopologyService;
    @Async
    @Scheduled(cron="0 0 * * * ?")  //整点执行
    //定时计算系统之间接口的运行情况并持久化
    public void SystemRelationshipStatistics() throws InterruptedException {    //系统接口调用关系统计
        TopologyVO topologyVO = new TopologyVO();
        List<TopologyVO> result =appsTopologyService.getTopology(topologyVO);
        for (TopologyVO topologyVO1 : result) {
            boolean exist = appsTopologyService.exist(topologyVO1);
            if (exist) {
                appsTopologyService.delete(topologyVO1);
            }
            appsTopologyService.insertTopology(topologyVO1);
        }
    }

}
