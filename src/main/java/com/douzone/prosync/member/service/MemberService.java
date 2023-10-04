package com.douzone.prosync.member.service;

import com.douzone.prosync.mail.dto.CertificationCodeDto;
import com.douzone.prosync.mail.dto.MailDto;
import com.douzone.prosync.member.dto.request.MemberLoginDto;
import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.dto.request.MemberPostDto;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {

     Member signup(MemberPostDto memberDto);


     MemberGetResponse selectMember(Long memberId);

     void updateMemberProfile(Long memberId, MemberPatchProfileDto dto);

     void updateMemberPassword(Long memberId, MemberPatchPasswordDto dto);


     void updateMemberDelete(Long memberId, HttpServletRequest request);


     void duplicateInspection(String email) ;

     void invalidateInspectionAndSend(MailDto mail);

     void verifyCertificationNumber(CertificationCodeDto code);

     String loginProcess(MemberLoginDto loginDto,HttpHeaders httpHeaders, HttpServletRequest request);



}
