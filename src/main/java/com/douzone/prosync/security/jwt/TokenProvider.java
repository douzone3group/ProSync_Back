package com.douzone.prosync.security.jwt;

import com.douzone.prosync.security.auth.MemberDetails;
import com.douzone.prosync.security.exception.ExpiredTokenException;
import com.douzone.prosync.security.exception.FailToRenewTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // JWT 토큰에 Subject로 멤버의 id(pk)를 넣어준다.
        return Jwts.builder()
                .setSubject(((MemberDetails) authentication.getPrincipal()).getMemberId().toString())
                .claim("email", ((MemberDetails) authentication.getPrincipal()).getUsername())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 서버에서 사용하는 Principal 객체에는 member의 pk 값만 저장해서 사용한다.
        User principal = new User(claims.getSubject(), "", new ArrayList<>());

        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    public String expiredTokenToRenewToken(String expiredToken) {
        Claims claims = null;
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(expiredToken).getBody();

            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(validity)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();

            return Jwts.builder()
                    .setClaims(claims) //
                    .setExpiration(validity)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

        } catch (Exception e) {
            throw new FailToRenewTokenException("Failed to renew the token", e);
        }

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
            throw new ExpiredTokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}