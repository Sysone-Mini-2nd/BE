package com.sys.stm.domains.messenger.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
/** 작성자: 조윤상 */
@ToString
@Getter
@Builder
public class MessageReadRequestDto {
    private long memberId;
    private long messageId;
    private long chatRoomId;
}
