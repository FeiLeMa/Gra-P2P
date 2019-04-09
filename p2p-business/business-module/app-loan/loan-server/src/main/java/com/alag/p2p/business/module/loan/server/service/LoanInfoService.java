package com.alag.p2p.business.module.loan.server.service;


import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {

    Double getHisAvgRate();

    List<LoanInfo> getListByPType(Map<String, Object> paramMap);

    List<LoanInfo> getAllByType(Integer pType);

    ServerResponse<LoanInfo> getLoanInfoById(Integer loanId);

    ServerResponse updateLPMoneyById(Map paramMap);

    ServerResponse updateLPById(LoanInfo loanInfo);
}
