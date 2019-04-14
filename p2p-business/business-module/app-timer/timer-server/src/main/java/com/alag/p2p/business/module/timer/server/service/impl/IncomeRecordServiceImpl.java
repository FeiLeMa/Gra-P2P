package com.alag.p2p.business.module.timer.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.core.common.tools.DateUtils;
import com.alag.p2p.business.module.bid.api.model.BidInfo;
import com.alag.p2p.business.module.bid.feign.controller.BidFeignService;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.feign.controller.LoanFeignService;
import com.alag.p2p.business.module.timer.server.mapper.IncomeRecordMapper;
import com.alag.p2p.business.module.timer.server.model.IncomeRecord;
import com.alag.p2p.business.module.timer.server.service.IncomeRecordService;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncomeRecordServiceImpl implements IncomeRecordService {

    private Logger logger = LogManager.getLogger(IncomeRecordServiceImpl.class);
    @Autowired
    private LoanFeignService loanFeignService;
    @Autowired
    private BidFeignService bidFeignService;
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private UserFeignService userFeignService;

    @LcnTransaction
    @Override
    public void generateIncomePlan() {
        //查询产品状态为1已满标的产品 -> 返回List<已满标产品>
        List<LoanInfo> loanInfoList = loanFeignService.selectLoanInfoByProductStatus(1).getData();

        //循环遍历，获取到每一个已满标产品
        for (LoanInfo loanInfo : loanInfoList) {

            //产品类型
            Integer productType = loanInfo.getProductType();

            //产品满标时间
            Date productFullTime = loanInfo.getProductFullTime();

            //产品周期
            Integer cycle = loanInfo.getCycle();

            //产品利率
            Double rate = loanInfo.getRate();

            logger.info("loanInfoId =>" + loanInfo.getId());
            //获取当前已满标产品的所有投资记录 -> 返回List<投资记录>
            List<BidInfo> bidInfoList = bidFeignService.queryBidInfoByLoanId(loanInfo.getId()).getData();

            //循环遍历投资记录List，获取到每一条的记录
            for (BidInfo bidInfo : bidInfoList) {

                Double bidMoney = bidInfo.getBidMoney();

                //将当前投资记录生成对应的收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                //用户标识
                incomeRecord.setUid(bidInfo.getUid());
                //产品标识
                incomeRecord.setLoanId(bidInfo.getLoanId());
                //投资记录标识
                incomeRecord.setBidId(bidInfo.getId());
                //投资金额
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                //收益状态:0未返还，1已返还
                incomeRecord.setIncomeStatus(0);


                //收益时间 = 产品满标时间 + 产品的周期(周期为天|月)
                Date incomeDate = null;

                //收益金额 = 投资金额 * 日利率 * 投资天数
                Double incomeMoney = null;

                //判断产品的类型
                if (Constants.PRODUCT_TYPE_X == productType) {
                    //新手宝产品(Date) = productFullTime(Date) + cycle(Integer)天;
                    incomeDate = DateUtils.getDateByAddDays(productFullTime, cycle);

                    incomeMoney = bidMoney * (rate / 100 / 365) * cycle;

                } else {
                    //优选和散标(Date) = productFullTime(Date) + cycle(Integer)月;
                    incomeDate = DateUtils.getDateByAddMonthes(productFullTime, cycle);

                    incomeMoney = bidMoney * (rate / 100 / 365) * cycle * 30;
                }

                incomeMoney = Math.round(incomeMoney * Math.pow(10, 2)) / Math.pow(10, 2);


                //收益日期
                incomeRecord.setIncomeDate(incomeDate);

                //收益金额
                incomeRecord.setIncomeMoney(incomeMoney);

                int insertCount = incomeRecordMapper.insertSelective(incomeRecord);

                if (insertCount > 0) {
                    logger.info("用户标识为" + bidInfo.getUid() + ",投资记录标识为" + bidInfo.getId() + ",生成收益计划成功");
                } else {

                    logger.info("用户标识为" + bidInfo.getUid() + ",投资记录标识为" + bidInfo.getId() + ",生成收益计划失败");
                }
            }


            //将当前循环遍历的产品状态更新为2满标且生成收益记录
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);
            ServerResponse response = loanFeignService.updateSelectiveById(updateLoanInfo);

            if (response.getStatus() == 0) {
                logger.info("产品标识为" + loanInfo.getId() + "修改状态为满标且成功收益计划成功");
            } else {
                logger.info("产品标识为" + loanInfo.getId() + "修改状态为满标且成功收益计划失败");

            }

        }
    }

    @LcnTransaction
    @Override
    public void generateIncomeBack() {
        //收益记录状态为0且收益时间与当前时间相同的收益记录 -> 返回List<收益记录>
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByIncomeStatus(0);

        //循环遍历收益记录
        for (IncomeRecord incomeRecord : incomeRecordList) {

            //准备参数
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("uid", incomeRecord.getUid());
            paramMap.put("bidMoney", incomeRecord.getBidMoney());
            paramMap.put("incomeMoney", incomeRecord.getIncomeMoney());

            //将当前收益记录的投资金额和收益金额返还给当前的用户，更新当前帐户的可用余额
            ServerResponse updateFinanceAccountCount = userFeignService.updateFinanceAccountByIncomeBack(paramMap);

            if (updateFinanceAccountCount.getStatus() == 0) {

                //将当前的收益记录的状态更新为1已返还
                IncomeRecord updateIncomeRecord = new IncomeRecord();
                updateIncomeRecord.setId(incomeRecord.getId());
                updateIncomeRecord.setIncomeStatus(1);
                int updateIncomeCount = incomeRecordMapper.updateByPrimaryKeySelective(updateIncomeRecord);

                if (updateIncomeCount > 0) {

                    logger.info("用户标识为" + incomeRecord.getUid() + ",收益记录标识为" + incomeRecord.getId() + "收益返还成功");

                } else {
                    logger.info("用户标识为" + incomeRecord.getUid() + ",收益记录标识为" + incomeRecord.getId() + "收益返还失败");

                }

            } else {
                logger.info("用户标识为" + incomeRecord.getUid() + ",收益记录标识为" + incomeRecord.getId() + "收益返还失败");
            }

        }


    }
}

