package com.definesys.dsgc.service.apilr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dsgc/apilr")
public class ApiLrController {
    @Autowired
    private ApiLrService apiLrService;
}
