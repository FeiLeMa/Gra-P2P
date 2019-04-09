package com.alag.p2p.business.module.loan.server.service.impl;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.redis.RedisService;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.core.common.tools.ObjToOne;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.server.mapper.LoanInfoMapper;
import com.alag.p2p.business.module.loan.server.service.LoanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public Double getHisAvgRate() {

        //获取操作key=value的数据类型的redis的操作对象,并获取指定key的value值
        Object objV = redisService.get(Constants.HISTORY_AVERAGE_RATE);
        Double historyAverageRate = null;
        //判断是否有值
        if (null == objV) {
            //没有值：去数据库查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();

            //将该值存放到redis缓存中
            redisService.set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 10L);

        }

        return objV == null ? historyAverageRate : ObjToOne.objToDouble(objV);
    }

    @Override
    public List<LoanInfo> getListByPType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public List<LoanInfo> getAllByType(Integer pType) {
        return loanInfoMapper.selectAllByType(pType);
    }

    @Override
    public ServerResponse<LoanInfo> getLoanInfoById(Integer loanId) {
        return ServerResponse.createBySuccess(loanInfoMapper.selectByPrimaryKey(loanId));
    }

    @Override
    public ServerResponse updateLPMoneyById(Map paramMap) {
        int updateLeftProductMoney = loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
        if (updateLeftProductMoney == 0) {
            return ServerResponse.createByErrorMessage("投资失败，修改剩余产品剩余可投金额失败");
        }
        return ServerResponse.createBySuccessMessage("修改剩余产品可投金额成功");
    }

    @Override
    public ServerResponse updateLPById(LoanInfo loanInfo) {
        int mRet = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
        if (mRet > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
