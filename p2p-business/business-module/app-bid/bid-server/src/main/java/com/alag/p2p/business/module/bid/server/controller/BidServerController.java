package com.alag.p2p.business.module.bid.server.controller;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ServerResponse<List<BidInfo>> queryBidInfoByLoanId(@RequestParam(value = "loanId",required = true) Integer loanId) {
        logger.info("loanId => " + loanId);

        List<BidInfo> bidInfoList = bidInfoService.getBidInfoByLoanId(loanId);

        return ServerResponse.createBySuccess(bidInfoList);
    }

    @PostMapping("invest")
    @Override
    @HystrixCommand(fallbackMethod="error")
    public ServerResponse invest(@RequestBody Map<String, Object> paramMap) {
        logger.info("paramMap => " + paramMap);
        //获取产品的版本号
        logger.info("paramMap.loanId = " + paramMap.get("loanId"));
        logger.info("(Integer)paramMap.loanId = " + (Integer)paramMap.get("loanId"));

        if (paramMap == null) {
            return ServerResponse.createByErrorMessage("参数为空");
        }

        ServerResponse response = bidInfoService.userInvest(paramMap);
        return response;
    }

    public ServerResponse error(Map paraMap,Throwable throwable){
        return ServerResponse.createByErrorMessage(throwable.getMessage());
    }
}
