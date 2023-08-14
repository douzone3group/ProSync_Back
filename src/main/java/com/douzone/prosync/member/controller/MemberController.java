package com.douzone.prosync.member.controller;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.MemberRequest;
import com.douzone.prosync.member.dto.MemberResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.mapper.MemberMapper;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.member.service.MemberService;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;

import static com.douzone.prosync.member.dto.MemberRequest.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;
    private final MemberRepository memberRepository;



    /**
     * 회원가입
     */
    @PostMapping("/members")
    public ResponseEntity signUp(@Valid @RequestBody PostDto postDto) {
        // 중복 검사
        memberService.signup(postDto);

        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 회원정보 수정
     */
    @PatchMapping("/members/me")
   public ResponseEntity updateMemberName(Principal principal,
                                      @Valid @RequestBody PatchDto patchDto){
        Long memberId = Long.parseLong(principal.getName());
        memberService.updateMemberProfile(memberId, patchDto);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PatchMapping("/members/me")
    public ResponseEntity updateMemberPassword(Principal principal,
                                               @Valid @RequestBody PatchDto patchDto){
        Long memberId = Long.parseLong(principal.getName());
        PatchDto updateMemberPassword = memberService.updateMemberPassword(memberId, patchDto);
        return new ResponseEntity(updateMemberPassword, HttpStatus.OK);
    }

    @PatchMapping("/members/me")
    public ResponseEntity updateMemberDelete(Principal principal,
                                               @Valid @RequestBody PatchDto patchDto){
        Long memberId = Long.parseLong(principal.getName());
        PatchDto updateMemberDelete = memberService.updateMemberDelete(memberId, patchDto);
        return new ResponseEntity(updateMemberDelete, HttpStatus.OK);
    }
//
//    /**
//     * 회원 삭제
//     */
//    @DeleteMapping("/{memberId}")
//    public ResponseEntity removeMember() {
//        return null;
//    }
//
    /**
     * 회원정보 조회
     */
    @GetMapping("/{memberId}")
    public ResponseEntity getMemberOne(@PathVariable("memberId") Long memberId){
        return new ResponseEntity(memberService.selectMember(memberId), HttpStatus.OK);
    }
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





}
