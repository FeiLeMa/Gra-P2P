package com.alag.p2p.business.module.user.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("p2p/user")
public interface UserController {

    @RequestMapping("queryAllUserCount")
    ServerResponse<Long> queryAllUserCount();


}
