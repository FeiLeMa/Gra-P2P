package com.alag.p2p.business.module.loan.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("p2p/loan")
public interface LoanInfoController {

    @GetMapping("queryHistoryAverageRate")
    ServerResponse<Double> queryHistoryAverageRate();

    @PostMapping(value = "queryLoanInfoListByProductType",produces = MediaType.APPLICATION_JSON_VALUE)
    ServerResponse<List<LoanInfo>> queryLoanInfoListByProductType(@RequestBody Map<String, Object> paramMap);


}
