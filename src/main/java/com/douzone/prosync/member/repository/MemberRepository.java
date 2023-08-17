package com.douzone.prosync.member.repository;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchDeletedDto;
import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper memberMapper;


    public Member save(MemberDto member) {
        memberMapper.save(member);
        log.error("member의 id는 {}", member.getMemberId());
        return  memberMapper.findById(member.getMemberId()).orElse(null);
    }

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    public void updateProfile(MemberPatchProfileDto dto) { memberMapper.updateProfile(dto);}
    public void updatePassword(MemberPatchPasswordDto dto) {
        memberMapper.updatePassword(dto);
    }
    public void updateDeleted(MemberPatchDeletedDto dto) {
        memberMapper.updateDeleted(dto);
    }
    public Optional<Member> findById(Long memberId) {
        return memberMapper.findById(memberId);
    };

    public Optional<Member>  findByEmail(String email) {
        return memberMapper.findByEmail(email);

    };


    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    // List<Item> findAll(ItemSearchCond itemSearch);

}
