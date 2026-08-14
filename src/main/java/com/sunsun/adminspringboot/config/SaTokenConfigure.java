package com.sunsun.adminspringboot.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * [Sa-Token 权限认证] 配置类
 * <p>使用全局过滤器统一鉴权（过滤器优先级最高，先于 Spring MVC 执行）</p>
 */
@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     * 注册 [Sa-Token 注解拦截器]
     * <p>处理 @SaCheckLogin / @SaCheckRole / @SaCheckPermission 等注解鉴权
     * （登录态已由 SaServletFilter 全局过滤器保证，此处仅开启注解校验）</p>
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login", "/auth/register",
                        "/doc.html", "/swagger-ui.html", "/swagger-ui/**",
                        "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**",
                        "/favicon.ico"
                );
    }

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 拦截所有路由，仅放行公开路由（登录、注册、接口文档、静态资源）
                .addInclude("/**")
                .addExclude(
                        "/auth/login", "/auth/register",
                        "/doc.html", "/swagger-ui.html", "/swagger-ui/**",
                        "/webjars/**", "/swagger-resources/**", "/v3/api-docs/**",
                        "/favicon.ico"
                )

                // 认证函数：每次请求执行（过滤器已按 include/exclude 路由，这里直接校验登录态）
                .setAuth(obj -> StpUtil.checkLogin())

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    log.debug("Sa-Token 鉴权异常: {}", e.getMessage());

                    // SaServletFilter 默认按 text/plain 写入响应，这里改为 JSON
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");

                    // 未登录 / token 缺失 / token 无效 / token 过期 → 业务码 401（HTTP 状态保持 200）
                    if (e instanceof NotLoginException nle) {
                        String msg = switch (nle.getType()) {
                            case NotLoginException.TOKEN_TIMEOUT -> "token已过期，请重新登录";
                            case NotLoginException.INVALID_TOKEN -> "无效token";
                            case NotLoginException.NOT_TOKEN -> "未携带Authorization凭证";
                            default -> "登录认证失败";
                        };
                        return SaResult.get(401, msg, null);
                    }
                    // 缺少权限 → 业务码 403
                    if (e instanceof NotPermissionException npe) {
                        return SaResult.get(403, "没有操作权限：" + npe.getPermission(), null);
                    }
                    // 缺少角色 → 业务码 403
                    if (e instanceof NotRoleException nre) {
                        return SaResult.get(403, "用户角色不足：" + nre.getRole(), null);
                    }
                    // 其他异常 → 500
                    return SaResult.error(e.getMessage());
                })

                // 前置函数：在每次认证函数之前执行（所有请求都会进入，用于设置安全响应头）
                .setBeforeAuth(r -> SaHolder.getResponse()
                        // 服务器名称
                        .setServer("sa-server")
                        // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                        .setHeader("X-Frame-Options", "SAMEORIGIN")
                        // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                        .setHeader("X-XSS-Protection", "1; mode=block")
                        // 禁用浏览器内容嗅探
                        .setHeader("X-Content-Type-Options", "nosniff"))
                ;
    }

}
