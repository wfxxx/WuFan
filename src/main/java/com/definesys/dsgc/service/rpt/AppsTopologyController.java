package com.definesys.dsgc.service.rpt;

import com.definesys.dsgc.service.rpt.bean.TopologyVO;
import com.definesys.mpaas.common.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dsgc/rpt")
@RestController
public class AppsTopologyController {
    @Autowired
    private AppsTopologyService appsTopologyService;

    @RequestMapping(value = "/getTopology",method = RequestMethod.POST)
    public Response getTopology(@RequestBody TopologyVO topologyVO) {
        return Response.ok().data(this.appsTopologyService.getAll(topologyVO));

    }
}
