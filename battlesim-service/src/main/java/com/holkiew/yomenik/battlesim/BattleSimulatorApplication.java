package com.holkiew.yomenik.battlesim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BattleSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleSimulatorApplication.class, args);
    }

}