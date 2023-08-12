package com.douzone.prosync.member.controller;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.sql.Timestamp;
import java.time.Instant;

import static com.douzone.prosync.member.dto.MemberRequest.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity signUp(@Valid @RequestBody PostDto postDto) {

        Member member =memberService.signup(postDto);

        return new ResponseEntity(member,HttpStatus.OK);
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
//     * 회원 삭제
//     */
//    @DeleteMapping("/{memberId}")
//    public ResponseEntity removeMember() {
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
//
//    /**
//     * 로그인
//     */
//    @PostMapping("/login")
//    public ResponseEntity login(@Valid @ModelAttribute PostDto postDto, HttpServletResponse response) {
//
//    }
//
//    /**
//     * 로그아웃
//     */
//    @GetMapping("/logout")
//    public ResponseEntity logout() {
//
//    }
//
//
//




}
