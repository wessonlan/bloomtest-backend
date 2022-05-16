package com.pingguo.bloomtest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pingguo.bloomtest.mapper")
public class BloomtestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloomtestApplication.class, args);
    }

}
