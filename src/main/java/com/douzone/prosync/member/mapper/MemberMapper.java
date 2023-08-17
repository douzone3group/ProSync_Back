package com.douzone.prosync.member.mapper;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchDeletedDto;
import com.douzone.prosync.member.dto.request.MemberPatchPasswordDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {


// Todo: 엔티티 사용하지말고 Dto로 넣기
    void save(MemberDto member);

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    void updateProfile(MemberPatchProfileDto profileDto);
    void updatePassword(MemberPatchPasswordDto passwordDto);
    void updateDeleted(MemberPatchDeletedDto DeletedDto);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);




    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    //List<MemberResponse.GetMemberResponse> findAll(ItemSearchCond itemSearch);


}
