package com.sys.stm.domains.messenger.dto.response;

import lombok.*;
/** 작성자: 조윤상 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileResponseDto {
    private long id;
    private String name;
    private String position;
    private String email;
    private String picUrl;
}
