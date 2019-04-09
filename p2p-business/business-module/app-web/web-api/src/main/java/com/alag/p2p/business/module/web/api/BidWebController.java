package com.alag.p2p.business.module.web.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("p2p/webBid")
public interface BidWebController {

    @PostMapping("invest")
    @ResponseBody
    ServerResponse invest(HttpServletRequest request,
                          @RequestParam(value = "loanId", required = true) Integer loanId,
                          @RequestParam(value = "bidMoney", required = true) Double bidMoney);
}
