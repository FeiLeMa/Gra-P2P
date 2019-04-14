package com.alag.p2p.business.module.timer.server;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages={
        "com.alag.p2p.business.core.common",
        "com.alag.p2p.business.module.timer.server"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.alag.p2p.business.module")
@EnableDistributedTransaction
public class TimerServer {
    public static void main(String[] args) {
        SpringApplication.run(TimerServer.class, args);
    }
}
