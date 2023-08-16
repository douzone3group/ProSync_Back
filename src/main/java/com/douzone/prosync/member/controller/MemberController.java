package com.douzone.prosync.member.controller;

import com.douzone.prosync.mail.service.MailService;
import com.douzone.prosync.mail.dto.CertificationCodeDto;
import com.douzone.prosync.mail.dto.MailDto;
import com.douzone.prosync.mail.exception.CertificationFailException;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.auth.MemberDetails;
import com.douzone.prosync.security.exception.DuplicateMemberException;
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

    private final MailService mailService;

    /**
     * 사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장하는 로직
     */
    @PostMapping("/send_verification")
    public ResponseEntity mailInvalidateAndSend(@Valid @RequestBody MailDto mail) {
        // email이 DB에 등록되어 있는지 확인한다.
        if (memberService.duplicateInspection(mail.getEmail())) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        String number = mailService.sendMail(mail.getEmail());

        // Redis에 key값은 "email: 사용자 email" 형태로 인증번호 저장
        redisService.setEmailCertificationNumber(mail.getEmail(),number);

        return new ResponseEntity(number, HttpStatus.OK);
    }

    /**
     * 사용자에게 이메일과 인증번호를 받아 Redis에 있는지 확인하는 로직
     */
    @PostMapping("/verify_code")
    public ResponseEntity verifyCertificationNumber(@Valid @RequestBody CertificationCodeDto code) {
        String number = redisService.getEmailCertificationNumber(code.getEmail());

        if (number==null || !number.equals(code.getCertificationNumber())) {
            throw new CertificationFailException("인증번호 불일치");
        }
        System.out.println("인증번호 통과, 인증번호를 지웁니다.");
        redisService.removeEmailCertificationNumber(code.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 회원가입 성공 로직
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

        // "device:장치지문_pk"을 key값으로 refreshToken을 Redis에 저장한다.
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
