package com.alag.p2p.business.module.bid.server.controller;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("p2p/bid")
public class BidServerController implements BidInfoController {

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("queryAllBidMoney")
    @Override
    public ServerResponse<Double> queryAllBidMoney() {

        Double allBidMoney = bidInfoService.getAllBidMoney();


        return ServerResponse.createBySuccess(allBidMoney);
    }
}
