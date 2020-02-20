package com.definesys.dsgc.service.apiroute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/apiRoute")
public class ApiRouteController {
    @Autowired
    private ApiRouteService apiRouteService;

}
