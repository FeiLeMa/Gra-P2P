package com.alag.p2p.business.module.web.server.controller;

import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.bid.api.BidInfoController;
import com.alag.p2p.business.module.bid.feign.controller.BidFeignService;
import com.alag.p2p.business.module.user.api.model.User;
import com.alag.p2p.business.module.web.api.BidWebController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("p2p/webBid")
public class BidWebServerController implements BidWebController {
    @Autowired
    private BidFeignService bidFeignService;
    public static final Logger logger = LogManager.getLogger(BidWebServerController.class);

    @PostMapping("invest")
    @Override
    public ServerResponse invest(HttpServletRequest request,
                                 @RequestParam("loanId") Integer loanId,
                                 @RequestParam("bidMoney") Double bidMoney) {

        logger.info("loanId => " + loanId);
        logger.info("BidMoney => " + bidMoney);
        User sesionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        logger.info(sesionUser);
        if (sesionUser == null) {
            return ServerResponse.createByErrorMessage("请先登录，再投资");
        } else {
            //准备请求参数
            Map<String, Object> paramMap = new HashMap<String, Object>();
            //用户标识
            paramMap.put("uid", sesionUser.getId());
            //产品标识
            paramMap.put("loanId", loanId);
            //投资金额
            paramMap.put("bidMoney", bidMoney);
            //手机号码
            paramMap.put("phone", sesionUser.getPhone());

            return bidFeignService.invest(paramMap);
        }

    }
}