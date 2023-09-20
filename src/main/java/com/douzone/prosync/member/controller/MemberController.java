package com.douzone.prosync.member.controller;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.mail.dto.CertificationCodeDto;
import com.douzone.prosync.mail.dto.MailDto;
import com.douzone.prosync.member.dto.MemberEmailDto;
import com.douzone.prosync.member.dto.request.*;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.dto.response.MemberSimpleResponseDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.redis.TokenStorageService;
import com.douzone.prosync.security.jwt.HmacAndBase64;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "member", description = "회원 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;


    private final TokenStorageService redisService;

    private final HmacAndBase64 hmacAndBase64;




    /**
     * 사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장하는 로직
     */
    @PostMapping("/send_verification")
    @Operation(summary = "이메일 인증", description = "사용자의 이메일을 받아 인증번호를 전송하고 Redis에 인증번호를 저장, 성공 시 상태코드 200", tags = "member")
    public ResponseEntity mailInvalidateAndSend(@Valid @RequestBody MailDto mail) {
        memberService.invalidateInspectionAndSend(mail);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 사용자에게 이메일과 인증번호를 받아 Redis에 있는지 확인하는 로직
     */
    @PostMapping("/verify_code")
    @Operation(summary = "이메일 인증", description = "사용자에게 이메일과 인증번호를 받아 Redis에 존재하는가 확인, 성공 시 상태코드 200", tags = "member")
    public ResponseEntity verifyCertificationNumber(@Valid @RequestBody CertificationCodeDto code) {
        memberService.verifyCertificationNumber(code);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 회원가입 성공 로직
     */

    @PostMapping("/members")
    @Operation(summary = "회원가입", description = "회원가입",tags = "member")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Member.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    @Transactional
    public ResponseEntity<MemberSimpleResponseDto> signUp(@Valid @RequestBody MemberPostDto postDto) {
        // 중복 검사
        Member member = memberService.signup(postDto);

        return new ResponseEntity<>(new MemberSimpleResponseDto(member.getMemberId()), HttpStatus.OK);
    }


    /**
     * 회원정보 프로필 수정
     */

    @PatchMapping("/members/profile")
    @Operation(summary = "프로필 수정", description = "닉네임과 소개글, 프로필 이미지 변경이 가능함",tags = "member" )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success", response = Member.class),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    @Transactional
    public ResponseEntity<MemberSimpleResponseDto> updateMemberProfile(@Parameter(hidden = true) @ApiIgnore Principal principal,
                                                                       @Valid @RequestBody MemberPatchProfileDto dto) {
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberProfile(memberId, dto);
        return new ResponseEntity(new MemberSimpleResponseDto(memberId), HttpStatus.OK);
    }

    /**
     * 회원정보 비밀번호 수정
     */
    @PatchMapping("/members/password")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 변경, 성공 시 상태코드 200",tags = "member")
    @Transactional
    public ResponseEntity updateMemberPassword(@Parameter(hidden = true) @ApiIgnore Principal principal,
                                               @Valid @RequestBody MemberPatchPasswordDto dto) {
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberPassword(memberId, dto);
        return new ResponseEntity(new MemberSimpleResponseDto(memberId), HttpStatus.OK);
    }



    /**
     * 회원 탈퇴 처리
     */
    @DeleteMapping("/members")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 Principal 객체에서 pk 가져와서 service단에서 memberId를 조회해 탈퇴 처리함", tags = "member")
    @Transactional
    public ResponseEntity updateMemberDelete(@Parameter(hidden = true) @ApiIgnore Principal principal,
                                             HttpServletRequest request) {



        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberDelete(memberId,request);


        // TODO: Access Token은 요청을 보낸 Axios API에서 쿠키를 삭제하도록 지시한다.
        return new ResponseEntity(new MemberSimpleResponseDto(memberId),HttpStatus.OK);
    }

    /**
     * 회원정보 조회
     */
    @GetMapping("/members")
    @Operation(summary = "회원정보 조회", description = "Principal 객체에서 memberId를 조회해서 회원 정보를 가져옴. 성공 시 상태코드 200", tags = "member")
    @Transactional(readOnly = true)
    public ResponseEntity<MemberGetResponse> getMemberOne(@Parameter(hidden = true) @ApiIgnore Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }



    /**
     * 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인, 성공 시 토큰과 상태코드 200", tags = "member")
    @Transactional
    public ResponseEntity login(@Valid @RequestBody MemberLoginDto loginDto, HttpServletRequest request) {

        HttpHeaders httpHeaders = new HttpHeaders();
        Long memberId = Long.parseLong(memberService.loginProcess(loginDto, httpHeaders, request));
        return new ResponseEntity<>(new MemberSimpleResponseDto(memberId), httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃 시 토큰 제거
     */
    @GetMapping("/removeToken")
    @Operation(summary = "로그아웃", description = "로그아웃 시 JWT 토큰을 제거 ", tags = "member")
    public ResponseEntity removeToken(@Parameter(hidden = true) @ApiIgnore Principal principal, HttpServletRequest request) {

        try {
            // Refresh 토큰을 Redis에서 제거하는 작업
            redisService.removeRefreshToken("refresh:" + hmacAndBase64.crypt(request.getRemoteAddr(), "HmacSHA512") + "_" + principal.getName());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ApplicationException(ErrorCode.CRYPT_ERROR);
        }

        // Todo : return 값 어떻게 줄지 생각해야한다.
        return new ResponseEntity(principal.getName() + " 삭제완료", HttpStatus.OK);
    }


    // 중복확인
    @PostMapping("/idcheck")
    public ResponseEntity idCheck(@RequestBody MemberEmailDto dto) {
        String email = dto.getEmail();
        System.out.println(email);
        boolean res = memberService.duplicateInspection(email);
        return new ResponseEntity(res, HttpStatus.OK);
    }

}




