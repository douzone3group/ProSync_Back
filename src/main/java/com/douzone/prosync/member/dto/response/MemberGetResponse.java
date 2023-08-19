package com.douzone.prosync.member.dto.response;

import com.douzone.prosync.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGetResponse {


        @ApiModelProperty(value = "멤버 식별자", example = "1")
        private Long memberId;
        @ApiModelProperty(value = "이메일", example = "abcd@naver.com")
        private String email;
    @ApiModelProperty(value = "닉네임", example = "hee")
        private String name;
    @ApiModelProperty(value = "생성일자", example = "2023-01-23")
        private Timestamp createdAt;
    @ApiModelProperty(value = "수정일자", example = "2023-12-12")
        private Timestamp modifiedAt;
    @ApiModelProperty(value = "회원 소개글", example = "hi")
        private String intro;

        // Todo: 파일 image 연결
        @ApiModelProperty(value = "프로필 이미지", example = "default")
        private String profileImage;


    // Todo: 만약 멤버 관리 페이지에서 멤버 리스트들을 조회할 때 simpleResponse를 작성한다.
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SimpleResponse {
//    }

    public static MemberGetResponse of(Member member){
        return MemberGetResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .intro(member.getIntro())
                .modifiedAt(member.getModifiedAt())
                .createdAt(member.getCreatedAt())
                .profileImage(member.getProfileImage())
                .build();
    }
}
