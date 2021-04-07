package com.definesys.dsgc.service.flow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/flow")
public class FlowController {
    @Autowired
    private FlowService flowService;


}
