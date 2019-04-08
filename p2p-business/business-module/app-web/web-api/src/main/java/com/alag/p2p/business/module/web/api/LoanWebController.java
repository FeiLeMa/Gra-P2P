package com.alag.p2p.business.module.web.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("p2p/webLoan")
public interface LoanWebController {

    @GetMapping("loadStat")
    ServerResponse<Map<String, Object>> loadStat(HttpServletRequest request);



}
