package com.gobanggame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@EnableScheduling
public class GobanggameApplication {

    public static void main(String[] args) {
        SpringApplication.run(GobanggameApplication.class, args);
    }

}
