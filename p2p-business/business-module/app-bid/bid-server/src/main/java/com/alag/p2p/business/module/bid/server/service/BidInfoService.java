package com.alag.p2p.business.module.bid.server.service;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.model.BidInfo;

import java.util.List;
import java.util.Map;

public interface BidInfoService {
    Double getAllBidMoney();

    List<BidInfo> getBidInfoByLoanId(Integer bidInfoId);


    ServerResponse userInvest(Map<String, Object> paramMap);
}
