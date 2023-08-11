package com.douzone.prosync.security.auth;

import com.douzone.prosync.member.dto.MemberResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.douzone.prosync.member.dto.MemberRequest.*;
import static com.douzone.prosync.member.dto.MemberResponse.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 로직
     */
    @Transactional
    public Member signup(PostDto memberDto) {
        if (memberRepository.findByEmail(memberDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // Todo : 이미지 넣어주기
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .isDeleted(memberDto.isDeleted())
                .intro(memberDto.getIntro())
                .build();

        return memberRepository.save(member);
    }

    /**
     * Member pk로 조회하기
     */
    @Transactional(readOnly = true)
    public Member getMemberById(Long memberId) {
        return (memberRepository.findById(memberId).orElse(null));
    }



}