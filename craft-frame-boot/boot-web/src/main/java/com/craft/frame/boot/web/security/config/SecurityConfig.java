package com.craft.frame.boot.web.security.config;

import com.craft.frame.boot.web.security.JwtUtils;
import com.craft.frame.boot.web.security.SecurityTool;
import com.craft.frame.boot.web.security.config.properties.JwtProperties;
import com.craft.frame.boot.web.security.config.properties.SecurityProperties;
import com.craft.frame.boot.web.security.filter.JwtAuthorizationFilter;
import com.craft.frame.boot.web.security.handler.DefaultJwtAccessDeniedHandler;
import com.craft.frame.boot.web.security.handler.DefaultJwtAuthenticationEntryPoint;
import com.craft.frame.boot.web.security.handler.DefaultLogoutSuccessHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author DURR
 * @desc 类描述
 * @date 2023/6/24 14:38
 */

@Configurable
@EnableConfigurationProperties({SecurityProperties.class, JwtProperties.class})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String ENABLE_SECURITY = "security.config.enable";

    private final SecurityTool securityTool;

    private final JwtUtils jwtUtils;

    private final JwtProperties jwtProperties;

    private final SecurityProperties securityProperties;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final AnonymousConfig properties;

    public SecurityConfig(SecurityTool securityTool, JwtUtils jwtUtils, JwtProperties jwtProperties,
                          SecurityProperties securityProperties, AuthenticationConfiguration authenticationConfiguration, AnonymousConfig properties) {
        this.securityTool = securityTool;
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
        this.securityProperties = securityProperties;
        this.authenticationConfiguration = authenticationConfiguration;
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DefaultLogoutSuccessHandler();
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new DefaultJwtAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new DefaultJwtAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), securityTool, jwtUtils, jwtProperties);
    }

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        if (!securityProperties.isEnable()) {
            return httpSecurity.authorizeRequests().anyRequest().permitAll().and().csrf().disable().build();
        }
        LOGGER.info("已开启Spring Security");
        httpSecurity.authorizeRequests()
                // 其他的接口都需要认证后才能请求
                .anyRequest().authenticated()
                // 禁用 CSRF
                .and().csrf().disable()
                // 不需要session（不创建会话）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //添加自定义Filter
                .and().addFilter(jwtAuthorizationFilter(authenticationConfiguration))
                // 授权异常处理
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
        if (StringUtils.isNotBlank(securityProperties.getLogoutPath())) {
            httpSecurity.logout().logoutUrl(securityProperties.getLogoutPath())
                    .logoutSuccessHandler(logoutSuccessHandler());
        }
        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnProperty(name = ENABLE_SECURITY)
    public WebSecurityCustomizer webSecurityCustomizer() {
        List<String> anonymousPaths = properties.getUrls();
        anonymousPaths.addAll(Arrays.asList(securityProperties.getIgnoreInterface()));
        if (CollectionUtils.isEmpty(anonymousPaths)) {
            return web -> web.ignoring().anyRequest();
        }
        String[] paths = anonymousPaths.toArray(new String[0]);
        LOGGER.info("Security匿名访问路径:{}", Arrays.toString(paths));
        return web -> web.ignoring().antMatchers(paths);
    }

}
