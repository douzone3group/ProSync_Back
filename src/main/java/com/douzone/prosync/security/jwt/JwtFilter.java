package com.douzone.prosync.security.jwt;

import com.douzone.prosync.constant.ConstantPool;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.exception.ExpiredTokenException;
import com.douzone.prosync.security.exception.NoJwtTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.douzone.prosync.constant.ConstantPool.*;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    private final RedisService redisService;



    // doFilter는 토큰의 인증정보를 SecurityContext에 저장하는 역할을 수행한다.
    // Todo : Access 토큰 존재여부 및 만료기간 검증 이후 Refresh 토큰 관련 로직 작성(Refresh 존재여부 확인)이 필요
    // 만약 access 토큰은 없고 refresh 토큰이 존재할 경우를 위해 request 요청의 헤더에 "unique-identifier"를 추가한다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // resolveToken을 통해 토큰을 받아와서 유효성 검증을 하고 정상 토큰이면 SecurityContext에 저장한다.
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                throw new NoJwtTokenException("JWT 토큰이 없습니다. Refresh 토큰을 확인하겠습니다.");
            }
        } catch (NoJwtTokenException | ExpiredTokenException e) {

            // redis에 refresh 토큰이 존재할 경우 새 토큰 발급한다.
            // Todo : 프론트 서버에서 요청에 대한 응답에 header가 있는지 확인해야한다.
            if (redisService.getRefreshToken(httpServletRequest.getHeader(HEADER_DEVICE_FINGERPRINT)
                    +"_"+httpServletRequest.getHeader(HEADER_USER_UNIQUE_IDENTIFIER))!=null) {

                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String renewToken = tokenProvider.createToken(authentication);
                httpServletResponse.setHeader(AUTHORIZATION_HEADER, "Bearer " + renewToken);

            }
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }


    // Request Header에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
