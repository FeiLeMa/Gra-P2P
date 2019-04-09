package com.alag.p2p.business.module.bid.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import feign.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("p2p/bid")
public interface BidInfoController {

    @RequestMapping("queryAllBidMoney")
    ServerResponse<Double> queryAllBidMoney();

    @GetMapping("queryBidInfoByLoanId")
    ServerResponse<List<BidInfo>> queryBidInfoByLoanId(@RequestParam(value = "loanId",required = true) Integer loanId);

    @PostMapping("invest")
    ServerResponse invest(Map<String, Object> paramMap);
}
