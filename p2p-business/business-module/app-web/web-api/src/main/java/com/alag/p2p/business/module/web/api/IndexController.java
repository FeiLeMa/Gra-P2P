package com.alag.p2p.business.module.web.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("p2p")
public interface IndexController {

    @GetMapping("index")
    String toIndex(Model model);

}
