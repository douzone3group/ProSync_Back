package com.douzone.prosync.config;

import com.douzone.prosync.security.jwt.JwtAccessDeniedHandler;
import com.douzone.prosync.security.jwt.JwtAuthenticationEntryPoint;
import com.douzone.prosync.security.jwt.JwtSecurityConfig;
import com.douzone.prosync.security.jwt.TokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CorsFilter corsFilter;

    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, CorsFilter corsFilter) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.corsFilter = corsFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable(); // 토큰 방식을 사용하기 위해 csrf 설정은 disable
//        http.cors(Customizer.withDefaults());
//        http.headers().frameOptions().disable();

        // 로그인 API, 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 모두 permitAll 설정을 한다.
        http
                .authorizeRequests()
                .mvcMatchers("/members/**").permitAll()
                .mvcMatchers("/api/hello", "/api/authenticate", "/api/signup").permitAll()
//                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .anyRequest().authenticated();

        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(csrf -> csrf.disable())

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );




                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                http.sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

//                // enable h2-console
//                .headers(headers ->
//                        headers.frameOptions(options ->
//                                options.sameOrigin()
//                        )
//                )

                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스도 적용해준다.
                .apply(new JwtSecurityConfig(tokenProvider));

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
