package com.douzone.prosync.member.dto.response;

import com.douzone.prosync.member.entity.Member;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberGetResponse {



        private Long memberId;
        private String email;
        private String name;
        private Timestamp createdAt;
        private Timestamp modifiedAt;
        private String intro;

        // Todo: 파일 image 연결
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
