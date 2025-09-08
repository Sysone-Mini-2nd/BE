package com.sys.stm.domains.messenger.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileUpdateRequestDto {
    private String name;
    private String position;
    private String email;
    private String picUrl;
}
