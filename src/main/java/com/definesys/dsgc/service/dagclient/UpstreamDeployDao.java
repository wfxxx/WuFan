package com.definesys.dsgc.service.dagclient;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UpstreamDeployDao {
    @Autowired
    private MpaasQueryFactory sw;
}
