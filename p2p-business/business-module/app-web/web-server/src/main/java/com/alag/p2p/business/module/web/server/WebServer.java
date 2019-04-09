package com.alag.p2p.business.module.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.alag.p2p.business.module")
public class WebServer {
    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }
}

