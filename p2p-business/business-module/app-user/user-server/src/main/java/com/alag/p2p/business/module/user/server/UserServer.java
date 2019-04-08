package com.alag.p2p.business.module.user.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages={
        "com.alag.p2p.business.core.common",
        "com.alag.p2p.business.module.user.server"})
@EnableEurekaClient
@EnableTransactionManagement
public class UserServer {
    public static void main(String[] args) {
        SpringApplication.run(UserServer.class, args);
    }
}
