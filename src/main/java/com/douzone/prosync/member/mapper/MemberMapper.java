package com.douzone.prosync.member.mapper;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.MemberRequest;
import com.douzone.prosync.member.dto.MemberRequest.PatchDeletedDto;
import com.douzone.prosync.member.dto.MemberRequest.PatchPasswordDto;
import com.douzone.prosync.member.dto.MemberRequest.PatchProfileDto;
import com.douzone.prosync.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

import static com.douzone.prosync.member.dto.MemberResponse.*;

@Mapper
public interface MemberMapper {


// Todo: 엔티티 사용하지말고 Dto로 넣기
    void save(MemberDto member);

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    void updateProfile(PatchProfileDto profileDto);
    void updatePassword(PatchPasswordDto passwordDto);
    void updateDeleted(PatchDeletedDto DeletedDto);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);




    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    //List<MemberResponse.GetMemberResponse> findAll(ItemSearchCond itemSearch);


}
