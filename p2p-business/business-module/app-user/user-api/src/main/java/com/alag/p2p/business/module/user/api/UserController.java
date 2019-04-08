package com.alag.p2p.business.module.user.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RequestMapping("p2p/user")
public interface UserController {

    @RequestMapping("queryAllUserCount")
    ServerResponse<Long> queryAllUserCount();

    @PostMapping("checkPhone")
    ServerResponse checkPhone(String phone);

    @PostMapping("checkCaptcha")
    ServerResponse checkCaptcha(HttpServletRequest request,@RequestParam(value = "captcha") String captcha);

    @PostMapping("register")
    ServerResponse register(@RequestParam("phone") String phone,
                            @RequestParam("passwd") String passwd,
                            @RequestParam("rePasswd") String rePasswd,
                            HttpServletRequest request);

    @PostMapping(value = "verifyRealName")
    ServerResponse verifyRealName(HttpServletRequest request,
                                         @RequestParam(value = "realName", required = true) String realName,
                                         @RequestParam(value = "idCard", required = true) String idCard,
                                         @RequestParam(value = "reIdCard", required = true) String reIdCard);


    @GetMapping("getSessionUser")
    ServerResponse getSessionUser(HttpServletRequest request);

    @GetMapping("MyFinanceAccount")
    ServerResponse<FinanceAccount> myFinanceAccount(HttpServletRequest request);

    @PostMapping("login")
    ServerResponse login(HttpServletRequest request,
                         @RequestParam(value = "phone", required = true) String phone,
                         @RequestParam(value = "loginPassword", required = true) String loginPassword);

}
