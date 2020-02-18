package com.definesys.dsgc.service.prowork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName ProWorkService
 * @Description TODO
 * @Author Xueyunlong
 * @Date 2020-2-18 9:06
 * @Version 1.0
 **/

@Service
@Transactional
public class ProWorkService {

    @Autowired
    private ProWorkDao proWorkDao ;
}
