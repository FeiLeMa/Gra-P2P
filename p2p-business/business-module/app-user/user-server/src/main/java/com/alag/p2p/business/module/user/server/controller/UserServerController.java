package com.alag.p2p.business.module.user.server.controller;


import com.alag.p2p.business.core.common.constant.Constants;
import com.alag.p2p.business.core.common.response.ServerResponse;
import com.alag.p2p.business.core.common.tools.HttpUtils;
import com.alag.p2p.business.module.user.api.UserController;
import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.api.model.User;
import com.alag.p2p.business.module.user.server.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("p2p/user")
public class UserServerController implements UserController {
    Logger logger = LogManager.getLogger(UserServerController.class);

    @Autowired
    public UserService userService;

    @RequestMapping("queryAllUserCount")
    @Override
    public ServerResponse<Long> queryAllUserCount() {

        Long userCount = userService.getAUserCount();

        return ServerResponse.createBySuccess(userCount);
    }

    @PostMapping(value = "checkPhone")
    @Override
    public ServerResponse checkPhone(String phone) {

        logger.info("Receiving phone is " + phone);

        if (!StringUtils.isEmpty(phone) && Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            User ret = userService.getUserByPhone(phone);
            return ret != null ? ServerResponse.createByErrorMessage("手机号码已被注册") : ServerResponse.createBySuccessMessage("OK");
        }

        return ServerResponse.createByErrorMessage("手机号码格式不对");
    }

    @PostMapping("checkCaptcha")
    @Override
    public ServerResponse checkCaptcha(HttpServletRequest request, @RequestParam("captcha") String captcha) {

        logger.info("recevicing captche is " + captcha);

        if (!StringUtils.isEmpty(captcha)) {
            String sessionCaptche = (String) request.getSession().getAttribute(Constants.CAPTCHA);
            if (StringUtils.equalsIgnoreCase(sessionCaptche, captcha)) {
                return ServerResponse.createBySuccessMessage("OK");
            }
            return ServerResponse.createByErrorMessage("验证码不匹配");
        }

        return ServerResponse.createByErrorMessage("验证码格式问题或为空");
    }

    @PostMapping("register")
    @Override
    public ServerResponse register(@RequestParam("phone") String phone,
                                   @RequestParam("passwd") String passwd,
                                   @RequestParam("rePasswd") String rePasswd,
                                   HttpServletRequest request) {

        if (!StringUtils.isAllEmpty(phone, passwd, rePasswd)) {

            if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
                return ServerResponse.createByErrorMessage("手机号码格式不对或已被注册");
            }


            if (!StringUtils.equals(passwd, rePasswd)) {
                return ServerResponse.createByErrorMessage("两次密码不一致");
            }


            //用户的注册(手机号，登录密码)【1.新增用户信息 2.开立帐户】 -> 返回Boolean|int|结果对象ResultObject
            ServerResponse ret = userService.register(phone, passwd);

            //判断是否注册成功
            if (ret.getStatus() != 0) {
                return ret;
            }

            //将用户的信息存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER, ret.getData());
            return ret;
        }


        return ServerResponse.createByErrorMessage("手机号码、密码、重复密码不可为空");
    }

    @PostMapping("verifyRealName")
    @Override
    public ServerResponse verifyRealName(HttpServletRequest request,
                                         @RequestParam(value = "realName", required = true) String realName,
                                         @RequestParam(value = "idCard", required = true) String idCard,
                                         @RequestParam(value = "reIdCard", required = true) String reIdCard) {
        //验证参数
        if (!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$", realName)) {
            return ServerResponse.createByErrorMessage("姓名格式不对");
        }

        if (!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", idCard)) {
            return ServerResponse.createByErrorMessage("请输入正确的身份证号码");
        }

        if (!StringUtils.equals(idCard, reIdCard)) {
            return ServerResponse.createByErrorMessage("两次输入身份证号码不一致");
        }

        //开始验证
        String host = "https://idcert.market.alicloudapi.com";
        String path = "/idcard";
        String method = "GET";
        String appcode = "8d92aab5fb4545a0be3739aab0637f8b";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("idCard", idCard);
        querys.put("name", realName);


        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String s = EntityUtils.toString(response.getEntity());
            logger.info(s);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String status = jsonObject.getString("status");

            if (StringUtils.equals(status, "01")) {
                User userSession = (User) request.getSession().getAttribute(Constants.SESSION_USER);

                userSession.setId(userSession.getId());
                userSession.setName(realName);
                userSession.setIdCard(idCard);
                int retV = userService.updateInfo(userSession);
                if (retV == 0){
                    return ServerResponse.createByErrorMessage("系统繁忙");
                }

            } else {
                return ServerResponse.createByErrorMessage("身份信息有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createBySuccess("身份认证通过");

    }

    @GetMapping(value = "getSessionUser")
    @Override
    public ServerResponse getSessionUser(HttpServletRequest request) {

        Object sessionUser = request.getSession().getAttribute(Constants.SESSION_USER);
        User userInfo = null;
        if (sessionUser instanceof User) {
            userInfo = (User) sessionUser;
        }

        return ServerResponse.createBySuccess(userInfo);
    }

    @GetMapping("queryFinanceAccountById")
    @Override
    public ServerResponse<FinanceAccount> queryFinanceAccount(HttpServletRequest request) {

        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (null == sessionUser) {
            return ServerResponse.createByErrorMessage("当前session没有用户");
        }
        logger.info("sessionUserId  ===>   " + sessionUser.getId());
        FinanceAccount financeAccount = userService.getFinanceAccountById(sessionUser.getId());
        System.out.println(financeAccount);
        return ServerResponse.createBySuccess(financeAccount);

    }

    @PostMapping("login")
    @Override
    public ServerResponse login(HttpServletRequest request,
                                @RequestParam(value = "phone", required = true) String phone,
                                @RequestParam(value = "loginPassword", required = true) String loginPassword) {

        //验证手机号码格式
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            return ServerResponse.createByErrorMessage("用户手机号码格式不对");
        }


        //用户登录【1.根据手机号和密码查询用户 2.更新最近登录时间】（手机号，密码） -> 返回User|Map
        User user = userService.login(phone, loginPassword);

        //判断用户是否存在
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户手机或密码错误");
        }

        //将用户的信息存放到session中
        request.getSession().setAttribute(Constants.SESSION_USER, user);

        return ServerResponse.createBySuccess("OK");
    }

    @PostMapping("getUserByPhone")
    @Override
    public ServerResponse<User> getUserByPhone(String phone) {
        //验证手机号码格式
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            return ServerResponse.createByErrorMessage("用户手机号码格式不对");
        }

        User user = userService.getUserByPhone(phone);
        return ServerResponse.createBySuccess(user);
    }

    @PutMapping("modifyUserById")
    @Override
    public ServerResponse modifyUserById(@RequestBody User user) {
        int ret = userService.updateInfo(user);
        if (ret > 0){
            return ServerResponse.createBySuccess("用户更新成功");
        }

        return ServerResponse.createByErrorMessage("用户更新失败");
    }

    @PutMapping("updateFinanceAccountByBid")
    @Override
    public ServerResponse updateFinanceAccountByBid(@RequestBody Map<String, Object> paramMap) {


        return userService.updateFAById(paramMap);
    }

    @PostMapping("updateFinanceAccountByIncomeBack")
    @Override
    public ServerResponse updateFinanceAccountByIncomeBack(@RequestBody Map<String, Object> paramMap) {
        return userService.updateFAByIncomeBack(paramMap);
    }

}
