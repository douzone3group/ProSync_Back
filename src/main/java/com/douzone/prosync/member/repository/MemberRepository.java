package com.douzone.prosync.member.repository;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository {

     Member save(MemberDto member);

     void updateProfile(Long memberId, MemberPatchProfileDto dto);

     void updatePassword(Long memberId,String modifiedPassword);

     void updateDeleted(Long memberId);

     Optional<Member> findById(Long memberId);

     Optional<Member> findByEmail(String email);
}
