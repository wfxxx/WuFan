package com.definesys.dsgc.service.interfacetest;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterfaceTestDao {
    @Autowired
    private MpaasQueryFactory sw;

}
