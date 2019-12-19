package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.mapper")
public class LeyouUserServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUserServerApplication.class, args);
    }
}
