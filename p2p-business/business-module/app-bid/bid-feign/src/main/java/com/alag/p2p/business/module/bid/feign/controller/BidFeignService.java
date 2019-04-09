package com.alag.p2p.business.module.bid.feign.controller;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@FeignClient("app-bid")
public interface BidFeignService extends BidInfoController {

}
