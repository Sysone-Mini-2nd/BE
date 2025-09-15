package com.sys.stm.domains.messenger.dto.response;

import lombok.*;

@ToString
@Getter
@Builder
public class MessageReadResponseDto {
    private long memberId;
    private long messageId;
}
