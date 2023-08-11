package com.douzone.prosync.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

public class MemberResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetMemberResponse {

        private Long memberId;
        private String email;
        private String nickname;
        private Timestamp createdAt;
        private Timestamp modifiedAt;
        private String intro;

        // Todo: 파일 image 연결
        // private String image;
    }





    // Todo: 만약 멤버 관리 페이지에서 멤버 리스트들을 조회할 때 simpleResponse를 작성한다.
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SimpleResponse {
//
//
//    }

}
