package com.luckkids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LuckKidsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckKidsServerApplication.class, args);
    }

}
