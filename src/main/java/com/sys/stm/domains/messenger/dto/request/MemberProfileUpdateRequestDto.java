package com.sys.stm.domains.messenger.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/** 작성자: 조윤상 */
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
