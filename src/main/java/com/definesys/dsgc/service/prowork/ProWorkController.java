package com.definesys.dsgc.service.prowork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ProWorkController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 9:05
 * @Version 1.0
 **/


@RestController
@RequestMapping(value = "dsgc/prowork")
public class ProWorkController {

    @Autowired
    private ProWorkService proWorkService;
}
