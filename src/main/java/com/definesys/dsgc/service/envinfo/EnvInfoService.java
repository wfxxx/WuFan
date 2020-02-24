package com.definesys.dsgc.service.envinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvInfoService {
    @Autowired
    private EnvInfoDao envInfoDao;
}
