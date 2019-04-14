package com.alag.p2p.business.module.loan.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("p2p/loan")
public interface LoanInfoController {

    @GetMapping("queryHistoryAverageRate")
    ServerResponse<Double> queryHistoryAverageRate();

    @PostMapping(value = "queryLoanInfoListByProductType", produces = MediaType.APPLICATION_JSON_VALUE)
    ServerResponse<List<LoanInfo>> queryLoanInfoListByProductType(@RequestBody Map<String, Object> paramMap);

    @GetMapping("page")
    ServerResponse<Map<String,Object>> loan(@RequestParam("currentPage") Integer currentPage,
                        @RequestParam("pType") Integer pType);

    @GetMapping("loanInfo")
    ServerResponse<Map<String, Object>> loanInfo(HttpServletRequest request, @RequestParam("loanId") Integer loanId);

    @RequestMapping("queryLoanById")
    ServerResponse<LoanInfo> queryLoanById(@RequestParam("loanId") Integer loanId);

    @PostMapping("modifyLeftProductMoneyByLoanId")
    ServerResponse modifyLeftProductMoneyByLoanId(@RequestBody Map paramMap);

    @PutMapping("updateSelectiveById")
    ServerResponse updateSelectiveById(@RequestBody LoanInfo loanInfo);

    @GetMapping("selectLoanInfoByProductStatus")
    ServerResponse<List<LoanInfo>> selectLoanInfoByProductStatus(@RequestParam("i") int i);
}
