package com.neighbor.eventmosaic.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmApiApplication.class, args);
    }

} 