package com.alag.p2p.business.module.loan.feign.controller;

import com.alag.p2p.business.module.loan.api.LoanInfoController;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@FeignClient("app-loan")
public interface LoanFeignService extends LoanInfoController {
}
