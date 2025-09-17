package com.sys.stm.domains.messenger.dto.request;

import lombok.*;
/** 작성자: 조윤상 */
@ToString
@Getter
@Builder
public class MessageDeleteRequestDto {
    private long chatRoomId;
    private long messageId;
}
