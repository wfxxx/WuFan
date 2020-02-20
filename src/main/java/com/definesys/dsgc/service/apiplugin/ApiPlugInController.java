package com.definesys.dsgc.service.apiplugin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/apiplugin")
public class ApiPlugInController {
    @Autowired
    private ApiPlugInService apiPlugInService;
}
