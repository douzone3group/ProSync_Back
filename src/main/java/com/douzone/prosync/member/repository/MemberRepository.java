package com.douzone.prosync.member.repository;

import com.douzone.prosync.member.dto.MemberRequest;
import com.douzone.prosync.member.dto.MemberRequest.PatchDto;
import com.douzone.prosync.member.dto.MemberResponse;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepository {

    private final MemberMapper memberMapper;

    public Member save(MemberDto member) {
        memberMapper.save(member);
        log.error("member의 id는 {}", member.getMemberId());
        return  memberMapper.findById(member.getMemberId()).orElse(null);
    }

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    public void update(MemberDto memberDto) {
        memberMapper.update(memberDto);
    }

    public Optional<MemberResponse.GetMemberResponse> getMemberOne(Long memberId){
       return memberMapper.getMemberOne(memberId);
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
