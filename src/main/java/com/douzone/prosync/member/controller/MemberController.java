package com.douzone.prosync.member.controller;

import com.douzone.prosync.constant.ConstantPool;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.auth.MemberDetails;
import com.douzone.prosync.security.jwt.JwtFilter;
import com.douzone.prosync.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.*;
import static com.douzone.prosync.member.dto.MemberRequest.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisService redisService;

    /**
     * 회원가입
     */
    @PostMapping("/members")
    public ResponseEntity signUp(@Valid @RequestBody PostDto postDto) {

        Member member = memberService.signup(postDto);

        return new ResponseEntity(member, HttpStatus.OK);
    }


//    /**
//     * 회원정보 수정
//     */
//    @PatchMapping("/{memberId}")
//    public ResponseEntity modifyMember() {
//
//    }

//
//    /**
//     * 회원정보 조회
//     */
//    @PatchMapping("/{memberId}")
//    public ResponseEntity getMemberInfo() {
//
//    }

    // Todo : 회원탈퇴 시 회원관련 토큰들 제거해줘야한다.
//

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticationToken을 이용해서 Authentication 객체를 생성하려고 authentication 메소드가 실행이 될 때
        // CustomUserDetailsService의 loadUserByUsername 메소드가 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // createToken 메소드를 통해서 JWT Token을 생성한다.
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);

        // "장치지문_pk"을 key값으로 refreshToken을 Redis에 저장한다.
        redisService.setRefreshToken(request.getHeader(HEADER_DEVICE_FINGERPRINT)
                +"_"+(((MemberDetails) authentication.getPrincipal()).getMemberId()));


        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃 시 토큰 제거
     */
    @GetMapping("/removeToken")
    public ResponseEntity logout(Principal principal, HttpServletRequest request) {

        // Refresh 토큰을 Redis에서 제거하는 작업
        redisService.removeRefreshToken(request.getHeader(HEADER_DEVICE_FINGERPRINT)+"_"+principal.getName());

        // Todo : return 값 어떻게 줄지 생각해야한다.
        return new ResponseEntity(principal.getName()+" 삭제완료", HttpStatus.OK);
    }


}
