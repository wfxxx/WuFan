package com.definesys.dsgc.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "dsgc/umng")
public class UsersController {

    @Autowired
    private UsersService usersService;

}
