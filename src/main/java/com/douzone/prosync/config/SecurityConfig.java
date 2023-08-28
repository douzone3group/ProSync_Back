package com.douzone.prosync.config;

import com.douzone.prosync.authorization.filter.CustomAuthorizationFilter;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final HmacAndBase64 hmacAndBase64;
    private final RedisService redisService;
    private final CorsFilter corsFilter;

    private final CustomAuthorizationFilter customAuthorizationFilter;
    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler, RedisService redisService, CorsFilter corsFilter,
                          HmacAndBase64 hmacAndBase64, RefreshTokenProvider refreshTokenProvider, CustomAuthorizationFilter customAuthorizationFilter) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.redisService = redisService;
        this.corsFilter = corsFilter;
        this.hmacAndBase64 = hmacAndBase64;
        this.refreshTokenProvider = refreshTokenProvider;
        this.customAuthorizationFilter = customAuthorizationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.cors(Customizer.withDefaults());
//        http.headers().frameOptions().disable();

        // 로그인 API, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 모두 permitAll 설정을 한다.
        http
                .authorizeRequests()
                .mvcMatchers("/api/v1/members","/api/v1/login", "/api/v1/send_verification","/api/v1/verify_code").permitAll()
                .mvcMatchers("/v2/api-docs","/favicon.ico","/swagger-ui/index.html").permitAll()
                .mvcMatchers("/**/*.css", "/**/*.js", "/**/*.png","/swagger-ui/**","/swagger-resources/**").permitAll()
                .mvcMatchers("/api/v1/test", "/api/v1/subscribe/**", "/api/v1/test2").permitAll() // TODO : 테스트용
                .anyRequest().authenticated();

        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                );




        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessUrl("/api/v1/removeToken") // 여기에 원하는 URL을 지정
                )


                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스도 적용해준다.
                .apply(new JwtSecurityConfig(tokenProvider,refreshTokenProvider,redisService,hmacAndBase64));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> {
            web.ignoring().requestMatchers(request -> request.getRequestURI().startsWith("/h2-console"));
            web.ignoring().requestMatchers(request -> request.getRequestURI().startsWith("/favicon.ico"));
        };
    }


}