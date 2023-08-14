package com.douzone.prosync.member.dto;

import com.douzone.prosync.member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class MemberRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class PostDto {
        private String email;
        private String password;
        private String name;
        private String intro;

        // Todo: 파일 image 연결
        // private String image;


    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchDto {
        @NotNull
        private Long memberId;
        @NotNull
        private String name;
        @NotNull // 나중 수정
        private String intro;
        @NotNull
        private String password;
        @NotNull
        private boolean isDeleted;

        // Todo: 파일 image 연결
        // private String image;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchDtoPassword {
        @NotNull
        private Long memberId;
        @NotNull
        private String password;


        // Todo: 파일 image 연결
        // private String image;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchDtoDelete {
        @NotNull
        private Long memberId;

        @NotNull
        private boolean isDeleted;


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
