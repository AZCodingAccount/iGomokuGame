package com.gobanggame.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
* knfie4j接口文档配置
* */
@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("五子棋AI")
                        .version("1.0")
                        .description("五子棋项目接口文档")
                        .termsOfService("https://test.com")
                        .contact(new Contact().name("AlbertZhang").url("https://test.com").email("han892577@qq.com"))
                );
    }



}