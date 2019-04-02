package com.alag.p2p.business.module.user.feign.controller;

import com.alag.p2p.business.module.user.api.UserController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient("app-user")
public interface UserFeignService extends UserController {
}
