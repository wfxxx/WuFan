package com.definesys.dsgc.service.envinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/envinfo")
public class EnvInfoController {
    @Autowired
    private EnvInfoService envInfoService;
}
