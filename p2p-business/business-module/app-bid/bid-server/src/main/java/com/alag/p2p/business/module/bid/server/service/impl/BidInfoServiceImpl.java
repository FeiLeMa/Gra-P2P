package com.alag.p2p.business.module.bid.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.core.common.tools.ObjToOne;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.server.mapper.BidInfoMapper;
import com.alag.p2p.business.module.bid.server.service.BidInfoService;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.feign.controller.LoanFeignService;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LoanFeignService loanFeignService;
    @Autowired
    private UserFeignService userFeignService;

    @Override
    public Double getAllBidMoney() {//获取指定key的操作对象

        //获取指定key的value值
        Object objV = redisService.get(Constants.ALL_BID_MONEY);
        Double allBidMoney = null;
        //判断是否有值
        if (null == objV) {
            //去数据库查询
            allBidMoney = bidInfoMapper.selectAllBidMoney();

            //存放到redis缓存中
            redisService.set(Constants.ALL_BID_MONEY, allBidMoney, 15L);
        }

        return objV==null?allBidMoney:ObjToOne.objToDouble(objV);
    }

    @Override
    public List<BidInfo> getBidInfoByLoanId(Integer loanId) {

        return bidInfoMapper.selectAllBidInfoByLoanId(loanId);
    }

    @LcnTransaction
    @Override
    public ServerResponse userInvest(Map<String, Object> paramMap) {
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
                bidInfo.setBidTime(new Date());
                //投资状态
                bidInfo.setBidStatus(1);
                int insertBidCount = bidInfoMapper.insertSelective(bidInfo);
                if (insertBidCount > 0) {
//                    int a = 10 / 0;
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
