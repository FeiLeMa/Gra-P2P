package com.alag.p2p.business.module.web.server.controller;


import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.module.bid.feign.controller.BidFeignService;
import com.alag.p2p.business.module.loan.api.model.LoanInfo;
import com.alag.p2p.business.module.loan.feign.controller.LoanFeignService;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.alag.p2p.business.module.web.api.IndexWebController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexWebServerController implements IndexWebController {

    private Logger logger = LogManager.getLogger(IndexWebServerController.class);
    @Autowired
    private LoanFeignService loanFeignService;
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private BidFeignService bidFeignService;

    @GetMapping("index")
    @Override
    public String toIndex(Model model) {

        logger.info("进入index");

        //获取历史年化收益率
        Double historyAvgRate = loanFeignService.queryHistoryAverageRate().getData();

        //获取平台注册总人数
        Long allUserCount = userFeignService.queryAllUserCount().getData();

        //获取平台累计投资金额
        Double allBidMoney = bidFeignService.queryAllBidMoney().getData();

        //将以下查询看成是一个分页，实际功能：根据产品类型查询产品信息显示前几个
        //数据持久层用户的是limit函数 limit 起始下标,截取长度
        //loanFeignService.queryLoanInfoListByProductType(产品类型，页码，每页显示条数);

        Map<String, Object> paramMap = new HashMap<String, Object>();

        //页码：起始下标
        paramMap.put("currentPage", 0);


        //获取新手宝产品:产品类型：0，显示第1页，每页显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);
        List<LoanInfo> xLoanInfoList = loanFeignService.queryLoanInfoListByProductType(paramMap).getData();


        //获取优选产品：产品类型：1，显示第1页，每页显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList = loanFeignService.queryLoanInfoListByProductType(paramMap).getData();


        //获取散标产品：产品类型：2，显示第2页，每页显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList = loanFeignService.queryLoanInfoListByProductType(paramMap).getData();

        model.addAttribute("xLoanInfoList", xLoanInfoList);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        model.addAttribute("sLoanInfoList", sLoanInfoList);
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAvgRate);
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);


        return "index";
    }

}
