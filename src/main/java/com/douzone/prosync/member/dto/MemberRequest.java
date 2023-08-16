package com.douzone.prosync.member.dto;

import lombok.*;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class MemberRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginDto {

        @Email
        private String email;

        @NotBlank @NotNull
        private String password;

    }



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class PostDto {

        @Email
        private String email;
        @NotBlank @NotNull
        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchProfileDto {

        private String name;
        private String intro;

        // Todo: 파일 image 연결
         private String profileImage;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchPasswordDto {

        private String password;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchDeletedDto {

        private boolean isDeleted;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class SimpleDto {

        private Long memberId;
    }
}
