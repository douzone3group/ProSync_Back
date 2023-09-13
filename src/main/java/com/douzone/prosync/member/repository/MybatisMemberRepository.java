package com.douzone.prosync.member.repository;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MybatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;


    // TODO: 에포크 시간으로 나옴, 타임스탬프 형식으로 바꿔줘야한다!
    public Member save(MemberDto member) {
        memberMapper.save(member);
        log.debug("member의 id는 {}", member.getMemberId());
        return  memberMapper.findById(member.getMemberId()).orElse(null);
    }

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    public void updateProfile(Long memberId, MemberPatchProfileDto dto) { memberMapper.updateProfile(memberId, dto,LocalDateTime.now());}
    public void updatePassword(Long memberId,String modifiedPassword) {
        memberMapper.updatePassword(memberId,modifiedPassword,LocalDateTime.now());
    }
    public void updateDeleted(Long memberId) {
        memberMapper.updateDeleted(memberId,LocalDateTime.now());
    }
    public Optional<Member> findById(Long memberId) {
        return memberMapper.findById(memberId);
    };

    public Optional<Member>  findByEmail(String email) {
        return memberMapper.findByEmail(email);

    };

    public List<Member> getMemberList(List<Long> memberIds) {
        return memberMapper.getMemberList(memberIds);
    }


    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    // List<Item> findAll(ItemSearchCond itemSearch);

}
