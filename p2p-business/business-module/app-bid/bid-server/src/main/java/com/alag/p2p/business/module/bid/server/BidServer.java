package com.alag.p2p.business.module.bid.server;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages={
        "com.alag.p2p.business.core.common",
        "com.alag.p2p.business.module.bid.server"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.alag.p2p.business.module")
@EnableDistributedTransaction
@EnableCircuitBreaker
public class BidServer {
    public static void main(String[] args) {
        SpringApplication.run(BidServer.class, args);
    }
}
