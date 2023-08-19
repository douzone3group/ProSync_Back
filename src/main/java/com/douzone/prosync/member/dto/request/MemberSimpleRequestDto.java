package com.douzone.prosync.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberSimpleRequestDto {
        @NotNull @NotBlank
        private Long memberId;

}
