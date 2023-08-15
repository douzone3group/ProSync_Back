package com.douzone.prosync.member.service;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

import static com.douzone.prosync.member.dto.MemberRequest.*;

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


        // 중복검사
        if (duplicateInspection(memberDto.getEmail())) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // Todo : 기본 이미지 넣어주기
        MemberDto member = MemberDto.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name("")
                .intro("")
                .createdAt(Timestamp.from(Instant.now()))
                .modifiedAt(Timestamp.from(Instant.now()))
                .profileImage("")
                .isDeleted(false).build();



        return memberRepository.save(member);
    }

    /**
     * Member pk로 조회하기
     */
    @Transactional(readOnly = true)
    public Member getMemberById(Long memberId) {
        return (memberRepository.findById(memberId).orElse(null));
    }


    /**
     * Email로 Member 중복검사하기
     */
    public boolean duplicateInspection(String email) {

        return !(memberRepository.findByEmail(email).orElse(null)==null);
    }

}