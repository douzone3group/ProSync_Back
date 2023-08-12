package com.douzone.prosync.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostDto {

        @Email
        private String email;

        @NotBlank @NotNull
        private String password;

        @NotBlank @NotNull
        private String name;

        @NotBlank @NotNull
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
