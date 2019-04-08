package com.alag.p2p.business.module.bid.server.controller;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("p2p/bid")
public class BidServerController implements BidInfoController {
    public static final Logger logger = LogManager.getLogger(BidServerController.class);

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping("queryAllBidMoney")
    @Override
    public ServerResponse<Double> queryAllBidMoney() {

        Double allBidMoney = bidInfoService.getAllBidMoney();


        return ServerResponse.createBySuccess(allBidMoney);
    }

    @GetMapping("queryBidInfoByLoanId")
    @Override
    public ServerResponse<List<BidInfo>> queryBidInfoByLoanId(@RequestParam("loanId") Integer loanId) {
        logger.info("loanId => " + loanId);

        List<BidInfo> bidInfoList = bidInfoService.getBidInfoByLoanId(loanId);

        return ServerResponse.createBySuccess(bidInfoList);
    }
}
