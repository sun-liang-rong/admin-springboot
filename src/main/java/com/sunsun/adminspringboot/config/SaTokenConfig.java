package com.sunsun.adminspringboot.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    // 注册Sa-Token拦截器，开启鉴权
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
                    // 全局鉴权：所有接口需要登录
                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/**")
                // 放行接口：登录、注册、swagger、静态资源
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/register",
                        "/doc.html",
                        "/webjars/**",
                        "/favicon.ico"
                );
    }
}