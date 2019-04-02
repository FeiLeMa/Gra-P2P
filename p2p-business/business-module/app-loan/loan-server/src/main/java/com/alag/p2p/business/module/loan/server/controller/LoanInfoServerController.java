package com.alag.p2p.business.module.loan.server.controller;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.loan.api.LoanInfoController;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.server.mapper.LoanInfoMapper;
import com.alag.p2p.business.module.loan.server.service.LoanInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.LogManager;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("p2p/loan")
public class LoanInfoServerController implements LoanInfoController {
    private Logger logger = LogManager.getLogger(LoanInfoServerController.class);


    @Autowired
    public RedisService redisService;
    @Autowired
    public LoanInfoService loanInfoService;

    /**
     * 获取历史年化收益率
     * @return
     */
    @GetMapping("queryHistoryAverageRate")
    @Override
    public ServerResponse<Double> queryHistoryAverageRate() {

        Double hisAvgRate = loanInfoService.getHisAvgRate();

        return ServerResponse.createBySuccess(hisAvgRate);
    }

    /**
     * 产品展示
     * @param paramMap
     * @return
     */

    @PostMapping(value = "queryLoanInfoListByProductType", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ServerResponse<List<LoanInfo>> queryLoanInfoListByProductType(@RequestBody Map<String, Object> paramMap) {

        logger.info(paramMap.get("productType") + " " + paramMap.get("pageSize"));

        List<LoanInfo> loanInfoList = loanInfoService.getListByPType(paramMap);

        return ServerResponse.createBySuccess(loanInfoList);
    }
}
