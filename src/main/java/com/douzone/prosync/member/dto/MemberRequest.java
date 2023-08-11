package com.douzone.prosync.member.dto;

import com.douzone.prosync.member.entity.Member;
import lombok.*;

import java.sql.Timestamp;

public class MemberRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostDto {
        private String email;
        private String password;
        private String nickname;
        private String intro;

        // Todo: 파일 image 연결
        // private String image;


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchDto {

        private Long memberId;
        private String password;
        private String nickname;
        private boolean isDeleted;
        private String intro;

        // Todo: 파일 image 연결
        // private String image;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SimpleDto {

        private Long memberId;
    }
}
