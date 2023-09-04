package com.douzone.prosync.member.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchProfileDto {


        @ApiModelProperty(value = "닉네임" ,example = "hee")
        @NotNull @NotBlank
        private String name;

        @ApiModelProperty(value = "회원 소개글",example = "hi")
        @NotNull @NotBlank
        private String intro;


        @ApiModelProperty(value = "파일식별자", example = "1")
        private Long fileId;

        @Setter
        private String profileImage;

}
