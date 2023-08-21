package com.douzone.prosync.member.service;

import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.dto.request.MemberPostDto;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;

public interface MemberService {

     Member signup(MemberPostDto memberDto);


     MemberGetResponse selectMember(Long memberId);

     void updateMemberProfile(Long memberId, MemberPatchProfileDto dto);

     void updateMemberPassword(Long memberId, MemberPatchPasswordDto dto);


     void updateMemberDelete(Long memberId);


     boolean duplicateInspection(String email) ;
}
