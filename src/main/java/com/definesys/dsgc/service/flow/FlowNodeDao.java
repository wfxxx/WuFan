package com.definesys.dsgc.service.flow;


import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FlowNodeDao {
    @Autowired
    private MpaasQueryFactory sw;
}
