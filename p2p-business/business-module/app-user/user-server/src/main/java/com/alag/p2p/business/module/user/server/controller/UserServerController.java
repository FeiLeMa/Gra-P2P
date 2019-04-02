package com.alag.p2p.business.module.user.server.controller;


import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.user.api.UserController;
import com.alag.p2p.business.module.user.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("p2p/user")
public class UserServerController implements UserController {

    @Autowired
    public UserService userService;

    @RequestMapping("queryAllUserCount")
    @Override
    public ServerResponse<Long> queryAllUserCount() {

        Long userCount = userService.getAUserCount();

        return ServerResponse.createBySuccess(userCount);
    }
}
