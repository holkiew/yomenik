package com.holkiew.yomenik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class YomenikApplication {

    public static void main(String[] args) {
        SpringApplication.run(YomenikApplication.class, args);
    }

}
