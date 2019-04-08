package com.alag.p2p.business.module.bid.server.service;

import com.alag.p2p.business.module.bid.api.model.BidInfo;

import java.util.List;

public interface BidInfoService {
    Double getAllBidMoney();

    List<BidInfo> getBidInfoByLoanId(Integer bidInfoId);
}
