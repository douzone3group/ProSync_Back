package com.douzone.prosync.member.service;

import com.douzone.prosync.member.dto.MemberResponse;
import com.douzone.prosync.member.dto.MemberResponse.GetMemberResponse;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import com.douzone.prosync.security.exception.NotFoundMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public GetMemberResponse selectMember(Long memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null){
            throw new NotFoundMemberException("회원 정보 없음.");
        }else{
        GetMemberResponse response = GetMemberResponse.builder()
                .email(member.getEmail())
                .intro(member.getIntro())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .memberId(member.getMemberId())
                .profileImage(member.getProfileImage())
                .build();

            return response;
        }
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public void updateMemberProfile(Long memberId, PatchProfileDto dto){
        memberRepository.findById(memberId).orElseThrow(() ->new NotFoundMemberException("회원 정보 없습니다."));
        memberRepository.updateProfile(dto);
    }
    /**
     * 패스워드 수정
     */
    @Transactional
    public void updateMemberPassword(Long memberId,PatchPasswordDto dto){
        memberRepository.findById(memberId).orElseThrow(()->new NotFoundMemberException());
        memberRepository.updatePassword(dto);
    }

    /**
     * 회원 탈퇴 처리
     */
    @Transactional
    public void updateMemberDelete(Long memberId, PatchDeletedDto dto){
        memberRepository.findById(memberId).orElseThrow(()->new NotFoundMemberException());
        memberRepository.updateDeleted(dto);


    }

    /**
     * Email로 Member 중복검사하기
     */
    public boolean duplicateInspection(String email) {

        return !(memberRepository.findByEmail(email).orElse(null)==null);
    }
}