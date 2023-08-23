package com.douzone.prosync.member.controller;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.mail.dto.CertificationCodeDto;
import com.douzone.prosync.mail.dto.MailDto;
import com.douzone.prosync.mail.service.MailService;
import com.douzone.prosync.member.dto.request.*;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.redis.RedisService;
import com.douzone.prosync.security.jwt.HmacAndBase64;
import com.douzone.prosync.security.jwt.RefreshTokenProvider;
import com.douzone.prosync.security.jwt.TokenProvider;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisService redisService;

    private final HmacAndBase64 hmacAndBase64;

    private final MailService mailService;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    /**
     * 사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장하는 로직
     */
    @ApiOperation(value = "이메일 인증", notes = "사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장, 성공 시 상태코드 200")
    @PostMapping("/send_verification")
    public ResponseEntity<MailDto> mailInvalidateAndSend(@Valid @RequestBody MailDto mail) {
        // email이 DB에 등록되어 있는지 확인한다.
        if (memberService.duplicateInspection(mail.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID);
        }

        String number = mailService.sendMail(mail.getEmail());

        // Redis에 key값은 "email: 사용자 email" 형태로 인증번호 저장
        redisService.set("email:" + mail.getEmail(), number, EMAIL_CERTIFICATION_NUMBER_DURATION);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 사용자에게 이메일과 인증번호를 받아 Redis에 있는지 확인하는 로직
     */
    @ApiOperation(value = "이메일 인증", notes = "사용자에게 이메일과 인증번호를 받아 Redis에 존재하는가 확인, 성공 시 상태코드 200")
    @PostMapping("/verify_code")
    public ResponseEntity verifyCertificationNumber(@Valid @RequestBody CertificationCodeDto code) {
        String number = redisService.get("email:" + code.getEmail());

        if (number==null || !number.equals(code.getCertificationNumber())) {
            throw new ApplicationException(ErrorCode.CERTIFICATION_NUMBER_MISMATCH);
        }
        System.out.println("인증번호 통과, 인증번호를 지웁니다.");
        redisService.remove("email:" + code.getEmail());
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 회원가입 성공 로직
     */
    @ApiOperation(value = "회원가입", notes = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Member.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    @PostMapping("/members")
    public ResponseEntity<Member> signUp(@Valid @RequestBody MemberPostDto postDto) {
        // 중복 검사
        Member member = memberService.signup(postDto);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }


    /**
     * 회원정보 프로필 수정
     */
    @ApiOperation(value = "프로필 수정", notes = "닉네임과 소개글, 프로필 이미지 변경이 가능함")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Member.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    @PatchMapping("/members/profile")
    public ResponseEntity updateMemberProfile(@ApiIgnore Principal principal,
                                           @Valid @RequestBody MemberPatchProfileDto dto){
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberProfile(memberId, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원정보 비밀번호 수정
     */
    @ApiOperation(value = "비밀번호 수정", notes = "비밀번호 변경, 성공 시 상태코드 200")
    @PatchMapping("/members/password")
    public ResponseEntity updateMemberPassword(@ApiIgnore Principal principal,
                                               @Valid @RequestBody MemberPatchPasswordDto dto){
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberPassword(memberId, dto);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴 처리
     */
    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴 Principal 객체에서 pk 가져와서 service단에서 memberId를 조회해 탈퇴 처리함")
    @DeleteMapping("/members")
    public ResponseEntity updateMemberDelete(@ApiIgnore Principal principal,
                                             HttpServletRequest request){
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberDelete(memberId);

        try {
        // Refresh 토큰을 Redis에서 제거하는 작업
            redisService.remove("refresh:" + hmacAndBase64.crypt(request.getRemoteAddr(), "HmacSHA512") + "_" + principal.getName());
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException|InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }

        // Access Token은 요청을 보낸 Axios API에서 쿠키를 삭제하도록 지시한다.
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원정보 조회
     */
    @ApiOperation(value = "회원정보 조회", notes = "Principal 객체에서 memberId를 조회해서 회원 정보를 가져옴. 성공 시 상태코드 200")
    @GetMapping("/members")
    public ResponseEntity<MemberGetResponse> getMemberOne(@ApiIgnore Principal principal){
        Long memberId = Long.parseLong(principal.getName());
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }



    // Todo : 회원탈퇴 시 회원관련 토큰들 제거해줘야한다.


    /**
     * 로그인
     */
    @ApiOperation(value = "로그인", notes = "로그인, 성공 시 토큰과 상태코드 200")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MemberLoginDto loginDto, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // authenticationToken을 이용해서 Authentication 객체를 생성하려고 authentication 메소드가 실행이 될 때
        // CustomUserDetailsService의 loadUserByUsername 메소드가 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // createToken 메소드를 통해서 JWT Token을 생성한다.
        String jwt = tokenProvider.createToken(authentication);
        String refresh = refreshTokenProvider.createToken(authentication, ipAddress);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
        httpHeaders.add(REFRESH_HEADER, "Bearer " + refresh);

        try {
            // "refresh:암호화된IP_pk"을 key값으로 refreshToken을 Redis에 저장한다.
            redisService.set("refresh:" + hmacAndBase64.crypt(ipAddress, "HmacSHA512")
                    + "_" + authentication.getName(), refresh, refreshTokenValidityInSeconds);
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException|InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }



        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃 시 토큰 제거
     */
    @ApiOperation(value = "로그아웃 시 토큰을 제거하는 기능", notes = "")
    @GetMapping("/removeToken")
    public ResponseEntity removeToken(@ApiIgnore Principal principal, HttpServletRequest request) {

        try {
        // Refresh 토큰을 Redis에서 제거하는 작업
            redisService.remove("refresh:" + hmacAndBase64.crypt(request.getRemoteAddr(), "HmacSHA512") + "_" + principal.getName());
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException|InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }

        // Todo : return 값 어떻게 줄지 생각해야한다.
        return new ResponseEntity(principal.getName()+" 삭제완료", HttpStatus.OK);
    }


}




