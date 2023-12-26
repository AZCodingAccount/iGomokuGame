package com.gobanggame.interceptor;

import com.gobanggame.pojo.pojo.JwtProperties;
import com.gobanggame.utils.BaseContext;
import com.gobanggame.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 判断如果请求的类是swagger的控制器，直接通行。
        if (handlerMethod.getBean().getClass().getName().equals("springfox.documentation.swagger.web.ApiResourceController")) {
            return true;
        }


        // 1、从请求头中获取令牌
        String token = request.getHeader("jwt");

        // 获取请求的URI和method
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        // System.out.println(requestURI + "::" + requestMethod);

        // 2、校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            // 判断当前是用户还是管理员
            if ("admin".equals(claims.get("userRole"))) {    // 管理员，把id存到ThreadLocal里面
                Object adminId = claims.get("userId");
                BaseContext.setAdminCurrentId(((Integer) adminId).longValue());
                // 3、通过，放行
                return true;

            } else if ("user".equals(claims.get("userRole"))) {
                // 这里有个新增反馈的问题，再判断一下，如果是POST且新增反馈且jwt有效，也让他通过
                if (requestURI.equals("/api/management/feedbacks") && requestMethod.equalsIgnoreCase("POST")) {
                    Object userId = claims.get("userId");// 管理员，把id存到ThreadLocal里面
                    BaseContext.setCurrentId(((Integer) userId).longValue());
                    return true;
                }
                // 拿着user的jwt访问管理员资源，踢回。
                if (requestURI.startsWith("/api/analysis") || requestURI.startsWith("/api/management")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
                Object userId = claims.get("userId");
                BaseContext.setCurrentId(((Integer) userId).longValue());// 管理员，把id存到ThreadLocal里面
                return true;
            }
            return false;   // 其他的用户类型不允许访问资源
        } catch (Exception ex) {
            log.info("校验不通过，信息为：{}", ex.getMessage());
            // 4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }


}
