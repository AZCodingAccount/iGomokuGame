package com.gobanggame.pojo.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: gobanggame
 * @author: AlbertZhang
 * @create: 2023-12-09 22:39
 * @description: jwt属性类
 **/
@Component
@ConfigurationProperties(prefix = "gobanggame.jwt")
@Data
public class JwtProperties {

    /**
     * 用户生成jwt令牌相关配置
     */
    private String SecretKey;
    private long Ttl;
}
