package com.gobanggame.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/*
* 阿里云oss存储注入的属性类
* */
@Component
@ConfigurationProperties(prefix = "gobanggame.alioss")
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
