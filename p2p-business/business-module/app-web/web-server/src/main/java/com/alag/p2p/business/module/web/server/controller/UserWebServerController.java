package com.alag.p2p.business.module.web.server.controller;

import com.alag.p2p.business.module.user.api.model.FinanceAccount;
import com.alag.p2p.business.module.user.feign.controller.UserFeignService;
import com.alag.p2p.business.module.web.api.UserWebController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("p2p/webUser")
public class UserWebServerController implements UserWebController {

    public static final Logger logger = LogManager.getLogger(UserWebServerController.class);
    private UserFeignService userFeignService;

    @RequestMapping("logout")
    @Override
    public String logout(HttpServletRequest request) {

        //让session失效或者清除指定session中key的值
        request.getSession().invalidate();

//        request.getSession().removeAttribute(Constants.SESSION_USER);
        logger.info("logout success!");

        return "redirect:/index";
    }

    @RequestMapping("myCenter")
    @Override
    public String myCenter(HttpServletRequest request,Model model) {
        FinanceAccount financeAccount = userFeignService.queryFinanceAccount(request).getData();
        model.addAttribute("financeAccount", financeAccount);

        return "myCenter";
    }

}
