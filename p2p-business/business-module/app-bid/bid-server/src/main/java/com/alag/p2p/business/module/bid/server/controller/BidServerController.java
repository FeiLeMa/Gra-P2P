package com.alag.p2p.business.module.bid.server.controller;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.feign.controller.LoanFeignService;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("p2p/bid")
public class BidServerController implements BidInfoController {
    public static final Logger logger = LogManager.getLogger(BidServerController.class);

    @Autowired
    private BidInfoService bidInfoService;
    @Autowired
    private LoanFeignService loanFeignService;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private RedisService redisService;

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
    public ServerResponse invest(@RequestBody Map<String, Object> paramMap) {
        logger.info("paramMap => " + paramMap);
        //获取产品的版本号
        logger.info("paramMap.loanId = " + paramMap.get("loanId"));
        logger.info("(Integer)paramMap.loanId = " + (Integer)paramMap.get("loanId"));

        LoanInfo loanInfo = loanFeignService.queryLoanById((Integer) paramMap.get("loanId")).getData();
        paramMap.put("version", loanInfo.getVersion());

        //更新产品剩余可投金额
        ServerResponse modifyLeftProductMoney = loanFeignService.modifyLeftProductMoneyByLoanId(paramMap);

        if (modifyLeftProductMoney.getStatus() == 0) {

            //更新帐户可用余额
            ServerResponse updateFinanceAccoun = userFeignService.updateFinanceAccountByBid(paramMap);

            if (updateFinanceAccoun.getStatus() == 0) {
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                //用户标识
                bidInfo.setUid((Integer) paramMap.get("uid"));
                //产品标识
                bidInfo.setLoanId((Integer) paramMap.get("loanId"));
                //投资金额
                bidInfo.setBidMoney((Double) paramMap.get("bidMoney"));
                //投资时间
                bidInfo.setBidTime(new Date());
                //投资状态
                bidInfo.setBidStatus(1);

                ServerResponse insertBidCount = bidInfoService.insertBidInfo(bidInfo);

                if (insertBidCount.getStatus() == 0) {

                    //再次查看产品的剩余可投金额是否为0
                    LoanInfo loanDetail = loanFeignService.queryLoanById((Integer) paramMap.get("loanId")).getData();

                    //为0：更新产品的状态及满标时间
                    if (0 == loanDetail.getLeftProductMoney()) {

                        //更新产品的状态及满标时间
                        LoanInfo updateLoanInfo = new LoanInfo();
                        updateLoanInfo.setId(loanDetail.getId());
                        updateLoanInfo.setProductFullTime(new Date());
                        updateLoanInfo.setProductStatus(1);//0未满标，1已满标，2满标且生成收益讲

                        ServerResponse updateLoanInfoCount = loanFeignService.updateSelectiveById(updateLoanInfo);

                        if (updateLoanInfoCount.getStatus() == 1) {
                            return ServerResponse.createByErrorMessage("更新产品满标状态失败");
                        }
                    }
                    String phone = (String) paramMap.get("phone");

                    //将用户的投资金额存放到redis缓存中
                    redisService.zIncrementScore(Constants.INVEST_TOP, phone, (Double) paramMap.get("bidMoney"));


                } else {
                    return ServerResponse.createByErrorMessage("新增投资记录失败");
                }

            } else {
                return ServerResponse.createByErrorMessage("更新账户可用余额失败");
            }


        } else {
            return ServerResponse.createByErrorMessage("更新剩余可投金额失败");
        }

        return ServerResponse.createBySuccessMessage("投资成功");
    }
}
