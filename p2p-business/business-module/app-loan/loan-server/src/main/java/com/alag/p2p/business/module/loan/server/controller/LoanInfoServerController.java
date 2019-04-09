package com.alag.p2p.business.module.loan.server.controller;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.feign.controller.BidFeignService;
import com.alag.p2p.business.module.loan.api.LoanInfoController;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.server.service.LoanInfoService;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.api.model.User;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.LogManager;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("p2p/loan")
public class LoanInfoServerController implements LoanInfoController {
    private Logger logger = LogManager.getLogger(LoanInfoServerController.class);


    @Autowired
    private RedisService redisService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidFeignService bidFeignService;
    @Autowired
    private UserFeignService userFeignService;


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

    @GetMapping("page")
    @Override
    public ServerResponse<Map<String,Object>> loan(Integer currentPage, Integer pType) {
        logger.info("currentPage => " + currentPage);
        logger.info("pType => " + pType);

        PageHelper.startPage(currentPage, 9);
        List<LoanInfo> loanInfoList = loanInfoService.getAllByType(pType);
        PageInfo<LoanInfo> pageInfo = new PageInfo<>(loanInfoList);

        Map<String,Object> retMap = new HashMap();
        retMap.put(Constants.TOTAL_ROWS, pageInfo.getTotal());
        retMap.put(Constants.TOTAL_PAGE, pageInfo.getPages());
        retMap.put(Constants.LOANINFO_LIST, pageInfo.getList());
        retMap.put(Constants.CURRENT_PAGE, pageInfo.getPageNum());


        return ServerResponse.createBySuccess(retMap);
    }

    @GetMapping("loanInfo")
    @Override
    public ServerResponse<Map<String, Object>> loanInfo(HttpServletRequest request, @Param("loanId") Integer loanId) {

        logger.info("loanInfoId => " + loanId);
        ServerResponse<LoanInfo> loanInfo = loanInfoService.getLoanInfoById(loanId);

        List<BidInfo> bidInfoList = bidFeignService.queryBidInfoByLoanId(loanId).getData();

        //获取当前用户的信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("loanInfo", loanInfo.getData());
        retMap.put("bidInfoList", bidInfoList);


        //判断用户是否登录
        if (null != sessionUser) {

            //获取当前用户的帐户可用余额
            FinanceAccount financeAccount = userFeignService.queryFinanceAccount(request).getData();
            retMap.put("financeAccount", financeAccount);
        }

        return ServerResponse.createBySuccess(retMap);
    }

    @RequestMapping("queryLoanById")
    @Override
    public ServerResponse<LoanInfo> queryLoanById(@RequestParam Integer loanId) {
        logger.info("loanId => " + loanId);
        return loanInfoService.getLoanInfoById(loanId);
    }

    @PostMapping("modifyLeftProductMoneyByLoanId")
    @Override
    public ServerResponse modifyLeftProductMoneyByLoanId(@RequestBody Map paramMap) {

        return loanInfoService.updateLPMoneyById(paramMap);
    }

    @Override
    public ServerResponse updateSelectiveById(LoanInfo loanInfo) {
        return loanInfoService.updateLPById(loanInfo);
    }
}
