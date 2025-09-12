package com.sys.stm.domains.messenger.dto.request;

import lombok.*;

@ToString
@Getter
@Builder
public class MessageDeleteRequestDto {
    private long chatRoomId;
    private long messageId;
}
