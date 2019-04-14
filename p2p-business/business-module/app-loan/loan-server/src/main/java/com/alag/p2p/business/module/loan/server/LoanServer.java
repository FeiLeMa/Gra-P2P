package com.alag.p2p.business.module.loan.server;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages={
        "com.alag.p2p.business.core.common",
        "com.alag.p2p.business.module.loan.server"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.alag.p2p.business.module")
@EnableDistributedTransaction
public class LoanServer {
    public static void main(String[] args) {
        SpringApplication.run(LoanServer.class, args);
    }
}
