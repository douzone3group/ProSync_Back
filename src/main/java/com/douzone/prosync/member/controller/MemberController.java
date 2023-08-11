package com.douzone.prosync.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.douzone.prosync.member.dto.MemberRequest.*;

@RestController
@RequestMapping("/members")
public class MemberController {


    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity signUp() {

    }


    /**
     * 회원정보 수정
     */
    @PatchMapping("/{memberId}")
    public ResponseEntity modifyMember() {

    }

    /**
     * 회원 삭제
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity removeMember() {

    }

    /**
     * 회원정보 조회
     */
    @PatchMapping("/{memberId}")
    public ResponseEntity getMemberInfo() {

    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity login(@Valid @ModelAttribute PostDto postDto, HttpServletResponse response) {
        
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity logout() {

    }







}
