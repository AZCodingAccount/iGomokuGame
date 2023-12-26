package com.gobanggame.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobanggame.interceptor.JwtTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/**") // 对所有资产都需要验证，下面是排除的url
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/admin/login")
                .excludePathPatterns("/api/add/website/click")
                .excludePathPatterns("/v3/api-docs/swagger-config")
                .excludePathPatterns("/v3/api-docs");
    }

    /*
     * 这个是通用跨域配置
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .maxAge(3600);
    }

    /*
     * 通用跨域配置，版本不用
     * */
//     @Bean
//     public CorsFilter corsFilter() {
//         CorsConfiguration config = new CorsConfiguration();
// // 允许白名单域名进行跨域调用
//         config.addAllowedOriginPattern("*");
// // 允许跨越发送cookie
//         config.setAllowCredentials(true);
// // 放行全部原始头信息
//         config.addAllowedHeader("*");
// // 允许所有请求方法跨域调用
//         config.addAllowedMethod("*");
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);
//         return new CorsFilter(source);
//     }

    // /**
    //  * 扩展Spring MVC框架的消息转化器
    //  *
    //  * @param converters
    //  */
    // public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    //     log.info("扩展消息转换器...");
    //     MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    //     converter.setObjectMapper(objectMapper);
    //     converters.add(0, converter);
    // }

    /**
     * 设置静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置拦截器访问静态资源
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
