package com.alag.p2p.business.module.web.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("p2p/webUser")
public interface UserWebController {

    @RequestMapping("logout")
    String logout(HttpServletRequest request);

    @RequestMapping("myCenter")
    String myCenter(HttpServletRequest request,Model model);
}
