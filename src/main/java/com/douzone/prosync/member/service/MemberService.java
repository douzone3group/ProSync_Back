package com.douzone.prosync.member.service;

import com.douzone.prosync.member.dto.MemberResponse;
import com.douzone.prosync.member.dto.MemberResponse.GetMemberResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import com.douzone.prosync.security.exception.NotFoundMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static com.douzone.prosync.member.dto.MemberRequest.*;

@Service
@Slf4j
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
        log.info(memberDto.toString());
        // Todo : 이미지 넣어주기
        MemberDto member = MemberDto.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .createdAt(Timestamp.from(Instant.now()))
                .modifiedAt(Timestamp.from(Instant.now()))
                .name(memberDto.getName())
                .isDeleted(false)
                .intro(memberDto.getIntro())
                .profileImage("sadf")
                .build();

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
                .build();

            return response;
        }
    }

    /**
     * Member pk로 조회하기
     */
    @Transactional
    public void updateMemberProfile(Long memberId, PatchDto dto){
        Member member = memberRepository.findById(memberId).orElseThrow(() ->new NotFoundMemberException("회원 정보 없습니다."));
        System.out.println(member.getPassword());

        if(member != null){
            MemberDto memberDto = MemberDto.builder()
                    .name(dto.getName())
                    .intro(dto.getIntro())
                    .password(member.getPassword())
                    .memberId(member.getMemberId())
                    .isDeleted(member.isDeleted())
                    .modifiedAt(Timestamp.from(Instant.now()))
                    .createdAt(member.getCreatedAt())
                    .build();

            memberRepository.update(memberDto);

        } else{

        }


    }

    @Transactional
    public PatchDto updateMemberPassword(Long memberId,PatchDto dto){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member != null){
            PatchDto patchDto = PatchDto.builder()
                    .name(member.getName())
                    .intro(member.getIntro())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .memberId(member.getMemberId())
                    .isDeleted(member.isDeleted())
                    .build();

            member.setPassword(passwordEncoder.encode(dto.getPassword()));

            return patchDto;
        }else {
            return null;
        }
    }

    /**
     * @param memberId z
     * @param dto
     * @return
     */
    @Transactional
    public PatchDto updateMemberDelete(Long memberId, PatchDto dto){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member != null){
            PatchDto patchDto = PatchDto.builder()
                    .name(member.getName())
                    .intro(member.getIntro())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .memberId(member.getMemberId())
                    .isDeleted(false)
                    .build();

            return patchDto;
        }else {
            return null;
        }
    }
}