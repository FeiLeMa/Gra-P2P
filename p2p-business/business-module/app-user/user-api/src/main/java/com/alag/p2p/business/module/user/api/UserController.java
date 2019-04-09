package com.alag.p2p.business.module.user.api;

import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.api.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("p2p/user")
public interface UserController {
    /**
     * 统计所有注册用户总数
     * @return
     */
    @RequestMapping("queryAllUserCount")
    ServerResponse<Long> queryAllUserCount();

    /**
     * 检查手机号码是否已经注册
     * @param phone
     * @return
     */
    @PostMapping("checkPhone")
    ServerResponse checkPhone(String phone);

    /**
     *
     * @param request
     * @param captcha
     * @return
     */
    @PostMapping("checkCaptcha")
    ServerResponse checkCaptcha(HttpServletRequest request,@RequestParam(value = "captcha") String captcha);

    /**
     *
     * @param phone
     * @param passwd
     * @param rePasswd
     * @param request
     * @return
     */
    @PostMapping("register")
    ServerResponse register(@RequestParam("phone") String phone,
                            @RequestParam("passwd") String passwd,
                            @RequestParam("rePasswd") String rePasswd,
                            HttpServletRequest request);

    /**
     * 实名认证
     * @param request
     * @param realName
     * @param idCard
     * @param reIdCard
     * @return
     */
    @PostMapping(value = "verifyRealName")
    ServerResponse verifyRealName(HttpServletRequest request,
                                         @RequestParam(value = "realName", required = true) String realName,
                                         @RequestParam(value = "idCard", required = true) String idCard,
                                         @RequestParam(value = "reIdCard", required = true) String reIdCard);

    /**
     * 查询当前session用户
     * @param request
     * @return
     */
    @GetMapping("getSessionUser")
    ServerResponse getSessionUser(HttpServletRequest request);

    /**
     * 查询当前session用户的资金账户
     * @param request
     * @return
     */
    @GetMapping("queryFinanceAccountById")
    ServerResponse<FinanceAccount> queryFinanceAccount(HttpServletRequest request);

    /**
     * 用户登录
     * @param request
     * @param phone
     * @param loginPassword
     * @return
     */
    @PostMapping("login")
    ServerResponse login(HttpServletRequest request,
                         @RequestParam(value = "phone", required = true) String phone,
                         @RequestParam(value = "loginPassword", required = true) String loginPassword);
    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    @PostMapping("getUserByPhone")
    ServerResponse<User> getUserByPhone(String phone);

    /**
     * 根据用户标识更新用户信息
     * @param user
     * @return
     */
    @PutMapping("modifyUserById")
    ServerResponse modifyUserById(User user);

    @PutMapping("updateFinanceAccountByBid")
    ServerResponse updateFinanceAccountByBid(@RequestBody Map<String, Object> paramMap);

}
