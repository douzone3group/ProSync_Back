package com.douzone.prosync.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchProfileDto {


        @NotNull @NotBlank
        private String name;
        @NotNull @NotBlank
        private String intro;

        // Todo: 파일 image 연결
        @NotNull @NotBlank
        private String profileImage;

}
