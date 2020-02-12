package com.definesys.dsgc.service.users;

import com.definesys.mpaas.query.MpaasQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsersDao {
    @Autowired
    private MpaasQueryFactory sw;
}
