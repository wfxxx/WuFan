package com.definesys.dsgc.service.apibs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "dsgc/apibs")
public class ApiBsController {
    @Autowired
    private BackenMngService backenMngService;
}
