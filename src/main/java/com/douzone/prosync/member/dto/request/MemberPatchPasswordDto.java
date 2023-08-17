package com.douzone.prosync.member.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPatchPasswordDto {

    @NotNull @NotBlank
    private String password;
}
