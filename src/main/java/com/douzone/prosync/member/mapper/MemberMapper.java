package com.douzone.prosync.member.mapper;

import com.douzone.prosync.member.dto.MemberDto;
import com.douzone.prosync.member.dto.request.MemberPatchProfileDto;
import com.douzone.prosync.member.dto.response.ProfileWithAuthorityDto;
import com.douzone.prosync.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {


// Todo: 엔티티 사용하지말고 Dto로 넣기
    void save(MemberDto member);

    // 회원 업데이트 및 삭제여부를 수정할 수 있다.
    void updateProfile(@Param("memberId") Long memberId,@Param("profileDto") MemberPatchProfileDto profileDto,@Param("modifiedAt") LocalDateTime modifiedAt);
    void updatePassword(@Param("memberId")Long memberId,@Param("password") String modifiedPassword,@Param("modifiedAt") LocalDateTime modifiedAt);
    void updateDeleted(@Param("memberId") Long memberId,@Param("modifiedAt") LocalDateTime modifiedAt);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    List<Member> getMemberList(@Param("memberIds")List<Long> memberIds);

    ProfileWithAuthorityDto getMemberProfileWithAuthority(@Param("memberId") Long memberId, @Param("projectId") Long projectId);


    // Todo: 멤버 관리 리스트에서 검색조건을 사용할 때 findAll을 작성한다.
    //List<MemberResponse.GetMemberResponse> findAll(ItemSearchCond itemSearch);


}
