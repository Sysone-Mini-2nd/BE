package com.sys.stm.domains.messenger.dto.request;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Builder
public class ChatRoomInvitationRequestDto {
    private long chatRoomId;
    private List<Long> memberIdList;
}
