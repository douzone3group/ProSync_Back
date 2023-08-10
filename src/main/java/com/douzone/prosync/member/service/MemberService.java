package com.douzone.prosync.security.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findByUsername(memberDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }


        Member user = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .activated(true)
                .build();

        return MemberDto.from(memberRepository.save(user));
    }

    // username을 기준으로 정보를 가져온다.
    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(memberRepository.findByUsername(username).orElse(null));
    }



}