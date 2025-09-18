package com.sys.stm.domains.messenger.dto.response;

import lombok.*;
/** 작성자: 조윤상 */
@ToString
@Getter
@Builder
public class MessageReadResponseDto {
    private long memberId;
    private long messageId;
}
