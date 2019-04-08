package com.alag.p2p.business.module.web.server.controller;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.feign.controller.BidFeignService;
import com.alag.p2p.business.module.loan.feign.controller.LoanFeignService;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.alag.p2p.business.module.web.api.LoanWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("p2p/webLoan")
public class LoanServerController implements LoanWebController {

    @Autowired
    private LoanFeignService loanFeignService;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private BidFeignService bidFeignService;

    @RequestMapping("loadStat")
    @ResponseBody
    @Override
    public ServerResponse<Map<String,Object>> loadStat(HttpServletRequest request) {
        Map<String,Object> retMap = new HashMap<String,Object>();

        //历史平均年化收益率
        Double historyAverageRate = loanFeignService.queryHistoryAverageRate().getData();

        //平台注册总人数
        Long allUserCount = userFeignService.queryAllUserCount().getData();

        //累计投资金额
        Double allBidMoney = bidFeignService.queryAllBidMoney().getData();

        retMap.put(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);
        retMap.put(Constants.ALL_USER_COUNT,allUserCount);
        retMap.put(Constants.ALL_BID_MONEY,allBidMoney);

        return ServerResponse.createBySuccess(retMap);
    }

}
