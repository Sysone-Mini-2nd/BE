package com.sys.stm.domains.messenger.dto.response;

import lombok.*;

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
