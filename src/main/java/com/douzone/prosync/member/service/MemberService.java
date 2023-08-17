package com.douzone.prosync.member.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchDeletedDto;
import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.dto.request.MemberPostDto;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
    public Member signup(MemberPostDto memberDto) {
        // 중복검사
        if (duplicateInspection(memberDto.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATED_USER_ID);
        }
        // Todo : 기본 이미지 넣어주기
        MemberDto member = memberDto.of();
        return memberRepository.save(member);
    }


    /**
     * Member pk로 조회하기
     */
    @Transactional(readOnly = true)
    public MemberGetResponse selectMember(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() ->new ApplicationException(ErrorCode.USER_NOT_FOUND));

            return MemberGetResponse.of(member);

    }

    /**
     * 프로필 수정
     */
    public void updateMemberProfile(Long memberId, MemberPatchProfileDto dto){
        memberRepository.findById(memberId).orElseThrow(() ->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        memberRepository.updateProfile(dto);
    }
    /**
     * 패스워드 수정
     */
    public void updateMemberPassword(Long memberId, MemberPatchPasswordDto dto){
        memberRepository.findById(memberId).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        memberRepository.updatePassword(dto);
    }

    /**
     * 회원 탈퇴 처리
     */
    public void updateMemberDelete(Long memberId, MemberPatchDeletedDto dto){
        memberRepository.findById(memberId).orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        memberRepository.updateDeleted(dto);


    }

    /**
     * Email로 Member 중복검사하기
     */
    public boolean duplicateInspection(String email) {
        return !(memberRepository.findByEmail(email).orElse(null)==null);
    }
}