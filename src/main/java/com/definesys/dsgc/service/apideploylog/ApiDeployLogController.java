package com.definesys.dsgc.service.apideploylog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/apideploylog")
public class ApiDeployLogController
{
    @Autowired
    private ApiDeployLogService apiDeployLogService;
}
