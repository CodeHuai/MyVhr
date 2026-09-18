package com.myhr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 基础配置（对应 vhr 的 SecurityConfig）
 *
 * TODO 仿写时按教程演进：
 *  1. 登录/注销成功后的 JSON 响应（AuthenticationSuccessHandler 等）
 *  2. JWT + Redis 方案：JwtAuthenticationTokenFilter、无状态会话
 *  3. 根据数据库角色动态配置访问权限
 *
 * 说明：WebSecurityConfigurerAdapter 在 Boot 2.7 中已标记过时但仍可用，
 * 与 vhr 教程写法保持一致；如需新写法可改用 SecurityFilterChain Bean。
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/ping").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }
}
