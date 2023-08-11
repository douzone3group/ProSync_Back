package com.douzone.prosync.member.mapper;

import com.douzone.prosync.member.dto.MemberRequest;
import com.douzone.prosync.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {


    void save(Member member);

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    void update(MemberRequest.PatchDto updateParam);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);


    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    // List<Item> findAll(ItemSearchCond itemSearch);


}
