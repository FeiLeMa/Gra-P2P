package com.alag.p2p.business.module.bid.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("p2p/bid")
public interface BidInfoController {

    @RequestMapping("queryAllBidMoney")
    ServerResponse<Double> queryAllBidMoney();
}
