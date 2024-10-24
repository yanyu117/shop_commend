package com.briup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
/*
http://localhost:8080
 */
@SpringBootApplication
@MapperScan("com.briup.dao")
public class BriupShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BriupShopApplication.class, args);
    }

}
