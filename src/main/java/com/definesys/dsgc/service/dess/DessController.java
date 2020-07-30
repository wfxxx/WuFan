package com.definesys.dsgc.service.dess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DessController
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-7-28 14:35
 * @Version 1.0
 **/
@RestController
@RequestMapping("/dess")
public class DessController {

    @Autowired
    private DessService dessService;







}
